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
    <sl:head title="Dettaglio evento per oggetto" />
    <sl:body>
        <sl:header showChangeOrganizationBtn="false" />
        <sl:menu showChangePasswordBtn="true" />
        <sl:content>
            <slf:messageBox />
            <sl:newLine skipLine="true"/>
            <slf:listNavBarDetail name="${oggListaNav}" />  
            <sl:newLine skipLine="true"/>
            <sl:contentTitle title="Dettaglio evento per oggetto" />
            <sl:newLine />
            <slf:fieldSet borderHidden="false">
                <slf:lblField name="<%=GestioneLogEventiForm.OggettoDetail.NM_TIPO_OGGETTO%>" colSpan="2" />
                <slf:lblField name="<%=GestioneLogEventiForm.OggettoDetail.NM_OGGETTO%>" colSpan="2" />
                <sl:newLine />
                <c:if test="${sessionScope['###_FORM_CONTAINER']['oggettoDetail']['nm_ambiente'].value != null}">
                    <slf:lblField name="<%=GestioneLogEventiForm.OggettoDetail.NM_AMBIENTE%>" colSpan="2" />
                    <sl:newLine />
                </c:if>

                <c:if test="${sessionScope['###_FORM_CONTAINER']['oggettoDetail']['nm_versatore'].value != null}">
                    <slf:lblField name="<%=GestioneLogEventiForm.OggettoDetail.NM_VERSATORE%>" colSpan="2" />
                    <sl:newLine />
                </c:if>
                    
                <c:if test="${sessionScope['###_FORM_CONTAINER']['oggettoDetail']['nm_ente'].value != null}">
                    <slf:lblField name="<%=GestioneLogEventiForm.OggettoDetail.NM_ENTE%>" colSpan="2" />
                    <sl:newLine />
                </c:if>
                    
                <c:if test="${sessionScope['###_FORM_CONTAINER']['oggettoDetail']['nm_strut'].value != null}">
                    <slf:lblField name="<%=GestioneLogEventiForm.OggettoDetail.NM_STRUT%>" colSpan="2" />
                </c:if>
                <sl:newLine />
            </slf:fieldSet>
            <sl:newLine skipLine="true"/>
            <h2 class="titleFiltri">Evento</h2>
            <slf:fieldSet borderHidden="false">
                <slf:lblField name="<%=GestioneLogEventiForm.EventoDetail.DT_REG_EVENTO%>" colSpan="2" />
                <slf:lblField name="<%=GestioneLogEventiForm.EventoDetail.NM_TIPO_EVENTO%>" colSpan="2" />
                <sl:newLine />
                <slf:lblField name="<%=GestioneLogEventiForm.EventoDetail.CALC_AZIONE%>" colSpan="2" />
                <c:if test="${mostraAgente == 'true'}">
                    <slf:lblField name="<%=GestioneLogEventiForm.EventoDetail.NM_AGENTE%>" colSpan="2"  />
                </c:if>    
                <sl:newLine />
                <slf:lblField name="<%=GestioneLogEventiForm.EventoDetail.CALC_RUOLI%>" colSpan="2"  />
                <slf:lblField name="<%=GestioneLogEventiForm.EventoDetail.ID_TRANSAZIONE%>" colSpan="1"  />
                <sl:newLine />
            </slf:fieldSet>
            <sl:pulsantiera>
                <slf:lblField name="<%=GestioneLogEventiForm.EventoDetail.VISUALIZZA_FOTO%>" colSpan="2" position="left" />
                <slf:lblField name="<%=GestioneLogEventiForm.EventoDetail.VISUALIZZA_DATI_COMPLETI%>" colSpan="2" position="left" />
            </sl:pulsantiera>
            <sl:newLine skipLine="true"/>
            <h2 class="titleFiltri">Lista modifiche</h2>
            <slf:list name="<%=GestioneLogEventiForm.ListaModifiche.NAME%>" />
            <sl:newLine skipLine="true"/>

            <slf:listNavBar  name="<%=GestioneLogEventiForm.ListaModifiche.NAME%>" />
            
        </sl:content>
        <sl:footer />
    </sl:body>
</sl:html>
