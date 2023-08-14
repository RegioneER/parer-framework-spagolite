package it.eng.parer.sacerlog.viewEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The persistent class for the LOG_V_RIC_EVENTI database table.
 *
 */
@Embeddable
public class LogVRicEventiId implements Serializable {

    private BigDecimal idOggettoEvento;
    private BigDecimal idAgenteEvento;

    @Column(name = "ID_OGGETTO_EVENTO")
    public BigDecimal getIdOggettoEvento() {
        return idOggettoEvento;
    }

    public void setIdOggettoEvento(BigDecimal idOggettoEvento) {
        this.idOggettoEvento = idOggettoEvento;
    }

    @Column(name = "ID_AGENTE_EVENTO")
    public BigDecimal getIdAgenteEvento() {
        return idAgenteEvento;
    }

    public void setIdAgenteEvento(BigDecimal idAgenteEvento) {
        this.idAgenteEvento = idAgenteEvento;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.idOggettoEvento);
        hash = 89 * hash + Objects.hashCode(this.idAgenteEvento);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LogVRicEventiId other = (LogVRicEventiId) obj;
        if (!Objects.equals(this.idOggettoEvento, other.idOggettoEvento)) {
            return false;
        }
        if (!Objects.equals(this.idAgenteEvento, other.idAgenteEvento)) {
            return false;
        }
        return true;
    }

}
