package it.eng.parer.sacerlog.viewEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class LogVRicEventiOrganizId implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigDecimal idAgenteEvento;
    private BigDecimal idOggettoEvento;

    public LogVRicEventiOrganizId() {
    }

    @Column(name = "ID_AGENTE_EVENTO")
    public BigDecimal getIdAgenteEvento() {
        return this.idAgenteEvento;
    }

    public void setIdAgenteEvento(BigDecimal idAgenteEvento) {
        this.idAgenteEvento = idAgenteEvento;
    }

    @Column(name = "ID_OGGETTO_EVENTO")
    public BigDecimal getIdOggettoEvento() {
        return this.idOggettoEvento;
    }

    public void setIdOggettoEvento(BigDecimal idOggettoEvento) {
        this.idOggettoEvento = idOggettoEvento;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.idAgenteEvento);
        hash = 59 * hash + Objects.hashCode(this.idOggettoEvento);
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
        final LogVRicEventiOrganizId other = (LogVRicEventiOrganizId) obj;
        if (!Objects.equals(this.idAgenteEvento, other.idAgenteEvento)) {
            return false;
        }
        if (!Objects.equals(this.idOggettoEvento, other.idOggettoEvento)) {
            return false;
        }
        return true;
    }

}
