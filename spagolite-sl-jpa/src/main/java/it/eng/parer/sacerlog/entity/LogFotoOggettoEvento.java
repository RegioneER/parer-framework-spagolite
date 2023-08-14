package it.eng.parer.sacerlog.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnTransformer;

/**
 * The persistent class for the LOG_FOTO_OGGETTO_EVENTO database table.
 *
 */
@Entity
@Table(schema = "SACER_LOG", name = "LOG_FOTO_OGGETTO_EVENTO")
@NamedQuery(name = "LogFotoOggettoEvento.deleteAll", query = "DELETE FROM LogFotoOggettoEvento")
public class LogFotoOggettoEvento implements Serializable {

    private static final long serialVersionUID = 1L;
    private long idFotoOggettoEvento;
    private Calendar dtRegEvento;
    private String blFotoOggetto;
    private LogOggettoEvento logOggettoEvento;

    public LogFotoOggettoEvento() {
    }

    @Id
    @SequenceGenerator(name = "LOG_FOTO_OGGETTO_EVENTO_IDFOTOOGGETTOEVENTO_GENERATOR", schema = "SACER_LOG", sequenceName = "SLOG_FOTO_OGGETTO_EVENTO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_FOTO_OGGETTO_EVENTO_IDFOTOOGGETTOEVENTO_GENERATOR")
    @Column(name = "ID_FOTO_OGGETTO_EVENTO")
    public long getIdFotoOggettoEvento() {
        return this.idFotoOggettoEvento;
    }

    public void setIdFotoOggettoEvento(long idFotoOggettoEvento) {
        this.idFotoOggettoEvento = idFotoOggettoEvento;
    }

    @Column(name = "DT_REG_EVENTO")
    @Temporal(TemporalType.TIMESTAMP)
    public Calendar getDtRegEvento() {
        return this.dtRegEvento;
    }

    public void setDtRegEvento(Calendar dtRegEvento) {
        this.dtRegEvento = dtRegEvento;
    }

    @ColumnTransformer(read = "to_clob(columnName)", write = "?")
    @Column(name = "BL_FOTO_OGGETTO", columnDefinition = "XMLType")
    public String getBlFotoOggetto() {
        return this.blFotoOggetto;
    }

    public void setBlFotoOggetto(String blFotoOggetto) {
        this.blFotoOggetto = blFotoOggetto;
    }

    // bi-directional many-to-one association to LogOggettoEvento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_OGGETTO_EVENTO")
    public LogOggettoEvento getLogOggettoEvento() {
        return this.logOggettoEvento;
    }

    public void setLogOggettoEvento(LogOggettoEvento logOggettoEvento) {
        this.logOggettoEvento = logOggettoEvento;
    }

}
