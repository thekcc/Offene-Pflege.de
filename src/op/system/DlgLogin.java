/*
 * OffenePflege
 * Copyright (C) 2006-2012 Torsten Löhr
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
 */
package op.system;

import java.awt.event.*;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;
import entity.files.SYSFilesTools;
import entity.system.SYSLoginTools;
import entity.system.SYSProps;
import entity.system.SYSPropsTools;
import op.OPDE;
import op.threads.DisplayMessage;
import op.tools.MyJDialog;
import op.tools.SYSTools;
import org.apache.commons.collections.Closure;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author __USER__
 */
public class DlgLogin extends MyJDialog {

    public static final String internalClassID = "dlglogin";

    private Closure actionBlock;

    private void btnExitActionPerformed(ActionEvent e) {
        dispose();
    }

    private void thisWindowActivated(WindowEvent e) {
        txtUsername.requestFocus();
    }

    public DlgLogin(Closure actionBlock) {
        super(false);
        OPDE.setLogin(null);

        this.actionBlock = actionBlock;

        initComponents();

        txtUsername.setText(SYSTools.catchNull(OPDE.getLocalProps().getProperty(SYSPropsTools.KEY_USER)));
        txtPassword.setText(SYSTools.catchNull(OPDE.getLocalProps().getProperty(SYSPropsTools.KEY_PASSWORD)));
        lblUsernamePassword.setText(SYSTools.xx("misc.msg.username") + "/" + SYSTools.xx("misc.msg.password"));

        setVisible(true);
    }



    /**
     * This method is called from within the constructor to
     * initialize the printerForm.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the PrinterForm Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel2 = new JPanel();
        lblOPDE = new JLabel();
        btnAbout = new JButton();
        lblUsernamePassword = new JLabel();
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        panel1 = new JPanel();
        btnExit = new JButton();
        hSpacer1 = new JPanel(null);
        btnLogin = new JButton();

        //======== this ========
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                thisWindowActivated(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(new FormLayout(
            "13dlu, default, 13dlu",
            "13dlu, $lgap, fill:48dlu:grow, $lgap, default, $lgap, 13dlu"));

        //======== jPanel2 ========
        {
            jPanel2.setBorder(new EmptyBorder(5, 5, 5, 5));
            jPanel2.setOpaque(false);
            jPanel2.setLayout(new VerticalLayout(10));

            //---- lblOPDE ----
            lblOPDE.setText("Offene-Pflege.de");
            lblOPDE.setFont(new Font("Arial", Font.PLAIN, 24));
            lblOPDE.setHorizontalAlignment(SwingConstants.CENTER);
            jPanel2.add(lblOPDE);

            //---- btnAbout ----
            btnAbout.setIcon(new ImageIcon(getClass().getResource("/artwork/256x256/opde-logo.png")));
            btnAbout.setBorderPainted(false);
            btnAbout.setBorder(null);
            btnAbout.setOpaque(false);
            btnAbout.setContentAreaFilled(false);
            btnAbout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnAbout.setToolTipText(null);
            btnAbout.addActionListener(e -> btnAboutActionPerformed(e));
            jPanel2.add(btnAbout);

            //---- lblUsernamePassword ----
            lblUsernamePassword.setText("text");
            lblUsernamePassword.setFont(new Font("Arial", Font.PLAIN, 18));
            jPanel2.add(lblUsernamePassword);

            //---- txtUsername ----
            txtUsername.setFont(new Font("Arial", Font.PLAIN, 18));
            txtUsername.addActionListener(e -> txtUsernameActionPerformed(e));
            txtUsername.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    txtUsernameFocusGained(e);
                }
            });
            jPanel2.add(txtUsername);

            //---- txtPassword ----
            txtPassword.setFont(new Font("Arial", Font.PLAIN, 18));
            txtPassword.addActionListener(e -> txtPasswordActionPerformed(e));
            txtPassword.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    txtPasswordFocusGained(e);
                }
            });
            jPanel2.add(txtPassword);
        }
        contentPane.add(jPanel2, CC.xy(2, 3, CC.FILL, CC.DEFAULT));

        //======== panel1 ========
        {
            panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));

            //---- btnExit ----
            btnExit.setIcon(new ImageIcon(getClass().getResource("/artwork/22x22/exit.png")));
            btnExit.addActionListener(e -> btnExitActionPerformed(e));
            panel1.add(btnExit);
            panel1.add(hSpacer1);

            //---- btnLogin ----
            btnLogin.setIcon(new ImageIcon(getClass().getResource("/artwork/22x22/apply.png")));
            btnLogin.setActionCommand("btnLogin");
            btnLogin.addActionListener(e -> DoLogin(e));
            panel1.add(btnLogin);
        }
        contentPane.add(panel1, CC.xy(2, 5, CC.RIGHT, CC.DEFAULT));
        setSize(320, 540);
        setLocationRelativeTo(getOwner());
    }// </editor-fold>//GEN-END:initComponents

    private void txtPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordFocusGained
        ((JTextField) evt.getSource()).selectAll();
    }//GEN-LAST:event_txtPasswordFocusGained

    private void txtUsernameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsernameFocusGained
        ((JTextField) evt.getSource()).selectAll();
    }//GEN-LAST:event_txtUsernameFocusGained


    private void txtPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPasswordActionPerformed
        btnLogin.doClick();
    }//GEN-LAST:event_txtPasswordActionPerformed

    @Override
    public void dispose() {
        actionBlock.execute(OPDE.getLogin());
//        SYSTools.unregisterListeners(this);
        super.dispose();
    }

    private void txtUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsernameActionPerformed
        txtPassword.requestFocus();
    }//GEN-LAST:event_txtUsernameActionPerformed

    private void DoLogin(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DoLogin

        if (SYSPropsTools.isTrue(SYSPropsTools.KEY_MAINTENANCE_MODE, null)){
            OPDE.getDisplayManager().addSubMessage(new DisplayMessage("dlglogin.maintenance.mode", DisplayMessage.IMMEDIATELY, 5));
            return;
        }

        String username = txtUsername.getText().trim();

        try {
            registerLogin();
            if (OPDE.getLogin() == null) {
                OPDE.getDisplayManager().addSubMessage(new DisplayMessage("misc.msg.usernameOrPasswordWrong"));
                OPDE.info(SYSTools.xx("misc.msg.usernameOrPasswordWrong") + ": " + username + "  " + SYSTools.xx("misc.msg.triedPassword") + ": " + new String(txtPassword.getPassword()));
            } else {
                OPDE.initProps();
                OPDE.info("Login: " + username + "  LoginID: " + OPDE.getLogin().getLoginID());
                dispose();
            }

        } catch (Exception se) {
            OPDE.fatal(se);
            System.exit(1);
        }
    }//GEN-LAST:event_DoLogin

    private void btnAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAboutActionPerformed
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI("http://www.offene-pflege.de"));
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (URISyntaxException use) {
                use.printStackTrace();

            }
        }
    }//GEN-LAST:event_btnAboutActionPerformed

    private void registerLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        OPDE.setLogin(SYSLoginTools.login(username, password));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JPanel jPanel2;
    private JLabel lblOPDE;
    private JButton btnAbout;
    private JLabel lblUsernamePassword;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPanel panel1;
    private JButton btnExit;
    private JPanel hSpacer1;
    private JButton btnLogin;
    // End of variables declaration//GEN-END:variables
}
