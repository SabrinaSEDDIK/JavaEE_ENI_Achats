package fr.eni.eniencheres.bll;

/**
 * Les codes disponibles sont entre 20000 et 29999
 */
public abstract class CodesResultatBLL {

    /**
     * Erreur renvoyée quand le prénom est trop court
     */
    public static final int ERREUR_PSEUDO_TROP_COURT                = 20000;
    /**
     * Erreur renvoyée quand le prénom est trop court
     */
    public static final int ERREUR_PSEUDO_MAUVAIS_FORMAT            = 20001;
    /**
     * Erreur renvoyée quand le nom est null
     */
    public static final int ERREUR_NOM_NULL                         = 20002;
    /**
     * Erreur renvoyée quand le prénom est trop court
     */
    public static final int ERREUR_PRENOM_TROP_COURT                = 20003;
    /**
     * Erreur renvoyée quand l'email est au mauvais format
     */
    public static final int ERREUR_EMAIL_MAUVAIS_FORMAT             = 20004;
    /**
     * Erreur renvoyée quand le champ téléphone est nul
     */
    public static final int ERREUR_CHAMP_TELEPHONE_NULL             = 20005;
    /**
     * Erreur renvoyée quand le champ téléphone est au mauvais format
     */
    public static final int ERREUR_CHAMP_TELEPHONE_MAUVAIS_FORMAT   = 20006;
    /**
     * Erreur renvoyée quand le champ téléphone est trop court
     */
    public static final int ERREUR_CHAMP_TELEPHONE_TROP_COURT       = 20007;
    /**
     * Erreur renvoyée quand le champ codePostal est au mauvais format
     */
    public static final int ERREUR_CHAMP_CODE_POSTAL_NULL           = 20008;
    /**
     * Erreur renvoyée quand le champ codePostal est au mauvais format
     */
    public static final int ERREUR_CHAMP_CODE_POSTAL_MAUVAIS_FORMAT = 20009;
    /**
     * Erreur renvoyée quand le champ téléphone est trop court
     */
    public static final int ERREUR_CHAMP_RUE_NULL                   = 20010;
    /**
     * Erreur renvoyée quand le champ ville est vide
     */
    public static final int ERREUR_CHAMP_VILLE_NULL                 = 20011;
    /**
     * Erreur renvoyée quand les mots de passe sont différents
     */
    public static final int ERREUR_MDP_DIFFERENTS                   = 20012;
    /**
     * Erreur renvoyée quand le mot de passe est trop court
     */
    public static final int ERREUR_MDP_TROP_COURT                   = 20013;
    /**
     * Erreur renvoyée quand on veut sélectionner une enchère avec un id nul
     */
    public static final int ERREUR_SELECT_ENCHERE_ID_NUL            = 20014;
    /**
     * Erreur renvoyée quand les nombres envoyés pour placé une enchère sont
     * négatifs ou nuls.
     */
    public static final int ERREUR_NOMBRES_PLACEMENT_ENCHERE        = 20015;
    
    /**
     * Erreur renvoyée quand la date de début d'enchère est dépassée
     */
    public static final int DATE_DEBUT_ENCHERE_INCORRECTE        = 20016;
    
    /**
     * Erreur renvoyée quand la date de fin d'enchère est dépassée ou datée du jour
     */
    public static final int DATE_FIN_ENCHERE_INCORRECTE        = 20017;
    
    /**
     * Erreur renvoyée quand le prix de vente est négatif
     */
    public static final int PRIX_VENTE_NEGATIF       = 20018;
    
    /**
     * Erreur renvoyée quand la catégorie n'est pas comprise entre 1 et 4 inclus
     */
    public static final int CATEGORIE_INCORRECTE       = 20019;
    

    public static final int ERREUR_ANCIEN_MDP_INCORRECT             = 20020;

    /**
     * Erreur renvoyée quand l'enchère placé est trop faible
     */
    public static final int ERREUR_ENCHERE_TROP_FAIBLE              = 20021;
    /**
     * Erreur renvoyée quand l'enchère placé est supérieur aux crédit de
     * l'enchérisseur
     */
    public static final int ERREUR_CREDIT_INSUFFISANT               = 20022;
    /**
     * Erreur renvoyée quand la personne qui essaie de supprimer un article
     * n'est pas celle qui l'a créé
     */
    public static final int ERREUR_SUPPRESSION_NON_PROPRIETAIRE     = 20023;
    /**
     * Erreur renvoyée quand la personne essaie de supprimer un article dont
     * l'enchère a commencé
     */
    public static final int ERREUR_SUPPRESSION_ENCHERE_COMMENCEE    = 20024;

}