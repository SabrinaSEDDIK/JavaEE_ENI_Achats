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
import fr.eni.eniencheres.messages.LecteurMessage;

/**
 * Servlet implementation class ServletAfficherProfil
 */
@WebServlet( "/ServletAfficherProfil" )
public class ServletAfficherProfil extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        // Création de l'utilisateur Manager
        UtilisateurManager utilisateurManager = new UtilisateurManager();
        // Récupération de la session
        HttpSession session = request.getSession();
        // Création de l'objet utilisateurRecupere
        Utilisateur utilisateurRecupere = null;
        // Création de l'url de redirection
        String urlRedirection = "";

        if ( session.getAttribute( "connected" ) != null
                && (boolean) session.getAttribute( "connected" ) == true ) {
            // Récupération du pseudo envoyé depuis id
            int idProfil = Integer.parseInt( request.getParameter( "id" ) );

            // On récupère dans un objet article l'article voulu
            try {
                utilisateurRecupere = utilisateurManager.RechercheUtilisateur( idProfil );
            } catch ( BusinessException e ) {
                e.printStackTrace();
            }

            if ( utilisateurRecupere == null ) {
                List<String> listeMessagesErreur = new ArrayList<>();
                listeMessagesErreur
                        .add( LecteurMessage.getMessageErreur( CodesResultatServlets.ERREUR_UTILISATEUR_SUPPRIME ) );
                request.setAttribute( "listeErreurs", listeMessagesErreur );
                // S'il y a une erreur, on renvoie vers la page d'accueil
                urlRedirection = "/ServletAccueil";
            } else {

                // Les infos permettant de connaître l'utilisateur connecté pour
                // faire apparaître le bouton modifier
                // session.setAttribute("infoUtilisateur", new Utilisateur());
                // session.setAttribute("connected", true);
                session.setAttribute( "utilisateur", utilisateurRecupere );

                // Récupération infos utilisateur
                // Utilisateur utilisateur = (Utilisateur)
                // session.getAttribute("infosUtilisateur" );
                urlRedirection = "/WEB-INF/afficherProfil.jsp";
            }
        }
        // Si on n'est pas connecté, on n'est pas censé se trouver ici
        else {
            List<String> listeMessagesErreur = new ArrayList<>();
            listeMessagesErreur.add( LecteurMessage.getMessageErreur( CodesResultatServlets.ERREUR_NOT_CONNECTED ) );
            request.setAttribute( "listeErreurs", listeMessagesErreur );
            // S'il y a une erreur, on renvoie vers la page d'accueil
            urlRedirection = "/ServletAccueil";
        }

        request.getRequestDispatcher( urlRedirection ).forward( request, response );

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        doGet( request, response );
    }

}
