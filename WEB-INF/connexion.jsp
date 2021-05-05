<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Page connexion</title>
<link rel="stylesheet" href="inc/style.css" type="text/css">
<link rel="stylesheet" href="inc/accueil.css" type="text/css">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6"
	crossorigin="anonymous">
</head>
<body>
	<header>

		<!-- logo Eni-Enchère -->
		<div class="me-3">
			<a href="ServletAccueil"><img src="img/logoEniEncheres.png" /></a>
		</div>

	</header>



	<div class="container-fluid">
		<!-- Messages d'erreurs éventuels -->
		<div class="container cadrage-erreur">
			<c:if test="${not empty listeErreurs }">
				<div class="alert alert-danger" role="alert">
					<c:forEach var="erreur" items="${listeErreurs}">
									${erreur}
								</c:forEach>
				</div>
			</c:if>
		</div>
		<div id="formulaireConnexion">
			<div class="row">
				<div class="col-md-12">

					<!-- TITRE -->
					<div>
						<h1 class="titre_centre mb-3 bleu">Connexion</h1>
					</div>
					<!-- MAIN -->
					<main class="container">

						<div class="container">
							<div class="row">
								
									<form method="post" action="ServletConnexion">
										<div class="form-group">

											<label for="pseudoMail"> Identifiant </label> <input
												id="pseudoMail" type="text" name="pseudoMail"
												class="form-control" value="${ cookie.souvenirPseudo.value }" placeholder="Pseudo ou Mail" required />
										</div>
										<div class="form-group">

											<label for="motDePasse"> Mot de passe </label> <input
												id="motDePasse" type="password" name="motDePasse"
												class="form-control" required /><br>
												<input type="checkbox" id="seSouvenir" name="seSouvenir" value="seSouvenir" /><label for="seSouvenir">Se souvenir de moi</label>
										</div>
										<br>
										<div class="container-fluid centrage-bouton">
											<div >
												<button type="submit" class="btn btn-primary">Connexion
												</button>
											</div >
											 <div>  <a href="<c:url value="/ServletCreationDeCompte"/>">
												<button type="button" class="btn active btn-secondary">
													Créer un compte</button>
													</a>
											</div>
										</div>
									</form>
								
							</div>
						</div>
					</main>
				</div>
			</div>
		</div>
	</div>

</body>
</html>