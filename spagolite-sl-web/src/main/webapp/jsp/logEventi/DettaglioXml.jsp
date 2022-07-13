<%@ page import="it.eng.parer.sacerlog.slite.gen.form.GestioneLogEventiForm" pageEncoding="UTF-8"%>
<%@ include file="../../include.jsp"%>

<sl:html>
    <sl:head title="Dettaglio xml" >
         <script src="<c:url value='/js/help/inithighlightingjs.js' />" type="text/javascript"></script>    
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
            </slf:fieldSet>
		</sl:content>
        <sl:footer />
    </sl:body>
</sl:html>
