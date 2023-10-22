<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="header.jsp" %>

<h1>Réservation du chalet</h1>
<form action="ChaletController?method=confirmReserverChalet" method="post">
    Nom: <input type="text" name="nomClient">
    Courriel: <input type="email" name="courriel">
    Date de réservation: <input type="date" name="dateReservation">
    Durée: <input type="number" name="duree">
    <input type="submit" value="Confirmer la réservation">
</form>

<%@ include file="footer.jsp" %>
