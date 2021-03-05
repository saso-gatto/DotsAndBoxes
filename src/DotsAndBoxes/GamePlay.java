package DotsAndBoxes;
import javax.swing.*;

import it.unical.mat.embasp.base.Handler;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class GamePlay {
	
    Color arancione = Color.decode("#fc7e3f");
    Color azzurro = Color.decode("#02a3d0");
	
    private final static int size = 8;	//Spessore linee
    private final static int dist = 50;	//Lunghezza di Edge

    private int dim;
    private Board board;
    private int turn;
    private boolean mouseEnabled;
    private ASPSolver blueSolver,redSolver;
   
    
    String redName, blueName;
    Main parent;

    private JLabel[][] vEdge, hEdge, box;
    private boolean[][] isSetVEdge, isSetHEdge;

    private JFrame frame;
    private JLabel redScoreLabel, blueScoreLabel, statusLabel;

   
    
    private MouseListener mouseListener = new MouseListener() {
    	
    	//Il metodo click
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            if(!mouseEnabled) 
            	return;
            processMove(getSource(mouseEvent.getSource()));
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {

        }

        //Tale metodo ci permette di colorare una determinata linea attraverso le coordinate del mouse
        //Viene applicato quando si passa sopra ad una linea per far vedere quale mossa sta facendo il giocatore
//        @Override
        public void mouseEntered(MouseEvent mouseEvent) {
//            if(!mouseEnabled) return;
//            Edge location = getSource(mouseEvent.getSource());
//            int x=location.getX(), y=location.getY();
//            System.out.println("MouseEvent,x: "+x+", y:"+y+" horizontal: "+location.getHorizontal());
//            if(location.getHorizontal()==1) {
//                if(isSetHEdge[x][y]) return;
//                hEdge[x][y].setBackground((turn == Board.RED) ? arancione : azzurro);
//            }
//            else {
//                if(isSetVEdge[x][y]) return; //se in pos x,y HEdge ï¿½ colorata, non la colorare.
//                vEdge[x][y].setBackground((turn == Board.RED) ? arancione : azzurro);
//            }
        }

//        //Al rilascio del mouse viene colorata la linea in pos X,Y del mouse.
//        @Override
        public void mouseExited(MouseEvent mouseEvent) {
//            if(!mouseEnabled) return;
//            Edge location = getSource(mouseEvent.getSource());
//            int x=location.getX(), y=location.getY();
//            if(location.getHorizontal()==1) {
//                if(isSetHEdge[x][y]) return;
//                hEdge[x][y].setBackground(Color.WHITE);
//            }
//            else {
//                if(isSetVEdge[x][y]) return;
//                vEdge[x][y].setBackground(Color.WHITE);
//            }
        }
    };

    private void processMove(Edge location) {
        int x=location.getX(), y=location.getY();

        System.out.println("Sono in processMove:x "+x+", y:"+y+" horizontal: "+location.getHorizontal());

        ArrayList<Point> quadrati;

        if(location.getHorizontal()==1) {
        	//if(isSetHEdge[x][y]) return;
            quadrati = board.setHEdge(x,y,turn); //Otteniamo il quadrato/i che abbiamo aggiungendo l'hedge in X,Y
            this.hEdge[x][y].setBackground(Color.BLACK);
            //isSetHEdge[x][y] = true;
        }
        else {
            //if(isSetVEdge[x][y]) return;
        	quadrati = board.setVEdge(x,y,turn);
            this.vEdge[x][y].setBackground(Color.BLACK);
           // isSetVEdge[x][y] = true;
        }

        for(Point p : quadrati)
            box[p.x][p.y].setBackground((turn == Board.RED) ? arancione : azzurro);

        redScoreLabel.setText(String.valueOf(board.getRedScore()));
        blueScoreLabel.setText(String.valueOf(board.getBlueScore()));

        if(board.isComplete()) {
            int winner = board.getWinner();
            if(winner == Board.RED) {
                statusLabel.setText("Il Giocatore 1 ha vinto!");
                statusLabel.setForeground(arancione);
            }
            else if(winner == Board.BLUE) {
                statusLabel.setText("Il Giocatore 2 ha vinto!");
                statusLabel.setForeground(azzurro);
            }
            else {
                statusLabel.setText("Pareggio!");
                statusLabel.setForeground(Color.BLACK);
            }
        }

        if(quadrati.isEmpty()) {
            if(turn == Board.RED) {
                turn = Board.BLUE;
                statusLabel.setText("Turno del Giocatore 2");
                statusLabel.setForeground(azzurro);
            }
            else {
                turn = Board.RED;
                statusLabel.setText("Turno del Giocatore 1");
                statusLabel.setForeground(arancione);
            }
        }

    }

    private void manageGame() {
        while(!board.isComplete()) {
            if((turn==Board.BLUE && this.blueSolver == null) || (turn==Board.RED && this.redSolver==null)) {
                mouseEnabled = true;
            }
            else {
                mouseEnabled = false;
                if (turn==Board.BLUE)
                	processMove(this.blueSolver.getNextMove(board, turn));
                else if (turn==Board.RED)
                	processMove(this.redSolver.getNextMove(board, turn));
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //Object conterrà le coordinate dell'edge da aggiungere
    private Edge getSource(Object object) {
        for(int i=0; i<dim; i++)
            for(int j=0; j<dim-1; j++)
                if(hEdge[i][j] == object) {
                    return new Edge(i,j,1);
                }
        for(int i=0; i<dim-1; i++)
            for(int j=0; j<dim; j++)
                if(vEdge[i][j] == object){
                    return new Edge(i,j,0);
                }
        System.out.println("non ho trovato un oggetto - PROBLEMA");
        return new Edge();
    }

    private JLabel getHorizontalEdge() {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(dist, size));
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        label.setOpaque(true);
        label.addMouseListener(mouseListener);
        return label;
    }

    private JLabel getVerticalEdge() {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(size, dist));
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        label.setOpaque(true);
        label.addMouseListener(mouseListener);
        return label;
    }

    private JLabel getDot() {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(size, size));
        label.setBackground(Color.BLACK);
        label.setOpaque(true);
        return label;
    }

    private JLabel getBox() {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(dist, dist));
        label.setOpaque(true);
        return label;
    }

    private JLabel getEmptyLabel(Dimension d) {
        JLabel label = new JLabel();
        label.setPreferredSize(d);
        return label;
    }

    public GamePlay(Main parent, ASPSolver blueSolver, ASPSolver redSolver, 
    		JFrame frame, int n,  String redName, String blueName) {
        this.parent = parent;
        this.frame = frame;
        this.dim = n;

        this.redName = redName;
        this.blueName = blueName;
        
        this.blueSolver= blueSolver;
        this.redSolver=redSolver;
        initGame();
    }

    private boolean goBack;

    private ActionListener closeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
        	System.exit(0);
        }
    };

    private void initGame() {

        board = Board.getInstance();
        int boardWidth = dim * size + (dim-1) * dist;
        turn = Board.RED;


        JPanel grid = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        grid.add(getEmptyLabel(new Dimension(2 * boardWidth, 10)), constraints);
        
        JPanel playerPanel = new JPanel(new GridLayout(2, 2));
        if(dim>3) 
        	playerPanel.setPreferredSize(new Dimension(2 * boardWidth, dist));
        else 
        	playerPanel.setPreferredSize(new Dimension(2 * boardWidth, 2 * dist));
        
        playerPanel.add(new JLabel("<html><font color='orange'>Giocatore 1:", SwingConstants.CENTER));
        playerPanel.add(new JLabel("<html><font color='#02a3d0'>Giocatore 2:", SwingConstants.CENTER));
        playerPanel.add(new JLabel("<html><font color='orange'>" + redName, SwingConstants.CENTER));
        playerPanel.add(new JLabel("<html><font color='#02a3d0'>" + blueName, SwingConstants.CENTER));
        ++constraints.gridy;
        grid.add(playerPanel, constraints);

        ++constraints.gridy;
        grid.add(getEmptyLabel(new Dimension(2 * boardWidth, 10)), constraints);

        JPanel scorePanel = new JPanel(new GridLayout(2, 2));
        scorePanel.setPreferredSize(new Dimension(2 * boardWidth, dist));
        scorePanel.add(new JLabel("<html><font color='orange'>Punteggio:", SwingConstants.CENTER));
        scorePanel.add(new JLabel("<html><font color='#02a3d0'>Punteggio:", SwingConstants.CENTER));
        
        redScoreLabel = new JLabel("0", SwingConstants.CENTER);
        redScoreLabel.setForeground(arancione);
        
        scorePanel.add(redScoreLabel);
        
        blueScoreLabel = new JLabel("0", SwingConstants.CENTER);
        blueScoreLabel.setForeground(azzurro);
        scorePanel.add(blueScoreLabel);
        ++constraints.gridy;
        grid.add(scorePanel, constraints);

        ++constraints.gridy;
        grid.add(getEmptyLabel(new Dimension(2 * boardWidth, 10)), constraints);

        this.vEdge = new JLabel[dim-1][dim];
     //   isSetVEdge = new boolean[dim-1][dim];

        this.hEdge = new JLabel[dim][dim-1];
      //  isSetHEdge = new boolean[n][n-1];

        box = new JLabel[dim-1][dim-1];

        for(int i=0; i<(2*dim-2); i++) {
            JPanel pane = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
            if(i%2==0) {
                pane.add(getDot());
                for(int j=0; j<(dim-1); j++) {
                	System.out.println("j: "+j+", i/2: "+i/2);
                    hEdge[j][i/2] = getHorizontalEdge();
                    pane.add(hEdge[j][i/2]);
                    pane.add(getDot());
                }
            }
            else {
                for(int j=0; j<(dim-2); j++) {
                    vEdge[j][i/2] = getVerticalEdge();
                    pane.add(vEdge[j][i/2]);
                    box[j][i/2] = getBox();
                    pane.add(box[j][i/2]);
                }
                int stampa = dim-1;
                vEdge[dim-2][i/2] = getVerticalEdge();
                pane.add(vEdge[dim-2][i/2]);
            }
            ++constraints.gridy;
            grid.add(pane, constraints);
        }


        ++constraints.gridy;
        grid.add(getEmptyLabel(new Dimension(2 * boardWidth, 10)), constraints);

        statusLabel = new JLabel("Turno del Giocatore 1", SwingConstants.CENTER);
        statusLabel.setForeground(arancione);
        statusLabel.setPreferredSize(new Dimension(2 * boardWidth, dist));
        ++constraints.gridy;
        grid.add(statusLabel, constraints);

        ++constraints.gridy;
        grid.add(getEmptyLabel(new Dimension(2 * boardWidth, 10)), constraints);

        JButton closeButton = new JButton("Termina la partita");
        closeButton.setPreferredSize(new Dimension(boardWidth, dist));
        closeButton.addActionListener(closeListener);
        ++constraints.gridy;
        grid.add(closeButton, constraints);

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();

        frame.setContentPane(grid);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        goBack = false;
        manageGame();

        while(!goBack) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        parent.initGUI();
    }

}
