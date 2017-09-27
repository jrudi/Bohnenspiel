package ai;

import java.util.Random;

public class BohnenspielAI {

	Random rand = new Random();
	State current;
	/**
	* @param enemyIndex The index that refers to the field chosen by the enemy in the last action.If this value is 0, than the AI is the starting player and has to specify the first move.
	* @return Return The index that refers to the field of the action chosen by this AI.
	*/

	public int getMove(int enemyIndex) {
		int index = 0;
		// have to choose the first move
        if(enemyIndex == 0){
            current = new State();
        }else if (enemyIndex == -1) {
			index = rand.nextInt(6) + 1;
		}
		// enemy acted and i have to react
		else if (enemyIndex > 0 && enemyIndex <= 6) {
			index = rand.nextInt(6) + 7;
		}
		else if (enemyIndex > 6 && enemyIndex <= 12) {
			index = rand.nextInt(6) + 1;
		}
		return index;
	}

    /**
     * Anfang einer A-B-Pruning Methode
     * TODO Knoten mit bester Heuristik wiederbekommen. Bisher wird nur dessen Heuristik-Wert returned.
     *
     * @param node Der Knoten der untersucht wird
     * @param depth Die aktuell verbleibende Suchtiefe
     * @param a bisheriges alpha
     * @param b bisheriges beta
     * @param max Soll Maximiert werden(eigener Zug) oder minimiert(gegnerischer Zug)
     * @return der Heuristic Wert des behandelten Knotens, wenn er ein Leafknoten ist(Suchtiefe==0)
     */
    public int alphabeta(State node, int depth,int a,int b, boolean max) {
        if (depth == 0 || node.isTerminal()) {
            return node.getHeuristic();
        }
        int v = 0;
        if (max) {
            v = Integer.MIN_VALUE;
            for (State child : node.getChildren()) {
                v = Math.max(v, alphabeta(child, depth - 1, a, b, false));
                a = Math.max(a, v);
                if (b >= a) {
                    break;
                }
            }
        } else {
            v = Integer.MAX_VALUE;
            for (State child : node.getChildren()) {
                v = Math.min(v, alphabeta(child, depth - 1, a, b, true));
                b = Math.min(b, v);
                if (b <= a) {
                    break;
                }

            }
        }
        return v;
    }
}