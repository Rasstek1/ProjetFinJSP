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
<div class="content" id="content">
    <div class="card shadow mb-4 form-container">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Reservation</h6>
        </div>




        <div class="card-body">
            <c:if test="${param.error == 'true'}">
                <div class="alert alert-danger" role="alert">
                    <p>Ce chalet n'est pas disponible pour les dates sélectionnées.</p>
                </div>
            </c:if>


            <form action="${pageContext.request.contextPath}/confirmation" method="post">
                <input type="hidden" name="numChalet" value="${chalet.numChalet}" />
                <input type="hidden" name="prix" value="${chalet.prix}" />


                <div class="form-group">
                    <label for="nomClient">Prénom :</label>
                    <input type="text" class="form-control" id="nomClient" name="nomClient" value="${nomClient}" required>
                </div>

                <div class="form-group">
                    <label for="telephone">Téléphone :</label>
                    <input type="text" class="form-control" id="telephone" name="telephone" value="${telephone}" required>
                </div>

                <div class="form-group">
                    <label for="courriel">Courriel :</label>
                    <input type="email" class="form-control" id="courriel" name="courriel" value="${courriel}" required>
                </div>

                <div class="form-group">
                    <label for="startDate">Date de Réservation :</label>
                    <input type="date" class="form-control" id="startDate" name="startDate" required>
                </div>


                <div class="form-group">
                    <label for="duree">Durée de la Réservation (en jours) :</label>
                    <input type="number" class="form-control" id="duree" name="duree" required min="1">
                </div>

                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />


                <button type="submit" class="btn btn-primary">Confirmer la Réservation</button>
            </form>
        </div>
    </div>
</div>
</body>


        <%@ include file="footer.jsp" %>
