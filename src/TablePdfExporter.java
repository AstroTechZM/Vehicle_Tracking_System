import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.UnitValue;



public class TablePdfExporter {

    /**
     * Exports the given JTable to a PDF file at filePath.
     * @param table the JTable to export
     * @param filePath where to write the PDF, e.g. "logs.pdf"
     * @throws IOException on file errors
     */
 public static void exportTableToPdf(JTable table, String filePath) throws Exception {
    PdfWriter writer = new PdfWriter(filePath);
    PdfDocument pdfDoc = new PdfDocument(writer);
    Document document = new Document(pdfDoc);

    int colCount = table.getColumnCount();
    Table pdfTable = new Table(colCount);
    pdfTable.setWidth(UnitValue.createPercentValue(100));

    // Add headers
    for (int col = 0; col < colCount; col++) {
        String header = table.getColumnName(col);
        pdfTable.addHeaderCell(new Cell().add(new Paragraph(header)));
    }

    // Add rows
    for (int row = 0; row < table.getRowCount(); row++) {
        for (int col = 0; col < colCount; col++) {
            Object val = table.getValueAt(row, col);
            pdfTable.addCell(new Cell().add(new Paragraph(
                val == null ? "" : val.toString())));
        }
    }

    document.add(pdfTable);
    document.close();  // 🚨 must close to flush content
}

}
