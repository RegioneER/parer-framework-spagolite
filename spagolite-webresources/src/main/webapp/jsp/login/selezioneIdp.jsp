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

<%@ page import="it.eng.spagoCore.ConfigSingleton"%>
<%@ page import="it.eng.spagoLite.spring.ParerSecurityConfiguration"%>
<%@ page import="java.util.Set" %>
<%@ include file="../../include.jsp"%>
        
<jsp:useBean id="idps" class="java.util.List" scope="request" />
<%
   pageContext.setAttribute("spidProfessionaleEnabled",System.getProperty("spid-professionale-enabled"));
%>

<sl:html>
    <sl:head title="Accedi con" /> 
     <sl:body>
       <sl:header  showHomeBtn="false"/>
        <div id="loginPage" class="center">
            <form action="" method="GET">
                <fieldset>
                    <div class="wizardTitle">
                        <span>Accedi con</span>
                        <table align="center">
                            <c:forEach items="${idps}" var="b">
                                <tr>
                                    <td align="center" >
                                        <BR/>
                                        <c:if test="${fn:contains(b.idpEntityId, '.lepida.it')}">
                                            <div style="border: 1px solid grey; border-radius: 5px; border-opacity: 0.6;">
                                                <a href="<c:url value="${b.link}" />">
                                                    <img src="<%=request.getContextPath()%>/img/EntraConSpidCie.png" alt="Logo" width="340px" height="85px"/>
                                                </a>
                                            </div>
                                        </c:if>
                                        <BR/>
                                        <c:if test="${spidProfessionaleEnabled == 'true'}">
                                            <c:if test="${fn:contains(b.idpEntityId, '.lepida.it')}">
                                                <div style="border: 1px solid grey; border-radius: 5px; border-opacity: 0.6;">
                                                    <a href="<c:url value="${b.link}"/>?<%=ParerSecurityConfiguration.PARAMETRO_MODALITA_SPID_PROFESSIONALE%>=<%=ParerSecurityConfiguration.MODALITA_SPID_PROFESSIONALE%>">
                                                        <img src="<%=request.getContextPath()%>/img/EntraConSpid-PX-Nuovo.png" alt="Logo" width="340px" height="85px"/>
                                                    </a>
                                                </div>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${not fn:contains(b.idpEntityId, '.lepida.it') and (not fn:contains(b.idpEntityId, 'localhost:')) }">
                                            <div style="border: 1px solid grey; border-radius: 5px; border-opacity: 0.6;">
                                                <a href="<c:url value="${b.link}"/>">
                                                    <img src="<%=request.getContextPath()%>/img/LogoIdp.png" alt="Logo" width="340px" height="85px"/>
                                                </a>
                                            </div>
                                        </c:if>
                                        <c:if test="${not fn:contains(b.idpEntityId, '.lepida.it') and fn:contains(b.idpEntityId, 'localhost:')}">
                                            <a href="<c:url value="${b.link}"/>">
                                                <c:out value="${b.linkDescription}"/></a>
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
