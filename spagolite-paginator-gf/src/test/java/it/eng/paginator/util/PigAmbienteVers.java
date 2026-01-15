/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna <p/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version. <p/> This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Affero General Public License for more details. <p/> You should
 * have received a copy of the GNU Affero General Public License along with this program. If not,
 * see <https://www.gnu.org/licenses/>.
 */

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
