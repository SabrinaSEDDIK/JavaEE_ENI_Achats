package fr.eni.eniencheres.ihm;

import java.io.IOException;
import java.time.LocalDate;
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

@WebServlet( "/ServletAffichageEnchere" )
public class ServletAffichageEnchere extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        // Création de l'url de redirection
        String urlRedirection = "";

        // Création du EnchereManager et de l'UtilisateurManager
        EnchereManager em = new EnchereManager();
        UtilisateurManager um = new UtilisateurManager();
        // Récupération de la session
        HttpSession sessionUtilisateur = request.getSession();
        // On vérifie que le booleen permettant d'indiquer l'état de la
        // connexion n'est pas null
        // puis s'il existe, s'il est sur l'état connecté, car sinon, pas accès
        // à cette servlet
        if ( sessionUtilisateur.getAttribute( "connected" ) != null
                && (boolean) sessionUtilisateur.getAttribute( "connected" ) == true ) {
            try {
                // Récupération de l'idArticle envoyer depuis la requête
                int idArticle = Integer.parseInt( request.getParameter( "idArticle" ) );
                // On récupère dans un objet article l'article voulu
                Enchere enchereRecupere = em.recupererDetailEnchereEnCours( idArticle );
                request.setAttribute( "enchere", enchereRecupere );

                // On vérifie maintenant que l'utilisateur souhaitant récupérer
                // le détail de la vente n'est pas le propriétaire de l'article
                // On récupère les infos de l'utilisateur
                Utilisateur utilisateur = (Utilisateur) sessionUtilisateur.getAttribute( "infosUtilisateur" );

                // Si la date de fin d'enchère est dépassée
                if ( enchereRecupere.getArticle().getDateFinEnchere().isBefore( LocalDate.now().plusDays( 1 ) ) ) {
                    // Il faut récupérer les infos de l'acheteur
                    Utilisateur acheteur = um.RechercheUtilisateur( enchereRecupere.getIdAcheteur() );
                    Utilisateur vendeur = um.RechercheUtilisateur( enchereRecupere.getIdVendeur() );

                    request.setAttribute( "acheteur", acheteur );
                    request.setAttribute( "vendeur", vendeur );

                    urlRedirection = "/WEB-INF/detailVenteTerminee.jsp";
                } else {
                    // Si l'id de l'utilisateur connecté est le même que l'id du
                    // vendeur de l'article de l'enchère
                    if ( utilisateur.getIdUtilisateur()
                            .equals( enchereRecupere.getIdVendeur() )
                            && enchereRecupere.getArticle().getDateDebutEnchere().isAfter( LocalDate.now() ) ) {
                        // On renvoie vers la page de modification d'un article
                        urlRedirection = "/WEB-INF/creationModificationVente.jsp";
                    } else {
                        // Sinon, on renvoie vers la page detailVenteEnCours.jsp
                        urlRedirection = "/WEB-INF/detailVenteEnCours.jsp";
                    }
                }

            } catch ( NumberFormatException e ) {
                List<String> listeMessagesErreur = new ArrayList<>();
                listeMessagesErreur.add( LecteurMessage.getMessageErreur( CodesResultatServlets.ERREUR_ID_NOT_INT ) );
                request.setAttribute( "listeErreurs", listeMessagesErreur );
                // S'il y a une erreur, on renvoie vers la page d'accueil
                urlRedirection = "/ServletAccueil";
            } catch ( BusinessException e ) {
                request.setAttribute( "listeErreurs", e.getListeMessageErreur() );
                // S'il y a une erreur, on renvoie vers la page d'accueil
                urlRedirection = "/ServletAccueil";
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

    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        doGet( request, response );
    }

}
