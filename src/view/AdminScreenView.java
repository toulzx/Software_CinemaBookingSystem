package view;

import action.Conn;
import action.Utils;
import entity.FilmScreen;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Vector;

public class AdminScreenView extends JFrame {

    JPanel panel;
    JScrollPane jsp;
    DefaultTableModel tableModel;
    JTable table;

    ArrayList<FilmScreen> screenList;
    ArrayList<FilmScreen> updateScreenList;

    JButton btnAdd;
    JButton btnUpdate;
    JButton btnDelete;

    int currentFilmId;

    public JPanel getPanel() {
        return panel;
    }

    AdminScreenView(int id) {
        currentFilmId = id;
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
        tableModel = new DefaultTableModel(FilmScreen.COLUMNS_NAME, 0) {
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
                    FilmScreen screen = new Conn().getScreenById((Integer) tableModel.getValueAt(e.getLastRow(),0));
                    String originalValue = screen.getVarByIndex(e.getColumn());
                    System.out.println(e.getLastRow() + "," + e.getColumn() + ": "  + originalValue + " => " + currentValue);       // test
                    if (!currentValue.trim().equals(originalValue.trim())) {
                        if(!screen.setVarByIndex(e.getColumn(),currentValue)) {
                            JOptionPane.showMessageDialog(null, "数据操作出错","状态", JOptionPane.ERROR_MESSAGE);
                            tableModel.setValueAt(originalValue, e.getFirstRow(), e.getColumn());
                        } else {
                            if (updateScreenList == null) {
                                updateScreenList = new ArrayList<>();
                                updateScreenList.add(screen);
                            } else {
                                updateScreenList.add(screen);
                            }
                        }
                    }
                }
            }
        });
        // 获取并插入数据
        screenList = new Conn().getScreenListByFilmId(currentFilmId);
        for (FilmScreen item : screenList) {
            Vector rowV = new Vector<>();
            rowV.add(item.getId());
            rowV.add(item.getFilmId());
            rowV.add(item.getStartDay());
            rowV.add(item.getTime());
            rowV.add(item.getTheaterId());
            tableModel.addRow(rowV);
        }
    }

    public void setButtons() {
        // 添加按钮
        btnAdd = new JButton("添加");
        ImageIcon imgAdd = new ImageIcon("src/res/icons/addBtn.png");
        imgAdd.setImage(imgAdd.getImage().getScaledInstance(135, 30,Image.SCALE_DEFAULT));
        btnAdd.setIcon(imgAdd);
        btnAdd.setBounds(150,510,120, 30);
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
        // 删除按钮
        btnDelete = new JButton("删除");
        ImageIcon imgDelete = new ImageIcon("src/res/icons/delBtn.png");
        imgDelete.setImage(imgDelete.getImage().getScaledInstance(100, 30,Image.SCALE_DEFAULT));
        btnDelete.setIcon(imgDelete);
        btnDelete.setBounds(410,510,90, 30);
        btnDelete.addActionListener(new BtnDeleteAction());
        panel.add(btnDelete);
    }

    class BtnAddAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae) {
            JFrame jf = null;
            try {
                jf = new AddScreenView().showFilmScreenAddView(currentFilmId, tableModel);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            jf.setLocationRelativeTo(null);
            jf.setVisible(true);
        }
    }

    class BtnUpdateAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (updateScreenList==null || updateScreenList.isEmpty()) {
                JOptionPane.showMessageDialog(null,"没有数据需要更新","状态", JOptionPane.WARNING_MESSAGE);
                return;
            }
            boolean flag = true;
            for (FilmScreen item: updateScreenList) {
                if (!new Conn().updateScreen(item)){
                    flag = false;
                    break;
                }
            }
            JOptionPane.showMessageDialog(null, flag?"更新成功":"更新失败","状态", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    class BtnDeleteAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae) {
            int row = table.getSelectedRow();
            if (row != -1){ //这句判断是否有选中的行
                int id = (int)table.getValueAt(row, 0);
                if (!new Conn().deleteScreenById(id)) {
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
