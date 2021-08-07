/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Gui.Puzzle;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author mynameis
 */
public class control3 {

    Puzzle puz;

    public control3() {
    }

    public control3(Puzzle puz) {
        this.puz = puz;
    }
    private int move;// count move
    private int time;// count time 
    private Timer timer;// run time 
    private int buttonSize = 70;
    private Button[][] board;//initial button matrix for contain button
    private boolean isplaying;///trạng thái người chơi
    private int height = Toolkit.getDefaultToolkit().getScreenSize().height;

    public void controller() {
        puz.setVisible(true);
        setSizeBoard();
        initBoard(getSize());
        initTime();
        btnNewGame();
    }

    private void setSizeBoard() {
        DefaultComboBoxModel dfcb = new DefaultComboBoxModel();
        for (int i = 3; i < (height - 50) / buttonSize; i++) {
            dfcb.addElement(i + "x" + i);
        }
        puz.getCbxSize().setModel(dfcb);
    }

    private void btnNewGame() {
        puz.getBtnNewGame().addActionListener((ae) -> {
            if (isplaying) {
                if (JOptionPane.showConfirmDialog(puz, "Are you in Game\nDo you want to new game", "message", 0) == 0) {
                    initBoard(getSize());
                    puz.getLblCount().setText("0");
                    puz.getLblTime().setText("0");
                }
            } else {
                initBoard(getSize());
                puz.getLblCount().setText("0");
                puz.getLblTime().setText("0");

            }
        });
    }

    private int getSize() {
        String txtSize = puz.getCbxSize().getSelectedItem() + "";
        int k = txtSize.indexOf("x");
        return (Integer.parseInt(txtSize.substring(0, k) + ""));
    }

    private void disableButton(int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j].setEnabled(false);
            }
        }
    }

    private void initBoard(int size) {
        time = 0;
        move = 0;
        isplaying = true;
        buttonSize = (height - 200) / size;
        LinkedList<Integer> lstRnd = getRandomList(size);
        board = new Button[size][size];
        puz.getPnlBoard().removeAll();
        puz.getPnlBoard().setLayout(new GridLayout(size, size));
        puz.getPnlBoard().setPreferredSize(new Dimension(buttonSize * size, buttonSize * size));
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int el = lstRnd.poll();
                board[i][j] = new Button((el == 0) ? "" : el + "");
                board[i][j].setPreferredSize(new Dimension(buttonSize, buttonSize));
                puz.getPnlBoard().add(board[i][j]);
                final int ii = i, jj = j;
                board[i][j].addActionListener(ae -> {
                    if (swap(ii, jj)) {
                        timer.start();
                        move++;
                        puz.getLblCount().setText(" " + move);
                        
                        if (isWin()) {
                            isplaying = false;
                            disableButton(size);
                            timer.stop();
                            JOptionPane.showMessageDialog(puz, "you are winner\nCount " + move + "\nTime " + time);
                        }

                    }

                });

            }
        }
        puz.pack();
    }

    private void initTime() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                time++;
                puz.getLblTime().setText(time + "");
            }
        });
    }

    private boolean isWin() {
        int size = board.length;
        if (board[size - 1][size - 1].getLabel().equals("")) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    int el = i * size + j;///lây vị trí 

                    if (el != size * size - 1 && !board[i][j].getLabel().equals(el + 1 + "")) {
                        return false;
                    }
                }
            }
        }
        else {
            return false;
        }

        return true;
    }

    private boolean swap(int x, int y) {//x :row ,y :col
        int size = board.length;
        
        if (size >= getSize()) {
            
            if (x - 1 >= 0 && board[x - 1][y].getLabel().equals("")){
                board[x - 1][y].setLabel(board[x][y].getLabel());
                board[x][y].setLabel("");
                System.out.println("tren");
                return true;
            } else if (x + 1 < size && board[x + 1][y].getLabel().equals("")) {
                board[x + 1][y].setLabel(board[x][y].getLabel());
                board[x][y].setLabel("");
                System.out.println("duoi");
                return true;

            } else if (y > 0 && board[x][y - 1].getLabel().equals("")) {
                board[x][y - 1].setLabel(board[x][y].getLabel());
                board[x][y].setLabel("");
                return true;
            } else if (y < size - 1 && board[x][y + 1].getLabel().equals("")) {
                board[x][y + 1].setLabel(board[x][y].getLabel());
                board[x][y].setLabel("");

                return true;
            }
        }

        return false;

    }

    /// tạo mảng chứa dữ liệu trống for add element
    private LinkedList<Integer> getRandomList(int size) {
        LinkedList<Integer> lstRnd = new LinkedList<>();
        for (int i = 0; i < size * size; i++) {
            lstRnd.add(i);
        }
        
        for (int i = 0; i < Math.pow(size, 4); i++) {
            Random rnd = new Random();
            int m = rnd.nextInt(4);
            int id = lstRnd.indexOf(0);
            if (m == 0 && id % size + 1 < size) {
                lstRnd.set(id, lstRnd.get(id + 1));
                lstRnd.set(id + 1, 0);
                System.out.println("chạy so 1");
            } else if (m == 1 && id / size > 0) {
                lstRnd.set(id, lstRnd.get(id - size));
                lstRnd.set(id - size, 0);
                System.out.println("chay so 2");
            } else if (m == 2 && id % size > 0) {
                lstRnd.set(id, lstRnd.get(id - 1));
                lstRnd.set(id - 1, 0);
                System.out.println("chay so 3");
            } else if (m == 3 && id / size < size - 1) {
                lstRnd.set(id, lstRnd.get(id + size));
                lstRnd.set(id + size, 0);
                System.out.println("chay so 4");
            }
        }
        return lstRnd;
    }

    public static void main(String[] args) {
        new control3(new Puzzle()).controller();
    }

}
