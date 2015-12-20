package gui;

import entities.Song;
import utils.SongService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class MainWindow extends JFrame{
    private static MainWindow instance = null;

    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myDB");
    public EntityManager entityManager = entityManagerFactory.createEntityManager();

    public int mainWindowWidth = 480;
    public int mainWindowHeight = 340;
    private	JPanel panel1;
    private	JPanel panel2;
    private	JPanel panel3;

    private JTabbedPane tabbedPane;
    private JScrollPane scrollPane;
    private JTable table;
    public java.util.List<Song> actualSongsList = new ArrayList<>();
    public SongService songService = new SongService(entityManager);

    private String[] columnNames = {"Title","Type","Length","Rating","Update","Delete"};

    private AddSongWindow addSongWindow;

    public static MainWindow getInstance() {
        if (instance == null) {
            instance = new MainWindow();
        }
        return instance;
    }

    public MainWindow(){
        setMainWindowValues();
        centerWindow();
        generateTabbedPanePages();
        add(generateTabbedPane());
        addExitProcedures();
        validate();
    }

    private void generateTabbedPanePages() {
        generatePage1();
        generatePage2();
        generatePage3();
    }

    private void addExitProcedures() {
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (entityManager != null) {
                    entityManager.close();
                }
                if (entityManagerFactory != null) {
                    entityManagerFactory.close();
                }
                System.exit(0);
            }
        });
    }

    private JTabbedPane generateTabbedPane() {
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab( "Songs", panel1 );
        tabbedPane.addTab( "Covers", panel2 );
        tabbedPane.addTab( "Authors", panel3 );
        return tabbedPane;
    }


    private void setMainWindowValues() {
        setSize(mainWindowWidth, mainWindowHeight);
        getContentPane().setLayout( new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        centerWindow();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void centerWindow() {
        setLocationRelativeTo(null);
    }

    public void generatePage1()
    {
        panel1 = new JPanel();
        //panel1.setLayout( new FlowLayout());
        //panel1.setLayout( new BoxLayout());
        //panel1.setLayout( new WrapLayout());

        scrollPane = new JScrollPane(generateTable());

        panel1.add(scrollPane, BorderLayout.NORTH);
        panel1.add(generateAddSongButton(), BorderLayout.SOUTH);

    }

    private JTable generateTable() {
        Object[][] data = generateDataFromDataBase();
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        table = new JTable(model);
        setTableButtons();
        return table;
    }

    private void setTableButtons() {
        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        table.getColumn("Update").setCellRenderer(new ButtonRenderer());
        table.getColumn("Update").setCellEditor(new ButtonEditor(new JCheckBox(),this));
        table.getColumn("Delete").setCellRenderer(new ButtonRenderer());
        table.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(),this));

        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
    }

    private JButton generateAddSongButton() {
        ImageIcon img = new ImageIcon("add_icon.jpg");
        JButton addSongButton = new JButton("Add song",img);
        MainWindow mainWindow = this;
        addSongButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(frame, "Eggs are not supposed to be green.");
                addSongWindow = new AddSongWindow(mainWindow);
            }
        });
        return addSongButton;
    }

    private Object[][] generateDataFromDataBase() {

        actualSongsList.clear();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Collection songCollection = entityManager.createQuery("SELECT e FROM Song e").getResultList();
            Object[][] data = new Object[numberOfSongs(songCollection)][6];

            int j = 0;
            for (Iterator i = songCollection.iterator(); i.hasNext();) {
                Song e = (Song) i.next();
                actualSongsList.add(e);
                data[j][0] = e.getTitle();
                data[j][1] = e.getType();
                data[j][2] = e.getLength();
                data[j][3] = e.getRating();
                data[j][4] = "update";
                data[j][5] = "delete";
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

    private int numberOfSongs(Collection songCollection) {
        int counter = 0;
        for (Iterator i = songCollection.iterator(); i.hasNext();) {
            Song e = (Song) i.next();
            counter++;
        }
        return counter;
    }

    public void revalidateMainWindow(){
        System.out.println("revalidateMainWindow");
        table.setModel(new DefaultTableModel(generateDataFromDataBase(), columnNames));
        setTableButtons();
    }

    public void generatePage2()
    {
        panel2 = new JPanel();
        panel2.setLayout( new BorderLayout() );
    }

    public void generatePage3()
    {
        panel3 = new JPanel();
        panel3.setLayout( new GridLayout( 3, 2 ) );
    }
}
