package view;

import action.Conn;
import action.Utils;
import entity.BookInfo;
import entity.Film;
import entity.Theater;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookTicketView {

    public static int current_film_id = 0;
    public static void page2book(int filmId) {        // 订票界面，选日期部分
        current_film_id = filmId;
        // frame
        JFrame frame=new JFrame("购票选时间段页面");
        frame.setBounds(500,200,700,600);
        // panel
        JPanel jp = new JPanel();
        jp.setLayout(null);
        jp.setBounds(0,0,200,500);
        // 以下依次配置页面元素
        // label
        JLabel jlabel = new JLabel("请选择日期");
        jlabel.setBounds(0,0,90,20);
        jp.add(jlabel);
        // ComboBox 获取影片的放映日期列表并载入下拉组合框
        JComboBox cmb = new JComboBox();
        cmb.setBounds(90,0,100,20);
        for (Date date : new Conn().getScreenDayList(filmId)) {
            cmb.addItem(date);
        }
        cmb.setSelectedIndex(-1);
        cmb.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                Date date = (Date) cmb.getSelectedItem();
                if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                    // 当组合框中元素被选中，清除组合框之后生成的所有元素（此处表现为放映时间按钮、荧幕、座位、提交按钮）
                    int ctCount = jp.getComponentCount();
                    for(int i = ctCount-1;i >=2;i--) {
                        jp.remove(i);
                    }
                    jp.repaint();
                    // （重新）生成放映时间按钮
                    ArrayList<String> screenTimeList = new ArrayList<>();
                    screenTimeList = new Conn().getScreenTimeList(filmId, date);
                    for(int i=0; i<screenTimeList.size(); i++) {
                        JButton btn = new JButton(screenTimeList.get(i));
                        btn.setBounds(90, 30 + i * 30, 100, 20);
                        btn.setFocusPainted(false);
                        int screenId=0, theaterId=0;
                        screenId = new Conn().getIdByScreenInfo(true, filmId, date, screenTimeList.get(i));
                        theaterId = new Conn().getIdByScreenInfo(false, filmId, date, screenTimeList.get(i));
                        int finalScreenId = screenId;
                        int finalTheaterId = theaterId;
                        int finalCount = screenTimeList.size();
                        btn.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent actionEvent) {
                                int ctCount = jp.getComponentCount();
                                for(int i = ctCount-1; i>=2+ finalCount; i--)
                                {
                                    jp.remove(i);
                                }
                                jp.repaint();
                                showTheaterSeats(frame,jp,finalScreenId,finalTheaterId);
                            }
                        });
                        jp.add(btn);
                    }
                }
            }
        });
        jp.add(cmb);
        // frame 配置面板
        frame.add(jp);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {
                PickFilmView.frame2PickFilm(PickFilmView.film_list_order_mode);
            }
            @Override
            public void windowClosed(WindowEvent e) {}
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
    }

    private static void showTheaterSeats(JFrame frame, JPanel jp, int screenId, int theaterId) {     // 绘制座位选择部分
        List<BookInfo> bookInfoList = new ArrayList<>();
        bookInfoList = new Conn().getBookListByScreenId(screenId);
        // 获得放映厅数据
        Theater theater = new Conn().getTheaterById(theaterId);
        // 设置荧幕图片
        ImageIcon imgScreen = new ImageIcon("src/res/icons/screen.png");
        imgScreen.setImage(imgScreen.getImage().getScaledInstance(400, 40, Image.SCALE_DEFAULT));
        JLabel labelImgScreen = new JLabel(imgScreen);
        labelImgScreen.setBounds(260,0,200+22*theater.getRows(),50);
        jp.add(labelImgScreen);
        // 设置放映厅信息
        String premiumRadioInfo = theater.getPremiumRadio()==1.0 ? "无" : (theater.getPremiumRadio()-1)*100 + "%";
        JLabel labelScreenInfo = new JLabel("<html>"
                + theater.getName()
                + "<br>影厅加成：" + premiumRadioInfo
                + "</html>");
        labelScreenInfo.setBounds(295+theater.getRows()*17,50,100,50);     // if tr.getRows()>=6
        // labelScreenInfo.setBounds(300+tr.getRows()*20,50,50,50);  // else
        jp.add(labelScreenInfo);
        // 设置座位
        ImageIcon imgSeatUnavailable = new ImageIcon("src/res/icons/seat_unavailable.png");
        imgSeatUnavailable.setImage(imgSeatUnavailable.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
        ImageIcon imgSeatAvail = new ImageIcon("src/res/icons/seat_available.png");
        imgSeatAvail.setImage(imgSeatAvail.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
        int [][]seat = new int[theater.getRows()][theater.getColumns()];
        for (int i = 0; i < theater.getRows(); i++) {
            for (int j = 0; j < theater.getColumns(); j++) {
                seat[i][j] = 0;
                JButton btn = new JButton(imgSeatAvail);
                btn.setBorder(null);
                btn.setBounds(300 + i * 43, 100 + j * 45, 35, 35);    // if tr.getRows()>=6
                // btn.setBounds(290 + i * 55, 100 + j * 50, 35, 35);   // else
                jp.add(btn);
                for (BookInfo bookInfo : bookInfoList) {
                    if (bookInfo.getSeatX() == (i + 1) && bookInfo.getSeatY() == (j + 1)) {
                        btn.setIcon(imgSeatUnavailable);
                        btn.setEnabled(false);
                    }
                }
                int finalI = i;
                int finalJ = j;
                int[] chosenSeatsNum = {0};
                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        chosenSeatsNum[0]++;
                        if (chosenSeatsNum[0] % 2 == 0) {
                            btn.setIcon(imgSeatAvail);
                            seat[finalI][finalJ] = 0;
                        } else {
                            btn.setIcon(imgSeatUnavailable);
                            seat[finalI][finalJ] = 1;
                        }
                    }
                });
            }
        }
        // 提交按钮
        ImageIcon imgSubmit = new ImageIcon("src/res/icons/submit.png");
        imgSubmit.setImage(imgSubmit.getImage().getScaledInstance(100, 40, Image.SCALE_DEFAULT));
        JButton btnSubmit = new JButton(imgSubmit);
        btnSubmit.setBounds(245+theater.getRows()*23,500,100,40);
        jp.add(btnSubmit);
        // 信息确认
        Theater finalTheater = theater;
        int finalScreenId = screenId;
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dialog2ConfirmTicket(frame, finalTheater, finalScreenId, seat);
            }
        });
    }

    private static void dialog2ConfirmTicket(JFrame frame, Theater finalTheater, int finalScreenId, int[][] seat) {
        // 生成订单列表
        ArrayList<BookInfo> newBookInfo = new ArrayList<>();
        int maxId = new Conn().getMaxBookId();
        for (int i = 0; i< finalTheater.getRows(); i++) {
            for(int j = 0; j< finalTheater.getColumns(); j++) {
                if(seat[i][j]==1) {
                    BookInfo bookInfo = new BookInfo();
                    bookInfo.setId(++maxId);
                    bookInfo.setScreenId(finalScreenId);
                    bookInfo.setSeatY(j+1);
                    bookInfo.setSeatX(i+1);
                    bookInfo.setValidCode("");      // update it later
                    bookInfo.setTel("");    // update it later
                    bookInfo.setStatus(false);
                    newBookInfo.add(bookInfo);
                }
            }
        }
        // 输入信息
        String telInfo = JOptionPane.showInputDialog("请输入11位电话号码:");
        while (telInfo.length()!=11)
        {
            telInfo = JOptionPane.showInputDialog("输入号码有误，请重新输入:");
        }
        for (BookInfo item:newBookInfo) {
            item.setTel(telInfo);
        }
        // 弹窗确认订票信息
        Film film = new Conn().getFilmById(current_film_id);
        String discountInfo = film.getDiscount()==1?"无":String.valueOf(film.getDiscount());
        double memberShipDiscount = new Utils().getMemberShipDiscount(telInfo);
        String memberShipDiscountInfo = memberShipDiscount==1.0 ? "无" : memberShipDiscount*100 + "折";
        String premiumRadioInfo = finalTheater.getPremiumRadio()==1.0 ? "无" : (finalTheater.getPremiumRadio()-1)*100 + "%";
        int confirmValue = JOptionPane.showConfirmDialog(null,
                "<html>" + film.getName() + " " + newBookInfo.size() + "张<br>"
                + "电影价格：" + film.getPrice() + "<br>"
                + "影院折扣：" + discountInfo + "<br>"
                + "会员折扣：" + memberShipDiscountInfo + "<br>"
                + "影厅加成：" + premiumRadioInfo + "<br><br>"
                + "总价格：￥" + newBookInfo.size()*film.getPrice()*film.getDiscount()*memberShipDiscount*finalTheater.getPremiumRadio()
                ,
                "确认订票信息", JOptionPane.YES_NO_OPTION);
        if (confirmValue==0) {
            // 生成验证码，弹窗购买成功
            String validCode = new Utils().generateValidCode();
            for (BookInfo item : newBookInfo) {
                item.setValidCode(validCode);
            }
            JOptionPane.showMessageDialog(null,
                    "<html>预定成功:<br>电话号码:"+telInfo+"<br>取票码:"+validCode+"</html>","成功",JOptionPane.INFORMATION_MESSAGE);
            if (new Conn().insertBook(newBookInfo)) {
                frame.dispose();        // 购票成功，关闭订票界面，打开选票界面
                PickFilmView.frame2PickFilm(PickFilmView.film_list_order_mode);
            } else {
                JOptionPane.showMessageDialog(frame,"操作失败！数据库报错","状态",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
