package com.example.admin.entity.User;

public class scheduled_invitation {
    private String currentDate;//日期
    private String currentTime;//时间
    private int occupied_seats;//这个时段还剩余多少座位

    public String getCurrentDate(){
        return currentDate;
    }

    public String getCurrentTime(){
        return currentTime;
    }

    public int getOccupied_seats() { return occupied_seats; }

    public void setCurrentDate(String cur_date){
        this.currentDate=cur_date;
    }

    public void setCurrentTime(String cur_time){
        this.currentTime=cur_time;
    }

    public void setOccupied_seats(int occupied_seats) {
        this.occupied_seats = occupied_seats;
    }

}
