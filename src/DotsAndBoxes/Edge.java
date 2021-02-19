package DotsAndBoxes;
public class Edge {
	//Classe che dovrebbe indicare le linee della griglia
    private int x, y;
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

    public boolean isHorizontal() {	//Metodo che dovrebbe verificare se una linea è disposta in orizzontale
        return horizontal;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return ((horizontal ? "H " : "V ") + x + " " + y);
    }

}
