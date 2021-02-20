package DotsAndBoxes;
import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("edge")
public class Edge {
	//Classe che dovrebbe indicare le linee della griglia
	
	@Param(0)
	private int x; 
	@Param(1)
	private int y;
	@Param(2)
	private boolean horizontal;

    Edge() {
        x = y = -1;
        horizontal = false;
    }

    Edge(int x, int y, boolean horizontal) {
        this.x = x;
        this.y = y;
        this.horizontal = horizontal;
    }

   
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public boolean getHorizontal() {	//Metodo che dovrebbe verificare se una linea � disposta in orizzontale
        return horizontal;
    }
	
	public void setHorizontal(boolean horizontal) {
		this.horizontal = horizontal;
	}

	
	@Override
    public String toString() {
        return ((horizontal ? "H " : "V ") + x + " " + y);
    }

}
