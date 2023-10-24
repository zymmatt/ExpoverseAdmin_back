package com.example.admin.entity.User;
// 每一天的参观人数(不是参观人次)
public class DayUserVisit {
    private String day; // "2023-10-24" 哪一天
    private int UserNum; // 有多少人来参观

    public int getUserNum() {
        return UserNum;
    }

    public void setUserNum(int userNum) {
        UserNum = userNum;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public DayUserVisit(String day, int UserNum){
        this.day=day;
        this.UserNum=UserNum;
    }

    @Override
    public String toString() {
        return "DayUserVisit{" +
                "day='" + day + '\'' +
                ", UserNum=" + UserNum +
                '}';
    }
}
