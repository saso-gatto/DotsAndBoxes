package DotsAndBoxes;
import java.awt.Point;
import java.util.ArrayList;

public class Board implements Cloneable {

    final static int RED = 0;
    final static int BLUE = 1;
    final static int BLACK = 2;
    final static int BLANK = 3;

    private int[][] hEdge;					//Griglia linee orizzontali
    private int[][] vEdge;					//Griglia delle linee verticali 
    private int[][] box;					//Griglia del gioco		
    
    public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	private int n, redScore, blueScore;		//n=numero righe e colonna.

    public Board(int n) {
        hEdge = new int[n-1][n];			
        vEdge = new int[n][n-1];
        box = new int[n-1][n-1];	
        fill(hEdge,BLANK);					//Indica che tutte le linee orizz. sono vuote
        fill(vEdge,BLANK);					//Indica che tutte le linee verticali sono vuote
        fill(box,BLANK);					//griglia vuota
        this.n = n;
        redScore = blueScore = 0;
    }

    public int[][] gethEdge() {
		return hEdge;
	}

	public void sethEdge(int[][] hEdge) {
		this.hEdge = hEdge;
	}

	public int[][] getvEdge() {
		return vEdge;
	}

	public void setvEdge(int[][] vEdge) {
		this.vEdge = vEdge;
	}

	public int[][] getBox() {
		return box;
	}

	public void setBox(int[][] box) {
		this.box = box;
	}

	public Board clone() {
        Board cloned = new Board(n);

        for(int i=0; i<(n-1); i++)
            for(int j=0; j<n; j++)
                cloned.hEdge[i][j] = hEdge[i][j];

        for(int i=0; i<n; i++)
            for(int j=0; j<(n-1); j++)
                cloned.vEdge[i][j] = vEdge[i][j];

        for(int i=0; i<(n-1); i++)
            for(int j=0; j<(n-1); j++)
                cloned.box[i][j] = box[i][j];

        cloned.redScore = redScore;
        cloned.blueScore = blueScore;

        return cloned;
    }

    private void fill(int[][] array, int val) {		//Metodo che ci permette di riempire una griglia
        for(int i=0; i<array.length; i++)
            for(int j=0; j<array[i].length; j++)
                array[i][j]=val;
    }

    public int getSize() { return n; }

    public int getRedScore() {
        return redScore;
    }

    public int getBlueScore() {
        return blueScore;
    }

    public int getScore(int color) {
        if(color == RED) return redScore;
        else return blueScore;
    }

    public static int toggleColor(int color) { //Inverte il colore usato. Serve per i turni dei partecipanti?	
        if(color == RED)
            return BLUE;
        else
            return RED;
    }

    public ArrayList<Edge> getAvailableMoves() {	
        ArrayList<Edge> ret = new ArrayList<Edge>();
        for(int i=0; i<(n-1);i++)
            for(int j=0; j<n; j++)
                if(hEdge[i][j] == BLANK)
                    ret.add(new Edge(i,j,1));
        for(int i=0; i<n; i++)
            for(int j=0; j<(n-1); j++)
                if(vEdge[i][j] == BLANK)
                    ret.add(new Edge(i,j,0));
        return ret;
    }

    //Il metodo setHEdge serve ad aggiungere una linea e ad assegnare un eventuale punteggio al giocatoer
    //I due if ci permettono di controllare anche i limiti della matrice
    public ArrayList<Point> setHEdge(int x, int y, int color) { 
        hEdge[x][y]=BLACK;
        ArrayList<Point> ret = new ArrayList<Point>();
        if(y<(n-1) && vEdge[x][y]==BLACK && vEdge[x+1][y]==BLACK && hEdge[x][y+1]==BLACK) {
            box[x][y]=color;
            ret.add(new Point(x,y));
            if(color == RED) redScore++;
            else blueScore++;
        }
        if(y>0 && vEdge[x][y-1]==BLACK && vEdge[x+1][y-1]==BLACK && hEdge[x][y-1]==BLACK) {
            box[x][y-1]=color;
            ret.add(new Point(x,y-1));
            if(color == RED) redScore++;
            else blueScore++;
        }
        return ret;
    }

    //Il metodo setVEdge serve ad aggiungere una linea e ad assegnare un eventuale punteggio al giocatore
    //I due if ci permettono di controllare anche i limiti della matrice

    public ArrayList<Point> setVEdge(int x, int y, int color) {
        vEdge[x][y]=BLACK;
        ArrayList<Point> ret = new ArrayList<Point>();
        if(x<(n-1) && hEdge[x][y]==BLACK && hEdge[x][y+1]==BLACK && vEdge[x+1][y]==BLACK) {
            box[x][y]=color;
            ret.add(new Point(x,y));
            if(color == RED) redScore++;
            else blueScore++;
        }
        if(x>0 && hEdge[x-1][y]==BLACK && hEdge[x-1][y+1]==BLACK && vEdge[x-1][y]==BLACK) {
            box[x-1][y]=color;
            ret.add(new Point(x-1,y));
            if(color == RED) redScore++;
            else blueScore++;
        }
        return ret;
    }

    //Condizione di stop del gioco
    public boolean isComplete() {
        return (redScore + blueScore) == (n - 1) * (n - 1);
    }
    
    //Ritorna il vincitore
    public int getWinner() {
        if(redScore > blueScore) return RED;
        else if(redScore < blueScore) return BLUE;
        else return BLANK;
    }
    
    //Reinizializza la board
    public Board getNewBoard(Edge edge, int color) {
        Board ret = clone();
        if(edge.getHorizontal()==1)
            ret.setHEdge(edge.getX(), edge.getY(), color);
        else
            ret.setVEdge(edge.getX(), edge.getY(), color);
        return ret;
    }

    //Metodo che pu� esser usato per sapere quante mosse hanno fatto i giocatori. 
    //Conta il numero di linee nere presente nella griglia
    private int getEdgeCount(int i, int j) {
        int count = 0;
        if(hEdge[i][j] == BLACK) count++;
        if(hEdge[i][j+1] == BLACK) count++;
        if(vEdge[i][j] == BLACK) count++;
        if(vEdge[i+1][j] == BLACK) count++;
        return count;
    }
    
    //Metodo che ci permette di controllare il numero di punti 
    public int getBoxCount(int nSides) {
        int count = 0;
        for(int i=0; i<(n-1); i++)
            for(int j=0; j<(n-1); j++) {
                if(getEdgeCount(i, j) == nSides)
                    count++;
            }
        return count;
    }

}
