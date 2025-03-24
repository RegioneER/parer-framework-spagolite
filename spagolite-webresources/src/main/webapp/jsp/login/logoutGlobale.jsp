<!-- Pagina JSP utilizzata per gestire il logout SAML2 secondo le nuove specifiche -->
<!-- che vogliono che all'interno dell'applicazione venga fatta una chiamata POST (e non più GET) -->
<!-- a {contextPath}/saml2/logout altrimenti non scatta la generazione della richiesta SAML2 verso -->
<!-- l'IDP. In sostanza è una pagina vuota che fa automaticamente un POST all'atto del load della pagina. -->
<!-- -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="it">
    <form id="myForm" action="${pageContext.servletContext.contextPath}/logout" method="post">
        <noscript>	
        <input type="submit" value="Clicca qui per eseguire il logout."/>
        </noscript>
    </form>
    <script type="text/javascript">
        document.getElementById('myForm').submit();
    </script>
</html>