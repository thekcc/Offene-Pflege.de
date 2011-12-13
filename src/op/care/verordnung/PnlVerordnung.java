/*
 * OffenePflege
 * Copyright (C) 2008 Torsten Löhr
 * This program is free software; you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License V2 as published by the Free Software Foundation
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even 
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General 
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, write to 
 * the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 * www.offene-pflege.de
 * ------------------------ 
 * Auf deutsch (freie Übersetzung. Rechtlich gilt die englische Version)
 * Dieses Programm ist freie Software. Sie können es unter den Bedingungen der GNU General Public License, 
 * wie von der Free Software Foundation veröffentlicht, weitergeben und/oder modifizieren, gemäß Version 2 der Lizenz.
 *
 * Die Veröffentlichung dieses Programms erfolgt in der Hoffnung, daß es Ihnen von Nutzen sein wird, aber 
 * OHNE IRGENDEINE GARANTIE, sogar ohne die implizite Garantie der MARKTREIFE oder der VERWENDBARKEIT FÜR EINEN 
 * BESTIMMTEN ZWECK. Details finden Sie in der GNU General Public License.
 *
 * Sie sollten ein Exemplar der GNU General Public License zusammen mit diesem Programm erhalten haben. Falls nicht, 
 * schreiben Sie an die Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA.
 * 
 */
package op.care.verordnung;

import entity.Bewohner;
import entity.BewohnerTools;
import entity.system.SYSRunningClasses;
import entity.system.SYSRunningClassesTools;
import entity.files.SYSFilesTools;
import entity.verordnungen.*;
import entity.vorgang.VorgaengeTools;
import op.OCSec;
import op.OPDE;
import op.care.CleanablePanel;
import op.care.FrmPflege;
import op.care.bhp.PnlBHP;
import op.care.med.vorrat.*;
import op.tools.*;
import tablemodels.TMVerordnung;
import tablerenderer.RNDHTML;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author tloehr
 */
public class PnlVerordnung extends CleanablePanel {

    public static final String internalClassID = "nursingrecords.prescription";
    private String bwkennung;
    private Bewohner bewohner;
    private FrmPflege parent;
    private boolean readOnly = false;
    private OCSec ocs;
    private JPopupMenu menu;

    /**
     * Dieser Actionlistener wird gebraucht, damit die einzelnen Menüpunkte des Kontextmenüs, nachdem sie
     * aufgerufen wurden, einen reloadTable() auslösen können.
     */
    private ActionListener standardActionListener;
    private SYSRunningClasses myRunningClass, blockingClass;

    /**
     * Creates new form PnlVerordnung
     */
    public PnlVerordnung(FrmPflege parent, Bewohner bewohner) {
        this.parent = parent;
        ocs = OPDE.getOCSec();

        initComponents();
        change2Bewohner(bewohner);
    }

    @Override
    public void change2Bewohner(Bewohner bewohner) {
        this.bewohner = bewohner;
        this.bwkennung = bewohner.getBWKennung();

        if (myRunningClass != null){
            SYSRunningClassesTools.endModule(myRunningClass);
        }

        Pair<SYSRunningClasses, SYSRunningClasses> pair = SYSRunningClassesTools.startModule(internalClassID, bewohner, new String[]{"nursingrecords.prescription", "nursingrecords.bhp", "nursingrecords.bhpimport"});
        myRunningClass = pair.getFirst();
        readOnly = !myRunningClass.isRW();

        if (readOnly) {
            blockingClass = pair.getSecond();
            btnLock.setToolTipText("<html><body><h3>Dieser Datensatz ist belegt durch:</h3>"
                    + blockingClass.getLogin().getUser().getNameUndVorname()
                    + "</body></html>");
        } else {
            btnLock.setToolTipText(null);
        }

        btnLock.setEnabled(readOnly);

        BewohnerTools.setBWLabel(lblBW, bewohner);

        // SYSTools.setBWLabel(lblBW, bwkennung);

        ocs.setEnabled(this, "btnNew", btnNew, !readOnly);
        ocs.setEnabled(this, "btnBuchen", btnBuchen, false);
        ocs.setEnabled(this, "btnVorrat", btnVorrat, false);
        ocs.setEnabled(this, "btnPrint", btnPrint, false);
        ocs.setEnabled(this, "btnBestellungen", btnBestellungen, false);

        standardActionListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                reloadTable();
            }
        };
        loadTable();
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jspVerordnung = new JScrollPane();
        tblVerordnung = new JTable();
        jPanel1 = new JPanel();
        cbOhneMedi = new JCheckBox();
        cbBedarf = new JCheckBox();
        cbMedi = new JCheckBox();
        cbAbgesetzt = new JCheckBox();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        cbRegel = new JCheckBox();
        jLabel12 = new JLabel();
        lblBW = new JLabel();
        jToolBar1 = new JToolBar();
        btnNew = new JButton();
        btnBuchen = new JButton();
        btnVorrat = new JButton();
        btnPrint = new JButton();
        btnStellplan = new JButton();
        btnBestellungen = new JButton();
        btnLock = new JButton();

        //======== this ========

        //======== jspVerordnung ========
        {
            jspVerordnung.setToolTipText("");
            jspVerordnung.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    jspVerordnungComponentResized(e);
                }
            });

            //---- tblVerordnung ----
            tblVerordnung.setModel(new DefaultTableModel(
                    new Object[][]{
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null},
                            {null, null, null, null},
                    },
                    new String[]{
                            "Title 1", "Title 2", "Title 3", "Title 4"
                    }
            ) {
                Class<?>[] columnTypes = new Class<?>[]{
                        Object.class, Object.class, Object.class, Object.class
                };

                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    return columnTypes[columnIndex];
                }
            });
            tblVerordnung.setToolTipText("");
            tblVerordnung.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    tblVerordnungMousePressed(e);
                }
            });
            jspVerordnung.setViewportView(tblVerordnung);
        }

        //======== jPanel1 ========
        {
            jPanel1.setBorder(new BevelBorder(BevelBorder.RAISED));

            //---- cbOhneMedi ----
            cbOhneMedi.setSelected(true);
            cbOhneMedi.setText("ohne Medikamente");
            cbOhneMedi.setBorder(BorderFactory.createEmptyBorder());
            cbOhneMedi.setMargin(new Insets(0, 0, 0, 0));
            cbOhneMedi.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cbOhneMediActionPerformed(e);
                }
            });

            //---- cbBedarf ----
            cbBedarf.setSelected(true);
            cbBedarf.setText("bei Bedarf");
            cbBedarf.setBorder(BorderFactory.createEmptyBorder());
            cbBedarf.setMargin(new Insets(0, 0, 0, 0));
            cbBedarf.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cbBedarfActionPerformed(e);
                }
            });

            //---- cbMedi ----
            cbMedi.setSelected(true);
            cbMedi.setText("mit Medikamenten");
            cbMedi.setBorder(BorderFactory.createEmptyBorder());
            cbMedi.setMargin(new Insets(0, 0, 0, 0));
            cbMedi.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cbMediActionPerformed(e);
                }
            });

            //---- cbAbgesetzt ----
            cbAbgesetzt.setText("Abgesetzte");
            cbAbgesetzt.setBorder(BorderFactory.createEmptyBorder());
            cbAbgesetzt.setMargin(new Insets(0, 0, 0, 0));
            cbAbgesetzt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cbAbgesetztActionPerformed(e);
                }
            });

            //---- jLabel1 ----
            jLabel1.setText("fm - nachts, fr\u00fch morgens | mo - morgens | mi - mittags");

            //---- jLabel2 ----
            jLabel2.setText("nm - nachmittags | ab - abends | sa - nachts, sp\u00e4t abends");

            //---- cbRegel ----
            cbRegel.setSelected(true);
            cbRegel.setText("regelm\u00e4\u00dfig");
            cbRegel.setBorder(BorderFactory.createEmptyBorder());
            cbRegel.setMargin(new Insets(0, 0, 0, 0));
            cbRegel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cbRegelActionPerformed(e);
                }
            });

            //---- jLabel12 ----
            jLabel12.setText("<html>Hinweis: &frac14; = 0,25 | <sup>1</sup>/<sub>3</sub> = 0,33 | &frac12; = 0,5 | &frac34; = 0,75</html>");

            GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                    jPanel1Layout.createParallelGroup()
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(jPanel1Layout.createParallelGroup()
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addComponent(cbAbgesetzt)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(cbMedi)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(cbOhneMedi)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(cbBedarf)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(cbRegel))
                                            .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                    .addComponent(jLabel12, GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jLabel2, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addContainerGap(131, Short.MAX_VALUE))
            );
            jPanel1Layout.setVerticalGroup(
                    jPanel1Layout.createParallelGroup()
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(cbAbgesetzt)
                                            .addComponent(cbMedi)
                                            .addComponent(cbOhneMedi)
                                            .addComponent(cbBedarf)
                                            .addComponent(cbRegel))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel1)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel2)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel12, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap(20, Short.MAX_VALUE))
            );
        }

        //---- lblBW ----
        lblBW.setFont(new Font("Dialog", Font.BOLD, 18));
        lblBW.setForeground(new Color(255, 51, 0));
        lblBW.setText("Nachname, Vorname (*GebDatum, 00 Jahre) [??1]");

        //======== jToolBar1 ========
        {
            jToolBar1.setFloatable(false);

            //---- btnNew ----
            btnNew.setIcon(new ImageIcon(getClass().getResource("/artwork/22x22/filenew.png")));
            btnNew.setText("Neu");
            btnNew.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    btnNewActionPerformed(e);
                }
            });
            jToolBar1.add(btnNew);

            //---- btnBuchen ----
            btnBuchen.setIcon(new ImageIcon(getClass().getResource("/artwork/22x22/shetaddrow.png")));
            btnBuchen.setText("Buchen");
            btnBuchen.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    btnBuchenActionPerformed(e);
                }
            });
            jToolBar1.add(btnBuchen);

            //---- btnVorrat ----
            btnVorrat.setIcon(new ImageIcon(getClass().getResource("/artwork/22x22/sheetremocolums.png")));
            btnVorrat.setText("Vorrat");
            btnVorrat.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    btnVorratActionPerformed(e);
                }
            });
            jToolBar1.add(btnVorrat);

            //---- btnPrint ----
            btnPrint.setIcon(new ImageIcon(getClass().getResource("/artwork/22x22/fileprint.png")));
            btnPrint.setText("Drucken");
            btnPrint.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    btnPrintActionPerformed(e);
                }
            });
            jToolBar1.add(btnPrint);

            //---- btnStellplan ----
            btnStellplan.setIcon(new ImageIcon(getClass().getResource("/artwork/22x22/fileprint.png")));
            btnStellplan.setText("Stellplan");
            btnStellplan.setToolTipText("<html>Druckt den Plan zum Tabletten stellen f\u00fcr den <b>aktuellen</b> Tag und f\u00fcr <b>alle</b> BewohnerInnen.</html>");
            btnStellplan.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    btnStellplanActionPerformed(e);
                }
            });
            jToolBar1.add(btnStellplan);

            //---- btnBestellungen ----
            btnBestellungen.setIcon(new ImageIcon(getClass().getResource("/artwork/22x22/filefind.png")));
            btnBestellungen.setText("Bestellungen");
            btnBestellungen.setToolTipText("Bestellungen f\u00fcr alle BewohnerInnen anzeigen.");
            btnBestellungen.setEnabled(false);
            btnBestellungen.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    btnBestellungenActionPerformed(e);
                }
            });
            jToolBar1.add(btnBestellungen);
        }

        //---- btnLock ----
        btnLock.setBackground(Color.white);
        btnLock.setIcon(new ImageIcon(getClass().getResource("/artwork/22x22/encrypted.png")));
        btnLock.setBorder(null);
        btnLock.setBorderPainted(false);
        btnLock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnLockActionPerformed(e);
            }
        });

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup()
                        .addComponent(jToolBar1, GroupLayout.DEFAULT_SIZE, 755, Short.MAX_VALUE)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblBW, GroupLayout.DEFAULT_SIZE, 697, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnLock)
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jspVerordnung, GroupLayout.DEFAULT_SIZE, 715, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup()
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jToolBar1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(btnLock)
                                        .addComponent(lblBW))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jspVerordnung, GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnStellplanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStellplanActionPerformed
        printStellplan();
    }//GEN-LAST:event_btnStellplanActionPerformed

    private void btnBestellungenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBestellungenActionPerformed
        new DlgBestellListe(parent);
        loadTable();
    }//GEN-LAST:event_btnBestellungenActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        printVerordnungen(null);
    }//GEN-LAST:event_btnPrintActionPerformed

    private void printVerordnungen(int[] sel) {
        try {
            // Create temp file.
            File temp = File.createTempFile("verordnungen", ".html");

            // Delete temp file when program exits.
            temp.deleteOnExit();

            // Write to temp file
            BufferedWriter out = new BufferedWriter(new FileWriter(temp));

//            TMVerordnung tm = new TMVerordnung(bwkennung, cbAbgesetzt.isSelected(), cbMedi.isSelected(),
//                    cbOhneMedi.isSelected(), cbBedarf.isSelected(), cbRegel.isSelected(), false);
            TMVerordnung tm = (TMVerordnung) tblVerordnung.getModel();

            String html = SYSTools.htmlUmlautConversion(op.care.verordnung.DBHandling.getVerordnungenAsHTML(tm, bewohner, sel));

            out.write(SYSTools.addHTMLTitle(html, SYSTools.getBWLabel(bwkennung), true));

            out.close();
            SYSPrint.handleFile(parent, temp.getAbsolutePath(), Desktop.Action.OPEN);
        } catch (IOException e) {
            new DlgException(e);
        }

    }

    private void printStellplan() {

        try {
            // Create temp file.
            File temp = File.createTempFile("stellplan", ".html");

            // Delete temp file when program exits.
            temp.deleteOnExit();

            // Write to temp file
            BufferedWriter out = new BufferedWriter(new FileWriter(temp));
            String html = SYSTools.htmlUmlautConversion(VerordnungTools.getStellplanAsHTML(bewohner.getStation().getEinrichtung()));

            out.write(html);

            out.close();
            SYSPrint.handleFile(parent, temp.getAbsolutePath(), Desktop.Action.OPEN);
        } catch (IOException e) {
            new DlgException(e);
        }

    }

    private void tblVerordnungMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblVerordnungMousePressed
        Point p = evt.getPoint();

        final ListSelectionModel lsm = tblVerordnung.getSelectionModel();
        boolean singleRowSelected = lsm.getMaxSelectionIndex() == lsm.getMinSelectionIndex();

        if (lsm.getMinSelectionIndex() == lsm.getMaxSelectionIndex()) {

            int row = tblVerordnung.rowAtPoint(p);
            lsm.setSelectionInterval(row, row);
        }

        final List<Verordnung> selection = ((TMVerordnung) tblVerordnung.getModel()).getVordnungenAt(tblVerordnung.getSelectedRows());

        // Kontext Menü
        if (singleRowSelected && evt.isPopupTrigger()) {

            final Verordnung verordnung = (Verordnung) selection.get(0);

            long num = BHPTools.getNumBHPs(verordnung);
            boolean editAllowed = !readOnly && num == 0;
            boolean changeAllowed = !readOnly && !verordnung.isBedarf() && !verordnung.isAbgesetzt() && num > 0;
            boolean absetzenAllowed = !readOnly && !verordnung.isAbgesetzt();
            boolean deleteAllowed = !readOnly && num == 0;


            //                    editAllowed = ;
//                    changeAllowed = !readOnly && sitid == 0 && abdatum.equals(SYSConst.TS_BIS_AUF_WEITERES) && num > 0;
//                    deleteAllowed = !readOnly && num == 0;
//                    absetzenAllowed = !readOnly && !abgesetzt;
//                    attachAllowed = !readOnly;
//                    //orderAllowed = !readOnly && !abgesetzt && vorid > 0 && anarztid > 0 && bestellid == 0;
//                    //delOrderAllowed = !readOnly && !abgesetzt && vorid > 0 && bestellid > 0;
//                    documentsAllowed = !readOnly;

            SYSTools.unregisterListeners(menu);
            menu = new JPopupMenu();

            JMenuItem itemPopupEdit = new JMenuItem("Korrigieren");
            itemPopupEdit.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    new DlgVerordnung(parent, bwkennung, verordnung, DlgVerordnung.EDIT_MODE);
                    reloadTable();
                }
            });

            menu.add(itemPopupEdit);
            ocs.setEnabled(this, "itemPopupEdit", itemPopupEdit, editAllowed);
            //ocs.setEnabled(this, "itemPopupEditText", itemPopupEditText, !readOnly && status > 0 && changeable);
            // -------------------------------------------------
            JMenuItem itemPopupChange = new JMenuItem("Verändern");
            itemPopupChange.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    new DlgVerordnung(parent, bwkennung, verordnung, DlgVerordnung.CHANGE_MODE);
                    loadTable();
                }
            });
            menu.add(itemPopupChange);
            ocs.setEnabled(this, "itemPopupChange", itemPopupChange, changeAllowed);
            // -------------------------------------------------
            JMenuItem itemPopupQuit = new JMenuItem("Absetzen");
            itemPopupQuit.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    new DlgAbsetzen(parent, tblVerordnung.getModel().getValueAt(tblVerordnung.getSelectedRow(), TMVerordnung.COL_MSSN).toString(), verordnung.getVerid());
                    reloadTable();
                }
            });
            menu.add(itemPopupQuit);
            ocs.setEnabled(this, "itemPopupQuit", itemPopupQuit, absetzenAllowed);
            // -------------------------------------------------
            JMenuItem itemPopupDelete = new JMenuItem("Löschen");
            itemPopupDelete.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    if (JOptionPane.showConfirmDialog(parent, "Soll die Verordnung wirklich gelöscht werden.",
                            "Verordnung löschen", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        op.care.verordnung.DBHandling.deleteVerordnung(verordnung.getVerid());
                        loadTable();
                    }
                }
            });
            menu.add(itemPopupDelete);
            ocs.setEnabled(this, "itemPopupDelete", itemPopupDelete, deleteAllowed);

            if (verordnung.hasMedi()) {
                menu.add(new JSeparator());

                final MedBestand bestandImAnbruch = MedBestandTools.findByVerordnungImAnbruch(verordnung);
                boolean bestandAbschliessenAllowed = !readOnly && bestandImAnbruch != null && !bestandImAnbruch.hasNextBestand();
                boolean bestandAnbrechenAllowed = !readOnly && bestandImAnbruch == null;

                JMenuItem itemPopupCloseBestand = new JMenuItem("Bestand abschließen");
                itemPopupCloseBestand.addActionListener(new java.awt.event.ActionListener() {

                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        try {
                            new DlgBestandAbschliessen(parent, bestandImAnbruch.getBestID());
                            Thread.sleep(1000);
                            reloadTable();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(PnlBHP.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                menu.add(itemPopupCloseBestand);
                ocs.setEnabled(this, "itemPopupCloseBestand", itemPopupCloseBestand, bestandAbschliessenAllowed);

                JMenuItem itemPopupOpenBestand = new JMenuItem("Bestand anbrechen");
                itemPopupOpenBestand.addActionListener(new java.awt.event.ActionListener() {

                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        try {
                            new DlgBestandAnbrechen(parent, verordnung.getDarreichung().getDafID(), bwkennung);
                            Thread.sleep(1000);
                            reloadTable();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(PnlBHP.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                menu.add(itemPopupOpenBestand);
                ocs.setEnabled(this, "itemPopupOpenBestand", itemPopupOpenBestand, bestandAnbrechenAllowed);
            }
            menu.add(new JSeparator());

            JMenuItem itemPopupPrint = new JMenuItem("Markierte Verordnungen drucken");
            itemPopupPrint.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    int[] sel = tblVerordnung.getSelectedRows();
                    printVerordnungen(sel);
                }
            });
            menu.add(itemPopupPrint);

            if (OPDE.getAppInfo().userHasAccessLevelForThisClass(internalClassID, InternalClassACL.SELECT) && !verordnung.isAbgesetzt() && singleRowSelected) {
                menu.add(new JSeparator());
                menu.add(SYSFilesTools.getSYSFilesContextMenu(parent, verordnung, standardActionListener));
            }

            if (OPDE.getAppInfo().userHasAccessLevelForThisClass(internalClassID, InternalClassACL.SELECT) && !verordnung.isAbgesetzt() && singleRowSelected) {
                menu.add(new JSeparator());
                menu.add(VorgaengeTools.getVorgangContextMenu(parent, verordnung, bewohner, standardActionListener));
            }


            menu.add(new JSeparator());
            JMenuItem itemPopupInfo = new JMenuItem("Infos anzeigen");
            itemPopupInfo.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    final MedBestand bestandImAnbruch = MedBestandTools.findByVerordnungImAnbruch(verordnung);

                    long dafid = 0;
                    double apv = 0d;
                    double apvBest = 0d;
                    if (bestandImAnbruch != null) {
                        dafid = bestandImAnbruch.getDarreichung().getDafID();
                        apv = op.care.med.DBHandling.getAPV(dafid, bwkennung);
                        apvBest = bestandImAnbruch.getApv().doubleValue();
                    }
                    JOptionPane.showMessageDialog(parent, "VerID: " + verordnung.getVerid() + "\nVorID: " + bestandImAnbruch.getVorrat().getVorID() + "\nDafID: " + dafid + "\nAPV: " + apv + "\nAPV (Bestand): " + apvBest, "Software-Infos", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            ocs.setEnabled(this, "itemPopupInfo", itemPopupInfo, true);
            menu.add(itemPopupInfo);


            menu.show(evt.getComponent(), (int) p.getX(), (int) p.getY());
        }
    }//GEN-LAST:event_tblVerordnungMousePressed

    private void btnVorratActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVorratActionPerformed
        new DlgVorrat(this.parent, bwkennung);
        loadTable();
    }//GEN-LAST:event_btnVorratActionPerformed

    private void btnLockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLockActionPerformed
        change2Bewohner(bewohner);
    }//GEN-LAST:event_btnLockActionPerformed

    private void cbRegelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbRegelActionPerformed
        if (!cbBedarf.isSelected() && !cbRegel.isSelected()) {
            cbBedarf.doClick();
        } else {
            loadTable();
        }
    }//GEN-LAST:event_cbRegelActionPerformed

    private void cbBedarfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbBedarfActionPerformed
        if (!cbBedarf.isSelected() && !cbRegel.isSelected()) {
            cbRegel.doClick();
        } else {
            loadTable();
        }
    }//GEN-LAST:event_cbBedarfActionPerformed

    private void cbOhneMediActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbOhneMediActionPerformed
        if (!cbOhneMedi.isSelected() && !cbMedi.isSelected()) {
            cbMedi.doClick();
        } else {
            loadTable();
        }
    }//GEN-LAST:event_cbOhneMediActionPerformed

    private void cbMediActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbMediActionPerformed
        if (!cbOhneMedi.isSelected() && !cbMedi.isSelected()) {
            cbOhneMedi.doClick();
        } else {
            loadTable();
        }
    }//GEN-LAST:event_cbMediActionPerformed

    private void cbAbgesetztActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbAbgesetztActionPerformed
        loadTable();
    }//GEN-LAST:event_cbAbgesetztActionPerformed

    private void btnBuchenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuchenActionPerformed
        new DlgBestand(parent, bwkennung);
        loadTable();
    }//GEN-LAST:event_btnBuchenActionPerformed

    private void jspVerordnungComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jspVerordnungComponentResized
        JScrollPane jsp = (JScrollPane) evt.getComponent();
        Dimension dim = jsp.getSize();
        // Größe der Text Spalten im DFN ändern.
        // Summe der fixen Spalten  = 175 + ein bisschen
        int textWidth = dim.width - (150 + 150 + 85 + 85 + 25);
        TableColumnModel tcm1 = tblVerordnung.getColumnModel();
        if (tcm1.getColumnCount() < 4) {
            return;
        }

        tcm1.getColumn(TMVerordnung.COL_MSSN).setPreferredWidth(150);
        tcm1.getColumn(TMVerordnung.COL_Dosis).setPreferredWidth(textWidth);
        tcm1.getColumn(TMVerordnung.COL_Hinweis).setPreferredWidth(150);
        tcm1.getColumn(TMVerordnung.COL_AN).setPreferredWidth(85);
        tcm1.getColumn(TMVerordnung.COL_AB).setPreferredWidth(85);
        tcm1.getColumn(0).setHeaderValue("Medikament / Massnahme");
        tcm1.getColumn(1).setHeaderValue("Dosierung / Häufigkeit");
        tcm1.getColumn(2).setHeaderValue("Hinweise");
        tcm1.getColumn(3).setHeaderValue("Angesetzt");
        tcm1.getColumn(4).setHeaderValue("Abgesetzt");
    }//GEN-LAST:event_jspVerordnungComponentResized

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        new DlgVerordnung(parent, bwkennung);
        loadTable();
    }//GEN-LAST:event_btnNewActionPerformed

    public void cleanup() {
        SYSTools.unregisterListeners(this);
        SYSRunningClassesTools.endModule(myRunningClass);
    }

    private void loadTable() {
//        ListSelectionModel lsm = tblVerordnung.getSelectionModel();
//        if (lsl != null) {
//            lsm.removeListSelectionListener(lsl);
//        }
//        lsl = new HandleSelections();

        tblVerordnung.setModel(new TMVerordnung(bewohner, cbAbgesetzt.isSelected(), cbMedi.isSelected()));
        tblVerordnung.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        //lsm.addListSelectionListener(lsl);

        ocs.setEnabled(this, "btnBuchen", btnBuchen, !readOnly);// && tblVerordnung.getModel().getRowCount() > 0);
        ocs.setEnabled(this, "btnVorrat", btnVorrat, !readOnly);// && tblVerordnung.getModel().getRowCount() > 0);
        ocs.setEnabled(this, "btnPrint", btnPrint, !readOnly && tblVerordnung.getModel().getRowCount() > 0);
        ocs.setEnabled(this, "btnBestellungen", btnBestellungen, false); //!readOnly && tblVerordnung.getModel().getRowCount() > 0);

        jspVerordnung.dispatchEvent(new ComponentEvent(jspVerordnung, ComponentEvent.COMPONENT_RESIZED));
        tblVerordnung.getColumnModel().getColumn(0).setCellRenderer(new RNDHTML());
        tblVerordnung.getColumnModel().getColumn(1).setCellRenderer(new RNDHTML());
        tblVerordnung.getColumnModel().getColumn(2).setCellRenderer(new RNDHTML());
        tblVerordnung.getColumnModel().getColumn(3).setCellRenderer(new RNDHTML());
        tblVerordnung.getColumnModel().getColumn(4).setCellRenderer(new RNDHTML());
    }

    private void reloadTable() {
        TMVerordnung tm = (TMVerordnung) tblVerordnung.getModel();
        tm.reload();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JScrollPane jspVerordnung;
    private JTable tblVerordnung;
    private JPanel jPanel1;
    private JCheckBox cbOhneMedi;
    private JCheckBox cbBedarf;
    private JCheckBox cbMedi;
    private JCheckBox cbAbgesetzt;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JCheckBox cbRegel;
    private JLabel jLabel12;
    private JLabel lblBW;
    private JToolBar jToolBar1;
    private JButton btnNew;
    private JButton btnBuchen;
    private JButton btnVorrat;
    private JButton btnPrint;
    private JButton btnStellplan;
    private JButton btnBestellungen;
    private JButton btnLock;
    // End of variables declaration//GEN-END:variables

//    class HandleSelections implements ListSelectionListener {
//
//        public void valueChanged(ListSelectionEvent lse) {
//            // Erst reagieren wenn der Auswahl-Vorgang abgeschlossen ist.
//            TableModel tm = tblVerordnung.getModel();
//            if (tm.getRowCount() <= 0) {
//                return;
//            }
//            if (!lse.getValueIsAdjusting()) {
//                DefaultListSelectionModel lsm = (DefaultListSelectionModel) lse.getSource();
//                boolean singleSelection = lsm.getMinSelectionIndex() == lsm.getMaxSelectionIndex();
//                currCol = lsm.getLeadSelectionIndex();
//                if (lsm.isSelectionEmpty() || !singleSelection) {
//                    currentVerID = 0;
//                    editAllowed = false;
//                    changeAllowed = false;
//                    deleteAllowed = false;
//                    absetzenAllowed = false;
//                    attachAllowed = !singleSelection;
//                    documentsAllowed = false;
//                    infosAllowed = false;
//                } else {
//                    currentVerID = ((Long) tm.getValueAt(lsm.getLeadSelectionIndex(), TMVerordnung.COL_VERID)).longValue();
//                    anarztid = ((Long) tm.getValueAt(lsm.getLeadSelectionIndex(), TMVerordnung.COL_ANARZTID)).longValue();
//                    abarztid = ((Long) tm.getValueAt(lsm.getLeadSelectionIndex(), TMVerordnung.COL_ABARZTID)).longValue();
//                    ankhid = ((Long) tm.getValueAt(lsm.getLeadSelectionIndex(), TMVerordnung.COL_ANKHID)).longValue();
//                    abkhid = ((Long) tm.getValueAt(lsm.getLeadSelectionIndex(), TMVerordnung.COL_ABKHID)).longValue();
//                    vorid = ((Long) tm.getValueAt(lsm.getLeadSelectionIndex(), TMVerordnung.COL_VORID)).longValue();
//                    //bestellid = ((Long) tm.getValueAt(lsm.getLeadSelectionIndex(), TMVerordnung.COL_BESTELLID)).longValue();
//                    boolean abgesetzt = ((Boolean) tm.getValueAt(lsm.getLeadSelectionIndex(), TMVerordnung.COL_ABGESETZT)).booleanValue();
//                    // Korrektur nur erlauben, wenn es noch keine abgehakten BHPs dazu gibt.
//                    long num = op.care.verordnung.DBRetrieve.numAffectedBHPs(currentVerID);
//                    Timestamp abdatum = (Timestamp) tm.getValueAt(lsm.getLeadSelectionIndex(), TMVerordnung.COL_ABDATUM);
//                    long sitid = ((Long) tm.getValueAt(lsm.getLeadSelectionIndex(), TMVerordnung.COL_SITID)).longValue();
//
////                    bestaetigungAllowed = !readOnly && ((abgesetzt && abkhid != 0 && abarztid == 0)
////                            || (!abgesetzt && ankhid != 0 && anarztid == 0));
//                    editAllowed = !readOnly && num == 0;
//                    changeAllowed = !readOnly && sitid == 0 && abdatum.equals(SYSConst.TS_BIS_AUF_WEITERES) && num > 0;
//                    deleteAllowed = !readOnly && num == 0;
//                    absetzenAllowed = !readOnly && !abgesetzt;
//                    attachAllowed = !readOnly;
//                    //orderAllowed = !readOnly && !abgesetzt && vorid > 0 && anarztid > 0 && bestellid == 0;
//                    //delOrderAllowed = !readOnly && !abgesetzt && vorid > 0 && bestellid > 0;
//                    documentsAllowed = !readOnly;
//                    infosAllowed = true;
//
////
////                    ocs.setEnabled(this, "btnEdit", btnEdit, );
////                    ocs.setEnabled(this, "btnChange", btnChange, );
////                    ocs.setEnabled(this, "btnAbsetzen", btnAbsetzen, !readOnly && num  > 0);
////                    ocs.setEnabled(this, "btnDelete", btnDelete, !readOnly && num > 0);
////                    ocs.setEnabled(this, "btnAttach", btnAttach, !readOnly);
//                }
//            }
//        }
//    }
}
