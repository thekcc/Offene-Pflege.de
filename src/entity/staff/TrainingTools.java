package entity.staff;

import entity.system.Commontags;
import op.OPDE;
import op.tools.Pair;
import op.tools.SYSCalendar;
import org.joda.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;

/**
 * Created by tloehr on 17.05.14.
 */
public class TrainingTools {

    /**
     * retrieves the PITs of the first and the last entry in the training table.
     *
     * @return
     */
    private static Pair<LocalDate, LocalDate> getMinMax() {
        Pair<LocalDate, LocalDate> result = null;

        EntityManager em = OPDE.createEM();
        Query queryMin = em.createQuery("SELECT t FROM Training t ORDER BY t.date ASC ");
        queryMin.setMaxResults(1);

        Query queryMax = em.createQuery("SELECT t FROM Training t ORDER BY t.date DESC ");
        queryMax.setMaxResults(1);

        try {
            ArrayList<Training> min = new ArrayList<Training>(queryMin.getResultList());
            ArrayList<Training> max = new ArrayList<Training>(queryMax.getResultList());
            if (min.isEmpty()) {
                result = null;
            } else {
                result = new Pair<LocalDate, LocalDate>(new LocalDate(min.get(0).getDate()), new LocalDate(max.get(0).getDate()));
            }

        } catch (Exception e) {
            OPDE.fatal(e);
        }

        em.close();
        return result;
    }

    public static Pair<LocalDate, LocalDate> getMinMax(Commontags commontag) {


        if (commontag == null){
            return getMinMax();
        }

        Pair<LocalDate, LocalDate> result = null;

        EntityManager em = OPDE.createEM();
        Query queryMin = em.createQuery("SELECT t FROM Training t WHERE :commontag MEMBER OF t.commontags ORDER BY t.date ASC ");
        queryMin.setParameter("commontag", commontag);
        queryMin.setMaxResults(1);

        Query queryMax = em.createQuery("SELECT t FROM Training t WHERE :commontag MEMBER OF t.commontags ORDER BY t.date DESC ");
        queryMax.setParameter("commontag", commontag);
        queryMax.setMaxResults(1);

        try {
            ArrayList<Training> min = new ArrayList<Training>(queryMin.getResultList());
            ArrayList<Training> max = new ArrayList<Training>(queryMax.getResultList());
            if (min.isEmpty()) {
                result = null;
            } else {
                result = new Pair<LocalDate, LocalDate>(new LocalDate(min.get(0).getDate()), new LocalDate(max.get(0).getDate()));
            }

        } catch (Exception e) {
            OPDE.fatal(e);
        }

        em.close();
        return result;
    }


    private static ArrayList<Training> getTrainings4(int iYear) {
        ArrayList<Training> list = new ArrayList<>();

        EntityManager em = OPDE.createEM();
        Query queryMin = em.createQuery("SELECT t FROM Training t WHERE t.date >= :from AND t.date <= :to ORDER BY t.date DESC ");
        queryMin.setParameter("from", SYSCalendar.boy(iYear).toDate());
        queryMin.setParameter("to", SYSCalendar.eoy(iYear).toDate());

        list.addAll(queryMin.getResultList());

        em.close();

        return list;

    }

    public static ArrayList<Training> getTrainings4(int iYear, Commontags commontag) {
        if (commontag == null) return getTrainings4(iYear);


        ArrayList<Training> list = new ArrayList<>();

        EntityManager em = OPDE.createEM();
        Query queryMin = em.createQuery("SELECT t FROM Training t WHERE :commontag MEMBER OF t.commontags AND t.date >= :from AND t.date <= :to ORDER BY t.date DESC ");
        queryMin.setParameter("commontag", commontag);
        queryMin.setParameter("from", SYSCalendar.boy(iYear).toDate());
        queryMin.setParameter("to", SYSCalendar.eoy(iYear).toDate());

        list.addAll(queryMin.getResultList());

        em.close();

        return list;

    }

}