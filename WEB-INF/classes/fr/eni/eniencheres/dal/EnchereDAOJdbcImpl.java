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
        // Si l'id envoy?? est ??gale ?? 0, on renvoie direct une erreur
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
                // Pour r??cup??rer l'ench??re la plus ??lev?? pour cet article
                // J'ai ORDER BY montant_enchere DESC dans ma requ??te
                // Et enfin, en faisant if ( rs.next() )
                // Je ne r??cup??re que le premier ??l??ment de la requ??te
                // Qui se trouve ??tre l'ench??re avec le montant le plus ??lev??
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
            // Si apr??s la requ??te, l'enchereDemandee est null
            // C'est qu'il n'y a pas eu d'ench??re r??alis??e sur l'article
            // Il faut donc aller chercher les infos ailleurs
            if ( enchereDemandee == null ) {
                try ( Connection connexionNoEnchere = ConnectionProvider.getConnection();
                        PreparedStatement requeteNoEnchere = connexion.prepareStatement( selectByIdNoEnchere ) ) {
                    requeteNoEnchere.setInt( 1, id );

                    try ( ResultSet rs = requeteNoEnchere.executeQuery() ) {
                        // Dans cette requ??te, un seul r??sultat est possible
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
                        // Si apr??s cette derni??re requ??te, enchereDemandee
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
            // D??sactivation de l'autoCommit
            connexion.setAutoCommit( false );

            // On r??cup??re l'ancienne ench??reMax pour avoir l'id de l'ancien
            // ench??risseur
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
                // Si les update ne se sont pas pass??s comme pr??vu, on rollback
                connexion.rollback();
                // Et on lance une businessException
                BusinessException businessException = new BusinessException();
                businessException.ajouterErreur( CodesResultatDAL.ERROR_PLACEMENT_ENCHERE );
                throw businessException;
            }
            // FIN NOUVEAU CODE

            // Apr??s avoir r??alis?? l'ench??re, on va cr??diter les cr??dits pour le
            // nouvel ench??risseur. Et on va le rembourser.
            // Si il existe bien sur
            // On v??rifie ca en regardant si prixVente est != 0
            // Si oui c'est qu'une ench??re a d??j?? ??t?? plac??e
            if ( enchereMax.getArticle().getPrixVente() != 0 ) {
                // On r??cup??re l'ancien ench??risseur
                requeteSelectUtilisateur.setInt( 1, enchereMax.getIdAcheteur() );
                try ( ResultSet rs = requeteSelectUtilisateur.executeQuery() ) {
                    if ( rs.next() ) {
                        int nouveauCredit = rs.getInt( "credit" ) + enchereMax.getArticle().getPrixVente();
                        requeteUpdateCredit.setInt( 1, nouveauCredit );
                        requeteUpdateCredit.setInt( 2, enchereMax.getIdAcheteur() );

                        resultatUpdateCreditAncienEncherisseur = requeteUpdateCredit.executeUpdate();
                        if ( resultatUpdateCreditAncienEncherisseur != 1 ) {
                            // Si l'update ne s'est pas bien pass??, on rollback
                            connexion.rollback();
                            // Et on lance une businessException
                            BusinessException businessException = new BusinessException();
                            businessException.ajouterErreur( CodesResultatDAL.ERROR_PLACEMENT_ENCHERE );
                            throw businessException;
                        }
                    } else {
                        // Si pas de r??sultat, c'est une erreur, on rollback
                        connexion.rollback();
                        // Et on lance une businessException
                        BusinessException businessException = new BusinessException();
                        businessException.ajouterErreur( CodesResultatDAL.ERROR_PLACEMENT_ENCHERE );
                        throw businessException;
                    }
                }
            } else {
                // Si c'est la premi??re ench??re, on valide le fait d'avoir mis ??
                // jour le cr??dit de l'ancien ench??risseur
                resultatUpdateCreditAncienEncherisseur = 1;
            }

            // Maintenant, on met ?? jour le cr??dit du nouvel encherisseur
            requeteSelectUtilisateur.setInt( 1, idEncherisseur );
            try ( ResultSet rs = requeteSelectUtilisateur.executeQuery() ) {
                if ( rs.next() ) {
                    if ( rs.getInt( "credit" ) - valeurEnchere > 0 ) {
                        int nouveauCredit = rs.getInt( "credit" ) - valeurEnchere;
                        requeteUpdateCredit.setInt( 1, nouveauCredit );
                        requeteUpdateCredit.setInt( 2, idEncherisseur );

                        resultatUpdateCreditNouvelEncherisseur = requeteUpdateCredit.executeUpdate();
                        if ( resultatUpdateCreditNouvelEncherisseur != 1 ) {
                            // Si l'update ne s'est pas bien pass??, on rollback
                            connexion.rollback();
                            // Et on lance une businessException
                            BusinessException businessException = new BusinessException();
                            businessException.ajouterErreur( CodesResultatDAL.ERROR_PLACEMENT_ENCHERE );
                            throw businessException;
                        }
                    } else {
                        // Si l'utilisateur n'a pas assez de cr??dit, c'est une
                        // erreur, on rollback
                        connexion.rollback();
                        // Et on lance une exception
                        BusinessException businessException = new BusinessException();
                        businessException.ajouterErreur( CodesResultatDAL.ERROR_CREDIT_INSUFFISANT );
                        throw businessException;
                    }
                } else {
                    // Si pas de r??sultat, c'est une erreur, on rollback
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
            // C'est que la personne a d??j?? plac?? une ench??re sur l'article
            // Il faut alors mettre ?? jour le montant de son ench??re
            if ( e.getMessage().contains( "enchere_pk" ) ) {
                try ( Connection connexion = ConnectionProvider.getConnection();
                        PreparedStatement requete = connexion.prepareStatement( sqlUpdateEnchere );
                        PreparedStatement requeteSelectUtilisateur = connexion
                                .prepareStatement( sqlSelectUtilisateurById );
                        PreparedStatement requeteUpdateCredit = connexion.prepareStatement( sqlMajCreditEncherisseur );
                        PreparedStatement requeteUpdateArticle = connexion
                                .prepareStatement( sqlUpdateArticleApresEnchere ) ) {
                    // D??sactivation de l'autoCommit
                    connexion.setAutoCommit( false );

                    // On r??cup??re l'ancienne ench??reMax pour avoir l'id de
                    // l'ancien ench??risseur
                    Enchere enchereMax = selectById( idArticle );

                    requete.setInt( 1, valeurEnchere );
                    requete.setString( 2, LocalDateTime.now().toString() );
                    requete.setInt( 3, idEncherisseur );
                    requete.setInt( 4, idArticle );

                    requeteUpdateArticle.setInt( 1, valeurEnchere );
                    requeteUpdateArticle.setInt( 2, idArticle );

                    resultatInsert = requete.executeUpdate();
                    resultatInsertArticle = requeteUpdateArticle.executeUpdate();

                    // Apr??s avoir r??alis?? l'ench??re, on va cr??diter les cr??dits
                    // pour le nouvel ench??risseur
                    // Et on va rembourser l'ancien encherisseur.
                    requeteSelectUtilisateur.setInt( 1, enchereMax.getIdAcheteur() );
                    try ( ResultSet rs = requeteSelectUtilisateur.executeQuery() ) {
                        if ( rs.next() ) {
                            // On calcule le nouveau cr??dit
                            int nouveauCredit = rs.getInt( "credit" ) + enchereMax.getArticle().getPrixVente();
                            // Et on l'envoie dans la requ??te
                            requeteUpdateCredit.setInt( 1, nouveauCredit );
                            requeteUpdateCredit.setInt( 2, enchereMax.getIdAcheteur() );

                            resultatUpdateCreditAncienEncherisseur = requeteUpdateCredit.executeUpdate();
                            if ( resultatUpdateCreditAncienEncherisseur != 1 ) {
                                // Si l'update ne s'est pas bien pass??, on
                                // rollback
                                connexion.rollback();
                                // Et on lance une businessException
                                BusinessException businessException = new BusinessException();
                                businessException.ajouterErreur( CodesResultatDAL.ERROR_PLACEMENT_ENCHERE );
                                throw businessException;
                            }
                        } else {
                            // Si pas de r??sultat, c'est une erreur, on rollback
                            connexion.rollback();
                            // Et on lance une businessException
                            BusinessException businessException = new BusinessException();
                            businessException.ajouterErreur( CodesResultatDAL.ERROR_PLACEMENT_ENCHERE );
                            throw businessException;
                        }
                    }
                    // Maintenant, on met ?? jour le cr??dit du nouvel
                    // encherisseur
                    requeteSelectUtilisateur.setInt( 1, idEncherisseur );
                    try ( ResultSet rs = requeteSelectUtilisateur.executeQuery() ) {
                        if ( rs.next() ) {
                            if ( rs.getInt( "credit" ) - valeurEnchere > 0 ) {
                                // On calcule le nouveau cr??dit
                                int nouveauCredit = rs.getInt( "credit" ) - valeurEnchere;
                                // Et on l'envoie dans la requ??te
                                requeteUpdateCredit.setInt( 1, nouveauCredit );
                                requeteUpdateCredit.setInt( 2, idEncherisseur );

                                resultatUpdateCreditNouvelEncherisseur = requeteUpdateCredit.executeUpdate();
                                if ( resultatUpdateCreditNouvelEncherisseur != 1 ) {
                                    // Si l'update ne s'est pas bien pass??, on
                                    // rollback
                                    connexion.rollback();
                                    // Et on lance une businessException
                                    BusinessException businessException = new BusinessException();
                                    businessException.ajouterErreur( CodesResultatDAL.ERROR_PLACEMENT_ENCHERE );
                                    throw businessException;
                                }
                            } else {
                                // Si l'utilisateur n'a pas assez de cr??dit,
                                // c'est une
                                // erreur, on rollback
                                connexion.rollback();
                                // Et on lance une exception
                                BusinessException businessException = new BusinessException();
                                businessException.ajouterErreur( CodesResultatDAL.ERROR_CREDIT_INSUFFISANT );
                                throw businessException;
                            }
                        } else {
                            // Si pas de r??sultat, c'est une erreur, on rollback
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
        // On v??rifie que tout s'est bien pass??, et on retourne l'??tat de la
        // requ??te
        if ( resultatInsert == 1 && resultatInsertArticle == 1 && resultatUpdateCreditAncienEncherisseur == 1
                && resultatUpdateCreditNouvelEncherisseur == 1 ) {
            return true;
        } else {
            return false;
        }
    }

    // M??thode qui permettait de mettre ?? jour la table article vendus lorsqu'on
    // utilisait le filtre pour v??rifier fin ench??re
    @Override
    public void majEnchere() throws BusinessException {

        try ( Connection connexion = ConnectionProvider.getConnection();
                PreparedStatement requete = connexion.prepareStatement( sqlListeArticle );
                ResultSet rs = requete.executeQuery() ) {
            LocalDate dateActuel = LocalDate.now();
            while ( rs.next() ) {
                // Si la date de fin d'ench??res est pass??e
                // On met ?? jour la prix de vente
                if ( LocalDate.parse( rs.getString( "date_fin_encheres" ) ).isBefore( dateActuel ) ) {
                    // Il faut donc r??cup??rer l'ench??re max pour cet article
                    Enchere enchereMax = selectById( rs.getInt( "no_article" ) );

                    // Et maintenant, il faut update la table ARTICLES_VENDUS
                    // Pour mettre le prix de vente, ce qui indique que
                    // l'ench??re est termin??e
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
