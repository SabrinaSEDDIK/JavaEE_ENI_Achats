package fr.eni.eniencheres.bll.bo;

public class Utilisateur {

    private Integer idUtilisateur;
    private String  pseudo;
    private String  nom;
    private String  prenom;
    private String  email;
    private String  telephone;
    private String  rue;
    private String  codePostal;
    private String  ville;
    private String  motDePasse;
    private Integer credit;

    public Utilisateur() {
        super();
    }

    public Utilisateur( Integer idUtilisateur, String pseudo, String nom, String prenom, String email, String telephone,
            String rue, String codePostal, String ville, String motDePasse, Integer credit ) {
        this( pseudo, nom, prenom, email, telephone, rue, codePostal, ville, motDePasse, credit );
        this.setIdUtilisateur( idUtilisateur );
    }

    // Ce constructeur sert à la création d'un objet utilisateur
    // A renvoyer en session après création d'un compte
    public Utilisateur( String pseudo, String nom, String prenom, String email, String telephone, String rue,
            String codePostal, String ville, String motDePasse, Integer credit ) {
        this.setPseudo( pseudo );
        this.setNom( nom );
        this.setPrenom( prenom );
        this.setEmail( email );
        this.setTelephone( telephone );
        this.setRue( rue );
        this.setCodePostal( codePostal );
        this.setVille( ville );
        this.setMotDePasse( motDePasse );
        this.setCredit( credit );
    }
 
 

    public Integer getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur( Integer idUtilisateur ) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo( String pseudo ) {
        this.pseudo = pseudo;
    }

    public String getNom() {
        return nom;
    }

    public void setNom( String nom ) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom( String prenom ) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone( String telephone ) {
        this.telephone = telephone;
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

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse( String motDePasse ) {
        this.motDePasse = motDePasse;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit( Integer credit ) {
        this.credit = credit;

    }

	@Override
	public String toString() {
		return "Utilisateur [idUtilisateur=" + idUtilisateur + ", pseudo=" + pseudo + ", nom=" + nom + ", prenom="
				+ prenom + ", email=" + email + ", telephone=" + telephone + ", rue=" + rue + ", codePostal="
				+ codePostal + ", ville=" + ville + ", motDePasse=" + motDePasse + ", credit=" + credit + "]";
	}

    
}
