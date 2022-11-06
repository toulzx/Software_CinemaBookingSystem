package action;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.File;
import java.util.Random;

public class Utils {

    private static final String GENERAL_MEMBER = "普通会员（无折扣）";
    private static final int GENERAL_MEMBER_ID = 0;
    private static final double GENERAL_MEMBER_DISCOUNT = 1;
    private static final int BRONZE_MEMBER_LEAST_BOOK_COUNT = 5;
    private static final String BRONZE_MEMBER = "青铜会员（98折）";
    private static final int BRONZE_MEMBER_ID = 1;
    private static final double BRONZE_MEMBER_DISCOUNT = 0.98;
    private static final int SILVER_MEMBER_LEAST_BOOK_COUNT = 15;
    private static final String SILVER_MEMBER = "白银会员（95折）";
    private static final int SILVER_MEMBER_ID = 2;
    private static final double SILVER_MEMBER_DISCOUNT = 0.95;
    private static final int GOLD_MEMBER_LEAST_BOOK_COUNT = 30;
    private static final String GOLD_MEMBER = "黄金会员（9折）";
    private static final int GOLD_MEMBER_ID = 3;
    private static final double GOLD_MEMBER_DISCOUNT = 0.9;

    public String generateValidCode()
    {
        String []code = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o",
                "p","q","r","s","t","u","v","w","x","y","z","1","2","3","4","5","6","7","8","9"};
        String rds = "";
        Random random = new Random();
        int k = 0;
        for(int i=0;i<6;i++)
        {
            k = random.nextInt(35);
            rds = rds + code[k];
        }
        return rds;
    }

    /**
     * @return GOLD_MEMBER(see static variables); SQLException(-1); full-level(0)
     */
    public int getMemberShipLevelLimit(String tel) {
        int count = new Conn().getMemberBookCount(tel);
        if (count == -1) {
            return -1;
        } else if (count < BRONZE_MEMBER_LEAST_BOOK_COUNT) {
            return BRONZE_MEMBER_LEAST_BOOK_COUNT;
        } else if (count < SILVER_MEMBER_LEAST_BOOK_COUNT) {
            return SILVER_MEMBER_LEAST_BOOK_COUNT;
        } else if (count < GOLD_MEMBER_LEAST_BOOK_COUNT) {
            return GOLD_MEMBER_LEAST_BOOK_COUNT;
        } else {
            return 0;
        }
    }

    /**
     * @return GOLD_MEMBER(see static variables); SQLException("")
     */
    public String getMemberShipLevelName(String tel) {
        int count = new Conn().getMemberBookCount(tel);
        if (count == -1) {
            return "";
        } else if (count < BRONZE_MEMBER_LEAST_BOOK_COUNT) {
            return GENERAL_MEMBER;
        } else if (count < SILVER_MEMBER_LEAST_BOOK_COUNT) {
            return BRONZE_MEMBER;
        } else if (count < GOLD_MEMBER_LEAST_BOOK_COUNT) {
            return SILVER_MEMBER;
        } else {
            return GOLD_MEMBER;
        }
    }

    /**
     * @return GOLD_MEMBER(see static variables); SQLException(-1)
     */
    public int getMemberShipLevel(String tel) {
        int count = new Conn().getMemberBookCount(tel);
        if (count == -1) {
            return -1;
        } else if (count < BRONZE_MEMBER_LEAST_BOOK_COUNT) {
            return GENERAL_MEMBER_ID;
        } else if (count < SILVER_MEMBER_LEAST_BOOK_COUNT) {
            return BRONZE_MEMBER_ID;
        } else if (count < GOLD_MEMBER_LEAST_BOOK_COUNT) {
            return SILVER_MEMBER_ID;
        } else {
            return GOLD_MEMBER_ID;
        }
    }

    public double getMemberShipDiscount(String tel) {
        int level = getMemberShipLevel(tel);
        switch (level) {
            case GENERAL_MEMBER_ID:
                return GENERAL_MEMBER_DISCOUNT;
            case BRONZE_MEMBER_ID:
                return  BRONZE_MEMBER_DISCOUNT;
            case SILVER_MEMBER_ID:
                return SILVER_MEMBER_DISCOUNT;
            case GOLD_MEMBER_ID:
                return GOLD_MEMBER_DISCOUNT;
            default:
                System.out.println("Alert: 会员折扣计算出现问题");
                return 1;
        }
    }

    /**
     * JTable Attributes Setting for AdminView
     */
    public void setTableAttribute(JTable table, DefaultTableModel tableModel) {
        // 使用 表格模型 创建 行排序器（TableRowSorter 实现了 RowSorter）
        RowSorter<DefaultTableModel> rowSorter = new TableRowSorter<>(tableModel);
        // 给表格设置行排序器
        table.setRowSorter(rowSorter);
        // 设置编辑器
//        TableCellEditor cellEditor = new CellEditor(new J)
        // 单选
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // 设置表格内容颜色
        table.setForeground(Color.BLACK);
        table.setFont(new Font(null, Font.PLAIN, 14));
        table.setSelectionForeground(Color.DARK_GRAY);
        table.setSelectionBackground(Color.LIGHT_GRAY);
        table.setGridColor(Color.GRAY);
        // 设置表头名称字体样式
        table.getTableHeader().setFont(new Font(null, Font.BOLD, 14));
        table.getTableHeader().setForeground(Color.RED);
        // 设置不允许手动改变列宽
        table.getTableHeader().setResizingAllowed(false);
        // 设置不允许拖动重新排序各列
        table.getTableHeader().setReorderingAllowed(false);
        // 设置行高
        table.setRowHeight(30);
        // 第一列列宽设置为40
        table.getColumnModel().getColumn(0).setPreferredWidth(10);
        // 设置滚动面板视口大小（超过该大小的行数据，需要拖动滚动条才能看到）
        table.setPreferredScrollableViewportSize(new Dimension(400, 300));
    }

    public String showFileOpenDialog() {
        // 创建一个默认的文件选取器
        JFileChooser fileChooser = new JFileChooser();

        // 设置默认显示的文件夹为当前文件夹
        fileChooser.setCurrentDirectory(new File("."));

        // 设置文件选择的模式（只选文件)
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // 设置是否允许多选
        fileChooser.setMultiSelectionEnabled(false);

        // 设置默认使用的文件过滤器
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel(*.xls)", "xls"));

        // 打开文件选择框（线程将被阻塞, 直到选择框被关闭）
        int result = fileChooser.showOpenDialog(null);
        File file = fileChooser.getSelectedFile();
        if (result == JFileChooser.APPROVE_OPTION) {
            // 如果点击了"确定", 则获取选择的文件路径
            // 如果允许选择多个文件, 则通过下面方法获取选择的所有文件
            // File[] files = fileChooser.getSelectedFiles();
            System.out.println("打开文件: " + file.getAbsolutePath() + "\n\n");
        }
        return file.getAbsolutePath();
    }
}
