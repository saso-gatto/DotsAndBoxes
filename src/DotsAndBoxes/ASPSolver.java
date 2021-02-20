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
/*		Board d = new Board();
		//Visualizziamo la griglia iniziale del Sudoku
//		displayMatrix();
//		
		//Creazione dell'oggetto handler che si occuper� di gestire l'invocazione 
		//del sistema ASP da utilizzare
		
		//Se si esegue la demo su Windows 64bit scommentare la seguente istruzione:
		handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2.exe"));

		//Se si esegue la demo su Linux 64bit scommentare la seguente istruzione:
		//handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2"));
		
		//Se si esegue la demo su MacOS 64bit scommentare la seguente istruzione:
		//handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2.mac_7"));
		
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
		InputProgram facts= new ASPInputProgram();
		for(int i=0;i<N;i++){
			for(int j=0;j<N;j++){
				if(sudokuMatrix[i][j]!=0){
					try {
						facts.addObjectInput(new Cell(i, j, sudokuMatrix[i][j]));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}	
			}			
		}
		
		//Aggiungiamo all'handler i fatti 
		handler.addProgram(facts);
		
		//Specifichiamo il programma logico tramite file
		InputProgram encoding= new ASPInputProgram();
		encoding.addFilesPath(encodingResource);
		
		//Aggiungiamo all'handler il programma logico
		handler.addProgram(encoding);
		
		//L'handler invoca DLV2 in modo SINCRONO dando come input il programma logico e i fatti
		Output o =  handler.startSync();
		
		//Analizziamo l'answer set che in quest caso e' unico e che rappresenta la soluzione
		//del Sudoku e aggiorniamo la matrice
		AnswerSets answersets = (AnswerSets) o;
		for(AnswerSet a:answersets.getAnswersets()){
			try {
				for(Object obj:a.getAtoms()){
					//Scartiamo tutto cio' che non e' un oggetto della classe Cell
					if(!(obj instanceof Cell)) continue;
					//Convertiamo in un oggetto della classe Cell e impostiamo il valore di ogni cella 
					//nella matrice rappresentante la griglia del Sudoku
					Cell cell= (Cell) obj;					
					sudokuMatrix[cell.getRow()][cell.getColumn()] = cell.getValue();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
			
		}

		//In alternativa l'handler puo' invocare DLV2 in modo ASINCRONO.
		//Scommentare la seguente linea e commentare le linee 89-110
		//handler.startAsync(new MyCallback(sudokuMatrix));
	}
	
//	private static void displayMatrix() {
//		for(int i=0;i<N;i++){
//			for(int j=0;j<N && i%3==0;j++){
//				System.out.print("----");
//				if(j==N-1)System.out.println();
//			}
//			for(int j=0;j<N;j++){
//				System.out.print((j%3==0)?"||":"  ");
//				System.out.print(sudokuMatrix[i][j]+" ");
//				if(j==N-1)System.out.print("||");
//			}
//			System.out.println();
//		}
//		for(int j=0;j<N;j++){
//			System.out.print("----");
//		}
//		System.out.println();
//*/	}

}
