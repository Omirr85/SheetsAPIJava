package bin.sheets;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static java.util.stream.Collectors.*;


public class SheetsHelper {
    public static final String APPLICATION_NAME = "Rapportservice";
    public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private Credential credential;

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved credentials/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final String CREDENTIALS_FILE_PATH = "../../resources/credentials.json";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        if (credential != null)
            return credential;

        // Load client secrets.
        InputStream in = bin.sheets.SheetsHelper.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        return credential;
    }

    public void GetTodaySheetForSummary(String spreadsheetId) throws GeneralSecurityException, IOException, DateTimeParseException {
        // check if today sheet exists
        Sheets service = getService();
        String todaySheetName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("d/MM/yyyy"));

        List<Sheet> list = service.spreadsheets().get(spreadsheetId).execute().getSheets();
        if (list.stream().filter(x -> x.getProperties().getTitle().equals(todaySheetName)).count() == 1)
            return;

        // if not, get the list of all sheets to delete and create the today sheet first (not possible to have 0 sheets)
        List<Sheet> listToDelete = list.stream().filter(x -> !x.getProperties().getTitle().equals(todaySheetName)).collect(toList());
        CreateSheetOrReplace(spreadsheetId, todaySheetName);
        for (Sheet sheet : listToDelete) {
            DeleteSheetByName(spreadsheetId, sheet.getProperties().getTitle());
        }

    }
    public void CreateTodaySheet(String spreadsheetId, int maxSheets) throws GeneralSecurityException, IOException, DateTimeParseException {
        Sheets service = getService();

        List<Sheet> list = service.spreadsheets().get(spreadsheetId).execute().getSheets();
        List<String> dateStrings = list.stream().filter(s -> !s.getProperties().getTitle().equals("Historiek")).map(s -> s.getProperties().getTitle()).collect(toList());

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        List<LocalDate> dates = dateStrings.stream().map(x -> LocalDate.parse(x, dateFormatter)).sorted().collect(toList());
        dates.add(LocalDate.now());
        dates = dates.stream().distinct().collect(toList());

        while (dates.size() > maxSheets)
        {
            LocalDate date = dates.get(0);
            try {
                DeleteSheetByName(spreadsheetId, date.format(dateFormatter));
            } catch (Exception e){
                System.out.println("Could not delete the oldest tab" );
                e.printStackTrace();
            }
            dates.remove(date);
        }

        String todaySheetName = LocalDate.now().format(dateFormatter);
        CreateSheetOrReplace(spreadsheetId, todaySheetName);

        List<List<Object>> table = Collections.singletonList(Collections.singletonList(
            ("Rapport gemaakt op " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))));
        WriteRange(table, spreadsheetId, todaySheetName + "!A1");
    }

    private void CreateSheetOrReplace(String spreadsheetId, String sheetName) throws GeneralSecurityException, IOException {
        DeleteSheetByName(spreadsheetId, sheetName);

        Sheets service = getService();
        AddSheetRequest addSheetRequest = new AddSheetRequest();
        addSheetRequest.setProperties(new SheetProperties().setTitle(sheetName));

        BatchUpdateSpreadsheetRequest request = new BatchUpdateSpreadsheetRequest();
        request.setRequests(Collections.singletonList(new Request().setAddSheet(addSheetRequest)));

        service.spreadsheets().batchUpdate(spreadsheetId,request).execute();
    }

    private void DeleteSheetByName(String spreadsheetId, String sheetName) throws GeneralSecurityException, IOException {
        Sheets service = getService();

        List<Sheet> list = service.spreadsheets().get(spreadsheetId).execute().getSheets();
        Optional<Sheet> sheet = list.stream().filter(s -> s.getProperties().getTitle().equals(sheetName)).findFirst();
        if (sheet.isPresent()) {
            int id = sheet.get().getProperties().getSheetId();
            DeleteSheet(spreadsheetId, id);
        }
    }

    private void DeleteSheet(String spreadsheetId, int sheetId) throws GeneralSecurityException, IOException {
        Sheets service = getService();
        DeleteSheetRequest deleteSheetRequest = new DeleteSheetRequest();

        deleteSheetRequest.setSheetId(sheetId);

        BatchUpdateSpreadsheetRequest request = new BatchUpdateSpreadsheetRequest();
        request.setRequests(Collections.singletonList(new Request().setDeleteSheet(deleteSheetRequest)));

        service.spreadsheets().batchUpdate(spreadsheetId,request).execute();
    }

    private Sheets getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public void WriteRange(List<List<Object>> table, String spreadsheetId, String cellrange, boolean writeTodaySheet) throws GeneralSecurityException, IOException {
        WriteRange(table, spreadsheetId, LocalDate.now().format(DateTimeFormatter.ofPattern("d/MM/yyyy")) + "!" + cellrange);
    }

    public void WriteRange(List<List<Object>> table, String spreadsheetId, String writeRange) throws GeneralSecurityException, IOException {
        Sheets service = getService();
        ValueRange vr = new ValueRange().setValues(table).setMajorDimension("ROWS");
        service.spreadsheets().values()
                .update(spreadsheetId, writeRange, vr)
                .setValueInputOption("RAW") //.setValueInputOption(raw ? "RAW" : "USER_ENTERED")
                .execute();
    }

    public List<List<Object>> ReadRange(String spreadsheetId, String readRange) throws GeneralSecurityException, IOException {
        Sheets service = getService();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, readRange)
                .execute();
        List<List<Object>> values = response.getValues();
        return values;
    }

    public int GetFirstEmptyRow (String spreadsheetId, String sheet, int startRow) throws GeneralSecurityException, IOException {
        int maxRows = 200;
        int countRow = 0;

        List<List<Object>> table = ReadRange(spreadsheetId, sheet + "!A" + startRow + ":A" + (maxRows+startRow-1));
        if (table == null)
            return startRow;
        while(table.size() == maxRows) {
            countRow++;
            table = ReadRange(spreadsheetId, sheet + "!A" + (startRow + countRow * maxRows) + ":A" + (maxRows - 1 + startRow + countRow * maxRows));
        }
        for (List row : table) {
            if (row.isEmpty()) {
                return table.indexOf(row) + countRow * maxRows + startRow;
            }
        }

        return table.size() + countRow * maxRows + startRow;
    }
}

