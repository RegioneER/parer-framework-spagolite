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
