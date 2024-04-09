<%@ page import="org.springframework.security.saml.metadata.MetadataManager" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="it.eng.spagoCore.configuration.ConfigSingleton"%>
<%@ page import="java.util.Set" %>
<%@ include file="../../include.jsp"%>

<%
    WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletConfig().getServletContext());
    MetadataManager mm = context.getBean("metadata", MetadataManager.class);
    Set<String> idps = mm.getIDPEntityNames();
    pageContext.setAttribute("idp", idps);
    pageContext.setAttribute("spidProfessionaleEnabled",System.getProperty("spid-professionale-enabled"));
%>

<sl:html>
    <sl:head title="Accedi con" /> 
     <sl:body>
       <sl:header  showHomeBtn="false"/>
        
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
                                        <BR/>

                                        <c:if test="${spidProfessionaleEnabled == 'true'}">
                                            <c:if test="${fn:contains(idpItem, '.lepida.it')}">
                                                <div style="border: 1px solid grey; border-radius: 5px; border-opacity: 0.6;">
                                                    <a href="<c:url value="${requestScope.idpDiscoReturnURL}?${requestScope.idpDiscoReturnParam}=${idpItem}&SlitePur=PX"/>" >
                                                        <img src="<%= request.getContextPath()%>/img/EntraConSpid-PX.png" alt="Logo" width="340px" height="85px"/>
                                                    </a>
                                                </div>
                                            </c:if>
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
	        
        <sl:footer />
    </sl:body>
</sl:html>