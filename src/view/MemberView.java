package view;

import action.Conn;
import action.Utils;

import javax.swing.*;

public class MemberView {
    String inputTel;
    public void Page2Member() {
        do{
            inputTel = JOptionPane.showInputDialog(null, "请输入你订票预留的手机号", "请输入", JOptionPane.QUESTION_MESSAGE);
        } while (new Conn().getMemberBookCount(inputTel)<=0 && !inputTel.isEmpty());

        if (inputTel.isEmpty()) {
            return;
        }

        JFrame frame = new JFrame();
        frame.setSize(700,600);

        JPanel jp = new JPanel();
        jp.setLayout(null);

        int limit = new Utils().getMemberShipLevelLimit(inputTel);
        String strLimit = limit==0?"-已达最高级-":String.valueOf(limit);


        JLabel labelHistory = new JLabel("<html>你看过的：" + new Conn().Stats(inputTel));
        labelHistory.setBounds(120,0, 600, 60);
        jp.add(labelHistory);

        JLabel labelMember = new JLabel( "<html> "
                +"当前会员等级是 " + new Utils().getMemberShipLevelName(inputTel) + "<br>"
                + "您已经消费 " + new Conn().getMemberBookCount(inputTel) + " 次<br>"
                + "下一级的门槛是 " +  strLimit + " 次<br>");
        labelMember.setBounds(120,60,600,100);
        jp.add(labelMember);

        JLabel labelRecommend = new JLabel("<html>大数据推荐 | 和你看过同一部电影的人也看过："
                + new Conn().Recommend(inputTel));
        labelRecommend.setBounds(120,160,600,100);
        jp.add(labelRecommend);

        frame.add(jp);
        frame.setVisible(true);
    }

}
