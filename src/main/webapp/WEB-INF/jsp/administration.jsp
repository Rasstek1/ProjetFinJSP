<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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
<div class="content">
    <div class="card shadow mb-4 form-container">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Ajout de Chalet</h6>
        </div>
        <div class="card-body">
            <p class="mb-4">Remplissez les champs ci-dessous pour ajouter un nouveau chalet.</p>

            <!-- Le formulaire pour ajouter un chalet -->
            <form action="ajouterChalet" method="post" enctype="multipart/form-data">


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
                    <label for="photosInput" class="form-label">Télécharger d'autres photos :</label>
                    <input type="file" name="photos" id="photosInput" class="form-control" multiple/>
                </div>


                <div class="d-flex justify-content-center mt-3">
                    <input type="submit" value="Ajouter le chalet" class="btn btn-primary"/>
                </div>
            </form>
        </div>
    </div>
</div>

<%@ include file="footer.jsp" %>

</body>
</html>
