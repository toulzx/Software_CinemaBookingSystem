# 剧场订票管理系统

题目来源：NJUPT_2022-2023-1_数据库课程设计  

## 项目结构、功能与介绍

见 `readme` 文件夹中的文件。

## 项目结构

``` text
CinemaBookingSystem
├─ database
│  ├─ sky-cloud_bookinfo.sql
│  ├─ sky-cloud_film.sql
│  ├─ sky-cloud_filmscreen.sql
│  └─ sky-cloud_theater.sql
├─ readme
│  ├─ .PNGs
│  ├─ .PDFs
├─ README.md
└─ src
   ├─ action
   │  ├─ Conn.java
   │  └─ Utils.java
   ├─ entity
   │  ├─ BookInfo.java
   │  ├─ Film.java
   │  ├─ FilmScreen.java
   │  └─ Theater.java
   ├─ lib
   │  ├─ jxl.jar
   │  └─ mysql-connector-java-8.0.28.jar
   ├─ res
   │  ├─ icons
   │  ├─ images
   │  ├─ sql_recommend.txt
   │  └─ sql_stats.txt
   └─ view
      ├─ AddScreenView.java
      ├─ AddTheaterView.java
      ├─ AdminBookView.java
      ├─ AdminFilmView.java
      ├─ AdminMain.java
      ├─ AdminScreenView.java
      ├─ AdminTheaterView.java
      ├─ BookTicketView.java
      ├─ GetTicketView.java
      ├─ Main.java
      ├─ MemberView.java
      └─ PickFilmView.java
```

## 参考项目

> [🔗Theater-booking-system-requirements](https://github.com/johyles/Theater-booking-system-requirements)

除了 AddViews 之外的代码全部完成重构。改进内容概括如下：

1. 合并前后台项目，优化代码逻辑，减小耦合。
2. 添加适合 MySQL8.0 的 connector，改进数据库表结构，对数据的查找筛选全部改由数据库操作（Conn类）完成。
3. 增加适合实验需要的新功能，修复原项目 bugs 若干。

