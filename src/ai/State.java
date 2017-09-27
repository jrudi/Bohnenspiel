package ai;

import java.util.ArrayList;

public class State{
	private int[] board;
	private boolean myTurn;
	private boolean first;
	private int myPoints,enemyPoints;
	private State parent;
	private ArrayList<State> children;

    /**
     * erzeugt ein State-Objekt im Startzustand
     * TODO Spieler1 oder Spieler2 festlegen
     */
	State(){
        myPoints=0;
		enemyPoints=0;
		myTurn=false;
		first=false;
        board = new int[]{6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6};
        parent = null;
		}

    /**
     * Erzeugt einen Knoten durch einen Zug
     * @param parent    Der Vorhergehende Zustand
     * @param move      Der Zug, der gemacht wurde (hier keine Überprüfung der Gültigkeit!
     */
    private State(State parent, int move){
	    this.myPoints=parent.myPoints;
	    this.enemyPoints=parent.enemyPoints;
	    this.myTurn=!parent.myTurn;
	    this.first=parent.first;
	    this.board= parent.board.clone();
	    this.parent=parent;
	    this.move(move);
    }

    /**
     * Zug auf einem Feld n
     * @param n das Feld, dessen Bohnen verteilt werden sollen
     */
    private void move(int n){
	        int n_amount = board[n];
	        for(int i=n+1;i<n+1+n_amount;i++){
                board[i%12]++;

            }
            int check = n+n_amount%12;
            board[n]=0;

            //Die Schleife geht rückwärts durch alle Felder und schaut ob die Bohnen entnommen werden können
            //Wenn ja, werden die Bohnen auf das zugeghörige Konto übertragen und das Feld auf 0 gesetzt
            //Beachte: -3%12==-3, Math.floorMod(-3,12)==9

            while(true){
                check = Math.floorMod(check,12);
	            if(board[check]==2||board[check]==4||board[check]==6){
	                if(this.myTurn){
	                    myPoints+=board[check];
                    }else{
	                    enemyPoints+=board[check];
                    }
                    board[check]=0;
	                check--;
                }else{
	                break;
                }
            }
    }

    /**
     * gibt die Liste der möglichen Kind-Knoten zurück
     * @return
     */
    ArrayList<State> getChildren() {
        return children;
    }

    /**
     * gibt die Summe der noch verbleibenden Bohnen zurück
     * TODO summe ist eigentlich auch nur 72-myScore-enemyScore
     * @return
     */
    private int getSum(){
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum+=board[i];
        }
        return sum;
    }

    /**
     * Zeigt an, ob Spiel fertig ist
     * TODO Wenn ein Spieler keinen Zug mehr machen kann, ist das Spiel auch zuende
     * @return
     */
    boolean isTerminal() {
        return this.getSum()==0;
    }

    /**
     * Summe der Bohnen auf den eigenen Spielfeldern
     * @return
     */
    private int sumMyBeans(){
        int sum=0;
        int start=first?0:6;

            for(int i=start;i<start+6;i++){
                sum+=this.board[i];
            }

        return sum;
    }

    /**
     * Summe der Bohnen auf gegnerischen Spielfeldern
     * @return
     */
    private int sumEnemyBeans(){
        int sum=0;
        int start=first?6:0;

        for(int i=start;i<start+6;i++){
            sum+=this.board[i];
        }

        return sum;
    }

    /**
     * gibt den Wert der Heuristik für den aktuellen Spielzustand zurück
     * TODO keine Ahnung ob das ne sinvolle Heuristik ist
     * @return
     */
    int getHeuristic() {
     return myPoints-enemyPoints + (sumMyBeans()-sumEnemyBeans())/2;

    }

    /**
     * Erzeugt Kind-Knoten für alle möglichen Spielzüge des aktuellen Spielers
     */
    private void makeAllMoves(){
        this.children = new ArrayList<>();
        int start = this.first==this.myTurn?0:6;
        int finish = start+6;
            for (int i=start; i < finish; i++) {
                if(this.board[i]!=0){
                    children.add(new State(this,i));
                }
            }
    }

    /**
     * gibt das Spielfeld in String-form zurück
     * Sieht ungefähr so aus:
     * 6 | 6 | 6 | 6 | 6 | 7
     * 0 | 7 | 7 | 7 | 7 | 7
     * Player1: 0, Player2: 0, HEUR: 1
     * @return
     */
    @Override
    public String toString() {
        String s = board[11]+" | " + board[10]+" | " + board[9]+" | " + board[8]+" | " + board[7]+" | " + board[6];
        s+="\n" + board[0]+" | " + board[1]+" | " + board[2]+" | " + board[3]+" | " + board[4]+" | " + board[5];
        s+="\n Player1: " + myPoints + ", Player2: " + enemyPoints + ", HEUR: " + this.getHeuristic();
        return s;

    }

    public static void main(String[] args){
        State s = new State();
        s.makeAllMoves();
        for(State x:s.children){
            System.out.println(x);

        }
        System.out.println("____");
        State z = s.children.get(0);
        z.makeAllMoves();
        for(State x:z.children){
            System.out.println(x);

        }
    }
}

