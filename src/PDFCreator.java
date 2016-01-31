import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFText2HTML;

import java.io.*;

public class PDFCreator {

    private static String convertText2Html(String fileName) throws IOException {
        PDDocument pdd = PDDocument.load(fileName);
        PDFText2HTML stripper = new PDFText2HTML("UTF-8");
        String text = stripper.getText(pdd);
        pdd.close();
        return text;
    }

    public static void main(String[] args) throws IOException, COSVisitorException {
        String fileName = "C:\\Users\\Covaciu\\Documents\\PATI15EHPR0792_HCP_Folder_r34_FSU.pdf";
        System.out.println(convertText2Html(fileName));
    }
}
