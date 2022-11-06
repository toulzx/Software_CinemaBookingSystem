package view;

import action.Conn;
import action.Utils;
import entity.Theater;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

public class AdminTheaterView {

    JPanel panel;
    JScrollPane jsp;
    DefaultTableModel tableModel;
    JTable table;

    ArrayList<Theater> theaterList;
    ArrayList<Theater> updateTheaterList;

    JButton btnAdd;
    JButton btnUpdate;
    JButton btnDelete;

    public JPanel getPanel() {return panel;}

    AdminTheaterView() {
        // table
        setTableModel();
        table = new JTable(tableModel);
        new Utils().setTableAttribute(table,tableModel);
        // scroll panel
        jsp = new JScrollPane();
        jsp.setBounds(20,0,640,500);
        jsp.setViewportView(table);
        // panel
        panel = new JPanel();
        panel.setSize(600, 600);
        panel.setLayout(null);
        panel.add(jsp);
        // button
        setButtons();
    }

    private void setTableModel() {
        tableModel = new DefaultTableModel(Theater.COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column !=0;
            }
        };
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if(e.getType() == TableModelEvent.UPDATE) {
                    String currentValue = tableModel.getValueAt(e.getFirstRow(),e.getColumn()).toString();
                    Theater theater = new Conn().getTheaterById((Integer) tableModel.getValueAt(e.getLastRow(),0));
                    String originalValue = theater.getVarByIndex(e.getColumn());
                    System.out.println(e.getLastRow() + "," + e.getColumn() + ": "  + originalValue + " => " + currentValue);       // test
                    if (!currentValue.trim().equals(originalValue.trim())) {
                        if(!theater.setVarByIndex(e.getColumn(),currentValue)) {
                            JOptionPane.showMessageDialog(null, "数据操作出错","状态", JOptionPane.ERROR_MESSAGE);
                            tableModel.setValueAt(originalValue, e.getFirstRow(), e.getColumn());
                        } else {
                            if (updateTheaterList == null) {
                                updateTheaterList = new ArrayList<>();
                                updateTheaterList.add(theater);
                            } else {
                                updateTheaterList.add(theater);
                            }
                        }
                    }
                }
            }
        });
        // 获取并插入数据
        theaterList = new Conn().getTheaterList();
        for (Theater theater : theaterList) {
            Vector rowV = new Vector<>();
            rowV.add(theater.getId());
            rowV.add(theater.getName());
            rowV.add(theater.getRows());
            rowV.add(theater.getColumns());
            rowV.add(theater.getPremiumRadio());
            tableModel.addRow(rowV);
        }
    }

    public void setButtons() {    //主要面板
        // 添加数据按钮
        btnAdd = new JButton("添加");
        ImageIcon imgAdd = new ImageIcon("src/res/icons/addBtn.png");
        imgAdd.setImage(imgAdd.getImage().getScaledInstance(100,30,Image.SCALE_DEFAULT));
        btnAdd.setIcon(imgAdd);;
        btnAdd.setBounds(120,510,120, 30);
        btnAdd.addActionListener(new BtnAddAction());
        panel.add(btnAdd);

        // 更新数据按钮
        btnUpdate = new JButton("更新");
//        ImageIcon imgUpdate = new ImageIcon("src/res/icons/addBatch.png");      // 未改
//        imgUpdate.setImage(imgUpdate.getImage().getScaledInstance(100, 30,Image.SCALE_DEFAULT));
//        btnUpdate.setIcon(imgUpdate);
        btnUpdate.setBounds(330,510,90, 30);
        btnUpdate.addActionListener(new BtnUpdateAction());
        panel.add(btnUpdate);

        btnDelete = new JButton("删除");
        ImageIcon icon4=new ImageIcon("res/icons/delBtn.png");
        icon4.setImage(icon4.getImage().getScaledInstance(100,30,Image.SCALE_DEFAULT));
        btnDelete.setIcon(icon4);
        btnDelete.setBounds(460,510,90, 30);
        btnDelete.addActionListener(new BtnDeleteAction());
        panel.add(btnDelete);

    }

    class BtnAddAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new AddTheaterView().Page2AddTheater(tableModel);
        }
    }

    class BtnUpdateAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (updateTheaterList==null || updateTheaterList.isEmpty()) {
                JOptionPane.showMessageDialog(null,"没有数据需要更新","状态", JOptionPane.WARNING_MESSAGE);
                return;
            }
            boolean flag = true;
            for (Theater item: updateTheaterList) {
                if (!new Conn().updateTheater(item)){
                    flag = false;
                    break;
                }
            }
            JOptionPane.showMessageDialog(null, flag?"更新成功":"更新失败","状态", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    class BtnDeleteAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int row = table.getSelectedRow();
            if (row != -1){ //这句判断是否有选中的行
                int id = (int)table.getValueAt(row, 0);
                if (!new Conn().deleteTheaterById(id)) {
                    JOptionPane.showMessageDialog(null, "数据库删除失败", "状态", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    tableModel.removeRow(row);
                    JOptionPane.showMessageDialog(null, "删除成功", "状态", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "请选择一行",  "状态", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

}

