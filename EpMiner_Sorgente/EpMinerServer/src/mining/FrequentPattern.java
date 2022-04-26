package mining;

import data.ContinuousAttribute;
import data.Data;
import data.DiscreteAttribute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author nicola francavilla
 * Classe che rappresenta un <b>ItemSet Frequente</b> (pattern)
 */
class FrequentPattern implements Iterable<Item>, Comparable<FrequentPattern>, Serializable {

    /**
     * <p>Array che contiene riferimenti a oggetti istanza della classe <b>Item</b> che definiscono il pattern
     *
     * @uml.property name="fp"
     * @uml.associationEnd multiplicity="(0 -1)" </p>
     */
    private ArrayList<Item> fp;
    /**
     * <p>Valore di supporto calcolato per il pattern <b>fp</b>
     *
     * @uml.property name="support"</p>
     */
    private float support;

    /**
     * <p>Costruttore che alloca <b>fp</b> come array di dimensione 0</p>
     */
    FrequentPattern() {
        fp = new ArrayList<Item>();
    }

    /**
     * <p>Costruttore che alloca <b>fp</b> e <b>Support</b> come copia del frequent pattern <b>FP</b> passato come argomento
     *
     * @param FP = FrequentPattern di cui creare una copia </p>
     */
    // costrutore per copia
    FrequentPattern(FrequentPattern FP) {
        fp = new ArrayList<Item>();
        for (Item currentItem : FP.fp)
            fp.add(currentItem);
        support = FP.getSupport();
    }

    /**
     * @param fp
     * @return
     */
    @Override
    public int compareTo(FrequentPattern fp) {
        return support < fp.getSupport() ? -1 : support > fp.getSupport() ? +1 : 0;
    }

    /**
     * @return
     */
    @Override
    public Iterator<Item> iterator() {
        return fp.iterator();
    }

    /**
     * <p>Si estende la dimensione di <b>fp</b> di 1 e si inserisce l'item passato come argomento nell'ultima posizione dell'arrayList
     *
     * @param item = Oggetto item da aggiungere al Pattern </p>
     */
    void addItem(Item item) {
        fp.add(item);
    }

    /**
     * <p>Restituisce l'item in posizione <b>Index</b> di <b>fp</b>
     *
     * @param index = posizione in <b>fp</b> di cui recuperare l'elemento
     * @return Item che occupa la posizione indicata in <b>fp</b> </p>
     */
    public Item getItem(int index) {
        return fp.get(index);
    }

    /**
     * <p>Restituisce il membro <b>Support</b>
     *
     * @return valore di supporto del pattern considerato</p>
     */
    public float getSupport() {
        return support;
    }

    /**
     * <p>Restituisce la dimensione (lunghezza) di <b>fp</b>
     *
     * @return lunghezza del pattern </p>
     */
    public int getPatternLength() {
        return fp.size();
    }

    /**
     * <p>Scandisce <b>fp</b> al fine di concatenare in una stringa la rappresentazione degli item; alla fine si concatena il loro supporto
     *
     * @return Stringa rappresentante l'item set e il suo supporto </p>
     */
    public String toString() {
        String value = "";
        Iterator<Item> e = fp.iterator();
        while (e.hasNext()) {
            value += e.next();
            if (e.hasNext())
                value += " AND ";
        }
        if (fp.size() > 0)
            value += "[" + support + "]";
        return value;
    }

    /**
     * <p>Calcola il supporto del pattern rappresentato dall'oggetto <b>This</b> rispetto al dataset <b>data</b> passato come argomento
     *
     * @param data = Dataset per il calcolo del supporto del pattern considerato.
     * @return valore di supporto del pattern nel dataset <b>Data</b> </p>
     */
    // Aggiorna il supporto
     float computeSupport(Data data) {
        int suppCount = 0;
        // indice esempio
        for (int i = 0; i < data.getNumberOfExamples(); i++) {
            // indice item
            boolean isSupporting = true;
            for (Item currentItem : this.fp) {
                if (currentItem instanceof DiscreteItem) {
                    DiscreteItem item = (DiscreteItem) currentItem;
                    DiscreteAttribute attribute = (DiscreteAttribute) item.getAttribute();
                    // Extract the example value
                    Object valueInExample = data.getAttributeValue(i, attribute.getIndex());
                    if (!item.checkItemCondition(valueInExample)) {
                        isSupporting = false;
                        break; // the ith example does not satisfy fp
                    }
                } else if (currentItem instanceof ContinuousItem) {
                    ContinuousAttribute attribute = (ContinuousAttribute) currentItem.getAttribute();
                    // Extract the example value
                    Object valueInExample = data.getAttributeValue(i, attribute.getIndex());
                    if (!currentItem.checkItemCondition(valueInExample)) {
                        isSupporting = false;
                        break; // the ith example does not satisfy fp
                    }
                }
            }
            if (isSupporting)
                suppCount++;
        }
        return ((float) suppCount) / (data.getNumberOfExamples());

    }

    /**
     * <p>Assegna al membro <b>Support</b> il parametro della procedura
     *
     * @param support = Valore di supporto del pattern</p>
     */
    public void setSupport(float support) {
        this.support = support;
    }
}
