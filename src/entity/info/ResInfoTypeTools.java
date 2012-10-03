package entity.info;

import op.OPDE;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: tloehr
 * Date: 24.10.11
 * Time: 16:33
 * To change this template use File | Settings | File Templates.
 */
public class ResInfoTypeTools {

    public static final int MODE_INTERVAL_BYSECOND = 0;
    public static final int MODE_INTERVAL_BYDAY = 1;
    public static final int MODE_INTERVAL_NOCONSTRAINTS = 2;
    public static final int MODE_INTERVAL_SINGLE_INCIDENTS = 3; // Das sind Ereignisse, bei denen von == bis gilt. Weitere Einschränkungen werden nicht gemacht.

    public static final int STATUS_INACTIVE_NORMAL = -1;
    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_SYSTEM = 10;
    public static final int STATUS_INACTIVE_SYSTEM = -10;

    public static final String TYP_DIAGNOSE = "DIAG";
    public static final String TYP_HEIMAUFNAHME = "HAUF";
    public static final String TYP_ABWESENHEIT = "ABWE1";
    public static final String TYP_KH_AUFENTHALT = "KH";

    public static final String ABWE_TYP_KH = "HOSPITAL";
    public static final String ABWE_TYP_HOLLIDAY = "HOLLIDAY";
    public static final String ABWE_TYP_OTHER = "OTHER";

    public static ResInfoType getByID(String id) {
        EntityManager em = OPDE.createEM();
        Query query = em.createQuery("SELECT b FROM ResInfoType b WHERE b.bwinftyp = :bwinftyp");
        query.setParameter("bwinftyp", id);
        List<ResInfoType> resInfoTypes = query.getResultList();
        em.close();
        return resInfoTypes.isEmpty() ? null : resInfoTypes.get(0);
    }

    public static List<ResInfoType> getByCat(ResInfoCategory category) {
        EntityManager em = OPDE.createEM();
        Query query = em.createQuery("SELECT b FROM ResInfoType b WHERE b.resInfoCat = :kat AND b.status >= 0 ORDER BY b.bWInfoKurz");
        query.setParameter("kat", category);
        List<ResInfoType> resInfoTypen = query.getResultList();
        em.close();
        return resInfoTypen;
    }

}
