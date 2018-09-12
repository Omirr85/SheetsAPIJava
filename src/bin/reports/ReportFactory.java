package bin.reports;

import bin.xml.XMLNode;
import bin.xml.XMLParser;

import java.util.ArrayList;
import java.util.Optional;

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
        try {
            String name = reportNocde.GetChild("name").getContent();
            String sheet = reportNocde.GetChild("sheet").getContent();
            String sql = reportNocde.GetChild("sql").getContent();
            String type = reportNocde.getAttributes().get("type");

            XMLNode historycheckNode = reportNocde.GetChild("historycheck");
            String historycheck = "";
            if (historycheckNode != null)
                historycheck = historycheckNode.getContent();

            XMLNode comparetozeroNode = reportNocde.GetChild("comparetozero");
            String comparetozero = "";
            if (comparetozeroNode != null)
                comparetozero = comparetozeroNode.getContent();

            XMLNode comparehigherkNode = reportNocde.GetChild("comparehigher");
            String comparehigher = "";
            if (comparehigherkNode != null)
                comparehigher = comparehigherkNode.getContent();

            return new Report(type, name, sheet
                    , historycheck == "1" ? true : false
                    , comparehigher == "1" ? true : false
                    , comparetozero == "1" ? true : false
                    , sql);
        }
        catch (NullPointerException ex) {
            System.out.println("XML Error");
            ex.printStackTrace();
        }
        return null;
    }
}
