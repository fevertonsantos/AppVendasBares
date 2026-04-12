package com.AppVendasBares.demo;

import java.io.OutputStream;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

public class PrinterRawTest {

    public static void main(String[] args) throws Exception {
        String printerName = "NOME_EXATO_DA_IMPRESSORA";

        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        PrintService selectedPrinter = null;

        for (PrintService service : services) {
            if (service.getName().equalsIgnoreCase(printerName)) {
                selectedPrinter = service;
                break;
            }
        }

        if (selectedPrinter == null) {
            System.out.println("Impressora não encontrada: " + printerName);
            return;
        }

        String texto = "Teste de impressão Java\r\n"
                + "App Vendas Bares\r\n"
                + "----------------------\r\n"
                + "Produto 1   R$ 10,00\r\n"
                + "Produto 2   R$ 20,00\r\n"
                + "Total       R$ 30,00\r\n\r\n";

        byte[] bytes = texto.getBytes("CP860");

        DocPrintJob job = selectedPrinter.createPrintJob();
        Doc doc = new SimpleDoc(bytes, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
        job.print(doc, null);

        System.out.println("Impressão enviada com sucesso.");
    }
}