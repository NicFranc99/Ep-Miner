package mining;

import data.DiscreteAttribute;

import java.io.Serializable;

/**
 * @author nicola francavilla
 * Classe concreta che estende la classe <b>Item</b> e rappresenta la coppia
 * <b><attributo discreto - valore discreto> (Outlook="Sunny")</b>
 */
public class DiscreteItem extends Item implements Serializable {
    /**
     * Invoca il costruttore della classe madre per avvalorare i membri
     *
     * @param attribute = attributo discreto
     * @param value     = valore dell'attributo discreto
     */
    DiscreteItem(DiscreteAttribute attribute, String value) {
        super(attribute, value);
    }

    /**
     * <p>Verifica che il membro <b>Value</b> sia uguale (nello stato) all'argomento passato come parametro del metodo
     *
     * @param value = valore dichiarato di tipo <b>Object</b>(sarà di tipo String)
     * @return <font color="green">true</font> = condizione soddisfatta, <font color="red">false</font> = altrimenti </p>
     */
    public boolean checkItemCondition(Object value) {
        return this.getValue().equals(value);
    }
}