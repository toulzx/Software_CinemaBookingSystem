package entity;

import java.util.Date;

public class Film {
    private Integer id;
    private  String name;
    private String info;
    private  String  url;
    private  Date time;
    private Float price = 100.0f;
    private Float discount = 0.0f;


    /** 与构造函数一一对应
     * 此数组在管理员界面下被引用，需保证对应顺序
     * */
    public static final String[] COLUMN_NAMES = {"编号","名称","简介","封面","时间","票价","折扣"};

    public Film(Integer id, String name, String info, String url, Date time, Float price, Float discount) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.url = url;
        this.time = time;
        this.price = price;
        this.discount = discount;
    }

    public Film() {
    }

    public boolean equal(Film film) {
        return id.equals(film.getId()) && name.equals(film.getName()) && info.equals(film.getInfo()) &&
                url.equals(film.getUrl()) && time.equals(film.getTime()) && price.equals(film.getPrice()) &&
                discount.equals(film.getDiscount());
    }

    public boolean setVarByIndex(int index, String value) {
        float f;
        switch (index) {
            case 1:
                setName(value);
                return true;
            case 2:
                setInfo(value);
                return true;
            case 3:
                setUrl(value);
                return true;
            case 4:
                try {
                    setTime(java.sql.Date.valueOf(value));
                    return true;
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    return false;
                }
            case 5:
                f = Float.parseFloat(value);
                if (f > 0.0) {
                    setPrice(f);
                    return true;
                }
            case 6:
                f = Float.parseFloat(value);
                if (f>0 && f<=1.0) {
                    setDiscount(f);
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
            case 2: return info;
            case 3: return url;
            case 4: return time.toString();
            case 5: return price.toString();
            case 6: return  discount.toString();
            default: return "";
        }
    }

    // getter and setter

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }

    public void setName(String n)
    {
        name = n;
    }

    public String getName()
    {
        return name;
    }

    public void setInfo(String i)
    {
        info = i;
    }

    public String getInfo()
    {
        return info;
    }

    public void setUrl(String u)
    {
        url = u;
    }

    public String getUrl()
    {
        return url;
    }

    public void setDate(Date d)
    {
        time = d;
    }

    public Date getDate()
    {
        return time;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }
}
