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
package op.care.planung;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.popup.JidePopup;
import com.toedter.calendar.JDateChooser;
import entity.info.BWInfoKatTools;
import entity.planung.Intervention;
import entity.planung.MassTermin;
import entity.planung.Planung;
import op.OPDE;
import op.care.planung.massnahmen.PnlSelectIntervention;
import op.threads.DisplayMessage;
import op.tools.GUITools;
import op.tools.MyJDialog;
import op.tools.SYSConst;
import org.apache.commons.collections.Closure;
import org.jdesktop.swingx.HorizontalLayout;
import tablemodels.TMPlanung;
import tablerenderer.RNDHTML;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

/**
 * @author root
 */
public class DlgPlanung extends MyJDialog {
    public static final String internalClassID = "nursingrecords.nursingprocess.dlgplanung";
    private Closure actionBlock;
    private Planung planung;
    private JPopupMenu menu;
    Action delete;

    /**
     * Creates new form DlgPlanung
     */
    public DlgPlanung(Planung planung, Closure actionBlock) {
        super();
        this.planung = planung;
        this.actionBlock = actionBlock;
        initComponents();
        initDialog();
        pack();
        setVisible(true);
    }

    private void initDialog() {
        cmbKategorie.setModel(new DefaultComboBoxModel(BWInfoKatTools.getCategoriesForNursingProcess().toArray()));

        txtStichwort.setText(planung.getStichwort());
        txtSituation.setText(planung.getSituation());
        txtZiele.setText(planung.getZiel());
        jdcKontrolle.setDate(planung.getNKontrolle());
        jdcKontrolle.setMinSelectableDate(new Date());
        cmbKategorie.setSelectedItem(planung.getKategorie());
        reloadInterventions();

        String mode = "new";
        OPDE.getDisplayManager().addSubMessage(new DisplayMessage(OPDE.lang.getString(internalClassID + "." + mode), 5));


    }

    @Override
    public void dispose() {
        jdcKontrolle.cleanup();
        super.dispose();
        actionBlock.execute(planung);
    }

    private void reloadInterventions() {
        tblPlanung.setModel(new TMPlanung(planung));
        tblPlanung.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tblPlanung.getColumnModel().getColumn(TMPlanung.COL_TXT).setCellRenderer(new RNDHTML());
        tblPlanung.getColumnModel().getColumn(TMPlanung.COL_TXT).setHeaderValue(OPDE.lang.getString(PnlPlanung.internalClassID + ".interventions"));

//        new RNDButton(tblPlanung, delete, TMPlanung.COL_DEL);
//        tblPlanung.getColumnModel().getColumn(TMPlanung.COL_DEL).setCellRenderer(delbutton);
//        tblPlanung.getColumnModel().getColumn(TMPlanung.COL_DEL).setCellEditor(delbutton);
    }

    private void btnAddInterventionActionPerformed(ActionEvent e) {
        /***
         *      _     _            _       _     _
         *     | |__ | |_ _ __    / \   __| | __| |
         *     | '_ \| __| '_ \  / _ \ / _` |/ _` |
         *     | |_) | |_| | | |/ ___ \ (_| | (_| |
         *     |_.__/ \__|_| |_/_/   \_\__,_|\__,_|
         *
         */
        final JidePopup popup = new JidePopup();
        PnlSelectIntervention pnlSelectIntervention = new PnlSelectIntervention(new Closure() {
            @Override
            public void execute(Object o) {
                popup.hidePopup();
                if (o != null) {
                    for (Object obj : (Object[]) o) {
                        Intervention intervention = (Intervention) obj;
                        planung.getMassnahmen().add(new MassTermin(planung, intervention));
                    }
                    reloadInterventions();
                }
            }
        });

        popup.setMovable(false);
        popup.getContentPane().setLayout(new BoxLayout(popup.getContentPane(), BoxLayout.LINE_AXIS));

        popup.setOwner(btnAddIntervention);
        popup.removeExcludedComponent(btnAddIntervention);
        popup.getContentPane().add(pnlSelectIntervention);
        popup.setDefaultFocusComponent(pnlSelectIntervention);
        GUITools.showPopup(popup, SwingConstants.NORTH_WEST);
    }

//    private void reloadBibliothek() {
//        ArrayList bib = DBHandling.loadBibliothek(txtSuche.getText(), bewohner.getBWKennung());
//
//        tblBib.setModel(new TMPlanung(bib));
//        tblBib.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//
//        jspBib.dispatchEvent(new ComponentEvent(jspBib, ComponentEvent.COMPONENT_RESIZED));
//        //tblBib.getColumnModel().getColumn(0).setCellRenderer(new RNDPlanung());
//        tblBib.getColumnModel().getColumn(0).setCellRenderer(new RNDPlanung());
//
//        //btnAdd.setEnabled(!txtSuche.equals("") && tblBib.getModel().getRowCount() == 0);
//
//    }

    /**
     * Reasons why you couldn't save it
     *
     * @return
     */
    private boolean saveOK() {
//        boolean datumXX = jdcKontrolle.getDate() == null;
//        boolean kategorieXX = cmbKategorie.getSelectedIndex() < 0;
//        boolean situationXX1 = (editMode == CHANGE_MODE && txtSituation.getText().equals(""));
//        boolean situationXX2 = (editMode == CHANGE_MODE && txtSituation.getText().equalsIgnoreCase(oldSituation));
//        boolean zieleXX1 = (editMode == CHANGE_MODE && txtZiele.getText().equals(""));
//        boolean zieleXX2 = (editMode == CHANGE_MODE && txtZiele.getText().equalsIgnoreCase(oldZiele));
//        btnSave.setEnabled(!(stichwortXX || datumXX || kategorieXX)); //|| situationXX1 || situationXX2 || zieleXX1 || zieleXX2));


        if (txtStichwort.getText().trim().isEmpty()) {
            OPDE.getDisplayManager().addSubMessage(new DisplayMessage(OPDE.lang.getString(internalClassID + ".stichwortxx"), DisplayMessage.WARNING));
            return false;
        }

        if (jdcKontrolle.getDate() == null) {
            OPDE.getDisplayManager().addSubMessage(new DisplayMessage(OPDE.lang.getString(internalClassID + ".datumxx"), DisplayMessage.WARNING));
            return false;
        }

        if (cmbKategorie.getSelectedItem() == null) {
            OPDE.getDisplayManager().addSubMessage(new DisplayMessage(OPDE.lang.getString(internalClassID + ".kategoriexx"), DisplayMessage.WARNING));
            return false;
        }

        return true;
//        if (!btnSave.isEnabled()) {
//            String ursache = "<html><body>Sie können auf dem / den folgenden Grund/Gründen nicht speichern:<ul>";
//            ursache += (stichwortXX ? "<li>Sie <b>müssen</b> ein Stichwort angeben.</li>" : "");
//            ursache += (datumXX ? "<li>Sie haben ein falsches datum für die nächste Kontrolle angegeben.</li>" : "");
//            ursache += (kategorieXX ? "<li>Sie haben keine Kategorie für die Planung ausgewählt.</li>" : "");
////            ursache += (situationXX1 ? "<li>Sie haben keinen Text zur Situationsbeschreibung eingegeben. Bei einer Planungsänderung ist das Pflicht.</li>" : "");
////            ursache += (situationXX2 ? "<li>Sie haben den Text zur Situationsbeschreibung nicht verändert. Bei einer Planungsänderung ist das Pflicht.</li>" : "");
////            ursache += (zieleXX1 ? "<li>Sie haben keinen Text zur Zielbeschreibung eingegeben. Bei einer Planungsänderung ist das Pflicht.</li>" : "");
////            ursache += (zieleXX2 ? "<li>Sie haben den Text zur Zielbeschreibung nicht verändert. Bei einer Planungsänderung ist das Pflicht.</li>" : "");
//            ursache += "</ul></body></html>";
//            btnSave.setToolTipText(ursache);
//        } else {
//            btnSave.setToolTipText(null);
//        }

    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel5 = new JPanel();
        jLabel4 = new JLabel();
        txtStichwort = new JTextField();
        jLabel5 = new JLabel();
        cmbKategorie = new JComboBox();
        jScrollPane3 = new JScrollPane();
        txtSituation = new JTextArea();
        jLabel3 = new JLabel();
        jLabel8 = new JLabel();
        jScrollPane1 = new JScrollPane();
        txtZiele = new JTextArea();
        jLabel7 = new JLabel();
        jdcKontrolle = new JDateChooser();
        panel2 = new JPanel();
        jspPlanung = new JScrollPane();
        tblPlanung = new JTable();
        panel3 = new JPanel();
        btnAddIntervention = new JButton();
        panel1 = new JPanel();
        btnCancel = new JButton();
        btnSave = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.setLayout(new FormLayout(
                "default, $lcgap, 280dlu:grow, $ugap, pref, $lcgap, default",
                "fill:default, $lgap, default, $rgap, pref, $lgap, default"));

        //======== jPanel5 ========
        {
            jPanel5.setLayout(new FormLayout(
                    "default, $lcgap, default:grow",
                    "fill:default, $rgap, default, 2*($lgap, fill:default:grow), $lgap, pref"));

            //---- jLabel4 ----
            jLabel4.setFont(new Font("Arial", Font.PLAIN, 14));
            jLabel4.setText("Stichwort:");
            jPanel5.add(jLabel4, CC.xy(1, 1, CC.DEFAULT, CC.TOP));

            //---- txtStichwort ----
            txtStichwort.setFont(new Font("Arial", Font.BOLD, 20));
            jPanel5.add(txtStichwort, CC.xy(3, 1));

            //---- jLabel5 ----
            jLabel5.setFont(new Font("Arial", Font.PLAIN, 14));
            jLabel5.setText("Kategorie:");
            jPanel5.add(jLabel5, CC.xy(1, 3));

            //---- cmbKategorie ----
            cmbKategorie.setModel(new DefaultComboBoxModel(new String[]{
                    "Item 1",
                    "Item 2",
                    "Item 3",
                    "Item 4"
            }));
            cmbKategorie.setFont(new Font("Arial", Font.PLAIN, 14));
            jPanel5.add(cmbKategorie, CC.xy(3, 3));

            //======== jScrollPane3 ========
            {

                //---- txtSituation ----
                txtSituation.setColumns(20);
                txtSituation.setLineWrap(true);
                txtSituation.setRows(5);
                txtSituation.setWrapStyleWord(true);
                txtSituation.setFont(new Font("Arial", Font.PLAIN, 14));
                jScrollPane3.setViewportView(txtSituation);
            }
            jPanel5.add(jScrollPane3, CC.xy(3, 5));

            //---- jLabel3 ----
            jLabel3.setFont(new Font("Arial", Font.PLAIN, 14));
            jLabel3.setText("Situation:");
            jPanel5.add(jLabel3, CC.xy(1, 5, CC.DEFAULT, CC.TOP));

            //---- jLabel8 ----
            jLabel8.setFont(new Font("Arial", Font.PLAIN, 14));
            jLabel8.setText("Ziele:");
            jPanel5.add(jLabel8, CC.xy(1, 7, CC.DEFAULT, CC.TOP));

            //======== jScrollPane1 ========
            {

                //---- txtZiele ----
                txtZiele.setColumns(20);
                txtZiele.setLineWrap(true);
                txtZiele.setRows(5);
                txtZiele.setWrapStyleWord(true);
                txtZiele.setFont(new Font("Arial", Font.PLAIN, 14));
                jScrollPane1.setViewportView(txtZiele);
            }
            jPanel5.add(jScrollPane1, CC.xy(3, 7));

            //---- jLabel7 ----
            jLabel7.setFont(new Font("Arial", Font.PLAIN, 14));
            jLabel7.setText("Erste Kontrolle am:");
            jPanel5.add(jLabel7, CC.xy(1, 9));

            //---- jdcKontrolle ----
            jdcKontrolle.setFont(new Font("Arial", Font.PLAIN, 14));
            jPanel5.add(jdcKontrolle, CC.xy(3, 9));
        }
        contentPane.add(jPanel5, CC.xy(3, 3, CC.DEFAULT, CC.FILL));

        //======== panel2 ========
        {
            panel2.setLayout(new FormLayout(
                    "default:grow",
                    "default, $lgap, default"));

            //======== jspPlanung ========
            {
                jspPlanung.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        jspPlanungComponentResized(e);
                    }
                });

                //---- tblPlanung ----
                tblPlanung.setModel(new DefaultTableModel(
                        new Object[][]{
                                {null, null, null, null},
                                {null, null, null, null},
                                {null, null, null, null},
                                {null, null, null, null},
                        },
                        new String[]{
                                "Title 1", "Title 2", "Title 3", "Title 4"
                        }
                ));
                tblPlanung.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                tblPlanung.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        tblPlanungMousePressed(e);
                    }
                });
                jspPlanung.setViewportView(tblPlanung);
            }
            panel2.add(jspPlanung, CC.xy(1, 1));

            //======== panel3 ========
            {
                panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));

                //---- btnAddIntervention ----
                btnAddIntervention.setText(null);
                btnAddIntervention.setIcon(new ImageIcon(getClass().getResource("/artwork/22x22/bw/add.png")));
                btnAddIntervention.setContentAreaFilled(false);
                btnAddIntervention.setBorderPainted(false);
                btnAddIntervention.setBorder(null);
                btnAddIntervention.setPressedIcon(new ImageIcon(getClass().getResource("/artwork/22x22/bw/add-pressed.png")));
                btnAddIntervention.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnAddInterventionActionPerformed(e);
                    }
                });
                panel3.add(btnAddIntervention);
            }
            panel2.add(panel3, CC.xy(1, 3));
        }
        contentPane.add(panel2, CC.xy(5, 3));

        //======== panel1 ========
        {
            panel1.setLayout(new HorizontalLayout(5));

            //---- btnCancel ----
            btnCancel.setIcon(new ImageIcon(getClass().getResource("/artwork/22x22/cancel.png")));
            btnCancel.setText(null);
            btnCancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    btnCancelActionPerformed(e);
                }
            });
            panel1.add(btnCancel);

            //---- btnSave ----
            btnSave.setIcon(new ImageIcon(getClass().getResource("/artwork/22x22/apply.png")));
            btnSave.setText(null);
            btnSave.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    btnSaveActionPerformed(e);
                }
            });
            panel1.add(btnSave);
        }
        contentPane.add(panel1, CC.xy(5, 5, CC.RIGHT, CC.DEFAULT));
        setSize(1120, 740);
        setLocationRelativeTo(getOwner());
    }// </editor-fold>//GEN-END:initComponents


    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        planung = null;
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void jspPlanungComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jspPlanungComponentResized
        JScrollPane jsp = (JScrollPane) evt.getComponent();
        if (tblPlanung.getRowCount() <= 0) {
            return;
        }
        Dimension dim = jsp.getSize();
        int textWidth = dim.width - 25;
        TableColumnModel tcm1 = tblPlanung.getColumnModel();
        tcm1.getColumn(0).setPreferredWidth(textWidth);
        tcm1.getColumn(0).setHeaderValue(OPDE.lang.getString(PnlPlanung.internalClassID + ".interventions"));
    }//GEN-LAST:event_jspPlanungComponentResized

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if (saveOK()) {
            dispose();
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void tblPlanungMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPlanungMousePressed
        if (!evt.isPopupTrigger()) {
            return;
        }
//
//        ListSelectionModel lsm = tblPlanung.getSelectionModel();
//        int row = tblPlanung.rowAtPoint(p);
//        if (lsm.isSelectionEmpty() || (lsm.getMinSelectionIndex() == lsm.getMaxSelectionIndex())) {
//            lsm.setSelectionInterval(row, row);
//        }

        menu = new JPopupMenu();

        /***
         *      _ _                 ____                         ____       _      _
         *     (_) |_ ___ _ __ ___ |  _ \ ___  _ __  _   _ _ __ |  _ \  ___| | ___| |_ ___
         *     | | __/ _ \ '_ ` _ \| |_) / _ \| '_ \| | | | '_ \| | | |/ _ \ |/ _ \ __/ _ \
         *     | | ||  __/ | | | | |  __/ (_) | |_) | |_| | |_) | |_| |  __/ |  __/ ||  __/
         *     |_|\__\___|_| |_| |_|_|   \___/| .__/ \__,_| .__/|____/ \___|_|\___|\__\___|
         *                                    |_|         |_|
         */
        JMenuItem itemPopupDelete = new JMenuItem(OPDE.lang.getString("misc.commands.delete"), SYSConst.icon22delete);
        itemPopupDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                for (int row : tblPlanung.getSelectedRows()) {
                    planung.getMassnahmen().remove(((TMPlanung) tblPlanung.getModel()).getMassTermin(row));
                }
                ((TMPlanung) tblPlanung.getModel()).fireTableDataChanged();
            }
        });
        menu.add(itemPopupDelete);
//        itemPopupDelete.setEnabled(!kontrollenMarkiert());

        /***
         *      _ _                 ____                        ____       _              _       _
         *     (_) |_ ___ _ __ ___ |  _ \ ___  _ __  _   _ _ __/ ___|  ___| |__   ___  __| |_   _| | ___
         *     | | __/ _ \ '_ ` _ \| |_) / _ \| '_ \| | | | '_ \___ \ / __| '_ \ / _ \/ _` | | | | |/ _ \
         *     | | ||  __/ | | | | |  __/ (_) | |_) | |_| | |_) |__) | (__| | | |  __/ (_| | |_| | |  __/
         *     |_|\__\___|_| |_| |_|_|   \___/| .__/ \__,_| .__/____/ \___|_| |_|\___|\__,_|\__,_|_|\___|
         *                                    |_|         |_|
         */
        final JMenuItem itemPopupSchedule = new JMenuItem(OPDE.lang.getString("misc.commands.editsheduling"), SYSConst.icon22clock);
        itemPopupSchedule.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                final JidePopup popup = new JidePopup();
                // TODO: java.lang.ArrayIndexOutOfBoundsException: 0
                int row = tblPlanung.getSelectedRows()[0];
                MassTermin firstMassTerminWillBeTemplate = ((TMPlanung) tblPlanung.getModel()).getMassTermin(row);
                JPanel dlg = new PnlSchedule(firstMassTerminWillBeTemplate, new Closure() {
                    @Override
                    public void execute(Object o) {
                        if (o != null) {
                            MassTermin template = (MassTermin) o;
                            for (int row : tblPlanung.getSelectedRows()) {
                                planung.getMassnahmen().remove(((TMPlanung) tblPlanung.getModel()).getMassTermin(row));
                            }
                            for (int row : tblPlanung.getSelectedRows()) {
                                planung.getMassnahmen().add(template.clone());
                            }
                            popup.hidePopup();
                            ((TMPlanung) tblPlanung.getModel()).fireTableDataChanged();
                        }
                    }
                });


                popup.setMovable(false);
                popup.getContentPane().setLayout(new BoxLayout(popup.getContentPane(), BoxLayout.LINE_AXIS));
                popup.getContentPane().add(dlg);
                popup.setOwner(itemPopupSchedule);
                popup.removeExcludedComponent(itemPopupSchedule);
                popup.setDefaultFocusComponent(dlg);

                GUITools.showPopup(popup, SwingConstants.NORTH_WEST);
            }
        });
        menu.add(itemPopupSchedule);


        Point p = evt.getPoint();
        menu.show(evt.getComponent(), (int) p.getX(), (int) p.getY());
    }//GEN-LAST:event_tblPlanungMousePressed

//    private void saveEDIT() {
//        HashMap hm = new HashMap();
//        hm.put("Stichwort", txtStichwort.getText());
//        hm.put("Situation", txtSituation.getText());
//        hm.put("Ziel", txtZiele.getText());
//        ListElement lel = (ListElement) cmbKategorie.getSelectedItem();
//        hm.put("BWIKID", lel.getPk());
//        hm.put("NKontrolle", jdcKontrolle.getDate());
//
//        Connection db = OPDE.getDb().db;
//        try {
//            // Hier beginnt eine Transaktion
//            db.setAutoCommit(false);
//            db.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
//            db.commit();
//
//            if (!op.tools.DBHandling.updateRecord("Planung", hm, "PlanID", planid)) {
//                throw new SQLException("Fehler bei Insert into Planung");
//            }
//            hm.clear();
//            DBHandling.cleanDFN(planid);
//            DBHandling.tmp2real(planid);
//            DFNImport.importDFN(planid);
//
//            db.commit();
//            db.setAutoCommit(true);
//
//        } catch (SQLException ex) {
//            try {
//                db.rollback();
//            } catch (SQLException ex1) {
//                new DlgException(ex1);
//                ex1.printStackTrace();
//                System.exit(1);
//            }
//            new DlgException(ex);
//        }
//    }
//
//    private void saveTEMPLATE() {
//        HashMap hm = new HashMap();
//        hm.put("BWKennung", bewohner.getBWKennung());
//        hm.put("Stichwort", txtStichwort.getText());
//        hm.put("Situation", txtSituation.getText());
//        hm.put("Ziel", txtZiele.getText());
//        ListElement lel = (ListElement) cmbKategorie.getSelectedItem();
//        hm.put("BWIKID", lel.getPk());
//        hm.put("Von", "!NOW!");
//        hm.put("Bis", "!BAW!");
//        hm.put("AnUKennung", OPDE.getLogin().getUser().getUKennung());
//        hm.put("AbUKennung", null);
//        hm.put("PlanKennung", OPDE.getDb().getUID("__plankenn"));
//        hm.put("NKontrolle", jdcKontrolle.getDate());
//
//        Connection db = OPDE.getDb().db;
//        try {
//            // Hier beginnt eine Transaktion
//            db.setAutoCommit(false);
//            db.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
//            db.commit();
//
//            planid = op.tools.DBHandling.insertRecord("Planung", hm);
//            if (planid < 0) {
//                throw new SQLException("Fehler bei Insert into Planung");
//            }
//            hm.clear();
//            DBHandling.tmp2real(planid);
//            DFNImport.importDFN(planid);
//
//            db.commit();
//            db.setAutoCommit(true);
//
//        } catch (SQLException ex) {
//            try {
//                db.rollback();
//            } catch (SQLException ex1) {
//                new DlgException(ex1);
//                ex1.printStackTrace();
//                System.exit(1);
//            }
//            new DlgException(ex);
//        }
//    }
//
//    private void saveCHANGE() {
//        // Daten für die NEUE Planung
//        HashMap hm = new HashMap();
//        hm.put("BWKennung", bewohner.getBWKennung());
//        hm.put("Stichwort", txtStichwort.getText());
//        hm.put("Situation", txtSituation.getText());
//        hm.put("Ziel", txtZiele.getText());
//        ListElement lel = (ListElement) cmbKategorie.getSelectedItem();
//        hm.put("BWIKID", lel.getPk());
//        hm.put("Von", "!NOW+1!");
//        hm.put("Bis", "!BAW!");
//        hm.put("AnUKennung", OPDE.getLogin().getUser().getUKennung());
//        hm.put("AbUKennung", null);
//        hm.put("PlanKennung", plankenn);
//        hm.put("NKontrolle", jdcKontrolle.getDate());
//
//        Connection db = OPDE.getDb().db;
//        try {
//            // Hier beginnt eine Transaktion
//            db.setAutoCommit(false);
//            db.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
//            db.commit();
//
//            DBHandling.absetzen(planid, txtSituation.getText());
//
//            planid = op.tools.DBHandling.insertRecord("Planung", hm);
//            if (planid < 0) {
//                throw new SQLException("Fehler bei Insert into Planung");
//            }
//            hm.clear();
//            DBHandling.tmp2real(planid);
//            DFNImport.importDFN(planid, SYSCalendar.nowDB(), 0);
//
//            db.commit();
//            db.setAutoCommit(true);
//
//        } catch (SQLException ex) {
//            try {
//                db.rollback();
//            } catch (SQLException ex1) {
//                new DlgException(ex1);
//                ex1.printStackTrace();
//                System.exit(1);
//            }
//            new DlgException(ex);
//        }
//    }
//
//    private void saveNEW() {
//        HashMap hm = new HashMap();
//        hm.put("BWKennung", bewohner.getBWKennung());
//        hm.put("Stichwort", txtStichwort.getText());
//        hm.put("Situation", txtSituation.getText());
//        hm.put("Ziel", txtZiele.getText());
//        ListElement lel = (ListElement) cmbKategorie.getSelectedItem();
//        hm.put("BWIKID", lel.getPk());
//        hm.put("Von", "!NOW!");
//        hm.put("Bis", "!BAW!");
//        hm.put("AnUKennung", OPDE.getLogin().getUser().getUKennung());
//        hm.put("AbUKennung", null);
//        hm.put("PlanKennung", OPDE.getDb().getUID("__plankenn"));
//        hm.put("NKontrolle", jdcKontrolle.getDate());
//
//        Connection db = OPDE.getDb().db;
//        try {
//            // Hier beginnt eine Transaktion
//            db.setAutoCommit(false);
//            db.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
//            db.commit();
//            planid = op.tools.DBHandling.insertRecord("Planung", hm);
//            if (planid < 0) {
//                throw new SQLException("Fehler bei Insert into Planung");
//            }
//            hm.clear();
//            DBHandling.tmp2real(planid);
//            DFNImport.importDFN(planid);
//
//            db.commit();
//            db.setAutoCommit(true);
//
//        } catch (SQLException ex) {
//            try {
//                db.rollback();
//            } catch (SQLException ex1) {
//                new DlgException(ex1);
//                ex1.printStackTrace();
//                System.exit(1);
//            }
//            new DlgException(ex);
//        }
//    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JPanel jPanel5;
    private JLabel jLabel4;
    private JTextField txtStichwort;
    private JLabel jLabel5;
    private JComboBox cmbKategorie;
    private JScrollPane jScrollPane3;
    private JTextArea txtSituation;
    private JLabel jLabel3;
    private JLabel jLabel8;
    private JScrollPane jScrollPane1;
    private JTextArea txtZiele;
    private JLabel jLabel7;
    private JDateChooser jdcKontrolle;
    private JPanel panel2;
    private JScrollPane jspPlanung;
    private JTable tblPlanung;
    private JPanel panel3;
    private JButton btnAddIntervention;
    private JPanel panel1;
    private JButton btnCancel;
    private JButton btnSave;
    // End of variables declaration//GEN-END:variables
}
