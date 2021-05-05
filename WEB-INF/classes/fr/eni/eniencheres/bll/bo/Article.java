package fr.eni.eniencheres.bll.bo;

import java.time.LocalDate;

public class Article {

    private Integer   identifiant;
    private String    nom;
    private String    description;
    private LocalDate dateDebutEnchere;
    private LocalDate dateFinEnchere;
    private Integer   miseAPrix;
    private Integer   prixVente;
    private Integer   idVendeur;
    private String    pseudoVendeur;
    private Integer   idCategorie;
    private Retrait   retrait;

    public Article( Integer identifiant, String nom, String description, LocalDate dateDebutEnchere,
            LocalDate dateFinEnchere, Integer miseAPrix, Integer prixVente, Integer idCategorie, Integer idVendeur,
            String pseudoVendeur ) {
        super();
        this.identifiant = identifiant;
        this.nom = nom;
        this.description = description;
        this.dateDebutEnchere = dateDebutEnchere;
        this.dateFinEnchere = dateFinEnchere;
        this.miseAPrix = miseAPrix;
        this.prixVente = prixVente;
        this.idCategorie = idCategorie;
        this.idVendeur = idVendeur;
        this.pseudoVendeur = pseudoVendeur;
    }

    // constructeur INSERT
    public Article( String nom, String description, LocalDate dateDebutEnchere,
            LocalDate dateFinEnchere, Integer miseAPrix, Integer prixVente, Integer idVendeur, Integer idCategorie,
            Retrait retrait ) {
        this.nom = nom;
        this.description = description;
        this.dateDebutEnchere = dateDebutEnchere;
        this.dateFinEnchere = dateFinEnchere;
        this.miseAPrix = miseAPrix;
        this.prixVente = prixVente;
        this.idVendeur = idVendeur;
        this.idCategorie = idCategorie;
        this.retrait = retrait;

    }

    // constructeur Update
    public Article( Integer identifiant, String nom, String description, LocalDate dateDebutEnchere,
            LocalDate dateFinEnchere, Integer miseAPrix, Integer prixVente, Integer idVendeur, Integer idCategorie,
            Retrait retrait ) {
        this.identifiant = identifiant;
        this.nom = nom;
        this.description = description;
        this.dateDebutEnchere = dateDebutEnchere;
        this.dateFinEnchere = dateFinEnchere;
        this.miseAPrix = miseAPrix;
        this.prixVente = prixVente;
        this.idVendeur = idVendeur;
        this.idCategorie = idCategorie;
        this.retrait = retrait;

    }

    // Constructeur utilisé lors de la récupération du détail d'une vente
    public Article( Integer identifiant, String nom, String description, LocalDate dateDebutEnchere,
            LocalDate dateFinEnchere, Integer miseAPrix,
            Integer prixVente, Integer idCategorie, String pseudoVendeur ) {
        super();
        this.identifiant = identifiant;
        this.nom = nom;
        this.description = description;
        this.dateDebutEnchere = dateDebutEnchere;
        this.dateFinEnchere = dateFinEnchere;
        this.miseAPrix = miseAPrix;
        this.prixVente = prixVente;
        this.idCategorie = idCategorie;
        this.pseudoVendeur = pseudoVendeur;
    }

    public Integer getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant( Integer identifiant ) {
        this.identifiant = identifiant;
    }

    public String getNom() {
        return nom;
    }

    public void setNom( String nom ) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public LocalDate getDateDebutEnchere() {
        return dateDebutEnchere;
    }

    public void setDateDebutEnchere( LocalDate dateDebutEnchere ) {
        this.dateDebutEnchere = dateDebutEnchere;
    }

    public LocalDate getDateFinEnchere() {
        return dateFinEnchere;
    }

    public void setDateFinEnchere( LocalDate dateFinEnchere ) {
        this.dateFinEnchere = dateFinEnchere;
    }

    public Integer getMiseAPrix() {
        return miseAPrix;
    }

    public void setMiseAPrix( Integer miseAPrix ) {
        this.miseAPrix = miseAPrix;
    }

    public Integer getPrixVente() {
        return prixVente;
    }

    public void setPrixVente( Integer prixVente ) {
        this.prixVente = prixVente;
    }

    public String getPseudoVendeur() {
        return pseudoVendeur;
    }

    public void setPseudoVendeur( String pseudoVendeur ) {
        this.pseudoVendeur = pseudoVendeur;
    }

    public Integer getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie( Integer idCategorie ) {
        this.idCategorie = idCategorie;
    }

    public Integer getIdVendeur() {
        return idVendeur;
    }

    public void setIdVendeur( Integer idVendeur ) {
        this.idVendeur = idVendeur;
    }

    public Retrait getRetrait() {
        return retrait;
    }

    public void setRetrait( Retrait retrait ) {
        this.retrait = retrait;
    }

    @Override
    public String toString() {
        return "Article [identifiant=" + identifiant + ", nom=" + nom + ", description=" + description
                + ", dateDebutEnchere=" + dateDebutEnchere + ", dateFinEnchere=" + dateFinEnchere + ", miseAPrix="
                + miseAPrix + ", prixVente=" + prixVente + ", idVendeur=" + idVendeur + ", pseudoVendeur="
                + pseudoVendeur + ", idCategorie=" + idCategorie + ", retrait=" + retrait + "]";
    }

}
