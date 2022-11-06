package view;

import action.Conn;
import entity.Film;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class PickFilmView {

    public final static int FILMS_IN_A_PAGE = 2;
    public final static int FILM_LIST_DEFAULT_ORDER = 2;

    /**
     * see method `getFilmList()` in Conn.class
     */
    public static int film_list_order_mode = FILM_LIST_DEFAULT_ORDER;
    public static int current_page = 1;

    /**
     * DO NOT merge these two methods: frame2PickFilm(), page2PickFilm();
     * Cause page2PickFilm(frame,,) is called repeatedly on the same frame in order to show different page...
     */
    public static void frame2PickFilm(int filmListMode) {
        // 设置页面数
        int count = new Conn().getFilmCount();
        int pageSize = count% FILMS_IN_A_PAGE ==0 ? count/ FILMS_IN_A_PAGE : (count/ FILMS_IN_A_PAGE)+1;
        // 创建面板
        JFrame frame = new JFrame("购票页面");
        frame.setBounds(500,300,290,450);
        // 配置菜单
        setMenuBar(frame);
        // 配置选票界面
        page2PickFilm(frame, pageSize, filmListMode);
    }

    public static void setMenuBar(JFrame frame) {
        JMenuBar menuBar = new JMenuBar();
        // 菜单0
        JMenu menu2Options = new JMenu("选项");
        JRadioButtonMenuItem radioButtonMenuItem2 = new JRadioButtonMenuItem("按销量排序");
        JRadioButtonMenuItem radioButtonMenuItem3 = new JRadioButtonMenuItem("按上映时间排序");
        // 子菜单添加到一级菜单
        menu2Options.addSeparator();                // 添加一个分割线
        menu2Options.add(radioButtonMenuItem2);
        menu2Options.add(radioButtonMenuItem3);
        // 将它们放到一个按钮组中实现单选效果，默认第一个单选按钮子菜单选中
        ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(radioButtonMenuItem2);
        btnGroup.add(radioButtonMenuItem3);
        if (!radioButtonMenuItem2.isSelected() && !radioButtonMenuItem3.isSelected()) {
            if (film_list_order_mode==2) {
                radioButtonMenuItem2.setSelected(true);
            } else {
                radioButtonMenuItem3.setSelected(true);
            }
        }
        radioButtonMenuItem2.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                frame.dispose();
                current_page = 1;
                if (radioButtonMenuItem2.isSelected()) {
                    film_list_order_mode = 2;
                    frame2PickFilm(film_list_order_mode);
                } else {
                    film_list_order_mode = 3;
                    frame2PickFilm(film_list_order_mode);
                }
            }
        });
        menuBar.add(menu2Options);

        // 菜单1
        JMenu menu2getTicket = new JMenu("取票");
        JMenuItem menuItem1 = new JMenuItem("打开");
        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new GetTicketView().getTicket(new JFrame("取票界面"));
            }
        });
        menu2getTicket.add(menuItem1);
        menuBar.add(menu2getTicket);
        // 菜单2
        JMenu menu2member = new JMenu("个性化");
        JMenuItem menuItem2= new JMenuItem("打开");
        menuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MemberView().Page2Member();
            }
        });
        menu2member.add(menuItem2);
        menuBar.add(menu2member);
        // 菜单3
        JMenu menu2admin = new JMenu("管理员");
        JMenuItem menuItem3= new JMenuItem("打开");
        menuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = JOptionPane.showInputDialog("请输入管理员口令:");;
                while (!password.equals(AdminMain.PASSWORD)) {
                    password = JOptionPane.showInputDialog("口令有误，请重新输入:");
                }   // if cancel, return null, throws NullPointerException, no further downward run
                frame.dispose();
                AdminMain.frame2Admin();
            }
        });
        menu2admin.add(menuItem3);
        menuBar.add(menu2admin);
        // menuBar 添加至 frame 中
        frame.setJMenuBar(menuBar);
    }

    public static void page2PickFilm(JFrame frame, int pageSize, int filmListMode)    //选票页面
    {
        // 创建选票页面
        JPanel jp = new JPanel();
        jp.setLayout(null);
        // 设置每项电影条目
        ArrayList<Film> filmList = new Conn().getFilmList(filmListMode);
        for(int i = 0; i< FILMS_IN_A_PAGE; i++) {
            item2FilmList(frame, jp, i, filmList);
        }
        // 设置上一页、下一页按钮
        btn2PageUpDown(frame,jp,true,pageSize,filmListMode);
        btn2PageUpDown(frame,jp,false,pageSize,filmListMode);
        // 配置面板
        frame.add(jp);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * set item of filmList in a page
     * @param itemIndex item index of current page
     */
    public static void item2FilmList(JFrame frame, JPanel jp, int itemIndex, ArrayList<Film> filmList) {
        Film film = filmList.get(FILMS_IN_A_PAGE*current_page-1+itemIndex-1);
        // 设置电影封面
        ImageIcon img = new ImageIcon(film.getUrl());
        img.setImage(img.getImage().getScaledInstance(90, 150, Image.SCALE_DEFAULT));
        JLabel label1 = new JLabel(img);
        label1.setBounds(20, 10+154*itemIndex, 90, 150);
        jp.add(label1);
        // 设置电影内容介绍
        JLabel label2 = new JLabel("<html>" + film.getName() + "<br>" + film.getInfo() +
                "<br>￥" + film.getPrice() + "<br>sales:" + new Conn().getSales(film.getId()) + "<br><br>"
                + film.getDate() + "</html>");
        label2.setBounds(110, 10+150*itemIndex, 90, 150);
        jp.add(label2);
        // 设置订票按钮
        ImageIcon img2 = new ImageIcon("src/res/icons/movie-ticket.png");
        img2.setImage(img2.getImage().getScaledInstance(50, 30, Image.SCALE_DEFAULT));
        JButton btn3 = new JButton(img2);
        btn3.setFocusPainted(false);
        btn3.setBounds(200, 70+150*itemIndex, 50, 30);
        btn3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {  // 跳转到订票界面
                frame.dispose();
                BookTicketView.page2book(film.getId());
            }
        });
        jp.add(btn3);
        //添加标签到面板
    }

    /**
     * set Buttons: PageUp and PageDown
     * @param isUp if true: set btn-pageUp
     */
    public static void btn2PageUpDown(JFrame jf, JPanel jp, boolean isUp, int pageSize, int filmListMode) {
        String imgUrl;   // 按钮背景图片地址
        int[] btnBounds; // 按钮位置大小: x'y'w'h
        // 设置不同按钮使用的参数
        if (isUp) {
            imgUrl = "src/res/icons/pre.png";
            btnBounds = new int[]{40, 330, 70, 40};

        } else {
            imgUrl = "src/res/icons/next.png";
            btnBounds = new int[]{160, 330, 70, 40};
        }
        // 按钮背景配置
        ImageIcon img = new ImageIcon(imgUrl);
        img.setImage(img.getImage().getScaledInstance(70, 40, Image.SCALE_DEFAULT));
        // 按钮配置
        JButton btn = new JButton(img);
        btn.setBounds(btnBounds[0],btnBounds[1],btnBounds[2],btnBounds[3]);
        btn.setFocusPainted(false);
        if ((isUp && current_page==1) || (!isUp && current_page==pageSize)) {
            btn.setEnabled(false);
        } else {
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 清空此页内容
                    jf.getContentPane().removeAll();
                    // 跳转下一页
                    if (isUp) {
                        --current_page;
                    } else {
                        ++current_page;
                    }
                    page2PickFilm(jf, pageSize, filmListMode);
                }
            });
        }
        jp.add(btn);
    }
}
