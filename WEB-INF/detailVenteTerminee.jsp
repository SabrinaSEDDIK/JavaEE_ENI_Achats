<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Détail de la vente terminée</title>
		<!-- FONTAWESOME / BOOTSTRAP / CSS -->
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.2/css/all.min.css" integrity="sha512-HK5fgLBL+xu6dm/Ii3z4xhlSUyZgTT9tuc/hSrtw6uzJOvgRr2a9jyxxT1ely+B+xFAmJKVSTbpM/CuL7qxO8w==" crossorigin="anonymous" />
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6" crossorigin="anonymous">
		<link rel="stylesheet" href="inc/accueil.css">
		<link rel="stylesheet" href="inc/style.css" type="text/css">
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
		<c:choose>
			<c:when test="${ infosUtilisateur.idUtilisateur == acheteur.idUtilisateur }">
				<h2 class="bleu">Vous avez remporté l'enchère</h2>
			</c:when>
			<c:otherwise>
				<h2 class="bleu"><c:out value="${ acheteur.pseudo }" /> a remporté l'enchère</h2>
			</c:otherwise>
		</c:choose>
		
		<p><c:out value="${ messageResultat }"/></p>
		<div id="encart-detail-vente">
			<div id="encart-photo-detail-vente">
				<img src="<c:url value="img/objet_vide.png"/>" alt="article">
			</div>
		
			<div id="encart-info-detail-vente">
				<ul>
					<li><c:out value="${ enchere.article.nom }"/> </li>
					<li>Description : <c:out value="${ enchere.article.description }"/> </li>
					<li>Meilleure offre : <c:out value="${ enchere.article.prixVente }"/></li>
					<li>Mise à prix : <c:out value="${ enchere.article.miseAPrix }"/> </li>
					<li>Retrait : <c:out value="${ enchere.rue += ' - ' += enchere.codePostal += ' - ' += enchere.ville }"/> </li>
					<li>Vendeur : <c:out value="${ enchere.article.pseudoVendeur }"/> </li>
					<li>Tél : <c:out value="${ vendeur.telephone }"/></li>
				</ul>
				
				<c:choose>
					<c:when test="${ infosUtilisateur.idUtilisateur == enchere.idVendeur }">
						<c:url value="ServletFinEnchere" var ="urlRetraitOk">
							<c:param name="idArticle" value="${ enchere.article.identifiant }"/>
						</c:url>
						<form method="post" action="${ urlRetraitOk }">
							<button type="submit" name="retrait" value="true">Retrait effectué</button>
						</form>
					</c:when>
					<c:otherwise>
						<a href="ServletAccueil">
							<button type="button">Back</button>
						</a>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		

	
	</body>
</html>