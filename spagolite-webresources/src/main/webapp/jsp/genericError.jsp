<%@page import="it.eng.spagoCore.ConfigSingleton"%>
<%@ include file="../../include.jsp"%>

<sl:html>
    <sl:head title="Errore generico" />
    <sl:body>
        <sl:header description="Errore generico" showHomeBtn="false"/>
        <div class="newLine "></div>

        <div id="menu">&nbsp;</div>
        <div id="content">
            <!-- Messaggio di errore (se presente) -->
            <c:set var="message" value='${requestScope["errorMessage"]}' />
            <!-- Message Box -->
            <div class="messages  plainError ">
                <ul>
                    <li class="message  error ">Si sono verificati dei problemi tecnici <strong>${requestScope["javax.servlet.error.request_uri"]}</strong>. <a href="./Home.html" title="Home">Torna alla home</a> </li>
                    <c:if test="${ not empty message }">
                        <li class="message  error ">${message}</li>
                    </c:if>
                </ul>
            </div>
        </div>
        <!--Footer-->
        <sl:footer />
    </sl:body>
</sl:html>
