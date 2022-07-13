package it.eng.parer.sacerlog.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;

/**
 * The persistent class for the LOG_CHIAVE_ACCESSO_EVENTO database table.
 *
 */
@Entity
@Table(name = "SACER_LOG.LOG_CHIAVE_ACCESSO_EVENTO")
@NamedQueries({ @NamedQuery(name = "LogChiaveAccessoEvento.findAll", query = "SELECT l FROM LogChiaveAccessoEvento l"),
        @NamedQuery(name = "LogChiaveAccessoEvento.deleteAll", query = "DELETE FROM LogChiaveAccessoEvento") })
public class LogChiaveAccessoEvento implements Serializable {

    private static final long serialVersionUID = 1L;
    private long idChiaveAccessoEvento;
    private BigDecimal idApplic;
    private BigDecimal idOggetto;
    private BigDecimal idTipoOggetto;
    private Calendar dtRegEvento;
    private LogEvento logEvento;

    public LogChiaveAccessoEvento() {
    }

    @Id
    @SequenceGenerator(name = "LOG_CHIAVE_ACCESSO_EVENTO_IDCHIAVEACCESSOEVENTO_GENERATOR", sequenceName = "SACER_LOG.SLOG_CHIAVE_ACCESSO_EVENTO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_CHIAVE_ACCESSO_EVENTO_IDCHIAVEACCESSOEVENTO_GENERATOR")
    @Column(name = "ID_CHIAVE_ACCESSO_EVENTO")
    public long getIdChiaveAccessoEvento() {
        return this.idChiaveAccessoEvento;
    }

    public void setIdChiaveAccessoEvento(long idChiaveAccessoEvento) {
        this.idChiaveAccessoEvento = idChiaveAccessoEvento;
    }

    @Column(name = "ID_APPLIC")
    public BigDecimal getIdApplic() {
        return this.idApplic;
    }

    public void setIdApplic(BigDecimal idApplic) {
        this.idApplic = idApplic;
    }

    @Column(name = "ID_OGGETTO")
    public BigDecimal getIdOggetto() {
        return this.idOggetto;
    }

    public void setIdOggetto(BigDecimal idOggetto) {
        this.idOggetto = idOggetto;
    }

    @Column(name = "ID_TIPO_OGGETTO")
    public BigDecimal getIdTipoOggetto() {
        return this.idTipoOggetto;
    }

    public void setIdTipoOggetto(BigDecimal idTipoOggetto) {
        this.idTipoOggetto = idTipoOggetto;
    }

    // @org.eclipse.persistence.annotations.Convert("ORACLE_DATE")
    @Column(name = "DT_REG_EVENTO")
    @Temporal(TemporalType.TIMESTAMP)
    public Calendar getDtRegEvento() {
        return this.dtRegEvento;
    }

    public void setDtRegEvento(Calendar dtRegEvento) {
        this.dtRegEvento = dtRegEvento;
    }

    // bi-directional many-to-one association to LogEvento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EVENTO")
    public LogEvento getLogEvento() {
        return this.logEvento;
    }

    public void setLogEvento(LogEvento logEvento) {
        this.logEvento = logEvento;
    }

}
