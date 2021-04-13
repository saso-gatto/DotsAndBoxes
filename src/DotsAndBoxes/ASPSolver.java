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
	private InputProgram var;

	
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
			ASPMapper.getInstance().registerClass(NoEdge.class);
			ASPMapper.getInstance().registerClass(Size.class);
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
		if(b.getMosseFatte().size()!= 0) {
			try {
				ArrayList<Edge> mosse = b.getMosseFatte();
				for (Edge e : mosse) {
					facts.addObjectInput(e);
				}
			} catch (Exception e) { e.printStackTrace(); }
			b.svuotaMosse();
		}		
	}
	
		public boolean check(Board b,Edge e) {
		ArrayList <NoEdge> mosse = b.getMosseDisponibili();
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
	
	public void aggiungiMosseDisponibili(InputProgram var, Board b) {
		ArrayList<NoEdge> mosse = b.getMosseDisponibili();
		for (NoEdge e : mosse) {
			try {
				var.addObjectInput(new NoEdge(e.getX(),e.getY(),e.getHorizontal()));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	
	public Edge getNextMove(Board b, int color) {
		if (this.start) {
			try {
				facts.addObjectInput(new Size(b.getDim()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			var = new ASPInputProgram();
			handler.addProgram(facts);
			handler.addProgram(var);

			this.start=false;
		}
		this.aggiungiFatto(b);
		//this.stampaAS();
		this.aggiungiMosseDisponibili(var,b);
		Edge ritorna=null;
		
		Output o =  handler.startSync();
		AnswerSets answersets = (AnswerSets) o;
		
		if (answersets.getAnswersets().size() <= 0) {
			System.out.println("No AS");
			System.out.println();
		}
		
		System.out.println(answersets.getOptimalAnswerSets());
		for(AnswerSet a: answersets.getOptimalAnswerSets()) {	
			try {
				System.out.println("Stampa AS");
				System.out.println(a.toString());
				
				for(Object obj:a.getAtoms()){

					
					//Scartiamo tutto cio' che non e' un oggetto della classe Assegno
					if(!(obj instanceof Assegno)) continue;
					
					Assegno mossa = (Assegno) obj;
					ritorna= new Edge(mossa.getX(), mossa.getY(), mossa.getHorizontal());					
				
					if(!check(b, ritorna)) {
						//System.out.println("Non aggiungo edge - continue");
						continue;
					}		
					facts.addObjectInput(ritorna);
					
					var.clearAll();
					return ritorna;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return ritorna;
	}
	
	
	public void stampaAS () {
		System.out.println("******** STAMPA AS ********");
		System.out.println(facts.getPrograms());
	}
}
