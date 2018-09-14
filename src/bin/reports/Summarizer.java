package bin.reports;

import bin.sheets.SheetsHelper;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;

public class Summarizer {
    static void SummarizeReport(Report report, SheetsHelper helper, int resultsSize) throws GeneralSecurityException, IOException {
        // get Sheet ready for summary
        String spreadsheetId = "13tEH52aFqUilVrFrOBfXjtaDD3nLL5a3PpHUQgWgBF8";
        helper.GetTodaySheetForSummary(spreadsheetId);

        // find the next line to write to
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("d/MM/yyyy"));
        int firstEmptyRowSummary = helper.GetFirstEmptyRow(spreadsheetId, today, 1);
        if (firstEmptyRowSummary == 1) {
            // if this is a new sheet, print headers
            helper.WriteRange(
                    Arrays.asList(
                            Collections.singletonList("Overzicht van de wekelijkse rapporten"),
                            Collections.singletonList("De verwachtingswaarde is de waarde van het vorige rapport of 0. Dit wordt ingesteld in het rapport"),
                            Arrays.asList("Verwachtingswaarde", "Huidig aantal", "Verwachting", "Rapportnaam")),
                    spreadsheetId,
                    today + "!A1");
            firstEmptyRowSummary += 3;
        }

        String verwacht = "";
        String vorigeWaarde = "0";

        // compare to a previous datapoint if needed
        if (report.getHistorycheck()) {
            // get previous result
            int firstEmptyRowPrevious = helper.GetFirstEmptyRow(report.getSpreadsheetId(), "Historiek", 4);
            vorigeWaarde = (helper.ReadRange(report.getSpreadsheetId(), "Historiek!B" + (firstEmptyRowPrevious - 2))).get(0).get(0).toString();
            if (!vorigeWaarde.isEmpty()) {
                int previous =0;
                if (tryParseInt(vorigeWaarde))
                     previous = Integer.parseInt(vorigeWaarde);
                if (previous >= resultsSize == report.getComparehigher())
                    verwacht = "Verwacht";
            }
            if (verwacht.equals(""))
                verwacht = "Niet verwacht";

            if (report.getComparetozero())
                verwacht = resultsSize > 0 ? "Niet verwacht" : "Verwacht";
        }

        // write results
        helper.WriteRange(
                Collections.singletonList(Arrays.asList(
                        vorigeWaarde, resultsSize, verwacht, report.getName())),
                spreadsheetId,
                today + "!A" + firstEmptyRowSummary);
    }

    private static boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
