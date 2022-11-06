package entity;

/**
 * @author toulzx
 */
public class BookInfo {
    private Integer id;
    private Integer screenId;
    private Integer seatX;
    private Integer seatY;
    private String tel;
    private String validCode;
    private Boolean status;  //0表示未取票，1表示已取票

    public BookInfo(Integer id, Integer screenId, Integer seatX, Integer seatY, String tel, String validCode, Boolean status) {
        this.id = id;
        this.screenId = screenId;
        this.seatX = seatX;
        this.seatY = seatY;
        this.tel = tel;
        this.validCode = validCode;
        this.status = status;
    }

    public BookInfo() {
    }

    // getter and setter below

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getScreenId() {
        return screenId;
    }
    public void setScreenId(Integer screenId) {
        this.screenId = screenId;
    }
    public Integer getSeatX() {
        return seatX;
    }
    public void setSeatX(Integer seatX) {
        this.seatX = seatX;
    }
    public Integer getSeatY() {
        return seatY;
    }
    public void setSeatY(Integer seatY) {
        this.seatY = seatY;
    }
    public String getTel() {
        return tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
    public String getValidCode() {
        return validCode;
    }
    public void setValidCode(String validCode) {
        this.validCode = validCode;
    }
    public Boolean getStatus() {
        return status;
    }
    public void setStatus(Boolean status) {
        this.status = status;
    }

}
