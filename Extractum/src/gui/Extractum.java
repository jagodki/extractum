/*
 * Copyright 2017 Christoph.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gui;

import Utilities.LogArea;
import exporting.ExportTableModel;
import extractum.ExtractumController;
import importing.ImportTableModel;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Window;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Christoph
 */
public class Extractum extends javax.swing.JFrame {
    
    private final LogArea log;
    private final LogWindow logWindow;
    private final DatabaseSettings dbSettings;
    private Properties settings;
    private ExtractumController ec;
    private String importDirectory = "";
    private String importFile = "";
    private String exportDirectory = "";
    private String exportFile = "";

    /**
     * Creates new form Extractum
     */
    public Extractum() {
        this.logWindow = new LogWindow(this, true);
        this.log = new LogArea(this.logWindow.getjTextAreaLog());
        this.settings = new Properties();
        this.ec = new ExtractumController();
        this.setFocusable(true);
        
        //import the settings file from class path
        try {
            this.settings.load(getClass().getClassLoader().getResourceAsStream("gui/settings.properties"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,
                "Not able to load settings file.\nPlease reade the log for further information.",
                "Settings",
                JOptionPane.ERROR_MESSAGE);
            this.log.log(LogArea.ERROR, "cannot load settings file", ex);
        }
        
        //init the next window
        this.dbSettings = new DatabaseSettings(this, true, this.log, this.settings);
        
        enableOSXFullscreen(this);
        initComponents();
        
        //update components from settings
        this.jLabelCurrentDatabase.setText(this.settings.getProperty("database"));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupImport = new javax.swing.ButtonGroup();
        jMenuItem1 = new javax.swing.JMenuItem();
        jTabbedPaneMain = new javax.swing.JTabbedPane();
        jPanelImport = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableImport = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaImportSql = new javax.swing.JTextArea();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTableImportSchema = new javax.swing.JTable();
        jPanelExport = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableExport = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextAreaExportSql = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableExportSchema = new javax.swing.JTable();
        jPanelStatistics = new javax.swing.JPanel();
        jToolBar = new javax.swing.JToolBar();
        jButtonDbConnection = new javax.swing.JButton();
        jButtonLoad = new javax.swing.JButton();
        jButtonImport = new javax.swing.JButton();
        jButtonExport = new javax.swing.JButton();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(5000, 0));
        jLabel2 = new javax.swing.JLabel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        jLabelCurrentDatabase = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(25, 0), new java.awt.Dimension(25, 0), new java.awt.Dimension(25, 32767));
        jMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemOpenConfig = new javax.swing.JMenuItem();
        jMenuItemImport = new javax.swing.JMenuItem();
        jMenuItemExport = new javax.swing.JMenuItem();
        jMenuEdit = new javax.swing.JMenu();
        jMenuItemValidateXML = new javax.swing.JMenuItem();
        jMenuItemDBConnection = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSelectAll = new javax.swing.JMenuItem();
        jMenuItemSelectReference = new javax.swing.JMenuItem();
        jMenuHelp = new javax.swing.JMenu();
        jMenuItemLicense = new javax.swing.JMenuItem();
        jMenuItemLog = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Extractum");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocation(new java.awt.Point(100, 100));
        setMinimumSize(new java.awt.Dimension(500, 500));

        jTableImport.setModel(new ImportTableModel());
        jTableImport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableImportMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableImport);

        jTextAreaImportSql.setEditable(false);
        jTextAreaImportSql.setColumns(20);
        jTextAreaImportSql.setRows(5);
        jScrollPane3.setViewportView(jTextAreaImportSql);

        jTableImportSchema.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null}
            },
            new String [] {
                "Schemas"
            }
        ));
        jTableImportSchema.setPreferredSize(new java.awt.Dimension(100, 64));
        jScrollPane7.setViewportView(jTableImportSchema);

        javax.swing.GroupLayout jPanelImportLayout = new javax.swing.GroupLayout(jPanelImport);
        jPanelImport.setLayout(jPanelImportLayout);
        jPanelImportLayout.setHorizontalGroup(
            jPanelImportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelImportLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelImportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelImportLayout.setVerticalGroup(
            jPanelImportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelImportLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelImportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanelImportLayout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(7, 7, 7)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jTabbedPaneMain.addTab("Config Viewer", jPanelImport);

        jPanelExport.setPreferredSize(new java.awt.Dimension(100, 132));

        jTableExport.setModel(new ExportTableModel());
        jTableExport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableExportMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTableExport);

        jTextAreaExportSql.setColumns(20);
        jTextAreaExportSql.setRows(5);
        jScrollPane6.setViewportView(jTextAreaExportSql);

        jTableExportSchema.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null}
            },
            new String [] {
                "Schemas"
            }
        ));
        jTableExportSchema.setPreferredSize(new java.awt.Dimension(100, 64));
        jScrollPane5.setViewportView(jTableExportSchema);

        javax.swing.GroupLayout jPanelExportLayout = new javax.swing.GroupLayout(jPanelExport);
        jPanelExport.setLayout(jPanelExportLayout);
        jPanelExportLayout.setHorizontalGroup(
            jPanelExportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelExportLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelExportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelExportLayout.setVerticalGroup(
            jPanelExportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelExportLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelExportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addGroup(jPanelExportLayout.createSequentialGroup()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jTabbedPaneMain.addTab("Export Config", jPanelExport);

        javax.swing.GroupLayout jPanelStatisticsLayout = new javax.swing.GroupLayout(jPanelStatistics);
        jPanelStatistics.setLayout(jPanelStatisticsLayout);
        jPanelStatisticsLayout.setHorizontalGroup(
            jPanelStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 926, Short.MAX_VALUE)
        );
        jPanelStatisticsLayout.setVerticalGroup(
            jPanelStatisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 432, Short.MAX_VALUE)
        );

        jTabbedPaneMain.addTab("Database Statistics", jPanelStatistics);

        jToolBar.setFloatable(false);
        jToolBar.setRollover(true);

        jButtonDbConnection.setText("DB Connection");
        jButtonDbConnection.setFocusable(false);
        jButtonDbConnection.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonDbConnection.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonDbConnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDbConnectionActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonDbConnection);

        jButtonLoad.setText("Load");
        jButtonLoad.setFocusable(false);
        jButtonLoad.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonLoad.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoadActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonLoad);

        jButtonImport.setText("Import");
        jButtonImport.setFocusable(false);
        jButtonImport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonImport.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImportActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonImport);

        jButtonExport.setText("Export");
        jButtonExport.setFocusable(false);
        jButtonExport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonExport.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar.add(jButtonExport);
        jToolBar.add(filler2);

        jLabel2.setText("selected database:");
        jToolBar.add(jLabel2);
        jToolBar.add(filler3);

        jLabelCurrentDatabase.setText("---");
        jToolBar.add(jLabelCurrentDatabase);
        jToolBar.add(filler1);

        jMenuFile.setText("File");

        jMenuItemOpenConfig.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemOpenConfig.setText("Open Configuration");
        jMenuItemOpenConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOpenConfigActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemOpenConfig);

        jMenuItemImport.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemImport.setText("Import Data");
        jMenuItemImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemImportActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemImport);

        jMenuItemExport.setText("Export Data");
        jMenuFile.add(jMenuItemExport);

        jMenuBar.add(jMenuFile);

        jMenuEdit.setText("Edit");

        jMenuItemValidateXML.setText("Validate XML");
        jMenuEdit.add(jMenuItemValidateXML);

        jMenuItemDBConnection.setText("Open Database Connection");
        jMenuItemDBConnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDBConnectionActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemDBConnection);
        jMenuEdit.add(jSeparator1);

        jMenuItemSelectAll.setText("Select All");
        jMenuEdit.add(jMenuItemSelectAll);

        jMenuItemSelectReference.setText("Select Referenced Tables");
        jMenuEdit.add(jMenuItemSelectReference);

        jMenuBar.add(jMenuEdit);

        jMenuHelp.setText("Help");

        jMenuItemLicense.setText("Show License");
        jMenuHelp.add(jMenuItemLicense);

        jMenuItemLog.setText("Show Log");
        jMenuItemLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemLogActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItemLog);

        jMenuBar.add(jMenuHelp);

        setJMenuBar(jMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPaneMain)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTabbedPaneMain)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * This function starts the import of a selected configuration and its data files.
     * @param evt 
     */
    private void jButtonImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImportActionPerformed
        if(this.importDirectory.equals("") || this.importFile.equals("")) {
            JOptionPane.showMessageDialog(null,
                        "Configuration file was not loaded before.",
                        "Import Data",
                        JOptionPane.ERROR_MESSAGE);
        } else {
            new Thread(() -> {
                ec.importData(log,
                              this.logWindow.getjProgressBarMain(),
                              this.logWindow.getjProgressBarMinor(),
                              "",
                              "",
                              this.settings.getProperty("host"),
                              this.settings.getProperty("port"),
                              this.settings.getProperty("user"),
                              this.settings.getProperty("pw"),
                              (ImportTableModel) this.jTableImport.getModel());
            }, "import data").start();
        }
    }//GEN-LAST:event_jButtonImportActionPerformed

    /**
     * Opens the dialog with the log area and the progressbars.
     * @param evt the event, that started the action
     */
    private void jMenuItemLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLogActionPerformed
        //edit the position of the window, so it is displayed at the middle of the main window
        this.logWindow.setLocation(this.getX() + this.getWidth() / 2 - this.logWindow.getWidth() / 2,
                                   this.getY() + this.getHeight() / 2 - this.logWindow.getHeight() / 2);
        this.logWindow.setVisible(true);
    }//GEN-LAST:event_jMenuItemLogActionPerformed

    /**
     * Opens the dialog containing the parametres of the database connection.
     * @param evt the event, that started the action
     */
    private void jButtonDbConnectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDbConnectionActionPerformed
        //edit the position of the window, so it is displayed at the middle of the main window
        this.dbSettings.setLocation(this.getX() + this.getWidth() / 2 - this.dbSettings.getWidth() / 2,
                                      this.getY() + this.getHeight() / 2 - this.dbSettings.getHeight() / 2);
        this.dbSettings.setVisible(true);
    }//GEN-LAST:event_jButtonDbConnectionActionPerformed

    /**
     * Opens the dialog containing the parametres of the database connection.
     * @param evt the event, that started the action
     */
    private void jMenuItemDBConnectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDBConnectionActionPerformed
        this.jButtonDbConnectionActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemDBConnectionActionPerformed

    /**
     * This function imports a config file.
     * @param evt 
     */
    private void jMenuItemImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemImportActionPerformed
        this.jButtonImportActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemImportActionPerformed

    /**
     * This function imports a config file.
     * @param evt 
     */
    private void jButtonLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLoadActionPerformed
        FileDialog fd = new FileDialog(new Frame(), "Choose a file", FileDialog.LOAD);
        fd.setDirectory(this.settings.getProperty("path"));
        fd.setVisible(true);
        this.importDirectory = fd.getDirectory();
        this.importFile = fd.getFile();
        String path = this.importDirectory + File.separator + this.importFile;
        
        new Thread(() -> {this.jMenuItemLogActionPerformed(evt);}, "open log window").start();
        
        String dbName = "";
        
        new Thread(() -> {
            if(this.jTabbedPaneMain.getSelectedIndex() == 0) {
                this.jLabelCurrentDatabase.setText(
                    this.ec.initTable((ImportTableModel) this.jTableImport.getModel(), null, path, log, logWindow.getjProgressBarMain())
                );
            } else if(this.jTabbedPaneMain.getSelectedIndex() == 1) {
                this.jLabelCurrentDatabase.setText(
                     this.ec.initTable(null, (ExportTableModel) this.jTableExport.getModel(), path, log, logWindow.getjProgressBarMain())
                );
            }
        }, "init table").start();
        
        this.log.log(LogArea.INFO, "load config file finished", null);
    }//GEN-LAST:event_jButtonLoadActionPerformed

    private void jMenuItemOpenConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOpenConfigActionPerformed
        this.jButtonLoadActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemOpenConfigActionPerformed

    /**
     * This function will be activated after a click on an entry of the import table.
     * The SQL-statement of the selected table will be shown in the text area.
     * @param evt 
     */
    private void jTableImportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableImportMouseClicked
        String tableName = (String) ((ImportTableModel) this.jTableImport.getModel()).getValueAt(this.jTableImport.getSelectedRow(), 0);
        this.jTextAreaImportSql.setText(this.ec.getImportSql(tableName));
    }//GEN-LAST:event_jTableImportMouseClicked

    /**
     * This function will be activated after a click on an entry of the export table.
     * The SQL-statement of the selected table will be shown in the text area.
     * @param evt 
     */
    private void jTableExportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableExportMouseClicked
        String tableName = (String) ((ExportTableModel) this.jTableExport.getModel()).getValueAt(this.jTableExport.getSelectedRow(), 0);
        this.jTextAreaExportSql.setText(this.ec.getExportSql(tableName));
    }//GEN-LAST:event_jTableExportMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            /* Set the system look and feel */
            String os = System.getProperty("os.name", "generic").toLowerCase();
            if ((os.contains("mac")) || (os.contains("darwin"))) {
                System.setProperty("apple.laf.useScreenMenuBar", "true");
                System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Extractum");
            }
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Extractum.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Extractum().setVisible(true);
        });
    }
    
    /**
     * This function loads libraries containing GUI-elements for macOS.
     * @param window 
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void enableOSXFullscreen(Window window) {
        String os = System.getProperty("os.name", "generic").toLowerCase();
        if ((os.contains("mac")) || (os.contains("darwin"))) {        
            try {
                Class util = Class.forName("com.apple.eawt.FullScreenUtilities");
                Class params[] = new Class[]{Window.class, Boolean.TYPE};
                Method method = util.getMethod("setWindowCanFullScreen", params);
                method.invoke(util, window, true);
            } catch (Exception e) {
                this.log.log(LogArea.ERROR, "Not able to load native GUI-elements of macOS", e);
                System.out.println(e.getMessage());
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupImport;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.JButton jButtonDbConnection;
    private javax.swing.JButton jButtonExport;
    private javax.swing.JButton jButtonImport;
    private javax.swing.JButton jButtonLoad;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelCurrentDatabase;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenu jMenuEdit;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenu jMenuHelp;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItemDBConnection;
    private javax.swing.JMenuItem jMenuItemExport;
    private javax.swing.JMenuItem jMenuItemImport;
    private javax.swing.JMenuItem jMenuItemLicense;
    private javax.swing.JMenuItem jMenuItemLog;
    private javax.swing.JMenuItem jMenuItemOpenConfig;
    private javax.swing.JMenuItem jMenuItemSelectAll;
    private javax.swing.JMenuItem jMenuItemSelectReference;
    private javax.swing.JMenuItem jMenuItemValidateXML;
    private javax.swing.JPanel jPanelExport;
    private javax.swing.JPanel jPanelImport;
    private javax.swing.JPanel jPanelStatistics;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPaneMain;
    private javax.swing.JTable jTableExport;
    private javax.swing.JTable jTableExportSchema;
    private javax.swing.JTable jTableImport;
    private javax.swing.JTable jTableImportSchema;
    private javax.swing.JTextArea jTextAreaExportSql;
    private javax.swing.JTextArea jTextAreaImportSql;
    private javax.swing.JToolBar jToolBar;
    // End of variables declaration//GEN-END:variables
}
