import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class LogsCsvExporter {

    /**
     * Exports the entire 'logs' table to a CSV file at the given path.
     * @param filePath where to write the CSV, e.g. "logs.csv"
     */
    public static void exportLogsToCsv(String filePath) throws SQLException, IOException {
        String sql = "SELECT * FROM logs";
        try (PreparedStatement ps = DBConnections.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery();
             FileWriter writer = new FileWriter(filePath)) {

            ResultSetMetaData md = rs.getMetaData();
            int colCount = md.getColumnCount();

            // 1) Write header row
            for (int i = 1; i <= colCount; i++) {
                writer.write(md.getColumnLabel(i));
                if (i < colCount) writer.write(",");
            }
            writer.write("\n");

            // 2) Write data rows
            while (rs.next()) {
                for (int i = 1; i <= colCount; i++) {
                    Object value = rs.getObject(i);
                    String cell = value != null
                                  ? value.toString().replace("\"", "\"\"")
                                  : "";
                    // wrap in quotes
                    writer.write("\"");
                    writer.write(cell);
                    writer.write("\"");
                    if (i < colCount) writer.write(",");
                }
                writer.write("\n");
            }
            writer.flush();
        }
    }
}
