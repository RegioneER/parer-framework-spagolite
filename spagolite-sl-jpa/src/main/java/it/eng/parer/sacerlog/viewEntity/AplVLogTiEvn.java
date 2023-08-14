package it.eng.parer.sacerlog.viewEntity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the APL_V_LOG_TI_EVN database table.
 *
 */
@Entity
@Table(name = "APL_V_LOG_TI_EVN", schema = "SACER_IAM")
@NamedQueries({ @NamedQuery(name = "AplVLogTiEvn.findAll", query = "SELECT a FROM AplVLogTiEvn a"),
        @NamedQuery(name = "AplVLogTiEvn.findByApplicTipoEvento", query = "SELECT a FROM AplVLogTiEvn a WHERE a.nmApplic = :nmApplic AND a.nmTipoEvento = :nmTipoEvento") })
public class AplVLogTiEvn implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigDecimal idApplic;
    private BigDecimal idTipoEvento;
    private String nmApplic;
    private String nmTipoEvento;
    private String tipoClasseEvento;
    private String tipoClassePremisEvento;
    private String tipoOrigineEvento;

    public AplVLogTiEvn() {
    }

    @Column(name = "ID_APPLIC")
    public BigDecimal getIdApplic() {
        return this.idApplic;
    }

    public void setIdApplic(BigDecimal idApplic) {
        this.idApplic = idApplic;
    }

    @Id
    @Column(name = "ID_TIPO_EVENTO")
    public BigDecimal getIdTipoEvento() {
        return this.idTipoEvento;
    }

    public void setIdTipoEvento(BigDecimal idTipoEvento) {
        this.idTipoEvento = idTipoEvento;
    }

    @Column(name = "NM_APPLIC")
    public String getNmApplic() {
        return this.nmApplic;
    }

    public void setNmApplic(String nmApplic) {
        this.nmApplic = nmApplic;
    }

    @Column(name = "NM_TIPO_EVENTO")
    public String getNmTipoEvento() {
        return this.nmTipoEvento;
    }

    public void setNmTipoEvento(String nmTipoEvento) {
        this.nmTipoEvento = nmTipoEvento;
    }

    @Column(name = "TIPO_CLASSE_EVENTO")
    public String getTipoClasseEvento() {
        return this.tipoClasseEvento;
    }

    public void setTipoClasseEvento(String tipoClasseEvento) {
        this.tipoClasseEvento = tipoClasseEvento;
    }

    @Column(name = "TIPO_CLASSE_PREMIS_EVENTO")
    public String getTipoClassePremisEvento() {
        return this.tipoClassePremisEvento;
    }

    public void setTipoClassePremisEvento(String tipoClassePremisEvento) {
        this.tipoClassePremisEvento = tipoClassePremisEvento;
    }

    @Column(name = "TIPO_ORIGINE_EVENTO")
    public String getTipoOrigineEvento() {
        return this.tipoOrigineEvento;
    }

    public void setTipoOrigineEvento(String tipoOrigineEvento) {
        this.tipoOrigineEvento = tipoOrigineEvento;
    }

}
