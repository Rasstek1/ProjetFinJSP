<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ include file="header.jsp" %>
<!DOCTYPE html>
<html>
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
    <div class="row">
        <!-- Formulaire à gauche -->
        <div class="col-md-6">
            <div class="card shadow mb-4 form-container">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">Ajout de Chalet</h6>
                </div>
                <div class="card-body">
                    <!-- Le formulaire pour ajouter un chalet -->
                    <form action="ajouterChalet" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <div class="mb-3">
                            <label for="nombreChambres" class="form-label">Nombre de chambres :</label>
                            <input type="number" id="nombreChambres" name="nombreChambres" class="form-control"
                                   value="${chalet.nombreChambres}" min="1" />
                        </div>

                        <div class="mb-3">
                            <label for="description" class="form-label">Description :</label>
                            <textarea id="description" name="description" class="form-control">${chalet.description}</textarea>
                        </div>
                        <div class="mb-3">
                            <label for="prix" class="form-label">Prix :</label>
                            <input type="text" id="prix" name="prix" class="form-control" value="${chalet.prix}"/>
                        </div>

                        <div class="mb-3">
                            <label for="photosInput" class="form-label">Télécharger des photos :</label>
                            <input type="file" name="photos" id="photosInput" class="form-control" multiple/>
                        </div>

                        <div class="d-flex justify-content-center mt-3">
                            <input type="submit" value="Ajouter le chalet" class="btn btn-primary"/>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- Tableau à droite -->
        <div class="col-md-6">
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">Liste des Chalets</h6>
                </div>
                <div class="card-body">
                    <!-- Tableau avec les miniatures des chalets, numéros et boutons Modifier/Supprimer -->
                    <table class="table">
                        <thead>
                        <tr>
                            <th>Numéro</th>
                            <th>Miniature</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>

                        <!-- Utilisez une boucle pour afficher chaque chalet -->
                        <c:forEach var="chalet" items="${chalets}">
                            <tr>
                                <td>${chalet.numChalet}</td>
                                <td>
                                    <img src="uploads/${chalet.listePhotos[0]}" alt="Photo du chalet ${chalet.numChalet}">
                                </td>
                                <td>
                                    <a href="<c:url value='/modifierChalet/${chalet.numChalet}'/>" class="btn btn-primary">Modifier</a>
                                    <a href="<c:url value='/supprimerChalet/${chalet.numChalet}'/>" class="btn btn-danger">Supprimer</a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

    </div>
</div>

<%@ include file="footer.jsp" %>

</body>
</html>
