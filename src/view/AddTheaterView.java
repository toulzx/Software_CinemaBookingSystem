package view;

import action.Conn;
import entity.Theater;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddTheaterView {
    public void Page2AddTheater(DefaultTableModel dtm){
        JFrame frame = new JFrame();
        frame.setSize(400,450);
        frame.setLocationRelativeTo(null);

        JPanel jp = new JPanel();
        jp.setLayout(null);

        // 输入放映厅名称的文本域
        JLabel label1=new JLabel("放映厅名称：");
        label1.setBounds(50,50,100,30);
        jp.add(label1);
        JTextField theaterName = new JTextField();
        theaterName.setBounds(160,50,160,30);
        jp.add(theaterName);
        // 提交失败后提示语（名称已存在）
        JLabel label22=new JLabel("");
        label22.setBounds(160,80,300,20);
        jp.add(label22);

        // 输入总排数的文本域
        JLabel label2=new JLabel("总排数：");
        label2.setBounds(50,100,100,30);
        jp.add(label2);
        JTextField theaterRows = new JTextField();
        theaterRows.setBounds(160,100,165,30);
        jp.add(theaterRows);

        // 输入总列数的文本域
        JLabel label3=new JLabel("总列数：");
        label3.setBounds(50,150,100,30);
        jp.add(label3);
        JTextField theaterColumns = new JTextField();
        theaterColumns.setBounds(160,150,165,30);
        jp.add(theaterColumns);

        // 输入票价加成文本域
        JLabel label4=new JLabel("票价加成：");
        label4.setBounds(50,200,100,30);
        jp.add(label4);
        JTextField theaterPremium = new JTextField();
        theaterPremium.setBounds(160,200,165,30);
        jp.add(theaterPremium);

        //创建“添加”按钮
        JButton btnAdd = new JButton("添加");
        ImageIcon icon3=new ImageIcon("src/res/icons/addBtn.png");
        icon3.setImage(icon3.getImage().getScaledInstance(100,30,Image.SCALE_DEFAULT));
        btnAdd.setIcon(icon3);
        btnAdd.setBounds(140, 270, 80, 30);
        jp.add(btnAdd);
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(new Conn().isTheaterNameUsed(theaterName.getText())){
                    int i=Integer.parseInt(theaterRows.getText());
                    int j=Integer.parseInt(theaterColumns.getText());
                    int flag = new Conn().addTheater(new Theater(0,theaterName.getText(),i,j,Float.valueOf(theaterPremium.getText())));
                    if (flag != -1) {
                        Object[] message = new Object[Theater.COLUMN_NAMES.length];
                        message[0]=0;
                        message[1]=theaterName.getText();
                        message[2]=i;
                        message[3]=j;
                        message[4]=theaterPremium.getText();
                        dtm.addRow(message);
                        JOptionPane.showMessageDialog(null, "插入成功!","状态",JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "插入失败！注意取值范围。","状态",JOptionPane.ERROR_MESSAGE);
                    }
                }
                else{
                    label22.setText("此放映厅已存在!");
                    label22.setForeground(Color.red);//设置颜色
                }
            }
        });
        frame.add(jp);
        frame.setVisible(true); // 设置界面可见
    }
}
