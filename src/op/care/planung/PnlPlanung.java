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

import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.pane.CollapsiblePanes;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideButton;
import entity.Bewohner;
import entity.info.BWInfoKat;
import entity.info.BWInfoKatTools;
import entity.planung.DFNTools;
import entity.planung.InterventionSchedule;
import entity.planung.NursingProcess;
import entity.planung.NursingProcessTools;
import entity.system.SYSPropsTools;
import entity.system.Unique;
import entity.system.UniqueTools;
import op.OPDE;
import op.threads.DisplayManager;
import op.tools.*;
import org.apache.commons.collections.Closure;
import org.jdesktop.swingx.JXSearchField;
import org.jdesktop.swingx.VerticalLayout;
import org.joda.time.DateMidnight;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.Query;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author tloehr
 */
public class PnlPlanung extends NursingRecordsPanel {
    public static final String internalClassID = "nursingrecords.nursingprocess";
    private JPopupMenu menu;
    private boolean initPhase;

    private ActionListener standardActionListener;

    private Bewohner bewohner;
    private JScrollPane jspSearch;
    private CollapsiblePanes searchPanes;


    private HashMap<NursingProcess, CollapsiblePane> planungCollapsiblePaneMap;
    //    private HashMap<BWInfoKat, CollapsiblePane> categoryCPMap;
    private HashMap<BWInfoKat, java.util.List<NursingProcess>> planungen;
    //    private HashMap<BWInfo, JToggleButton> bwinfo4html;
//    private HashMap<BWInfoTyp, java.util.List<BWInfo>> bwinfos;
    private java.util.List<BWInfoKat> kategorien;

    private JToggleButton tbInactive;
    private JXSearchField txtSearch;

    /**
     * Creates new form PnlPlanung
     */
    public PnlPlanung(Bewohner bewohner, JScrollPane jspSearch) {
        initPhase = true;
        this.jspSearch = jspSearch;
//        standardActionListener = new ActionListener() {
//
//            public void actionPerformed(ActionEvent e) {
//                reloadTable();
//            }
//        };
        initComponents();
        initPanel();
        initPhase = false;

        change2Bewohner(bewohner);

    }

    private void initPanel() {
        planungCollapsiblePaneMap = new HashMap<NursingProcess, CollapsiblePane>();
//        categoryCPMap = new HashMap<BWInfoKat, CollapsiblePane>();
        planungen = new HashMap<BWInfoKat, java.util.List<NursingProcess>>();
        kategorien = new ArrayList<BWInfoKat>();
        prepareSearchArea();
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void reload() {
        reloadDisplay();
    }

    @Override
    public void change2Bewohner(Bewohner bewohner) {
        this.bewohner = bewohner;
        GUITools.setBWDisplay(bewohner);
//        categoryCPMap.clear();
        planungCollapsiblePaneMap.clear();
        planungen.clear();
        kategorien.clear();
        reloadDisplay();
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jspPlanung = new JScrollPane();
        cpPlan = new CollapsiblePanes();

        //======== this ========
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        //======== jspPlanung ========
        {

            //======== cpPlan ========
            {
                cpPlan.setLayout(new BoxLayout(cpPlan, BoxLayout.X_AXIS));
            }
            jspPlanung.setViewportView(cpPlan);
        }
        add(jspPlanung);
    }// </editor-fold>//GEN-END:initComponents

//    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
//        new DlgPlanung(parent, bewohner);
//        reloadTable();
//    }//GEN-LAST:event_btnNewActionPerformed
//
//    private void cbPastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPastActionPerformed
//        reloadTable();
//    }//GEN-LAST:event_cbPastActionPerformed
//
//    private void tblPlanungMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPlanungMousePressed
//        if (!evt.isPopupTrigger()) {
//            return;
//        }
//        Point p = evt.getPoint();
//        ListSelectionModel lsm = tblPlanung.getSelectionModel();
//        //int col = tblPlanung.columnAtPoint(p);
//        int row = tblPlanung.rowAtPoint(p);
//        lsm.setSelectionInterval(row, row);
//        final long planid = ((Long) tblPlanung.getModel().getValueAt(row, TMPlanungen.COL_PLANID)).longValue();
//        boolean abgesetzt = ((Boolean) tblPlanung.getModel().getValueAt(row, TMPlanungen.COL_ABGESETZT)).booleanValue();
//        final String stichwort = tblPlanung.getModel().getValueAt(row, TMPlanungen.COL_STICHWORT).toString();
//        long numAffectedDFNs = DBHandling.numAffectedDFNs(planid);
//        boolean sameUser = tblPlanung.getModel().getValueAt(row, TMPlanungen.COL_ANUKENNUNG).toString().equalsIgnoreCase(OPDE.getLogin().getUser().getUKennung());
//        boolean singleRowSelected = lsm.getMaxSelectionIndex() == lsm.getMinSelectionIndex();
//
//        /**
//         * BEARBEITEN
//         * Eine Planung kann GEändert werden (Korrektur)
//         * - Wenn es KEINE abgehakten, zugehörigen DFNs gibt.
//         * - Wenn sie nicht bereits abgesetzt wurde.
//         * - Wenn sie von mir ist.
//         *
//         */
//        boolean bearbeitenMöglich = OPDE.isAdmin() || (!abgesetzt && sameUser && numAffectedDFNs == 0);
//
//        // if (evt.isPopupTrigger()) {
//
//        //final HashMap entry = (HashMap) bwinfo.getAttribute().get(tblPlanung.getSelectedRow());
//        SYSTools.unregisterListeners(menu);
//        menu = new JPopupMenu();
//
//        // BEARBEITEN
//        JMenuItem itemPopupEdit = new JMenuItem("Bearbeiten");
//        itemPopupEdit.addActionListener(new java.awt.event.ActionListener() {
//
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                new DlgPlanung(parent, bewohner, planid, DlgPlanung.EDIT_MODE);
//                reloadTable();
//            }
//        });
//        menu.add(itemPopupEdit);
//
//        JMenuItem itemPopupChange = new JMenuItem("Verändern");
//        itemPopupChange.addActionListener(new java.awt.event.ActionListener() {
//
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                try {
//                    new DlgPlanung(parent, bewohner, planid, DlgPlanung.CHANGE_MODE);
//                    Thread.sleep(1000);// Sonst, falsche Darstellung in Tabelle
//                    reloadTable();
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(PnlPlanung.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
//        menu.add(itemPopupChange);
//
//        JMenuItem itemPopupQuit = new JMenuItem("Absetzen");
//        itemPopupQuit.addActionListener(new java.awt.event.ActionListener() {
//
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                try {
//                    new DlgAbsetzen(parent, planid, stichwort);
//                    Thread.sleep(1000); // Sonst, falsche Darstellung in Tabelle
//                    reloadTable();
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(PnlPlanung.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
//        menu.add(itemPopupQuit);
//
//        JMenuItem itemPopupDelete = new JMenuItem("Löschen");
//        itemPopupDelete.addActionListener(new java.awt.event.ActionListener() {
//
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                if (JOptionPane.showConfirmDialog(parent, "Möchten Sie diese Planung und die zugehörigen DFNs wirklich löschen ?",
//                        stichwort + " löschen ?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
//                    DBHandling.deletePlanung(planid);
//                    reloadTable();
//                }
//            }
//        });
//        menu.add(itemPopupDelete);
//
//        JMenuItem itemPopupControl = new JMenuItem("Überprüfung eintragen");
//        itemPopupControl.addActionListener(new java.awt.event.ActionListener() {
//
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                new DlgPKontrolle(parent, planid);
//                reloadTable();
//            }
//        });
//        menu.add(itemPopupControl);
//
//        menu.add(new JSeparator());
//        JMenuItem itemPopupInfo = new JMenuItem("Infos anzeigen");
//        itemPopupInfo.addActionListener(new java.awt.event.ActionListener() {
//
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                JOptionPane.showMessageDialog(parent, "PlanID: " + planid + "\n", "Software-Infos", JOptionPane.INFORMATION_MESSAGE);
//            }
//        });
//        menu.add(itemPopupInfo);
//        ocs.setEnabled(this, "itemPopupInfo", itemPopupInfo, true);
//
//
//        /**
//         * Löschen
//         * Eine Planung kann gelöscht werden
//         * - Wenn es KEINE abgehakten, zugehörigen DFNs gibt. Sonst nicht.
//         * - man Eigentümer, mindestens PDL oder ADMIN ist
//         *
//         */
//        boolean löschenMöglich = OPDE.isAdmin() || (!abgesetzt && sameUser && numAffectedDFNs == 0);
//
//        /**
//         * Verändern
//         * Eine Planung kann VERändert werden
//         * - Wenn es abgehakte, zugehörige DFNs gibt. Sonst nicht.
//         * - ab Examen aufwärts.
//         *
//         */
//        boolean verändernMöglich = !abgesetzt && numAffectedDFNs > 0;
//        /**
//         * Absetzen
//         * Eine Planung kann abgesetzt werden.
//         * - immer
//         * - ab Examen aufwärts.
//         *
//         */
//        boolean absetzenMöglich = !abgesetzt && numAffectedDFNs > 0;
//
//
//        ocs.setEnabled(this, "itemPopupEdit", itemPopupEdit, bearbeitenMöglich);
//        ocs.setEnabled(this, "itemPopupChange", itemPopupChange, verändernMöglich);
//        ocs.setEnabled(this, "itemPopupDelete", itemPopupDelete, löschenMöglich);
//        ocs.setEnabled(this, "itemPopupQuit", itemPopupQuit, absetzenMöglich);
//        ocs.setEnabled(this, "itemPopupControl", itemPopupQuit, absetzenMöglich);
//
//        if (singleRowSelected) {
////            menu.add(new JSeparator());
////            menu.add(op.share.vorgang.DBHandling.getVorgangContextMenu(parent, "Planung", planid, bwkennung, fileActionListener));
//
//            EntityManager em = OPDE.createEM();
//            Planung planung = em.find(Planung.class, planid);
//            if (!planung.isAbgesetzt()) {
//                menu.add(new JSeparator());
//                menu.add(VorgaengeTools.getVorgangContextMenu(parent, planung, bewohner, standardActionListener));
//            }
//            em.close();
//        }
//
//
//        menu.show(evt.getComponent(), (int) p.getX(), (int) p.getY());
////        } else if (bearbeitenMöglich && evt.getClickCount() == 2) { // Bearbeiten, wenn möglich
////            new DlgPlanung(parent, bwkennung, planid, DlgPlanung.EDIT_MODE);
////            reloadTable();
////        }
//    }//GEN-LAST:event_tblPlanungMousePressed
//
//    private void jspPlanungComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jspPlanungComponentResized
//        JScrollPane jsp = (JScrollPane) evt.getComponent();
//        Dimension dim = jsp.getSize();
//        // Größe der Text Spalten im DFN ändern.
//        // Summe der fixen Spalten  = 175 + ein bisschen
//        int textWidth = dim.width - (150 + 65 + 65 + 25);
//        TableColumnModel tcm1 = tblPlanung.getColumnModel();
//        if (tcm1.getColumnCount() < 4) {
//            return;
//        }
//
//        tcm1.getColumn(TMPlanungen.COL_KATEGORIE).setPreferredWidth(150);
//        tcm1.getColumn(TMPlanungen.COL_BEMERKUNG).setPreferredWidth(textWidth);
//        tcm1.getColumn(TMPlanungen.COL_AN).setPreferredWidth(65);
//        tcm1.getColumn(TMPlanungen.COL_AB).setPreferredWidth(65);
//        tcm1.getColumn(TMPlanungen.COL_KATEGORIE).setHeaderValue("Kategorie");
//        tcm1.getColumn(TMPlanungen.COL_BEMERKUNG).setHeaderValue("Bemerkung");
//        tcm1.getColumn(TMPlanungen.COL_AN).setHeaderValue("Angesetzt");
//        tcm1.getColumn(TMPlanungen.COL_AB).setHeaderValue("Abgesetzt");
//    }//GEN-LAST:event_jspPlanungComponentResized

//    private void btnVorlageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVorlageActionPerformed
//        long template = DlgVorlage.showDialog(parent);
//        if (template > 0) {
//            new DlgPlanung(parent, bewohner, template, DlgPlanung.TEMPLATE_MODE);
//            reloadTable();
//        }
//    }//GEN-LAST:event_btnVorlageActionPerformed
//
//    private void cbDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbDetailsActionPerformed
//        reloadTable();
//    }//GEN-LAST:event_cbDetailsActionPerformed
//
//    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
//        try {
//            // Create temp file.
//            File temp = File.createTempFile("planung", ".html");
//
//            // Delete temp file when program exits.
//            temp.deleteOnExit();
//
//            // Write to temp file
//            BufferedWriter out = new BufferedWriter(new FileWriter(temp));
//            if (cbPast.isSelected()) {
//                out.write(DBHandling.getPlanungenAsHTML(bwkennung, SYSConst.DATE_VON_ANFANG_AN, SYSConst.DATE_BIS_AUF_WEITERES));
//            } else {
//                Date now = SYSCalendar.nowDBDate();
//                Date von = new Date(SYSCalendar.erkenneDatum("03.01.2011").getTimeInMillis());
//
//                out.write(DBHandling.getPlanungenAsHTML(bwkennung, von, von));
//            }
//            out.close();
//            SYSFilesTools.handleFile(temp, Desktop.Action.OPEN);
//        } catch (IOException e) {
//        }
//    }//GEN-LAST:event_btnPrintActionPerformed
//
//    private void btnCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopyActionPerformed
//        new DlgCopy(parent, bwkennung);
//        reloadTable();
//    }//GEN-LAST:event_btnCopyActionPerformed


    private void reloadDisplay() {
        /***
         *               _                 _ ____  _           _
         *      _ __ ___| | ___   __ _  __| |  _ \(_)___ _ __ | | __ _ _   _
         *     | '__/ _ \ |/ _ \ / _` |/ _` | | | | / __| '_ \| |/ _` | | | |
         *     | | |  __/ | (_) | (_| | (_| | |_| | \__ \ |_) | | (_| | |_| |
         *     |_|  \___|_|\___/ \__,_|\__,_|____/|_|___/ .__/|_|\__,_|\__, |
         *                                              |_|            |___/
         */
        initPhase = true;

        final boolean withworker = false;
        if (withworker) {

//            OPDE.getMainframe().setBlocked(true);
//            OPDE.getDisplayManager().setProgressBarMessage(new DisplayMessage(OPDE.lang.getString("misc.msg.wait"), -1, 100));
//
////            lblWait.setText(OPDE.lang.getString("misc.msg.wait"));
////            ((CardLayout) pnlCard.getLayout()).show(pnlCard, "cardWait");
//
//            tabKat.removeAll();
//            bwinfos.clear();
//            panelmap.clear();
//
//            SwingWorker worker = new SwingWorker() {
//                TableModel model;
//
//                @Override
//                protected Object doInBackground() throws Exception {
//                    try {
//                        int progress = 0;
//
//                        // Eliminate empty categories
//                        kategorien = new ArrayList<BWInfoKat>();
//                        for (final BWInfoKat kat : BWInfoKatTools.getKategorien()) {
//                            if (!BWInfoTypTools.findByKategorie(kat).isEmpty()) {
//                                kategorien.add(kat);
//                            }
//                        }
//
//                        // create tabs
//                        for (final BWInfoKat kat : kategorien) {
//                            progress++;
//                            OPDE.getDisplayManager().setProgressBarMessage(new DisplayMessage(OPDE.lang.getString("misc.msg.wait"), progress, kategorien.size()));
//
//                            if (!BWInfoTypTools.findByKategorie(kat).isEmpty()) {
//                                tabKat.addTab(kat.getBezeichnung(), new JScrollPane(createCollapsiblePanesFor(kat)));
//                            } else {
//                                kategorien.remove(kat);
//                            }
//                        }
//                    } catch (Exception e) {
//                        OPDE.fatal(e);
//                    }
//                    return null;
//                }
//
//                @Override
//                protected void done() {
//                    txtHTML.setText(null);
//                    tabKat.setSelectedIndex(SYSPropsTools.getInteger(internalClassID + ":tabKatSelectedIndex"));
//                    refreshDisplay();
//                    btnBWDied.setEnabled(bewohner.isAktiv());
//                    btnBWMovedOut.setEnabled(bewohner.isAktiv());
//                    btnBWisAway.setEnabled(bewohner.isAktiv() && !BWInfoTools.isAbwesend(bewohner));
//                    btnBWisBack.setEnabled(bewohner.isAktiv() && BWInfoTools.isAbwesend(bewohner));
//                    initPhase = false;
//                    OPDE.getDisplayManager().setProgressBarMessage(null);
//                    OPDE.getMainframe().setBlocked(false);
//                }
//            };
//            worker.execute();

        } else {

            cpPlan.removeAll();

            if (kategorien.isEmpty()) {
                // Elmininate empty categories
                for (final BWInfoKat kat : BWInfoKatTools.getKategorien()) {
                    if (!NursingProcessTools.findByKategorieAndBewohner(bewohner, kat).isEmpty()) {
                        kategorien.add(kat);
                    }
                }
            }

            cpPlan.setLayout(new JideBoxLayout(cpPlan, JideBoxLayout.Y_AXIS));
            for (BWInfoKat kat : kategorien) {
                cpPlan.add(createCollapsiblePanesFor(kat));
            }
            cpPlan.addExpansion();
//            refreshDisplay();
        }
        initPhase = false;

    }

//    private void reloadTable() {
//
//        tblPlanung.setModel(new TMPlanungen(bwkennung, cbPast.isSelected(), cbDetails.isSelected()));
//
//        tblPlanung.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
////        lsm.addListSelectionListener(lsl);
//        jspPlanung.dispatchEvent(new ComponentEvent(jspPlanung, ComponentEvent.COMPONENT_RESIZED));
//
//        tblPlanung.getColumnModel().getColumn(0).setCellRenderer(new RNDPlanungen());
//        tblPlanung.getColumnModel().getColumn(1).setCellRenderer(new RNDPlanungen());
//        tblPlanung.getColumnModel().getColumn(2).setCellRenderer(new RNDPlanungen());
//        tblPlanung.getColumnModel().getColumn(3).setCellRenderer(new RNDPlanungen());
//
//
//    }


    private CollapsiblePane createCollapsiblePanesFor(final BWInfoKat kat) {
        final CollapsiblePane katpane = new CollapsiblePane(kat.getBezeichnung());
//        categoryCPMap.put(kat, katpane);

        katpane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                try {
                    if (katpane.isCollapsed()) {
                        katpane.setCollapsed(false);
                    } else {
                        // collapse all children
                        for (NursingProcess planung : planungen.get(kat)) {
                            planungCollapsiblePaneMap.get(planung).setCollapsed(true);
                        }
                        katpane.setCollapsed(true);
                    }
                } catch (PropertyVetoException e) {
                    OPDE.error(e);
                }
            }
        });
        katpane.setSlidingDirection(SwingConstants.SOUTH);
        katpane.setBackground(kat.getBackgroundHeader());
        katpane.setForeground(kat.getForegroundHeader());
        katpane.setOpaque(false);
        JPanel katPanel = new JPanel();

        if (!planungen.containsKey(kat)) {
            planungen.put(kat, NursingProcessTools.findByKategorieAndBewohner(bewohner, kat));
        }

        katPanel.setLayout(new VerticalLayout());
        for (NursingProcess planung : planungen.get(kat)) {
            CollapsiblePane panel = createPanelFor(planung);
            katPanel.add(panel);
            planungCollapsiblePaneMap.put(planung, panel);
        }
        katpane.setContentPane(katPanel);

        try {
            katpane.setCollapsed(true);
        } catch (PropertyVetoException e) {
            OPDE.error(e);
        }
        return katpane;
    }


    private CollapsiblePane createPanelFor(final NursingProcess planung) {
        final CollapsiblePane panelForPlanung = new CollapsiblePane();
        long numDFNs = DFNTools.getNumDFNs(planung);

        /***
         *      _   _ _____    _    ____  _____ ____
         *     | | | | ____|  / \  |  _ \| ____|  _ \
         *     | |_| |  _|   / _ \ | | | |  _| | |_) |
         *     |  _  | |___ / ___ \| |_| | |___|  _ <
         *     |_| |_|_____/_/   \_\____/|_____|_| \_\
         *
         */

        JPanel titlePanelleft = new JPanel();
        titlePanelleft.setLayout(new BoxLayout(titlePanelleft, BoxLayout.LINE_AXIS));

        /***
         *      _     _       _    _           _   _                _   _                _
         *     | |   (_)_ __ | | _| |__  _   _| |_| |_ ___  _ __   | | | | ___  __ _  __| | ___ _ __
         *     | |   | | '_ \| |/ / '_ \| | | | __| __/ _ \| '_ \  | |_| |/ _ \/ _` |/ _` |/ _ \ '__|
         *     | |___| | | | |   <| |_) | |_| | |_| || (_) | | | | |  _  |  __/ (_| | (_| |  __/ |
         *     |_____|_|_| |_|_|\_\_.__/ \__,_|\__|\__\___/|_| |_| |_| |_|\___|\__,_|\__,_|\___|_|
         *
         */
        JideButton title = GUITools.createHyperlinkButton(getHyperlinkButtonTextFor(planung), null, null);
//        title.addMouseListener(GUITools.getHyperlinkStyleMouseAdapter());
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    panelForPlanung.setCollapsed(!panelForPlanung.isCollapsed());
                } catch (PropertyVetoException e) {
                    OPDE.error(e);
                }
            }
        });
        titlePanelleft.add(title);


        JPanel titlePanelright = new JPanel();
        titlePanelright.setLayout(new BoxLayout(titlePanelright, BoxLayout.LINE_AXIS));


        /***
         *      ____        _   _                   _       _     _
         *     | __ ) _   _| |_| |_ ___  _ __      / \   __| | __| |
         *     |  _ \| | | | __| __/ _ \| '_ \    / _ \ / _` |/ _` |
         *     | |_) | |_| | |_| || (_) | | | |  / ___ \ (_| | (_| |
         *     |____/ \__,_|\__|\__\___/|_| |_| /_/   \_\__,_|\__,_|
         *
         */
        if (OPDE.getAppInfo().userHasAccessLevelForThisClass(internalClassID, InternalClassACL.INSERT)) { // => ACL_MATRIX
            JButton btnAdd = new JButton(SYSConst.icon22add);
            btnAdd.setPressedIcon(SYSConst.icon22addPressed);
            btnAdd.setAlignmentX(Component.RIGHT_ALIGNMENT);
            btnAdd.setContentAreaFilled(false);
            btnAdd.setBorder(null);
            btnAdd.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {

                    new DlgPlanung(planung.clone(), new Closure() {
                        @Override
                        public void execute(Object o) {
                            if (o != null) {
                                EntityManager em = OPDE.createEM();
                                try {
                                    em.getTransaction().begin();
                                    em.lock(em.merge(bewohner), LockModeType.OPTIMISTIC);
                                    em.lock(em.merge(planung), LockModeType.OPTIMISTIC);
                                    // Fetch the new Plan from the PAIR
                                    NursingProcess myNewNP = em.merge(((Pair<NursingProcess, ArrayList<InterventionSchedule>>) o).getFirst());
                                    // Close old NP
                                    NursingProcessTools.close(planung, myNewNP.getSituation());
                                    // Create new DFNs according to plan
                                    DFNTools.generate(em, myNewNP.getInterventionSchedule(), new DateMidnight(), true);
                                    em.getTransaction().commit();
                                    // Refresh Display
                                    addNursingProcessToDisplay(myNewNP);
                                    reloadDisplay();
                                } catch (OptimisticLockException ole) {
                                    if (em.getTransaction().isActive()) {
                                        em.getTransaction().rollback();
                                    }
                                    if (ole.getMessage().indexOf("Class> entity.Bewohner") > -1) {
                                        OPDE.getMainframe().emptyFrame();
                                        OPDE.getMainframe().afterLogin();
                                    }
                                    OPDE.getDisplayManager().addSubMessage(DisplayManager.getLockMessage());
                                } catch (Exception e) {
                                    if (em.getTransaction().isActive()) {
                                        em.getTransaction().rollback();
                                    }
                                    OPDE.fatal(e);
                                } finally {
                                    em.close();
                                }
//                                reloadTable();
                            }
                        }
                    });

                }
            });
            titlePanelright.add(btnAdd);
            btnAdd.setEnabled(!planung.isAbgesetzt());
        }

        /***
         *      ____        _   _                _____    _ _ _
         *     | __ ) _   _| |_| |_ ___  _ __   | ____|__| (_) |_
         *     |  _ \| | | | __| __/ _ \| '_ \  |  _| / _` | | __|
         *     | |_) | |_| | |_| || (_) | | | | | |__| (_| | | |_
         *     |____/ \__,_|\__|\__\___/|_| |_| |_____\__,_|_|\__|
         *
         */
        if (OPDE.getAppInfo().userHasAccessLevelForThisClass(internalClassID, InternalClassACL.UPDATE)) {  // => ACL_MATRIX
            JButton btnEdit = new JButton(SYSConst.icon22edit);
            btnEdit.setPressedIcon(SYSConst.icon22editPressed);
            btnEdit.setAlignmentX(Component.RIGHT_ALIGNMENT);
            btnEdit.setContentAreaFilled(false);
            btnEdit.setBorder(null);
            btnEdit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    new DlgPlanung(planung, new Closure() {
                        @Override
                        public void execute(Object planung) {
                            if (planung != null) {
                                EntityManager em = OPDE.createEM();
                                try {
                                    em.getTransaction().begin();
                                    em.lock(em.merge(bewohner), LockModeType.OPTIMISTIC);
                                    NursingProcess mynp = em.merge(((Pair<NursingProcess, ArrayList<InterventionSchedule>>) planung).getFirst());
                                    em.lock(mynp, LockModeType.OPTIMISTIC);
                                    // Schedules to delete
                                    for (InterventionSchedule is : ((Pair<NursingProcess, ArrayList<InterventionSchedule>>) planung).getSecond()) {
                                        em.remove(em.merge(is));
                                    }
                                    // No unused DFNs to delete
                                    Query delQuery = em.createQuery("DELETE FROM DFN dfn WHERE dfn.nursingProcess = :nursingprocess ");
                                    delQuery.setParameter("nursingprocess", mynp);
                                    delQuery.executeUpdate();
                                    // Create new DFNs according to plan
                                    DFNTools.generate(em, mynp.getInterventionSchedule(), new DateMidnight(), true);
                                    em.getTransaction().commit();
                                    Collections.sort(planungen.get(mynp.getKategorie()));
                                    reloadDisplay();
                                } catch (OptimisticLockException ole) {
                                    if (em.getTransaction().isActive()) {
                                        em.getTransaction().rollback();
                                    }
                                    if (ole.getMessage().indexOf("Class> entity.Bewohner") > -1) {
                                        OPDE.getMainframe().emptyFrame();
                                        OPDE.getMainframe().afterLogin();
                                    }
                                    OPDE.getDisplayManager().addSubMessage(DisplayManager.getLockMessage());
                                } catch (Exception e) {
                                    if (em.getTransaction().isActive()) {
                                        em.getTransaction().rollback();
                                    }
                                    OPDE.fatal(e);
                                } finally {
                                    em.close();
                                }
//                                reloadTable();
                            }
                        }
                    });
                }
            });
            titlePanelright.add(btnEdit);
            btnEdit.setEnabled(!planung.isAbgesetzt() && numDFNs == 0);
        }


        /***
         *      ____        _   _                ____  _
         *     | __ ) _   _| |_| |_ ___  _ __   / ___|| |_ ___  _ __
         *     |  _ \| | | | __| __/ _ \| '_ \  \___ \| __/ _ \| '_ \
         *     | |_) | |_| | |_| || (_) | | | |  ___) | || (_) | |_) |
         *     |____/ \__,_|\__|\__\___/|_| |_| |____/ \__\___/| .__/
         *                                                     |_|
         */
        if (OPDE.getAppInfo().userHasAccessLevelForThisClass(internalClassID, InternalClassACL.CANCEL)) { // => ACL_MATRIX
            JButton btnStop = new JButton(SYSConst.icon22stop);
            btnStop.setPressedIcon(SYSConst.icon22stopPressed);
            btnStop.setAlignmentX(Component.RIGHT_ALIGNMENT);
            btnStop.setContentAreaFilled(false);
            btnStop.setBorder(null);
            btnStop.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {

                }
            });
            btnStop.setEnabled(!planung.isAbgesetzt());
            titlePanelright.add(btnStop);
        }

        /***
         *      ____        _   _                ____       _      _
         *     | __ ) _   _| |_| |_ ___  _ __   |  _ \  ___| | ___| |_ ___
         *     |  _ \| | | | __| __/ _ \| '_ \  | | | |/ _ \ |/ _ \ __/ _ \
         *     | |_) | |_| | |_| || (_) | | | | | |_| |  __/ |  __/ ||  __/
         *     |____/ \__,_|\__|\__\___/|_| |_| |____/ \___|_|\___|\__\___|
         *
         */
        if (OPDE.getAppInfo().userHasAccessLevelForThisClass(internalClassID, InternalClassACL.DELETE)) {  // => ACL_MATRIX
            JButton btnDelete = new JButton(SYSConst.icon22delete);
            btnDelete.setPressedIcon(SYSConst.icon22deletePressed);
            btnDelete.setAlignmentX(Component.RIGHT_ALIGNMENT);
            btnDelete.setContentAreaFilled(false);
            btnDelete.setBorder(null);
            btnDelete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {

                }
            });
            btnDelete.setEnabled(!planung.isAbgesetzt() && numDFNs == 0);
            titlePanelright.add(btnDelete);
        }


        titlePanelleft.setOpaque(false);
        titlePanelright.setOpaque(false);
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);


        titlePanel.setLayout(new GridBagLayout());
        ((GridBagLayout) titlePanel.getLayout()).columnWidths = new int[]{0, 80};
        ((GridBagLayout) titlePanel.getLayout()).columnWeights = new double[]{1.0, 1.0};

        titlePanel.add(titlePanelleft, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 0, 5), 0, 0));

        titlePanel.add(titlePanelright, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 0, 0), 0, 0));

//        titlePanel.add(titlePanelleft);
//        titlePanel.add(titlePanelright);


        panelForPlanung.setTitleLabelComponent(titlePanel);
        panelForPlanung.setSlidingDirection(SwingConstants.SOUTH);
//            panelForPlanung.setStyle(CollapsiblePane.TREE_STYLE);
//            panelForPlanung.setHorizontalAlignment(SwingConstants.LEADING);

//            panelForBWInfoTyp.setEmphasized(bwinfos.get(typ).isEmpty());

//            JPanel contentPanel = new JPanel();
//            contentPanel.setLayout(new VerticalLayout());

        /***
         *       ___ ___  _  _ _____ ___ _  _ _____
         *      / __/ _ \| \| |_   _| __| \| |_   _|
         *     | (_| (_) | .` | | | | _|| .` | | |
         *      \___\___/|_|\_| |_| |___|_|\_| |_|
         *
         */
        JTextPane contentPane = new JTextPane();
        contentPane.setContentType("text/html");
        contentPane.setText(SYSTools.toHTML(NursingProcessTools.getAsHTML(planung, false)));
        panelForPlanung.setContentPane(contentPane);
        try {
            panelForPlanung.setCollapsed(true);
        } catch (PropertyVetoException e) {
            OPDE.error(e);
        }

        panelForPlanung.setSlidingDirection(SwingConstants.SOUTH);
        panelForPlanung.setHorizontalAlignment(SwingConstants.LEADING);
        panelForPlanung.setBackground(planung.isAbgesetzt() ? Color.lightGray : planung.getKategorie().getBackgroundContent());
        panelForPlanung.setForeground(planung.isAbgesetzt() ? SYSConst.grey80 : planung.getKategorie().getForegroundContent());
        panelForPlanung.setOpaque(false);

        panelForPlanung.setVisible(tbInactive.isSelected() || !planung.isAbgesetzt());

        return panelForPlanung;
    }

    public void refreshDisplay() {
        for (BWInfoKat kat : kategorien) {
            for (NursingProcess planung : planungen.get(kat)) {
                planungCollapsiblePaneMap.get(planung).setVisible(tbInactive.isSelected() || !planung.isAbgesetzt());
            }
        }
    }

    private void prepareSearchArea() {
        searchPanes = new CollapsiblePanes();
        searchPanes.setLayout(new JideBoxLayout(searchPanes, JideBoxLayout.Y_AXIS));
        jspSearch.setViewportView(searchPanes);

        JPanel mypanel = new JPanel();
        mypanel.setLayout(new VerticalLayout(5));
        mypanel.setBackground(Color.WHITE);

        CollapsiblePane searchPane = new CollapsiblePane(OPDE.lang.getString(internalClassID));
        searchPane.setStyle(CollapsiblePane.PLAIN_STYLE);
        searchPane.setCollapsible(false);

        try {
            searchPane.setCollapsed(false);
        } catch (PropertyVetoException e) {
            OPDE.error(e);
        }

        GUITools.addAllComponents(mypanel, addCommands());
        GUITools.addAllComponents(mypanel, addFilters());

        searchPane.setContentPane(mypanel);

        searchPanes.add(searchPane);
        searchPanes.addExpansion();
    }

    private String getHyperlinkButtonTextFor(NursingProcess planung) {
        String result = "<b>" + planung.getStichwort() + "</b> ";

        if (planung.isAbgesetzt()) {
            result += DateFormat.getDateInstance().format(planung.getVon()) + " &rarr; " + DateFormat.getDateInstance().format(planung.getBis());
        } else {
            result += DateFormat.getDateInstance().format(planung.getVon()) + " &rarr;| ";
        }

        return SYSTools.toHTMLForScreen(result);
    }


    private List<Component> addFilters() {
        List<Component> list = new ArrayList<Component>();

        txtSearch = new JXSearchField(OPDE.lang.getString("misc.msg.searchphrase"));
        txtSearch.setInstantSearchDelay(750);
        txtSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        list.add(txtSearch);

        tbInactive = GUITools.getNiceToggleButton(OPDE.lang.getString(internalClassID + ".inactive"));
        tbInactive.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (initPhase) return;
                SYSPropsTools.storeState(internalClassID + ":tbInactive", tbInactive);
                refreshDisplay();
            }
        });
        tbInactive.setHorizontalAlignment(SwingConstants.LEFT);
        list.add(tbInactive);
        SYSPropsTools.restoreState(internalClassID + ":tbInactive", tbInactive);
        return list;
    }

    private void addNursingProcessToDisplay(NursingProcess np) {
//        private HashMap<NursingProcess, CollapsiblePane> planungCollapsiblePaneMap;
//    private HashMap<BWInfoKat, java.util.List<NursingProcess>> planungen;

        if (!planungen.containsKey(np.getKategorie())) {
            planungen.put(np.getKategorie(), new ArrayList<NursingProcess>());
        }
        planungen.get(np.getKategorie()).add(np);
        Collections.sort(planungen.get(np.getKategorie()));

        if (!kategorien.contains(np.getKategorie())) {
            kategorien.add(np.getKategorie());
        }
        Collections.sort(kategorien);
    }

    private List<Component> addCommands() {

        List<Component> list = new ArrayList<Component>();

        /***
         *      _   _
         *     | \ | | _____      __
         *     |  \| |/ _ \ \ /\ / /
         *     | |\  |  __/\ V  V /
         *     |_| \_|\___| \_/\_/
         *
         */
        if (OPDE.getAppInfo().userHasAccessLevelForThisClass(internalClassID, InternalClassACL.INSERT)) {
            JideButton addButton = GUITools.createHyperlinkButton(OPDE.lang.getString("misc.commands.new"), new ImageIcon(getClass().getResource("/artwork/22x22/bw/add.png")), new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    new DlgPlanung(new NursingProcess(bewohner), new Closure() {
                        @Override
                        public void execute(Object planung) {
                            if (planung != null) {
                                EntityManager em = OPDE.createEM();
                                try {
                                    em.getTransaction().begin();
                                    em.lock(em.merge(bewohner), LockModeType.OPTIMISTIC);
                                    Unique unique = UniqueTools.getNewUID(em, NursingProcessTools.UNIQUEID);
                                    NursingProcess myplan = em.merge((NursingProcess) planung);
                                    myplan.setPlanKennung(unique.getUid());
                                    DFNTools.generate(em, myplan.getInterventionSchedule(), new DateMidnight(), true);
                                    em.getTransaction().commit();
                                    addNursingProcessToDisplay(myplan);
                                    reloadDisplay();
                                } catch (OptimisticLockException ole) {
                                    if (em.getTransaction().isActive()) {
                                        em.getTransaction().rollback();
                                    }
                                    if (ole.getMessage().indexOf("Class> entity.Bewohner") > -1) {
                                        OPDE.getMainframe().emptyFrame();
                                        OPDE.getMainframe().afterLogin();
                                    }
                                    OPDE.getDisplayManager().addSubMessage(DisplayManager.getLockMessage());
                                } catch (Exception e) {
                                    if (em.getTransaction().isActive()) {
                                        em.getTransaction().rollback();
                                    }
                                    OPDE.fatal(e);
                                } finally {
                                    em.close();
                                }
                            }
                        }
                    });
                }
            });
            list.add(addButton);
        }


        return list;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JScrollPane jspPlanung;
    private CollapsiblePanes cpPlan;
    // End of variables declaration//GEN-END:variables
}
