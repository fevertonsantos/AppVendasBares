package com.AppVendasBares.demo;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

public class PrinterListTest {

    public static void main(String[] args) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);

        if (services.length == 0) {
            System.out.println("Nenhuma impressora encontrada.");
            return;
        }

        for (PrintService ps : services) {
            System.out.println("Impressora: " + ps.getName());
        }
    }
}