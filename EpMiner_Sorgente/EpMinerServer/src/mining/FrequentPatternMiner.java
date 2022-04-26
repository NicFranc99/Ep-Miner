package mining;

import utility.Queue;
import data.Data;
import data.Attribute;
import data.ContinuousAttribute;
import data.DiscreteAttribute;
import data.EmptySetException;
import utility.EmptyQueueException;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;
import java.util.Collections;
import java.io.*;

/**
 * @author nicola francavilla
 * <p>Classe concreta che include i metodi per la scoperta di pattern frequenti con Algoritmo <b>APRIORI</b> </p>
 */
public class FrequentPatternMiner implements Iterable<FrequentPattern>, Serializable {
    /**
     * <p>Lista che contiene riferimenti a oggetti istanza della classe <b>FrequentPattern</b> che definiscono il pattern
     *
     * @uml.property name="outputFP" </p>
     */
    private List<FrequentPattern> outputFP = new LinkedList<FrequentPattern>();

    /**
     * <p>Costruttore che genera tutti i pattern k=1 frequenti e per ognugno di questi genera quelli con k>1 richiamando <em>expandFrequentPatterns()</em>
     * I Pattern sono memorizzati nel membro <b>OutputFP</b></p>
     *
     * @param data   = Insieme delle transazioni
     * @param minSup = minimo supporto
     * @throws EmptySetException  = Caso in cui si passa a tale metodo un dataset vuoto
     * @throws ClassCastException = nel caso in cui si prova a fare un cast di una classe con un'altra di cui non è sottoClasse
     */
    public FrequentPatternMiner(Data data, float minSup) throws EmptySetException, ClassCastException {
        if (data.getNumberOfExamples() == 0)
            throw new EmptySetException("Data Empty!");
        Queue<Object> fpQueue = new Queue<Object>();
        for (Attribute currentAttribute : data.getAttributeSet()) {
            if (currentAttribute instanceof DiscreteAttribute) {
                for (int j = 0; j < ((DiscreteAttribute) currentAttribute).getNumberOfDistinctValues(); j++) {
                    DiscreteItem item = new DiscreteItem((DiscreteAttribute) currentAttribute,
                            ((DiscreteAttribute) currentAttribute).getValue(j));
                    FrequentPattern fp = new FrequentPattern();
                    fp.addItem(item);
                    fp.setSupport(fp.computeSupport(data));
                    if (fp.getSupport() >= minSup) { // 1-FP CANDIDATE
                        fpQueue.enqueue(fp);
                        // System.out.println(fp);
                        outputFP.add(fp);
                    }
                }
            } else if (currentAttribute instanceof ContinuousAttribute) {
                ContinuousAttribute continuousAttribute = (ContinuousAttribute) currentAttribute;
                Iterator<Float> e = continuousAttribute.iterator();

                if (e.hasNext()) {
                    float min = e.next();

                    while (e.hasNext()) {
                        float max = e.next();
                        ContinuousItem item = new ContinuousItem(continuousAttribute, new Interval(min, max));
                        FrequentPattern fp = new FrequentPattern();
                        fp.addItem(item);
                        fp.setSupport(fp.computeSupport(data));

                        if (fp.getSupport() >= minSup) { // 1-FP CANDIDATE
                            fpQueue.enqueue(fp);
                            // System.out.println(fp);
                            outputFP.add(fp);
                        }
                        min = max;
                    }
                }
            } else
                throw new ClassCastException();
        }
        outputFP = expandFrequentPatterns(data, minSup, fpQueue, outputFP);
        sort();
    }

    /**
     * @return
     */
    @Override
    public Iterator<FrequentPattern> iterator() {
        return outputFP.iterator();
    }

/*    private List<FrequentPattern> getoutputFP() {
        return outputFP;
    }*/

    /**
     * <p>Crea un nuovo pattern a cui aggiunge tutti ghli item di FP e il parametro Item</p>
     *
     * @param FP   = Pattern FP da raffinare
     * @param item = Item da aggiungere ad FP
     * @return Nuovo pattern ottenuto per effetto del <em>Raffinamento</em>
     */
    private FrequentPattern refineFrequentPattern(FrequentPattern FP, Item item) {
        FrequentPattern raffinateFP = new FrequentPattern(FP);
        raffinateFP.addItem(item);
        return raffinateFP;
    }

    /**
     * <p>Scandisce <b>outputFP</b> al fine di concatenare in un'unica stringa i pattern frequenti letti.</p>
     *
     * @return Stringa rappresentante il valore di <b>OutputFP</b>
     */
    public String toString() {
        String result = "Frequent patterns" + "\n";
        int i = 1;
        FrequentPattern fp = new FrequentPattern();
        Iterator<FrequentPattern> e = outputFP.iterator();
        while (e.hasNext()) {
            fp = e.next();
            result += String.valueOf(i) + ":" + fp + "\n";
            i++;
        }
        return result;
    }

    /**
     * <p>Finchè fpQueue contiene elementi, si estrae un elemento dalla coda fpQueue, Si generano tutti i raffinamenti
     * per questo (aggiungendo un nuovo Item <U>non incluso</U>). Per ogni raffinamento si verifica se è frequente e, in tal caso
     * lo si aggiunge sia ad fpQueue sia ad outputFP</p>
     *
     * @param data     = Insieme delle transaz<ioni
     * @param minSup   = minimo supporto
     * @param fpQueue  = cosa contenente i pattern da valutare
     * @param outputFP = lista dei pattern frequenti già estratti
     * @return Lista Linkata popolata con pattern frequenti a k>1
     */
    private List<FrequentPattern> expandFrequentPatterns(Data data, float minSup, Queue<Object> fpQueue,
                                                         List<FrequentPattern> outputFP) {
        while (true) {
            try {
                FrequentPattern fp = (FrequentPattern) fpQueue.first(); // fp to be refined
                fpQueue.dequeue();
                for (Attribute currentAttribute : data.getAttributeSet()) {
                    boolean found = false;
                    for (Item currentItem : fp)
                        // different
                        // form attributes already involved into the items
                        // of fp
                        if (currentItem.getAttribute().equals(currentAttribute)) {
                            found = true;
                            break;
                        }
                    if (!found) // data.getAttribute(i) is not involve into an item of fp
                    {
                        if (currentAttribute instanceof DiscreteAttribute) {
                            for (int j = 0; j < ((DiscreteAttribute) currentAttribute)
                                    .getNumberOfDistinctValues(); j++) {
                                DiscreteItem item = new DiscreteItem(
                                        (DiscreteAttribute) currentAttribute,
                                        ((DiscreteAttribute) (currentAttribute)).getValue(j));
                                FrequentPattern newFP = refineFrequentPattern(fp, item); // generate refinement
                                newFP.setSupport(newFP.computeSupport(data));
                                if (newFP.getSupport() >= minSup) {
                                    fpQueue.enqueue(newFP);
                                    // System.out.println(newFP);
                                    outputFP.add(newFP);
                                }
                            }
                        } else if (currentAttribute instanceof ContinuousAttribute) {
                            ContinuousAttribute continuousAttribute = (ContinuousAttribute) currentAttribute;
                            Iterator<Float> e = continuousAttribute.iterator();

                            if (e.hasNext()) {
                                float min = e.next();

                                while (e.hasNext()) {

                                    float max = e.next();
                                    ContinuousItem item = new ContinuousItem(continuousAttribute, new Interval(min, max));
                                    FrequentPattern newFP = refineFrequentPattern(fp, item); // generate refinement
                                    newFP.setSupport(newFP.computeSupport(data));
                                    if (newFP.getSupport() >= minSup) {
                                        fpQueue.enqueue(newFP);
                                        outputFP.add(newFP);
                                    }
                                    min = max;
                                }
                            }
                        } else
                            throw new ClassCastException();
                    }
                }
            } catch (EmptyQueueException e) {
                break;
            }
        }
        return outputFP;
    }

    /**
     * Tale metodo di occupa di serializzare l'oggetto <b>FrequentPatternMiner</b> e memorizzarlo in un file il cui nome è passato come parametro
     *
     * @param nomeFile = nome del file da creare con all'ìnterno memorizzata la serializzazione dell'oggetto.
     * @throws IOException Eccezione che viene sollevata qualora ci fossero errori durante la seralizzazione dell'oggetto.
     */
    public void salva(String nomeFile) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(nomeFile));
        out.writeObject((FrequentPatternMiner) this); // Write byte stream to file system.
        out.close();
    }

    /**
     * Tale metodo si occupa di leggere e restituire l'oggetto come è memorizzato nel file, il cui nome è passato come parametro.
     *
     * @param nomeFile
     * @return Oggetto recuperato dalla deserializzazione
     * @throws FileNotFoundException  Eccezione che viene sollevata qualora il file non venisse trovato
     * @throws IOException            Eccezione che viene sollevata qualora ci fossero errori durante la seralizzazione dell'oggetto.
     * @throws ClassNotFoundException
     */
    public static FrequentPatternMiner carica(String nomeFile)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(nomeFile));
        FrequentPatternMiner fp = (FrequentPatternMiner) in.readObject();
        in.close();
        return fp;
    }

    /**
     * <p>Tale metodo si occupa di ordinare <b>{@link mining.FrequentPatternMiner#outputFP}</b> usando il comparatore associato alla classe {@link mining.FrequentPattern}</p>
     */
    private void sort() {
        Collections.sort(outputFP);
    }
}
