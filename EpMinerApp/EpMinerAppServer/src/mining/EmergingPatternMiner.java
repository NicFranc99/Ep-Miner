package mining;

import data.Data;
import data.EmptySetException;

import java.util.List;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Iterator;
import java.io.*;

/**
 * @author nicola francavilla
 * <p>Classe concreta che modella la scoperta di un Emerging Pattern a partire dalla lista di Frequent Pattern</p>
 */
public class EmergingPatternMiner implements Iterable<EmergingPattern>, Serializable {
    /**
     * <p>Lista che contiene riferimenti a oggetti istanza della classe <b>EmergingPattern</b> che definiscono il Pattern </p>
     *
     * @uml.property name="epList"
     */
    private List<EmergingPattern> epList = new LinkedList<EmergingPattern>();

    /**
     * <p>Si scandiscono tutti i frequent Patternin fpList, per ognugno di essi si calcola il grow rate usando dataBackGround e se tale
     * valore è <U>Maggiore uguale</U> di minG allora il pattern viene aggiunto ad epList.
     * Alla fine viene ordinato l'emergingPattern rispetto al GrowRate dei pattern. {@link EmergingPatternMiner#sort()}</p>
     *
     * @param dataBackground = Dataset di background su cui calcolare il growRate di tutti i pattern presenti in fpList considerando come minimo GrowRate "minG"
     * @param fpList         = lista di pattern di cui calcolare il growRate in dataBackGround
     * @param minG           = minimo supporto da considerare nel calcolo
     * @throws EmptySetException = Caso in cui si passa a tale metodo un dataset vuoto
     */
    public EmergingPatternMiner(Data dataBackground, FrequentPatternMiner fpList, float minG) throws EmptySetException {
        if (dataBackground.getNumberOfExamples() == 0)
            throw new EmptySetException("dataBackGround Empty!");
        Iterator<FrequentPattern> e = fpList.iterator();
        while (e.hasNext()) {
            FrequentPattern fp = e.next();
            try {
                EmergingPattern ep = computeEmergingPattern(dataBackground, fp, minG);
                epList.add(ep);
            } catch (EmergingPatternException ex) {
                ex.fillInStackTrace();
            }
        }
        sort();
    }

    @Override
    public Iterator<EmergingPattern> iterator() {
        return epList.iterator();
    }

    /**
     * <p>Si ottiene da fp il suo supporto relativo al dataset target. Si calcola il supporto di fp relativo al dataset di BackGround.
     * Si calcola il growRate come rapporto dei due supporti.</p>
     *
     * @param dataBackground = Insieme delle transazioni di background (databackground)
     * @param fp             = pattern frequente (fp) di cui calcolare il growRate
     * @return GrowRate di fp
     */
    private float computeGrowRate(Data dataBackground, FrequentPattern fp) {
        float backgroundSupport = fp.computeSupport(dataBackground);
        float targetSupport = fp.getSupport();
        return targetSupport / backgroundSupport;
    }

    /**
     * <p>Verifica che il growRate di fp sia <U>Maggiore di minGR</U> e in tal caso crea un oggetto istanza di EmergingPattern da fp</p>
     *
     * @param dataBackground = Insieme di transazioni di backGround (dataBackground)
     * @param fp             = Frequent Pattern
     * @param minGR          = minimo GrowRate
     * @return Restituisce l'emerging pattern creato da fp se la condizione sul GrowRate è soddisfatta, null altrimenti
     * @throws EmergingPatternException = Caso in cui il il glowRate calcolato non sia maggiore uguale di minGR.
     */
    private EmergingPattern computeEmergingPattern(Data dataBackground, FrequentPattern fp, float minGR) throws EmergingPatternException {
        float glowRate = computeGrowRate(dataBackground, fp);
        if (glowRate >= minGR)
            return new EmergingPattern(fp, glowRate);
        else
            throw new EmergingPatternException(glowRate + "isn't >= of " + minGR);
    }

    /**
     * <p>Tale metodo si occupa di ordinare <b>{@link mining.EmergingPatternMiner#epList}</b> usando il comparatore associato alla classe {@link mining.EmergingPattern}</p>
     */
    private void sort() {
        Collections.sort(epList, new ComparatorGrowRate());
    }

    /**
     * <p>Scandisce <b>epList</b> al fine di concatenare in un'unica stringa tutte le stringhe che rappresentano i pattern emergenti letti</p>
     *
     * @return Stringa rappresentante il calore di <b>epList</b>
     */
    public String toString() {
        Iterator<EmergingPattern> e = epList.iterator();
        String result = "Emerging patterns" + "\n";
        int i = 1;
        while (e.hasNext()) {
            result = result + String.valueOf(i) + ":" + (e.next()).toString() + "\n";
            i++;
        }
        return result;
    }

    /**
     * Tale metodo di occupa di serializzare l'oggetto <b>EmergingPatternMiner</b> e memorizzarlo in un file il cui nome è passato come parametro
     *
     * @param nomeFile = nome del file da creare con all'ìnterno memorizzata la serializzazione dell'oggetto.
     * @throws IOException Eccezione che viene sollevata qualora ci fossero errori durante la seralizzazione dell'oggetto.
     */
    public void salva(String nomeFile) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(nomeFile));
        out.writeObject(this); //Write byte stream to file system.
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
    public static EmergingPatternMiner carica(String nomeFile) throws FileNotFoundException, IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(nomeFile));
        EmergingPatternMiner ep = (EmergingPatternMiner) in.readObject();
        in.close();
        return ep;
    }
}
