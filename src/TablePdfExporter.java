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

public class TablePdfExporter {

    /**
     * Exports the given JTable to a PDF file at filePath.
     * @param table the JTable to export
     * @param filePath where to write the PDF, e.g. "logs.pdf"
     * @throws IOException on file errors
     */
    public static void exportTableToPdf(JTable table, String filePath) throws IOException {
        // 1. Prepare PDF writer and document
        PdfWriter writer = new PdfWriter(filePath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // 2. Read the table model
        TableModel model = table.getModel();
        int colCount = model.getColumnCount();
        int rowCount = model.getRowCount();

        // 3. Create an itext Table with the same number of columns
        Table pdfTable = new Table(colCount);
        pdfTable.setWidthPercent(100);

        // 4. Add header cells
        for (int c = 0; c < colCount; c++) {
            String header = model.getColumnName(c);
            Cell headerCell = new Cell().add(header)
                                        .setBold()
                                        .setTextAlignment(TextAlignment.CENTER);
            pdfTable.addHeaderCell(headerCell);
        }

        // 5. Add data cells
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                Object value = model.getValueAt(r, c);
                String text = value == null ? "" : value.toString();
                pdfTable.addCell(new Cell().add(text));
            }
        }

        // 6. Add the table to the document and close
        document.add(pdfTable);
        document.close();
    }
}
