package fr.eni.eniencheres.bll;

import fr.eni.eniencheres.bll.bo.Enchere;
import fr.eni.eniencheres.bll.bo.Utilisateur;
import fr.eni.eniencheres.dal.DAOFactory;
import fr.eni.eniencheres.dal.EnchereDAO;
import fr.eni.eniencheres.messages.BusinessException;

public class EnchereManager {

    EnchereDAO enchereDAO;

    public EnchereManager() {
        enchereDAO = DAOFactory.getEnchereDAO();
    }

    public Enchere recupererDetailEnchereEnCours( int id ) throws BusinessException {
        if ( id != 0 ) {
            return enchereDAO.selectById( id );
        } else {
            BusinessException businessException = new BusinessException();
            businessException.ajouterErreur( CodesResultatBLL.ERREUR_SELECT_ENCHERE_ID_NUL );
            throw businessException;
        }

    }

    public boolean encherir( int idArticle, Utilisateur encherisseur, int valeurEnchere ) throws BusinessException {

        BusinessException businessException = new BusinessException();

        // On vérifie la valeur des nombres fournit par la servlet.
        verifierNombre( valeurEnchere, businessException );
        verifierNombre( idArticle, businessException );
        verifierNombre( encherisseur.getIdUtilisateur(), businessException );

        Enchere enchereMaxArticle = enchereDAO.selectById( idArticle );

        // On vérifie le montant de l'enchère
        verifierEnchere( valeurEnchere, enchereMaxArticle.getArticle().getPrixVente(), businessException );
        // On vérifie les crédits
        verifierCredit( valeurEnchere, encherisseur.getCredit(), businessException );

        // On vérifie que les vérifications sur les nombres n'ont pas renvoyées
        // d'erreurs
        if ( !businessException.hasErreurs() ) {
            // On place l'enchère
            boolean resultatEnchere = enchereDAO.encherir( idArticle, encherisseur.getIdUtilisateur(),
                    valeurEnchere );

            return resultatEnchere;
        } else {
            throw businessException;
        }

    }

    private void verifierCredit( int valeurEnchere, int credit, BusinessException businessException ) {
        // Si l'enchère placé est supérieur aux crédits de l'enchérisseur
        if ( valeurEnchere > credit ) {
            businessException.ajouterErreur( CodesResultatBLL.ERREUR_CREDIT_INSUFFISANT );
        }

    }

    private void verifierEnchere( int valeurEnchere, int valeurEnchereMaxActuelle,
            BusinessException businessException ) {
        // Si l'enchère placée est inférieur à l'enchère max actuelle
        if ( valeurEnchereMaxActuelle >= valeurEnchere ) {
            businessException.ajouterErreur( CodesResultatBLL.ERREUR_ENCHERE_TROP_FAIBLE );
        }
    }

    private void verifierNombre( int nombre, BusinessException businessException ) {

        if ( nombre <= 0 ) {
            businessException.ajouterErreur( CodesResultatBLL.ERREUR_NOMBRES_PLACEMENT_ENCHERE );
        }
    }

    public void majEnchere() throws BusinessException {
        enchereDAO.majEnchere();
    }

}
