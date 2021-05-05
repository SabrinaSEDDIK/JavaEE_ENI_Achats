package fr.eni.eniencheres.ihm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fr.eni.eniencheres.bll.UtilisateurManager;
import fr.eni.eniencheres.bll.bo.Utilisateur;
import fr.eni.eniencheres.messages.BusinessException;
import fr.eni.eniencheres.messages.LecteurMessage;

@WebServlet( "/ServletConnexion" )
public class ServletConnexion extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        request.getRequestDispatcher( "/WEB-INF/connexion.jsp" ).forward( request, response );
    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        // 1 Récupération des saisies utilisateur
        String pseudoMail = request.getParameter( "pseudoMail" );
        String motDePasse = request.getParameter( "motDePasse" );
        // Préparation de l'URL de redirection
        String URLRedirection = "";
        // Création du Manager
        UtilisateurManager utilisateurManager = new UtilisateurManager();

        try {
            // Vérification dans la base de données que le pseudo/mail et mdp
            // concorde
            // La DAO nous renvoie un objet Utilisateur
            Utilisateur utilisateur = utilisateurManager.verifUtilisateur( pseudoMail, motDePasse );
            // S'il est non nul, c'est que le pseudo/mail et le mdp concorde
            if ( utilisateur != null ) {
                // On créé donc la session
                HttpSession session = request.getSession();
                // Et on y place les attributs nécessaires au controle de la
                // connexion
                session.setAttribute( "connected", true );
                session.setAttribute( "infosUtilisateur", utilisateur );
                URLRedirection = "/ServletAccueil";

            }
            // Si l'objet Utilisateur renvoyer par la DAO est nul
            else {
                // On renvoie vers la page de connexion avec un message
                // d'erreur.
                URLRedirection = "/WEB-INF/connexion.jsp";
                List<String> listeMessagesErreur = new ArrayList<>();
                listeMessagesErreur.add( LecteurMessage.getMessageErreur( CodesResultatServlets.ERREUR_WRONG_ID ) );
                request.setAttribute( "listeErreurs", listeMessagesErreur );
                // S'il y a une erreur, on renvoie vers la page de connexion
                URLRedirection = "/WEB-INF/connexion.jsp";
            }
        } catch ( BusinessException ex ) {
            List<String> listeMessagesErreur = new ArrayList<>();
            listeMessagesErreur.add( LecteurMessage.getMessageErreur( CodesResultatServlets.ERREUR_NOT_CONNECTED ) );
            request.setAttribute( "listeErreurs", listeMessagesErreur );
            // S'il y a une erreur, on renvoie vers la page de connexion
            URLRedirection = "/WEB-INF/connexion.jsp";
        }

        Cookie[] cookies = request.getCookies();
        boolean cookieExistant = false;
        String[] checkBox = request.getParameterValues( "seSouvenir" );

        if ( checkBox != null ) {
            for ( String valeur : checkBox ) {
                // Si la checkbox est sélectionné
                if ( valeur.equals( "seSouvenir" ) ) {
                    // On vérifie si le cookie pseudoMail existe déjà
                    for ( Cookie cookie : cookies ) {
                        if ( cookie.getName().equals( "souvenirPseudo" ) && cookie.getValue().equals( pseudoMail ) ) {
                            cookieExistant = true;
                        }
                    }
                    // Si elle n'existe pas, on créé le cookie
                    if ( !cookieExistant ) {
                        Cookie cookie = new Cookie( "souvenirPseudo", pseudoMail );
                        // Age du cookie 1 semaine
                        cookie.setMaxAge( 60 * 60 * 24 * 7 );
                        response.addCookie( cookie );
                    }
                }
            }
        }

        if ( request.getAttribute( "listeErreurs" ) != null ) {
            request.getRequestDispatcher( URLRedirection ).forward( request, response );
        } else {
            response.sendRedirect( request.getContextPath() + URLRedirection );
        }
    }

}
