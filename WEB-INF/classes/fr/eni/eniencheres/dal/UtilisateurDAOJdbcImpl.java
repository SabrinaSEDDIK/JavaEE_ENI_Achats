package fr.eni.eniencheres.dal;

// Rudy
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import fr.eni.eniencheres.bll.bo.Utilisateur;
import fr.eni.eniencheres.messages.BusinessException;

public class UtilisateurDAOJdbcImpl implements UtilisateurDAO {

    private static final String sqlCreerUtilisateur                         = "INSERT INTO UTILISATEURS (pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur, etat_compte) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private String              sqlSelect                                   = "SELECT * FROM Utilisateurs WHERE (pseudo LIKE ? OR email LIKE ?) AND mot_de_passe LIKE ? AND etat_compte = 1";
    private String              sqlSelectById                               = "SELECT * FROM Utilisateurs WHERE no_utilisateur=? AND etat_compte = 1";
    private static final String UPDATE                                      = "UPDATE UTILISATEURS SET pseudo = ?, nom=?, prenom=?, email=?, telephone=?, rue=?, code_postal=?, ville=?, mot_de_passe=? WHERE no_utilisateur=?;";

    // REQUETES A LA DESACTIVATION D'UN COMPTE
    // ETAPE 1 : etat_compte passe � 0
    private static final String UPDATE_ETAT_COMPTE                          = "UPDATE UTILISATEURS SET etat_compte = ? WHERE no_utilisateur = ?";
    // ETAPE 2 : suppression de ses ench�res en cours et si c'�tait le meilleur
    // ench�risseur, on d�bite le cr�dit de l'ench�risseur suivant
    private static final String ENCHERES_EN_COURS                           = "SELECT e.no_article AS \"idArticlesEncheres\" FROM ENCHERES e INNER JOIN ARTICLES_VENDUS a ON (e.no_article = a.no_article) WHERE GETDATE() BETWEEN date_debut_encheres AND date_fin_encheres GROUP BY e.no_article;";
    private static final String DEUX_MEILLEURS_ENCHERISSEURS                = "SELECT TOP(2) e.no_utilisateur, e.montant_enchere, credit FROM ENCHERES e INNER JOIN ARTICLES_VENDUS a ON (e.no_article = a.no_article) INNER JOIN UTILISATEURS u ON (e.no_utilisateur = u.no_utilisateur) WHERE e.no_article = ? AND GETDATE() BETWEEN date_debut_encheres AND date_fin_encheres ORDER BY date_enchere DESC";
    private static final String UPDATE_CREDIT_ENCHERISSEUR_SUIVANT          = "UPDATE UTILISATEURS SET credit = ? WHERE no_utilisateur = ?";
    private static final String UPDATE_PRIX_VENTE                           = "UPDATE ARTICLES_VENDUS SET prix_vente = ? WHERE no_article = ?";
    private static final String DELETE_ENCHERES_EN_COURS_UTILISATEUR        = "DELETE e FROM ENCHERES e INNER JOIN ARTICLES_VENDUS a ON (e.no_article = a.no_article) WHERE e.no_utilisateur = ? AND GETDATE() BETWEEN date_debut_encheres AND date_fin_encheres;";
    // ETAPE 3 : suppression de ses ventes en cours - csq :
    private static final String ID_VENTES_EN_COURS                          = "SELECT no_article FROM ARTICLES_VENDUS WHERE no_utilisateur = ? AND GETDATE() BETWEEN date_debut_encheres AND date_fin_encheres;";
    private static final String MEILLEURS_ENCHERISSEURS_SUR_VENTES_EN_COURS = "SELECT TOP (1) e.no_utilisateur, montant_enchere, credit FROM ENCHERES e INNER JOIN ARTICLES_VENDUS a ON (e.no_article = a.no_article) INNER JOIN UTILISATEURS u ON (u.no_utilisateur = e.no_utilisateur) WHERE a.no_utilisateur = ? AND e.no_article = ? ORDER BY date_enchere DESC";
    private static final String UPDATE_CREDIT_ENCHERISSEURS                 = "UPDATE UTILISATEURS SET credit = ? WHERE no_utilisateur = ?";
    private static final String SUPPRESSION_VENTES_EN_COURS                 = "DELETE FROM ARTICLES_VENDUS WHERE no_utilisateur = ? AND GETDATE() BETWEEN date_debut_encheres AND date_fin_encheres";
    private static final String SUPPRESSION_VENTES_NON_DEBUTEES             = "DELETE FROM ARTICLES_VENDUS WHERE no_utilisateur = ? AND GETDATE() < date_debut_encheres";

    // SQL pour fin d'une enchère
    private static final String sqlCreditementVendeur                       = "UPDATE UTILISATEURS SET credit = ? WHERE no_utilisateur = ?";
    private static final String sqlRecuperationEtatVenteArticle             = "SELECT etat_vente FROM ARTICLES_VENDUS WHERE no_article = ?";
    private static final String sqlFinEnchereArticleVendu                   = "UPDATE ARTICLES_VENDUS SET etat_vente = 1 WHERE no_article = ?";

    @Override
    public Utilisateur insert( Utilisateur utilisateur ) throws BusinessException {
        // Si l'utilisateur est null, on renvoie direct une erreur
        if ( utilisateur == null ) {
            BusinessException businessException = new BusinessException();
            businessException.ajouterErreur( CodesResultatDAL.INSERT_OBJET_NULL );
            throw businessException;
        }

        try ( Connection connexion = ConnectionProvider.getConnection();
                PreparedStatement requete = connexion.prepareStatement( sqlCreerUtilisateur,
                        PreparedStatement.RETURN_GENERATED_KEYS ) ) {
            int index = 1;
            requete.setString( index++, utilisateur.getPseudo() );
            requete.setString( index++, utilisateur.getNom() );
            requete.setString( index++, utilisateur.getPrenom() );
            requete.setString( index++, utilisateur.getEmail() );
            requete.setString( index++, utilisateur.getTelephone() );
            requete.setString( index++, utilisateur.getRue() );
            requete.setString( index++, utilisateur.getCodePostal() );
            requete.setString( index++, utilisateur.getVille() );
            requete.setString( index++, utilisateur.getMotDePasse() );
            requete.setInt( index++, 0 ); // credit
            requete.setInt( index++, 0 ); // admin
            requete.setInt( index++, 1 ); // etat_compte
            requete.executeUpdate();
            ResultSet rs = requete.getGeneratedKeys();
            if ( rs.next() ) {
                utilisateur.setIdUtilisateur( rs.getInt( 1 ) );
                utilisateur.setCredit( 0 );
            }

        } catch ( Exception e ) {
            e.printStackTrace();
            BusinessException businessException = new BusinessException();
            // Si le message d'erreur contient unique_pseudo ou unique_email
            // On renvoie une BusinessException car le compte existe déjà
            if ( e.getMessage().contains( "unique_pseudo" ) || e.getMessage().contains( "unique_email" ) ) {
                businessException.ajouterErreur( CodesResultatDAL.INSERT_OBJET_DEJA_EXISTANT );
            } else {
                businessException.ajouterErreur( CodesResultatDAL.INSERT_OBJET_ECHEC );
            }
            throw businessException;
        }
        return utilisateur;

    }

    @Override
    public void delete( Utilisateur utilisateur ) throws BusinessException {

        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        ResultSet rs4 = null;

        try ( Connection cnx = ConnectionProvider.getConnection();
                Statement stmt = cnx.createStatement();
                PreparedStatement pstmt2 = cnx.prepareStatement( DEUX_MEILLEURS_ENCHERISSEURS );
                PreparedStatement pstmt3 = cnx.prepareStatement( UPDATE_CREDIT_ENCHERISSEUR_SUIVANT );
                PreparedStatement pstmt4 = cnx.prepareStatement( DELETE_ENCHERES_EN_COURS_UTILISATEUR );
                PreparedStatement pstmt5 = cnx.prepareStatement( UPDATE_ETAT_COMPTE );
                PreparedStatement pstmt6 = cnx.prepareStatement( ID_VENTES_EN_COURS );
                PreparedStatement pstmt7 = cnx.prepareStatement( MEILLEURS_ENCHERISSEURS_SUR_VENTES_EN_COURS );
                PreparedStatement pstmt8 = cnx.prepareStatement( UPDATE_CREDIT_ENCHERISSEURS );
                PreparedStatement pstmt9 = cnx.prepareStatement( SUPPRESSION_VENTES_EN_COURS );
                PreparedStatement pstmt10 = cnx.prepareStatement( SUPPRESSION_VENTES_NON_DEBUTEES );
                PreparedStatement pstmt11 = cnx.prepareStatement( UPDATE_PRIX_VENTE ); ) {

            // ETAPE 1

            rs = stmt.executeQuery( ENCHERES_EN_COURS );

            Integer idArticle = null;
            Integer idEncherisseurSuivant = null;
            Integer miseEncherisseurSuivant = null;
            Integer creditEncherisseurSuivant = null;
            Integer nouveauCredit = null;
            Integer idMeilleurEncherisseur = null;
            while ( rs.next() ) {
                idArticle = rs.getInt( 1 );
                pstmt2.setInt( 1, idArticle );
                rs2 = pstmt2.executeQuery();
                if ( rs2.next() ) {
                    idMeilleurEncherisseur = rs2.getInt( 1 );
                    if ( idMeilleurEncherisseur == utilisateur.getIdUtilisateur() && rs2.next() ) {
                        idEncherisseurSuivant = rs2.getInt( 1 );
                        miseEncherisseurSuivant = rs2.getInt( 2 );
                        creditEncherisseurSuivant = rs2.getInt( 3 );
                        nouveauCredit = creditEncherisseurSuivant - miseEncherisseurSuivant;
                        pstmt3.setInt( 1, nouveauCredit );
                        pstmt3.setInt( 2, idEncherisseurSuivant );
                        pstmt3.execute();
                        pstmt11.setInt( 1, miseEncherisseurSuivant );
                        pstmt11.setInt( 2, idArticle );
                        pstmt11.execute();
                    }
                }
            }
            pstmt4.setInt( 1, utilisateur.getIdUtilisateur() );
            pstmt4.execute();
            pstmt5.setInt( 1, 0 );
            pstmt5.setInt( 2, utilisateur.getIdUtilisateur() );
            pstmt5.execute();

            // ETAPE 2
            Integer idArticle2 = null;
            Integer idEncherisseur = null;
            Integer montantMise = null;
            Integer credit = null;
            Integer nouveauCredit2 = null;

            pstmt6.setInt( 1, utilisateur.getIdUtilisateur() );
            rs3 = pstmt6.executeQuery();
            while ( rs3.next() ) {
                idArticle2 = rs3.getInt( 1 );
                pstmt7.setInt( 1, utilisateur.getIdUtilisateur() );
                pstmt7.setInt( 2, idArticle2 );
                rs4 = pstmt7.executeQuery();
                while ( rs4.next() ) {
                    idEncherisseur = rs4.getInt( 1 );
                    montantMise = rs4.getInt( 2 );
                    credit = rs4.getInt( 3 );
                    nouveauCredit2 = credit + montantMise;
                    pstmt8.setInt( 1, nouveauCredit2 );
                    pstmt8.setInt( 2, idEncherisseur );
                    pstmt8.execute();
                }
            }
            pstmt9.setInt( 1, utilisateur.getIdUtilisateur() );
            pstmt9.execute();
            pstmt10.setInt( 1, utilisateur.getIdUtilisateur() );
            pstmt10.execute();

        } catch ( SQLException sqle ) {
            sqle.printStackTrace();
            BusinessException businessException = new BusinessException();
            businessException.ajouterErreur( CodesResultatDAL.ERREUR_DELETE_UTILISATEUR );
            throw businessException;
        } finally {
            fermer( rs );
            fermer( rs2 );
            fermer( rs3 );
            fermer( rs4 );

        }
    }

    private void fermer( AutoCloseable ressource ) {
        if ( ressource != null ) {
            try {
                ressource.close();
            } catch ( Exception ex ) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public Utilisateur selectFrom( String pseudoMail, String motDePasse ) throws BusinessException {
        Utilisateur utilisateur = null;

        try ( Connection con = ConnectionProvider.getConnection();
                PreparedStatement stmt = con.prepareStatement( sqlSelect ) ) {
            // 1Prepare
            stmt.setString( 1, pseudoMail );
            stmt.setString( 2, pseudoMail );
            stmt.setString( 3, motDePasse );
            // 2 Executer via RS
            try ( ResultSet result = stmt.executeQuery() ) {

                // 3 Exploiter les données du RS
                if ( result.next() ) {
                    int id = result.getInt( "no_utilisateur" );
                    String pseudo = result.getString( "pseudo" );
                    String nom = result.getString( "nom" );
                    String prenom = result.getString( "prenom" );
                    String email = result.getString( "email" );
                    String tel = result.getString( "telephone" );
                    String rue = result.getString( "rue" );
                    String codePostal = result.getString( "code_postal" );
                    String ville = result.getString( "ville" );
                    String motPasse = result.getString( "mot_de_passe" );
                    int credit = result.getInt( "credit" );

                    utilisateur = new Utilisateur( id, pseudo, nom, prenom, email, tel, rue, codePostal, ville,
                            motPasse, credit );

                }

                return utilisateur;

            } catch ( SQLException ex ) {
                ex.printStackTrace();
                BusinessException businessException = new BusinessException();
                businessException.ajouterErreur( CodesResultatDAL.INSERT_OBJET_NULL );
                throw businessException;

            }

        } catch ( SQLException ex ) {
            ex.printStackTrace();
            BusinessException businessException = new BusinessException();
            businessException.ajouterErreur( CodesResultatDAL.INSERT_OBJET_NULL );
            throw businessException;
        }
    }

    public Utilisateur selectById( int id ) throws BusinessException {
        Utilisateur utilisateur = null;

        try ( Connection con = ConnectionProvider.getConnection();
                PreparedStatement stmt = con.prepareStatement( sqlSelectById ) ) {
            // 1Prepare
            stmt.setInt( 1, id );

            // 2 Executer via RS
            try ( ResultSet result = stmt.executeQuery() ) {

                // 3 Exploiter les données du RS
                if ( result.next() ) {
                    int idUtilisateur = result.getInt( "no_utilisateur" );
                    String pseudoUtilisateur = result.getString( "pseudo" );
                    String nom = result.getString( "nom" );
                    String prenom = result.getString( "prenom" );
                    String email = result.getString( "email" );
                    String tel = result.getString( "telephone" );
                    String rue = result.getString( "rue" );
                    String codePostal = result.getString( "code_postal" );
                    String ville = result.getString( "ville" );
                    String motPasse = result.getString( "mot_de_passe" );
                    int credit = result.getInt( "credit" );

                    utilisateur = new Utilisateur( idUtilisateur, pseudoUtilisateur, nom, prenom, email, tel, rue,
                            codePostal, ville, motPasse, credit );
                }

                return utilisateur;

            } catch ( SQLException ex ) {
                ex.printStackTrace();
                BusinessException businessException = new BusinessException();
                businessException.ajouterErreur( CodesResultatDAL.INSERT_OBJET_NULL );
                throw businessException;

            }

        } catch ( SQLException ex ) {
            ex.printStackTrace();
            BusinessException businessException = new BusinessException();
            businessException.ajouterErreur( CodesResultatDAL.INSERT_OBJET_NULL );
            throw businessException;
        }
    }

    @Override
    public Utilisateur update( Utilisateur utilisateur ) throws BusinessException {
        try ( Connection connexion = ConnectionProvider.getConnection();
                PreparedStatement pstmt = connexion.prepareStatement( UPDATE ) ) {

            pstmt.setString( 1, utilisateur.getPseudo() );
            pstmt.setString( 2, utilisateur.getNom() );
            pstmt.setString( 3, utilisateur.getPrenom() );
            pstmt.setString( 4, utilisateur.getEmail() );
            pstmt.setString( 5, utilisateur.getTelephone() );
            pstmt.setString( 6, utilisateur.getRue() );
            pstmt.setString( 7, utilisateur.getCodePostal() );
            pstmt.setString( 8, utilisateur.getVille() );
            pstmt.setString( 9, utilisateur.getMotDePasse() );
            pstmt.setInt( 10, utilisateur.getIdUtilisateur() );
            pstmt.execute();

        } catch ( SQLException sqle ) {
            sqle.printStackTrace();
            BusinessException be = new BusinessException();
            be.ajouterErreur( CodesResultatDAL.ERREUR_UPDATE_UTILISATEUR );
            throw be;
        }

        return utilisateur;
    }

    /**
     * Méthode permettant de créditer un utilisateur à la fin d'une vente En
     * deux étapes : 1) Récupération du crédit de l'utilisateur 2) Mise à jour
     * de son crédit
     * 
     * @param idUtilisateur
     *            : id de l'utilisateur à créditer
     * @param prixVente
     *            : prix de vente de l'objet que l'on doit rajouter au crédit de
     *            l'utilisateur
     */
    @Override
    public void crediterUtilisateur( Integer idUtilisateur, Integer idArticle, Integer prixVente )
            throws BusinessException {
        try ( Connection connexion = ConnectionProvider.getConnection();
                PreparedStatement requeteSelectEtatVente = connexion
                        .prepareStatement( sqlRecuperationEtatVenteArticle );
                PreparedStatement requeteSelect = connexion.prepareStatement( sqlSelectById );
                PreparedStatement requeteUpdate = connexion.prepareStatement( sqlCreditementVendeur );
                PreparedStatement requeteUpdateArticleVendu = connexion
                        .prepareStatement( sqlFinEnchereArticleVendu ) ) {
            // Désactivation de l'autoCommit
            connexion.setAutoCommit( false );

            // Première étape : vérifier que l'article n'a pas déjà été vendu
            int etatVente = 0;

            requeteSelectEtatVente.setInt( 1, idArticle );
            try ( ResultSet rs = requeteSelectEtatVente.executeQuery() ) {
                // Si on a un article avec et identifiant
                if ( rs.next() ) {
                    // On récupère son etat de vente
                    etatVente = rs.getInt( "etat_vente" );

                    // S'il n'est pas déjà vendu
                    if ( etatVente == 0 ) {
                        // Ajout du paramètre dans la requête
                        requeteSelect.setInt( 1, idUtilisateur );
                        int nouveauCreditUtilisateur = 0;
                        // On récupère les infos du vendeur
                        try ( ResultSet rsInfoVendeur = requeteSelect.executeQuery() ) {
                            // Si on récupère bien l'utilisateur
                            if ( rsInfoVendeur.next() ) {
                                // On calcule son nouveau credit
                                nouveauCreditUtilisateur = rsInfoVendeur.getInt( "credit" ) + prixVente;
                                // On rajoute dans la requête le nouveau crédit
                                // du vendeur
                                requeteUpdate.setInt( 1, nouveauCreditUtilisateur );
                                requeteUpdate.setInt( 2, idUtilisateur );

                                // On met l'etat de l'article sur vendu
                                requeteUpdateArticleVendu.setInt( 1, idArticle );

                                // Et on effectue les deux mises à jour
                                int resultatUpdate = requeteUpdate.executeUpdate();
                                int resultatUpdateArticleVendu = requeteUpdateArticleVendu.executeUpdate();
                                if ( resultatUpdate != 1 || resultatUpdateArticleVendu != 1 ) {
                                    // Si plusieurs ligne on été modifées, on
                                    // rollback
                                    connexion.rollback();
                                } else {
                                    // Si une seule ligne a été modifiée, on
                                    // peut commit
                                    connexion.commit();
                                }
                            } else {
                                BusinessException businessException = new BusinessException();
                                businessException.ajouterErreur( CodesResultatDAL.ERROR_RECUPERATION_UTILISATEUR );
                                throw businessException;
                            }
                        }
                    }
                    // S'il est déjà vendu
                    else {
                        // On renvoie une erreur, objet déjà vendu
                        BusinessException businessException = new BusinessException();
                        businessException.ajouterErreur( CodesResultatDAL.ERROR_ARTICLE_DEJA_VENDU );
                        throw businessException;
                    }
                } else {
                    // On lance un erreur, l'article n'existe pas
                    BusinessException businessException = new BusinessException();
                    businessException.ajouterErreur( CodesResultatDAL.SELECTBY_ID_ARTICLE_INEXISTANT );
                    throw businessException;
                }
            }

        } catch ( SQLException e ) {
            e.printStackTrace();
            BusinessException businessException = new BusinessException();
            businessException.ajouterErreur( CodesResultatDAL.ERROR_BASE_DE_DONNEES );
            throw businessException;
        }
    }
}
