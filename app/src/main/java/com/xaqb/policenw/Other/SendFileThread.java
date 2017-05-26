package com.xaqb.policenw.Other;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;
import com.xaqb.policenw.Utils.ProcUnit;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class SendFileThread extends Thread {
    public String FsUrl = "";
    public String FsRight = "";
    public String FsUser = "";
    public String FsPath = "";
    private int FiCounter = 600;
    private boolean FbRun = false;
    public Handler FoHandler = null;
    public ServerSet FoServer = null;

    @Override
    public void run() {

        String sRet = "", sUrl = "";
        ServerItem oItem = null;
        while (FbRun) {
//            Log.i("threadtest","zhixing ---while");
            try {
                Thread.sleep(100);
                if (FiCounter > 600) {
                    FiCounter = 0;
                    if (FsUrl.length() > 6) {
                        File oFile = new File(FsPath);
                        File[] oFiles = null;
                        if (oFile != null) oFiles = oFile.listFiles();
                        int i;
                        long iTime = 0;
                        if (oFiles != null)
                            for (i = 0; i < oFiles.length; i++) {

                                if (oFiles[i].getName().length() >= 35) {
                                    Log.i("start", "upload" + i);
                                    Log.i("file===", oFiles[i].toString());
                                    if (FoServer != null) oItem = FoServer.get();
                                    sUrl = oItem == null ? FsUrl : oItem.get();
                                    iTime = SystemClock.uptimeMillis();
                                    Log.w("error", sUrl);
                                    sRet = ProcUnit.httpUploadFile(sUrl, FsUser, FsPath, oFiles[i].getName(), FsRight);
                                    Log.i("filename", oFiles[i].getName());
                                    iTime = SystemClock.uptimeMillis() - iTime;
                                    if (oItem != null) oItem.addTime((int) iTime);
                                    if (sRet.length() > 0 && sRet.substring(0, 1).equals("0")) {
                                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                                        DocumentBuilder builder = factory.newDocumentBuilder();
                                        Document doc = builder.parse(oFiles[i]);
                                        Element element = doc.getDocumentElement();
                                        NodeList bookNodes;
                                        if (oFiles[i].getName().contains("jdwlj")) {
                                            bookNodes = element.getElementsByTagName("ylcs_jdwlj");
                                        } else {
                                            bookNodes = element.getElementsByTagName("ylcs_jdwld");
                                        }
                                        String orderid = "";
                                        for (int j = 0; j < bookNodes.getLength(); j++) {
                                            Element bookElement = (Element) bookNodes.item(j);
                                            NodeList childNodes = bookElement.getChildNodes();  //获取根节点下的子节点
                                            for (int k = 0; k < childNodes.getLength(); k++) {
                                                if (childNodes.item(k).getNodeType() == Node.ELEMENT_NODE) {
                                                    if ("ydh".equals(childNodes.item(k).getNodeName())) {
                                                        orderid = childNodes.item(k).getFirstChild().getNodeValue();
                                                    }
                                                }
                                            }
                                        }
                                        Log.i("orderid=====", orderid);
                                        oFiles[i].delete();
                                        writeLog(oFiles[i].getName(), orderid);
                                    } else {
                                        CrashReport.postCatchedException(new Throwable(sRet));  // bugly会将这个throwable上报
                                    }
                                }
                            }
                    }
                }
            } catch (Throwable e) {
                CrashReport.postCatchedException(e);  // bugly会将这个throwable上报
                continue;
            } finally {
                FiCounter++;
            }
        }
    }


    public void over() {
        FbRun = false;
    }

    public void open() {
        if (!FbRun) {
            FbRun = true;
            this.start();
        }
    }

    protected void writeLog(String sFile, String orderid) {
        if (FoHandler != null) {
            Message oMess = new Message();
            oMess.what = 0;
            oMess.obj = new String[]{sFile, orderid};
            FoHandler.sendMessage(oMess);
        }
    }


}
