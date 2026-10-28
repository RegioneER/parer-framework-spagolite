<%--
 Engineering Ingegneria Informatica S.p.A.

 Copyright (C) 2023 Regione Emilia-Romagna
 <p/>
 This program is free software: you can redistribute it and/or modify it under the terms of
 the GNU Affero General Public License as published by the Free Software Foundation,
 either version 3 of the License, or (at your option) any later version.
 <p/>
 This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU Affero General Public License for more details.
 <p/>
 You should have received a copy of the GNU Affero General Public License along with this program.
 If not, see <https://www.gnu.org/licenses/>.
 --%>

<%@ page import="it.eng.parer.sacerlog.slite.gen.form.GestioneLogEventiForm" pageEncoding="UTF-8"%>
<%@ include file="../../include.jsp"%>

<sl:html>
    <sl:head title="Dettaglio transazione" />
    <sl:body>
        <sl:header showChangeOrganizationBtn="false" />
        <sl:menu showChangePasswordBtn="true" />
        <sl:content>
            <slf:messageBox />
            <sl:newLine skipLine="true"/>
            <slf:fieldBarDetailTag name="<%=GestioneLogEventiForm.OggettoDetail.NAME%>" hideBackButton="false" />
            <sl:newLine skipLine="true"/>
            <sl:contentTitle title="Dettaglio transazione" />
            <slf:fieldSet  borderHidden="false">
                <slf:lblField name="<%=GestioneLogEventiForm.EventoPrincipale.ID_TRANSAZIONE%>" colSpan="2" />
                <sl:newLine />
                <slf:lblField name="<%=GestioneLogEventiForm.EventoPrincipale.DT_REG_EVENTO%>" colSpan="2" />
                <slf:lblField name="<%=GestioneLogEventiForm.EventoPrincipale.NM_TIPO_EVENTO%>" colSpan="2" />
                <sl:newLine />
                <slf:lblField name="<%=GestioneLogEventiForm.EventoPrincipale.AZIONE_COMPOSITA%>" colSpan="2" />
                <c:if test="${mostraAgente == 'true'}">
                    <slf:lblField name="<%=GestioneLogEventiForm.EventoPrincipale.NM_AGENTE%>" colSpan="2" />
                </c:if>
                <sl:newLine />
            </slf:fieldSet>
            <sl:pulsantiera>
                <slf:lblField name="<%=GestioneLogEventiForm.EventoPrincipale.VISUALIZZA_DATI_COMPLETI_TRANSAZIONE%>" colSpan="2" position="left" />
            </sl:pulsantiera>
            <sl:newLine skipLine="true" />

            <sl:contentTitle title="Lista oggetti evento principale" />
            <slf:list name="<%=GestioneLogEventiForm.OggettiEventoPrincipaleList.NAME%>" />
            <slf:listNavBar  name="<%=GestioneLogEventiForm.OggettiEventoPrincipaleList.NAME%>" />
            <sl:newLine skipLine="true" />

            <sl:contentTitle title="Lista eventi successivi" />
            <slf:list name="<%=GestioneLogEventiForm.EventiSuccessiviList.NAME%>" />
            <slf:listNavBar  name="<%=GestioneLogEventiForm.EventiSuccessiviList.NAME%>" />
            <sl:newLine skipLine="true" />

        </sl:content>
        <sl:footer />
    </sl:body>
</sl:html>
