package ai;

import java.util.ArrayList;
import java.util.Random;

public class BohnenspielAI {

	Random rand = new Random();
	State current;
	final static int DEPTH = 2;
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
        System.out.println("MOVE: " + result);
        System.out.println(this.current.toString());
        return result+1;
	}

    public int max(State node,int depth,int a,int b){
        int v = Integer.MIN_VALUE;
        if(depth==0){
            return node.getHeuristic();
        }
        for (State child : node.getChildren()) {
            v = Math.max(v, min(child, depth - 1, a, b));
            a = Math.max(a, v);
            if (b >= a) {
                break;
            }
        }
        //System.out.println(v+" max");
        return v;
    }
    public int min(State node,int depth,int a,int b){
        int v = Integer.MAX_VALUE;
        if(depth==0){
            return node.getHeuristic();
        }
        for (State child : node.getChildren()) {
            v = Math.min(v, max(child, depth - 1, a, b));
            b = Math.min(b, v);
            if (b <= a) {
                break;
            }
        }
        //System.out.println(v + " min");
        return v;
    }

    public int init(){
        ArrayList<State> list = current.getChildren();
        ArrayList<Integer> val = new ArrayList<>();
        int maxval = Integer.MIN_VALUE;
        int i=-1;
        for(State s:list){
            val.add(min(s,BohnenspielAI.DEPTH,Integer.MAX_VALUE,Integer.MIN_VALUE));
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