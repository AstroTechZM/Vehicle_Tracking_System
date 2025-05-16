import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Test {
    private static final Map<Integer, Color> rowColors = new HashMap<>();

    public static void main(String[] args) {
        // Create table model with sample data
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Name", "Age", "Occupation"}, 0);
        model.addRow(new Object[]{"John", 30, "Developer"});
        model.addRow(new Object[]{"Alice", 25, "Designer"});
        model.addRow(new Object[]{"Bob", 40, "Manager"});

        // Create a custom JTable that highlights rows
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                
                // Check if this row has a custom color
                if (rowColors.containsKey(row)) {
                    c.setBackground(rowColors.get(row));
                } else {
                    c.setBackground(getBackground());
                }
                return c;
            }
        };

        // Add table to a scroll pane and frame
        JFrame frame = new JFrame("Row Highlight Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JScrollPane(table));
        frame.pack();
        frame.setVisible(true);

        // Example: Highlight row 1 (Alice) in yellow
        highlightRow(table, 1, Color.YELLOW);

        // Example: Highlight row 2 (Bob) in light green
        highlightRow(table, 2, new Color(200, 255, 200));
    }

    /**
     * Changes the background color of a specific row in a JTable.
     * @param table The JTable to modify.
     * @param row The row index (0-based).
     * @param color The color to apply.
     */
    public static void highlightRow(JTable table, int row, Color color) {
        if (row < 0 || row >= table.getRowCount()) {
            throw new IllegalArgumentException("Invalid row index: " + row);
        }
        rowColors.put(row, color); // Store the color for this row
        table.repaint(); // Force a repaint to apply changes
    }
}
