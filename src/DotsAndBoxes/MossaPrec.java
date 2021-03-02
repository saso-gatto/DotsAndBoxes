package DotsAndBoxes;
import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("mossaPrec")
public class MossaPrec {

	@Param(0)
	private int mossaPrec;

	public int getMossaPrec() {
		return mossaPrec;
	}

	public void setMossaPrec(int mossaPrec) {
		this.mossaPrec = mossaPrec;
	}

	public MossaPrec() {
		
	}
	
	public MossaPrec(int mossaPrec) {
		super();
		this.mossaPrec = mossaPrec;
	} 
	
	
}
