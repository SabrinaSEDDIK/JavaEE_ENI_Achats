<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Détail de la vente en cours</title>
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.2/css/all.min.css" integrity="sha512-HK5fgLBL+xu6dm/Ii3z4xhlSUyZgTT9tuc/hSrtw6uzJOvgRr2a9jyxxT1ely+B+xFAmJKVSTbpM/CuL7qxO8w==" crossorigin="anonymous" />
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6" crossorigin="anonymous">
		<link rel="stylesheet" href="inc/style.css" type="text/css">
		<link rel="stylesheet" href="inc/accueil.css">
	</head>
	<body>
	<header class="container-fluid pt-4 px-4">
			<div class="space-between">
				<div class="me-3">
					<a href="ServletAccueil"><img src="img/logoEniEncheres.png"/></a>
				</div>
			</div>
			<br>
	</header>
	
	<main class="container">
		<h2 class="bleu">Détail vente</h2>
		<!-- COMPTE MODIFIE avec succès -->
		<c:if test="${not empty messageResultat}">
			<div class="alert alert-success" role="alert">
				<c:out value="${ messageResultat }" />
			</div>
		</c:if>
		<!--  <p><c:out value="${ messageResultat }"/></p>-->
		<!-- Messages d'erreurs éventuels -->
		<c:if test="${not empty listeErreurs }">
			<div class="alert alert-danger" role="alert">
				<c:forEach var="erreur" items="${ listeErreurs }">
					<c:out value="${ erreur }" />
				</c:forEach>
			</div>
		</c:if>
		<div id="encart-detail-vente">
			<div id="encart-photo-detail-vente">
				<img src="img/objet_vide.png" alt="Image Article">
			</div>
		
			<div id="encart-info-detail-vente">
				<ul>
					<li><c:out value="${ enchere.article.nom }"/> </li>
					<li>Description : <c:out value="${ enchere.article.description }"/> </li>
					<li>Catégorie : <c:out value="${ enchere.categorie }"/> </li>
					<c:choose>
						<c:when test="${ enchere.article.prixVente == 0 }">
							<li>Meilleure offre : <em>Aucune enchère encore réalisée</em></li>
						</c:when>
						<c:otherwise>
							<li>Meilleure offre : <c:out value="${ enchere.article.prixVente }"/></li>
						</c:otherwise>
					</c:choose>
					<li>Mise à prix : <c:out value="${ enchere.article.miseAPrix }"/> </li>
					<fmt:parseDate  value="${ enchere.article.dateDebutEnchere}"  type="date" pattern="yyyy-MM-dd" var="dateConvertiDebut" />
					<li>Début de l'enchère : <fmt:formatDate value="${ dateConvertiDebut }" type="date" pattern="dd/MM/yyyy"/></li>
					<fmt:parseDate  value="${ enchere.article.dateFinEnchere}"  type="date" pattern="yyyy-MM-dd" var="dateConverti" />
					<li>Fin de l'enchère : <fmt:formatDate value="${ dateConverti }" type="date" pattern="dd/MM/yyyy"/></li>
					<li>Retrait : <c:out value="${ enchere.rue += ' - ' += enchere.codePostal += ' - ' += enchere.ville }"/> </li>
					<li>Vendeur : <c:out value="${ enchere.article.pseudoVendeur }"/> </li>
				</ul>
				<c:url value="ServletEncherir" var="urlEncherir">
					<c:param name = "idArticle" value = "${ enchere.article.identifiant }"/>
					<c:param name = "idEncherisseur" value = "${ infosUtilisateur.idUtilisateur }"/>
				</c:url>
				<c:if test="${ infosUtilisateur.idUtilisateur != enchere.idVendeur }">
					<form method="post" action="${ urlEncherir }">
						<label for="propositionEnchere">Ma proposition : </label><input type="number" value="${ enchere.article.prixVente == 0 ? enchere.article.miseAPrix + 1 : enchere.article.prixVente + 1 }" min="${ enchere.article.prixVente == 0 ? enchere.article.miseAPrix + 1 : enchere.article.prixVente + 1 }" name="propositionEnchere" id="propositionEnchere">
						<button type="submit" name="choix" value="encherir" class="button_formulaire">Enchérir</button>
					</form>
				</c:if>
				
			</div>
		</div>
	</main>
	</body>
</html>