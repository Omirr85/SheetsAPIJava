package bin.reports;

import bin.sheets.SheetsHelper;
import org.mortbay.util.SingletonList;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReportQueue {
    private ArrayList<Report> reportList;
    private int iterations;

    public ReportQueue() {
        reportList = ReportFactory.GenerateFromXML();
        iterations = 0;

        // enable testing
        Report r = reportList.get(0);
        reportList = new ArrayList<Report>(){{add(r);}};
    }

    public void Start(SheetsHelper helper) {
        while (iterations < 5 && reportList.size() > 0) {
            iterations++;

            for (Report report : reportList) {
                DoReport(report, helper);
            }
            reportList.removeIf(x -> x.isDone());
        }
    }

    private void DoReport(Report report, SheetsHelper helper) {
        System.out.println("Starting " + report.getType() + " report " + report.getName());

        switch(report.getType()) {
            case ("default") :
                try {
                    SimpleReport.DoReport(report, helper);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        System.out.println("Finished report " + report.getName());
        report.setDone(true);
    }
}
