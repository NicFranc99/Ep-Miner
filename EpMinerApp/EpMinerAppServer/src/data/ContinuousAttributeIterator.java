package data;

import java.util.Iterator;

/**
 * @author nicola francavilla
 * <p>Tale classe realizza l'iteratore che itera sugli elementi della sequenza composta da {@link data.ContinuousAttributeIterator#numValues} valori reali equidistanti
 * tra loro (cut Points) compresi tra {@link data.ContinuousAttributeIterator#min} e {@link data.ContinuousAttributeIterator#max}}. Ottenuti per mezzo della discretizzazione.</p>
 */
public class ContinuousAttributeIterator implements Iterator<Float> {
    /**
     * @uml.property name="min" minimo
     */
    private float min;
    /**
     * @uml.property name="max" massimo
     */
    private float max;
    /**
     * @uml.property name="j" Posizione dell'iteratore nella collezione dei <b>cut Point</b> generati per [{@link data.ContinuousAttributeIterator#min},{@link data.ContinuousAttributeIterator#max}[
     * tramite discretizzazione.
     */
    private int j = 0;
    /**
     * @uml.property name="numValues" numero di intervalli di discretizzazione.
     */
    private int numValues;

    /**
     * <p>Avvalora i membri dell'attributo della classe con i membri passati come parametri del costruttore</p>
     *
     * @param min       = minimo
     * @param max       = massimo
     * @param numValues = numero di intervalli per la discretizzazione
     */
    ContinuousAttributeIterator(float min, float max, int numValues) {
        this.min = min;
        this.max = max;
        this.numValues = numValues;
    }

    /**
     * Metodo utilizzato per controllare se l'indice j sia maggiore uguale al numero degli intervalli per la discretizzazione
     *
     * @return <font color="green"> True </font> = se {@link data.ContinuousAttributeIterator#j}<= {@link data.ContinuousAttributeIterator#numValues}<font color="green">false</font> = altrimenti</p>
     */
    @Override
    public boolean hasNext() {

        return (j <= numValues);
    }

    /**
     * <p>Incrementa {@link data.ContinuousAttributeIterator#j} e restituisce il cutPoint in posizione {@link data.ContinuousAttributeIterator#j} - 1</p>.
     *
     * @return (min + ( j - 1)*(max-min)/numValues).
     */
    public Float next() {

        j++;
        return min + ((max - min) / numValues) * (j - 1);
    }

    public void remove() {


    }

}
