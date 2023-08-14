package it.eng.paginator.util;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

/**
 * @author manuel.bertuzzi@eng.it
 */
@Entity
@Table(name = "PIG_AMBIENTE_VERS")
@Immutable
@NamedQueries({
        @NamedQuery(name = "PigAmbienteVers.findAll", query = "SELECT v FROM PigAmbienteVers v ORDER BY v.nmAmbienteVers ASC"),

        @NamedQuery(name = "PigAmbienteVers.findGreaterThanId", query = "SELECT v FROM PigAmbienteVers v WHERE v.idAmbienteVers > :idAmbienteVers ORDER BY v.nmAmbienteVers ASC") })
public class PigAmbienteVers {

    @Id
    @Column(name = "ID_AMBIENTE_VERS")
    private Long idAmbienteVers;

    @Column(name = "NM_AMBIENTE_VERS")
    private String nmAmbienteVers;

    public Long getIdAmbienteVers() {
        return idAmbienteVers;
    }

    public String getNmAmbienteVers() {
        return nmAmbienteVers;
    }
}
