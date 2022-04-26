package mining;

/**
 * @author nicola francavilla
 * <p>Classe concreta che modella un pattern Emergente</p>
 */
public class EmergingPattern extends FrequentPattern {
    /**
     * <p>Grow rate del pattern emergente
     *
     * @uml.property name="growrate"</p>
     */
    private float growrate;

    /**
     * <p>Chiama il costruttore della superClasse passandogli <b>fp</b> e inizializza il membro <b>Growrate</b> con l'argomento del costruttore
     *
     * @param fp       = pattern
     * @param growrate = Grow rate del pattern</p>
     */
    EmergingPattern(FrequentPattern fp, float growrate) {
        super(fp);
        this.growrate = growrate;
    }

    /**
     * <p>Restituisce il valore del membro <b>GrowRate</b>
     *
     * @return grow rate del pattern </p>
     */
    float getGrowRate() {
        return growrate;
    }

    /**
     * <p>Si crea e restituisce la stringa che rappresenta il pattern, il suo supporto e il suo GrowRate
     *
     * @return Stringa contenente il pattern emergente nella forma <b>"pattern [supporto][growrate]"</b></p>
     */
    public String toString() {
        String result = new String();
        result = super.toString() + "[" + growrate + "]";
        return result;
    }
}
