<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="header.jsp" %>

<head>
    <style>
        .form-container {
            max-width: 600px;
            margin: 0 auto;
        }
    </style>
</head>

<body>
<div class="content">
    <div class="card shadow mb-4 form-container">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Reservation</h6>
        </div>
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/confirmReserverChalet" method="post">
                <div class="form-group">
                    <label for="nomClient">Nom du Client :</label>
                    <input type="text" class="form-control" id="nomClient" name="nomClient" required>
                </div>

                <div class="form-group">
                    <label for="courriel">Courriel :</label>
                    <input type="email" class="form-control" id="courriel" name="courriel" required>
                </div>

                <div class="form-group">
                    <label for="dateReservation">Date de Réservation :</label>
                    <input type="date" class="form-control" id="dateReservation" name="dateReservation" required>
                </div>

                <div class="form-group">
                    <label for="duree">Durée de la Réservation (en jours) :</label>
                    <input type="number" class="form-control" id="duree" name="duree" required min="1">
                </div>

                <input type="hidden" name="numChalet" value="${chalet.numChalet}">

                <button type="submit" class="btn btn-primary">Confirmer la Réservation</button>
            </form>
        </div>
    </div>
</div>
</body>


        <%@ include file="footer.jsp" %>
