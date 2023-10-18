package com.example.admin.entity.User;

public class LanguageWord {
    private String ID;
    private int TYPE;
    private String CNS;
    private String CNT;
    private String EN;
    private String ES;

    public LanguageWord(String ID,int TYPE,String CNS,String CNT,String EN,String ES){
        this.ID=ID;
        this.TYPE=TYPE;
        this.CNS=CNS;
        this.CNT=CNT;
        this.EN=EN;
        this.ES=ES;
    }

    public int getTYPE() {
        return TYPE;
    }

    public String getCNS() {
        return CNS;
    }

    public String getCNT() {
        return CNT;
    }

    public String getEN() {
        return EN;
    }

    public String getES() {
        return ES;
    }

    public String getID() {
        return ID;
    }

    public void setCNS(String CNS) {
        this.CNS = CNS;
    }

    public void setCNT(String CNT) {
        this.CNT = CNT;
    }

    public void setEN(String EN) {
        this.EN = EN;
    }

    public void setES(String ES) {
        this.ES = ES;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }

    @Override
    public String toString() {
        return "LanguageWord{" +
                "ID='" + ID + '\'' +
                ", TYPE=" + TYPE +
                ", CNS='" + CNS + '\'' +
                ", CNT='" + CNT + '\'' +
                ", EN='" + EN + '\'' +
                ", ES='" + ES + '\'' +
                '}';
    }
}
