package fr.eni.eniencheres.dal;

public abstract class DAOFactory {

    public static ArticleDAO getArticleDAO() {

        return new ArticleDAOJdbcImpl();
    }

    public static UtilisateurDAO getUtilisateurDAO() {

        return new UtilisateurDAOJdbcImpl();
    }

    public static EnchereDAO getEnchereDAO() {

        return new EnchereDAOJdbcImpl();
    }
}
