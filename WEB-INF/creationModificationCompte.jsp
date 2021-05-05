<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		
		<c:choose>
				<c:when test="${ creer == true}">
					<title>Création de votre compte</title>
				</c:when>
				<c:otherwise>
					<title>Modification de votre compte</title>
				</c:otherwise>
		</c:choose>	
			
		<!-- Bootstrap -->
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6" crossorigin="anonymous">
		<link rel="stylesheet" href="inc/style.css" type="text/css">
	</head>
	
	
	<!-- BODY -->
	<body class="mt-5">
	
	<header class="container-fluid pt-4 px-4 pb-4">
		<div class="space-between">
			<div class="me-3">
				<a href="ServletAccueil"><img src="img/logoEniEncheres.png" /></a>
			</div>
		</div>
	</header>
	
		<!-- TITRE -->
		<h1 class="titre_centre mb-3">Mon Profil</h1>
	

		<!-- ERREURS EVENTUELLES -->
		<c:if test="${not empty listeErreurs }">
			<div class="alert alert-danger container" role="alert">
				<c:forEach var="erreur" items="${listeErreurs}">
					${erreur}<br>
				</c:forEach>
			</div>
		</c:if>
		
		<!-- FORMULAIRE -->
		
		<div id="encart-formulaire">
			<form method="post" action="<c:url value="/ServletCreationDeCompte"/>">
			
			<div id="formulaire-aligned">
				<div class="input-aligned">
					<div>
						<label for="pseudo">Pseudo : </label>
						<input type="text" id="pseudo" name="pseudo" value="<c:out value="${ creer==false ? infosUtilisateur.pseudo : requestScope.utilisateurForm.pseudo }"/>">
					</div>
					<br>
					<div>
						<label for="prenom">Prénom : </label>
						<input type="text" id="prenom" name="prenom" value="<c:out value="${  creer==false ? infosUtilisateur.prenom : requestScope.utilisateurForm.prenom }"/>">
					</div>
					<br>
					<div>
						<label for="telephone">Téléphone : </label>
						<input type="text" id="telephone" name="telephone" value="<c:out value="${ creer==false ? infosUtilisateur.telephone : requestScope.utilisateurForm.telephone }"/>">
					</div>
					<br>
					<div>
						<label for="codePostal">Code Postal : </label>
						<input type="text" id="codePostal" name="codePostal" value="<c:out value="${ creer==false ? infosUtilisateur.codePostal : requestScope.utilisateurForm.codePostal }"/>">
					</div>
					<br>
					<c:choose>
						<c:when test="${ creer == true}">
							<div>
								<label for="motDePasse">Mot de passe : </label>
								<input type="password" id="motDePasse" name="motDePasse" required>
							</div>
						</c:when>
						<c:otherwise>
							<div>
								<label for="motDePasse">Mot de passe actuel : </label>
								<input type="password" id="motDePasse" name="motDePasse" required>
							</div>
							<br>
							<div>
								<label for="nouveauMotDePasse">Nouveau mot de passe : </label>
								<input type="password" id="nouveauMotDePasse" name="nouveauMotDePasse">
							</div>
							<br>
								<span>Crédit : ${infosUtilisateur.credit}</span>
						</c:otherwise>
					</c:choose>	
					
					
					
					
				</div>
				
				<div class="input-aligned">
					<div>
						<label for="nom">Nom : </label>
						<input type="text" id="nom" name="nom" value="<c:out value="${ creer==false ? infosUtilisateur.nom : requestScope.utilisateurForm.nom }"/>">
					</div>
					<br>
					
					<div>
						<label for="email">Email : </label>
						<input type="text" id="email" name="email" value="<c:out value="${ creer==false ? infosUtilisateur.email : requestScope.utilisateurForm.email }"/>">
					</div>
					<br>
					
					<div>
						<label for="rue">Rue : </label>
						<input type="text" id="rue" name="rue" value="<c:out value="${ creer==false ? infosUtilisateur.rue : requestScope.utilisateurForm.rue }"/>">
					</div>
					<br>
					
					<div>
						<label for="ville">Ville : </label>
						<input type="text" id="ville" name="ville" value="<c:out value="${ creer==false ? infosUtilisateur.ville : requestScope.utilisateurForm.ville }"/>">
					</div>
					<br>
					
					<c:choose>
						<c:when test="${ creer == true}">
							<div>
								<label for="confirmationMdP">Confirmation : </label>
								<input type="password" id="confirmationMdP" name="confirmationMdP" required>
							</div>
						</c:when>
						<c:otherwise>
							<br><br>
							<div class="pt-2">
								<label for="confirmationMdP">Confirmation du nouveau mot de passe : </label>
								<input type="password" id="confirmationMdP" name="confirmationMdP">
							</div>
						</c:otherwise>
					</c:choose>	
					
				</div>		
			</div>
			<div class="centrage-bouton">
				<c:choose>
					<c:when test="${  creer==false }">
						<input type="submit" name="choix" value="Enregistrer" class="button_formulaire">
						<a href="<c:url value="/ServletSuppressionCompte"/>"><input type="button" value="Supprimer mon compte" class="button_formulaire" style="width:200px;"></a>
					</c:when>
					<c:otherwise>
						<input type="submit" name = "choix" value="Créer" class="button_formulaire">
						<a href="<c:url value="/ServletAccueil"/>"><input type="button" value="Annuler" class="button_formulaire"></a> 
					</c:otherwise>
				</c:choose>
			</div>
			
			</form>
		</div>
</body>
</html>