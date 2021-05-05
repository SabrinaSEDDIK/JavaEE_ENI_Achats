package fr.eni.eniencheres.ihm;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fr.eni.eniencheres.bll.ArticleManager;
import fr.eni.eniencheres.bll.bo.Utilisateur;
import fr.eni.eniencheres.messages.BusinessException;

/**
 * Servlet implementation class ServletEnregistrementArticle
 */

@WebServlet( "/ServletEnregistrementArticle" )

public class ServletEnregistrementArticle extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        System.out.println( LocalDate.now().isAfter( LocalDate.now() ) );
        // R�cup�ration de la session
        HttpSession session = request.getSession();

        // R�cup�ration des informations du formulaire
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
            Integer idUtilisateur = ( (Utilisateur) session.getAttribute( "infosUtilisateur" ) ).getIdUtilisateur();
            Integer idCategorie = Integer.parseInt( categorie );
            LocalDate dateDebut = LocalDate.parse( debutEnchere );
            LocalDate dateFin = LocalDate.parse( finEnchere );
            Integer prix = Integer.parseInt( prixInitial );
            // Insertion
            ArticleManager am = new ArticleManager();
            am.insertionArticle( nomArticle, description, dateDebut, dateFin, prix, idUtilisateur, idCategorie, rue,
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
            request.setAttribute( "succesInsertion", false );
            URLRedirection = "/ServletNouvelleVente";
        } else {
            request.setAttribute( "succesInsertion", true );
            URLRedirection = "/ServletAccueil";
        }
        RequestDispatcher rd = request.getRequestDispatcher( URLRedirection );
        rd.forward( request, response );

    }

}
