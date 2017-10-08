package ai;

import java.util.ArrayList;
import java.util.Random;

public class BohnenspielAI {

	Random rand = new Random();
	State current;
	final static int DEPTH = 11;
	/**
	* @param enemyIndex The index that refers to the field chosen by the enemy in the last action.If this value is 0, than the AI is the starting player and has to specify the first move.
	* @return Return The index that refers to the field of the action chosen by this AI.
	*/

	public int getMove(int enemyIndex) {
        if(enemyIndex == 0 || enemyIndex ==-1) {
            current = new State(true);
        }else {
            if (this.current == null) {
                this.current = new State(false);
            }
            this.current = new State(this.current, enemyIndex - 1);
        }
        int result = init();
        this.current = new State(this.current,result);
        System.out.println("__________________");
        System.out.println("MOVE: " + result + " COUNT: " + current.ctr);
        System.out.println(this.current.toString());
        return result+1;
	}

    /**
     * Max Methode der Min-Max-Suche, findet den maximalen Wert für alle möglichen Züge und ruft auf diesen Knoten min() auf
     * @param node  der zu untersuchende Knoten
     * @param depth die noch zu bestreitende Suchtiefe
     * @param a das bisherige alpha
     * @param b das bisherige beta
     * @return der maximierte Wert
     */
    public int max(State node,int depth,int a,int b){
        int v = Integer.MIN_VALUE;
        if(depth==0){
            return node.getHeuristic();
        }else if(node.isTerminal()){
            node.transferFinalPoints();
            //System.out.println("TERMINAL: " + (node.ctr-current.ctr));
            return node.getPointDifference()>0?500:-500;

        }
        for (State child : node.getChildren()) {
            v = Math.max(v, min(child, depth - 1, a, b));
            a = Math.max(a, v);
            if (b <= a) {
                break;
            }
        }
        return v;
    }

/**
 * Min Methode der Min-Max-Suche, findet den minimalen Wert für alle möglichen Züge und ruft auf diesen Knoten max() auf
 * @param node  der zu untersuchende Knoten
 * @param depth die noch zu bestreitende Suchtiefe
 * @param a das bisherige alpha
 * @param b das bisherige beta
 * @return der minimierte Wert
 */
    public int min(State node,int depth,int a,int b){
        int v = Integer.MAX_VALUE;
        if(depth==0){
            return node.getHeuristic();
        }else if(node.isTerminal()){
            node.transferFinalPoints();
            return node.getPointDifference()>0?500:-500;
        }
        for (State child : node.getChildren()) {
            v = Math.min(v, max(child, depth - 1, a, b));
            b = Math.min(b, v);
            if (b <= a) {
                break;
            }
        }
        return v;
    }

    /**
     * Initiiert die Min-Max-Suche, indem die möglichen Züge als States in eine Liste eingefügt werden. Danach wird auf jedem max() aufgerufen.
     * Parallel dazu gibt es eine Liste, in die am gleichen Index das Ergebnis von max() eingetragen wird.
     * Zuletzt werden die Werte verglichen und der Zug mit dem Index des Maximums wird gewählt.
     * @return Zahl zwischen 0 und 11, steht für den zu wählenden Zug
     */

    public int init(){
        ArrayList<State> list = current.getChildren();
        ArrayList<Integer> val = new ArrayList<>();
        int maxval = Integer.MIN_VALUE;
        int i=-1;
        if(Math.random()>0.99){
            System.out.println("*");
            return list.get((int)(Math.random()*list.size())).lastMove;
        }
        for(State s:list){
            val.add(min(s,BohnenspielAI.DEPTH,Integer.MIN_VALUE,Integer.MAX_VALUE));
        }
        for(Integer x:val){
            if(x>maxval){
                i=val.indexOf(x);
                maxval = x;
            }
        }
        return list.get(i).lastMove;
    }
}