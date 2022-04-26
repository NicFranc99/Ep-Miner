package data;

import java.io.Serializable;

/**
 * @author nicola francavilla
 * Classe che modella un attributo discreto rappresentando l'insieme di valori distinti del relativo dominio
 */
public class DiscreteAttribute extends Attribute implements Serializable {
    /**
     * @uml.property name="values"
     * array di stringhe, una per ciascun valore discreto , che rappresenta
     * il domino dell’attributo
     */
    private String values[];

    /**
     * Invoca il costruttore della classe madre e avvalora l'array <b>values[]</b> con i valori discreti in input
     *
     * @param name   = nome simbolico dell'attributo
     * @param index  = identificativo numerico dell'attributo (Indice colonna)
     * @param values = valori discreti (Array di stringhe) che costituiscono il dominio dell'attributo
     */
     DiscreteAttribute(String name, int index, String values[]) {
        super(name, index);
        this.values = values;
    }

    /**
     * Restituisce la cardinalità del membro <b>Values</b>
     *
     * @return numero di valori discreti dell'attributo
     */
    public int getNumberOfDistinctValues() {
        return this.values.length;
    }

    /**
     * Restituisce il valore in posizione i del membro <b>Values</b>
     *
     * @param index = indice di tipo intero
     * @return Restituisce un valore nel dominio dell'attributo
     */
    public String getValue(int index) {
        return this.values[index];
    }
}
