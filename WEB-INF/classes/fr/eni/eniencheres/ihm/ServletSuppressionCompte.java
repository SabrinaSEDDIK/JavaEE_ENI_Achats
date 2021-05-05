package fr.eni.eniencheres.ihm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fr.eni.eniencheres.bll.UtilisateurManager;
import fr.eni.eniencheres.bll.bo.Utilisateur;
import fr.eni.eniencheres.messages.BusinessException;

/**
 * Servlet implementation class ServletSuppressionCompte
 */
@WebServlet( "/ServletSuppressionCompte" )
public class ServletSuppressionCompte extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        List<String> listeErreurs = new ArrayList<>();
        try {
            UtilisateurManager um = new UtilisateurManager();
            um.supprimerUtilisateur( ( (Utilisateur) session.getAttribute( "infosUtilisateur" ) ) );
        } catch ( BusinessException be ) {
            be.printStackTrace();
            if ( !listeErreurs.isEmpty() ) {
                listeErreurs.addAll( be.getListeMessageErreur() );
            } else {
                listeErreurs = be.getListeMessageErreur();
            }
        }

        request.setAttribute( "listeErreurs", listeErreurs );
        String URLRedirection;
        if ( !listeErreurs.isEmpty() ) {
            request.setAttribute( "suppressionCompte", false );
            URLRedirection = "/ServletAffichageProfil";
        } else {
            request.setAttribute( "suppressionCompte", true );
            request.getSession().setAttribute( "infosUtilisateur", null );
            request.getSession().setAttribute( "connected", false );
            URLRedirection = "/ServletAccueil";
        }
        RequestDispatcher rd = request.getRequestDispatcher( URLRedirection );
        rd.forward( request, response );

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
