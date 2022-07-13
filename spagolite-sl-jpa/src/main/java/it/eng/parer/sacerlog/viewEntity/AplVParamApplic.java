package it.eng.parer.sacerlog.viewEntity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the APL_V_PARAM_APPLIC database table.
 *
 */
@Entity
@Table(name = "APL_V_PARAM_APPLIC")
@NamedQuery(name = "AplVParamApplic.findByNmParamApplic", query = "SELECT a FROM AplVParamApplic a WHERE a.nmParamApplic = :nmParamApplic")
public class AplVParamApplic implements Serializable {

    private static final long serialVersionUID = 1L;
    private String dsParamApplic;
    private String dsValoreParamApplic;
    private String nmParamApplic;
    private String tiParamApplic;

    public AplVParamApplic() {
    }

    @Column(name = "DS_PARAM_APPLIC")
    public String getDsParamApplic() {
        return this.dsParamApplic;
    }

    public void setDsParamApplic(String dsParamApplic) {
        this.dsParamApplic = dsParamApplic;
    }

    @Column(name = "DS_VALORE_PARAM_APPLIC")
    public String getDsValoreParamApplic() {
        return this.dsValoreParamApplic;
    }

    public void setDsValoreParamApplic(String dsValoreParamApplic) {
        this.dsValoreParamApplic = dsValoreParamApplic;
    }

    @Id
    @Column(name = "NM_PARAM_APPLIC")
    public String getNmParamApplic() {
        return this.nmParamApplic;
    }

    public void setNmParamApplic(String nmParamApplic) {
        this.nmParamApplic = nmParamApplic;
    }

    @Column(name = "TI_PARAM_APPLIC")
    public String getTiParamApplic() {
        return this.tiParamApplic;
    }

    public void setTiParamApplic(String tiParamApplic) {
        this.tiParamApplic = tiParamApplic;
    }

}
