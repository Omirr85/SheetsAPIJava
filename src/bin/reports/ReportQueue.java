package bin.reports;

import java.sql.SQLException;
import java.util.ArrayList;

public class ReportQueue {
    private final ArrayList<Report> reportList;
    private int iterations;

    public ReportQueue() {
        reportList = ReportFactory.GenerateFromXML();
        iterations = 0;
    }

    public void Start() {
        while (iterations < 5 && reportList.size() > 0) {
            iterations++;

            for (Report report : reportList) {
                DoReport(report);
            }
            reportList.removeIf(x -> x.isDone());
        }
    }

    private void DoReport(Report report) {
        System.out.println("Starting " + report.getType() + " report " + report.getName());

        switch(report.getType()) {
            case ("default") :
                try {
                    DefaultReport.DoReport(report);
                } catch (SQLException e) {

                    e.printStackTrace();
                }
        }

        System.out.println("Finished report " + report.getName());
        report.setDone(true);
    }
}
