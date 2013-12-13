package entity.roster;


import op.OPDE;
import op.tools.Pair;
import op.tools.SYSCalendar;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created with IntelliJ IDEA.
 * User: tloehr
 * Date: 22.07.13
 * Time: 15:08
 * To change this template use File | Settings | File Templates.
 */
public class ContractsParameterSet implements Cloneable, Comparable<ContractsParameterSet> {

    LocalDate from, to;
    Pair<LocalTime, LocalTime> night;
    BigDecimal vacationDaysPerYear, wagePerHour, workingDaysPerWeek, targetHoursPerMonth, holidayPremiumPercentage, nightPremiumPercentage;
    String section;
    boolean trainee;


    public ContractsParameterSet() {
        this(SYSCalendar.eom(new LocalDate()).plusDays(1), SYSCalendar.eom(new LocalDate()).plusDays(1).plusYears(1).minusDays(1), new Pair<LocalTime, LocalTime>(new LocalTime(23, 0), new LocalTime(6, 0)), new BigDecimal(30),
                new BigDecimal(8.5d), new BigDecimal(6), new BigDecimal(158), new BigDecimal(15), new BigDecimal(25), RosterXML.sections[RosterXML.CARE], false, false);

    }

    public ContractsParameterSet(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;

        trainee = false;
        exam = false;
    }

    private ContractsParameterSet(LocalDate from, LocalDate to, Pair<LocalTime, LocalTime> night, BigDecimal vacationDaysPerYear, BigDecimal wagePerHour, BigDecimal workingDaysPerWeek, BigDecimal targetHoursPerMonth, BigDecimal hollidayPremiumPercentage, BigDecimal nightPremiumPercentage, String section, boolean trainee, boolean exam) {
        this.from = from;
        this.to = to;
        this.night = night;
        this.vacationDaysPerYear = vacationDaysPerYear;
        this.wagePerHour = wagePerHour;
        this.workingDaysPerWeek = workingDaysPerWeek;
        this.targetHoursPerMonth = targetHoursPerMonth;
        this.holidayPremiumPercentage = hollidayPremiumPercentage;
        this.nightPremiumPercentage = nightPremiumPercentage;
        this.section = section;
        this.trainee = trainee;
        this.exam = exam;
    }

    public boolean hasSufficientParamters() {
        return night != null && from != null && to != null && vacationDaysPerYear != null && wagePerHour != null && workingDaysPerWeek != null && targetHoursPerMonth != null && holidayPremiumPercentage != null && nightPremiumPercentage != null && section != null;
    }

    @Override
    protected Object clone() {
        return new ContractsParameterSet(from, to, night, vacationDaysPerYear, wagePerHour, workingDaysPerWeek, targetHoursPerMonth, holidayPremiumPercentage, nightPremiumPercentage, section, trainee, exam);
    }

    public boolean isExam() {
        return exam;
    }

    public void setExam(boolean exam) {
        this.exam = exam;
    }

    boolean exam;

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public boolean isTrainee() {
        return trainee;
    }

    public void setTrainee(boolean trainee) {
        this.trainee = trainee;
    }

    /**
     * since when is/was this contract valid ?
     *
     * @return
     */
    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    /**
     * when did/does it stop ?
     *
     * @return
     */
    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }

    public Pair<LocalTime, LocalTime> getNight() {
        return night;
    }

    public void setNight(Pair<LocalTime, LocalTime> night) {
        this.night = night;
    }

    public BigDecimal getVacationDaysPerYear() {
        return vacationDaysPerYear;
    }

    public void setVacationDaysPerYear(BigDecimal vacationDaysPerYear) {
        this.vacationDaysPerYear = vacationDaysPerYear;
    }

    public BigDecimal getWagePerHour() {
        return wagePerHour;
    }

    public void setWagePerHour(BigDecimal wagePerHour) {
        this.wagePerHour = wagePerHour;
    }

    public BigDecimal getWorkingDaysPerWeek() {
        return workingDaysPerWeek;
    }

    public void setWorkingDaysPerWeek(BigDecimal workingDaysPerWeek) {
        this.workingDaysPerWeek = workingDaysPerWeek;
    }

    public BigDecimal getTargetHoursPerMonth() {
        return targetHoursPerMonth;
    }

    public void setTargetHoursPerMonth(BigDecimal targetHoursPerMonth) {
        this.targetHoursPerMonth = targetHoursPerMonth;
    }

    public BigDecimal getHolidayPremiumPercentage() {
        return holidayPremiumPercentage;
    }

    public void setHolidayPremiumPercentage(BigDecimal holidayPremiumPercentage) {
        this.holidayPremiumPercentage = holidayPremiumPercentage;
    }

    public BigDecimal getNightPremiumPercentage() {
        return nightPremiumPercentage;
    }

    public void setNightPremiumPercentage(BigDecimal nightPremiumPercentage) {
        this.nightPremiumPercentage = nightPremiumPercentage;
    }

    public int compareTo(ContractsParameterSet o) {
        return from.compareTo(o.getFrom());
    }

    public void setTargetHoursPerWeek(BigDecimal targetHoursPerWeek) {
        if (targetHoursPerWeek == null) {
            targetHoursPerMonth = null;
            return;
        }
        targetHoursPerMonth = targetHoursPerWeek.multiply(new BigDecimal(52)).divide(new BigDecimal(12), 2, RoundingMode.HALF_UP);
    }

    public void setTargetHoursPerDay(BigDecimal targetHoursPerDay) {
        if (targetHoursPerDay == null) {
            targetHoursPerMonth = null;
            return;
        }

        targetHoursPerMonth = targetHoursPerDay.multiply(workingDaysPerWeek).multiply(new BigDecimal(52)).divide(new BigDecimal(12), 4, RoundingMode.HALF_UP);
    }

    public BigDecimal getTargetHoursPerWeek() {
        if (targetHoursPerMonth == null) return null;
        return targetHoursPerMonth.multiply(new BigDecimal(12)).divide(new BigDecimal(52), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTargetHoursPerDay() {
        if (targetHoursPerMonth == null || workingDaysPerWeek == null) return null;
        return getTargetHoursPerWeek().divide(workingDaysPerWeek, 2, RoundingMode.HALF_UP);
    }


    public BigDecimal getDayValue() {
        return getTargetHoursPerWeek().divide(getWorkingDaysPerWeek(), 2, RoundingMode.HALF_UP);
    }

    /**
     * how many holiday extra hours for the specific month for this contract ?
     *
     * @param month
     * @return
     */
    public BigDecimal getHolidayHours(LocalDate month) {

        LocalDate myfrom = new LocalDate(Math.max(SYSCalendar.bom(month).toDateTimeAtStartOfDay().getMillis(), from.toDateTimeAtStartOfDay().getMillis()));
        LocalDate myto = new LocalDate(Math.min(SYSCalendar.eom(month).toDateTimeAtStartOfDay().getMillis(), to.toDateTimeAtStartOfDay().getMillis()));

        if (myfrom.compareTo(myto) > 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal holidayHours = BigDecimal.ZERO;

        for (LocalDate day = myfrom; day.compareTo(myto) <= 0; day = day.plusDays(1)) {
            if (day.getDayOfWeek() != DateTimeConstants.SUNDAY && OPDE.isHoliday(day)) {
                holidayHours = holidayHours.add(getDayValue());
            }
        }

        return holidayHours;
    }

}
