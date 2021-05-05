package fr.eni.eniencheres.dal;

import java.util.List;

import fr.eni.eniencheres.bll.bo.Enchere;
import fr.eni.eniencheres.messages.BusinessException;

public interface EnchereDAO {

    public abstract void insert( Enchere enchere ) throws BusinessException;

    public abstract void delete( Enchere enchere ) throws BusinessException;

    public abstract Enchere selectById( int id ) throws BusinessException;

    public abstract List<Enchere> selectAll( String motRecherche, String categories ) throws BusinessException;

    public abstract boolean encherir( int idArticle, int idEncherisseur, int valeurEnchere )
            throws BusinessException;

    public abstract void majEnchere() throws BusinessException;

}
