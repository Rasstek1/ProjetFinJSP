<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="header.jsp" %>

<h1>Photos du chalet</h1>
<img src="${photoCourante}" alt="Photo du chalet">
<button onclick="previousPhoto()">Précédent</button>
<button onclick="nextPhoto()">Suivant</button>

<script>
    function previousPhoto() {
        // Logique pour montrer la photo précédente
    }

    function nextPhoto() {
        // Logique pour montrer la photo suivante
    }
</script>

<%@ include file="footer.jsp" %>
