package it.eng.parer.sacerlog.entity;

import java.io.Serializable;
import java.sql.Clob;
import java.util.Calendar;
import javax.persistence.*;
import org.eclipse.persistence.annotations.Customizer;

/**
 * The persistent class for the LOG_FOTO_OGGETTO_EVENTO database table.
 *
 */
@Entity
@Table(name = "SACER_LOG.LOG_FOTO_OGGETTO_EVENTO")
@Customizer(LogFotoOggettoEventoXMLDataCustomizer.class)
@NamedQuery(name = "LogFotoOggettoEvento.deleteAll", query = "DELETE FROM LogFotoOggettoEvento")
public class LogFotoOggettoEvento implements Serializable {

    private static final long serialVersionUID = 1L;
    private long idFotoOggettoEvento;
    private Calendar dtRegEvento;
    private Clob blFotoOggetto;
    private LogOggettoEvento logOggettoEvento;

    public LogFotoOggettoEvento() {
    }

    @Id
    @SequenceGenerator(name = "LOG_FOTO_OGGETTO_EVENTO_IDFOTOOGGETTOEVENTO_GENERATOR", sequenceName = "SACER_LOG.SLOG_FOTO_OGGETTO_EVENTO", allocationSize = 1)
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

    public Clob getBlFotoOggetto() {
        return this.blFotoOggetto;
    }

    public void setBlFotoOggetto(Clob blFotoOggetto) {
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
