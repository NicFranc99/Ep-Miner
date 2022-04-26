package data;

import mining.FrequentPatternMiner;


/**
 * @author nicola francavilla
 * <p>Classe utilizzata per modellare l'eccezione che occorre quando l'insieme di Training fosse vuoto (Non contiene transazioni/Esempi). Tale eccezione
 * è sollevata nei costruttori di <b>{@link mining.FrequentPatternMiner#FrequentPatternMiner(Data, float)}</b> e <b>{@link mining.EmergingPatternMiner#EmergingPatternMiner(Data, FrequentPatternMiner, float)}</b></p>
 */
public class EmptySetException extends Exception {
    public EmptySetException(String message) {
        super(message);
    }
}
