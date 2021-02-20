package DotsAndBoxes;
import javax.swing.*;

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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    private int n;
 //   private GameSolver redSolver, blueSolver;
    private String redName, blueName;

    private JFrame frame;
    private JLabel modeError, sizeError;

    String[] players = {"Seleziona Giocatore", "Human", "Random Player", "Greedy Player", "Minimax Search", "Alpha-Beta Pruning","Monte Carlo Search"};
    private JRadioButton[] sizeButton;

    JComboBox<String> redList, blueList;
    ButtonGroup sizeGroup;

    
    
    
	private static String encodingResource="encodings/DotsAndBoxes";
	
	private static Handler handler;
    
	
	
    public Main() {

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        redList = new JComboBox<String>(players);
        blueList = new JComboBox<String>(players);

        sizeButton = new JRadioButton[8];
        sizeGroup = new ButtonGroup();
        for(int i=0; i<8; i++) {
            String size = String.valueOf(i+3-1);
            sizeButton[i] = new JRadioButton(size + " x " + size);
            sizeGroup.add(sizeButton[i]);
        }
    }

    private JLabel getEmptyLabel(Dimension d) {
        JLabel label = new JLabel();
        label.setPreferredSize(d);
        return label;
    }

    private boolean startGame;
//
//    private GameSolver getSolver(int level) {
//        if(level == 1) return new RandomSolver();
//        else if(level == 2) return new GreedySolver();
//        else if(level == 3) return new MinimaxSolver();
//        else if(level == 4) return new AlphaBetaSolver();
//        else if(level == 5) return new MCSolver();
//        else return null;
//    }

    private ActionListener submitListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int rIndex = redList.getSelectedIndex();
            int bIndex = blueList.getSelectedIndex();
            if(rIndex==0 || bIndex==0) {
                modeError.setText("Seleziona i giocatori per continuare.");
                return;
            }
            else {
                modeError.setText("");
                redName = players[rIndex];
                blueName = players[bIndex];
//                if(rIndex > 1) redSolver = getSolver(rIndex - 1);
//                if(bIndex > 1) blueSolver = getSolver(bIndex - 1);
            }
            for(int i=0; i<8; i++) {
                if(sizeButton[i].isSelected()) {
                    n = i+3;
                    startGame = true;
                    return;
                }
            }
            sizeError.setText("Seleziona la grandezza della griglia per continuare.");
        }
    };

    public void initGUI() {


    	//Se si esegue la demo su MacOS 64bit scommentare la seguente istruzione:
    			handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2.mac_7"));
    			
    	//Se si esegue la demo su Windows 64bit scommentare la seguente istruzione:
  		//handler = new DesktopHandler(new DLV2DesktopService("lib/dlv2.exe"));    	
    	
    		//Specifichiamo i fatti in input, in questo caso tramite oggetti della 
    		//classe Cell che viene prima registrata all'ASPMapper
    			try {
    				ASPMapper.getInstance().registerClass(Cell.class);
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
    			//Visualizziamo la griglia cosi' ottenuta
    			// displayMatrix();
    			
    			//In alternativa l'handler puo' invocare DLV2 in modo ASINCRONO.
    			//Scommentare la seguente linea e commentare le linee 89-110
    			//handler.startAsync(new MyCallback(sudokuMatrix));
    		
    	

        JPanel grid = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel titleLabel = new JLabel(new ImageIcon(getClass().getResource("title.png")));
        grid.add(titleLabel, constraints);

        ++constraints.gridy;
        grid.add(getEmptyLabel(new Dimension(500,25)), constraints);

        modeError = new JLabel("", SwingConstants.CENTER);
        modeError.setForeground(Color.RED);
        modeError.setPreferredSize(new Dimension(500, 25));
        ++constraints.gridy;
        grid.add(modeError, constraints);

        JPanel modePanel = new JPanel(new GridLayout(2, 2));
        modePanel.setPreferredSize(new Dimension(400, 50));
        modePanel.add(new JLabel("<html><font color='red'> Giocatore 1:", SwingConstants.CENTER));
        modePanel.add(new JLabel("<html><font color='blue'> Giocatore 2:", SwingConstants.CENTER));
        modePanel.add(redList);
        modePanel.add(blueList);
        redList.setSelectedIndex(0);
        blueList.setSelectedIndex(0);
        ++constraints.gridy;
        grid.add(modePanel, constraints);

        ++constraints.gridy;
        grid.add(getEmptyLabel(new Dimension(500,25)), constraints);

        sizeError = new JLabel("", SwingConstants.CENTER);
        sizeError.setForeground(Color.RED);
        sizeError.setPreferredSize(new Dimension(500, 25));
        ++constraints.gridy;
        grid.add(sizeError, constraints);

        ++constraints.gridy;
        JLabel messageLabel = new JLabel("Seleziona la grandezza della griglia:");
        messageLabel.setPreferredSize(new Dimension(400, 50));
        grid.add(messageLabel, constraints);

        JPanel sizePanel = new JPanel(new GridLayout(4, 2));
        sizePanel.setPreferredSize(new Dimension(400, 100));
        for(int i=0; i<8; i++)
            sizePanel.add(sizeButton[i]);
        sizeGroup.clearSelection();
        ++constraints.gridy;
        grid.add(sizePanel, constraints);

        ++constraints.gridy;
        grid.add(getEmptyLabel(new Dimension(500, 25)), constraints);

        JButton submitButton = new JButton("Inizia il Gioco");
        submitButton.addActionListener(submitListener);
        ++constraints.gridy;
        grid.add(submitButton, constraints);

        ++constraints.gridy;
        grid.add(getEmptyLabel(new Dimension(500, 25)), constraints);

        frame.setContentPane(grid);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        startGame = false;
        while(!startGame) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        new GamePlay(this, frame, n, redName, blueName);
    }

    public static void main(String[] args) {
        new Main().initGUI();
    }

}
