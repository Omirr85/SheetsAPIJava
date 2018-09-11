package bin.reports;

import bin.sheets.SheetsHelper;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.List;

public class SimpleReport extends BaseReport {
    public static void DoReport(Report report, SheetsHelper helper) throws SQLException, GeneralSecurityException, IOException {
        List<List<Object>> results = GetQueryResult(report.getSql());

        helper.CreateTodaySheet(report.getSpreadsheetId(), report.getMaxSheets());

        // modify WriteRange to write to todayssheet if needed
        helper.WriteRange(results, report.getSpreadsheetId(), "11/09/2018!A3");
    }
}
