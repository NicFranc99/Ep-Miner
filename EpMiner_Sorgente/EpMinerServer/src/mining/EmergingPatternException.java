package mining;

/**
 * @author nicola francavilla
 * <p>Classe utilizzata per modellare l'eccezione che occorre qualora il pattern corrente <U>non soddisfa</U> la condizione di minimo Grow Rate.</p>
 */
final class EmergingPatternException extends Exception {
    EmergingPatternException(String message) {
        super(message);
    }
}
