package it.eng.parer.sacerlog.entity;

import java.io.Serializable;
import java.math.BigDecimal;

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

/**
 * The persistent class for the LOG_AGENTE_EVENTO database table.
 *
 */
@Entity
@Table(schema = "SACER_LOG", name = "LOG_AGENTE_EVENTO")
@NamedQueries({ @NamedQuery(name = "LogAgenteEvento.findAll", query = "SELECT l FROM LogAgenteEvento l"),
        @NamedQuery(name = "LogAgenteEvento.deleteAll", query = "DELETE FROM LogAgenteEvento") })
public class LogAgenteEvento implements Serializable {

    private static final long serialVersionUID = 1L;
    private long idAgenteEvento;
    private BigDecimal idAgente;
    private String tiRuoloAgenteEvento;
    private LogEvento logEvento;

    public LogAgenteEvento() {
    }

    @Id
    @SequenceGenerator(name = "LOG_AGENTE_EVENTO_IDAGENTEEVENTO_GENERATOR", schema = "SACER_LOG", sequenceName = "SLOG_AGENTE_EVENTO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_AGENTE_EVENTO_IDAGENTEEVENTO_GENERATOR")
    @Column(name = "ID_AGENTE_EVENTO")
    public long getIdAgenteEvento() {
        return this.idAgenteEvento;
    }

    public void setIdAgenteEvento(long idAgenteEvento) {
        this.idAgenteEvento = idAgenteEvento;
    }

    @Column(name = "ID_AGENTE")
    public BigDecimal getIdAgente() {
        return this.idAgente;
    }

    public void setIdAgente(BigDecimal idAgente) {
        this.idAgente = idAgente;
    }

    @Column(name = "TI_RUOLO_AGENTE_EVENTO")
    public String getTiRuoloAgenteEvento() {
        return this.tiRuoloAgenteEvento;
    }

    public void setTiRuoloAgenteEvento(String tiRuoloAgenteEvento) {
        this.tiRuoloAgenteEvento = tiRuoloAgenteEvento;
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
