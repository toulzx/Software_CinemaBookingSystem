package action;

import entity.BookInfo;
import entity.Film;
import entity.FilmScreen;
import entity.Theater;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Conn {

    Connection conn;
    Statement st;
    PreparedStatement pst;
    ResultSet rs;

    public void getConn() {
        //1.配置、加载驱动
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        //2.配置、建立与数据库的连接
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sky-cloud", "root", "888888");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void closeConn(){
        try {
            if(rs!=null){
                rs.close();
                rs = null;
            }
            if(pst!=null){
                pst.close();
                pst = null;
            }
            if(st!=null){
                st.close();
                st = null;
            }
            conn.close();
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*-------------------------------------------code refined begin------------------------------------------------------*/

    /**
     * 测试项目用，自动根据当前日期更新表数据，以保证近 7 日始终有放映日期可选
     */
    public void autoUpdateDatabase()
    {
        getConn();
        try {
            st = conn.createStatement();
            String sql = "select startDay from filmscreen where id=14";
            rs = st.executeQuery(sql);
            if (rs.next()) {
                java.sql.Date temp_date = rs.getDate("startDay");
                String sql2 = "update filmscreen set startDay = date_add(startDay, interval datediff(now(),?) day)";
                pst = conn.prepareStatement(sql2);
                pst.setDate(1, temp_date);
                pst.executeUpdate();
                closeConn();
                System.out.println("test: screen 表中放映日期自增成功。");
            } else {
                System.out.println("test: 放映日期自增失败！锚点数据(id=14)被删除了！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("test: 放映日期自增失败！");
        }
    }

    /**
     * @return null if no data or SQLException
     */
    public Film getFilmById(int id)     //查找某个电影
    {
        getConn();
        try{
            st = conn.createStatement();
            String sql = "select * from film where uid="+id;
            rs = st.executeQuery(sql);
            if(rs.next()) {
                return new Film(
                        rs.getInt("uid"),
                        rs.getString("uname"),
                        rs.getString("info"),
                        rs.getString("localAddress"),
                        rs.getDate("time"),
                        rs.getFloat("price"),
                        rs.getFloat("discount")
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param mode
     * 1: ordered by id;
     * 2: ordered by sales(, release-time, id);
     * 3: ordered by release-time(, sales, id);
     */
    public ArrayList<Film> getFilmList(int mode)
    {
        getConn();
        ArrayList<Film> filmList = new ArrayList<>();
        try {
            st = conn.createStatement();
            String sql;
            switch (mode) {
                case 3:
                    sql = "select * from film left outer join ( " +
                            "select filmscreen.filmId as fId, count(filmscreen.filmId) as sales from filmscreen, bookinfo " +
                            "where filmscreen.id=bookinfo.screenId group by fId " +
                            ")As sales on sales.fId=film.uid order by `time` desc, sales desc, uid";
                    break;
                case 2:
                    sql = "select * from film left outer join ( " +
                            "select filmscreen.filmId as fId, count(filmscreen.filmId) as sales from filmscreen, bookinfo " +
                            "where filmscreen.id=bookinfo.screenId group by fId " +
                            ")As sales on sales.fId=film.uid order by sales desc,`time` desc, uid";
                    break;
                case 1:
                default:
                    sql = "select * from film";
            }
            rs = st.executeQuery(sql);
            while(rs.next()){
                filmList.add( new Film(
                        rs.getInt("uid"),
                        rs.getString("uname"),
                        rs.getString("info"),
                        rs.getString("localAddress"),
                        rs.getDate("time"),
                        rs.getFloat("price"),
                        rs.getFloat("discount")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filmList;
    }

    /**
     * @return -1 if SQLException.
     */
    public int getFilmCount()      //查找电影数量
    {
        getConn();
        try{
            st = conn.createStatement();
            String sql = "select count(*) from film";
            rs = st.executeQuery(sql);
            rs.next();  // Initially cursor 'rs' is positioned before first row.
            return rs.getInt(1);
        }catch(Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * @return false if SQLException
     */
    public boolean updateFilm(Film film)    // 更新 film
    {
        getConn();
        try {
            String sql = "update film set uname=?, info=?, localAddress=?, `time`=?, price=?, discount=? where uid=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1,film.getName());
            pst.setString(2,film.getInfo());
            pst.setString(3,film.getUrl());
            pst.setString(4,film.getTime().toString());
            pst.setString(5,String.valueOf(film.getPrice()));
            pst.setString(6,String.valueOf(film.getDiscount()));
            pst.setString(7,film.getId().toString());
            pst.executeUpdate();
            closeConn();
            System.out.println("数据更新成功！\n");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("数据更新失败！\n");
            return false;
        }
    }

    /**
     * @return false if SQLException
     */
    public boolean deleteFilmById(int id)       // 电影删除
    {
        getConn();
        try{
            st = conn.createStatement();
            String sql = "delete from film where uid="+id;
            st.executeUpdate(sql);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<FilmScreen> getScreenListById()      // 获取场次信息列表
    {
        getConn();
        ArrayList<FilmScreen> screenList = new ArrayList<>();
        try {
            st = conn.createStatement();
            String sql = "select * from filmScreen";
            rs =st.executeQuery(sql);
            while (rs.next()) {
                screenList.add(
                        new FilmScreen(
                                rs.getInt("id"),
                                rs.getInt("filmId"),
                                rs.getDate("startDay"),
                                rs.getString("time"),
                                rs.getInt("theaterId")
                        )
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenList;
    }

    public ArrayList<FilmScreen> getScreenListByFilmId(int FilmId)    //按电影id查找电影
    {
        getConn();
        ArrayList<FilmScreen> screenList = new ArrayList<>();
        try{
            st = conn.createStatement();
            String sql = "select * from filmscreen where filmId ="+FilmId;
            rs = st.executeQuery(sql);
            while (rs.next()) {
                FilmScreen screen = new FilmScreen();
                screen.setId(rs.getInt(1));
                screen.setFilmId(rs.getInt(2));
                screen.setStartDay(rs.getDate(3));
                screen.setTime(rs.getString(4));
                screen.setTheaterId(rs.getInt(5));
                screenList.add(screen);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return screenList;
    }

    /**
     * @return null if not found or SQLException
     */
    public FilmScreen getScreenById(int screenId)      //按场次id获取场次信息
    {
        getConn();
        try {
            st = conn.createStatement();
            String sql = "select * from filmScreen where id = "+screenId;
            rs =st.executeQuery(sql);
            while (rs.next()) {
                return new FilmScreen(
                        rs.getInt("id"),
                        rs.getInt("filmId"),
                        rs.getDate("startDay"),
                        rs.getString("time"),
                        rs.getInt("theaterId")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取某影片近 7 日的放映日期列表
     * Attention: `java.util.Date` and `java.sql.Date` is different, use full-name to indicate
     * though `import java.util.Date` and use `Date` is available
     */
    public ArrayList<java.util.Date> getScreenDayList(int filmId)
    {
        getConn();
        ArrayList<java.util.Date> list = new ArrayList<>();
        try {
            String sql = "select startDay from filmScreen where filmId=? " +
                    "and datediff(startDay,now())>=0 and datediff(startDay,now())<=7 group by startDay";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, filmId);
            rs = pst.executeQuery();
            while (rs.next()) {
                list.add(rs.getDate(1));
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return list;
    }

    public ArrayList<String> getScreenTimeList(int filmId, java.util.Date date)     // 获取某影片某日的放映时间列表
    {
        getConn();
        ArrayList<String> list = new ArrayList<>();
        try {
            st = conn.createStatement();
            String sql;
            if (date.toString().equals(java.time.LocalDate.now().toString())) {
                sql = "select time from filmScreen where filmId=" + filmId + " and Date(startDay)='" + date.toString() +
                        "' and now() < str_to_date(time,'%H:%i')";
            } else {
                sql = "select time from filmScreen where filmId=" + filmId + " and Date(startDay)='" + date.toString() + "'";
            }
            rs = st.executeQuery(sql);
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return list;
    }

    /**
     * @return -1 if SQLException
     */
    public int addScreen(FilmScreen screen)  //场次添加
    {
        getConn();
        try{
            String sql = "insert into filmscreen (filmId,startDay,`time`,theaterId)values(?,?,?,?)  ";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, screen.getFilmId());
            pst.setDate(2, (java.sql.Date) screen.getStartDay());
            pst.setString(3,screen.getTime());
            pst.setInt(4,screen.getTheaterId());
            pst.executeUpdate();
            System.out.println("数据插入成功！\n");
            getConn();
            st = conn.createStatement();
            String sql2 = "select id from filmscreen where filmId="+screen.getFilmId()+
                    " and startDay='" + (java.sql.Date) screen.getStartDay() +
            "' and `time`='" + screen.getTime() + "' and theaterId=" + screen.getTheaterId();
            rs = st.executeQuery(sql2);
            if (rs.next()) {
                return rs.getInt(1);
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("数据插入失败！\n");
        }
        return -1;
    }

    /**
     * @return false if SQLException
     */
    public boolean updateScreen(FilmScreen screen)    // 更新 screen
    {
        getConn();
        try {
            String sql = "update filmscreen set filmId=?, startDay=?, `time`=?, theaterId=? where id=?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1,screen.getFilmId());
            pst.setDate(2,(java.sql.Date)screen.getStartDay());
            pst.setString(3,screen.getTime());
            pst.setInt(4,screen.getTheaterId());
            pst.setInt(5,screen.getId());
            pst.executeUpdate();
            closeConn();
            System.out.println("数据更新成功！\n");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("数据更新失败！\n");
            return false;
        }
    }


    /**
     * @return false if SQLException
     */
    public boolean deleteScreenById(int id)       //场次删除
    {
        getConn();
        try{
            st = conn.createStatement();
            String sql = "delete from filmscreen where id="+id;
            st.executeUpdate(sql);
            return true;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * The attribute combination {`filmId`, `date`, `time`} is a candidate code that uniquely identifies a tuple
     * @param isScreenId if true, return screenId, else return theaterId
     * @return if 0, wrong.
     */
    public int getIdByScreenInfo(boolean isScreenId, int filmId, Date date, String time) // 通过放映信息确定 id
    {
        getConn();
        try {
            st = conn.createStatement();
            String sql = "select id, theaterId from filmscreen where filmId=" + filmId +
                    " and Date(startDay)='" + date + "' and time='" + time + "'";
            rs = st.executeQuery(sql);
            rs.next();
            if (isScreenId) {
                return rs.getInt(1);
            } else {
                return rs.getInt(2);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return 0;
    }

    public ArrayList<Theater> getTheaterList()      // 获得放映厅信息列表
    {
        getConn();
        ArrayList<Theater> theaterList = new ArrayList<>();
        try {
            st = conn.createStatement();
            String sql = "select * from theater";
            rs = st.executeQuery(sql);
            while(rs.next()){
                theaterList.add( new Theater(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("rows"),
                        rs.getInt("cols"),
                        rs.getFloat("premiumRadio")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return theaterList;
    }

    /**
     * @return -1 if SQLException
     */
    public int getTheaterCount()     //放映厅总记录数
    {
        getConn();
        try{
            st = conn.createStatement();
            String sql = "select count(*) from theater";
            rs = st.executeQuery(sql);
            rs.next();
            return rs.getInt(1);
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public Theater getTheaterById(int theaterId)     //获取放映厅信息
    {
        getConn();
        Theater theater = new Theater();
        try {
            st = conn.createStatement();
            String sql = "select * from theater where id = "+theaterId;
            rs = st.executeQuery(sql);
            if (rs.next()) {
                theater.setId(rs.getInt(1));
                theater.setName(rs.getString(2));
                theater.setRows(rs.getInt(3));
                theater.setColumns(rs.getInt(4));
                theater.setPremiumRadio(rs.getFloat(5));
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return theater;
    }

    public boolean isTheaterNameUsed(String name)  // 查询是否存在
    {
        getConn();
        try {
            st = conn.createStatement();
            String sql = "select * from theater where name='"+name+"'";
            rs = st.executeQuery(sql);
            return !rs.next();
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @return false if SQLException
     */
    public boolean updateTheater(Theater theater)    // 更新 theater
    {
        getConn();
        try {
            String sql = "update theater set `name`=?, `rows`=?, cols=?, premiumRadio=? where uid=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1,theater.getName());
            pst.setString(2,theater.getRows().toString());
            pst.setString(3,theater.getColumns().toString());
            pst.setString(4,String.valueOf(theater.getPremiumRadio()));
            pst.setString(5,theater.getId().toString());
            pst.executeUpdate();
            closeConn();
            System.out.println("数据更新成功！\n");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("数据更新失败！\n");
            return false;
        }
    }

    /**
     * @return -1 if SQLException
     */
    public int addTheater(Theater theater){        //添加放映厅信息
        getConn();
        try{
            // `rows` seems to be a keyword in MySQL, so wrap it with back-quotes would be better
            String sql = "insert into theater(`name`,`rows`,`cols`,`premiumRadio`) VALUES (?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1,theater.getName());
            pst.setInt(2,theater.getRows()) ;
            pst.setInt(3,theater.getColumns());
            pst.setFloat(4,theater.getPremiumRadio());
            pst.executeUpdate();
            System.out.println("数据插入成功！\n");
            getConn();
            st = conn.createStatement();
            String sql2 = "select id from theater where `name`='"+theater.getName()+"'";
            rs = st.executeQuery(sql2);
            if (rs.next()) {
                return rs.getInt(1);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("数据插入失败！\n");
        }
        return -1;
    }

    /**
     * @return false if SQLException
     */
    public boolean deleteTheaterById(int id)  //删除放映室信息
    {
        getConn();
        try{
            String sql = "delete from theater where id = ?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1,id);
            pst.executeUpdate();
            closeConn();
            System.out.println("数据删除成功！\n");
            return true;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("数据删除失败！\n");
            return false;
        }
    }

    public ArrayList<BookInfo> getBookList()      // 获得订票信息列表
    {
        getConn();
        ArrayList<BookInfo> bookList = new ArrayList<>();
        try {
            st = conn.createStatement();
            String sql = "select * from bookinfo";
            rs = st.executeQuery(sql);
            while(rs.next()){
                bookList.add( new BookInfo(
                        rs.getInt("id"),
                        rs.getInt("screenId"),
                        rs.getInt("seatX"),
                        rs.getInt("seatY"),
                        rs.getString("tel"),
                        rs.getString("validCode"),
                        rs.getBoolean("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookList;
    }

    public int getMaxBookId()     //查找预定的最大id
    {
        getConn();
        int i = 0;
        try {
            st = conn.createStatement();
            String sql = "select max(id) from bookinfo";
            rs = st.executeQuery(sql);
            rs.next();
            i = rs.getInt(1);
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * @return null if SQLException
     */
    public BookInfo getBookById(int id) {
        getConn();
        try {
            st = conn.createStatement();
            String sql = "select * from bookinfo where id=" + id;
            rs = st.executeQuery(sql);
            if (rs.next()) {
                return new BookInfo(
                        id,
                        rs.getInt("screenId"),
                        rs.getInt("seatX"),
                        rs.getInt("seatY"),
                        rs.getString("tel"),
                        rs.getString("validCode"),
                        rs.getBoolean("status")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return null if SQLException or empty
     */
    public BookInfo getBookByTelAndCode(String tel, String code)       // 查询取票信息
    {
        getConn();
        try{
            st=conn.createStatement();
            String sql="select * from bookinfo where tel='"+tel+"' and validCode='"+code+"'";
            rs=st.executeQuery(sql);
            if (rs.next()) {
                return new BookInfo(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getBoolean(7)
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<BookInfo> getBookListByScreenId(int screenId)    //按照场次id来查找预定信息
    {
        getConn();
        ArrayList<BookInfo> fs = new ArrayList<BookInfo>();
        try{
            st = conn.createStatement();
            String sql = "select * from bookinfo where screenId = "+screenId;
            rs = st.executeQuery(sql);
            while (rs.next()) {
                BookInfo bk = new BookInfo();
                bk.setId(rs.getInt(1));
                bk.setScreenId(rs.getInt(2));
                bk.setSeatX(rs.getInt(3));
                bk.setSeatY(rs.getInt(4));
                bk.setTel(rs.getString(5));
                bk.setValidCode(rs.getString(6));
                bk.setStatus(rs.getBoolean(7));
                fs.add(bk);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return fs;
    }

    /**
     * @return -1 if SQLException
     */
    public int getSales(int filmId)     // 通过电影 id 查询某电影的销量
    {
        getConn();
        try{
            st = conn.createStatement();
            String sql = "select count(filmscreen.filmId) as sales from filmscreen, bookinfo " +
                    "where filmscreen.id=bookinfo.screenId and filmscreen.filmId=" + filmId;
            rs = st.executeQuery(sql);
            rs.next();
            return rs.getInt(1);
        }catch(Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * @return -1 if SQLException
     */
    public int getMemberBookCount (String tel) // 获取某人的订单数
    {
        getConn();
        try{
            st = conn.createStatement();
            String sql = "select count(*) from bookinfo where tel='" + tel + "'";
            rs = st.executeQuery(sql);
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * @return false if status=1 or SQLException
     */
    public boolean getStatusById(int id)
    {
        getConn();
        try {
            st = conn.createStatement();
            String sql = "select status from bookinfo where id=" + id;
            rs = st.executeQuery(sql);
            rs.next();
            return rs.getInt("status") == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @return
     * true: if success;
     * false: if SQLException, terminate.
     */
    public boolean insertBook(ArrayList<BookInfo> list)     // 插入若干条订票数据
    {
        getConn();
        int id,screenId,seatX,seatY;
        String tel,validCode;
        boolean status;
        for (BookInfo bookInfo : list) {
            id = bookInfo.getId();
            screenId = bookInfo.getScreenId();
            seatX = bookInfo.getSeatX();
            seatY = bookInfo.getSeatY();
            tel = bookInfo.getTel();
            validCode = bookInfo.getValidCode();
            status = bookInfo.getStatus();
            try{
                String sql2 = "insert into bookinfo (id,screenId,seatX,seatY,tel,validCode,status) values (?,?,?,?,?,?,?)";
                pst = conn.prepareStatement(sql2);
                // 事实上 id 值的设置并不会生效，作为自增主键会以数据库实际情况为主
                pst.setInt(1,id);
                pst.setInt(2,screenId);
                pst.setInt(3,seatX);
                pst.setInt(4,seatY);
                pst.setString(5,tel);
                pst.setString(6,validCode);
                pst.setBoolean(7,status);
                pst.executeUpdate();
            }catch(SQLException e){
                e.printStackTrace();
                System.out.println("第" + list.indexOf(bookInfo) + "条数据插入失败，插入终止");
                return false;
            }
        }
        return true;
    }

    public boolean deleteBookById(int id)
    {
        getConn();
        try {
            st = conn.createStatement();
            String sql = "delete from bookinfo where id=" + id;
            st.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



    public String IdtTheaterName(int screenid){     //通过在 book 中的 screenid 查询到 theater 中的 name
        getConn();
        try {
            String sql = "select name from theater where id=(select theaterId from filmscreen where id=?)";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, screenid);
            rs=pst.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }


    public String getFilmNameByScreenId(int screenid){       //通过在 book 中的 screenId 查询到 film 中的 name
        getConn();
        try {
            String sql = "select uname from film where uid=(select filmId from filmscreen where id = ? )";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, screenid);
            rs=pst.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }


    public String IdtFilmTime(int screenId){    // 通过在 book 中的 screenId 查询到 screen 中的 startDay 和 time
        getConn();
        try {
            String sql = "select startDay,time from filmscreen where id=?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, screenId);
            rs=pst.executeQuery();
            if (rs.next()) {
                return rs.getDate(1) + " " + rs.getString(2);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @return false if SQLException
     */
    public boolean updateStatus(String tel,String validcode){  // 取票后更新status=1
        getConn();
        try{
            String sql = "update bookinfo set status=1 where tel =? and validCode=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1,tel);
            pst.setString(2,validcode);
            pst.executeUpdate();
            System.out.println("数据更新成功！\n");
            closeConn();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("数据修改失败！\n");
            return false;
        }
    }

    /**
     * 根据看过相同影片的人们的观影情况，推荐本人还没看过的电影
     * @return 适应 label 内容与格式的一串字符串
     */
    public String Recommend(String tel)
    {
        getConn();
        String temp = "<br>";
        try {
            String sql = new String(Files.readAllBytes(Paths.get("src/res/sql_recommend.txt")));
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, tel);
            preparedStatement.setString(2, tel);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // 要注意这里二次调用了类内函数，意味着 rs pst 会被所调用的函数使用，导致查询出错！
                // 报错内容："Cannot determine value type from string ’xxx‘"
                String str = getFilmById(resultSet.getInt(1)).getName();
                int per = resultSet.getInt(2);
                temp += "->" + str + "(" + per + "人次)<br>";
            }
            return temp;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return "-数据库出错-";
        }
    }

    /**
     * 查询某人看过的电影及次数
     * @return 适应 label 内容与格式的一串字符串
     */
    public String Stats(String tel)
    {
        getConn();
        String temp ="<br>";
        try {
            String sql = new String(Files.readAllBytes(Paths.get("src/res/sql_stats.txt")));
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, tel);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String str = getFilmById(resultSet.getInt(1)).getName();
                int amount = resultSet.getInt(2);
                temp += "=>" + str + "(" + amount + "次)<br>";
            }
            return temp;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return "-数据库出错-";
        }
    }


    /*-------------------------------------------code refined end------------------------------------------------------*/


    //电影增加数据
    public boolean addFilm(List list){
        getConn();
        try{
            for (int i = 0; i < list.size(); i++) {
                List temp = (List) list.get(i);
                for (int j = 0; j < temp.size(); j++) {
                    String sql = "insert into film (uname,info,photo,time) values (?,?,?,?) ";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, (String) temp.get(0));
                    pst.setString(2, (String) temp.get(1));
                    pst.setString(3, (String) temp.get(2));
                    pst.setString(4, (String) temp.get(3));
                    pst.executeUpdate();
                }
            }
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }


    //查询要删除的放映厅的id是否和book中有没有相应的
    public ResultSet selectTheaterId(int id){//----------------查询需要删除的信息从而选择
        getConn();
        try {
            String sql = "select * from bookinfo where id=?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            rs=pst.executeQuery();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }


}
