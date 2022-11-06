package view;
import action.Conn;

class Main {
    public static void main(String[] agrs) {
        // 初始化表，测试用
        new Conn().autoUpdateDatabase();
        // 创建选票页面
        PickFilmView.frame2PickFilm(PickFilmView.FILM_LIST_DEFAULT_ORDER);
//        AdminMain.frame2Admin();        // test
    }
}