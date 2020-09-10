package com.sample.dataintenttest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    final static String TAG="DataIntentTest";
    TextView txtLog;
    BroadcastReceiver broadcastReceiver;
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtLog=findViewById(R.id.txtLog);
        txtLog.setMovementMethod(new ScrollingMovementMethod());
    }
    @Override
    protected void onResume() {
        addLog("onResume called");
        super.onResume();
        myRegisterReceiver();
    }

    @Override
    protected void onPause() {

        super.onPause();
        addLog("onPause called");
        unregisterReceiver(broadcastReceiver);
        broadcastReceiver=null;
    }

    void myRegisterReceiver() {
        if(broadcastReceiver!=null)
            return;
        addLog("myRegisterReceiver called");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                addLog("===================\nreceived Intent:");
                Log.d(TAG, intent.toURI());
                Bundle bundle = intent.getExtras();
                Bundle bundleExtra = intent.getBundleExtra("extrakey");
                String action = intent.getAction();
                Set<String> categories = intent.getCategories();
                String packet = intent.getPackage();
                ComponentName o = intent.getComponent();
                String classname="";
                if(o!=null) {
                    classname = o.getClassName();
                }
                if (bundle != null) {
                    addLog( "action="+action);
                    if(categories!=null) {
                        if (categories.size() > 0) {
                            for (String c : categories) {
                                Log.d(TAG, "category: " + c);
                                addLog("category: " + c);
                            }
                        }
                    }else{
                        addLog("categories=null");
                    }
                    addLog( "package="+packet);
                    addLog("classname="+classname);

                    for (String key : bundle.keySet()) {
                        Object value = bundle.get(key);
                        addLog(String.format("'%s': %s (%s)", key, value.toString(), value.getClass().getName()));
                    }
                }
                addLog("===================\n");
                //String sData = intent.getStringExtra(getResources().getString(R.string.datawedge_intent_key_data));

            }
        };
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction("com.sample.DATA");
        context.registerReceiver(broadcastReceiver, iFilter);// new IntentFilter(getResources().getString(R.string.actionBARCODECustom)));
        addLog("registering new Receiver with Action/Category");
    }

    void addLog(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, msg);
                String old=txtLog.getText().toString();
                old+="\n"+msg;
                txtLog.setText(old);
            }
        });
    }
}
