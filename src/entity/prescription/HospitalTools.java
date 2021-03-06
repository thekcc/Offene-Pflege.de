package entity.prescription;

import op.OPDE;
import op.tools.SYSTools;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: tloehr
 * Date: 14.12.11
 * Time: 13:30
 * To change this template use File | Settings | File Templates.
 */
public class HospitalTools {

    public static ListCellRenderer getKHRenderer() {
        return (jList, o, i, isSelected, cellHasFocus) -> {
            String text;
            if (o == null) {
                text = SYSTools.xx("misc.commands.>>noselection<<");
            } else if (o instanceof Hospital) {
                text = ((Hospital) o).getName() + ", " + ((Hospital) o).getCity();
            } else {
                text = o.toString();
            }
            return new DefaultListCellRenderer().getListCellRendererComponent(jList, text, i, isSelected, cellHasFocus);
        };
    }


    public static String getCompleteAddress(Hospital kh) {
        if (kh != null) {
            if (OPDE.isAnonym()) {
                return "[" + SYSTools.xx("misc.msg.anon") + "]";
            }
            return kh.getName() + ", " + kh.getStreet() + ", " + kh.getCity() + ", Tel: " + kh.getTel();
        } else {
            return SYSTools.xx("misc.msg.noentryyet");
        }
    }

    public static String getFullName(Hospital kh) {
        if (kh == null)
            return "--";


        if (OPDE.isAnonym()) {
            return "[" + SYSTools.xx("misc.msg.anon") + "]";
        }

        String string = kh.getName() + SYSTools.catchNull(kh.getCity(), ", ", "");
//        string += SYSTools.catchNull(kh.getTel(), SYSTools.xx("misc.msg.phone") + ": ", " ") + SYSTools.catchNull(kh.getFax(), SYSTools.xx("misc.msg.fax") + ": ", " ");
//        String string = kh.getName() + ", " + SYSTools.catchNull(kh.getStreet(), "", ", ") + SYSTools.catchNull(kh.getZIP(), "", " ") + SYSTools.catchNull(kh.getCity(), "", ", ");
//        string += SYSTools.catchNull(kh.getTel(), SYSTools.xx("misc.msg.phone") + ": ", " ") + SYSTools.catchNull(kh.getFax(), SYSTools.xx("misc.msg.fax") + ": ", " ");
        return string;
    }
}
