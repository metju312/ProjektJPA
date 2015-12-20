package gui.guiTools;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;


public class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(UIManager.getColor("Button.background"));
        }
        setText((value == null) ? "" : value.toString());

        ImageIcon img = new ImageIcon("images/add_icon.jpg");
        setIcon(img);
        return this;
    }
}
