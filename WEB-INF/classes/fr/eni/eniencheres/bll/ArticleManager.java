package fr.eni.eniencheres.bll;

import java.time.LocalDate;
import java.util.List;

import fr.eni.eniencheres.bll.bo.Article;
import fr.eni.eniencheres.bll.bo.Retrait;
import fr.eni.eniencheres.dal.ArticleDAO;
import fr.eni.eniencheres.dal.DAOFactory;
import fr.eni.eniencheres.messages.BusinessException;

public class ArticleManager {

    private ArticleDAO articleDAO = DAOFactory.getArticleDAO();

    ///////////////////////////////////// SELECT
    ///////////////////////////////////// ///////////////////////////////////////////////////

    public List<Article> getEncheresOuvertes( String motRecherche, String categories ) throws BusinessException {
        return articleDAO.selectEncheresOuvertes( motRecherche, categories );
    }

    public List<Article> getEncheresEnCours( String motRecherche, String categories, int idUtilisateur )
            throws BusinessException {
        return articleDAO.selectEncheresEnCours( motRecherche, categories, idUtilisateur );
    }

    public List<Article> getEncheresRemportees( String motRecherche, String categories, int idUtilisateur )
            throws BusinessException {
        return articleDAO.selectEncheresRemportees( motRecherche, categories, idUtilisateur );
    }

    public List<Article> getVentesEnCours( String motRecherche, String categories, int idUtilisateur )
            throws BusinessException {
        return articleDAO.selectVentesEnCours( motRecherche, categories, idUtilisateur );
    }

    public List<Article> getVentesNonDebutees( String motRecherche, String categories, int idUtilisateur )
            throws BusinessException {
        return articleDAO.selectVentesNonDebutees( motRecherche, categories, idUtilisateur );
    }

    public List<Article> getVentesTerminees( String motRecherche, String categories, int idUtilisateur )
            throws BusinessException {
        return articleDAO.selectVentesTerminees( motRecherche, categories, idUtilisateur );
    }

    public Article selectById( int idArticle ) throws BusinessException {
        // TODO Tester l'idArticle
        return articleDAO.selectById( idArticle );
    }

    //////////////////////// INSERT
    //////////////////////// ///////////////////////////////////////////////////////////

    public void insertionArticle( String nomArticle, String description, LocalDate debutEnchere, LocalDate finEnchere,
            Integer prixInitial, Integer idUtilisateur, Integer idCategorie, String rue, String codePostal,
            String ville ) throws BusinessException {
        BusinessException be = new BusinessException();
        // La date de d�but d'ench�re est d�pass�e
        if ( debutEnchere.isBefore( LocalDate.now() ) ) {
            be.ajouterErreur( CodesResultatBLL.DATE_DEBUT_ENCHERE_INCORRECTE );
        }
        // La date de fin d'ench�re ne peut pas �tre d�pass�e ou dat�e � ce jour
        if ( finEnchere.isBefore( debutEnchere ) || finEnchere.isEqual( debutEnchere ) ) {
            be.ajouterErreur( CodesResultatBLL.DATE_FIN_ENCHERE_INCORRECTE );
        }
        // Le prix de vente entr� est n�gatif
        if ( prixInitial < 0 ) {
            be.ajouterErreur( CodesResultatBLL.PRIX_VENTE_NEGATIF );
        }
        // La catégorie est comprise entre 1 et 4
     
        if ( idCategorie < 1 || idCategorie > 4 ) {
            be.ajouterErreur( CodesResultatBLL.CATEGORIE_INCORRECTE );
        }
        if ( be.hasErreurs() ) {
            throw be;
        } else {
            // création objet retrait
            Retrait retrait = new Retrait( rue, codePostal, ville );
            // insertion article
            Article article = new Article( nomArticle, description, debutEnchere, finEnchere, prixInitial, null,
                    idUtilisateur, idCategorie, retrait );
            articleDAO.insertArticle( article );
        }

    }

    // Modification d'une enchère

    public void modificationArticle( int idArticle, String nomArticle, String description, LocalDate debutEnchere,
            LocalDate finEnchere, Integer prixInitial, Integer idUtilisateur, Integer idCategorie, String rue,
            String codePostal, String ville ) throws BusinessException {
        BusinessException be = new BusinessException();
        // La date de début d'enchère est dépassée
        if ( debutEnchere.isBefore( LocalDate.now() ) ) {
            be.ajouterErreur( CodesResultatBLL.DATE_DEBUT_ENCHERE_INCORRECTE);
        }
        // La date de fin d'enchère ne peut pas être dépassée ou datée à ce jour
        if ( finEnchere.isBefore( debutEnchere ) || finEnchere.isEqual( debutEnchere ) ) {
            be.ajouterErreur(CodesResultatBLL.DATE_FIN_ENCHERE_INCORRECTE );
        }
        // Le prix de vente entré est négatif
        if ( prixInitial < 0 ) {
            be.ajouterErreur(  CodesResultatBLL.PRIX_VENTE_NEGATIF);
        }
        // Catégorie est comprise entre 1 et 4
        if ( idCategorie < 1 || idCategorie > 4 ) {
            be.ajouterErreur( CodesResultatBLL.CATEGORIE_INCORRECTE );
        }
        if ( be.hasErreurs() ) {
            throw be;
        } else {
            // Creation objet retrait
            Retrait retrait = new Retrait( rue, codePostal, ville );
            // Update de l'article
            Article article = new Article( idArticle, nomArticle, description, debutEnchere, finEnchere, prixInitial,
                    null,
                    idUtilisateur, idCategorie, retrait );
            articleDAO.updateArticle( article );
        }

    }

    public boolean annulerVente( int idArticle, int idUtilisateur ) throws BusinessException {
        BusinessException businessException = new BusinessException();

        // On récupère l'article
        Article articleEnCoursDeSuppression = selectById( idArticle );
        // On vérifie que celui qui essaie de supprimer est bien celui qui l'a
        // créé
        verifierProprietaire( articleEnCoursDeSuppression.getIdVendeur(), idUtilisateur, businessException );
        // On vérifie que la date de début d'enchère n'est pas passé
        verifierDate( articleEnCoursDeSuppression.getDateDebutEnchere(), businessException );

        if ( !businessException.hasErreurs() ) {
            return articleDAO.deleteArticle( idArticle );
        } else {
            throw businessException;
        }
    }

    public void verifierDate( LocalDate date, BusinessException businessException ) {
        if ( date.isBefore( LocalDate.now() ) ) {
            businessException.ajouterErreur( CodesResultatBLL.ERREUR_SUPPRESSION_ENCHERE_COMMENCEE );
        }
    }

    public void verifierProprietaire( int idVendeurArticle, int idUtilisateur, BusinessException businessException ) {
        if ( idVendeurArticle != idUtilisateur ) {
            businessException.ajouterErreur( CodesResultatBLL.ERREUR_SUPPRESSION_NON_PROPRIETAIRE );
        }
    }

}
