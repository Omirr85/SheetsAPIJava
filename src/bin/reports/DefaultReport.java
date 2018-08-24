package bin.reports;

import bin.sheets.SheetsHelper;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.List;

public class DefaultReport extends BaseReport {
    public static void DoReport(Report report, SheetsHelper helper) throws SQLException, GeneralSecurityException, IOException {
        List<List<Object>> results = GetQueryResult(report.getSql());

        //Rapport gemaakt op 26/07/2018 10:56:50 op Productie
        helper.WriteRange(results, report.getSheet(), "Test!A3");
        helper.CreateSheet(results, report.getSheet(), "24/08/2018");
    }
}
