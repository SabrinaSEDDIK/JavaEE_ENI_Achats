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

import fr.eni.eniencheres.bll.ArticleManager;
import fr.eni.eniencheres.bll.bo.Utilisateur;
import fr.eni.eniencheres.messages.BusinessException;

@WebServlet( "/ServletSuppressionArticle" )
public class ServletSuppressionArticle extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        HttpSession sessionUtilisateur = request.getSession();
        List<String> listeErreurs = new ArrayList<>();

        // La page n'est accessible que si l'on est connecté
        if ( sessionUtilisateur.getAttribute( "connected" ) != null
                && (boolean) sessionUtilisateur.getAttribute( "connected" ) == true ) {
            ArticleManager am = new ArticleManager();

            try {
                // On récupère les infos utile pour la suppression
                Utilisateur utilisateur = (Utilisateur) sessionUtilisateur.getAttribute( "infosUtilisateur" );
                int idArticle = Integer.parseInt( request.getParameter( "idArticle" ) );

                am.annulerVente( idArticle, utilisateur.getIdUtilisateur() );
            } catch ( NumberFormatException nfe ) {
                nfe.printStackTrace();
                listeErreurs.add( "L'identifiant de l'article à supprimer n'est pas correcte." );
            } catch ( BusinessException be ) {
                be.printStackTrace();
                if ( !listeErreurs.isEmpty() ) {
                    listeErreurs.addAll( be.getListeMessageErreur() );
                } else {
                    listeErreurs = be.getListeMessageErreur();
                }
            }
        }

        if ( !listeErreurs.isEmpty() ) {
            request.setAttribute( "succesSuppressionArticle", false );

        } else {
            request.setAttribute( "succesSuppressionArticle", true );
        }

        request.setAttribute( "listeErreurs", listeErreurs );
        request.getRequestDispatcher( "/" ).forward( request, response );
    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        doGet( request, response );
    }

}
