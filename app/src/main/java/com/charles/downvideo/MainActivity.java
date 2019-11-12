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

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;

import java.io.File;
import java.io.Serializable;
import java.util.Queue;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_PERMISSION_STORAGE = 0x01;
    private Queue<String> leaveOneUrlQueue;
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
        findViewById(R.id.down_video_all).setOnClickListener(this);
        findViewById(R.id.down_video_1).setOnClickListener(this);
    }

    private void initData() {
        leaveOneUrlQueue = UrlManager.getLeaveOneUrlQueue();
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

    private int currentFinishTask = 0;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.down_video_1) {
            //测试错的链接
            for (int i = 0; i < 2; i++) {
                String url = "https://cns2.ef-cdn.com/Juno/13/05/49/v/130549/GE_8.5.3_v2.mp4";
                GetRequest<File> request = OkGo.get(url);
                OkDownload.request(url, request)
                        .register(new DownloadListener(url) {
                            @Override
                            public void onStart(Progress progress) {
                                Log.e("Charles", "onStart==" + progress);
                            }

                            @Override
                            public void onProgress(Progress progress) {
                                Log.e("Charles", "onProgress==" + progress);
                            }


                            @Override
                            public void onError(Progress progress) {
                                Log.e("Charles", "onError==" + progress);
                                Log.e("Charles", "onError==" + progress.exception);
                                Throwable throwable = progress.exception;
                                if (throwable != null) throwable.printStackTrace();
                            }

                            @Override
                            public void onFinish(File file, Progress progress) {
                                currentFinishTask++;
                                Log.e("Charles", "完成===" +currentFinishTask);
                                Log.e("Charles2", "onFinish==" + progress.speed);
                            }

                            @Override
                            public void onRemove(Progress progress) {
                                Log.e("Charles2", "onRemove==" + progress.fileName);

                            }
                        }).fileName("EF_error.mp4").save().start();
            }
        } else if (v.getId() == R.id.down_video_all) {
            //开始下载
            intent = new Intent(MainActivity.this, DownLoadService.class);
            intent.putExtra("data", (Serializable) leaveOneUrlQueue);
            intent.putExtra("leave", 1);
            intent.putExtra("sizeType", "all");
            startService(intent);
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
