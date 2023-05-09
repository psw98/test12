package com.android.test1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Main2 extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.data);

        String serviceUrl = "http://ws.bus.go.kr/api/rest/busRouteInfo/getBusRouteList";
        String serviceKey = "dXP6O0Gea1tGb5qO3i%2BucqTUR6I6Y19Q2YhhnoztEF%2BZTYKsiQK3Z11FOoF26nFgq16Kc7%2BgEcCD%2FGkSxjNXjQ%3D%3D";
        String strSrch = "406";
        String strUrl= serviceUrl+"?ServiceKey="+serviceKey+"&strSrch="+strSrch;
        DownloadWebpageTask1 task1= new DownloadWebpageTask1();
        task1.execute(strUrl);
    }

    private class DownloadWebpageTask1  extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... urls) {
                try {
                    return (String)downloadUrl((String)urls[0]);
                } catch (IOException e) {
                    return e.getMessage();
                }
            }

            protected void onPostExecute(String result) {
                String headerCd="";
                String busRouteId="";
                String busRouteNm="";
                try {
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp=factory.newPullParser();

                    xpp.setInput(new StringReader(result));
                    int eventType = xpp.getEventType();


                    boolean bSet_headerCd=false;
                    boolean bSet_busRouteId=false;
                    boolean bSet_busRouteNm=false;

                    while(eventType != XmlPullParser.END_DOCUMENT) {
                        if(eventType == XmlPullParser.START_DOCUMENT) {
                            ;
                        } else if(eventType == XmlPullParser.START_TAG) {
                            String tag_name=xpp.getName();
                            if(tag_name.equals("headerCd")) {
                                bSet_headerCd=true;
                            }
                            if(tag_name.equals("busRouteId")) {
                                bSet_busRouteId=true;
                            }
                            if(tag_name.equals("busRouteNm")) {
                                bSet_busRouteNm=true;
                            }
                        } else if(eventType == XmlPullParser.TEXT) {
                            if(bSet_headerCd) {
                                headerCd = xpp.getText();
                                tv.append("headerCd: " + headerCd + "\n");
                                bSet_headerCd=false;
                            }

                            if(headerCd.equals("0")) {
                                if(bSet_busRouteId) {
                                    busRouteId=xpp.getText();
                                    tv.append("busRouteId: "+busRouteId+"\n");
                                    bSet_busRouteId=false;
                                }
                                if(bSet_busRouteNm) {
                                    busRouteNm=xpp.getText();
                                    tv.append("busRouteNm: "+busRouteNm+"\n");
                                    bSet_busRouteNm=false;
                                }
                            }
                        } else if(eventType == XmlPullParser.END_TAG) {
                            ;
                        }
                        eventType = xpp.next();
                    }
                } catch (Exception e) {
                    tv.setText(e.getMessage());
                }

                String serviceUrl = "http://ws.bus.go.kr/api/rest/buspos/getBusPosByRtid";
                String serviceKey = "dXP6O0Gea1tGb5qO3i%2BucqTUR6I6Y19Q2YhhnoztEF%2BZTYKsiQK3Z11FOoF26nFgq16Kc7%2BgEcCD%2FGkSxjNXjQ%3D%3D";
                String strUrl= serviceUrl+"?ServiceKey="+serviceKey+"&busRouteId="+busRouteId;
                DownloadWebpageTask2 task2 = new DownloadWebpageTask2();
                task2.execute(strUrl);
            }
            private String downloadUrl(String myurl) throws IOException {

                HttpURLConnection conn=null;
                try{
                    URL url=new URL(myurl);
                    conn=(HttpURLConnection) url.openConnection();
                    BufferedInputStream buf=new BufferedInputStream(conn.getInputStream());
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(buf,"utf-8"));

                    String line=null;
                    String page="";
                    while((line= bufferedReader.readLine()) != null) {
                        page += line;
                    }
                    return page;
                } finally {
                    conn.disconnect();
                }
            }
    }

            private class DownloadWebpageTask2 extends  DownloadWebpageTask1 {

                protected void onPostExecute(String result) {

                    String headerCd="";
                    String plainNo="";
                    String gpsX="";
                    String gpsY="";

                    boolean bSet_headerCd=false;
                    boolean bSet_plainNo=false;
                    boolean bSet_gpsX=false;
                    boolean bSet_gpsY=false;

                    tv.append("==== 버스 위치 =====\n");
                    try {
                        XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
                        factory.setNamespaceAware(true);
                        XmlPullParser xpp = factory.newPullParser();

                        xpp.setInput(new StringReader(result));
                        int eventType=xpp.getEventType();
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            if(eventType == XmlPullParser.START_DOCUMENT) {
                                ;
                            } else if(eventType == XmlPullParser.START_TAG) {
                                String tag_name = xpp.getName();
                                if (tag_name.equals("headerCd")) {
                                    bSet_headerCd=true;
                                }
                                if (tag_name.equals("gpsX")) {
                                    bSet_gpsX=true;
                                }
                                if (tag_name.equals("gpsY")) {
                                    bSet_gpsY=true;
                                }
                                if (tag_name.equals("plainNo")) {
                                    bSet_plainNo=true;
                                }
                            } else if (eventType == XmlPullParser.TEXT) {
                                if (bSet_headerCd) {
                                    headerCd=xpp.getText();
                                    tv.append("headerCd: "+headerCd+"\n");
                                    bSet_headerCd=false;
                                }

                                if (headerCd.equals("0")) {
                                    if(bSet_gpsX) {
                                        gpsX=xpp.getText();
                                        tv.append("gpsX: "+gpsX+"\n");
                                        bSet_gpsX=false;
                                    }
                                    if (bSet_gpsY) {
                                        gpsY=xpp.getText();
                                        tv.append("gpsY: "+gpsY+"\n");
                                        bSet_gpsY=false;
                                    }
                                    if (bSet_plainNo) {
                                        plainNo=xpp.getText();
                                        tv.append("plainNo: "+plainNo+"\n");
                                        bSet_plainNo=false;
                                    }
                                }
                            } else if(eventType == XmlPullParser.END_TAG) {
                                ;
                            }
                            eventType=xpp.next();
                        }
                    } catch (Exception e) {
                        tv.setText(e.getMessage());
                    }
                }

            }


}