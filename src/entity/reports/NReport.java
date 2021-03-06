package entity.reports;

import entity.Ownable;
import entity.files.SYSNR2FILE;
import entity.info.Resident;
import entity.process.QProcess;
import entity.process.QProcessElement;
import entity.process.SYSNR2PROCESS;
import entity.system.Commontags;
import entity.system.Users;
import interfaces.Attachable;
import op.OPDE;
import op.tools.SYSTools;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Diese Entity Klasse ist für die Speicherung von Pflegeberichten zuständig.
 * Da OPDE dokumentenecht sein soll, werden hier ein paar Besonderheit gewährleistet.
 * So werden Pflegeberichte nicht gelöscht, sondern als gelöscht markiert. Ebenso bei den
 * Änderungen eines Berichtes. Der alte Bericht bleibt erhalten und der neue Bericht ersetzt den alten.
 * <ul>
 * <li><b>LÖSCHEN</b> - bedeutet, dass das Attribut <B>delPit</B> nicht mehr null ist, sondern den Zeitpunkt der Löschung enthält.
 * Das Attribut {@code deletedBy} enthält die Information über den Benutzer, der die Löschung vorgenommen hat. </li>
 * <li><b>ÄNDERUNG</b> - wenn ein Bericht geändert wird, dann passiert folgendes:
 * <li>der alte Bericht wird geklont, also haben wir jetzt einen alten und einen neuen Bericht</li>
 * <li>der neue Bericht wird als Ersatz für den alten markiert. Das bedeutet konkret
 * <ol>
 * <li>Im Attribut replacedBy steht der key des neuen Berichtes</li>
 * <li>Alle verknüpften Dateien und Qualitätsprocesse werden entfernt und dem neuen Bericht zugeordnet.</li>
 * <li>Die Attribute editedBy und editedPit werden entsprechend gesetzt</li>
 * </ol>
 * </li>
 * <li>der neue Bericht wird als Ersatz für den alten markiert. Das bedeutet konkret
 * <p>
 * <p>
 * <p>
 * <p>
 * </ul>
 *
 * @author tloehr
 */
@Entity
@Table(name = "nreports")
public class NReport extends Ownable implements Serializable, QProcessElement, Comparable<NReport>, Cloneable, Attachable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PBID")
    private Long pbid;
    @Version
    @Column(name = "version")
    private Long version;

    /**
     * Hier steht der Zeitpunkt, an dem das beschriebene Ereignis stattgefunden hat.
     */
    @Basic(optional = false)
    @Column(name = "PIT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pit;

    /**
     * This is the time, when the new report was entered. A replacement report has also the PIT of the replacement time.
     * It does NOT carry over the newPIT from the old report.
     */
    @Basic(optional = false)
    @Column(name = "NewPIT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date newPIT;

    /**
     * Falls dieser Bericht nachbearbeitet wurde, dann steht hier der entsprechende Zeitpunkt drin. `null` wenn nicht.
     */
    @Basic(optional = true)
    @Column(name = "EditedPIT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date editedPIT;

    /**
     * Ein Bericht gilt als gelöscht, wenn das Löschdatum hier steht. `null` wenn der Bericht **nicht** gelöscht ist.
     */
    @Basic(optional = true)
    @Column(name = "DelPIT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date delPIT;

    /**
     * Der eigentliche Text des Berichtes.
     */
    @Lob
    @Column(name = "Text")
    private String text;

    /**
     * Das hier ist ein Relikt aus der Zeit als alle in der Pflege so auf den Minuten herumgeritten sind. Man kann hier angeben, wie lange
     * die Tätigkeit, die in dem Bericht beschrieben wurde, gedauert hat.
     */
    @Basic(optional = false)
    @Column(name = "Dauer")
    private int minutes;

    /**
     * Die Kennung des Benutzers, der diesen Bericht eingetragen hat.
     */
    @JoinColumn(name = "NewBy", referencedColumnName = "UKennung")
    @ManyToOne
    private Users newBy;


    /**
     * Bei gelöschten Berichten steht hier der User, der die Löschung vorgenommen hat.
     */
    @JoinColumn(name = "DeletedBy", referencedColumnName = "UKennung")
    @ManyToOne
    private Users deletedBy;

    /**
     * Das ist der BW, dem der Bericht zugeordnet wurde.
     */
    @JoinColumn(name = "BWKennung", referencedColumnName = "BWKennung")
    @ManyToOne
    private Resident resident;

    /**
     * Das ist der User, der den Bericht bearbeitet hat. `null` wenn nicht.
     */
    @JoinColumn(name = "EditedBy", referencedColumnName = "UKennung")
    @ManyToOne
    private Users editedBy;

    /**
     * Falls der Bericht ersetzt wurde, dann steht der Bericht, der ihn ersetzt hat. `null` wenn nicht.
     */
    @JoinColumn(name = "ReplacedBy", referencedColumnName = "PBID")
    @OneToOne
    private NReport replacedBy;

    /**
     * Falls dieser Bericht einen anderen ersetzt hat, dann steht hier der ersetzte Bericht drin. `null` wenn nicht.
     */
    @JoinColumn(name = "ReplacementFor", referencedColumnName = "PBID")
    @OneToOne
    private NReport replacementFor;

    /**
     * Die Liste von angehangenen Dateien.
     */
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "nReport")
    private Collection<SYSNR2FILE> attachedFilesConnections;

    /**
     * Das hier gilt nur für Übergabe Berichte. Es enthält alle User, die diesen Berichte zur Kenntnis genommen haben.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bericht", fetch = FetchType.EAGER)
    private List<NR2User> usersAcknowledged;

    /**
     * die Liste alles Qualitätsprozesse, zu dem dieser Bericht zugeordnet wurde.
     */
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "nreport")
    private Collection<SYSNR2PROCESS> attachedProcessConnections;

    /**
     * Die Liste aller TAGs für diesen Bericht.
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "nreports2tags", joinColumns =
    @JoinColumn(name = "pbid"), inverseJoinColumns =
    @JoinColumn(name = "ctagid"))
    private Collection<Commontags> commontags;

    public NReport() {
    }

    /**
     * Der Standard Konstuktor. Hier drüber werden fast alle Berichte erstellt.
     * @param resident das ist die Kennung des BWs für den der neue Bericht erstellt wird.
     */
    public NReport(Resident resident) {
        this.pit = new Date();
        this.newPIT = new Date();
        this.text = "";
        this.minutes = 3;
        this.resident = resident;
        this.newBy = OPDE.getLogin().getUser();
        this.attachedFilesConnections = new ArrayList<SYSNR2FILE>();
        this.commontags = new ArrayList<Commontags>();
        this.attachedProcessConnections = new ArrayList<SYSNR2PROCESS>();
        this.usersAcknowledged = new ArrayList<NR2User>();
        this.version = 0l;
    }

    /**
     * Hilfs Konstruktor zum Clonen der Objekte
     *
     * @param pit
     * @param newPIT
     * @param editedPIT
     * @param text
     * @param minutes
     * @param newBy
     * @param resident
     * @param editedBy
     * @param replacedBy
     * @param replacementFor
     */
    private NReport(Date pit, Date newPIT, Date editedPIT, String text, int minutes, Users newBy, Resident resident, Users editedBy, NReport replacedBy, NReport replacementFor) {
        this.pit = pit;
        this.newPIT = newPIT;
        this.editedPIT = editedPIT;
        this.text = SYSTools.tidy(text);
        this.minutes = minutes;
        this.newBy = newBy;
        this.resident = resident;
        this.editedBy = editedBy;
        this.replacedBy = replacedBy;
        this.replacementFor = replacementFor;
        this.attachedFilesConnections = new ArrayList<SYSNR2FILE>();
        this.commontags = new ArrayList<Commontags>();
        this.attachedProcessConnections = new ArrayList<SYSNR2PROCESS>();
        this.usersAcknowledged = new ArrayList<NR2User>();
        this.version = 0l;
    }

    public Long getPbid() {
        return pbid;
    }

    public void setPbid(Long pbid) {
        this.pbid = pbid;
    }

    public List<NR2User> getUsersAcknowledged() {
        return usersAcknowledged;
    }

    public Date getPit() {
        return pit;
    }

    public void setPit(Date pit) {
        this.pit = pit;
    }

    public Date getEditedPIT() {
        return editedPIT;
    }

    public String getText() {
        SYSTools.anonymizeText(resident.getNameNeverAnonymous(), text);
        return text;
    }

    public void setText(String text) {
        this.text = SYSTools.tidy(text);
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int dauer) {
        this.minutes = dauer;
    }

    public Resident getResident() {
        return resident;
    }


    public Collection<SYSNR2FILE> getAttachedFilesConnections() {
        return attachedFilesConnections;
    }

    public Date getNewPIT() {
        return newPIT;
    }

    public void setNewPIT(Date newPIT) {
        this.newPIT = newPIT;
    }

    public void setEditedPIT(Date editedPIT) {
        this.editedPIT = editedPIT;
    }

    public Date getDelPIT() {
        return delPIT;
    }

    public void setDelPIT(Date delPIT) {
        this.delPIT = delPIT;
    }

    public Users getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(Users deletedBy) {
        this.deletedBy = deletedBy;
    }

    public void setResident(Resident resident) {
        this.resident = resident;
    }

    public Users getEditedBy() {
        return editedBy;
    }

    public void setEditedBy(Users editedBy) {
        this.editedBy = editedBy;
    }

    public NReport getReplacedBy() {
        return replacedBy;
    }

    public void setReplacedBy(NReport replacedBy) {
        this.replacedBy = replacedBy;
    }

    /**
     *
     * @return je nachdem ob der Bericht ersetzt wurde `true` oder `false`
     */
    public boolean isReplaced() {
        return replacedBy != null;
    }

    public boolean isReplacement() {
        return replacementFor != null;
    }

    public boolean isAddedLater() {
        return Seconds.secondsBetween(new DateTime(pit), new DateTime(newPIT)).isGreaterThan(Seconds.seconds(NReportTools.IGNORED_AMOUNT_SECONDS_TILL_THE_CLOCK_TURNS_UP));
    }

    /**
     * die Entscheidung ob ein Pflegebericht gelöscht wurde hängt ausschließlich vom Attribut `delPit` ab.
     * @return je nachdem ob der Bericht gelöscht wurde `true` oder `false`
     */
    public boolean isDeleted() {
        return delPIT != null;
    }

    /**
     * Ein Bericht gilt als *veraltet*, wenn er entweder gelöscht wurde oder ersetzt.
     *
     * @return je nachdem ob der Bericht veraltet ist `true` oder `false`
     */
    public boolean isObsolete() {
        return isDeleted() || isReplaced();
    }

    /**
     * Ein "Ersatzbericht", "weiss" wer er mal war, indem er in <code>replacementFor</code> auf den alten,
     * ersetzten Bericht zeigt. Normale Berichte geben hier <code>null</code> zurück.
     *
     * @return
     */
    public NReport getReplacementFor() {
        return replacementFor;
    }

    public void setReplacementFor(NReport replacementFor) {
        this.replacementFor = replacementFor;
    }

    public Collection<Commontags> getCommontags() {
        return commontags;
    }


    public Users getNewBy() {
        return newBy;
    }

    @Override
    public Users getUser() {
        return newBy;
    }

    public void setNewBy(Users user) {
        this.newBy = user;
    }

    @Override
    public Users getOwner() {
        return newBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NReport nReport = (NReport) o;

        if (minutes != nReport.minutes) return false;
//        if (editedBy != null ? !editedBy.equals(nReport.editedBy) : nReport.editedBy != null) return false;
        if (editedPIT != null ? !editedPIT.equals(nReport.editedPIT) : nReport.editedPIT != null) return false;
        if (newPIT != null ? !newPIT.equals(nReport.newPIT) : nReport.newPIT != null) return false;
        if (delPIT != null ? !delPIT.equals(nReport.delPIT) : nReport.delPIT != null) return false;
        if (pbid != null ? !pbid.equals(nReport.pbid) : nReport.pbid != null) return false;
        if (pit != null ? !pit.equals(nReport.pit) : nReport.pit != null) return false;
//        if (replacedBy != null ? !replacedBy.equals(nReport.replacedBy) : nReport.replacedBy != null) return false;
//        if (replacementFor != null ? !replacementFor.equals(nReport.replacementFor) : nReport.replacementFor != null)
//            return false;
        if (resident != null ? !resident.equals(nReport.resident) : nReport.resident != null) return false;
        if (commontags != null ? !commontags.equals(nReport.commontags) : nReport.commontags != null) return false;
        if (text != null ? !text.equals(nReport.text) : nReport.text != null) return false;
        if (newBy != null ? !newBy.equals(nReport.newBy) : nReport.newBy != null) return false;
        if (version != null ? !version.equals(nReport.version) : nReport.version != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pbid != null ? pbid.hashCode() : 0;
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (pit != null ? pit.hashCode() : 0);
        result = 31 * result + (editedPIT != null ? editedPIT.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + minutes;
        result = 31 * result + (newBy != null ? newBy.hashCode() : 0);
        result = 31 * result + (resident != null ? resident.hashCode() : 0);
//        result = 31 * result + (editedBy != null ? editedBy.hashCode() : 0);
//        result = 31 * result + (replacedBy != null ? replacedBy.hashCode() : 0);
//        result = 31 * result + (replacementFor != null ? replacementFor.hashCode() : 0);
        result = 31 * result + (commontags != null ? commontags.hashCode() : 0);
        return result;
    }

    @Override
    public String getContentAsHTML() {
        return NReportTools.getNReportAsHTML(this, false);
    }

    @Override
    public String getPITAsHTML() {
        return NReportTools.getPITAsHTML(this);
    }

    @Override
    public long getID() {
        return pbid;
    }


    @Override
    public String toString() {
        return "entity.reports.NReport[pbid=" + pbid + "]";
    }

    @Override
    public ArrayList<QProcess> getAttachedProcesses() {
        ArrayList<QProcess> list = new ArrayList<QProcess>();
        for (SYSNR2PROCESS att : attachedProcessConnections) {
            list.add(att.getQProcess());
        }
        return list;
    }

    public Collection<SYSNR2PROCESS> getAttachedQProcessConnections() {
        return attachedProcessConnections;
    }

    @Override
    public String getTitle() {
        return SYSTools.xx("misc.msg.report") + ": " + text;
    }

    @Override
    public NReport clone() {

        final NReport clonedReport = new NReport(pit, newPIT, editedPIT, text, minutes, newBy, resident, editedBy, null, null);

        CollectionUtils.forAllDo(commontags, new Closure() {
            public void execute(Object o) {
                clonedReport.commontags.add((Commontags) o);
            }
        });

        CollectionUtils.forAllDo(attachedProcessConnections, new Closure() {
            public void execute(Object o) {
                SYSNR2PROCESS oldAssignment = (SYSNR2PROCESS) o;
                clonedReport.attachedProcessConnections.add(new SYSNR2PROCESS(oldAssignment.getQProcess(), clonedReport));
            }
        });

        CollectionUtils.forAllDo(attachedFilesConnections, new Closure() {
            public void execute(Object o) {
                SYSNR2FILE oldAssignment = (SYSNR2FILE) o;
                clonedReport.attachedFilesConnections.add(new SYSNR2FILE(oldAssignment.getSysfile(), clonedReport, clonedReport.getNewBy(), clonedReport.getPit()));
            }
        });
        return clonedReport;
    }

    @Override
    public long getPITInMillis() {
        return pit.getTime();
    }

    @Override
    public int compareTo(NReport other) {
        return pit.compareTo(other.getPit()) * -1;
    }

    @Override
    public boolean isActive() {
        return resident.isActive() && !isObsolete();
    }
}
