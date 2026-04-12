package com.AppVendasBares.demo;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.output.WindowsPrintService;

public class PrintEscPosTest {

    public static void main(String[] args) throws Exception {
        String printerName = "NOME_EXATO_DA_IMPRESSORA";

        EscPos escpos = new EscPos(new WindowsPrintService(printerName));

        Style titulo = new Style()
                .setBold(true)
                .setJustification(EscPosConst.Justification.Center);

        Style normal = new Style()
                .setJustification(EscPosConst.Justification.Left);

        escpos.writeLF(titulo, "APP VENDAS BARES");
        escpos.writeLF(titulo, "CUPOM TESTE");
        escpos.writeLF("--------------------------------");

        escpos.writeLF(normal, "Produto 1         R$ 10,00");
        escpos.writeLF(normal, "Produto 2         R$ 20,00");
        escpos.writeLF("--------------------------------");
        escpos.writeLF(titulo, "TOTAL: R$ 30,00");

        escpos.feed(4);
        escpos.cut(EscPos.CutMode.FULL);
        escpos.close();

        System.out.println("Cupom enviado com sucesso.");
    }
}