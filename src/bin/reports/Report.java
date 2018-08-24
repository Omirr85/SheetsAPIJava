package bin.reports;

public class Report {
    public Report(String type, String name, String sheet, Boolean historycheck, String sql) {
        this.type = type;
        this.name = name;
        this.sheet = sheet;
        this.historycheck = historycheck;
        this.sql = sql;
    }

    private boolean done;
    private String type;
    private String name;
    private String sheet;
    private Boolean historycheck = false;
    private String sql;

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getSheet() {
        return sheet;
    }

    public Boolean getHistorycheck() {
        return historycheck;
    }

    public String getSql() {
        return sql;
    }


    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
