package bin.reports;

import java.sql.SQLException;

public class DefaultReport extends BaseReport {
    public static void DoReport(Report report) throws SQLException {
        GetQueryResult(report.getSql());
    }


}
