package fr.eni.eniencheres.dal;

import java.util.List;

import fr.eni.eniencheres.bll.bo.Article;
import fr.eni.eniencheres.messages.BusinessException;

public interface ArticleDAO {

    //////////////// INSERT /////////////////////////
    public abstract void insertArticle( Article article ) throws BusinessException;

    //////////////////////// DELETE ////////////////////////////////
    public abstract boolean deleteArticle( int idArticle ) throws BusinessException;

    ////////////////////// SELECT ///////////////////////////////////////

    public abstract Article selectById( int id ) throws BusinessException;

    public abstract List<Article> selectEncheresOuvertes( String motRecherche, String categories )
            throws BusinessException;

    public abstract List<Article> selectEncheresEnCours( String motRecherche, String categories, int idUtilisateur )
            throws BusinessException;

    public abstract List<Article> selectEncheresRemportees( String motRecherche, String categories, int idUtilisateur )
            throws BusinessException;

    public abstract List<Article> selectVentesEnCours( String motRecherche, String categories, int idUtilisateur )
            throws BusinessException;

    public abstract List<Article> selectVentesNonDebutees( String motRecherche, String categories, int idUtilisateur )
            throws BusinessException;

    public abstract List<Article> selectVentesTerminees( String motRecherche, String categories, int idUtilisateur )
            throws BusinessException;

    public abstract boolean updateArticle( Article article ) throws BusinessException;

}
