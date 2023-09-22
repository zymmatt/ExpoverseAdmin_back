package com.example.admin.entity.Survey;
// 在问卷中对某一道问题给出的填空回答
public class QuesFill {
    private int ques_id;
    private int loginid;
    private String filltext;
    private Long trigger_timestamp; // 这个问题的回答时间戳,在部分统计中会用到

    public QuesFill(int ques_id, int loginid, String filltext){
        this.ques_id = ques_id;
        this.loginid = loginid;
        this.filltext = filltext;
    }

    public QuesFill(int ques_id, int loginid, String filltext, Long trigger_timestamp){
        this.ques_id = ques_id;
        this.loginid = loginid;
        this.filltext = filltext;
        this.trigger_timestamp = trigger_timestamp;
    }


    public int getQues_id() {
        return ques_id;
    }

    public void setQues_id(int ques_id) {
        this.ques_id = ques_id;
    }

    public int getLoginid() {
        return loginid;
    }

    public void setLoginid(int loginid) {
        this.loginid = loginid;
    }

    public Long getTrigger_timestamp() {
        return trigger_timestamp;
    }

    public void setTrigger_timestamp(Long trigger_timestamp) {
        this.trigger_timestamp = trigger_timestamp;
    }

    public String getFilltext() {
        return filltext;
    }

    public void setFilltext(String filltext) {
        this.filltext = filltext;
    }

}
