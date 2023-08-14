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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnTransformer;

/**
 * The persistent class for the LOG_DELTA_FOTO database table.
 *
 */
@Entity
@Table(schema = "SACER_LOG", name = "LOG_DELTA_FOTO")
@NamedQueries({ @NamedQuery(name = "LogDeltaFoto.findAll", query = "SELECT l FROM LogDeltaFoto l"),
        @NamedQuery(name = "LogDeltaFoto.deleteAll", query = "DELETE FROM LogDeltaFoto") })
public class LogDeltaFoto implements Serializable {

    private static final long serialVersionUID = 1L;
    private long idDeltaFoto;
    private String blDeltaFoto;
    private Calendar dtRegEvento;
    private LogOggettoEvento logOggettoEvento1;
    private LogOggettoEvento logOggettoEvento2;

    public LogDeltaFoto() {
    }

    @Id
    @SequenceGenerator(name = "LOG_DELTA_FOTO_IDDELTAFOTO_GENERATOR", schema = "SACER_LOG", sequenceName = "SLOG_DELTA_FOTO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_DELTA_FOTO_IDDELTAFOTO_GENERATOR")
    @Column(name = "ID_DELTA_FOTO")
    public long getIdDeltaFoto() {
        return this.idDeltaFoto;
    }

    public void setIdDeltaFoto(long idDeltaFoto) {
        this.idDeltaFoto = idDeltaFoto;
    }

    @ColumnTransformer(read = "to_clob(columnName)", write = "?")
    @Column(name = "BL_DELTA_FOTO", columnDefinition = "XMLType")
    public String getBlDeltaFoto() {
        return this.blDeltaFoto;
    }

    public void setBlDeltaFoto(String blDeltaFoto) {
        this.blDeltaFoto = blDeltaFoto;
    }

    @Column(name = "DT_REG_EVENTO")
    @Temporal(TemporalType.TIMESTAMP)
    public Calendar getDtRegEvento() {
        return this.dtRegEvento;
    }

    public void setDtRegEvento(Calendar dtRegEvento) {
        this.dtRegEvento = dtRegEvento;
    }

    // bi-directional many-to-one association to LogOggettoEvento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_OGGETTO_EVENTO_PREC")
    public LogOggettoEvento getLogOggettoEvento1() {
        return this.logOggettoEvento1;
    }

    public void setLogOggettoEvento1(LogOggettoEvento logOggettoEvento1) {
        this.logOggettoEvento1 = logOggettoEvento1;
    }

    // bi-directional many-to-one association to LogOggettoEvento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_OGGETTO_EVENTO")
    public LogOggettoEvento getLogOggettoEvento2() {
        return this.logOggettoEvento2;
    }

    public void setLogOggettoEvento2(LogOggettoEvento logOggettoEvento2) {
        this.logOggettoEvento2 = logOggettoEvento2;
    }

}
