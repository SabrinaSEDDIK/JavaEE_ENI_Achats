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
import fr.eni.eniencheres.bll.bo.Article;
import fr.eni.eniencheres.bll.bo.Utilisateur;
import fr.eni.eniencheres.messages.BusinessException;

//SERVLET QUI MENE A LA PAGE D'ACCUEIL

@WebServlet( "/ServletAccueil" )
public class ServletAccueil extends HttpServlet {

    private static final long serialVersionUID    = 1L;
    private List<String>      listeMessagesErreur = null;
    private HttpSession       session             = null;

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        // On récupère la session
        session = request.getSession();
        // R�cup�rer les param�tres du formulaire
        String motRecherche = request.getParameter( "recherche" );
        String categories = request.getParameter( "categories" );
        //SAUVEGARDE
        request.setAttribute("motRecherche", motRecherche);
        

        
        if ( categories == null ) {
            motRecherche = "";
            categories = "1234";
        }
        switch ( categories ) {
        case "1":
            categories = "1111";
            //SAUVEGARDE
            request.setAttribute("categorie", 1);
            break;
        case "2":
            categories = "2222";
            //SAUVEGARDE
            request.setAttribute("categorie", 2);
            break;
        case "3":
            categories = "3333";
            //SAUVEGARDE
            request.setAttribute("categorie", 3);
            break;
        case "4":
            categories = "4444";
            //SAUVEGARDE
            request.setAttribute("categorie", 4);
            break;
        default:
            categories = "1234";
            //SAUVEGARDE
            request.setAttribute("categorie", 0);
        }

        try {
            ArticleManager am = new ArticleManager();
            // On regarde si l'attribut connected existe
            // et si on est bien connecté
            if ( session.getAttribute( "connected" ) != null
                    && (boolean) session.getAttribute( "connected" ) == true ) {
                int idUtilisateur = ( (Utilisateur) session.getAttribute( "infosUtilisateur" ) ).getIdUtilisateur();
                // 3 voies possibles : VENTES - ACHATS - AUCUN
                String modeRecherche = request.getParameter( "mode" );
                ////////// VENTES //////////
                if ( modeRecherche != null && modeRecherche.equals( "ventes" ) ) {
                	//SAUVEGARDE
                	request.setAttribute("mode", 2);
                    String ventesEnCours = request.getParameter( "ventesEnCours" );
                    String ventesNonDebutees = request.getParameter( "ventesNonDebutees" );
                    String ventesTerminees = request.getParameter( "ventesTerminees" );
                    List<Article> listeVentesEnCours = am.getVentesEnCours( motRecherche, categories, idUtilisateur );
                    List<Article> listeVentesNonDebutees = am.getVentesNonDebutees( motRecherche, categories,
                            idUtilisateur );
                    List<Article> listeVentesTerminees = am.getVentesTerminees( motRecherche, categories,
                            idUtilisateur );
                    List<Article> listeToutesVentes = new ArrayList<>();
                    
                    if ( ventesEnCours != null ) {
                    	//SAUVEGARDE
                    	request.setAttribute("ventesEnCours", true);
                        listeToutesVentes = listeVentesEnCours;
                    }
                    if ( ventesNonDebutees != null ) {
                    	//SAUVEGARDE
                    	request.setAttribute("ventesNonDebutees", true);
                        if ( listeToutesVentes.isEmpty() ) {
                            listeToutesVentes = listeVentesNonDebutees;
                        } else {
                            listeToutesVentes.addAll( listeVentesNonDebutees );
                        }
                    }
                    if ( ventesTerminees != null ) {
                    	//SAUVEGARDE
                    	request.setAttribute("ventesTerminees", true);
                        if ( listeToutesVentes.isEmpty() ) {
                            listeToutesVentes = listeVentesTerminees;
                        } else {
                            listeToutesVentes.addAll( listeVentesTerminees );
                        }
                    }
                    if ( ventesEnCours == null && ventesNonDebutees == null && ventesTerminees == null ) {
                        listeToutesVentes = listeVentesEnCours;
                        if ( listeToutesVentes.isEmpty() ) {
                            listeToutesVentes = listeVentesNonDebutees;
                        } else {
                            listeToutesVentes.addAll( listeVentesNonDebutees );
                        }
                        if ( listeToutesVentes.isEmpty() ) {
                            listeToutesVentes = listeVentesTerminees;
                        } else {
                            listeToutesVentes.addAll( listeVentesTerminees );
                        }
                    }
                    request.setAttribute( "listeArticles", listeToutesVentes );

                    ////////// ACHATS //////////
                } else if ( modeRecherche != null && modeRecherche.equals( "achats" ) ) {
                	//SAUVEGARDE
                	request.setAttribute("mode", 1);
                    List<Article> listeAchats = new ArrayList<>();
                    String encheresOuvertes = request.getParameter( "encheresOuvertes" );
                    String encheresEnCours = request.getParameter( "encheresEnCours" );
                    String encheresRemportees = request.getParameter( "encheresRemportees" );

                    if ( encheresOuvertes == null && encheresEnCours == null && encheresRemportees == null ) {
                        listeAchats = am.getEncheresOuvertes( motRecherche, categories );
                    } else {
                        if ( encheresOuvertes != null ) {
                        	//SAUVEGARDE
                        	request.setAttribute("encheresOuvertes", true);
                            listeAchats = am.getEncheresOuvertes( motRecherche, categories );
                        }
                        if ( encheresEnCours != null ) {
                        	//SAUVEGARDE
                        	request.setAttribute("encheresEnCours", true);
                            if ( listeAchats.isEmpty() ) {
                                listeAchats = am.getEncheresEnCours( motRecherche, categories, idUtilisateur );
                   
                            }
                        }
                        if ( encheresRemportees != null ) {
                        	//SAUVEGARDE
                        	request.setAttribute("encheresRemportees", true);
                            List<Article> listeEncheresRemportees = am.getEncheresRemportees( motRecherche, categories,
                                    idUtilisateur );
                            if ( listeAchats.isEmpty() ) {
                                listeAchats = listeEncheresRemportees;
                            } else {
                                listeAchats.addAll( listeEncheresRemportees );
                            }
                        }
                    }
                    request.setAttribute( "listeArticles", listeAchats );

                } else { ////// CONNECTE - NI ACHAT NI VENTE ////////
                    List<Article> listeArticles = am.getEncheresOuvertes( motRecherche, categories );
                    request.setAttribute( "listeArticles", listeArticles );
                }
            } else { ////// NON CONNECTE ////////
                List<Article> listeArticles = am.getEncheresOuvertes( motRecherche, categories );
                request.setAttribute( "listeArticles", listeArticles );
            }

        } catch ( BusinessException be ) {
            listeMessagesErreur = new ArrayList<>();
            be.printStackTrace();
            for ( String string : be.getListeMessageErreur() ) {
                listeMessagesErreur.add( string );
            }
            request.setAttribute( "listeErreurs", listeMessagesErreur );
        }

        request.setAttribute( "session", session );
        request.getRequestDispatcher( "/WEB-INF/accueil.jsp" ).forward( request, response );

    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        

        doGet( request, response );
    }

}
