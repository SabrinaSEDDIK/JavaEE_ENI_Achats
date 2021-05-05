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

import fr.eni.eniencheres.bll.EnchereManager;
import fr.eni.eniencheres.bll.UtilisateurManager;
import fr.eni.eniencheres.bll.bo.Enchere;
import fr.eni.eniencheres.bll.bo.Utilisateur;
import fr.eni.eniencheres.messages.BusinessException;
import fr.eni.eniencheres.messages.LecteurMessage;

@WebServlet( "/ServletFinEnchere" )
public class ServletFinEnchere extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        response.getWriter().append( "Served at: " ).append( request.getContextPath() );
    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        EnchereManager em = new EnchereManager();
        UtilisateurManager um = new UtilisateurManager();

        List<String> listeErreurs = new ArrayList<>();

        if ( session.getAttribute( "connected" ) != null
                && (boolean) session.getAttribute( "connected" ) == true ) {
            try {
                // On récupère l'id de l'article dont on parle
                int idArticle = Integer.parseInt( request.getParameter( "idArticle" ) );
                // On récupère depuis la base de données la meilleure enchère
                // sur cet article
                Enchere enchereTerminee = em.recupererDetailEnchereEnCours( idArticle );
                // On récupère les infos de l'utilisateur en session
                Utilisateur utilisateur = (Utilisateur) session.getAttribute( "infosUtilisateur" );
                // Si l'utilisateur qui accède à cette page est bien le vendeur
                // de l'article
                if ( utilisateur.getIdUtilisateur() == enchereTerminee.getIdVendeur() ) {
                    // On peut mettre à jour le crédit de l'utilisateur
                    um.creditementVendeurEnchereTerminee( utilisateur.getIdUtilisateur(),
                            enchereTerminee.getArticle().getIdentifiant(),
                            enchereTerminee.getArticle().getPrixVente() );
                }
                // Si on n'est pas le propriétaire de l'article, on lance un
                // erreur
                else {
                    BusinessException businessException = new BusinessException();
                    businessException.ajouterErreur( CodesResultatServlets.ERREUR_CLOTURE_ENCHERE_NON_PROPRIETAIRE );
                    throw businessException;
                }
            } catch ( NumberFormatException e ) {
                listeErreurs.add( LecteurMessage.getMessageErreur( CodesResultatServlets.ERREUR_ID_NOT_INT ) );
                request.setAttribute( "listeErreurs", listeErreurs );
            } catch ( BusinessException e ) {
                listeErreurs = e.getListeMessageErreur();
                request.setAttribute( "listeErreurs", e.getListeMessageErreur() );
            }
        }
        // Si on n'est pas connecté, on n'est pas censé se trouver ici
        else {
            listeErreurs.add( LecteurMessage.getMessageErreur( CodesResultatServlets.ERREUR_NOT_CONNECTED ) );
            request.setAttribute( "listeErreurs", listeErreurs );
        }

        if ( !listeErreurs.isEmpty() ) {
            request.setAttribute( "succesCreditementCompte", false );
        } else {
            request.setAttribute( "succesCreditementCompte", true );
        }

        request.getRequestDispatcher( "/ServletAccueil" ).forward( request, response );

    }

}
