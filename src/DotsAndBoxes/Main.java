package DotsAndBoxes;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Main {

    private int n;
    private String redName, blueName;

    private JFrame frame;
    private JLabel modeError, sizeError;

    String[] players = {"Seleziona Giocatore", "Umano", "Giocatore ASP"};
    private JRadioButton[] sizeButton;

    JComboBox<String> redList, blueList;
    ButtonGroup sizeGroup;
    
    private ASPSolver redSolver;
    private ASPSolver blueSolver;	
	
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

    private ASPSolver getSolver(int level) {
        if(level == 1)
        	return new ASPSolver();
        else
        	return null;
    }

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
                if(rIndex > 1) redSolver = getSolver(rIndex - 1);
                if(bIndex > 1) blueSolver = getSolver(bIndex - 1);
            }
            for(int i=0; i<8; i++) { //Serve ad inizializzare una griglia fino a 9x9
                if(sizeButton[i].isSelected()) {
                    n = i+3;
                    startGame = true;
                    return;
                }
            }
            sizeError.setText("Seleziona la grandezza della griglia per continuare.");
        }
    };

    private static Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
        Image img = icon.getImage();  
        Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight,  java.awt.Image.SCALE_SMOOTH);  
        return new ImageIcon(resizedImage);
    }
    
    public void initGUI() {
    	JPanel grid = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        ImageIcon icon = new  ImageIcon(getClass().getResource("logo.png"));
        ImageIcon img = (ImageIcon) resizeIcon(icon, 460, 250);
        JLabel logoLabel = new JLabel(img);
        grid.add(logoLabel, constraints);

        ++constraints.gridy;
        grid.add(getEmptyLabel(new Dimension(500,25)), constraints);

        modeError = new JLabel("", SwingConstants.CENTER);
        modeError.setForeground(Color.RED);
        modeError.setPreferredSize(new Dimension(500, 25));
        ++constraints.gridy;
        grid.add(modeError, constraints);

        JPanel modePanel = new JPanel(new GridLayout(2, 2));
        modePanel.setPreferredSize(new Dimension(400, 50));
        modePanel.add(new JLabel("<html><font color='orange'> Giocatore 1:", SwingConstants.CENTER));
        modePanel.add(new JLabel("<html><font color='#02a3d0'> Giocatore 2:", SwingConstants.CENTER));
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
        for(int i=0; i<6; i++)
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

        new GamePlay(this, blueSolver,redSolver ,frame, n, redName, blueName);
    }

    public static void main(String[] args) {
        new Main().initGUI();
    }

}
