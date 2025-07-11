<%@page import="it.eng.spagoCore.ConfigSingleton"%>
<%@ include file="../../include.jsp"%>

<sl:html>
    <sl:head title="Logout eseguito" />
    <sl:body>
        <sl:header  showHomeBtn="false"/>
        <div class="newLine "></div>
        <%
            // per sicurezza invalido la sessione!
            session.invalidate();
        %>
        <div id="menu"></div>
        <div id="content">
            <!-- Message Box -->
            <div class="messages  plainInfo">
                <ul>
                    <li class="message  info  ">Logout eseguito correttamente. <a href="./Login.html" title="Vai alla pagina di login ">Effettua un nuovo accesso</a></li>
                </ul>
            </div>
        </div>
        
        <!--Footer-->
        <sl:footer />
    </sl:body>
</sl:html>
