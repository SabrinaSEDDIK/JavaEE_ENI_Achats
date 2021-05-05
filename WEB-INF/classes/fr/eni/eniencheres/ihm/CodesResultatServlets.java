package fr.eni.eniencheres.ihm;

/**
 * Les codes disponibles sont entre 30000 et 39999
 */
public abstract class CodesResultatServlets {

    public static final int ERREUR_FORMAT                           = 30000;

    /**
     * Erreur lancée lorsque l'id envoyé pour l'enchère n'est pas un id
     */
    public static final int ERREUR_ID_NOT_INT                       = 30001;
    /**
     * Erreur lancée lorsque l'on essaie d'accéder à une page sans être connecté
     * alors que la connexion est nécessaire
     */
    public static final int ERREUR_NOT_CONNECTED                    = 30002;
    /**
     * Erreur lancé lorsque lors de la connexion, le pseudo/email et mdp ne sont
     * pas concordant
     */
    public static final int ERREUR_IDENTIFIANT_NON_CONCORDANT       = 30003;
    /**
     * Erreur lancé lorsque l'id de l'enchère n'est pas un nombre
     */
    public static final int ERREUR_PARSAGE_NOMBRE_SERVLET_ENCHERIR  = 30004;
    /**
     * Erreur survenant lorsque l'on inscrit de mauvais identifiants
     */
    public static final int ERREUR_WRONG_ID                         = 30005;
    /**
     * Erreur survenant lorsqu'un utilisateur essaie d'enchérir sur une de ses
     * enchères
     */
    public static final int ERREUR_PROPRE_ENCHERE                   = 30006;
    /**
     * Erreur survenant lorsqu'une enchère échoue
     */
    public static final int ERREUR_ECHEC_ENCHERE                    = 30007;
    /**
     * Erreur survenant lorsqu'une enchère échoue
     */
    public static final int ERREUR_CLOTURE_ENCHERE_NON_PROPRIETAIRE = 30008;
    /**
     * Erreur survenant lorsque l'on inscrit de mauvais identifiants
     */
    public static final int ERREUR_UTILISATEUR_SUPPRIME             = 30009;

}