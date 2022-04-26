package mining;

import java.util.Comparator;

/**
 * @author nicola francavilla
 * <p>Classe utilizzata per definire un comparatore, ossia per il confronto tra due emergingPattern rispetto al loro <b>GrowRate</b></p>
 */
class ComparatorGrowRate implements Comparator<EmergingPattern> {
    public int compare(EmergingPattern em1, EmergingPattern em2) {
        return em1.getGrowRate() < em2.getGrowRate() ? -1 :
                em1.getGrowRate() > em2.getGrowRate() ? +1 : 0;
    }
}
