package mining;

import java.io.Serializable;

/**
 * @author nicola francavilla
 * <p>Classe che modella un intervallo reale <b>[inf,sup[.</b></p>
 */
class Interval implements Serializable {
    /**
     * @uml.property name="inf" estremo inferiore.
     */
    private float inf;
    /**
     * @uml.property name="sup" estremo superiore.
     */
    private float sup;

    /**
     * <p>Avvalora i due attribute {@link mining.Interval#inf} e {@link mining.Interval#sup} con i parametri del costruttore</p>
     *
     * @param inf estremo inferiore.
     * @param sup estremo superiore.
     */
    Interval(float inf, float sup) {
        this.inf = inf;
        this.sup = sup + 0.0606f;
    }

    /**
     * Avvalora {@link mining.Interval#inf} con il parametro passato a tale metodo.
     *
     * @param inf estremo inferiore di cui avvalorare l'attributo {@link mining.Interval#inf}
     */
    void setInf(float inf) {
        this.inf = inf;
    }

    /**
     * Avvalora {@link mining.Interval#sup} con il parametro passato a tale metodo.
     *
     * @param sup estremo superiore di cui avvalorare l'attributo {@link mining.Interval#sup}
     */
    void setSup(float sup) {
        this.sup = sup;
    }

    /**
     * Restituisce l'estremo inferiore {@link mining.Interval#inf}
     *
     * @return estremo inferiore
     */
    float getInf() {
        return inf;
    }

    /**
     * Restituisce l'estremo superiore {@link mining.Interval#sup}
     *
     * @return estremo superiore
     */
    float getSup() {
        return sup;
    }

    /**
     * <p>Restituisce <font color="green">True</font> = Caso in cui il parametro è <U>maggiore uguale</U> di {@link mining.Interval#inf} e <U>minore</U> di {@link mining.Interval#sup}
     * <font color="red">False</font>=altrimenti.</p>
     *
     * @param value
     * @return
     */
    boolean checkValueInclusion(float value) {
        return value >= inf && value < sup;
    }

    /**
     * <p>Rappresenta in una stringa gli estremi (inferiore,superiore) dell'intervallo e restituisce tale stringa</p>
     *
     * @return Riferimento ad una stringa in cui si rappresenta l'intervallo <b>[inf,sup[.</b>
     */
    public String toString() {
        return "[" + inf + "," + sup + "[";
    }
}
