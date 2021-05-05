package fr.eni.eniencheres.bll.bo;

public class Enchere {

    private Article article;
    private String  categorie;
    private int     idVendeur;
    private int     idAcheteur;
    private String  rue;
    private String  codePostal;
    private String  ville;

    public Enchere( Article article, String categorie, int idVendeur, int idAcheteur, String rue, String codePostal,
            String ville ) {
        this.article = article;
        this.categorie = categorie;
        this.idVendeur = idVendeur;
        this.idAcheteur = idAcheteur;
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
    }

    public Enchere( Article article, String categorie, int idVendeur, String rue, String codePostal,
            String ville ) {
        this.article = article;
        this.categorie = categorie;
        this.idVendeur = idVendeur;
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle( Article article ) {
        this.article = article;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie( String categorie ) {
        this.categorie = categorie;
    }

    public int getIdVendeur() {
        return idVendeur;
    }

    public void setIdVendeur( int idVendeur ) {
        this.idVendeur = idVendeur;
    }

    public int getIdAcheteur() {
        return idAcheteur;
    }

    public void setIdAcheteur( int idAcheteur ) {
        this.idAcheteur = idAcheteur;
    }

    public String getRue() {
        return rue;
    }

    public void setRue( String rue ) {
        this.rue = rue;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal( String codePostal ) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille( String ville ) {
        this.ville = ville;
    }

    @Override
    public String toString() {
        return "Enchere [article=" + article + ", categorie=" + categorie + ", idVendeur=" + idVendeur + ", idAcheteur="
                + idAcheteur + ", rue=" + rue + ", codePostal=" + codePostal + ", ville=" + ville + "]";
    }

}
