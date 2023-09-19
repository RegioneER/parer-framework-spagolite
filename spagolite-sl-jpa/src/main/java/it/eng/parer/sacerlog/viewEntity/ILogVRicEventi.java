/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna
 * <p/>
 * This program is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.sacerlog.viewEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 *
 * @author Iacolucci_M
 */
public interface ILogVRicEventi extends Serializable {

    String getDsKeyOggetto();

    Timestamp getDtRegEvento();

    BigDecimal getIdAgente();

    BigDecimal getIdAgenteEvento();

    BigDecimal getIdApplic();

    BigDecimal getIdEvento();

    BigDecimal getIdOggetto();

    BigDecimal getIdOggettoEvento();

    BigDecimal getIdOrganizApplic();

    BigDecimal getIdTipoEvento();

    BigDecimal getIdTipoOggetto();

    BigDecimal getIdTipoOggettoPadre();

    BigDecimal getIdTransazione();

    String getNmAgente();

    String getNmAmbiente();

    String getNmApplic();

    String getNmAzione();

    String getNmEnte();

    String getNmGeneratoreAzione();

    String getNmOggetto();

    String getNmStruttura();

    String getNmTipoEvento();

    String getNmTipoOggetto();

    String getNmTipoOggettoPadre();

    String getNmVersatore();

    String getTiRuoloAgenteEvento();

    String getTipoAzione();

    String getTipoClasseEvento();

    String getTipoOrigineAgente();

    String getDsMotivoScript();

    void setDsKeyOggetto(String dsKeyOggetto);

    void setDtRegEvento(Timestamp dtRegEvento);

    void setIdAgente(BigDecimal idAgente);

    void setIdAgenteEvento(BigDecimal idAgenteEvento);

    void setIdApplic(BigDecimal idApplic);

    void setIdEvento(BigDecimal idEvento);

    void setIdOggetto(BigDecimal idOggetto);

    void setIdOggettoEvento(BigDecimal idOggettoEvento);

    void setIdOrganizApplic(BigDecimal idOrganizApplic);

    void setIdTipoEvento(BigDecimal idTipoEvento);

    void setIdTipoOggetto(BigDecimal idTipoOggetto);

    void setIdTipoOggettoPadre(BigDecimal idTipoOggettoPadre);

    void setIdTransazione(BigDecimal idTransazione);

    void setNmAgente(String nmAgente);

    void setNmAmbiente(String nmAmbiente);

    void setNmApplic(String nmApplic);

    void setNmAzione(String nmAzione);

    void setNmEnte(String nmEnte);

    void setNmGeneratoreAzione(String nmGeneratoreAzione);

    void setNmOggetto(String nmOggetto);

    void setNmStruttura(String nmStruttura);

    void setNmTipoEvento(String nmTipoEvento);

    void setNmTipoOggetto(String nmTipoOggetto);

    void setNmTipoOggettoPadre(String nmTipoOggettoPadre);

    void setNmVersatore(String nmVersatore);

    void setTiRuoloAgenteEvento(String tiRuoloAgenteEvento);

    void setTipoAzione(String tipoAzione);

    void setTipoClasseEvento(String tipoClasseEvento);

    void setTipoOrigineAgente(String tipoOrigineAgente);

    void setDsMotivoScript(String dsMotivoScript);
}
