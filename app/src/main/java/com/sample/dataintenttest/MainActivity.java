package com.sample.dataintenttest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    for (String key : bundle.keySet()) {
                        Object value = bundle.get(key);
                        Log.d(TAG, String.format("'%s': %s (%s)", key, value.toString(), value.getClass().getName()));
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
                Log.d("IntentTest", msg);
                String old=txtLog.getText().toString();
                old+="\n"+msg;
                txtLog.setText(old);
            }
        });
    }
}
