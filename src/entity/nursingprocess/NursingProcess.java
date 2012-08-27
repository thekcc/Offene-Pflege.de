/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.nursingprocess;

import entity.Users;
import entity.info.BWInfoKat;
import entity.info.Resident;
import entity.process.QProcess;
import entity.process.QProcessElement;
import entity.process.SYSNP2PROCESS;
import op.OPDE;
import op.tools.SYSConst;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author tloehr
 */
@Entity
@Table(name = "Planung")
@NamedQueries({
        @NamedQuery(name = "Planung.findAll", query = "SELECT p FROM NursingProcess p"),
        @NamedQuery(name = "Planung.findByPlanID", query = "SELECT p FROM NursingProcess p WHERE p.planID = :planID"),
        @NamedQuery(name = "Planung.findByVorgang", query = " "
                + " SELECT p FROM NursingProcess p "
                + " JOIN p.attachedVorgaenge av"
                + " JOIN av.vorgang v"
                + " WHERE v = :vorgang "),
        @NamedQuery(name = "Planung.findByStichwort", query = "SELECT p FROM NursingProcess p WHERE p.stichwort = :stichwort"),
        @NamedQuery(name = "Planung.findByVon", query = "SELECT p FROM NursingProcess p WHERE p.von = :von"),
        @NamedQuery(name = "Planung.findByBis", query = "SELECT p FROM NursingProcess p WHERE p.bis = :bis"),
        @NamedQuery(name = "Planung.findByPlanKennung", query = "SELECT p FROM NursingProcess p WHERE p.planKennung = :planKennung"),
        @NamedQuery(name = "Planung.findByNKontrolle", query = "SELECT p FROM NursingProcess p WHERE p.nKontrolle = :nKontrolle")})
public class NursingProcess implements Serializable, QProcessElement, Comparable<NursingProcess>, Cloneable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PlanID")
    private Long planID;
    @Basic(optional = false)
    @Column(name = "Stichwort")
    private String stichwort;
    @Lob
    @Column(name = "Situation")
    private String situation;
    @Lob
    @Column(name = "Ziel")
    private String ziel;
    //    @Basic(optional = false)
//    @Column(name = "BWIKID")
//    private long bwikid;
    @Basic(optional = false)
    @Column(name = "Von")
    @Temporal(TemporalType.TIMESTAMP)
    private Date von;
    @Basic(optional = false)
    @Column(name = "Bis")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bis;
    @Basic(optional = false)
    @Column(name = "PlanKennung")
    private long planKennung;
    @Basic(optional = false)
    @Column(name = "NKontrolle")
    @Temporal(TemporalType.DATE)
    private Date nKontrolle;
    @Version
    @Column(name = "version")
    private Long version;

    // ==
    // N:1 Relationen
    // ==
    @JoinColumn(name = "AnUKennung", referencedColumnName = "UKennung")
    @ManyToOne
    private Users angesetztDurch;
    @JoinColumn(name = "AbUKennung", referencedColumnName = "UKennung")
    @ManyToOne
    private Users abgesetztDurch;
    @JoinColumn(name = "BWKennung", referencedColumnName = "BWKennung")
    @ManyToOne
    private Resident resident;
    @JoinColumn(name = "BWIKID", referencedColumnName = "BWIKID")
    @ManyToOne
    private BWInfoKat kategorie;

    // ==
    // 1:N Relationen
    // ==
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nursingProcess")
    private Collection<SYSNP2PROCESS> attachedVorgaenge;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nursingProcess")
    private Collection<NPControl> kontrollen;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nursingProcess")
//    @OrderBy("intervention.bezeichnung ASC")
    private List<InterventionSchedule> interventionSchedules;

    public NursingProcess() {
    }

    public NursingProcess(Resident bewohner) {
        this.resident = bewohner;
        this.angesetztDurch = OPDE.getLogin().getUser();
        interventionSchedules = new ArrayList<InterventionSchedule>();
        kontrollen = new ArrayList<NPControl>();
        attachedVorgaenge = new ArrayList<SYSNP2PROCESS>();
        nKontrolle = new DateTime().plusWeeks(4).toDate();
        von = new Date();
        bis = SYSConst.DATE_BIS_AUF_WEITERES;
        this.planKennung = -1l;
    }

    public Long getPlanID() {
        return planID;
    }

    public void setPlanID(Long planID) {
        this.planID = planID;
    }


    public String getStichwort() {
        return stichwort;
    }

    public void setStichwort(String stichwort) {
        this.stichwort = stichwort;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getZiel() {
        return ziel;
    }

    public void setZiel(String ziel) {
        this.ziel = ziel;
    }

    public Date getVon() {
        return von;
    }

    public void setVon(Date von) {
        this.von = von;
    }

    public Date getBis() {
        return bis;
    }

    public void setBis(Date bis) {
        this.bis = bis;
    }


    public long getPlanKennung() {
        return planKennung;
    }

    public void setPlanKennung(long planKennung) {
        this.planKennung = planKennung;
    }

    public Date getNKontrolle() {
        return nKontrolle;
    }

    public void setNKontrolle(Date nKontrolle) {
        this.nKontrolle = nKontrolle;
    }

    public Users getAbgesetztDurch() {
        return abgesetztDurch;
    }

    public void setAbgesetztDurch(Users abgesetztDurch) {
        this.abgesetztDurch = abgesetztDurch;
    }

    public Users getAngesetztDurch() {
        return angesetztDurch;
    }

    public void setAngesetztDurch(Users angesetztDurch) {
        this.angesetztDurch = angesetztDurch;
    }

    public Resident getResident() {
        return resident;
    }

    public void setResident(Resident bewohner) {
        this.resident = bewohner;
    }

    public BWInfoKat getKategorie() {
        return kategorie;
    }

    public void setKategorie(BWInfoKat kategorie) {
        this.kategorie = kategorie;
    }

    public Collection<SYSNP2PROCESS> getAttachedVorgaenge() {
        return attachedVorgaenge;
    }

    public boolean isAbgesetzt() {
        return bis.before(SYSConst.DATE_BIS_AUF_WEITERES);
    }

    public Collection<NPControl> getKontrollen() {
        return kontrollen;
    }

    public List<InterventionSchedule> getInterventionSchedule() {
        return interventionSchedules;
    }

    @Override
    public String getTitle() {
        return stichwort;
    }

    @Override
    public long getPITInMillis() {
        return von.getTime();
    }

    @Override
    public String getContentAsHTML() {
        // TODO: fehlt noch
        return "<html>not yet</html>";
    }

    @Override
    public String getPITAsHTML() {
        // TODO: fehlt noch
        return "<html>not yet</html>";
    }

    @Override
    public long getID() {
        return planID;
    }

    @Override
    public Users getUser() {
        return angesetztDurch;
    }

    @Override
    public ArrayList<QProcess> getAttachedProcesses() {
        ArrayList<QProcess> list = new ArrayList<QProcess>();
        for (SYSNP2PROCESS att : attachedVorgaenge) {
            list.add(att.getVorgang());
        }
        return list;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (planID != null ? planID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof NursingProcess)) {
            return false;
        }
        NursingProcess other = (NursingProcess) object;
        if ((this.planID == null && other.planID != null) || (this.planID != null && !this.planID.equals(other.planID))) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(NursingProcess nursingProcess) {
        if (stichwort.compareTo(nursingProcess.getStichwort()) != 0) {
            return stichwort.compareTo(nursingProcess.getStichwort());
        }

        return von.compareTo(nursingProcess.getVon());
    }

    public NursingProcess(String stichwort, String situation, String ziel, Date von, Date bis, long planKennung, Date nKontrolle, Long version, Users angesetztDurch, Users abgesetztDurch, Resident bewohner, BWInfoKat kategorie, Collection<SYSNP2PROCESS> attachedVorgaenge, Collection<NPControl> kontrollen, List<InterventionSchedule> interventionSchedules) {
        this.stichwort = stichwort;
        this.situation = situation;
        this.ziel = ziel;
        this.von = von;
        this.bis = bis;
        this.planKennung = planKennung;
        this.nKontrolle = nKontrolle;
        this.version = version;
        this.angesetztDurch = angesetztDurch;
        this.abgesetztDurch = abgesetztDurch;
        this.resident = bewohner;
        this.kategorie = kategorie;
        this.attachedVorgaenge = attachedVorgaenge;
        this.kontrollen = kontrollen;
        this.interventionSchedules = interventionSchedules;
    }

    @Override
    public NursingProcess clone() {
        NursingProcess myNewNP = new NursingProcess(stichwort, situation, ziel, von, bis, planKennung, nKontrolle, 0l, angesetztDurch, abgesetztDurch, resident, kategorie, new ArrayList<SYSNP2PROCESS>(), new ArrayList<NPControl>(), new ArrayList<InterventionSchedule>());
        for (InterventionSchedule is : interventionSchedules) {
            InterventionSchedule myIS = is.clone();
            myIS.setNursingProcess(myNewNP);
            myNewNP.getInterventionSchedule().add(myIS);
        }
        return myNewNP;
    }

    @Override
    public String toString() {
        return "entity.nursingprocess.Planung[planID=" + planID + "]";
    }
}
