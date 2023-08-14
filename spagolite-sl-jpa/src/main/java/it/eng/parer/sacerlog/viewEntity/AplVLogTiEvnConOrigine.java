package it.eng.parer.sacerlog.viewEntity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the APL_V_LOG_TI_EVN_CON_ORIGINE database table.
 *
 */
@Entity
@Table(name = "APL_V_LOG_TI_EVN_CON_ORIGINE", schema = "SACER_IAM")
@NamedQueries({ @NamedQuery(name = "AplVLogTiEvnConOrigine.findAll", query = "SELECT a FROM AplVLogTiEvnConOrigine a"),
        @NamedQuery(name = "AplVLogTiEvnConOrigine.findByApplicIdAzioneCompSw", query = "SELECT a FROM AplVLogTiEvnConOrigine a WHERE a.nmApplic = :nmApplic AND a.id.idAzionePaginaCompSw = :idAzionePaginaCompSw"),
        @NamedQuery(name = "AplVLogTiEvnConOrigine.findByApplicFinestraAzione", query = "SELECT a FROM AplVLogTiEvnConOrigine a WHERE a.nmApplic = :nmApplic AND a.nmPaginaCompSw = :nmPaginaCompSw AND a.nmAzionePaginaCompSw = :nmAzionePaginaCompSw") })
public class AplVLogTiEvnConOrigine implements Serializable {

    private static final long serialVersionUID = 1L;
    private AplVLogTiEvnConOrigineId aplVLogTiEvnConOrigineId;
    private String nmApplic;
    private String nmAzionePaginaCompSw;
    private String nmPaginaCompSw;
    private String nmTipoEvento;
    private String tipoClasseEvento;
    private String tipoClassePremisEvento;
    private String tipoOrigineEvento;

    public AplVLogTiEvnConOrigine() {
    }

    @EmbeddedId
    public AplVLogTiEvnConOrigineId getAplVLogTiEvnConOrigineId() {
        return aplVLogTiEvnConOrigineId;
    }

    public void setAplVLogTiEvnConOrigineId(AplVLogTiEvnConOrigineId aplVLogTiEvnConOrigineId) {
        this.aplVLogTiEvnConOrigineId = aplVLogTiEvnConOrigineId;
    }

    @Column(name = "NM_APPLIC")
    public String getNmApplic() {
        return this.nmApplic;
    }

    public void setNmApplic(String nmApplic) {
        this.nmApplic = nmApplic;
    }

    @Column(name = "NM_AZIONE_PAGINA_COMP_SW")
    public String getNmAzionePaginaCompSw() {
        return this.nmAzionePaginaCompSw;
    }

    public void setNmAzionePaginaCompSw(String nmAzionePaginaCompSw) {
        this.nmAzionePaginaCompSw = nmAzionePaginaCompSw;
    }

    @Column(name = "NM_PAGINA_COMP_SW")
    public String getNmPaginaCompSw() {
        return this.nmPaginaCompSw;
    }

    public void setNmPaginaCompSw(String nmPaginaCompSw) {
        this.nmPaginaCompSw = nmPaginaCompSw;
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