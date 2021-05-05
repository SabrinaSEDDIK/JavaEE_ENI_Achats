<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.time.LocalDate" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
		<title>ENI Enchères - Accueil</title>
		<script type="text/javascript">
			function AchatsEnable(){
				document.getElementById("encheresOuvertes").removeAttribute("disabled");
				document.getElementById("encheresEnCours").removeAttribute("disabled");
				document.getElementById("encheresRemportees").removeAttribute("disabled");
				document.getElementById("ventesEnCours").setAttribute("disabled", "");
				document.getElementById("ventesNonDebutees").setAttribute("disabled", "");
				document.getElementById("ventesTerminees").setAttribute("disabled", "");
				
			}
			function VentesEnable(){
				document.getElementById("ventesEnCours").removeAttribute("disabled");
				document.getElementById("ventesNonDebutees").removeAttribute("disabled");
				document.getElementById("ventesTerminees").removeAttribute("disabled");
				document.getElementById("encheresOuvertes").setAttribute("disabled", "");
				document.getElementById("encheresEnCours").setAttribute("disabled", "");
				document.getElementById("encheresRemportees").setAttribute("disabled", "");
			}
		</script>
		<!-- FONTAWESOME / BOOTSTRAP / CSS -->
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.2/css/all.min.css" integrity="sha512-HK5fgLBL+xu6dm/Ii3z4xhlSUyZgTT9tuc/hSrtw6uzJOvgRr2a9jyxxT1ely+B+xFAmJKVSTbpM/CuL7qxO8w==" crossorigin="anonymous" />
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6" crossorigin="anonymous">
		<link rel="stylesheet" href="inc/accueil.css">
		<link rel="icon" href="data:;base64,iVBORw0KGgo=">
	</head>
	
	<body>
		<header class="d-flex align-items-center container-fluid">
			<c:choose>
				<c:when test="${ sessionScope.connected == true }">
				
					<div class="w-100 justify-content-between">
						<div class="me-3">
							<a href="ServletAccueil"><img src="img/logoEniEncheres.png"/></a>
						</div>                                                                     
						<div style="text-align:right;">
							<div>${infosUtilisateur.getPseudo() } est connecté(e)</div>
							<a href="<c:url value="ServletAccueil"/>">Enchères</a> &nbsp;
							<a href="ServletNouvelleVente">Vendre un article</a> &nbsp;
							<a href="ServletAfficherProfil?id=${infosUtilisateur.getIdUtilisateur()}">Mon profil</a> &nbsp;
							<a href="<c:url value="ServletDeconnexion"/>">Déconnexion</a>
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="w-100 d-flex justify-content-between align-items-center px-2">
						<a href="ServletAccueil"><img src="img/logoEniEncheres.png"/></a>
						<a href="ServletConnexion" class="text-white">S'inscrire - Se connecter</a>
					</div>
				</c:otherwise>
			</c:choose>	
			<br>
		</header>
		
		<section class="banner mb-5">
	       <div class="banner-container">
	                <h1><span>ENI ENCHÈRES</span></h1>
	                <span class="sub-title">Vos enchères en 1 clic</span>
	        </div>
        </section>
		
		<!-- MAIN -->
		<main class="container">
			<!-- NOUVEL ARTICLE inséré avec succès -->
			<c:if test="${succesInsertion==true}">
				<div class="alert alert-success" role="alert">
					Votre article a été ajouté avec succès, veuillez le retrouver dans "Mes ventes"
				</div>
			</c:if>
			
			<!-- COMPTE MODIFIE avec succès -->
			<c:if test="${succesModifCompte==true}">
				<div class="alert alert-success" role="alert">
					Votre compte a été modifié avec succès !
				</div>
			</c:if>
			
			<!-- COMPTE SUPPRIME avec succès -->
			<c:if test="${suppressionCompte==true}">
				<div class="alert alert-success" role="alert">
					Votre compte a été supprimé avec succès
				</div>
			</c:if>
			<!-- COMPTE CREE avec succès -->
			<c:if test="${succesCreationCompte==true}">
				<div class="alert alert-success" role="alert">
					Bienvenue ${infosUtilisateur.pseudo }, votre compte a été créé avec succès !
				</div>
			</c:if>
			<!-- Modification compte avec succès -->
			<c:if test="${succesModificationArticle==true}">
				<div class="alert alert-success" role="alert">
					Votre vente a bien été modifié avec succès !
				</div>
			</c:if>
			
			<!-- Suppression Vente avec succès -->
			<c:if test="${succesSuppressionArticle==true}">
				<div class="alert alert-success" role="alert">
					Votre vente a été supprimé avec succès.
				</div>
			</c:if>
			
			<!-- Suppression Vente avec succès -->
			<c:if test="${succesCreditementCompte==true}">
				<div class="alert alert-success" role="alert">
					Votre compte a été crédité avec succès.
				</div>
			</c:if>
			
			<!-- Messages d'erreurs éventuels -->
			<c:if test="${not empty listeErreurs }">
				<div class="alert alert-danger" role="alert">
					<c:forEach var="erreur" items="${listeErreurs}">
						${erreur}
					</c:forEach>
				</div>
			</c:if>
			
		
			
		
			<!--  SECTION FORMULAIRE -->
			<section class="mt-5">
				<form class="row" method="get" action="ServletAccueil">
					<div class="row w-100 mx-auto">
					<!-- barre de recherche  -->
						<div class="col-12">
							<label class="form-label" for="motContenu">Filtres :</label><br>
							<div class="input-group align-self-center">
				                <div class="input-group-prepend">
				                    <div class="input-group-text bg-blue py-2" id="btnGroupAddon"><i class="fas fa-search"></i></div>
				                </div>
				                <input type="text" class="form-control py-1" id="motContenu" name="recherche" placeholder="Le nom de l'article contient" value="${motRecherche }"/>
			           		</div>
		           		 </div>
		           	<!-- sélection catégorie(s) -->
		           		<div class="col-12 mt-3">
							<label class="form-label" for="categories">Catégorie : </label>
							<select class="form-select" id="categories" name="categories">
								<option value="0"<c:if test="${categorie == 0 }">selected</c:if>>Toutes</option>
								<option value="1"<c:if test="${categorie == 1 }">selected</c:if>>Informatique</option>
								<option value="2"<c:if test="${categorie == 2 }">selected</c:if>>Ameublement</option>
								<option value="3"<c:if test="${categorie == 3 }">selected</c:if>>Vêtement</option>
								<option value="4"<c:if test="${categorie == 4 }">selected</c:if>>Sport &amp; Loisirs</option>
							</select>
						</div>
						<!-- bouton rechercher -->
						<div class="pt-4">
							<input class="login btn p-2 w-100" type="submit" value="Rechercher">
						</div>

						<br>
					<!-- recherche avancée (en mode connecté) -->
						<c:if test="${connected==true}">
						<div class="row">
							<div class="col-6">
								<input type="radio" id="achats" name="mode" value="achats" <c:if test="${mode == 1 }">checked</c:if> onclick=AchatsEnable();><label class="form-label" for="achats">&nbsp; Achats</label>
								<br>
								<input type="checkbox" id="encheresOuvertes" name="encheresOuvertes" <c:if test="${encheresOuvertes == true }">checked</c:if> disabled><label class="form-label" for="encheresOuvertes">&nbsp; enchères ouvertes</label>
								<br>
								<input type="checkbox" id="encheresEnCours" name="encheresEnCours" <c:if test="${encheresEnCours == true }">checked</c:if> disabled><label class="form-label" for="encheresEnCours">&nbsp; mes enchères en cours</label>
								<br>
								<input type="checkbox" id="encheresRemportees" name="encheresRemportees" <c:if test="${encheresRemportees == true }">checked</c:if>  disabled><label class="form-label" for="encheresRemportees">&nbsp; mes enchères remportées</label>
							</div>
			
							<div class="col-6 py-2">
								<input type="radio" id="ventes" name="mode" value="ventes" <c:if test="${mode == 2 }">checked</c:if> onclick=VentesEnable();><label class="form-label" for="ventes">&nbsp; Mes ventes</label>
								 <br>
							 	<input type="checkbox" id="ventesEnCours" name="ventesEnCours" <c:if test="${ventesEnCours == true }">checked</c:if> disabled><label class="form-label" for="ventesEnCours">&nbsp; mes ventes en cours</label>
								 <br>
								 <input type="checkbox" id="ventesNonDebutees" name="ventesNonDebutees" <c:if test="${ventesNonDebutees == true }">checked</c:if>  disabled><label class="form-label" for="ventesNonDebutees" >&nbsp; ventes non débutées</label>
								 <br>
								 <input type="checkbox" id="ventesTerminees" name="ventesTerminees" <c:if test="${ventesTerminees == true }">checked</c:if> disabled><label class="form-label" for="ventesTerminees" >&nbsp; ventes terminées</label>
							</div>
						</div>
						</c:if>
						

					</div>
					

				</form>
			</section>	
	
			<!--  SECTION AFFICHAGE DES ENCHERES -->
			<section class="mt-5">
				<div class="container row m-0 p-0">
				<c:forEach var="article" items="${listeArticles}">
					<div class="card col-lg-6 mb-3 shadow">
					  <div class="row g-0">
					    <div class="col-md-4">
					      <img src="img/objet_trucvide.png" class="py-5" alt="Image Article">
						</div>
					    <div class="col-md-8">
					      <div class="card-body">
					      	<c:url value="/ServletAffichageEnchere" var="urlAffichageEnchere">
					      		<c:param name = "idArticle" value = "${ article.identifiant }"/>
					      	</c:url>
					        <h5 class="card-title"><a href="${ urlAffichageEnchere }">${article.nom}</a></h5>
					        <p class="card-text">Prix : ${article.prixVente == 0 ? article.miseAPrix : article.prixVente } points</p>
					        <p class="card-text">Fin de l'enchère :
					        	<fmt:parseDate  value="${article.dateFinEnchere}"  type="date" pattern="yyyy-MM-dd" var="parsedDate" />
					        	<fmt:formatDate value="${parsedDate}" type="date" pattern="dd-MM-yyyy"/>
					        </p>
					        <p class="card-text"><small class="text-muted">Vendeur : <a href="<c:url value="/ServletAfficherProfil?id=${article.idVendeur}"/>">${article.pseudoVendeur}</a></small></p>
					      </div>
					    </div>
					  </div>
					</div>
				</c:forEach>
				<c:if test="${empty listeArticles}">
					<p class="pb-5">Désolé, aucun article ne correspond à votre recherche :( </p>
				</c:if>
			</div>
			</section>
			
		</main>
		
		
	</body>
</html>