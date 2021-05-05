package fr.eni.eniencheres.bll;

import fr.eni.eniencheres.bll.bo.Utilisateur;
import fr.eni.eniencheres.dal.DAOFactory;
import fr.eni.eniencheres.dal.UtilisateurDAO;
import fr.eni.eniencheres.messages.BusinessException;

public class UtilisateurManager {

    UtilisateurDAO utilisateurDAO;
    Utilisateur    utilisateur = new Utilisateur();
    boolean        modeModif   = false;

    public UtilisateurManager() {
        utilisateurDAO = DAOFactory.getUtilisateurDAO();
    }

    public Utilisateur creerUtilisateur( Integer idUtilisateur, String pseudo, String nom, String prenom, String email,
            String telephone,
            String rue,
            String codePostal, String ville, String ancienMdP, String motDePasse, String ConfirmationMdP,
            String nouveauMdP, String choixUtilisateur ) throws BusinessException {
        // TODO controler tous les éléments du formulaire et renvoyer une
        // BusinessException si probl�me
        BusinessException businessException = new BusinessException();

        // Validation des différents champs
        if ( choixUtilisateur.equals( "Enregistrer" ) ) {
            utilisateur.setIdUtilisateur( idUtilisateur );
            modeModif = true;
        }
        validationPseudo( pseudo, businessException );
        utilisateur.setPseudo( pseudo );
        validationNom( nom, businessException );
        utilisateur.setNom( nom );
        validationPrenom( prenom, businessException );
        utilisateur.setPrenom( prenom );
        validationChampEmail( email, businessException );
        utilisateur.setEmail( email );
        validationTelephone( telephone, businessException );
        utilisateur.setTelephone( telephone );
        validationRue( rue, businessException );
        utilisateur.setRue( rue );
        validationCodePostal( codePostal, businessException );
        utilisateur.setCodePostal( codePostal );
        validationVille( ville, businessException );
        utilisateur.setVille( ville );

        if ( modeModif ) {
            validationAncienMdP( ancienMdP, motDePasse, businessException );
            validationMotDePasse( nouveauMdP, ConfirmationMdP, businessException );
            if ( nouveauMdP.trim().isEmpty() == false ) {
                utilisateur.setMotDePasse( nouveauMdP );
            } else {
                utilisateur.setMotDePasse( ancienMdP );
            }
        } else {
            validationMotDePasse( motDePasse, ConfirmationMdP, businessException );
            utilisateur.setMotDePasse( motDePasse );
        }

        Utilisateur utilisateurOK = null;
        // Si après validation des champs, l'objet contenant les exceptions n'a
        // pas d'erreurs
        if ( !businessException.hasErreurs() ) {
            if ( choixUtilisateur.equals( "Enregistrer" ) ) {
                utilisateurOK = utilisateurDAO.update( utilisateur );
            } else {
                // On demande à la DAL d'enregistrer l'utilisateur.
                utilisateurOK = utilisateurDAO.insert( utilisateur );
            }
        }
        // Sinon, on renvoie l'erreur à la couche supérieure
        else {
            throw businessException;
        }
        return utilisateurOK;
    }

    // Vérification par la BLL que l'utilisateur respecte les conditions avant
    // envoi DAL
    public Utilisateur verifUtilisateur( String pseudoMail, String motDePasse ) throws BusinessException {
        // BusinessException businessException = new BusinessException();
        // validationPseudo( pseudoMail, businessException );
        // Après validation des champs, même chose que créer utilisateur
        // if ( !businessException.hasErreurs() ) {
        // On demande à la DAL de vérifier l'utilisateur.
        return utilisateurDAO.selectFrom( pseudoMail, motDePasse );

        // boolean résultat
        // test boolean
        // objet utilisateur toutes les infos - retourne objet utilisateur -
        // mettre dans session
        // }
        // Sinon, on renvoie l'erreur à la couche supérieure
        // else {
        // throw businessException;
        // }
    }

    public Utilisateur RechercheUtilisateur( int id ) throws BusinessException {
        return utilisateurDAO.selectById( id );
    }

    // Getter de l'utilisateur en train d'être enregistré
    // Afin de pouvoir afficher ces infos dans le formulaire en cas d'erreur
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    private void validationPseudo( String pseudo, BusinessException businessException ) {
        // TODO Vérification caractère alphanumérique uniquement
        // Le pseudo doit contenir au moins 2 lettres et ne pas être nul
        if ( pseudo != null && pseudo.trim().length() < 2 ) {
            businessException.ajouterErreur( CodesResultatBLL.ERREUR_PSEUDO_TROP_COURT );
        }
        if ( pseudo != null && !pseudo.matches( "^[a-zA-Z0-9]*$" ) ) {
            businessException.ajouterErreur(
                    CodesResultatBLL.ERREUR_PSEUDO_MAUVAIS_FORMAT );
        }
    }

    private void validationNom( String nom, BusinessException businessException ) {
        // Le champ nom ne doit pas être vide
        if ( nom == null ) {
            businessException.ajouterErreur( CodesResultatBLL.ERREUR_NOM_NULL );
        }
    }

    private void validationPrenom( String prenom, BusinessException businessException ) {
        // Le prénom doit contenir au moins 2 lettres et ne pas être nul
        if ( prenom != null && prenom.trim().length() < 2 ) {
            businessException.ajouterErreur( CodesResultatBLL.ERREUR_PRENOM_TROP_COURT );
        }
    }

    private void validationChampEmail( String email, BusinessException businessException ) {
        // Vérification à l'aide d'une regex du format de l'adresse email
        if ( email != null && !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) {
            businessException.ajouterErreur( CodesResultatBLL.ERREUR_EMAIL_MAUVAIS_FORMAT );
        }
    }

    private void validationTelephone( String telephone, BusinessException businessException ) {
        if ( telephone != null ) {
            if ( !telephone.matches( "^\\d+$" ) ) {
                businessException.ajouterErreur( CodesResultatBLL.ERREUR_CHAMP_TELEPHONE_MAUVAIS_FORMAT );
            } else if ( telephone.trim().length() < 4 ) {
                businessException.ajouterErreur( CodesResultatBLL.ERREUR_CHAMP_TELEPHONE_TROP_COURT );
            }
        } else {
            businessException.ajouterErreur( CodesResultatBLL.ERREUR_CHAMP_TELEPHONE_NULL );
        }
    }

    private void validationRue( String rue, BusinessException businessException ) {
        // Le champ Rue ne doit pas être vide
        if ( rue == null || rue.trim().length() == 0 ) {
            businessException.ajouterErreur( CodesResultatBLL.ERREUR_CHAMP_RUE_NULL );
        }
    }

    private void validationCodePostal( String codePostal, BusinessException businessException ) {
        if ( codePostal != null ) {
            if ( !codePostal.matches( "^\\d+$" ) ) {
                businessException.ajouterErreur( CodesResultatBLL.ERREUR_CHAMP_CODE_POSTAL_MAUVAIS_FORMAT );
            }
        } else {
            businessException.ajouterErreur( CodesResultatBLL.ERREUR_CHAMP_CODE_POSTAL_NULL );
        }
    }

    private void validationVille( String ville, BusinessException businessException ) {
        // Le champ Ville ne doit pas être vide
        if ( ville == null || ville.trim().length() == 0 ) {
            businessException.ajouterErreur( CodesResultatBLL.ERREUR_CHAMP_VILLE_NULL );
        }
    }

    private void validationMotDePasse( String motDePasse, String confirmationMdP,
            BusinessException businessException ) {
        // Les deux mots de passe doivent être identiques
        if ( motDePasse.equals( confirmationMdP ) ) {
            if ( modeModif && motDePasse.equals( "" ) ) {
                // ne rien faire^^
            }
            // Le mot de passe doit contenir au moins 5 caractères et ne pas
            // être nul
            else if ( motDePasse != null && motDePasse.trim().length() < 8 ) {
                businessException.ajouterErreur( CodesResultatBLL.ERREUR_MDP_TROP_COURT );
            }
        } else {
            businessException.ajouterErreur( CodesResultatBLL.ERREUR_MDP_DIFFERENTS );
        }
    }

    private void validationAncienMdP( String ancienMdP, String motDePasse, BusinessException businessException ) {

        if ( !motDePasse.equals( ancienMdP ) ) {
            businessException.ajouterErreur( CodesResultatBLL.ERREUR_ANCIEN_MDP_INCORRECT );
        }

    }

    ///////////////////////// DELETE ////////////////////////////////////////

    public void supprimerUtilisateur( Utilisateur utilisateur ) throws BusinessException {
        utilisateurDAO.delete( utilisateur );

    }

    public void creditementVendeurEnchereTerminee( Integer idUtilisateur, Integer idArticle, Integer prixVente )
            throws BusinessException {
        utilisateurDAO.crediterUtilisateur( idUtilisateur, idArticle, prixVente );
    }

}
