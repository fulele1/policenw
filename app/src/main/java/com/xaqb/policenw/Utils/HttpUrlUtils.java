package com.xaqb.policenw.Utils;

/**
 * Created by lenovo on 2017/3/3.
 */

public class HttpUrlUtils {
    private static HttpUrlUtils httpUrl = new HttpUrlUtils();

    public static HttpUrlUtils getHttpUrl() {
        return httpUrl;
    }

    private String getBaseUrl() {
        return "http://xawz.xaqianbai.net:8090";
    }

    //查询订单
    //http://xawz.xaqianbai.net:8090/open.ashx?action=policeexpressinfo&code=719677781148
    public String quer_yCode(){
        return "/open.ashx?action=policeexpressinfo";
    }

    // 登录
    //http://xawz.xaqianbai.net:8090/open.ashx?action=policelogin&user=xaqianbai&pwd=D41D8CD98F00B204E9800998ECF8427E
    public String user_login() {
        return "/open.ashx?action=policelogin";
    }

    //找回密码
    //http://xawz.xaqianbai.net:8090/open.ashx?action=policemodipwd&old=D41D8CD98F00B204E9800998ECF8427E&new=D41D8CD98F00B204E9800998ECF8427E

    public String back_password() {
        return "/open.ashx?action=policemodipwd";
    }

    //获取验证码
    public String get_v_code() {
        return "/open.ashxsmscode.json?";
    }

    //快递员查询
    // http://xawz.xaqianbai.net:8090/open.ashx?action=policeemployees
    public String get_query_per() {
        return "/open.ashx?action=policeemployees";
    }

    //快递员详情查询
    // http://xawz.xaqianbai.net:8090/open.ashx?action=policeemployeeinfo&empcode=A0161011400011201611250004
    public String get_query_per_detail() {
        return "/open.ashx?action=policeemployeeinfo";
    }
    //企业查询
    // http://xawz.xaqianbai.net:8090/open.ashx?action=policecompanies
    public String get_query_com() {
        return "/open.ashx?action=policecompanies";
    }

    //企业查询详情
    // http://xawz.xaqianbai.net:8090/open.ashx?action=policecompanyinfo&comcode=A0161011300078
    public String get_query_com_detail() {
        return "/open.ashx?action=policecompanyinfo";
    }

    public String get_updata() {
        return "http://update.xaqianbai.com";
    }

    //辖区查询详情
    //"http://192.168.0.137/open.ashx?action=policesevenInfos
    public String get_query_org_detail() {
        return "/open.ashx?action=policesevenInfos";
    }

}
