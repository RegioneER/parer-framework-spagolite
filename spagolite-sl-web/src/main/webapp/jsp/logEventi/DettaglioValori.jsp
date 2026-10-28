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
    <sl:head title="Dettaglio valori">
       <script src="<c:url value='/js/help/inithighlightingjs.js' />" type="text/javascript"></script>    
    </sl:head>
    <sl:body>
        <sl:header showChangeOrganizationBtn="false" />
        <sl:menu showChangePasswordBtn="true" />
        <sl:content>
            <slf:messageBox />
            <sl:newLine skipLine="true"/>
            <sl:contentTitle title="Dettaglio valori" />
            <slf:fieldBarDetailTag name="<%=GestioneLogEventiForm.ValoreDetail.NAME%>" hideBackButton="false" />
            <sl:newLine skipLine="true" />
            <slf:fieldSet borderHidden="false" >
                <sl:newLine />
                <slf:lblField name="<%=GestioneLogEventiForm.ValoreDetail.DS_VECCHIO_VALORE%>" colSpan="5" />
                <sl:newLine />
                <slf:lblField name="<%=GestioneLogEventiForm.ValoreDetail.DS_NUOVO_VALORE%>" colSpan="5" />
                <sl:newLine />
                <slf:lblField name="<%=GestioneLogEventiForm.ValoreDetail.CL_VECCHIO_VALORE%>" colSpan="4" controlWidth="w100" />
                <sl:newLine />
                <slf:lblField name="<%=GestioneLogEventiForm.ValoreDetail.CL_NUOVO_VALORE%>" colSpan="4" controlWidth="w100" />
                <sl:newLine />
            </slf:fieldSet>
        </sl:content>
        <sl:footer />
    </sl:body>
</sl:html>
