package com.example.downloadmanagertest;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    static final String TAG = "ApkClientActivity";
    Context mContext;
    DownloadManager manager ;
    DownloadCompleteReceiver receiver;
    Button downBtn ;
    String urlstr = "http://opentest.speech.sogou.com/download/model.awb";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        //获取下载服务
        manager =(DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        receiver = new DownloadCompleteReceiver();
        downBtn = (Button)findViewById(R.id.downBtn);
        downBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建下载请求
                DownloadManager.Request down=new DownloadManager.Request (Uri.parse(urlstr));
                //设置允许使用的网络类型，这里是移动网络和wifi都可以
                down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);
                //禁止发出通知，既后台下载
              //  down.setNotificationVisibility(View.VISIBLE);
                down.setShowRunningNotification(true);
                //不显示下载界面
                down.setDestinationInExternalPublicDir("xbc", "model.awb");
                down.setVisibleInDownloadsUi(true);
                //设置下载后文件存放的位置
               // down.setDestinationInExternalFilesDir(mContext, null, "model.awb");
                //将下载请求放入队列
                manager.enqueue(down);
            }
        });
    }
    //接受下载完成后的intent
    class DownloadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                Log.v(TAG," download complete! id : "+downId);
               // Toast.makeText(context, intent.getAction()+"id : "+downId, Toast.LENGTH_SHORT).show();
                Toast.makeText(mContext, "model.awb 下载完成", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    @Override
    protected void onResume() {
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        super.onResume();
    }
    
    @Override
    protected void onDestroy() {
        if(receiver != null)unregisterReceiver(receiver);
        super.onDestroy();
    }
}