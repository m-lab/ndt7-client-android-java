package it.alessangiorgi.ndt7clientandroidjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import speedtest.model.Server;

public class MainActivity extends AppCompatActivity implements SpeedTestHelper.OnData, ServerAndISPHelper.OnServerListener {
    private static final String TAG = "SpeedTest";
    private SpeedTestHelper speedTestHelper;
    private ServerAndISPHelper serverAndISPHelper;
    private TextView serverText,pingText,jitterText,downloadText,uploadText,providerIpText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        serverAndISPHelper = new ServerAndISPHelper();
        serverAndISPHelper.setOnServerListener(this);
        speedTestHelper = new SpeedTestHelper();
        speedTestHelper.setOnDataListener(this);
        serverAndISPHelper.getInfo();
    }
    private void initView(){
        serverText = findViewById(R.id.server);
        pingText = findViewById(R.id.ping);
        jitterText = findViewById(R.id.jitter);
        downloadText = findViewById(R.id.download);
        uploadText = findViewById(R.id.upload);
        providerIpText = findViewById(R.id.providerIp);

    }
    private void putMsgOnUI(String msg, TextView tv) {
        runOnUiThread(() -> tv.setText(msg));
    }
    private void speedTest(List<Server> server, int picked){
        try {
                speedTestHelper.runSpeedTest(server.get(picked).urls.wssDownload, server.get(picked).urls.wssUpload);
        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }
    @Override
    public void onServerSearchStarted() {
        Log.e(TAG, "onServerSearchStarted: " );
    }

    @Override
    public void onServerFound(List<Server> server) {
        String cityWithCountry = String.format("%s, %s",
                server.get(0).location.city, server.get(0).location.country);
        Log.e(TAG, "onServerFound: "+cityWithCountry );
        String formattedStr = String.format("Server: %s", cityWithCountry);
        serverText.setText(formattedStr);
        speedTest(server,0);
    }

    @Override
    public void onServerError() {
        Log.e(TAG, "onServerError: " );

    }

    @Override
    public void onPing(int ping) {
        Log.e(TAG, "onPing: "+ping );
        String formattedStr = String.format(Locale.getDefault(),"Ping: %d ms",ping);
        putMsgOnUI(formattedStr,pingText);
    }

    @Override
    public void onJitter(double jitter) {
        Log.e(TAG, "onJitter: "+jitter );
        String formattedStr = String.format(Locale.getDefault(),"RTTvar: %f ms",jitter);
        putMsgOnUI(formattedStr,jitterText);
    }

    @Override
    public void onDownload(double download) {
        Log.e(TAG, "onDownload: "+download );
        String downloadStr = String.format(Locale.getDefault(),"Download: %f Mbps",download);
        putMsgOnUI(downloadStr,downloadText);
    }

    @Override
    public void onUpload(double upload) {
        Log.e(TAG, "onUpload: "+upload );
        String uploadStr = String.format(Locale.getDefault(),"Upload: %f Mbps",upload);
        putMsgOnUI(uploadStr,uploadText);
    }

    @Override
    public void onProviderIp(String providerIp) {
        Log.e(TAG, "onProviderIp: "+providerIp );
        String providerIpStr = String.format(Locale.getDefault(),"Your ip: %s",providerIp);
        putMsgOnUI(providerIpStr,providerIpText);
    }

    @Override
    public void onFinished() {
        Log.e(TAG, "onFinished: " );
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,"Finished",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onFinishedDownload() {
        Log.e(TAG, "onFinishedDownload: " );
        runOnUiThread(() -> Toast.makeText(MainActivity.this,"Starting Upload...",Toast.LENGTH_LONG).show());
    }

    @Override
    public void onError() {
        Log.e(TAG, "onError: " );
    }
}