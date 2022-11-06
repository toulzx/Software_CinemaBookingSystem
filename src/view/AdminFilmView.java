package view;

import action.Conn;
import action.Utils;
import entity.Film;
import jxl.Sheet;
import jxl.Workbook;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class AdminFilmView {

    JPanel panel;
    JTable table;
    DefaultTableModel tableModel;
    JScrollPane jsp;

    JButton btnUpdate;
    JButton btnImport;
    JButton btnDeleteRow;
    JButton btnScreenManage;

    ArrayList<Film> filmList;
    ArrayList<Film> updateFilmList;

    int selectedFilmId;
    int selectedRow;

    public int getSelectedFilmId() {
        return selectedFilmId;
    }

    public void setSelectedFilmId(int filmId) {
        this.selectedFilmId = filmId;
    }

    public JPanel getPanel() {
        return panel;
    }

    AdminFilmView() {
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
        // 设置表头，数据稍后导入
        tableModel = new DefaultTableModel(Film.COLUMN_NAMES, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if(e.getType() == TableModelEvent.UPDATE){
                    String currentValue = tableModel.getValueAt(e.getFirstRow(),e.getColumn()).toString();
                    Film film = new Conn().getFilmById((Integer) tableModel.getValueAt(e.getLastRow(),0));
                    String originalValue = film.getVarByIndex(e.getColumn());
                    System.out.println(e.getLastRow() + "," + e.getColumn() + ": "  + originalValue + " => " + currentValue);       // test
                    if (!currentValue.trim().equals(originalValue.trim())) {
                        if(!film.setVarByIndex(e.getColumn(),currentValue)) {
                            JOptionPane.showMessageDialog(null, "请检查日期格式！","状态", JOptionPane.ERROR_MESSAGE);
                            tableModel.setValueAt(originalValue, e.getFirstRow(), e.getColumn());
                        } else {
                            if (updateFilmList == null) {
                                updateFilmList = new ArrayList<>();
                                updateFilmList.add(film);
                            } else if (updateFilmList.isEmpty()) {
                                updateFilmList.add(film);
                            } else {
//                            if (checkDuplication(updateFilmList, film)) {
//                                updateFilmList.add(film);
//                            }
                                updateFilmList.add(film);
                            }
                        }
                    }
                }
            }
        });
        // 获取并插入数据
        filmList = new Conn().getFilmList(1);
        for (Film film : filmList) {
            Vector rowV = new Vector<>();
            rowV.add(film.getId());
            rowV.add(film.getName());
            rowV.add(film.getInfo());
            rowV.add(film.getUrl());
            rowV.add(film.getDate().toString());    // 日期形式则无法修改
            rowV.add(film.getPrice());
            rowV.add(film.getDiscount());
            tableModel.addRow(rowV);
        }
    }

    public void setButtons() {
        // 更新数据按钮
        btnUpdate = new JButton("更新");
//        ImageIcon imgUpdate = new ImageIcon("src/res/icons/addBatch.png");      // 未改
//        imgUpdate.setImage(imgUpdate.getImage().getScaledInstance(140, 30,Image.SCALE_DEFAULT));
//        btnUpdate.setIcon(imgUpdate);
        btnUpdate.setBounds(120,510,120, 30);
        btnUpdate.addActionListener(new BtnUpdateAction());
        panel.add(btnUpdate);
        // 导入数据按钮
        btnImport = new JButton("从Excel批量导入");
        ImageIcon imgImport = new ImageIcon("src/res/icons/addBatch.png");
        imgImport.setImage(imgImport.getImage().getScaledInstance(140, 30,Image.SCALE_DEFAULT));
        btnImport.setIcon(imgImport);
        btnImport.setBounds(270,510,120, 30);
        btnImport.addActionListener(new BtnImportAction());
        panel.add(btnImport);
        // 删除行按钮
        btnDeleteRow = new JButton("删除");
        ImageIcon imgDeleteRow = new ImageIcon("src/res/icons/delBtn.png");
        imgDeleteRow.setImage(imgDeleteRow.getImage().getScaledInstance(100, 30,Image.SCALE_DEFAULT));
        btnDeleteRow.setIcon(imgDeleteRow);
        btnDeleteRow.setBounds(410,510,90, 30);
        btnDeleteRow.addActionListener(new BtnDeleteAction());
        panel.add(btnDeleteRow);
        // 场次管理按钮
        btnScreenManage = new JButton("场次管理");
        ImageIcon imgScreenManage = new ImageIcon("src/res/icons/manageScreenBtn.png");
        imgScreenManage.setImage(imgScreenManage.getImage().getScaledInstance(100, 30,Image.SCALE_DEFAULT));
        btnScreenManage.setIcon(imgScreenManage);
        btnScreenManage.setBounds(510,510,90, 30);
        btnScreenManage.addActionListener(new BtnScreenManageAction());
        panel.add(btnScreenManage);
    }

    /**
     * 从 excel 导入数据
     */
    public void importFilmsFromExcel(File file ) throws Exception {
        // 创建输入流，读取Excel
        InputStream is = new FileInputStream(file.getAbsolutePath());
        // jxl提供的Workbook类
        Workbook wb = Workbook.getWorkbook(is);
        // 创建一个Sheet对象
        Sheet sheet = wb.getSheet("Sheet1");
        //创建外list，存储每个电影
        List<List> outerList= new ArrayList<>();
        // sheet.getRows()返回该页的总行数
        for (int i = 1; i < sheet.getRows(); i++) {
            //创建内list，存储每个电影的具体内容
            List innerList=new ArrayList();
            // sheet.getColumns()返回该页的总列数
            for (int j = 0; j < sheet.getColumns(); j++) {
                //获取单元格内的内容
                String cellInfo = sheet.getCell(j, i).getContents();
                if(cellInfo.isEmpty()){
                    continue;
                }
                //内list添加内容
                innerList.add(cellInfo);
            }
            //外list添加内容
            outerList.add(i-1, innerList);
        }
        for (List temp : outerList) {
            for (Object o : temp) {
                System.out.println(o);
            }
        }
        if (new Conn().addFilm((outerList))){
            System.out.println("导入成功！");
        }
    }

    public boolean checkDuplication(ArrayList<Film> list, Film film)    // 检查更新内容在之前的队列中是否存在，写完发现不用
    {
        Film temp = new Film();
        for(Film item : list) {
            if(item.getId().equals(film.getId())) {
                temp = item;
                break;
            }
        }
        return temp.equal(film);
    }

    class BtnUpdateAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (updateFilmList==null || updateFilmList.isEmpty()) {
                JOptionPane.showMessageDialog(null,"没有数据需要更新","状态", JOptionPane.WARNING_MESSAGE);
                return;
            }
            boolean flag = true;
            for (Film item: updateFilmList) {
                if (!new Conn().updateFilm(item)){
                    flag = false;
                    break;
                }
            }
            JOptionPane.showMessageDialog(null, flag?"更新成功":"更新失败","状态", JOptionPane.INFORMATION_MESSAGE);
            // 不要执行  `tableModel.fireTableDataChanged();` ， 会使得 `tableChanged` 报错 index 溢出
        }
    }

    class BtnImportAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                importFilmsFromExcel(new File(new Utils().showFileOpenDialog()));
                JOptionPane.showMessageDialog(null, "导入成功","成功", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            tableModel.fireTableDataChanged();
        }
    }

    class BtnDeleteAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae) {
            int row= table.getSelectedRow();
            if(row!=-1) {    // 判断是否有选中的行
                int id = (int)table.getValueAt(row, 0);
                if (!new Conn().deleteFilmById(id)) {
                    JOptionPane.showMessageDialog(null, "数据库删除失败", "状态", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    tableModel.removeRow(row);
                    JOptionPane.showMessageDialog(null, "删除成功", "状态", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "请选择一行",  "状态", JOptionPane.WARNING_MESSAGE);
            }
            // 不要执行  `tableModel.fireTableDataChanged();` ， 会使得 `tableChanged` 报错 index 溢出
        }
    }

    class BtnScreenManageAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae) {
            selectedRow = table.getSelectedRow();
            if (selectedRow <0){
                JOptionPane.showMessageDialog(null, "未选择电影！","失败",JOptionPane.ERROR_MESSAGE);
            } else {
                setSelectedFilmId((int)table.getValueAt(selectedRow, 0)); //这句选择要管理的电影ID
                JFrame jf =new JFrame("场次管理");
                jf.setSize(700,650);
                jf.setLocationRelativeTo(null);
                jf.add(new AdminScreenView(getSelectedFilmId()).getPanel());
                jf.setVisible(true);
                // 不要执行  `tableModel.fireTableDataChanged();` ， 会使得 `tableChanged` 报错 index 溢出
            }

        }
    }

}
