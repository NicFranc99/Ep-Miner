package utility;

/**
 * @param <T>
 * @author Map tutor
 * <p>Classe utilizzata per modellare una coda generica (di tipo qualunque)</p>
 */
public class Queue<T> {
    /**
     * @uml.property name = "begin" indica l'inizio della coda
     */
    private Record begin = null;
    /**
     * @uml.property name = "end" indica la fine della coda
     */
    private Record end = null;

    private class Record {

        Object elem;

        Record next;

        Record(Object e) {
            this.elem = e;
            this.next = null;
        }
    }

    /**
     * <p>Metodo che controlla se la Queue corrente è vuota o contiene elementi al suo interno
     *
     * @return <font color="green"> True </font> = coda vuota <font color="green">false</font> = altrimenti</p>
     */
    public boolean isEmpty() {
        return this.begin == null;
    }

    public void enqueue(Object e) {
        if (this.isEmpty())
            this.begin = this.end = new Record(e);
        else {
            this.end.next = new Record(e);
            this.end = this.end.next;
        }
    }

    /**
     * <p>
     *
     * @return Restituisce il primo elemento della coda
     * @throws EmptyQueueException = nel caso in cui si tenta di leggere l'elemento di una coda vuota(priva di elementi).</p>
     */
    public Object first() throws EmptyQueueException {
        if (this.isEmpty())
            throw new EmptyQueueException("The queue is empty!");

        return this.begin.elem;
    }

    /**
     * <p>Elimina l'elemento che si trova in cima alla coda
     *
     * @throws EmptyQueueException = nel caso in cui sitenta di eliminare l'elemento di una coda vuota (prima di elementi).</p>
     */
    public void dequeue() throws EmptyQueueException {
        if (this.begin == this.end) {
            if (this.begin == null)
                throw new EmptyQueueException("The queue is empty!");

            else
                this.begin = this.end = null;
        } else {
            begin = begin.next;
        }

    }

}