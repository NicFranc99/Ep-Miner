package utility;

/**
 * @author nicola francavilla
 * <p>Classe utilizzata per modellare l'eccezione che occore qualora si cerca di leggere/cancellare ({@link Queue#first()} / {@link Queue#dequeue()}) da una coda vuota.</p>
 */
public final class EmptyQueueException extends Exception {
     EmptyQueueException(String message) {
        super(message);
    }
}
