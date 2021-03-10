package DotsAndBoxes;
import java.awt.Point;
import java.util.ArrayList;

public class Board implements Cloneable {

    final static int RED = 0;
    final static int BLUE = 1;
    final static int BLACK = 2;
    final static int BLANK = 3;

    private int[][] vEdge;					//Griglia linee orizzontali
    private int[][] hEdge;					//Griglia delle linee verticali 
    private int[][] box;					//Griglia del gioco		
    
	private int dim, redScore, blueScore;		//n=numero righe e colonna.
	
	public static Board instance=null;
	
	private Edge ultimaMossa;
	private ArrayList<Edge> mosseFatte = new ArrayList<Edge>();
	
    public Board(int n) {
        vEdge = new int[n-1][n];			
        hEdge = new int[n][n-1];
        box = new int[n-1][n-1];	
        fill(vEdge,BLANK);					//Indica che tutte le linee orizz. sono vuote
        fill(hEdge,BLANK);					//Indica che tutte le linee verticali sono vuote
        fill(box,BLANK);					//griglia vuota
        this.dim = n-1;
        redScore = blueScore = 0;
    }
    
    public void addUltimaMossa(Edge e) {
    	this.mosseFatte.add(e);
    	this.ultimaMossa=e;
    }
    
    public Edge getUltimaMossa() {
    	return this.ultimaMossa;
    }
    
    public Boolean checkMossa(Edge e) {
    	if(mosseFatte.contains(e)) 
    		return false;
    	return true;
    }
    

    
   /* public static Board getInstance() {
    	if (instance==null)
    		instance= new Board();
    	return instance;
    }*/

    public int[][] gethEdge() {
		return vEdge;
	}

	public void sethEdge(int[][] hEdge) {
		this.vEdge = hEdge;
	}

	public int[][] getvEdge() {
		return hEdge;
	}

	public void setvEdge(int[][] vEdge) {
		this.hEdge = vEdge;
	}

	public int[][] getBox() {
		return box;
	}

	public void setBox(int[][] box) {
		this.box = box;
	}

    public int getDim() {
		return dim;
	}

	public void setDim(int n) {
		this.dim = n;
	}
	
	
    private void fill(int[][] array, int val) {		//Metodo che ci permette di riempire una griglia
        for(int i=0; i<array.length; i++)
            for(int j=0; j<array[i].length; j++)
                array[i][j]=val;
    } 


    public int getRedScore() {
        return redScore;
    }

    public int getBlueScore() {
        return blueScore;
    }

    public int getScore(int color) {
        if(color == RED) 
        	return redScore;
        else 
        	return blueScore;
    }

    public static int toggleColor(int color) { //Inverte il colore usato. Serve per i turni dei partecipanti?	
        if(color == RED)
            return BLUE;
        else
            return RED;
    }
    

    public ArrayList<Edge> getMosseDisponibili() {	
        ArrayList<Edge> mosse = new ArrayList<Edge>();
        for(int i=0; i<=dim; i++)
            for(int j=0; j<dim; j++)
                if(hEdge[i][j] == BLANK) {
                	//System.out.println("mossa disponibile: "+i+j+1);
                    mosse.add(new Edge(i,j,1));
                }
        for(int i=0; i<dim; i++)
            for(int j=0; j<=dim; j++)
                if(vEdge[i][j] == BLANK) {
                	//System.out.println("mossa disponibile: "+i+j+0);
                    mosse.add(new Edge(i,j,0));
                }
        return mosse;
    }

    //Il metodo setHEdge serve ad aggiungere una linea e ad assegnare un eventuale punteggio al giocatoer
    //I due if ci permettono di controllare anche i limiti della matrice
    public ArrayList<Point> setVEdge(int x, int y, int color) { 
    	//System.out.println("Sono in setVedge, x "+x+" + y "+y);
    	System.out.println("Dim VEDGE ----> "+ dim);
    	vEdge[x][y]=BLACK;
        ArrayList<Point> quadrati = new ArrayList<Point>();
        if(y<(dim) && hEdge[x][y]==BLACK && hEdge[x+1][y]==BLACK && vEdge[x][y+1]==BLACK) {
            box[x][y]=color;
            quadrati.add(new Point(x,y));
            if(color == RED) 
            	redScore++;
            else
            	blueScore++;
        }
        if(y>0 && hEdge[x][y-1]==BLACK && hEdge[x+1][y-1]==BLACK && vEdge[x][y-1]==BLACK) {
            box[x][y-1]=color;
            quadrati.add(new Point(x,y-1));
            if(color == RED) 
            	redScore++;
            else 
            	blueScore++;
        }
        return quadrati;
    }

    //il metodo torna i quadrati creati con l'aggiunta dell'arco orizzontale in pos X,Y.
    public ArrayList<Point> setHEdge(int x, int y, int color) {
    	//System.out.println("Sono in hEdge, la dim in board �: "+dim);
    	//System.out.println("Sono in HEdge: x: "+x+", y:"+y);

        hEdge[x][y]=BLACK;
        ArrayList<Point> quadrati = new ArrayList<Point>();
        if(x<(dim) && vEdge[x][y]==BLACK && vEdge[x][y+1]==BLACK && hEdge[x+1][y]==BLACK) {
            box[x][y]=color;
            quadrati.add(new Point(x,y));
            if(color == RED) redScore++;
            else blueScore++;
        }
        if(x>0 && vEdge[x-1][y]==BLACK && vEdge[x-1][y+1]==BLACK && hEdge[x-1][y]==BLACK) {
            box[x-1][y]=color;
            quadrati.add(new Point(x-1,y));
            if(color == RED) redScore++;
            else blueScore++;
        }
        return quadrati;
    }

    //Condizione di stop del gioco
    public boolean isComplete() {
    	if ((redScore + blueScore) == (dim* dim)) {
    		System.out.println("Condizione di fine gioco verificata");
    		return true;
    	}
        return false;
    }
    
    //Ritorna il vincitore
    public int getWinner() {
        if(redScore > blueScore) 
        	return RED;
        else if(redScore < blueScore) 
        	return BLUE;
        else 
        	return BLANK;
    }
    
    void stampaBoard() {
    	System.out.println("*********** BOARD ************");
    	for(int i=0; i<=dim; i++)
            for(int j=0; j<dim; j++)
               if(hEdge[i][j] == BLACK) {
               	System.out.println("Edge: "+i+", "+j+",1");
              }
   	 for(int i=0; i<dim; i++)
            for(int j=0; j<=dim; j++)
               if(vEdge[i][j] == BLACK) {
               	System.out.println("Edge: "+i+", "+j+",0");
               }
    }

    public int getTotalEdge() {
    	int cont=0;
    	 for(int i=0; i<=dim; i++)
             for(int j=0; j<dim; j++)
                if(hEdge[i][j] == BLACK) {
                	//System.out.println("Stampo l'arco in pos: "+i+", "+j);
                	cont++;
               }
    	 for(int i=0; i<dim; i++)
             for(int j=0; j<=dim; j++)
                if(vEdge[i][j] == BLACK) {
                	//System.out.println("Stampo l'arco in pos: "+i+", "+j);
                	cont++;
                }
        return cont;
    }

}
