package com.lyl.learn.model;

import java.util.List;

public class LearnItemModel {
    String cn = "";
    String en = "";
    int type = 0;
    List<String> enData;
    List<String> cnData;

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getEnData() {
        return enData;
    }

    public void setEnData(List<String> enData) {
        this.enData = enData;
    }

    public List<String> getCnData() {
        return cnData;
    }

    public void setCnData(List<String> cnData) {
        this.cnData = cnData;
    }
}
