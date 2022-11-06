package view;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class AdminMain {
    public static final String PASSWORD = "666";

    public static void frame2Admin() {
        // 创建 JFrame 实例
        JFrame jf = new JFrame("管理员系统");
        jf.setSize(720, 650);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);

        // 创建选项卡面板
        final JTabbedPane tabbedPane = new JTabbedPane();
        // 创建第 1 个选项卡（选项卡包含 标题、图标 和 tip提示）
        tabbedPane.addTab("影片管理", new ImageIcon("bb.jpg"), new AdminFilmView().getPanel(),"影片信息");
        // 创建第 2 个选项卡
        tabbedPane.addTab("放映厅管理", new ImageIcon("bb.jpg"), new AdminTheaterView().getPanel(),"放映厅信息");
        // 创建第 3 个选项卡
        tabbedPane.addTab("预定管理", new ImageIcon("bb.jpg"), new AdminBookView().getPanel(), "订票信息");
        // 添加选项卡选中状态改变的监听器
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println("当前选中的选项卡: " + tabbedPane.getSelectedIndex()+1);
            }
        });

        // 配置 frame
        jf.setContentPane(tabbedPane);
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
}
