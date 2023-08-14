package it.eng.parer.sacerlog.viewEntity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the LOG_V_USR_ABIL_ORGANIZ database table.
 *
 */
@Entity
@Table(name = "LOG_V_USR_ABIL_ORGANIZ", schema = "SACER_LOG")
@NamedQueries({ @NamedQuery(name = "LogVUsrAbilOrganiz.findAll", query = "SELECT u FROM LogVUsrAbilOrganiz u"),
        @NamedQuery(name = "LogVUsrAbilOrganiz.findByApplicAndUser", query = "SELECT u FROM LogVUsrAbilOrganiz u WHERE u.nmApplic = :nmApplic AND u.logVUsrAbilOrganizId.idUserIam = :idUserIam ORDER BY u.dlCompositoOrganiz"), })
public class LogVUsrAbilOrganiz implements Serializable {

    private static final long serialVersionUID = 1L;
    private String dlCompositoOrganiz;
    private String dlPathIdOrganizIam;
    private String dsOrganiz;
    private String flAttivo;
    private BigDecimal idApplic;
    private BigDecimal idDichAbilOrganiz;
    private BigDecimal idOrganizApplic;
    private BigDecimal idOrganizIamPadre;
    private BigDecimal idTipoOrganiz;
    private BigDecimal idUsoUserApplic;
    private String nmApplic;
    private String nmOrganiz;
    private String nmTipoOrganiz;
    private LogVUsrAbilOrganizId logVUsrAbilOrganizId;

    public LogVUsrAbilOrganiz() {
    }

    public LogVUsrAbilOrganiz(BigDecimal idOrganizIam, BigDecimal idOrganizApplic) {
        this.logVUsrAbilOrganizId = new LogVUsrAbilOrganizId();
        logVUsrAbilOrganizId.setIdOrganizIam(idOrganizIam);
        this.idOrganizApplic = idOrganizApplic;
    }

    @EmbeddedId
    public LogVUsrAbilOrganizId getLogVUsrAbilOrganizId() {
        return logVUsrAbilOrganizId;
    }

    public void setLogVUsrAbilOrganizId(LogVUsrAbilOrganizId logVUsrAbilOrganizId) {
        this.logVUsrAbilOrganizId = logVUsrAbilOrganizId;
    }

    @Column(name = "DL_COMPOSITO_ORGANIZ")
    public String getDlCompositoOrganiz() {
        return this.dlCompositoOrganiz;
    }

    public void setDlCompositoOrganiz(String dlCompositoOrganiz) {
        this.dlCompositoOrganiz = dlCompositoOrganiz;
    }

    @Column(name = "DL_PATH_ID_ORGANIZ_IAM")
    public String getDlPathIdOrganizIam() {
        return this.dlPathIdOrganizIam;
    }

    public void setDlPathIdOrganizIam(String dlPathIdOrganizIam) {
        this.dlPathIdOrganizIam = dlPathIdOrganizIam;
    }

    @Column(name = "DS_ORGANIZ")
    public String getDsOrganiz() {
        return this.dsOrganiz;
    }

    public void setDsOrganiz(String dsOrganiz) {
        this.dsOrganiz = dsOrganiz;
    }

    @Column(name = "FL_ATTIVO", columnDefinition = "char")
    public String getFlAttivo() {
        return this.flAttivo;
    }

    public void setFlAttivo(String flAttivo) {
        this.flAttivo = flAttivo;
    }

    @Column(name = "ID_APPLIC")
    public BigDecimal getIdApplic() {
        return this.idApplic;
    }

    public void setIdApplic(BigDecimal idApplic) {
        this.idApplic = idApplic;
    }

    @Column(name = "ID_DICH_ABIL_ORGANIZ")
    public BigDecimal getIdDichAbilOrganiz() {
        return this.idDichAbilOrganiz;
    }

    public void setIdDichAbilOrganiz(BigDecimal idDichAbilOrganiz) {
        this.idDichAbilOrganiz = idDichAbilOrganiz;
    }

    @Column(name = "ID_ORGANIZ_APPLIC")
    public BigDecimal getIdOrganizApplic() {
        return this.idOrganizApplic;
    }

    public void setIdOrganizApplic(BigDecimal idOrganizApplic) {
        this.idOrganizApplic = idOrganizApplic;
    }

    @Column(name = "ID_ORGANIZ_IAM_PADRE")
    public BigDecimal getIdOrganizIamPadre() {
        return this.idOrganizIamPadre;
    }

    public void setIdOrganizIamPadre(BigDecimal idOrganizIamPadre) {
        this.idOrganizIamPadre = idOrganizIamPadre;
    }

    @Column(name = "ID_TIPO_ORGANIZ")
    public BigDecimal getIdTipoOrganiz() {
        return this.idTipoOrganiz;
    }

    public void setIdTipoOrganiz(BigDecimal idTipoOrganiz) {
        this.idTipoOrganiz = idTipoOrganiz;
    }

    @Column(name = "ID_USO_USER_APPLIC")
    public BigDecimal getIdUsoUserApplic() {
        return this.idUsoUserApplic;
    }

    public void setIdUsoUserApplic(BigDecimal idUsoUserApplic) {
        this.idUsoUserApplic = idUsoUserApplic;
    }

    @Column(name = "NM_APPLIC")
    public String getNmApplic() {
        return this.nmApplic;
    }

    public void setNmApplic(String nmApplic) {
        this.nmApplic = nmApplic;
    }

    @Column(name = "NM_ORGANIZ")
    public String getNmOrganiz() {
        return this.nmOrganiz;
    }

    public void setNmOrganiz(String nmOrganiz) {
        this.nmOrganiz = nmOrganiz;
    }

    @Column(name = "NM_TIPO_ORGANIZ")
    public String getNmTipoOrganiz() {
        return this.nmTipoOrganiz;
    }

    public void setNmTipoOrganiz(String nmTipoOrganiz) {
        this.nmTipoOrganiz = nmTipoOrganiz;
    }
}
