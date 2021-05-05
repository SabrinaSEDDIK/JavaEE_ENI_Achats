<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.time.LocalDate"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!-- JQuery -->
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"
	integrity="sha512-894YE6QWD5I59HgZOGReFYm4dnWc1Qt5NtvYSaNcOP+u1T9qYdvdihz0PPSiiqn/+/3e7Jo4EaG7TubfWGUrMQ=="
	crossorigin="anonymous"></script>
<!-- Bootstrap -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/css/bootstrap.min.css"
	integrity="sha384-B0vP5xmATw1+K9KRQjQERJvTumQW0nPEzvF6L/Z6nronJ3oUOFUFpCjEUQouq2+l"
	crossorigin="anonymous">
<!-- CSS -->
<link rel="stylesheet" href="inc/accueil.css">

<title>ENI Enchères - Nouvelle vente</title>
</head>

<body>
	<!-- HEADER -->
	<header class="container-fluid pt-4 px-4 pb-4">
		<div class="space-between">
			<div class="me-3">
				<a href="ServletAccueil"><img src="img/logoEniEncheres.png" /></a>
			</div>
			<div style="text-align: right;">
				<div>${infosUtilisateur.pseudo } est connecté(e)</div>
				<a href="ServletAccueil">Enchères</a> &nbsp; <a
					href="ServletNouvelleVente">Vendre un article</a> &nbsp; <a
					href="ServletAfficherProfil?id=${infosUtilisateur.idUtilisateur}">Mon
					profil</a> &nbsp; <a href="ServletDeconnexion">Déconnexion</a>
			</div>
		</div>
	</header>
	<!-- MAIN -->
	<main class="row container">

		<!-- apercu image -->
		<figure class="col-4 figure">
			<img src="img/indisponible.png" id="img"
				class="figure-img img-fluid rounded">
		</figure>
		<div class="col-8">

			<h1 class="bleu">Nouvelle vente</h1>

			<!-- Messages d'erreurs éventuels -->
			<c:if test="${not empty listeErreurs }">
				<div class="alert alert-danger" role="alert">
					<c:forEach var="erreur" items="${listeErreurs}">
						${erreur}
					</c:forEach>
				</div>
			</c:if>


			<!-- Formulaire -->
			<c:choose>
				<c:when test="${ enchere.article != null }">
					<c:url value="ServletModificationEnchere" var="urlEnregistrement">
						<c:param name="idArticle" value="${ param.idArticle }" />
					</c:url>
				</c:when>
				<c:otherwise>
					<c:url value="ServletEnregistrementArticle" var="urlEnregistrement" />
				</c:otherwise>
			</c:choose>


			<form action="${ urlEnregistrement }" method="post">
				<label class="form-label" for="nomArticle">Article :</label> <input
					type="text" class="form-control" id="nomArticle" name="nomArticle"
					size="35" maxlength="30"
					value="<c:out value="${ enchere.article.nom }"/>" required>

				<label class="form-label pt-3" for="description">Description
					:</label>

				<textarea class="form-control" id="description" name="description"
					rows="4" cols="46" maxlength="300" required><c:out
						value="${ enchere.article.description }" /></textarea>
				<label class="form-label pt-3" for="categories">Catégorie :
				</label> <select id="categories" name="categorie" class="form-control">
					<c:choose>
						<c:when test="${ enchere.article.idCategorie == 1 }">
							<option value="0">- - -</option>
							<option value="1" selected>Informatique</option>
							<option value="2">Ameublement</option>
							<option value="3">Vêtement</option>
							<option value="4">Sport &amp; Loisirs</option>
						</c:when>
						<c:when test="${ enchere.article.idCategorie == 2 }">
							<option value="0">- - -</option>
							<option value="1">Informatique</option>
							<option value="2" selected>Ameublement</option>
							<option value="3">Vêtement</option>
							<option value="4">Sport &amp; Loisirs</option>
						</c:when>
						<c:when test="${ enchere.article.idCategorie == 3 }">
							<option value="0">- - -</option>
							<option value="1">Informatique</option>
							<option value="2">Ameublement</option>
							<option value="3" selected>Vêtement</option>
							<option value="4">Sport &amp; Loisirs</option>
						</c:when>
						<c:when test="${ enchere.article.idCategorie == 4 }">
							<option value="0">- - -</option>
							<option value="1">Informatique</option>
							<option value="2">Ameublement</option>
							<option value="3">Vêtement</option>
							<option value="4" selected>Sport &amp; Loisirs</option>
						</c:when>
						<c:otherwise>
							<option value="0">- - -</option>
							<option value="1">Informatique</option>
							<option value="2">Ameublement</option>
							<option value="3">Vêtement</option>
							<option value="4">Sport &amp; Loisirs</option>
						</c:otherwise>
					</c:choose>
				</select> <label class="form-label pt-3" for="photo">Photo : &nbsp;</label> <br>
				<input type="file" id="photo" name="photoArticle" accept="image/*">
				<br> <label class="form-label pt-2" for="prixInitial">Mise
					à prix : </label> <input type="number" class="form-control" min="0"
					step="1" id="number" name="prixInitial"
					value="${ enchere.article.miseAPrix }" placeholder="pts"> <label
					class="form-label pt-3" for="debutEnchere">Début de
					l'enchère : </label>
				<c:choose>
					<c:when test="${ enchere.article.dateDebutEnchere != null }">
						<input id="debutEnchere" class="form-control" type="date"
							name="debutEnchere" min="${LocalDate.now()}" value="${enchere.article.dateDebutEnchere}"
							required>
					</c:when>
					<c:otherwise>
						<input id="debutEnchere" class="form-control" type="date"
							name="debutEnchere" min="${LocalDate.now()}"
							value="${LocalDate.now()}" required>
					</c:otherwise>
				</c:choose>

				<label class="form-label pt-3" for="finEnchere">Fin de
					l'enchère : </label>
				<c:choose>
					<c:when test="${ enchere.article.dateFinEnchere != null }">
						<input id="finEnchere" type="date" class="form-control"
							name="finEnchere" value="${enchere.article.dateFinEnchere}"
							required>
					</c:when>
					<c:otherwise>
						<input id="finEnchere" type="date" class="form-control"
							name="finEnchere" min="${LocalDate.now().plusDays(1)}"
							value="${LocalDate.now().plusDays(1)}" required>
					</c:otherwise>
				</c:choose>

				<br>
				<fieldset>
					<legend>Retrait</legend>

					<label class="form-label" for="rue">Rue :</label> <input
						type="text" class="form-control" id="rue" name="rue" size="40"
						maxlength="30" value="${infosUtilisateur.getRue()}" required>

					<label class="form-label pt-3" for="codePostal">Code postal
						:</label> <input type="text" class="form-control" id="codePostal"
						name="codePostal" size="10" maxlength="15"
						value="${infosUtilisateur.getCodePostal() }" required> <label
						class="form-label pt-3" for="ville">Ville :</label> <input
						type="text" class="form-control" id="ville" name="ville" size="40"
						maxlength="30" value="${infosUtilisateur.getVille() }" required>
				</fieldset>
				<br>

				<button class="btn btn-secondary" type="submit" name="choix"
					value="enregistrer">Enregistrer</button>
				<a href="ServletAccueil"><input type="button"
					class="btn btn-secondary" name="choix" value="Annuler"></a>
				<c:if
					test="${ enchere.article.dateFinEnchere.isAfter(LocalDate.now()) }">
					<c:url value="ServletSuppressionArticle"
						var="urlSuppressionArticle">
						<c:param name="idArticle" value="${ param.idArticle }" />
					</c:url>
					<a href="${ urlSuppressionArticle }"><input type="button"
						class="btn btn-secondary" name="choix" value="Annuler la vente" /></a>
				</c:if>
			</form>
			<br>
			<br>

		</div>
	</main>





	<script>
		function readURL(input) {
			if (input.files && input.files[0]) {
				var reader = new FileReader();

				reader.onload = function(e) {
					$('#img').attr('src', e.target.result);
				}

				reader.readAsDataURL(input.files[0]);
			}
		}

		$("#photo").change(function() {
			readURL(this);
		});
	</script>

</body>
</html>