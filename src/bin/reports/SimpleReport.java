package bin.reports;

import bin.sheets.SheetsHelper;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SimpleReport extends BaseReport {
    public static void DoReport(Report report, SheetsHelper helper) throws SQLException, GeneralSecurityException, IOException {
        // fetech the results of the query
        List<List<Object>> results = GetQueryResult(report.getSql());

        // create a sheet using today's date as name. delete sheets to not exceed the maxSheets limit
        helper.CreateTodaySheet(report.getSpreadsheetId(), report.getMaxSheets());

        // limit the sheet to only have the exact number of columns needed
        if (results != null)
            helper.DeleteAllButXColumns(report.getSpreadsheetId(), LocalDate.now().format(DateTimeFormatter.ofPattern("d/MM/yyyy")), results.get(0).size());

        // write the results to the sheet
        helper.WriteRange(results, report.getSpreadsheetId(), "A3", true);

        // keep track of all results in the sheet named "Historiek"
        int firstEmptyRow = helper.GetFirstEmptyRow(report.getSpreadsheetId(), "Historiek", 4);
        helper.WriteRange(
                Collections.singletonList(Arrays.asList(
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),String.valueOf(results.size()))),
                report.getSpreadsheetId(),
                "Historiek!A" + firstEmptyRow);

        // always end with the summary report
        Summarizer.SummarizeReport(report, helper, results.size());
    }

}
