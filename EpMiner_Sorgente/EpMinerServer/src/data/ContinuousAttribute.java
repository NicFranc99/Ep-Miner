package data;

import java.io.Serializable;
import java.util.Iterator;

/**
 * @author nicola francavilla
 * Classe che modella un attributo continuo (numerico) rappresentandone il dominio [min,max]
 */
public class ContinuousAttribute extends Attribute implements Iterable<Float>, Serializable {
    /**
     * @uml.property name="max"
     * Estremo superiore dell'intervallo
     */
    private float max;
    /**
     * @uml.property name="min"
     * Estremo inferiore dell'intervallo
     */
    private float min;

    /**
     * Invoca il costruttore della classe madre e avvalora i membri
     *
     * @param name  = nome dell'attributo
     * @param index = identificativo numerico dell'attributo
     * @param min   = estremo inferiore dell'intervallo
     * @param max   = estremo superiore dell'intervallo
     */
    ContinuousAttribute(String name, int index, float min, float max) {
        super(name, index);
        this.max = max;
        this.min = min;
    }

    /**
     * Restituisce il valore del membro <b>Min</b>
     *
     * @return estremo inferiore dell'intervallo
     */
    public float getMin() {
        return this.min;
    }

    /**
     * Restituisce il valore del membro <b>Max</b>
     *
     * @return estremo superiore dell'intervallo
     */
    public   float getMax() {
        return this.max;
    }

    /**
     * <p>Istanzia e restituisce un riferimento ad oggetto di classe {@link data.ContinuousAttributeIterator} con numero di intervalli di discretizzazione pari a 5.</p>
     *
     * @return Riferimento a una istanza di {@link data.ContinuousAttributeIterator}
     */
    public Iterator<Float> iterator() {
        return new ContinuousAttributeIterator(min, max, 5);
    }

}