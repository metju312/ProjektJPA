package gui.windows.panels;

import entities.Author;
import gui.guiTools.ButtonEditor;
import gui.guiTools.ButtonRenderer;
import gui.windows.dialogs.AddAuthorWindow;
import gui.windows.frames.MainWindow;
import net.miginfocom.swing.MigLayout;

import javax.persistence.EntityTransaction;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class AuthorsPanel extends JPanel {
    private MainWindow mainWindow;
    private JScrollPane scrollPane;

    private String[] columnNames = {"FirstName", "LastName", "Age", "Genre","UpdateAuthor","DeleteAuthor"};
    private JTable authorsTable;

    public java.util.List<Author> actualAuthorsList = new ArrayList<>();

    public AuthorsPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setCustomLayout();
        add(generateScrollPane(), "wrap");
        add(generateAddAuthorButton());
    }

    private JButton generateAddAuthorButton() {
        ImageIcon img = new ImageIcon("add_icon.jpg");
        JButton addAuthorButton = new JButton("Add author",img);
        addAuthorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddAuthorWindow addAuthorWindow = new AddAuthorWindow(mainWindow);
            }
        });
        return addAuthorButton;
    }

    private JScrollPane generateScrollPane() {
        scrollPane = new JScrollPane(generateTable());
        return scrollPane;
    }

    private JTable generateTable() {
        authorsTable = new JTable(new DefaultTableModel(generateDataFromDataBase(), columnNames));
        setTableButtons();
        return authorsTable;
    }

    private void setTableButtons() {
        authorsTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        authorsTable.getColumn("UpdateAuthor").setCellRenderer(new ButtonRenderer());
        authorsTable.getColumn("UpdateAuthor").setCellEditor(new ButtonEditor(new JCheckBox(),mainWindow));
        authorsTable.getColumn("DeleteAuthor").setCellRenderer(new ButtonRenderer());
        authorsTable.getColumn("DeleteAuthor").setCellEditor(new ButtonEditor(new JCheckBox(),mainWindow));


        authorsTable.getColumnModel().getColumn(0).setPreferredWidth(180);
        authorsTable.setPreferredScrollableViewportSize(new Dimension(500, 120));
    }

    private Object[][] generateDataFromDataBase() {

        actualAuthorsList.clear();
        EntityTransaction transaction = mainWindow.entityManager.getTransaction();
        try {
            transaction.begin();
            Collection authorCollection = mainWindow.entityManager.createQuery("SELECT e FROM Author e").getResultList();
            Object[][] data = new Object[numberOfAuthors(authorCollection)][6];

            int j = 0;
            for (Iterator i = authorCollection.iterator(); i.hasNext();) {
                Author e = (Author) i.next();
                actualAuthorsList.add(e);
                data[j][0] = e.getFirstName();
                data[j][1] = e.getLastName();
                data[j][2] = e.getAge();
                data[j][3] = e.getGenre();
                data[j][4] = "UpdateAuthor";
                data[j][5] = "DeleteAuthor";
                j++;
            }
            transaction.commit();
            return data;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
        return null;
    }

    public int numberOfAuthors(Collection authorCollection) {
        int counter = 0;
        for (Iterator i = authorCollection.iterator(); i.hasNext();) {
            Author e = (Author) i.next();
            counter++;
        }
        return counter;
    }

    public void refreshAuthorsTable(){
        authorsTable.setModel(new DefaultTableModel(generateDataFromDataBase(), columnNames));
        setTableButtons();
    }

    private void setCustomLayout() {
        setLayout(new MigLayout());
    }
}
