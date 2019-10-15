package com.charles.downvideo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.Serializable;
import java.util.Queue;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_PERMISSION_STORAGE = 0x01;
    private Queue<String> leaveFourUrlQueue;
    protected Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkSDCardPermission();
        initClick();
        initData();
    }

    private void initClick() {
        findViewById(R.id.down_video_1).setOnClickListener(this);
        findViewById(R.id.down_video_2).setOnClickListener(this);
        findViewById(R.id.down_video_3).setOnClickListener(this);
        findViewById(R.id.down_video_4).setOnClickListener(this);
        findViewById(R.id.down_video_5).setOnClickListener(this);
        findViewById(R.id.down_video_6).setOnClickListener(this);
        findViewById(R.id.down_video_7).setOnClickListener(this);
        findViewById(R.id.down_video_8).setOnClickListener(this);
        findViewById(R.id.down_video_9).setOnClickListener(this);
        findViewById(R.id.down_video_10).setOnClickListener(this);
        findViewById(R.id.down_video_11).setOnClickListener(this);
        findViewById(R.id.down_video_12).setOnClickListener(this);
    }

    private void initData() {
        leaveFourUrlQueue = UrlManager.getLeaveFourUrlQueue();
    }

    /**
     * 检查SD卡权限
     */
    protected void checkSDCardPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //获取权限
            } else {
                Log.e("Charles", "权限被禁止，无法下载文件！");
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.down_video_4) {
            //开始下载
            intent = new Intent(MainActivity.this, DownLoadService.class);
            intent.putExtra("data", (Serializable) leaveFourUrlQueue);
            intent.putExtra("leave", 4);
            startService(intent);
        } else if (v.getId() == R.id.down_video_1) {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intent);
    }
}
