package data;

import java.io.Serializable;

/**
 * @author nicola francavilla
 * Classe astratta che modella un generico attributo discreto o continuo.
 */
public abstract class Attribute implements Serializable {
    /**
     * @uml.property name="name"
     * Nome simbolico dell'attributo
     */
    private String name;
    /**
     * @uml.property name="index"
     * Identificativo numerico dell'attributo (Indica la posizione della colonna che
     * rappresenta l'attributo nella tabella di dati)
     */
    private int index;

    /**
     * Costruttore utilizzato per inizializzare i valori dei membri <b>name,index</b>
     *
     * @param name  = nome simbolico dell'attributo da creare
     * @param index = identificativo dell'attributo da creare
     */
    Attribute(String name, int index) {
        this.name = name;
        this.index = index;
    }

    /**
     * Restituisce il valore nel membro <b>name</b>;
     *
     * @return nome dell'attributo attualmente in uso
     */
    private String getName() {
        return this.name;
    }

    /**
     * Restituisce il valore del membro <b>Index</b>
     *
     * @return identificativo numerico dell'attributo
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Restituisce il valore del membro <b>Name</b>
     *
     * @return identificativo dell'attributo
     */
    public String toString() {
        return this.getName();
    }
}