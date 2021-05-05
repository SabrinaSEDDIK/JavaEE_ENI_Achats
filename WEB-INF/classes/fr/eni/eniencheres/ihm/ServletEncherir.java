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

@WebServlet( "/ServletEncherir" )
public class ServletEncherir extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        response.sendRedirect( request.getContextPath() + "/ServletAccueil" );
    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        EnchereManager em = new EnchereManager();
        UtilisateurManager um = new UtilisateurManager();
        HttpSession session = request.getSession();
        String URLRedirection = "";
        List<String> listeErreurs = new ArrayList<>();

        if ( session.getAttribute( "connected" ) != null
                && (boolean) session.getAttribute( "connected" ) == true ) {
            String messageResultat = null;
            int idArticle = 0;

            try {
                idArticle = Integer.parseInt( request.getParameter( "idArticle" ) );
            } catch ( NumberFormatException e ) {
                listeErreurs.add(
                        LecteurMessage
                                .getMessageErreur( CodesResultatServlets.ERREUR_PARSAGE_NOMBRE_SERVLET_ENCHERIR ) );
            }

            try {
                // Récupération de la valeur de l'enchère placée
                int valeurEnchere = Integer.parseInt( request.getParameter( "propositionEnchere" ) );
                // Récupération de l'id de l'enchérisseur
                int idEncherisseur = Integer.parseInt( request.getParameter( "idEncherisseur" ) );
                // Récupération de l'enchère pour vérification entre id du
                // propriétaire et id de l'enchérisseur
                Enchere enchereAffichee = em.recupererDetailEnchereEnCours( idArticle );
                // On récupère les infos de l'enchérisseur
                Utilisateur encherisseur = um.RechercheUtilisateur( idEncherisseur );

                // On ne peut pas enchérir sur un article dont on est le
                // propriétaire

                if ( idEncherisseur != enchereAffichee.getIdVendeur() ) {
                    boolean resultatEnchere = em.encherir( idArticle, encherisseur, valeurEnchere );

                    if ( resultatEnchere ) {
                        messageResultat = "Votre enchère à bien été placée.";
                        // Si le résultat de la mise à jour est bon, on met à
                        // jour
                        // l'objet Utilisateur se trouvant dans la session
                        Utilisateur utilisateur = (Utilisateur) session.getAttribute( "infosUtilisateur" );
                        session.setAttribute( "infosUtilisateur",
                                um.RechercheUtilisateur( utilisateur.getIdUtilisateur() ) );
                        session.setAttribute( messageResultat, utilisateur );
                    } else {
                        BusinessException businessException = new BusinessException();
                        businessException.ajouterErreur( CodesResultatServlets.ERREUR_ECHEC_ENCHERE );
                        throw businessException;
                    }
                } else {
                    BusinessException businessException = new BusinessException();
                    businessException.ajouterErreur( CodesResultatServlets.ERREUR_PROPRE_ENCHERE );
                    throw businessException;
                }

            } catch ( NumberFormatException e ) {
                listeErreurs.add(
                        LecteurMessage
                                .getMessageErreur( CodesResultatServlets.ERREUR_PARSAGE_NOMBRE_SERVLET_ENCHERIR ) );
                request.setAttribute( "listeErreurs", listeErreurs );
            } catch ( BusinessException e ) {
                request.setAttribute( "listeErreurs", e.getListeMessageErreur() );
            }

            URLRedirection = "/ServletAffichageEnchere?idArticle=" + idArticle;
            request.setAttribute( "messageResultat", messageResultat );
        }
        // Si on n'est pas connecté, on n'est pas censé se trouver ici
        else {
            listeErreurs.add( LecteurMessage.getMessageErreur( CodesResultatServlets.ERREUR_NOT_CONNECTED ) );
            request.setAttribute( "listeErreurs", listeErreurs );
            // S'il y a une erreur, on renvoie vers la page d'accueil
            URLRedirection = "/ServletAccueil";
        }

        request.getRequestDispatcher( URLRedirection ).forward( request, response );
    }

}
