package entity.qms;

import io.lamma.LammaConst;
import io.lamma.LammaConversion;
import io.lamma.Recurrence;
import op.OPDE;
import op.tools.SYSConst;
import op.tools.SYSTools;
import org.joda.time.LocalDate;

import java.text.DateFormat;

/**
 * Created by tloehr on 17.06.14.
 */
public class QmsschedTools {

    public static String getAsHTML(Qmssched qmssched) {
        String result = "";

        result += SYSTools.catchNull(qmssched.getText()).isEmpty() ? "" : SYSConst.html_paragraph(SYSConst.html_bold(OPDE.lang.getString("misc.msg.comment") + ": " + qmssched.getText()));

        String wdh = getRepeatPattern(qmssched);
        result += SYSConst.html_paragraph(qmssched.hasTime() ? DateFormat.getTimeInstance(DateFormat.SHORT).format(qmssched.getTime()) + " " + OPDE.lang.getString("misc.msg.Time.short") + ", " + wdh : wdh);

        if (qmssched.getStation() != null) {
            result += SYSConst.html_paragraph(OPDE.lang.getString("misc.msg.station") + ": " + qmssched.getStation().getName() + ", " + qmssched.getStation().getHome().getName());
        } else if (qmssched.getHome() != null) {
            result += SYSConst.html_paragraph(OPDE.lang.getString("misc.msg.home") + ": " + qmssched.getHome().getName());
        }

//        result += "</table>";


        return result;
    }

    public static String getRepeatPattern(Qmssched qmssched) {
        String result = "";

        if (qmssched.isDaily()) {
            if (qmssched.getDaily() > 1) {
                result += OPDE.lang.getString("misc.msg.every") + " " + qmssched.getDaily() + " " + OPDE.lang.getString("misc.msg.Days2");
            } else {
                result += OPDE.lang.getString("misc.msg.everyDay");
            }
        } else if (qmssched.isWeekly()) {
            if (qmssched.getWeekly() == 1) {
                result += result += OPDE.lang.getString("misc.msg.everyWeek");
            } else {
                result += OPDE.lang.getString("misc.msg.every") + " " + qmssched.getWeekly() + " " + OPDE.lang.getString("misc.msg.weeks");
            }

            String daylist = "";

            daylist += (qmssched.getMon() > 0 ? OPDE.lang.getString("misc.msg.monday").substring(0, 3) + ", " : "");
            daylist += (qmssched.getTue() > 0 ? OPDE.lang.getString("misc.msg.tuesday").substring(0, 3) + ", " : "");
            daylist += (qmssched.getWed() > 0 ? OPDE.lang.getString("misc.msg.wednesday").substring(0, 3) + ", " : "");
            daylist += (qmssched.getThu() > 0 ? OPDE.lang.getString("misc.msg.thursday").substring(0, 3) + ", " : "");
            daylist += (qmssched.getFri() > 0 ? OPDE.lang.getString("misc.msg.friday").substring(0, 3) + ", " : "");
            daylist += (qmssched.getSat() > 0 ? OPDE.lang.getString("misc.msg.saturday").substring(0, 3) + ", " : "");
            daylist += (qmssched.getSun() > 0 ? OPDE.lang.getString("misc.msg.sunday").substring(0, 3) + ", " : "");

            if (!daylist.isEmpty()) {
                result += " {" + daylist.substring(0, daylist.length() - 2) + "}";
            }

        } else if (qmssched.isMonthly()) {
            if (qmssched.getMonthly() == 1) {
                result += OPDE.lang.getString("misc.msg.everyMonth") + " ";
            } else {
                result += OPDE.lang.getString("misc.msg.every") + " " + qmssched.getMonthly() + " " + OPDE.lang.getString("misc.msg.months") + " ";
            }

            if (qmssched.getDaynum() > 0) {
                result += OPDE.lang.getString("misc.msg.atchrono") + " " + qmssched.getDaynum() + ". " + OPDE.lang.getString("misc.msg.ofTheMonth");
                //                result += "jeweils am " + schedule.getTagNum() + ". des Monats";
            } else {
                int wtag = 0;
                String tag = "";
                tag += (qmssched.getMon() > 0 ? OPDE.lang.getString("misc.msg.monday") : "");
                tag += (qmssched.getTue() > 0 ? OPDE.lang.getString("misc.msg.tuesday") : "");
                tag += (qmssched.getWed() > 0 ? OPDE.lang.getString("misc.msg.wednesday") : "");
                tag += (qmssched.getThu() > 0 ? OPDE.lang.getString("misc.msg.thursday") : "");
                tag += (qmssched.getFri() > 0 ? OPDE.lang.getString("misc.msg.friday") : "");
                tag += (qmssched.getSat() > 0 ? OPDE.lang.getString("misc.msg.saturday") : "");
                tag += (qmssched.getSun() > 0 ? OPDE.lang.getString("misc.msg.sunday") : "");

                // In this case, only one of the below can be >0. So this will work.
                wtag += qmssched.getMon();
                wtag += qmssched.getTue();
                wtag += qmssched.getWed();
                wtag += qmssched.getThu();
                wtag += qmssched.getFri();
                wtag += qmssched.getSat();
                wtag += qmssched.getSun();

                result += OPDE.lang.getString("misc.msg.atchrono") + " " + wtag + ". " + tag + " " + OPDE.lang.getString("misc.msg.ofTheMonth");
            }
        } else {
            result = "";
        }

        LocalDate ldatum = new LocalDate(qmssched.getlDate());
        LocalDate today = new LocalDate();

        if (ldatum.compareTo(today) > 0) { // Die erste Ausführung liegt in der Zukunft
            result += OPDE.lang.getString("opde.controlling.qms.dlgqmsplan.pnlschedule.ldate") + ": " + DateFormat.getDateInstance().format(qmssched.getlDate());
        }

        return result;
    }


    /**
     * takes the recurrence pattern inside a qmssched and creates a list of recurrences for a lamma sequence generator.
     *
     * @param qmssched
     * @return
     */
    public static Recurrence getRecurrence(Qmssched qmssched) {

        Recurrence recurrences = null;

        if (qmssched.isDaily()) {
            recurrences = LammaConversion.days(qmssched.getDaily());
        } else if (qmssched.isWeekly()) {
            if (qmssched.getMon() > 0) {
                recurrences = LammaConversion.weeks(qmssched.getWeekly(), LammaConst.MONDAY);
            } else if (qmssched.getTue() > 0) {
                recurrences = LammaConversion.weeks(qmssched.getWeekly(), LammaConst.TUESDAY);
            } else if (qmssched.getWed() > 0) {
                recurrences = LammaConversion.weeks(qmssched.getWeekly(), LammaConst.WEDNESDAY);
            } else if (qmssched.getThu() > 0) {
                recurrences = LammaConversion.weeks(qmssched.getWeekly(), LammaConst.THURSDAY);
            } else if (qmssched.getFri() > 0) {
                recurrences = LammaConversion.weeks(qmssched.getWeekly(), LammaConst.FRIDAY);
            } else if (qmssched.getSat() > 0) {
                recurrences = LammaConversion.weeks(qmssched.getWeekly(), LammaConst.SATURDAY);
            } else if (qmssched.getSun() > 0) {
                recurrences = LammaConversion.weeks(qmssched.getWeekly(), LammaConst.SUNDAY);
            }
        } else if (qmssched.isMonthly()) {
            if (qmssched.getDaynum() > 0) {
                recurrences = LammaConversion.months(qmssched.getMonthly(), LammaConversion.nthDayOfMonth(qmssched.getDaynum()));
            } else {
                if (qmssched.getMon() > 0) {
                    recurrences = LammaConversion.months(qmssched.getMonthly(), LammaConversion.nthWeekdayOfMonth(qmssched.getMon(), LammaConst.MONDAY));
                } else if (qmssched.getTue() > 0) {
                    recurrences = LammaConversion.months(qmssched.getMonthly(), LammaConversion.nthWeekdayOfMonth(qmssched.getTue(), LammaConst.TUESDAY));
                } else if (qmssched.getWed() > 0) {
                    recurrences = LammaConversion.months(qmssched.getMonthly(), LammaConversion.nthWeekdayOfMonth(qmssched.getWed(), LammaConst.WEDNESDAY));
                } else if (qmssched.getThu() > 0) {
                    recurrences = LammaConversion.months(qmssched.getMonthly(), LammaConversion.nthWeekdayOfMonth(qmssched.getThu(), LammaConst.THURSDAY));
                } else if (qmssched.getFri() > 0) {
                    recurrences = LammaConversion.months(qmssched.getMonthly(), LammaConversion.nthWeekdayOfMonth(qmssched.getFri(), LammaConst.FRIDAY));
                } else if (qmssched.getSat() > 0) {
                    recurrences = LammaConversion.months(qmssched.getMonthly(), LammaConversion.nthWeekdayOfMonth(qmssched.getSat(), LammaConst.SATURDAY));
                } else if (qmssched.getSun() > 0) {
                    recurrences = LammaConversion.months(qmssched.getMonthly(), LammaConversion.nthWeekdayOfMonth(qmssched.getSun(), LammaConst.SUNDAY));
                }
            }
        }
        return recurrences;
    }

}
