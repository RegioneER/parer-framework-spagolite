<%@ page import="org.springframework.security.saml.metadata.MetadataManager" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="java.util.Set" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<%
    WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletConfig().getServletContext());
    MetadataManager mm = context.getBean("metadata", MetadataManager.class);
    Set<String> idps = mm.getIDPEntityNames();
    pageContext.setAttribute("idp", idps);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="it" dir="ltr" lang="it">
    <head>
        <title>Accedi con</title>
        <meta http-equiv="Content-Language" content="it" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <link rel="shortcut icon" type="image/x-icon" href="<%= request.getContextPath()%>/img/regione/favicon.ico" />
        <link href="<%= request.getContextPath()%>/css/slForms.css" rel="stylesheet" type="text/css" />
        <link href="<%= request.getContextPath()%>/css/slForms-over.css" rel="stylesheet" type="text/css" />
        <link href="<%= request.getContextPath()%>/css/slScreen.css" rel="stylesheet" type="text/css" media="screen" />
    </head>
    <body>
        <div class="header">
            <img class="floatLeft" src="<%=request.getContextPath()%>/img/logo_sacer_small.png" alt="Logo">
            <a href="http://parer.ibc.regione.emilia-romagna.it" title="ParER - Polo Archivistico Regionale dell'Emilia-Romagna">
                <img class="floatRight" src="<%=request.getContextPath()%>/img/regione/LogoParer.png" alt="Logo">
            </a>
            <div class="newLine"></div>
        </div>        
        
        <div id="loginPage" class="center">

            <form action="<c:url value="${requestScope.idpDiscoReturnURL}"/>" method="GET">
                <fieldset>
                    <div class="wizardTitle">
                        <span>Accedi con</span>
                        <table align="center">
                            <c:forEach var="idpItem" items="${idp}">
                                <tr>
                                    <td align="center" >
                                        <BR/>
                                        <c:if test="${fn:contains(idpItem, '.lepida.it')}">
                                            <div style="border: 1px solid grey; border-radius: 5px; border-opacity: 0.6;">
                                                <a href="<c:url value="${requestScope.idpDiscoReturnURL}?${requestScope.idpDiscoReturnParam}=${idpItem}"/>" >
                                                    <img src="<%= request.getContextPath()%>/img/EntraConSpid.png" alt="Logo" width="340px" height="85px"/>
                                                </a>
                                            </div>
                                        </c:if>
                                        <c:if test="${not fn:contains(idpItem, '.lepida.it') and not fn:contains(idpItem, 'localhost:')}">
                                            <div style="border: 1px solid grey; border-radius: 5px; border-opacity: 0.6;">
                                                <a href="<c:url value="${requestScope.idpDiscoReturnURL}?${requestScope.idpDiscoReturnParam}=${idpItem}"/>">
                                                    <img src="<%= request.getContextPath()%>/img/LogoIdp.png" alt="Logo" width="340px" height="85px"/>
                                                </a>  
                                            </div>
                                        </c:if>
                                        <c:if test="${not fn:contains(idpItem, '.lepida.it') and fn:contains(idpItem, 'localhost:')}">
                                            <a href="<c:url value="${requestScope.idpDiscoReturnURL}?${requestScope.idpDiscoReturnParam}=${idpItem}"/>">
                                                <c:out value="${idpItem}"/></a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>
                    <div class="newLine skipLine"></div>
                </fieldset>
                <div class="newLine skipLine"></div>
                <div class="newLine skipLine"></div>
            </form>
        </div>
        <div class="newLine skipLine"></div>
        <div class="newLine skipLine"></div>
        <!--Footer-->
        <div class="footer">
            <div class="left">
                <a href="http://www.regione.emilia-romagna.it/" title="Regione Emilia-Romagna">
                    <img src="<%=request.getContextPath()%>/img/regione/LogoER.png" alt="Regione Emilia-Romagna"/>
                </a>
            </div>
            <div class="right">
                <a href="http://www.ibc.regione.emilia-romagna.it/" title="Istituto per i beni artistici culturali e naturali della Regione Emilia-Romagna">
                    <img src="<%=request.getContextPath()%>/img/regione/LogoIbc.png" alt="Logo IBC"/>
                </a>
            </div>
        </div>
    </body>
</html>