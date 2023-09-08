package com.example.admin.entity.Survey;

// 某一个选项是否有被选择
public class SingleOptionSelect {
    private int option_id;
    private boolean selected;

    public boolean isSelected() { return selected; }

    public int getOption_id() { return option_id; }
}
