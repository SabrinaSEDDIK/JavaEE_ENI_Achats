package fr.eni.eniencheres.ihm;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fr.eni.eniencheres.bll.ArticleManager;
import fr.eni.eniencheres.bll.bo.Utilisateur;
import fr.eni.eniencheres.messages.BusinessException;

@WebServlet( "/ServletModificationEnchere" )
public class ServletModificationEnchere extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        request.getRequestDispatcher( "/ServletAccueil" ).forward( request, response );
    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        // Récupération de la session
        HttpSession session = request.getSession();

        // Récupération des informations du formulaire
        String nomArticle = request.getParameter( "nomArticle" );
        String description = request.getParameter( "description" );
        String categorie = request.getParameter( "categorie" );
        String prixInitial = request.getParameter( "prixInitial" );
        String debutEnchere = request.getParameter( "debutEnchere" );
        String finEnchere = request.getParameter( "finEnchere" );
        String rue = request.getParameter( "rue" );
        String codePostal = request.getParameter( "codePostal" );
        String ville = request.getParameter( "ville" );

        List<String> listeErreurs = new ArrayList<>();
        // Conversion
        try {
            int idArticle = Integer.parseInt( request.getParameter( "idArticle" ) );
            Integer idUtilisateur = ( (Utilisateur) session.getAttribute( "infosUtilisateur" ) ).getIdUtilisateur();
            Integer idCategorie = Integer.parseInt( categorie );
            LocalDate dateDebut = LocalDate.parse( debutEnchere );
            LocalDate dateFin = LocalDate.parse( finEnchere );
            Integer prix = Integer.parseInt( prixInitial );
            // Insertion
            ArticleManager am = new ArticleManager();
            am.modificationArticle( idArticle, nomArticle, description, dateDebut, dateFin, prix, idUtilisateur,
                    idCategorie, rue,
                    codePostal, ville );
            // Gestion des erreurs
        } catch ( IllegalStateException ise ) {
            ise.printStackTrace();
            listeErreurs.add( "Une erreur s'est produite, veuillez recommencer." );
        } catch ( NumberFormatException nfe ) {
            nfe.printStackTrace();
            listeErreurs.add( "Certains champs sont incorrects, veuillez recommencer." );
        } catch ( BusinessException be ) {
            be.printStackTrace();
            if ( !listeErreurs.isEmpty() ) {
                listeErreurs.addAll( be.getListeMessageErreur() );
            } else {
                listeErreurs = be.getListeMessageErreur();
            }
        }

        request.setAttribute( "listeErreurs", listeErreurs );

        // Redirection
        String URLRedirection;
        if ( !listeErreurs.isEmpty() ) {
            request.setAttribute( "succesModificationArticle", false );
            URLRedirection = "/ServletAffichageEnchere&idArticle="
                    + ( (Utilisateur) session.getAttribute( "infosUtilisateur" ) ).getIdUtilisateur();
        } else {
            request.setAttribute( "succesModificationArticle", true );
            URLRedirection = "/ServletAccueil";
        }
        RequestDispatcher rd = request.getRequestDispatcher( URLRedirection );
        rd.forward( request, response );
    }

}
