package com.xaqb.policenw.Other;

import java.util.ArrayList;

public class ServerSet {
    protected ArrayList<ServerItem> FaItem = new ArrayList<ServerItem>();

    public void load(String sServer) {
        String[] aServer = sServer.split(";");
        FaItem.clear();
        int i;
        for (i = 0; i < aServer.length; i++)
            FaItem.add(new ServerItem(aServer[i]));
    }

    protected void reset() {
        int i, iLen = FaItem.size();
        for (i = 0; i < iLen; i++) FaItem.get(i).reset();
    }

    public ServerItem get() {
        int i, iLen = FaItem.size(), iSel = 0;
        double fMin = 0;
        if (iLen > 0) {
            fMin = FaItem.get(0).getTime();
            for (i = 1; i < iLen; i++) {
                if (FaItem.get(i).getTime() < fMin) {
                    iSel = i;
                    fMin = FaItem.get(i).getTime();
                }
            }
            if (fMin >= 10000) reset();
            return FaItem.get(iSel);
        }
        return null;

    }

    public void setTime(String sTime) {
        String[] aTime = sTime.split(",");
        if (aTime.length == FaItem.size() * 2) {
            int i, iLen = FaItem.size();
            for (i = 0; i < iLen; i++)
                FaItem.get(i).setTime(aTime[i * 2], aTime[i * 2 + 1]);

        }
    }

    public String getTime() {
        int i, iLen = FaItem.size();
        StringBuilder oStr = new StringBuilder();
        if (iLen > 0) oStr.append(FaItem.get(0).getStr());
        for (i = 1; i < iLen; i++) oStr.append("," + FaItem.get(0).getStr());
        return oStr.toString();
    }

    public int size() {
        return FaItem.size();
    }
}
