package com.company;

import bin.reports.ReportQueue;
import bin.sheets.SheetsHelper;

public class Main {

    public static void main(String[] args) {
        ReportQueue queue = new ReportQueue();
        SheetsHelper helper = new SheetsHelper();
        queue.Start(helper);
    }
}
