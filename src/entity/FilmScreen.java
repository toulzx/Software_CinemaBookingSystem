package entity;

import view.AddScreenView;

import java.util.Date;

/**
 * @author toulzx
 */
public class FilmScreen {
    private Integer id;
    private Integer filmId;
    private Date startDay;
    private String time;
    private Integer theaterId;


    /** 与构造函数一一对应
     * 此数组在管理员界面下被引用，需保证对应顺序
     * */
    public static final String[] COLUMNS_NAME = {"编号","电影编号","放映日期","放映时间","放映厅编号"};

    public FilmScreen(Integer id, Integer filmId, Date startDay, String time, Integer theaterId) {
        this.id = id;
        this.filmId = filmId;
        this.startDay = startDay;
        this.time = time;
        this.theaterId = theaterId;
    }

    public FilmScreen() {
    }

    public boolean setVarByIndex(int index, String value) {
        switch (index) {
            case 1:
                setFilmId(Integer.valueOf(value));
                return true;
            case 2:
                try {
                    setStartDay(java.sql.Date.valueOf(value));
                    return true;
                } catch(IllegalArgumentException e) {
                    e.printStackTrace();
                    System.out.println("日期格式有误！");
                    return false;
                }
            case 3:
                if (AddScreenView.ValidTime(value)) {
                    setTime(value);
                    return true;
                }
                System.out.println("时间格式有误！");
            case 4:
                setTheaterId(Integer.valueOf(value));
                return true;
            case 0:
            default: return false;
        }
    }

    public String getVarByIndex(int index) {
        switch (index) {
            case 0: return id.toString();
            case 1: return filmId.toString();
            case 2: return startDay.toString();
            case 3: return time;
            case 4: return theaterId.toString();
            default: return "";
        }
    }

    // getter and setter below

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getFilmId() {
        return filmId;
    }
    public void setFilmId(Integer filmId) {
        this.filmId = filmId;
    }
    public Date getStartDay() {
        return startDay;
    }
    public void setStartDay(Date startDay) {
        this.startDay = startDay;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public Integer getTheaterId() {
        return theaterId;
    }
    public void setTheaterId(Integer theaterId) {
        this.theaterId = theaterId;
    }
}
