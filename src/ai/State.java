package ai;

public class State{
	public int[] board;
	public boolean myTurn;
	public boolean first;
	public int myPoints,enemyPoints;
	public State parent;

	public State(){
        myPoints=0;
		enemyPoints=0;
		myTurn=false;
		first=false;
        board = new int[]{6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6};
        parent = null;
		}

    public void move(int n,boolean myturn){
	        int n_amount = board[n];
	        for(int i=n+1;n<n+1+n_amount;i++){
	            board[i%12]++;
            }
            int check = n+1+n_amount%12;

            //Die Schleife geht rückwärts durch alle Felder und schaut ob die Bohnen entnommen werden können
            //Wenn ja, werden die Bohnen auf das zugeghörige Konto übertragen und das Feld auf 0 gesetzt
            //Beachte: -3%12==-3, Math.floorMod(-3,12)==9
            //askdnakljdsasjd
            //asdölajshdöalsdh
            while(true){
                check = Math.floorMod(check,12);
	            if(board[check]==2||board[check]==4||board[check]==6){
	                if(myturn){
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

    public int getSum(){
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum+=board[i];
        }
        return sum;
    }


	}

