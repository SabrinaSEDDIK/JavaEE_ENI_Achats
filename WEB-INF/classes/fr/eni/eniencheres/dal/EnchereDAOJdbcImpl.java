package fr.eni.eniencheres.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import fr.eni.eniencheres.bll.bo.Article;
import fr.eni.eniencheres.bll.bo.Enchere;
import fr.eni.eniencheres.messages.BusinessException;

public class EnchereDAOJdbcImpl implements EnchereDAO {

    private static final String selectById                   = "SELECT a.no_article, e.no_utilisateur AS acheteur, a.nom_article, description, c.no_categorie, c.libelle, e.montant_enchere, a.prix_initial, a.date_debut_encheres, a.date_fin_encheres, r.rue, r.code_postal, r.ville, a.no_utilisateur, u.pseudo FROM ARTICLES_VENDUS AS a "
            + "                                                INNER JOIN ENCHERES AS e ON a.no_article = e.no_article "
            + "                                                INNER JOIN CATEGORIES AS c ON a.no_categorie = c.no_categorie "
            + "                                                INNER JOIN RETRAITS AS r ON a.no_article = r.no_article "
            + "                                                INNER JOIN UTILISATEURS AS u ON a.no_utilisateur = u.no_utilisateur "
            + "                                                    WHERE a.no_article = ? "
            + "                                                        ORDER BY montant_enchere DESC";

    private static final String selectByIdNoEnchere          = "SELECT a.no_article, a.nom_article, description, c.no_categorie, c.libelle, a.prix_initial, a.date_debut_encheres, a.date_fin_encheres, r.rue, r.code_postal, r.ville, a.no_utilisateur, u.pseudo FROM ARTICLES_VENDUS AS a "
            + "                                                INNER JOIN CATEGORIES AS c ON a.no_categorie = c.no_categorie"
            + "                                                INNER JOIN RETRAITS AS r ON a.no_article = r.no_article"
            + "                                                INNER JOIN UTILISATEURS AS u ON a.no_utilisateur = u.no_utilisateur"
            + "                                                    WHERE a.no_article = ?";

    private static final String sqlAjouterEnchere            = "INSERT INTO ENCHERES VALUES(?, ?, ?, ?)";
    private static final String sqlUpdateEnchere             = "UPDATE ENCHERES SET montant_enchere = ?, date_enchere = ? WHERE (no_utilisateur = ? AND no_article = ? )";
    private static final String sqlUpdateArticleApresEnchere = "UPDATE ARTICLES_VENDUS SET prix_vente = ? WHERE no_article = ?";
    private static final String sqlSelectUtilisateurById     = "SELECT pseudo, credit from UTILISATEURS WHERE no_utilisateur = ?";
    private static final String sqlMajCreditEncherisseur     = "UPDATE UTILISATEURS SET credit = ? WHERE no_utilisateur = ?";

    private static final String sqlListeArticle              = "SELECT * FROM ARTICLES_VENDUS";
    private static final String sqlUpdateVenteArticle        = "UPDATE ARTICLES_VENDUS SET prix_vente = ? WHERE no_article = ?";

    @Override
    public void insert( Enchere enchere ) throws BusinessException {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete( Enchere enchere ) throws BusinessException {
        // TODO Auto-generated method stub

    }

    @Override
    public Enchere selectById( int id ) throws BusinessException {
        // Si l'id envoyé est égale à 0, on renvoie direct une erreur
        if ( id == 0 ) {
            BusinessException businessException = new BusinessException();
            businessException.ajouterErreur( CodesResultatDAL.SELECTBY_ENCHERE_ID_NULL );
            throw businessException;
        }
        Enchere enchereDemandee = null;
        try ( Connection connexion = ConnectionProvider.getConnection();
                PreparedStatement requete = connexion.prepareStatement( selectById ) ) {
            requete.setInt( 1, id );

            try ( ResultSet rs = requete.executeQuery() ) {
                // Pour récupérer l'enchère la plus élevé pour cet article
                // J'ai ORDER BY montant_enchere DESC dans ma requête
                // Et enfin, en faisant if ( rs.next() )
                // Je ne récupère que le premier élément de la requête
                // Qui se trouve être l'enchère avec le montant le plus élevé
                if ( rs.next() ) {
                    Article articleRecupere = new Article( rs.getInt( "no_article" ), rs.getString( "nom_article" ),
                            rs.getString( "description" ),
                            LocalDate.parse( rs.getString( "date_debut_encheres" ) ),
                            LocalDate.parse( rs.getString( "date_fin_encheres" ) ),
                            rs.getInt( "prix_initial" ), rs.getInt( "montant_enchere" ), rs.getInt( "no_categorie" ),
                            rs.getString( "pseudo" ) );
                    String categorie = rs.getString( "libelle" );
                    int idVendeur = rs.getInt( "no_utilisateur" );
                    int idAcheteur = rs.getInt( "acheteur" );
                    String rue = rs.getString( "rue" );
                    String codePostal = rs.getString( "code_postal" );
                    String ville = rs.getString( "ville" );

                    enchereDemandee = new Enchere( articleRecupere, categorie, idVendeur, idAcheteur, rue, codePostal,
                            ville );
                }

            }
            // Si après la requête, l'enchereDemandee est null
            // C'est qu'il n'y a pas eu d'enchère réalisée sur l'article
            // Il faut donc aller chercher les infos ailleurs
            if ( enchereDemandee == null ) {
                try ( Connection connexionNoEnchere = ConnectionProvider.getConnection();
                        PreparedStatement requeteNoEnchere = connexion.prepareStatement( selectByIdNoEnchere ) ) {
                    requeteNoEnchere.setInt( 1, id );

                    try ( ResultSet rs = requeteNoEnchere.executeQuery() ) {
                        // Dans cette requête, un seul résultat est possible
                        // S'il y en a un, on fait donc un
                        if ( rs.next() ) {
                            Article articleRecupere = new Article( rs.getInt( "no_article" ),
                                    rs.getString( "nom_article" ),
                                    rs.getString( "description" ),
                                    LocalDate.parse( rs.getString( "date_debut_encheres" ) ),
                                    LocalDate.parse( rs.getString( "date_fin_encheres" ) ),
                                    rs.getInt( "prix_initial" ), 0, rs.getInt( "no_categorie" ),
                                    rs.getString( "pseudo" ) );

                            String categorie = rs.getString( "libelle" );
                            int idVendeur = rs.getInt( "no_utilisateur" );
                            String rue = rs.getString( "rue" );
                            String codePostal = rs.getString( "code_postal" );
                            String ville = rs.getString( "ville" );

                            enchereDemandee = new Enchere( articleRecupere, categorie, idVendeur, rue,
                                    codePostal,
                                    ville );

                        }
                        // Si après cette dernière requête, enchereDemandee
                        // est encore null, c'est que l'article n'existe pas
                        // On renvoie donc une erreur article inexistant
                        if ( enchereDemandee == null ) {
                            BusinessException businessException = new BusinessException();
                            businessException.ajouterErreur( CodesResultatDAL.SELECTBY_ID_ARTICLE_INEXISTANT );
                            throw businessException;
                        }
                    }
                }
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
            BusinessException businessException = new BusinessException();
            businessException.ajouterErreur( CodesResultatDAL.ERROR_RECUPERATION_ENCHERE );
        }
        return enchereDemandee;
    }

    @Override
    public List<Enchere> selectAll( String motRecherche, String categories ) throws BusinessException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean encherir( int idArticle, int idEncherisseur, int valeurEnchere ) throws BusinessException {
        int resultatInsert = 0;
        int resultatInsertArticle = 0;
        int resultatUpdateCreditAncienEncherisseur = 0;
        int resultatUpdateCreditNouvelEncherisseur = 0;

        try ( Connection connexion = ConnectionProvider.getConnection();
                PreparedStatement requete = connexion.prepareStatement( sqlAjouterEnchere );
                PreparedStatement requeteSelectUtilisateur = connexion.prepareStatement( sqlSelectUtilisateurById );
                PreparedStatement requeteUpdateCredit = connexion.prepareStatement( sqlMajCreditEncherisseur );
                PreparedStatement requeteUpdateArticle = connexion.prepareStatement( sqlUpdateArticleApresEnchere ) ) {
            // Désactivation de l'autoCommit
            connexion.setAutoCommit( false );

            // On récupère l'ancienne enchèreMax pour avoir l'id de l'ancien
            // enchérisseur
            Enchere enchereMax = selectById( idArticle );

            requete.setInt( 1, idEncherisseur );
            requete.setInt( 2, idArticle );
            requete.setString( 3, LocalDateTime.now().toString() );
            requete.setInt( 4, valeurEnchere );

            requeteUpdateArticle.setInt( 1, valeurEnchere );
            requeteUpdateArticle.setInt( 2, idArticle );

            resultatInsert = requete.executeUpdate();
            resultatInsertArticle = requeteUpdateArticle.executeUpdate();
            // NOUVEAU CODE
            if ( resultatInsert != 1 && resultatInsertArticle != 1 ) {
                // Si les update ne se sont pas passés comme prévu, on rollback
                connexion.rollback();
                // Et on lance une businessException
                BusinessException businessException = new BusinessException();
                businessException.ajouterErreur( CodesResultatDAL.ERROR_PLACEMENT_ENCHERE );
                throw businessException;
            }
            // FIN NOUVEAU CODE

            // Après avoir réalisé l'enchère, on va créditer les crédits pour le
            // nouvel enchérisseur. Et on va le rembourser.
            // Si il existe bien sur
            // On vérifie ca en regardant si prixVente est != 0
            // Si oui c'est qu'une enchère a déjà été placée
            if ( enchereMax.getArticle().getPrixVente() != 0 ) {
                // On récupère l'ancien enchérisseur
                requeteSelectUtilisateur.setInt( 1, enchereMax.getIdAcheteur() );
                try ( ResultSet rs = requeteSelectUtilisateur.executeQuery() ) {
                    if ( rs.next() ) {
                        int nouveauCredit = rs.getInt( "credit" ) + enchereMax.getArticle().getPrixVente();
                        requeteUpdateCredit.setInt( 1, nouveauCredit );
                        requeteUpdateCredit.setInt( 2, enchereMax.getIdAcheteur() );

                        resultatUpdateCreditAncienEncherisseur = requeteUpdateCredit.executeUpdate();
                        if ( resultatUpdateCreditAncienEncherisseur != 1 ) {
                            // Si l'update ne s'est pas bien passé, on rollback
                            connexion.rollback();
                            // Et on lance une businessException
                            BusinessException businessException = new BusinessException();
                            businessException.ajouterErreur( CodesResultatDAL.ERROR_PLACEMENT_ENCHERE );
                            throw businessException;
                        }
                    } else {
                        // Si pas de résultat, c'est une erreur, on rollback
                        connexion.rollback();
                        // Et on lance une businessException
                        BusinessException businessException = new BusinessException();
                        businessException.ajouterErreur( CodesResultatDAL.ERROR_PLACEMENT_ENCHERE );
                        throw businessException;
                    }
                }
            } else {
                // Si c'est la première enchère, on valide le fait d'avoir mis à
                // jour le crédit de l'ancien enchérisseur
                resultatUpdateCreditAncienEncherisseur = 1;
            }

            // Maintenant, on met à jour le crédit du nouvel encherisseur
            requeteSelectUtilisateur.setInt( 1, idEncherisseur );
            try ( ResultSet rs = requeteSelectUtilisateur.executeQuery() ) {
                if ( rs.next() ) {
                    if ( rs.getInt( "credit" ) - valeurEnchere > 0 ) {
                        int nouveauCredit = rs.getInt( "credit" ) - valeurEnchere;
                        requeteUpdateCredit.setInt( 1, nouveauCredit );
                        requeteUpdateCredit.setInt( 2, idEncherisseur );

                        resultatUpdateCreditNouvelEncherisseur = requeteUpdateCredit.executeUpdate();
                        if ( resultatUpdateCreditNouvelEncherisseur != 1 ) {
                            // Si l'update ne s'est pas bien passé, on rollback
                            connexion.rollback();
                            // Et on lance une businessException
                            BusinessException businessException = new BusinessException();
                            businessException.ajouterErreur( CodesResultatDAL.ERROR_PLACEMENT_ENCHERE );
                            throw businessException;
                        }
                    } else {
                        // Si l'utilisateur n'a pas assez de crédit, c'est une
                        // erreur, on rollback
                        connexion.rollback();
                        // Et on lance une exception
                        BusinessException businessException = new BusinessException();
                        businessException.ajouterErreur( CodesResultatDAL.ERROR_CREDIT_INSUFFISANT );
                        throw businessException;
                    }
                } else {
                    // Si pas de résultat, c'est une erreur, on rollback
                    connexion.rollback();
                    // Et on lance une businessException
                    BusinessException businessException = new BusinessException();
                    businessException.ajouterErreur( CodesResultatDAL.ERROR_PLACEMENT_ENCHERE );
                    throw businessException;
                }
            }
            connexion.commit();
        } catch ( SQLException e ) {
            // Si le message d'erreur contient la chaine enchere_pk
            // C'est que la personne a déjà placé une enchère sur l'article
            // Il faut alors mettre à jour le montant de son enchère
            if ( e.getMessage().contains( "enchere_pk" ) ) {
                try ( Connection connexion = ConnectionProvider.getConnection();
                        PreparedStatement requete = connexion.prepareStatement( sqlUpdateEnchere );
                        PreparedStatement requeteSelectUtilisateur = connexion
                                .prepareStatement( sqlSelectUtilisateurById );
                        PreparedStatement requeteUpdateCredit = connexion.prepareStatement( sqlMajCreditEncherisseur );
                        PreparedStatement requeteUpdateArticle = connexion
                                .prepareStatement( sqlUpdateArticleApresEnchere ) ) {
                    // Désactivation de l'autoCommit
                    connexion.setAutoCommit( false );

                    // On récupère l'ancienne enchèreMax pour avoir l'id de
                    // l'ancien enchérisseur
                    Enchere enchereMax = selectById( idArticle );

                    requete.setInt( 1, valeurEnchere );
                    requete.setString( 2, LocalDateTime.now().toString() );
                    requete.setInt( 3, idEncherisseur );
                    requete.setInt( 4, idArticle );

                    requeteUpdateArticle.setInt( 1, valeurEnchere );
                    requeteUpdateArticle.setInt( 2, idArticle );

                    resultatInsert = requete.executeUpdate();
                    resultatInsertArticle = requeteUpdateArticle.executeUpdate();

                    // Après avoir réalisé l'enchère, on va créditer les crédits
                    // pour le nouvel enchérisseur
                    // Et on va rembourser l'ancien encherisseur.
                    requeteSelectUtilisateur.setInt( 1, enchereMax.getIdAcheteur() );
                    try ( ResultSet rs = requeteSelectUtilisateur.executeQuery() ) {
                        if ( rs.next() ) {
                            // On calcule le nouveau crédit
                            int nouveauCredit = rs.getInt( "credit" ) + enchereMax.getArticle().getPrixVente();
                            // Et on l'envoie dans la requête
                            requeteUpdateCredit.setInt( 1, nouveauCredit );
                            requeteUpdateCredit.setInt( 2, enchereMax.getIdAcheteur() );

                            resultatUpdateCreditAncienEncherisseur = requeteUpdateCredit.executeUpdate();
                            if ( resultatUpdateCreditAncienEncherisseur != 1 ) {
                                // Si l'update ne s'est pas bien passé, on
                                // rollback
                                connexion.rollback();
                                // Et on lance une businessException
                                BusinessException businessException = new BusinessException();
                                businessException.ajouterErreur( CodesResultatDAL.ERROR_PLACEMENT_ENCHERE );
                                throw businessException;
                            }
                        } else {
                            // Si pas de résultat, c'est une erreur, on rollback
                            connexion.rollback();
                            // Et on lance une businessException
                            BusinessException businessException = new BusinessException();
                            businessException.ajouterErreur( CodesResultatDAL.ERROR_PLACEMENT_ENCHERE );
                            throw businessException;
                        }
                    }
                    // Maintenant, on met à jour le crédit du nouvel
                    // encherisseur
                    requeteSelectUtilisateur.setInt( 1, idEncherisseur );
                    try ( ResultSet rs = requeteSelectUtilisateur.executeQuery() ) {
                        if ( rs.next() ) {
                            if ( rs.getInt( "credit" ) - valeurEnchere > 0 ) {
                                // On calcule le nouveau crédit
                                int nouveauCredit = rs.getInt( "credit" ) - valeurEnchere;
                                // Et on l'envoie dans la requête
                                requeteUpdateCredit.setInt( 1, nouveauCredit );
                                requeteUpdateCredit.setInt( 2, idEncherisseur );

                                resultatUpdateCreditNouvelEncherisseur = requeteUpdateCredit.executeUpdate();
                                if ( resultatUpdateCreditNouvelEncherisseur != 1 ) {
                                    // Si l'update ne s'est pas bien passé, on
                                    // rollback
                                    connexion.rollback();
                                    // Et on lance une businessException
                                    BusinessException businessException = new BusinessException();
                                    businessException.ajouterErreur( CodesResultatDAL.ERROR_PLACEMENT_ENCHERE );
                                    throw businessException;
                                }
                            } else {
                                // Si l'utilisateur n'a pas assez de crédit,
                                // c'est une
                                // erreur, on rollback
                                connexion.rollback();
                                // Et on lance une exception
                                BusinessException businessException = new BusinessException();
                                businessException.ajouterErreur( CodesResultatDAL.ERROR_CREDIT_INSUFFISANT );
                                throw businessException;
                            }
                        } else {
                            // Si pas de résultat, c'est une erreur, on rollback
                            connexion.rollback();
                            // Et on lance une businessException
                            BusinessException businessException = new BusinessException();
                            businessException.ajouterErreur( CodesResultatDAL.ERROR_PLACEMENT_ENCHERE );
                            throw businessException;
                        }
                    }
                    connexion.commit();
                } catch ( SQLException e1 ) {
                    BusinessException businessException = new BusinessException();
                    businessException.ajouterErreur( CodesResultatDAL.ERROR_PLACEMENT_ENCHERE );
                    throw businessException;
                }
            } else {
                e.printStackTrace();
            }
        }
        // On vérifie que tout s'est bien passé, et on retourne l'état de la
        // requête
        if ( resultatInsert == 1 && resultatInsertArticle == 1 && resultatUpdateCreditAncienEncherisseur == 1
                && resultatUpdateCreditNouvelEncherisseur == 1 ) {
            return true;
        } else {
            return false;
        }
    }

    // Méthode qui permettait de mettre à jour la table article vendus lorsqu'on
    // utilisait le filtre pour vérifier fin enchère
    @Override
    public void majEnchere() throws BusinessException {

        try ( Connection connexion = ConnectionProvider.getConnection();
                PreparedStatement requete = connexion.prepareStatement( sqlListeArticle );
                ResultSet rs = requete.executeQuery() ) {
            LocalDate dateActuel = LocalDate.now();
            while ( rs.next() ) {
                // Si la date de fin d'enchères est passée
                // On met à jour la prix de vente
                if ( LocalDate.parse( rs.getString( "date_fin_encheres" ) ).isBefore( dateActuel ) ) {
                    // Il faut donc récupérer l'enchère max pour cet article
                    Enchere enchereMax = selectById( rs.getInt( "no_article" ) );

                    // Et maintenant, il faut update la table ARTICLES_VENDUS
                    // Pour mettre le prix de vente, ce qui indique que
                    // l'enchère est terminée
                    try ( PreparedStatement requeteUpdateVente = connexion.prepareStatement( sqlUpdateVenteArticle ) ) {
                        requeteUpdateVente.setInt( 1, enchereMax.getArticle().getPrixVente() );
                        requeteUpdateVente.setInt( 2, rs.getInt( "no_article" ) );

                        requeteUpdateVente.executeUpdate();
                    }
                }
            }
        } catch ( SQLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
