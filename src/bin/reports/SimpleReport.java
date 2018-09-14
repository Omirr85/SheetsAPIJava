package bin.reports;

import bin.sheets.SheetsHelper;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SimpleReport extends BaseReport {
    public static void DoReport(Report report, SheetsHelper helper) throws SQLException, GeneralSecurityException, IOException {

        List<List<Object>> results = GetQueryResult(report.getSql());

        helper.CreateTodaySheet(report.getSpreadsheetId(), report.getMaxSheets());
        helper.WriteRange(results, report.getSpreadsheetId(), "A3", true);

        int firstEmptyRow = helper.GetFirstEmptyRow(report.getSpreadsheetId(), "Historiek", 4);
        helper.WriteRange(
                Collections.singletonList(Arrays.asList(
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),String.valueOf(results.size()))),
                report.getSpreadsheetId(),
                "Historiek!A" + firstEmptyRow);

        // always end with summary report
        Summarizer.SummarizeReport(report, helper, results.size());
    }

}
