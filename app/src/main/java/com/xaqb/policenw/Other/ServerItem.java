package com.xaqb.policenw.Other;

public class ServerItem {
    protected String FsServer = "";
    protected int FiNum = 0;
    protected double FfTime = 0;

    public ServerItem(String sServer) {
        FsServer = sServer;
    }

    public void setTime(String sNum, String sTime) {
        try {
            int iNum = Integer.parseInt(sNum);
            double fTime = Double.parseDouble(sTime);
            if (iNum >= 0 && fTime >= 0) {
                FiNum = iNum;
                FfTime = fTime;
            }
        } catch (Exception E) {
            return;
        }

    }

    public String getStr() {
        return Integer.toString(FiNum) + "," + Double.toString(FfTime);
    }

    public void reset() {
        FiNum = 0;
        FfTime = 0;
    }

    public void addTime(int iTime) {
        FfTime = (FiNum * FfTime + iTime) / (FiNum + 1);
        FiNum++;
    }

    public double getTime() {
        return FfTime;
    }

    public String get() {
        return FsServer;
    }
}
