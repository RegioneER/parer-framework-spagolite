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
    <sl:head title="Dettaglio xml" >
        <script src="<c:url value='/js/help/inithighlightingjs.js' />" type="text/javascript"></script>    
        <script type='text/javascript' >
            $(document).ready(function () {
                $("[name='operation__scarica_xml']").click(function () {
                    $('.overlay').hide();
                });
            });
        </script>
    </sl:head>
    <sl:body>
        <sl:header showChangeOrganizationBtn="false" />
        <sl:menu showChangePasswordBtn="true" />
        <sl:content> 
            <slf:messageBox />
            <sl:newLine skipLine="true"/>
            <sl:contentTitle title="Dettaglio xml" />
            <slf:fieldBarDetailTag name="<%=GestioneLogEventiForm.ValoreXml.NAME%>" hideBackButton="false" />
            <slf:fieldSet borderHidden="false" >
                <slf:lblField name="<%=GestioneLogEventiForm.ValoreXml.CL_XML%>" colSpan="4" controlWidth="w100" />
                <sl:newLine />
                <slf:lblField name="<%=GestioneLogEventiForm.ValoreXml.SCARICA_XML%>" colSpan="4" controlWidth="w100" />
                <sl:newLine />
            </slf:fieldSet>
        </sl:content>
        <sl:footer />
    </sl:body>
</sl:html>
