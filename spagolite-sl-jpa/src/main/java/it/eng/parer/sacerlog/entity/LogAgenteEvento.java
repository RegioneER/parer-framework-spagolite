package it.eng.parer.sacerlog.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * The persistent class for the LOG_AGENTE_EVENTO database table.
 *
 */
@Entity
@Table(name = "SACER_LOG.LOG_AGENTE_EVENTO")
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
    @SequenceGenerator(name = "LOG_AGENTE_EVENTO_IDAGENTEEVENTO_GENERATOR", sequenceName = "SACER_LOG.SLOG_AGENTE_EVENTO", allocationSize = 1)
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
