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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author mynameis
 */
public class control2 {

    private Puzzle puz;
    private int buttonSize = 90;
    private Button[][] board;
    private int move;
    private int time;
    //private Thread timer;
    private Timer timer;
    private boolean isPlaying;
    int height = (int) Toolkit.getDefaultToolkit().getScreenSize().height;

    public control2() {
    }

    public control2(Puzzle puz) {
        this.puz = puz;
    }

    public void contr() {
        puz.setVisible(true);
        loadBoardSize();
        buttonHandle();
        System.out.println(getSize());
        
       
       
        initTimer();
    }

    private void buttonHandle() { // sử lí button play( tên + mã sv + ngày) 
        //initBoard(3);
        //move(getSize());
      
        
        puz.getBtnNewGame().addActionListener(evt -> { //
            if (isPlaying) { // nếu ng chơi đang chơi thì hiện thị thông báo và nếu ng chơi chọn Yes thì sẽ khởi tạo trò chơi mới và sẽ chơi lại từ đâu
                if (JOptionPane.showConfirmDialog(puz, "U are in a game\nDo u wanna play a new game?", "Question", 0) == 0) {
                    initBoard(getSize());
                      //move(getSize());
                    // timer.start();
                }
            } else { // còn nếu ko, ở trạng thái ng chơi rảnh (ko trong trận đấu) thì sẽ bắt đầu game
                initBoard(getSize());
                  //move(getSize());
            }
            puz.getLblCount().setText("0");
            puz.getLblTime().setText("0 sec");
             move(getSize());
        });
        
    }

    private void loadBoardSize() {
        DefaultComboBoxModel dfcb = new DefaultComboBoxModel();
        for (int i = 3; i < height / buttonSize; i++) {
            dfcb.addElement(i + "x" + i);
        }
        puz.getCbxSize().setModel(dfcb);
    }

    private int getSize() {// lay size từ cbx 
        return puz.getCbxSize().getSelectedIndex() + 3;
    }

    public void disableButton() {//enable các button sau khi win
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                board[i][j].setEnabled(false);
            }
        }
    }
    public void initTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                time++;
                puz.getLblTime().setText(time + " sec");
            }
        });
    }

    private void initBoard(int size) {// khởi tạo board chơi 
        System.out.println("init boad");
        time = 0;
        move = 0;
        isPlaying=true;
        LinkedList<Integer> lstRnd = getRandomGame(size);/// lấy list game
        board = new Button[size][size];
        puz.getPnlBoard().removeAll();
        puz.getPnlBoard().setLayout(new GridLayout(size, size));
        puz.getPnlBoard().setPreferredSize(new Dimension(size * buttonSize, size * buttonSize));
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int el = lstRnd.poll();
                board[i][j] = new Button((el == 0) ? "" : el + "");
                board[i][j].setPreferredSize(new Dimension(buttonSize, buttonSize)); // set size cho button
                puz.getPnlBoard().add(board[i][j]); // add button vào pnlBoard
                
               
                
            }
        }
       
        
        puz.pack();
    }
    public void move(int size){
        size=getSize();
        //System.out.println("di choi ở move");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                 final int ii = i, jj = j;
                board[i][j].addActionListener(evt -> {
                    timer.start();
                    isPlaying=true;
                    if (swap(ii, jj)) {
                        System.out.println("dao choi swap");
                        move++;
                        puz.getLblCount().setText(move + "");
                        if (isWin()) {
                            disableButton();
                            timer.stop();
                            JOptionPane.showMessageDialog(puz, "You are winner\ncount: " + move + "\ntime: " + time);
                            isPlaying = false;
                        }
                    }
                });
            }
            
        }
        
    }

    private boolean swap(int x, int y) {//x là vị trí hàng y là vị trí cột
        System.out.println("hello");
        int size = board.length;
        if (x - 1 >= 0) {// dk :k nằm ở hàng trên cùng
            if (board[x - 1][y].getLabel().equals("")) {//nghĩa là th rong o hang tren
                board[x - 1][y].setLabel(board[x][y].getLabel());
                board[x][y].setLabel("");
                System.out.println("tren");
                return true; //swap thanh cong
            }
        }
        if (x + 1 < size) {// dk :k nằm ở hàng duoi cung :x<bsize-1
            if (board[x + 1][y].getLabel().equals("")) {//nghĩa là th rong o hang tren
                board[x + 1][y].setLabel(board[x][y].getLabel());
                board[x][y].setLabel("");
                System.out.println("duoi");
                return true; //swap thanh cong

            }
        }
        // trai :lay theo tọa độ của col
        if (y >= 1) {// dk :k nằm ở cột ngoài cùng
            if (board[x][y - 1].getLabel().equals("")) {//nghĩa là th rong o hang tren
                board[x][y - 1].setLabel(board[x][y].getLabel());
                board[x][y].setLabel("");
                System.out.println("trai");
                return true; //swap thanh cong

            }
        }
        if (y + 1 < size) {// dk :k nằm ở cột ngoài cùng
            if (board[x][y + 1].getLabel().equals("")) {//nghĩa là th rong o hang tren
                board[x][y + 1].setLabel(board[x][y].getLabel());
                board[x][y].setLabel("");
                System.out.println("phai");
                return true; //swap thanh cong

            }
        }
        return false;
    }

    private boolean isWin() {
        int size = board.length;
        if (board[size - 1][size - 1].getLabel().equals("")) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    int el = i * size + j;
                    System.out.println(el + 1);
                    if (el != size * size - 1 && !board[i][j].getLabel().equals(el + 1 + "")) {
                        return false;
                    }
                }
            }
        }
        else{
            return false ;
        }

        return true;
    }

    private LinkedList<Integer> getRandomGame(int bsize) {
        LinkedList<Integer> lstRnd = new LinkedList<>();// tạo linkedlist empty để chứa dữ liệu
        for (int i = 0; i < bsize * bsize; i++) {
            lstRnd.add(i);
        }
        /// tạo vòng for để trộn lstRnd 
        for (int i = 0; i < Math.pow(bsize, 4); i++) {
            Random rnd = new Random();

            int m = rnd.nextInt(4);//random xem hoán trên dưới trái phải
            int id = lstRnd.indexOf(0);//lay vị trí của số 0
            if (m == 0 && id % bsize + 1 < bsize) {// đổi sang phải
                lstRnd.set(id, lstRnd.get(id + 1));
                lstRnd.set(id + 1, 0);
            } else if (m == 1 && (id / bsize + 1) < bsize) { // dịch chuyển xuống dưới
                lstRnd.set(id, lstRnd.get(id + bsize));
                lstRnd.set(id + bsize, 0);
            } else if (m == 2 && id % bsize > 0) {// đổi sang trai
                lstRnd.set(id, lstRnd.get(id - 1));
                lstRnd.set(id - 1, 0);
            } else if (m == 3 && id / bsize > 0) {// đổi sang tren
                lstRnd.set(id, lstRnd.get(id - bsize));
                lstRnd.set(id - bsize, 0);
            }
        }
        return lstRnd;
    }

    public static void main(String[] args) {
        new control2(new Puzzle()).contr();

    }

}
