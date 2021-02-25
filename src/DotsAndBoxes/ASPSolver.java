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

	
	private static int[][] hedge;
	private static int[][] vedge;
	
	private static int N=9;
	
	private static String encodingResource="encodings/DotsAndBoxes";
	
	private static Handler handler;
	
	private InputProgram facts;
	
	
	public ASPSolver() {

		if(System.getProperty("os.name").equals("Mac OS X")) {
			handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2.mac_7"));
			System.out.println("Sistema operativo Mac OS X");
		}
		else if(System.getProperty("os.name").equals("Windows 10")) {
			handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2.exe"));
			System.out.println("Sistema operativo Windows 10");
		}
		else {
			System.out.println("Errore, sistema operativo non riconosciuto con IDLV");
		}
				
				
		//Specifichiamo i fatti in input, in questo caso tramite oggetti della 
		//classe Edge che viene prima registrata all'ASPMapper
		try {
			ASPMapper.getInstance().registerClass(Edge.class);
		} catch (ObjectNotValidException | IllegalAnnotationException e1) {
			e1.printStackTrace();
		}

		facts= new ASPInputProgram();
	/*		
		try {
			facts.addObjectInput(new Edge(1,1,1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	*/		
		//Aggiungiamo all'handler i fatti 
		handler.addProgram(facts);
		
		//Specifichiamo il programma logico tramite file
		InputProgram encoding= new ASPInputProgram();
		encoding.addFilesPath(encodingResource);
				
		//Aggiungiamo all'handler il programma logico
		handler.addProgram(encoding);
		
	}
	
//Metodo che possiamo utilizzare per aggiungere eventuali celle delle matrici?
	public void trovaFatti(Board b)  {
		this.N=b.getN();
		int [][]hEdge=b.gethEdge();
		int [][]vEdge=b.getvEdge();
		for(int i=0; i<(N-1);i++)
            for(int j=0; j<N; j++)
					try {
						facts.addObjectInput(new Edge(i, j, 1));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        for(int i=0; i<N; i++)
            for(int j=0; j<(N-1); j++)
					try {
						facts.addObjectInput(new Edge(i, j, 0));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	}
	
	public boolean check(Board b,Edge e) {
		ArrayList <Edge> mosse = b.getAvailableMoves();
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
		
		try {
			facts.addObjectInput(new Size(b.getSize()));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Edge ritorna=null;
	
		ASPInputProgram var = new ASPInputProgram();

		//L'handler invoca DLV2 in modo SINCRONO dando come input il programma logico e i fatti
		Output o =  handler.startSync();
				
		//Analizziamo l'answer set
		AnswerSets answersets = (AnswerSets) o;
		
		if (answersets.getAnswersets().size() <= 0)
			System.out.println("No AS");
		int cont = 0;
		for(AnswerSet a:answersets.getAnswersets()){ 
			
			try {
				for(Object obj:a.getAtoms()){
					cont++;
					System.out.println("--------- AS -------------");
					
					//Scartiamo tutto cio' che non e' un oggetto della classe Edge
					if(!(obj instanceof Edge)) continue;
					//Convertiamo in un oggetto della classe Edge e impostiamo il valore di ogni cella 
					Edge edge= (Edge) obj;					
					
					if(!check(b, edge)) continue;
					
					ritorna= edge;
					System.out.println("--------- edge "+edge);
									
					var.addObjectInput(new Edge(edge.getX(), edge.getY(), edge.getHorizontal()));
					handler.addProgram(var);
					var.clearAll();
					return ritorna;
				}
			} catch (Exception e) {
						e.printStackTrace();
					} 
		}
//		ritorna = new Edge(1,1,true);
		System.out.println("cont "+ cont);
		return ritorna;
	}
	
}
