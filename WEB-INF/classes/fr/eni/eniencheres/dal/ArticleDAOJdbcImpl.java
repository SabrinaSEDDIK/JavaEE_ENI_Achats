package fr.eni.eniencheres.dal;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import fr.eni.eniencheres.bll.bo.Article;
import fr.eni.eniencheres.messages.BusinessException;

public class ArticleDAOJdbcImpl implements ArticleDAO {

    // SQL
    private static final String SELECT_ENCHERES_OUVERTES     = "SELECT * FROM ARTICLES_VENDUS a INNER JOIN UTILISATEURS u ON (a.no_utilisateur=u.no_utilisateur) WHERE no_categorie IN (?,?,?,?) AND nom_article LIKE ? AND GETDATE() BETWEEN date_debut_encheres AND date_fin_encheres;";
    private static final String SELECT_ENCHERES_EN_COURS     = "SELECT * FROM ENCHERES e INNER JOIN ARTICLES_VENDUS a ON (e.no_article = a.no_article) INNER JOIN UTILISATEURS u ON (a.no_utilisateur = u.no_utilisateur) WHERE no_categorie IN (?,?,?,?) AND nom_article LIKE ? AND e.no_utilisateur = ? AND GETDATE() BETWEEN date_debut_encheres AND date_fin_encheres;";
    private static final String SELECT_ENCHERES_REMPORTEES_1 = "SELECT e.no_article AS \"idArticlesEncheres\" FROM ENCHERES e INNER JOIN ARTICLES_VENDUS a ON (e.no_article = a.no_article) WHERE no_categorie IN (?,?,?,?) AND nom_article LIKE ? AND GETDATE() > date_fin_encheres GROUP BY e.no_article;";
    private static final String SELECT_ENCHERES_REMPORTEES_2 = "SELECT TOP(1) a.no_utilisateur as \"idVendeur\", e.no_utilisateur, e.no_article, a.nom_article, a.no_categorie, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, pseudo FROM ENCHERES e INNER JOIN ARTICLES_VENDUS a ON (e.no_article = a.no_article) INNER JOIN UTILISATEURS u ON (a.no_utilisateur = u.no_utilisateur) WHERE e.no_article = ? ORDER BY date_enchere DESC";
    private static final String SELECT_VENTES_EN_COURS       = "SELECT * FROM ARTICLES_VENDUS a INNER JOIN UTILISATEURS u ON (a.no_utilisateur=u.no_utilisateur) WHERE no_categorie IN (?,?,?,?) AND nom_article LIKE ? AND a.no_utilisateur = ? AND GETDATE() BETWEEN date_debut_encheres AND date_fin_encheres;";
    private static final String SELECT_VENTES_NON_DEBUTEES   = "SELECT * FROM ARTICLES_VENDUS a INNER JOIN UTILISATEURS u ON (a.no_utilisateur=u.no_utilisateur) WHERE no_categorie IN (?,?,?,?) AND nom_article LIKE ? AND a.no_utilisateur = ? AND GETDATE() < date_debut_encheres";
    private static final String SELECT_VENTES_TERMINEES      = "SELECT * FROM ARTICLES_VENDUS a INNER JOIN UTILISATEURS u ON (a.no_utilisateur=u.no_utilisateur) WHERE no_categorie IN (?,?,?,?) AND nom_article LIKE ? AND a.no_utilisateur = ? AND GETDATE() > date_fin_encheres";

    private static final String INSERT_ARTICLE               = "INSERT INTO ARTICLES_VENDUS (nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, no_utilisateur, no_categorie, etat_vente) VALUES (?,?,?,?,?,?,?,0);";
    private static final String INSERT_RETRAIT               = "INSERT INTO RETRAITS VALUES (?,?,?,?);";

    private static final String UPDATE_ARTICLE               = "UPDATE ARTICLES_VENDUS SET nom_article = ?, description = ?, date_debut_encheres = ?, date_fin_encheres = ?, prix_initial = ?, no_categorie = ? WHERE no_article = ?";
    private static final String UPDATE_RETRAIT               = "UPDATE RETRAITS SET rue = ?, code_postal = ?, ville = ? WHERE no_article = ?";

    private static final String DELETE_RETRAITS              = "DELETE FROM RETRAITS WHERE no_article = ?";
    private static final String DELETE_ARTICLE               = "DELETE FROM ARTICLES_VENDUS WHERE no_article = ?";

    private static final String SELECT_BY_ID                 = "SELECT no_article, nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, a.no_utilisateur, a.no_categorie, u.pseudo "
            + "                                                     FROM ARTICLES_VENDUS AS a "
            + "                                                         INNER JOIN UTILISATEURS AS u ON a.no_utilisateur = u.no_utilisateur WHERE no_article = ?";

    @Override
    public List<Article> selectEncheresOuvertes( String motRecherche, String categories ) throws BusinessException {

        List<Article> liste = new ArrayList<Article>();

        Connection cnx = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            cnx = ConnectionProvider.getConnection();
            pstmt = cnx.prepareStatement( SELECT_ENCHERES_OUVERTES );
            pstmt.setString( 1, String.valueOf( categories.charAt( 0 ) ) );
            pstmt.setString( 2, String.valueOf( categories.charAt( 1 ) ) );
            pstmt.setString( 3, String.valueOf( categories.charAt( 2 ) ) );
            pstmt.setString( 4, String.valueOf( categories.charAt( 3 ) ) );
            pstmt.setString( 5, "%" + motRecherche + "%" );
            rs = pstmt.executeQuery();
            while ( rs.next() ) {
                liste.add( new Article( rs.getInt( "no_article" ), rs.getString( "nom_article" ),
                        rs.getString( "description" ), rs.getDate( "date_debut_encheres" ).toLocalDate(),
                        rs.getDate( "date_fin_encheres" ).toLocalDate(), rs.getInt( "prix_initial" ),
                        rs.getInt( "prix_vente" ), rs.getInt( "no_categorie" ), rs.getInt( "no_utilisateur" ),
                        rs.getString( "pseudo" ) ) );
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
            BusinessException businessException = new BusinessException();
            businessException.ajouterErreur( CodesResultatDAL.SELECT_ECHEC );
            throw businessException;
        } finally {
            fermer( rs );
            fermer( pstmt );
            fermer( cnx );
        }
        return liste;
    }

    @Override
    public List<Article> selectEncheresEnCours( String motRecherche, String categories, int idUtilisateur )
            throws BusinessException {

        List<Article> liste = new ArrayList<Article>();

        Connection cnx = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            cnx = ConnectionProvider.getConnection();
            pstmt = cnx.prepareStatement( SELECT_ENCHERES_EN_COURS );
            pstmt.setString( 1, String.valueOf( categories.charAt( 0 ) ) );
            pstmt.setString( 2, String.valueOf( categories.charAt( 1 ) ) );
            pstmt.setString( 3, String.valueOf( categories.charAt( 2 ) ) );
            pstmt.setString( 4, String.valueOf( categories.charAt( 3 ) ) );
            pstmt.setString( 5, "%" + motRecherche + "%" );
            pstmt.setInt( 6, idUtilisateur );
            rs = pstmt.executeQuery();
            while ( rs.next() ) {
                liste.add( new Article( rs.getInt( "no_article" ), rs.getString( "nom_article" ),
                        rs.getString( "description" ), rs.getDate( "date_debut_encheres" ).toLocalDate(),
                        rs.getDate( "date_fin_encheres" ).toLocalDate(), rs.getInt( "prix_initial" ),
                        rs.getInt( "prix_vente" ), rs.getInt( "no_categorie" ), rs.getInt( "no_utilisateur" ),
                        rs.getString( "pseudo" ) ) );
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
            BusinessException businessException = new BusinessException();
            businessException.ajouterErreur( CodesResultatDAL.SELECT_ECHEC );
            throw businessException;
        } finally {
            fermer( rs );
            fermer( pstmt );
            fermer( cnx );
        }
        return liste;
    }

    @Override
    public List<Article> selectEncheresRemportees( String motRecherche, String categories, int idUtilisateur )
            throws BusinessException {

        List<Article> listeRenvoyee = new ArrayList<Article>();
        List<Article> liste = new ArrayList<Article>();
        List<Integer> listeIdArticles = new ArrayList<>();

        Connection cnx = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        PreparedStatement stmt = null;
        try {
            cnx = ConnectionProvider.getConnection();
            stmt = cnx.prepareStatement( SELECT_ENCHERES_REMPORTEES_1 );
            stmt.setString( 1, String.valueOf( categories.charAt( 0 ) ) );
            stmt.setString( 2, String.valueOf( categories.charAt( 1 ) ) );
            stmt.setString( 3, String.valueOf( categories.charAt( 2 ) ) );
            stmt.setString( 4, String.valueOf( categories.charAt( 3 ) ) );
            stmt.setString( 5, "%" + motRecherche + "%" );
            rs = stmt.executeQuery();
            // etape 1 : r�cup�rer une liste d'id des articles dont la vente est
            // termin�e
            while ( rs.next() ) {
                listeIdArticles.add( rs.getInt( "idArticlesEncheres" ) );
            }
            // �tape 2 : seconde requ�te SQL
            List<Integer> idGagnants = new ArrayList<>();
            for ( Integer id : listeIdArticles ) {
                pstmt = cnx.prepareStatement( SELECT_ENCHERES_REMPORTEES_2 );
                pstmt.setInt( 1, id );
                rs2 = pstmt.executeQuery();

                while ( rs2.next() ) {

                    liste.add( new Article( rs2.getInt( "no_article" ), rs2.getString( "nom_article" ),
                            rs2.getString( "description" ), rs2.getDate( "date_debut_encheres" ).toLocalDate(),
                            rs2.getDate( "date_fin_encheres" ).toLocalDate(), rs2.getInt( "prix_initial" ),
                            rs2.getInt( "prix_vente" ), rs2.getInt( "no_categorie" ), rs2.getInt( "idVendeur" ),
                            rs2.getString( "pseudo" ) ) );
                    idGagnants.add( rs2.getInt( "no_utilisateur" ) );
                }
            }
            for ( int i = 0; i < liste.size(); i++ ) {
                if ( idGagnants.get( i ) == idUtilisateur ) {
                    listeRenvoyee.add( liste.get( i ) );
                }
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
            BusinessException businessException = new BusinessException();
            businessException.ajouterErreur( CodesResultatDAL.SELECT_ECHEC );
            throw businessException;
        } finally {
            fermer( rs );
            fermer( pstmt );
            fermer( cnx );
        }
        return listeRenvoyee;
    }

    @Override
    public List<Article> selectVentesEnCours( String motRecherche, String categories, int idUtilisateur )
            throws BusinessException {

        List<Article> liste = new ArrayList<Article>();

        Connection cnx = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            cnx = ConnectionProvider.getConnection();
            pstmt = cnx.prepareStatement( SELECT_VENTES_EN_COURS );
            pstmt.setString( 1, String.valueOf( categories.charAt( 0 ) ) );
            pstmt.setString( 2, String.valueOf( categories.charAt( 1 ) ) );
            pstmt.setString( 3, String.valueOf( categories.charAt( 2 ) ) );
            pstmt.setString( 4, String.valueOf( categories.charAt( 3 ) ) );
            pstmt.setString( 5, "%" + motRecherche + "%" );
            pstmt.setInt( 6, idUtilisateur );
            rs = pstmt.executeQuery();
            while ( rs.next() ) {
                liste.add( new Article( rs.getInt( "no_article" ), rs.getString( "nom_article" ),
                        rs.getString( "description" ), rs.getDate( "date_debut_encheres" ).toLocalDate(),
                        rs.getDate( "date_fin_encheres" ).toLocalDate(), rs.getInt( "prix_initial" ),
                        rs.getInt( "prix_vente" ), rs.getInt( "no_categorie" ), rs.getInt( "no_utilisateur" ),
                        rs.getString( "pseudo" ) ) );
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
            BusinessException businessException = new BusinessException();
            businessException.ajouterErreur( CodesResultatDAL.SELECT_ECHEC );
            throw businessException;
        } finally {
            fermer( rs );
            fermer( pstmt );
            fermer( cnx );
        }
        return liste;
    }

    @Override
    public List<Article> selectVentesNonDebutees( String motRecherche, String categories, int idUtilisateur )
            throws BusinessException {

        List<Article> liste = new ArrayList<Article>();

        Connection cnx = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            cnx = ConnectionProvider.getConnection();
            pstmt = cnx.prepareStatement( SELECT_VENTES_NON_DEBUTEES );
            pstmt.setString( 1, String.valueOf( categories.charAt( 0 ) ) );
            pstmt.setString( 2, String.valueOf( categories.charAt( 1 ) ) );
            pstmt.setString( 3, String.valueOf( categories.charAt( 2 ) ) );
            pstmt.setString( 4, String.valueOf( categories.charAt( 3 ) ) );
            pstmt.setString( 5, "%" + motRecherche + "%" );
            pstmt.setInt( 6, idUtilisateur );
            rs = pstmt.executeQuery();
            while ( rs.next() ) {
                liste.add( new Article( rs.getInt( "no_article" ), rs.getString( "nom_article" ),
                        rs.getString( "description" ), rs.getDate( "date_debut_encheres" ).toLocalDate(),
                        rs.getDate( "date_fin_encheres" ).toLocalDate(), rs.getInt( "prix_initial" ),
                        rs.getInt( "prix_vente" ), rs.getInt( "no_categorie" ), rs.getInt( "no_utilisateur" ),
                        rs.getString( "pseudo" ) ) );
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
            BusinessException businessException = new BusinessException();
            businessException.ajouterErreur( CodesResultatDAL.SELECT_ECHEC );
            throw businessException;
        } finally {
            fermer( rs );
            fermer( pstmt );
            fermer( cnx );
        }
        return liste;
    }

    @Override
    public List<Article> selectVentesTerminees( String motRecherche, String categories, int idUtilisateur )
            throws BusinessException {

        List<Article> liste = new ArrayList<Article>();

        Connection cnx = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            cnx = ConnectionProvider.getConnection();
            pstmt = cnx.prepareStatement( SELECT_VENTES_TERMINEES );
            pstmt.setString( 1, String.valueOf( categories.charAt( 0 ) ) );
            pstmt.setString( 2, String.valueOf( categories.charAt( 1 ) ) );
            pstmt.setString( 3, String.valueOf( categories.charAt( 2 ) ) );
            pstmt.setString( 4, String.valueOf( categories.charAt( 3 ) ) );
            pstmt.setString( 5, "%" + motRecherche + "%" );
            pstmt.setInt( 6, idUtilisateur );
            rs = pstmt.executeQuery();
            while ( rs.next() ) {
                liste.add( new Article( rs.getInt( "no_article" ), rs.getString( "nom_article" ),
                        rs.getString( "description" ), rs.getDate( "date_debut_encheres" ).toLocalDate(),
                        rs.getDate( "date_fin_encheres" ).toLocalDate(), rs.getInt( "prix_initial" ),
                        rs.getInt( "prix_vente" ), rs.getInt( "no_categorie" ), rs.getInt( "no_utilisateur" ),
                        rs.getString( "pseudo" ) ) );
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
            BusinessException businessException = new BusinessException();
            businessException.ajouterErreur( CodesResultatDAL.SELECT_ECHEC );
            throw businessException;
        } finally {
            fermer( rs );
            fermer( pstmt );
            fermer( cnx );
        }
        return liste;
    }

    @Override
    public void insertArticle( Article article ) throws BusinessException {

        Connection cnx = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs = null;
        try {
            cnx = ConnectionProvider.getConnection();
            pstmt = cnx.prepareStatement( INSERT_ARTICLE, Statement.RETURN_GENERATED_KEYS );
            pstmt.setString( 1, article.getNom() );
            pstmt.setString( 2, article.getDescription() );
            pstmt.setDate( 3, Date.valueOf( article.getDateDebutEnchere() ) );
            pstmt.setDate( 4, Date.valueOf( article.getDateFinEnchere() ) );
            pstmt.setInt( 5, article.getMiseAPrix() );
            pstmt.setInt( 6, article.getIdVendeur() );
            pstmt.setInt( 7, article.getIdCategorie() );
            pstmt.execute();
            rs = pstmt.getGeneratedKeys();
            rs.next();
            Integer idArticle = rs.getInt( 1 );
            pstmt2 = cnx.prepareStatement( INSERT_RETRAIT );
            pstmt2.setInt( 1, idArticle );
            pstmt2.setString( 2, article.getRetrait().getRue() );
            pstmt2.setString( 3, article.getRetrait().getCodePostal() );
            pstmt2.setString( 4, article.getRetrait().getVille() );
            pstmt2.execute();

        } catch ( SQLException sqle ) {
            sqle.printStackTrace();
            BusinessException be = new BusinessException();
            be.ajouterErreur( CodesResultatDAL.INSERT_ERREUR );
            throw be;
        } finally {
            fermer( rs );
            fermer( pstmt );
            fermer( cnx );
        }
    }

    @Override
    public boolean deleteArticle( int idArticle ) throws BusinessException {
        boolean resultatDelete = false;
        try ( Connection connexion = ConnectionProvider.getConnection();
                PreparedStatement requeteDeleteRetrait = connexion.prepareStatement( DELETE_RETRAITS );
                PreparedStatement requeteDeleteArticle = connexion.prepareStatement( DELETE_ARTICLE ) ) {
            requeteDeleteRetrait.setInt( 1, idArticle );
            requeteDeleteArticle.setInt( 1, idArticle );

            resultatDelete = requeteDeleteRetrait.executeUpdate() == 1 && requeteDeleteArticle.executeUpdate() == 1;
        } catch ( Exception e ) {
            e.printStackTrace();
            BusinessException businessException = new BusinessException();
            businessException.ajouterErreur( CodesResultatDAL.ERROR_DELETE_ARTICLE );
            throw businessException;
        }
        return resultatDelete;
    }

    @Override
    public Article selectById( int id ) throws BusinessException {
        Article articleRecupere = null;
        try ( Connection connexion = ConnectionProvider.getConnection();
                PreparedStatement requeteSelectById = connexion.prepareStatement( SELECT_BY_ID ) ) {
            requeteSelectById.setInt( 1, id );

            try ( ResultSet rs = requeteSelectById.executeQuery() ) {
                if ( rs.next() ) {
                    articleRecupere = new Article( rs.getInt( "no_article" ), rs.getString( "nom_article" ),
                            rs.getString( "description" ),
                            LocalDate.parse( rs.getString( "date_debut_encheres" ) ),
                            LocalDate.parse( rs.getString( "date_fin_encheres" ) ),
                            rs.getInt( "prix_initial" ), rs.getInt( "prix_vente" ), rs.getInt( "no_categorie" ),
                            rs.getInt( "no_utilisateur" ), rs.getString( "pseudo" ) );
                }
                // S'il n'y a pas de résultat, c'est que l'article n'existe pas.
                else {
                    BusinessException businessException = new BusinessException();
                    businessException.ajouterErreur( CodesResultatDAL.SELECTBY_ID_ARTICLE_INEXISTANT );
                    throw businessException;
                }
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
            BusinessException businessException = new BusinessException();
            businessException.ajouterErreur( CodesResultatDAL.ERROR_SELECT_ARTICLE );
            throw businessException;
        }
        return articleRecupere;
    }

    @Override
    public boolean updateArticle( Article article ) throws BusinessException {

        int resultatUpdateArticle = 0;
        int resultatUpdateRetrait = 0;
        try ( Connection connexion = ConnectionProvider.getConnection();
                PreparedStatement requeteUpdateArticle = connexion.prepareStatement( UPDATE_ARTICLE );
                PreparedStatement requeteUpdateRetrait = connexion.prepareStatement( UPDATE_RETRAIT ) ) {

            // Ajout des paramètres de requête pour l'article
            requeteUpdateArticle.setString( 1, article.getNom() );
            requeteUpdateArticle.setString( 2, article.getDescription() );
            requeteUpdateArticle.setDate( 3, Date.valueOf( article.getDateDebutEnchere() ) );
            requeteUpdateArticle.setDate( 4, Date.valueOf( article.getDateFinEnchere() ) );
            requeteUpdateArticle.setInt( 5, article.getMiseAPrix() );
            requeteUpdateArticle.setInt( 6, article.getIdCategorie() );
            requeteUpdateArticle.setInt( 7, article.getIdentifiant() );

            // Ajout des paramètres de requête pour le retrait
            requeteUpdateRetrait.setString( 1, article.getRetrait().getRue() );
            requeteUpdateRetrait.setString( 2, article.getRetrait().getCodePostal() );
            requeteUpdateRetrait.setString( 3, article.getRetrait().getVille() );
            requeteUpdateRetrait.setInt( 4, article.getIdentifiant() );

            resultatUpdateArticle = requeteUpdateArticle.executeUpdate();
            resultatUpdateRetrait = requeteUpdateRetrait.executeUpdate();

        } catch ( SQLException e ) {
            e.printStackTrace();
            BusinessException businessException = new BusinessException();
            businessException.ajouterErreur( CodesResultatDAL.ERROR_UPDATE_ARTICLE );
            throw businessException;
        }

        if ( resultatUpdateArticle == 1 && resultatUpdateRetrait == 1 ) {
            return true;
        } else {
            return false;
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

}
