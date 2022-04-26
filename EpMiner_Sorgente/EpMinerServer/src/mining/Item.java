package mining;

import data.Attribute;

import java.io.Serializable;

/**
 * @author nicola francavilla
 * Classe che modella un generico <b>Item</b> (coppia composta da attributo-valore, per esempio Outlook="Sunny")
 */
abstract class Item implements Serializable {
    /**
     * @uml.property name = "attribute"
     * Attributo coinvolto nell'item
     */
    private Attribute attribute;
    /**
     * @uml.property name="value"
     * Valore assegnato all'attributo
     */
    private Object value;

    /**
     * Inizializza i valori dei membri <b>Attribute,Value</b> con i parametri passati come argomento del costruttore
     *
     * @param attribute = riferimento per inizializzare il campo <b>Attribute</b>
     * @param value     = riferimento per inizializzare il campo <b>Value</b>
     */
     Item(Attribute attribute, Object value) {
        this.attribute = attribute;
        this.value = value;
    }

    /**
     * Restituisce il membro <b>Attribute</b>
     *
     * @return attributo membro dell'item
     */
     public Attribute getAttribute() {
        return this.attribute;
    }

    /**
     * Restituisce il membro <b>Value</b>
     *
     * @return valore coinvolto nell'item
     */
     public Object getValue() {
        return value;
    }

    /**
     * <p>Metodo da realizzare nelle sottoclassi che estendono la classe Item
     * Verifica che il membro <b>Value</b> sia uguale (nello stato) all'argomento passato come parametro del metodo
     *
     * @param value = valore dichiarato di tipo <b>Object</b>(sarà di tipo String)
     * @return <font color="green">true</font> = condizione soddisfatta, <font color="red">false</font> = altrimenti </p>
     */
    abstract boolean checkItemCondition(Object value);

    /**
     * <p>Restituisce una stringa nella forma <b><attribute>=<value></b>
     *
     * @return stringa composta da: <b>Attributo coinvolto nell'item + valore assegnato all'attributo</b></p>
     */
    public String toString() {
        return "(" + this.attribute + "=" + this.value + ")";
    }
}
