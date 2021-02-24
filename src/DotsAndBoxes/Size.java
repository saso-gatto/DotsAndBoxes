package DotsAndBoxes;
import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("size")
public class Size {

	@Param(0)
	private int dim;

	public int getDim() {
		return dim;
	}

	public void setDim(int dim) {
		this.dim = dim;
	}

	public Size() {}
	
	public Size(int dim) {
		super();
		this.dim = dim;
	} 
	
	
}
