package fr.eni.eniencheres.ihm;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet( "/ServletDeconnexion" )
public class ServletDeconnexion extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        // Récupération de la session
        HttpSession sessionUtilisateur = request.getSession();

        if ( sessionUtilisateur.getAttribute( "connected" ) != null
                && (boolean) sessionUtilisateur.getAttribute( "connected" ) == true ) {
            // On détruit la session et tous les objets contenus dedans
            sessionUtilisateur.invalidate();
        }

        // On renvoie avec un redirect pour changer l'url dans le navigateur
        // Si on fait ca avec un requestDispatcher
        // l'utilisateur verra toujours /ServletDeconnexion
        // Sur la page d'accueil
        response.sendRedirect( request.getContextPath() + "/ServletAccueil" );
    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        doGet( request, response );
    }

}
