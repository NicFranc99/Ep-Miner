package mining;

import java.io.Serializable;

import data.Attribute;
import data.ContinuousAttribute;

/**
 * @author nicola francavilla
 * <p>Classe concreta che modella la coppia <b><attributo continuo - Intervallo di valori></b>(Temperatura in [10;30.31[)</p>
 */
class ContinuousItem extends Item implements Serializable {
    /**
     * <p>Chiama il costruttore della superclasse {@link mining.Item#Item(Attribute, Object)} passandogli come argomenti <b>attribute</b> e <b>value.</b></p>
     *
     * @param attribute = attributo continuo
     * @param value     = intervallo
     */
    public ContinuousItem(ContinuousAttribute attribute, Interval value) {
        super(attribute, value);
    }

    /**
     * <p>Verifica che il parametro passato (<b>value</b>) rappresenti un numero reale incluso tra gli estremi dell'intervallo associato
     * all'item in oggetto</p>
     *
     * @param value = valore di cui verificare le condizioni
     * @return <font color="green">True</font> = nel caso in cui il valore passato rappresenti un numero reale compreso nell'intervallo associato.
     * <font color="red">False</font> = altrimenti.
     */
    boolean checkItemCondition(Object value) {
        Interval intervallo = (Interval) getValue();
        return intervallo.checkValueInclusion((float) value);
    }

    /**
     * <p>Avvalora la stringa che rappresenta lo stato dell'oggetto e ne restituisce il riferimento.</p>
     *
     * @return Stringa che rappresenta lo stato dell'oggetto nella forma <b><nome attributo> in [<inf>,<sup>[</b>
     */
    public String toString() {
        return "(" + getAttribute().toString() + " in " + (Interval) getValue() + ")";
    }
}
