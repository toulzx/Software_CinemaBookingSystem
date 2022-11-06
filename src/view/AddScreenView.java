package view;

import action.Conn;
import entity.FilmScreen;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;
//import org.apache.commons.lang3.time.DateUtils;

public class AddScreenView {
    DefaultTableModel dtm=null;
    int getFilmid;
    public JFrame showFilmScreenAddView(int filmid,DefaultTableModel dtm1) throws ParseException {
        dtm = dtm1;
        getFilmid = filmid;
        // 创建 JFrame 实例
        JFrame frame = new JFrame("空场次添加 ");
        JPanel jpa=new JPanel();    //创建面板
        jpa.setLayout(null);

        frame.add(jpa);
        // Setting the width and height of frame
        frame.setSize(400, 450);

        /*
         * 创建文本域用于用户输入
         */
        // 输入场次日期的文本域
        JLabel label2=new JLabel("场次日期：");
        label2.setBounds(50,100,100,30);
        jpa.add(label2);
        JTextField screenDate = new JTextField();
        screenDate.setBounds(160,100,165,30);
        jpa.add(screenDate);
        // 输入场次时间的文本域
        JLabel label3=new JLabel("场次时间：");
        label3.setBounds(50,150,100,30);
        jpa.add(label3);
        JTextField screenTime = new JTextField();
        screenTime.setBounds(160,150,165,30);
        jpa.add(screenTime);
        //放映室id
        JLabel label4=new JLabel("放映室id：");    //创建标签
        label4.setBounds(50,200,100,30);
        jpa.add(label4);
        JTextField theaterId = new JTextField();
        theaterId.setBounds(160,200,165,30);
        jpa.add(theaterId);

        JLabel label22=new JLabel("");
        label22.setBounds(160,125,300,20);
        jpa.add(label22);
        JLabel label33=new JLabel("");
        label33.setBounds(160,175,300,20);
        jpa.add(label33);

        //创建“添加”按钮
        JButton AddButton = new JButton("添加");
        AddButton.setBounds(140, 270, 80, 30);
        jpa.add(AddButton);

        //按钮监听
        AddButton.addActionListener(new ActionListener()
        {
            int clicks=0;
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(!ValidDate(screenDate.getText())){
                    label22.setText("请使用yyyy-MM-dd格式");
                    //label22.setFont(new Font("楷体",Font.BOLD,16));    //修改字体样式
                    label22.setForeground(Color.red);//设置颜色
                }
                if(!ValidTime(screenTime.getText())){
                    label33.setText("请使用HH:ii格式");
                    label33.setForeground(Color.red);//设置颜色
                }
                else if(ValidTime(screenTime.getText())){
                    label33.setText("格式正确");
                    label33.setForeground(Color.blue);//设置颜色
                }

                if (screenDate.getText()!=null&&screenTime.getText()!=null&&theaterId.getText()!=null){
                    FilmScreen screen = new FilmScreen(0,
                            getFilmid,
                            java.sql.Date.valueOf(screenDate.getText()),
                            screenTime.getText(),
                            Integer.valueOf(theaterId.getText())
                    );
                    int flag = new Conn().addScreen( screen );
                    if(flag != -1){
                        Vector rowV = new Vector();
                        rowV.add(flag);
                        rowV.add(getFilmid);
                        rowV.add(screenDate.getText());
                        rowV.add(screenTime.getText());
                        rowV.add(Integer.parseInt(theaterId.getText()));
                        dtm.addRow(rowV);
                        JOptionPane.showMessageDialog(null, "添加成功", "状态", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                    }else{
                        JOptionPane.showMessageDialog(null, "数据库添加失败","状态",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return frame;// 设置界面可见
    }

    public static boolean ValidDate(String str) {
        boolean convertSuccess=true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy-MM-dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007-02-29会被接受，并转换成2007-03-01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess=false;
        }
        return convertSuccess;
    }

    public static boolean ValidTime(String str) {
        boolean convertSuccess=true;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            convertSuccess=false;
        }
        return convertSuccess;
    }


}
