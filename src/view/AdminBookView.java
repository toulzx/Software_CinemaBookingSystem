package view;


import action.Conn;
import action.Utils;
import entity.BookInfo;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class AdminBookView {

    JPanel panel;
    JTable table;
    DefaultTableModel tableModel;
    JScrollPane jsp;

    ArrayList bookList;

    JButton btnDelete;
    JButton btnStatus;

    public static final String[] COLUMNS_NAME = {"编号","电影","放映时间","放映厅","座位","状态"};

    public JPanel getPanel() {
        return panel;
    }

    AdminBookView() {
        // table
        setTableModel();
        table=new JTable(tableModel);
        new Utils().setTableAttribute(table,tableModel);
        // scroll panel
        jsp = new JScrollPane();
        jsp.setBounds(20,0,650,500);
        jsp.setViewportView(table);
        // panel
        panel = new JPanel();
        panel.setSize(600, 600);
        panel.setLayout(null);
        panel.add(jsp);
        // button
        setButtons();
    }

    public void setTableModel() {
        tableModel =new DefaultTableModel(COLUMNS_NAME, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if(e.getType() == TableModelEvent.UPDATE) {
                    // no need for updating, so do nothing
                }
            }
        });
        // 获取并插入数据
        bookList = new Conn().getBookList();
        for (Object o : bookList) {
            Vector rowV = new Vector();
            BookInfo bookInfo = (BookInfo) o;
            rowV.add(bookInfo.getId());
            // 通过 screenId 查找电影名称
            rowV.add( new Conn().getFilmNameByScreenId(bookInfo.getScreenId()) );
            // 通过 screenId 查找放映日期时间
            rowV.add( new Conn().IdtFilmTime(bookInfo.getScreenId()) );
            // 通过 screenId 查找放映厅
            rowV.add( new Conn().IdtTheaterName(bookInfo.getScreenId()) );
            // 查找座位
            rowV.add(bookInfo.getSeatX() + "行" + bookInfo.getSeatY() + "列");
            // 出票状态
            if (bookInfo.getStatus()) {
                rowV.add("已取票");
            } else {
                rowV.add("未出票");
            }
            tableModel.addRow(rowV);
        }
    }

    public void setButtons() {
        // 删除行按钮
        btnDelete = new JButton("删除");
        ImageIcon imgDeleteRow = new ImageIcon("src/res/icons/delBtn.png");
        imgDeleteRow.setImage(imgDeleteRow.getImage().getScaledInstance(100, 30,Image.SCALE_DEFAULT));
        btnDelete.setIcon(imgDeleteRow);
        btnDelete.setBounds(210,510,90, 30);
        btnDelete.addActionListener(new BtnDeleteAction());
        panel.add(btnDelete);
        // 取票按钮
        btnStatus = new JButton("出票");
        btnStatus.setBounds(460,510,90, 30);
        btnStatus.setBackground(Color.pink);
        btnStatus.addActionListener(new BtnStatusAction());
        panel.add(btnStatus);
    }


    class BtnStatusAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int confirmValue = JOptionPane.showConfirmDialog(null,
                    "是否取票？" , "确认信息", JOptionPane.YES_NO_OPTION);
            if (confirmValue == 0) {
                int rowId = Integer.parseInt(tableModel.getValueAt(table.getSelectedRow(),0).toString());
                BookInfo bookInfo = new Conn().getBookById(rowId);
                if (!bookInfo.getStatus()) {
                    if (new Conn().updateStatus(bookInfo.getTel(),bookInfo.getValidCode())) {
                        tableModel.setValueAt("已出票",table.getSelectedRow(),COLUMNS_NAME.length-1);
                        JOptionPane.showMessageDialog(null,"出票成功","状态",JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null,"出票失败！","状态",JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null,"票已被取出！","状态",JOptionPane.ERROR_MESSAGE);
                }
            }

        }
    }
    class BtnDeleteAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae) {
            int row= table.getSelectedRow();
            if(row!=-1) {    // 判断是否有选中的行
                int id = (int)table.getValueAt(row, 0);
                boolean status = new Conn().getStatusById(id);
                if (!new Conn().deleteBookById(id)) {
                    JOptionPane.showMessageDialog(null, "数据库删除失败", "状态", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("数据删除失败");
                } else if (status) {
                    JOptionPane.showMessageDialog(null, "票已取出不可删除", "状态", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("数据删除失败");
                } else {
                    tableModel.removeRow(row);
                    JOptionPane.showMessageDialog(null, "删除成功", "状态", JOptionPane.ERROR_MESSAGE);
                    System.out.println("数据删除成功");
                }
            } else {
                JOptionPane.showMessageDialog(null, "请选择一行",  "状态", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

}
