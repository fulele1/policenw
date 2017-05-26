package com.xaqb.policenw.entity;

/**
 * Created by lenovo on 2017/3/15.
 */

public class Company {
    private String com;//企业名称
    private String coms;//品牌名称
    private String comCode;//企业编码
    private String policeCount;//场所数量
    private String perCount;//从业人员数量
    private String getCount;//收件数量
    private String per;//企业负责人
    private String num;//企业负责人电话

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPer() {
        return per;
    }

    public void setPer(String per) {
        this.per = per;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    private String address;//企业地址
    private String postCount;//投件数量



    public String getComs() {
        return coms;
    }

    public void setComs(String coms) {
        this.coms = coms;
    }


    public String getComCode() {
        return comCode;
    }

    public void setComCode(String comCode) {
        this.comCode = comCode;
    }



    public String getPoliceCount() {
        return policeCount;
    }

    public void setPoliceCount(String policeCount) {
        this.policeCount = policeCount;
    }

    public String getPerCount() {
        return perCount;
    }

    public void setPerCount(String perCount) {
        this.perCount = perCount;
    }

    public String getGetCount() {
        return getCount;
    }

    public void setGetCount(String getCount) {
        this.getCount = getCount;
    }

    public String getPostCount() {
        return postCount;
    }

    public void setPostCount(String postCount) {
        this.postCount = postCount;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }
}
