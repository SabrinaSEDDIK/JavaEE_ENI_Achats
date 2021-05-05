package fr.eni.eniencheres.messages;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Administrator
 *
 *         Cette classe permet de recenser l'ensemble des erreurs (par leur
 *         code) pouvant survenir lors d'un traitement quel que soit la couche à
 *         l'origine.
 */
public class BusinessException extends Exception {
    private static final long serialVersionUID = 1L;
    private List<String>      listeMessagesErreur;

    public BusinessException() {
        super();
        this.listeMessagesErreur = new ArrayList<>();
    }

    /**
     * 
     * @param code
     *            Code de l'erreur. Doit avoir un message associé dans un
     *            fichier properties.
     */
    public void ajouterErreur( int code ) {
        String messageErreur = LecteurMessage.getMessageErreur( code );

        if ( !this.listeMessagesErreur.contains( messageErreur ) ) {
            this.listeMessagesErreur.add( messageErreur );
        }
    }

    public boolean hasErreurs() {
        return this.listeMessagesErreur.size() > 0;
    }

    public List<String> getListeMessageErreur() {
        return this.listeMessagesErreur;
    }

}