package view;

import action.Conn;
import entity.BookInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.util.List;

public class GetTicketView {

    JFrame frame;

    Conn cc=new Conn();
    JPanel panel =new JPanel();
    int isflag=3;
    List list1;
    String phoneNo;
    String validCode;
    JTextField text1 = new JTextField();
    JTextField text2 = new JTextField();
    JLabel label=new JLabel("");

    public void getTicket(JFrame jf) {
        frame = jf;
        jf.setBounds(500,300,600,600);
        panel.setSize(600,600);
        panel.setLayout(null);

        JLabel phone=new JLabel("手机号:");
        phone.setBounds(200, 150, 50, 30);

        JLabel code=new JLabel("取票码:");
        code.setBounds(200, 220, 50, 30);

        label.setBounds(240,250,200,20);

        ImageIcon icon=new ImageIcon(GetTicketView.class.getClassLoader().getResource("res/icons/submitBtn.png"));
        JButton choose=new JButton("选定");
        choose.setBounds(240, 300, 90, 30);
        icon.setImage(icon.getImage().getScaledInstance(110,30,Image.SCALE_DEFAULT));
        choose.setIcon(icon);

        choose.addActionListener(new MyTable());

        text1.setBounds(250, 150, 120, 30);
        text2.setBounds(250,220 , 120, 30);

        panel.add(label);
        panel.add(phone);
        panel.add(code);
        panel.add(choose);
        panel.add(text1);
        panel.add(text2);

        // 配置 frame
        jf.add(panel);
        jf.setVisible(true);
        // 关闭管理员界面后，重新打开选票界面
        jf.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        jf.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {
                jf.dispose();
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

    public void getTicketMessage(String tel,String code) throws SQLException {

        JDialog ff=new JDialog();
        //ff.setSize(300, 500);
        ff.setBounds(550,250,300,500);

        JPanel jp = new JPanel();
        jp.setSize(300, 500);
        jp.setLayout(null);

        BookInfo bookInfo = new Conn().getBookByTelAndCode(tel, code);
        if (bookInfo != null) {
            JOptionPane.showMessageDialog(null,"<html>电影票序号:"
                    +bookInfo.getId()+"<br>手机号:"+bookInfo.getTel()+"<br>取票码:"+bookInfo.getValidCode()
                    +"<br>座位号:"+bookInfo.getSeatY()+"行"+bookInfo.getSeatX()+"列","取票成功",JOptionPane.INFORMATION_MESSAGE);
        } else {

        }

    }

    public int validExist(String tel, String code) throws SQLException {
        isflag =3;
        BookInfo bookInfo = new Conn().getBookByTelAndCode(tel, code);
        if (bookInfo != null) {
            if (bookInfo.getStatus()) {     //已出票
                isflag =2;
            } else {    //可以打印
                isflag =1;
            }
        } else {    //信息不对
            isflag=3;
        }
        return isflag;
    }

    class MyTable  implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {

            try {
                phoneNo=text1.getText().trim();
                validCode=text2.getText().trim();
                if(validExist(phoneNo,validCode)==3){
                    label.setText("输入手机号或取票码有误!");
                    label.setForeground(Color.red);
                } else if(validExist(phoneNo,validCode)==1){
                    cc.updateStatus(phoneNo,validCode);
                    getTicketMessage(phoneNo,validCode);
                    label.setText("取票成功!");
                    frame.dispose();
                    PickFilmView.frame2PickFilm(PickFilmView.film_list_order_mode);
                    label.setForeground(Color.red);
                }else if(validExist(phoneNo,validCode)==2){
                    label.setText("已出票，无法取票!");
                    label.setForeground(Color.red);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
