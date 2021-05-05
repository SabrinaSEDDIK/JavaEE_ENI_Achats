package fr.eni.eniencheres.dal;

/**
 * Les codes disponibles sont entre 10000 et 19999
 */
public abstract class CodesResultatDAL {

    /**
     * Echec g�n�ral quand tentative de s�lection d'objet
     */
    public static final int SELECT_ECHEC                   = 10000;

    /**
     * Echec g�n�ral quand erreur non g�r�e � l'insertion
     */
    public static final int INSERT_OBJET_ECHEC             = 10001;

    /**
     * Echec de l'insertion d'un objet null
     */
    public static final int INSERT_OBJET_NULL              = 10002;

    /**
     * Echec de l'insertion d'un utilisateur car déjà existant
     */
    public static final int INSERT_OBJET_DEJA_EXISTANT     = 10003;
    /**
     * Echec d'un SelectById avec un id nul pour une enchère
     */
    public static final int SELECTBY_ENCHERE_ID_NULL       = 10004;
    /**
     * Echec d'un SelectById pour un article qui n'existe pas
     */
    public static final int SELECTBY_ID_ARTICLE_INEXISTANT = 10005;
    /**
     * Echec d'une insertion d'article
     */
    public static final int INSERT_ERREUR                  = 10006;
    /**
     * Echec à la suppression d'un utilisateur
     */
    public static final int ERREUR_DELETE_UTILISATEUR      = 10007;
    /**
     * Echec de la mise à jour des données d'un utilisateur
     */
    public static final int ERREUR_UPDATE_UTILISATEUR      = 10008;
    /**
     * Erreur lors de la suppression d'une article
     */
    public static final int ERROR_DELETE_ARTICLE           = 10009;
    /**
     * Erreur DAL pour la récupération d'une enchère
     */
    public static final int ERROR_RECUPERATION_ENCHERE     = 10010;
    /**
     * Erreur DAL pour la récupération d'un utilisateur
     */
    public static final int ERROR_RECUPERATION_UTILISATEUR = 10011;
    /**
     * Erreur DAL lors du placement d'une enchère
     */
    public static final int ERROR_PLACEMENT_ENCHERE        = 10012;
    /**
     * Erreur DAL lors du placement d'une enchère avec crédit insuffisant
     */
    public static final int ERROR_CREDIT_INSUFFISANT       = 10013;
    /**
     * Erreur DAL lors de la mise à jour d'un article
     */
    public static final int ERROR_UPDATE_ARTICLE           = 10014;
    /**
     * Erreur DAL lors de la sélection d'un article
     */
    public static final int ERROR_SELECT_ARTICLE           = 10015;
    /**
     * Erreur DAL lorsque l'on essaie de finaliser la vente d'un article déjà
     * vendu
     */
    public static final int ERROR_ARTICLE_DEJA_VENDU       = 10016;
    /**
     * Erreur DAL lorsqu'un problème intervient avec la base de données
     */
    public static final int ERROR_BASE_DE_DONNEES          = 10017;

}
