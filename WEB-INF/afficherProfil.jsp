<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Affichage de profil</title>
<!-- BOOTSTRAP/CSS -->
<link rel="stylesheet" href="inc/style.css" type="text/css">
<link rel="stylesheet" href="inc/accueil.css" type="text/css">
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
</head>
<body>

	<header>
		<!-- logo Eni-Enchère -->
		<div class="me-3">
			<a href="ServletAccueil"><img src="img/logoEniEncheres.png" /></a>
		</div>



	</header>
	<!-- Si besoin pour afficher les id utilisateur et vendeur
		<label for="idUtilisateur">idUtilisateur :${infosUtilisateur.getIdUtilisateur()}</label> <br>
		<label for="idProfil">idProfil :${utilisateur.idUtilisateur}</label> <br>
		-->
	<div id="formulaireConnexion">
		<!-- Profil de l'utilisateur sous forme de titre -->
		<h1 class="titre_centre bleu">Profil de ${utilisateur.pseudo}</h1>
		<div id="formulaire-aligned">
			<div>
				<div class="container-fluid">
					<div class="row">
						<div class="col-md-12">
							<div id="centrage-img-profil">
								<img alt="Bootstrap Image Preview" src="img/head-profile.png"
									class="rounded-circle" />
							</div>
						</div>
					</div>
				</div>
				<div id="centrage-text">
					<!-- Affichage des infos utilisateurs -->
					<label for="pseudo">
						<a class="gras">Pseudo : </a>
							${utilisateur.pseudo}
					</label> 
						<br> 
					<label for="prenom"> 
					<a class="gras">Prénom :</a> 
						${utilisateur.prenom}
					</label> 
						<br>
						<label for="nom"> 
					<a class="gras">Nom :</a> 
						${utilisateur.nom}
					</label> 
						<br>
					 <label for="email">
					<a class="gras">Email :</a>
						${utilisateur.email} 
						</label> <br>
						<label for="telephone">
						<a class="gras">Téléphone :</a> 
						${utilisateur.telephone} </label> 
						<br> 
						<label for="rue"><a class="gras">Rue :</a> 
						${utilisateur.rue}</label> 
						<br>
					<label for="codePostal">
					<a class="gras">Code Postal :</a>
						${utilisateur.codePostal}
						</label> 
						<br> 
						<label for="ville">
						<a class="gras">Ville :</a> 
						${utilisateur.ville}</label> 
						<br>

					<!-- Si je suis connecté et que le vendeur possède le même id que l'utilisateur -->
					<!-- Alors, j'affiche les crédits -->
					<c:if
						test="${infosUtilisateur.getIdUtilisateur()==utilisateur.idUtilisateur}">
						<label for="credit"><a class="gras">Credit :</a>
							${utilisateur.credit}</label>
						<br>
					</c:if>
				</div>
				<br>
				<div class="centrage-bouton">
					<!-- Si je suis connecté et que le vendeur possède le même id que l'utilisateur -->
					<!-- Alors, je peux modifier les infos et j'affiche le bouton Modifier -->
					<c:if
						test="${infosUtilisateur.getIdUtilisateur()==utilisateur.idUtilisateur}">
						<a href="<c:url value="/ServletCreationDeCompte"/>">
							<button type="button" class="btn active btn-secondary">
								Modifier</button>
						</a>
					</c:if>
				</div>
			</div>

		</div>
	</div>

</body>
</html>