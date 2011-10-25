package entity;

import op.OPDE;

import javax.persistence.Query;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: tloehr
 * Date: 24.10.11
 * Time: 16:33
 * To change this template use File | Settings | File Templates.
 */
public class BWInfoTypTools {

    public static BWInfoTyp findByBWINFTYP(String bwinftyp) {
        Query query = OPDE.getEM().createNamedQuery("BWInfoTyp.findByBwinftyp");
        query.setParameter("bwinftyp", bwinftyp);
        List<BWInfoTyp> bwInfoTyps = query.getResultList();
        return bwInfoTyps.isEmpty() ? null : bwInfoTyps.get(0);
    }

}