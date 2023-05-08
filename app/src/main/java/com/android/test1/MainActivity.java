package com.android.test1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.data);

        String file = "customers.xml";
        String result = "";
        try {
            InputStream is = getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            result = new String(buffer, "utf-8");

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(result));
            int eventType = xpp.getEventType();
            boolean bSet = false;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    ;
                } else if (eventType == XmlPullParser.START_TAG) {
                    String tag_name = xpp.getName();
                    if (tag_name.equals("name") || tag_name.equals("address"))
                        bSet = true;
                } else if (eventType == XmlPullParser.TEXT) {
                    if (bSet) {
                        String data = xpp.getText();
                        tv.append(data + "\n");
                        bSet = false;
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    ;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            tv.setText(e.getMessage());
        }
    }
}
