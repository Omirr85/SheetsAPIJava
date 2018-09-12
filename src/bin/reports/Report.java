package bin.reports;

public class Report {
    public Report(String type, String name, String sheet, boolean historycheck, boolean comparehigher, Boolean comparetozero, String sql) {
        this.type = type;
        this.name = name;
        this.spreadsheet = sheet;
        this.historycheck = historycheck;
        this.comparehigher = comparehigher;
        this.comparetozero = comparetozero;
        this.sql = sql;
        this.maxSheets = 5;
    }

    private boolean done;
    private String type;
    private String name;
    private String spreadsheet;
    private Boolean historycheck = false;
    private Boolean comparehigher = false;
    private Boolean comparetozero = false;
    private String sql;
    private int maxSheets;

    public int getMaxSheets() { return maxSheets; }

    public String getType() { return type; }

    public String getName() {
        return name;
    }

    public String getSpreadsheetId() {
        return spreadsheet;
    }

    public Boolean getHistorycheck() { return historycheck; }

    public Boolean getComparetozero() { return comparetozero; }

    public Boolean getComparehigher() { return comparehigher; }

    public String getSql() {
        return sql;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void setMaxSheets(int maxSheets) {
        this.maxSheets = maxSheets;
    }
}
