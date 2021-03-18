package DotsAndBoxes;
import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("noedge")
public class NoEdge {
	
	@Param(0)
	private int x; 
	@Param(1)
	private int y;
	@Param(2)
	private int horizontal; // 0 -> verticale    1 -> orizzontale 

    public NoEdge() {

    }

    public NoEdge(int x, int y, int horizontal) {
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
	
	public int getHorizontal() {	//Metodo che dovrebbe verificare se una linea � disposta in orizzontale
        return horizontal;
    }
	
	public void setHorizontal(int horizontal) {
		this.horizontal = horizontal;
	}

	
	@Override
    public String toString() {
        return ((horizontal==1 ? "H " : "V ") + x + " " + y);
    }

}
