package DotsAndBoxes;

import java.util.ArrayList;

import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.languages.IllegalAnnotationException;
import it.unical.mat.embasp.languages.ObjectNotValidException;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;

public class ASPSolver {

	private static String encodingResource="encodings/DotsAndBoxes";
	private static Handler handler;
	private boolean start;
	
	private InputProgram facts;
	
	public ASPSolver() {

		if(System.getProperty("os.name").equals("Mac OS X")) {
			handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2.mac_7"));
			//handler.addOption(new OptionDescriptor("-n 0"));
			System.out.println("Sistema operativo Mac OS X");
		}
		else if(System.getProperty("os.name").equals("Windows 10")) {
			handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2.exe"));
			//handler.addOption(new OptionDescriptor("-n 0"));
			System.out.println("Sistema operativo Windows 10");
		}
		else {
			System.out.println("Errore, sistema operativo non riconosciuto con IDLV");
		}
				
				
		//classe Edge che viene prima registrata all'ASPMapper
		try {
			ASPMapper.getInstance().registerClass(Edge.class);
			ASPMapper.getInstance().registerClass(Size.class);
			ASPMapper.getInstance().registerClass(MossaPrec.class);
			ASPMapper.getInstance().registerClass(Assegno.class);

		} catch (ObjectNotValidException | IllegalAnnotationException e1) {
			e1.printStackTrace();
		}

		facts= new ASPInputProgram();
		this.start=true;
		InputProgram encoding= new ASPInputProgram();
		encoding.addFilesPath(encodingResource);

		handler.addProgram(encoding);
		
	}
	
	
	
	public void aggiungiFatto(Board b) {
		try {
			facts.addObjectInput(new MossaPrec(b.getTotalEdge()));
		} catch (Exception e) { e.printStackTrace(); }
		
		
		if(b.getMosseFatte().size()!= 0) {
			System.out.println("Aggiungo ai fatti l'ultima mossa");
			
			try {
				ArrayList<Edge> mosse = b.getMosseFatte();
				for (Edge e : mosse) {
					System.out.println("Sono in aggiungi fatto: "+e.getX()+","+e.getY()+","+e.getHorizontal());
					facts.addObjectInput(e);
				}
			} catch (Exception e) { e.printStackTrace(); }
			b.svuotaMosse();
		}
		
		
	}
	
		public boolean check(Board b,Edge e) {
		ArrayList <Edge> mosse = b.getMosseDisponibili();
		for (int i = 0; i<mosse.size(); i++) {
			int x=mosse.get(i).getX();
			int y=mosse.get(i).getY();
			int h=mosse.get(i).getHorizontal();
			if (x==e.getX() && y==e.getY() && h==e.getHorizontal()) {
				return true;
			}
		}
		return false;
	}
	
	
	public Edge getNextMove(Board b, int color) {
		System.out.println("sono in getNextMove");
		if (this.start) {
			try {
				facts.addObjectInput(new Size(b.getDim()));
				facts.addObjectInput(new MossaPrec());
			} catch (Exception e) {
				e.printStackTrace();
			}
			handler.addProgram(facts);
			this.start=false;
		}
		
		this.aggiungiFatto(b);
		//this.stampaAS();
		
		Edge ritorna=null;
		
		Output o =  handler.startSync();
		AnswerSets answersets = (AnswerSets) o;
		
		if (answersets.getAnswersets().size() <= 0) {
			System.out.println("No AS");
			System.out.println();
		}
			
		int cont = 0;

		//for(AnswerSet a:answersets.getAnswersets()){ 
		System.out.println("STAMP ANSWERSET");
		//System.out.println(answersets.getOutput());
		//System.out.println(answersets.getAnswerSetsString());
		System.out.println(answersets.getOptimalAnswerSets());
		for(AnswerSet a: answersets.getOptimalAnswerSets()) {	
			try {
				System.out.println("Stampa AS");
				System.out.println(a.toString());
				
				for(Object obj:a.getAtoms()){
					System.out.println("--------- AS -------------");
					
					//Scartiamo tutto cio' che non e' un oggetto della classe Edge
					if(!(obj instanceof Assegno)) continue;
					Assegno mossa = (Assegno) obj;
					Edge edge= new Edge(mossa.getX(), mossa.getY(), mossa.getHorizontal());					
					System.out.println(edge.getX()+" "+edge.getY()+" "+edge.getHorizontal());	
					
					if(!check(b, edge)) {
						System.out.println("Non aggiungo edge - continue");
						continue;
					}
					cont++;
					ritorna= edge;			
					
					facts.addObjectInput(edge);
					//handler.addProgram(facts);

					return ritorna;
				}
			} catch (Exception e) {
						e.printStackTrace();
					} 
		}
		System.out.println("cont "+ cont);
		return ritorna;
	}
	
	
	public void stampaAS () {
		System.out.println("******** STAMPA AS ********");
		System.out.println(facts.getPrograms());
	}
}
