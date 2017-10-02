package ai;

import java.util.ArrayList;

public class State {
  int[] board;
  private boolean myTurn;
  private boolean first;
  private int myPoints, enemyPoints;
  private ArrayList<State> children;
  public int lastMove, ctr;

  /**
   * erzeugt ein State-Objekt im Startzustand TODO Spieler1 oder Spieler2 festlegen
   */
  State(boolean f) {
    myPoints = 0;
    enemyPoints = 0;
    myTurn = f;
    first = f;
    board = new int[] {6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6};
    lastMove = -1;
    ctr = 0;
  }

  /**
   * Erzeugt einen Knoten durch einen Zug
   * 
   * @param parent Der Vorhergehende Zustand
   * @param move Der Zug, der gemacht wurde (hier keine Überprüfung der Gültigkeit!
   */
  State(State parent, int move) {
    this.myPoints = parent.myPoints;
    this.enemyPoints = parent.enemyPoints;
    this.first = parent.first;
    this.ctr = parent.ctr + 1;
    this.board = parent.board.clone();
    this.myTurn = !parent.myTurn;
    this.move(move);
    this.lastMove = move;

  }

  /**
   * Zug auf einem Feld n
   * 
   * @param n das Feld, dessen Bohnen verteilt werden sollen
   */
  private void move(int n) {
    int n_amount = board[n];
    board[n] = 0;

    for (int i = n + 1; i < n + 1 + n_amount; i++) {
      board[i % 12]++;

    }
    int check = n + n_amount % 12;

    // Die Schleife geht rückwärts durch alle Felder und schaut ob die Bohnen entnommen werden
    // können
    // Wenn ja, werden die Bohnen auf das zugeghörige Konto übertragen und das Feld auf 0 gesetzt
    // Beachte: -3%12==-3, Math.floorMod(-3,12)==9

    while (true) {
      check = Math.floorMod(check, 12);
      if (board[check] == 2 || board[check] == 4 || board[check] == 6) {
        if (!this.myTurn) {
          myPoints += board[check];
        } else {
          enemyPoints += board[check];
        }
        board[check] = 0;
        check--;
      } else {
        break;
      }
    }
  }

  /**
   * gibt die Liste der möglichen Kind-Knoten zurück
   * 
   * @return
   */
  ArrayList<State> getChildren() {
    if (children == null) {
      this.makeAllMoves();
    }
    return children;
  }

  int getPointDifference() {
    return this.myPoints - this.enemyPoints;
  }

  /**
   * gibt die Summe der noch verbleibenden Bohnen zurück TODO summe ist eigentlich auch nur
   * 72-myScore-enemyScore
   * 
   * @return
   */
  private int getSum() {
    int sum = 0;
    for (int i = 0; i < 12; i++) {
      sum += board[i];
    }
    return sum;
  }

  /**
   * Zeigt an, ob Spiel fertig ist
   * 
   * @return
   */
  boolean isTerminal() {
    boolean terminal =
        this.myTurn && this.sumMyBeans() == 0 || !this.myTurn && this.sumEnemyBeans() == 0;

    return terminal;
  }

  void transferFinalPoints() {
    if (this.isTerminal()) {
      if (this.myTurn && this.sumMyBeans() == 0) {
        enemyPoints += sumEnemyBeans();
      } else {
        myPoints += sumMyBeans();
      }
    }
  }

  /**
   * Summe der Bohnen auf den eigenen Spielfeldern
   * 
   * @return
   */
  private int sumMyBeans() {
    int sum = 0;
    int start = first ? 0 : 6;

    for (int i = start; i < start + 6; i++) {
      sum += this.board[i];
    }

    return sum;
  }

  /**
   * Summe der Bohnen auf gegnerischen Spielfeldern
   * 
   * @return
   */
  private int sumEnemyBeans() {
    int sum = 0;
    int start = first ? 6 : 0;

    for (int i = start; i < start + 6; i++) {
      sum += this.board[i];
    }

    return sum;
  }

  /**
   * gibt den Wert der Heuristik für den aktuellen Spielzustand zurück TODO keine Ahnung ob das ne
   * sinvolle Heuristik ist
   * 
   * @return
   */

  // Aushungern
  int getHeuristic() {
    int nullFeld = 0;
    int nichtNullFeld = 0;
    int startGegner = first ? 6 : 0;
    int start = first ? 0 : 6;

    // Eigenes Feld ueberwachen
    for (int i = start; i < this.board.length; i++) {
      if (board[i] == 0) {
        nichtNullFeld += 5;
      }
    }

    // Gegner Feld ueberwachen
    for (int i = startGegner; i < this.board.length; i++) {
      if (board[i] == 0) {
        nullFeld += 5;
      }
    }
    // Wenn KI beginnt und eigenes Feld aushungert
    if (startGegner == 0 && nichtNullFeld >= 15) {
      return myPoints + (sumMyBeans() - sumEnemyBeans()) + nullFeld + nichtNullFeld;
    }
    // Wenn KI beginnt
    else if (startGegner == 0) {
      return myPoints + (sumMyBeans() - sumEnemyBeans()) + nullFeld;
    }
    // Gegner beginnt und eigenes Feld hungert aus.
    else if (nichtNullFeld >= 15) {
      return myPoints + 72 - enemyPoints * 2 + nichtNullFeld;
    }
    // Gegner beginnt
    else {
      return myPoints + 72 - enemyPoints * 2;
    }

  }

  /**
   * Erzeugt Kind-Knoten für alle möglichen Spielzüge des aktuellen Spielers
   */
  private void makeAllMoves() {
    this.children = new ArrayList<>();
    int start = this.first == this.myTurn ? 0 : 6;
    int finish = start + 6;
    for (int i = start; i < finish; i++) {
      if (this.board[i] != 0) {
        children.add(new State(this, i));
      }
    }
  }

  /**
   * gibt das Spielfeld in String-form zurück Sieht ungefähr so aus: 6 | 6 | 6 | 6 | 6 | 7 0 | 7 |
   * 7 | 7 | 7 | 7 Player1: 0, Player2: 0, HEUR: 1
   * 
   * @return
   */
  @Override
  public String toString() {
    String s = board[11] + " | " + board[10] + " | " + board[9] + " | " + board[8] + " | "
        + board[7] + " | " + board[6];
    s += "\n" + board[0] + " | " + board[1] + " | " + board[2] + " | " + board[3] + " | " + board[4]
        + " | " + board[5];
    s += "\nAI: " + myPoints + ", ENEMY: " + enemyPoints + ", HEUR: " + this.getHeuristic()
        + ", Spieler" + (myTurn ? "1" : "2");

    return s;

  }

  /*
   * public static void main(String[] args){ State s = new State(true);
   * 
   * for (int i = 0; i < 100; i++) { System.out.println(s); if(!s.isTerminal()){ s.makeAllMoves();
   * 
   * 
   * s = s.children.get(0);} System.out.println(i); } }
   */
}

