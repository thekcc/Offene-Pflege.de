/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package entity.system;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author tloehr
 */
@Entity
@Table(name = "SYSLOG")
//@NamedQueries({
//    @NamedQuery(name = "Syslog.findAll", query = "SELECT s FROM Syslog s"),
//    @NamedQuery(name = "Syslog.findByLogid", query = "SELECT s FROM Syslog s WHERE s.logid = :logid"),
//    @NamedQuery(name = "Syslog.findByHost", query = "SELECT s FROM Syslog s WHERE s.host = :host"),
//    @NamedQuery(name = "Syslog.findByIp", query = "SELECT s FROM Syslog s WHERE s.ip = :ip"),
//    @NamedQuery(name = "Syslog.findByHostkey", query = "SELECT s FROM Syslog s WHERE s.hostkey = :hostkey"),
//    @NamedQuery(name = "Syslog.findByPit", query = "SELECT s FROM Syslog s WHERE s.pit = :pit"),
//    @NamedQuery(name = "Syslog.findByLoglevel", query = "SELECT s FROM Syslog s WHERE s.loglevel = :loglevel")})
public class Syslog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "LOGID")
    private Long logid;
    @Column(name = "Host")
    private String host;
    @Column(name = "IP")
    private String ip;
    @Column(name = "Hostkey")
    private String hostkey;
    @Basic(optional = false)
    @Column(name = "PIT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pit;
    @Lob
    @Column(name = "MESSAGE")
    private String message;
    @Column(name = "Loglevel")
    private Short loglevel;
    @JoinColumn(name = "LoginID", referencedColumnName = "LoginID")
    @ManyToOne
    private SYSLogin login;

    public Syslog() {
    }

    public Syslog(String host, String ip, String hostkey, Date pit, String message, Short loglevel, SYSLogin login) {
        this.host = host;
        this.ip = ip;
        this.hostkey = hostkey;
        this.pit = pit;
        this.message = message;
        this.loglevel = loglevel;
        this.login = login;
    }

    public Long getLogid() {
        return logid;
    }

    public void setLogid(Long logid) {
        this.logid = logid;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHostkey() {
        return hostkey;
    }

    public void setHostkey(String hostkey) {
        this.hostkey = hostkey;
    }

    public SYSLogin getLogin() {
        return login;
    }

    public void setLogin(SYSLogin login) {
        this.login = login;
    }



    public Date getPit() {
        return pit;
    }

    public void setPit(Date pit) {
        this.pit = pit;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Short getLoglevel() {
        return loglevel;
    }

    public void setLoglevel(Short loglevel) {
        this.loglevel = loglevel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (logid != null ? logid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Syslog)) {
            return false;
        }
        Syslog other = (Syslog) object;
        if ((this.logid == null && other.logid != null) || (this.logid != null && !this.logid.equals(other.logid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.system.Syslog[logid=" + logid + "]";
    }

}
