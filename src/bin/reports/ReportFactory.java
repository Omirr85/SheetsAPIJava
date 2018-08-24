package bin.reports;

import bin.xml.XMLNode;
import bin.xml.XMLParser;

import java.util.ArrayList;

public class ReportFactory {
    public static final String reportsPath = "/resources/reports.xml";

    public static ArrayList<Report> GenerateFromXML()
    {
        XMLNode reports = XMLParser.Parse(reportsPath);
        ArrayList<Report> list = new ArrayList<Report>();

        for (int i = 0; i < reports.getChildren().size(); i++) {
            list.add(BuildReport(reports.getChildren().get(i)));
        }

        return list;
    }

    private static Report BuildReport(XMLNode reportNocde) {
        String name = reportNocde.GetChild("name").getContent();
        String sheet = reportNocde.GetChild("sheet").getContent();
        String historycheck = reportNocde.GetChild("historycheck").getContent();
        String sql = reportNocde.GetChild("sql").getContent();
        String type = reportNocde.getAttributes().get("type");
        return new Report(type, name, sheet, historycheck == "1" ? true : false, sql);
    }
}
