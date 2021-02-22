package DotsAndBoxes;

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
import it.unical.mat.embasp.specializations.dlv.desktop.DLVDesktopService;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;

public class ASPSolver {

	
	private static int[][] hedge;
	private static int[][] vedge;
	
	private static int N=9;
	
	private static String encodingResource="encodings/DotsAndBoxes";
	
	private static Handler handler;
	
	
	
	public ASPSolver() {
		
		//Creazione dell'oggetto handler che si occuper� di gestire l'invocazione 
		//del sistema ASP da utilizzare
				
		//Se si esegue la demo su Windows 64bit scommentare la seguente istruzione:
		//handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2.exe"));

				//Se si esegue la demo su Linux 64bit scommentare la seguente istruzione:
				//handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2"));
				
		//Se si esegue la demo su MacOS 64bit scommentare la seguente istruzione:
		handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2.mac_7"));
				
				//In alternativa, aggiungere nella cartella lib l'eseguibile di DLV2 
				//appropriato in base al proprio sistema e sostituire a "nome_exe_dlv2" 
				//il nome dell'eseguibile di DLV2 nella seguente istruzione e scommentarla
				//handler = new DesktopHandler(new DLV2DesktopService("lib/nome_exe_dlv2"));
				
		//Specifichiamo i fatti in input, in questo caso tramite oggetti della 
		//classe Cell che viene prima registrata all'ASPMapper
		try {
			ASPMapper.getInstance().registerClass(Edge.class);
		} catch (ObjectNotValidException | IllegalAnnotationException e1) {
			e1.printStackTrace();
		}


	}
	
	public Edge getNextMove(Board b, int color) {
		System.out.println("sono in getNextMove");
		InputProgram facts= new ASPInputProgram();
		Edge ritorna=null;
//		facts.addObjectInput(new Edge(i, j, horizontal));

		
		//Aggiungiamo all'handler i fatti 
		//handler.addProgram(facts);
		

		//Specifichiamo il programma logico tramite file
		InputProgram encoding= new ASPInputProgram();
		encoding.addFilesPath(encodingResource);
				
		//Aggiungiamo all'handler il programma logico
		handler.addProgram(encoding);
				
		//L'handler invoca DLV2 in modo SINCRONO dando come input il programma logico e i fatti
		Output o =  handler.startSync();
				
		//Analizziamo l'answer set
		AnswerSets answersets = (AnswerSets) o;
		for(AnswerSet a:answersets.getAnswersets()){ 
			System.out.println("AS");
			try {
				for(Object obj:a.getAtoms()){
					//Scartiamo tutto cio' che non e' un oggetto della classe Edge
					if(!(obj instanceof Edge)) continue;
					//Convertiamo in un oggetto della classe Edge e impostiamo il valore di ogni cella 
					Edge edge= (Edge) obj;					
					ritorna= edge;	
					System.out.println("--------- edge "+edge);
					return ritorna;
//					if(edge.getHorizontal()) {
//						hedge=b.gethEdge();
//						hedge[edge.getX()][edge.getY()]=b.BLACK;
//						b.sethEdge(hedge);
//					}
//					else {
//						vedge = b.getvEdge();
//						vedge[edge.getX()][edge.getY()]=b.BLACK;
//						b.setvEdge(vedge);
//					}
				}
			} catch (Exception e) {
						e.printStackTrace();
					} 
		}
		return ritorna;
	}
	
}
