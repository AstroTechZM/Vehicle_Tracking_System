

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class Test {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Styled JTable");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 400);

            // Sample Data
            String[] columns = {"License Plate", "Owner Name", "Check-In", "Check-Out", "Status"};
            Object[][] data = {
                {"KAB123A", "John Doe", "8:00 AM", "12:00 PM", "Checked Out"},
                {"KCD456B", "Jane Smith", "9:30 AM", "", "Checked In"},
            };

            JTable table = new JTable(data, columns);
            styleTable(table);

            JScrollPane scrollPane = new JScrollPane(table);
            frame.add(scrollPane);

            frame.setVisible(true);
        });
    }

    private static void styleTable(JTable table) {
        // Font and Row Height
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(28);

        // Header Styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(30, 58, 95));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(100, 35));

        // Row striping (alternating colors)
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean isSelected,
                                                           boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, isSelected, hasFocus, row, col);
                if (isSelected) {
                    c.setBackground(new Color(46, 90, 138));
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });

        // Grid color and spacing
        table.setGridColor(new Color(220, 220, 220));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
    }
}

