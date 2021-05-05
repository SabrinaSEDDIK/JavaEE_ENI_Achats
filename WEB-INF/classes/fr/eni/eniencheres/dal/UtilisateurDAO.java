package fr.eni.eniencheres.dal;

import fr.eni.eniencheres.bll.bo.Utilisateur;
import fr.eni.eniencheres.messages.BusinessException;

public interface UtilisateurDAO {

    public abstract Utilisateur insert( Utilisateur utilisateur ) throws BusinessException;

    public abstract void delete( Utilisateur utilisateur ) throws BusinessException;

    public abstract Utilisateur selectFrom( String pseudoMail, String motDePasse ) throws BusinessException;

    public abstract Utilisateur selectById( int id ) throws BusinessException;

    public abstract Utilisateur update( Utilisateur utilisateur ) throws BusinessException;

    public abstract void crediterUtilisateur( Integer idUtilisateur, Integer idArticle, Integer prixVente )
            throws BusinessException;

}
