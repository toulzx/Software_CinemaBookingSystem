package entity;

public class Theater {

    private Integer id;
    private String name;
    private Integer rows;
    private Integer columns;
    private Float premiumRadio=1.0f;

    /** 与构造函数一一对应
     * 此数组在管理员界面下被引用，需保证对应顺序
     * */
    public static final String[] COLUMN_NAMES = {"编号","厅名","最大行数","最大列数","票价加成"};

    public Theater(Integer id, String name, Integer rows, Integer columns, Float premiumRadio) {
        this.id = id;
        this.name = name;
        this.rows = rows;
        this.columns = columns;
        this.premiumRadio = premiumRadio;
    }

    public Theater() {
    }

    public boolean setVarByIndex(int index, String value) {
        int i;
        switch (index) {
            case 1:
                setName(value);
                return true;
            case 2:
                i = Integer.parseInt(value);
                if (i>0) {
                    setRows(Integer.valueOf(value));
                    return true;
                }
            case 3:
                i = Integer.parseInt(value);
                if (i>0) {
                    setColumns(Integer.valueOf(value));
                    return true;
                }
            case 4:
                if (Float.parseFloat(value) >= 1.0) {
                    setPremiumRadio(Float.valueOf(value));
                    return true;
                }
            case 0:
            default: return false;
        }
    }

    public String getVarByIndex(int index) {
        switch (index) {
            case 0: return id.toString();
            case 1: return name;
            case 2: return rows.toString();
            case 3: return columns.toString();
            case 4: return premiumRadio.toString();
            default: return "";
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getColumns() {
        return columns;
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public Float getPremiumRadio() {
        return premiumRadio;
    }

    public void setPremiumRadio(Float premiumRadio) {
        this.premiumRadio = premiumRadio;
    }

}
