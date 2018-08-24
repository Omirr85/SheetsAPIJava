package com.company;

import bin.reports.ReportQueue;
import bin.sql.DatabaseConnection;
import bin.xml.XMLNode;
import bin.xml.XMLParser;
import main.java.InheritTest;
import main.java.PostgreSqlExample;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Connection;

public class Main {

    public static void main(String[] args) throws IOException, GeneralSecurityException {


        /*DatabaseConnection dcon = new DatabaseConnection();
        Connection c = dcon.getConnection();*/

        ReportQueue queue = new ReportQueue();
        queue.Start();

        /*

        XMLReaderDOM xml = new XMLReaderDOM();
        xml.main();

        PostgreSqlExample  c = new PostgreSqlExample();
        c.main(new String[]{});*/
    }
}
