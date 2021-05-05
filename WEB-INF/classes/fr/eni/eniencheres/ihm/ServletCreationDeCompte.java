package fr.eni.eniencheres.ihm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fr.eni.eniencheres.bll.UtilisateurManager;
import fr.eni.eniencheres.bll.bo.Utilisateur;
import fr.eni.eniencheres.messages.BusinessException;

@WebServlet( "/ServletCreationDeCompte" )
public class ServletCreationDeCompte extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        HttpSession sessionUtilisateur = request.getSession();
        if ( sessionUtilisateur.getAttribute( "connected" ) != null
                && (boolean) sessionUtilisateur.getAttribute( "connected" ) == true ) {
            request.setAttribute( "creer", false );
            request.getRequestDispatcher(
                    request.getAttribute( "URLRedirection" ) != null ? (String) request.getAttribute( "URLRedirection" )
                            : "/WEB-INF/creationModificationCompte.jsp" )
                    .forward( request, response );
        } else {
        	if(request.getHeader("referer").contains("Profil")) {
        		response.sendRedirect(request.getContextPath()+"/ServletAccueil");
        	}else {
        		request.setAttribute( "creer", true );
        		request.getRequestDispatcher(
                        request.getAttribute( "URLRedirection" ) != null ? (String) request.getAttribute( "URLRedirection" )
                                : "/WEB-INF/creationModificationCompte.jsp" )
                        .forward( request, response );
        	}
            
        }
      
    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        // Création du Manager
        UtilisateurManager um = new UtilisateurManager();
        // URL de redirection
        Utilisateur utilisateur = null;
        String URLRedirection = "/WEB-INF/creationModificationCompte.jsp";
        List<String> listeErreurs = new ArrayList<>();
        HttpSession sessionUtilisateur = null;
        Integer idUtilisateur = null;
        Integer creditUtilisateur = null;

        // Récupération des différentes informations du formulaire
        String pseudo = request.getParameter( "pseudo" );
        String nom = request.getParameter( "nom" );
        String prenom = request.getParameter( "prenom" );
        String email = request.getParameter( "email" );
        String telephone = request.getParameter( "telephone" );
        String rue = request.getParameter( "rue" );
        String codePostal = request.getParameter( "codePostal" );
        String ville = request.getParameter( "ville" );
        String motDePasseDonne = request.getParameter( "motDePasse" );
        String confirmationMdP = request.getParameter( "confirmationMdP" );
        String nouveauMdP = request.getParameter( "nouveauMotDePasse" );
        String choixUtilisateur = request.getParameter( "choix" );
        String ancienMdP = "";

        sessionUtilisateur = request.getSession();
        // EN MODE MODIF : R�cup�ration de l'id, du cr�dit et de l'ancien mot de
        // passe de l'utilisateur
        if ( sessionUtilisateur.getAttribute( "connected" ) != null
                && (boolean) sessionUtilisateur.getAttribute( "connected" ) == true ) {

            Utilisateur profil = (Utilisateur) ( sessionUtilisateur.getAttribute( "infosUtilisateur" ) );
            idUtilisateur = profil.getIdUtilisateur();
            creditUtilisateur = profil.getCredit();
            ancienMdP = profil.getMotDePasse();
        }

        // Tentative de création de l'utilisateur avec les infos récupérées
        try {
            utilisateur = um.creerUtilisateur( idUtilisateur, pseudo, nom, prenom, email, telephone, rue, codePostal,
                    ville, ancienMdP, motDePasseDonne,
                    confirmationMdP, nouveauMdP, choixUtilisateur );
        } catch ( BusinessException be ) {
            // On met dans la liste d'erreurs les erreurs qui sont remontées
            listeErreurs = be.getListeMessageErreur();
        } catch ( Exception e ) {
            listeErreurs.add( e.getMessage() );
        }

        if ( utilisateur != null && listeErreurs.isEmpty() ) {
            if ( utilisateur.getCredit() == null ) {// = ON EST EN MODE MODIF
                utilisateur.setCredit( creditUtilisateur );
                request.setAttribute( "succesModifCompte", true );
            } else { // = ON EST EN MODE CREER
                request.setAttribute( "succesCreationCompte", true );
            }
            sessionUtilisateur.setAttribute( "connected", true );
            sessionUtilisateur.setAttribute( "infosUtilisateur", utilisateur );
            URLRedirection = "/ServletAccueil";
        } else {
            if ( nouveauMdP != null ) {// = ON EST EN MODE MODIF
                request.setAttribute( "succesModifCompte", false );
            } else { // = ON EST EN MODE CREER
                request.setAttribute( "succesCreationCompte", false );
            }
        }

        // On place dans la requête l'utilisateur créé
        // ou en cours de création si des erreurs sont remontés
        request.setAttribute( "utilisateurForm", um.getUtilisateur() );
        request.setAttribute( "listeErreurs", listeErreurs );
        request.setAttribute( "URLRedirection", URLRedirection );
        doGet( request, response );

    }// fin POST

}// fin SERVLET
