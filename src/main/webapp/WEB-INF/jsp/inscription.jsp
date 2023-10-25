<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %> <!-- Pour le jeton crsf -->

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
            <h6 class="m-0 font-weight-bold text-primary">Inscription</h6>
        </div>
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/inscription" method="post">
                <div class="form-group">
                    <label for="nomClient">Nom :</label>
                    <input type="text" class="form-control" id="nomClient" name="nom" required>
                </div>

                <div class="form-group">
                    <label for="prenomClient">Prénom :</label>
                    <input type="text" class="form-control" id="prenomClient" name="prenom" required>
                </div>

                <div class="form-group">
                    <label for="adresseClient">Adresse :</label>
                    <input type="text" class="form-control" id="adresseClient" name="adresse" required>
                </div>

                <div class="form-group">
                    <label for="telephoneClient">Téléphone :</label>
                    <input type="text" class="form-control" id="telephoneClient" name="telephone" required>
                </div>

                <div class="form-group">
                    <label for="courriel">Courriel :</label>
                    <input type="email" class="form-control" id="courriel" name="courriel" required>
                </div>

                <div class="form-group">
                    <label for="motPasse">Mot de Passe :</label>
                    <input type="password" class="form-control" id="motPasse" name="motPasse" required>
                </div>

                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />


                <button type="submit" class="btn btn-primary">S'inscrire</button>
            </form>
        </div>
    </div>
</div>
</body>
