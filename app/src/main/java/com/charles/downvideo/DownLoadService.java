package com.charles.downvideo;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;
import java.util.Queue;

public class DownLoadService extends IntentService {

    public String FILE_PATH;
    private DownloadTask task;

    private int currentFinishTask = 0;
    private int startQueueSize;
    private static int thread_task_max = 12;

    public DownLoadService() {
        super("downloadService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int leave = intent.getIntExtra("leave", 0);
        String sizeType = intent.getStringExtra("sizeType");
        Queue<String> leaveOneUrlQueue = (Queue<String>) intent.getSerializableExtra("data");
        startQueueSize = leaveOneUrlQueue.size();
        toDownVideo(leaveOneUrlQueue, leave, sizeType.equals("all"));
    }

    private void toDownVideo(Queue<String> queue, int leave, boolean isRealAll) {
        if (queue.size() > thread_task_max) {
            FILE_PATH = Environment.getExternalStorageDirectory() + File.separator + "EF_VIDEO_" + leave + File.separator;
            OkDownload.getInstance().setFolder(FILE_PATH);
            OkDownload.getInstance().getThreadPool().setCorePoolSize(3);
        }

        int currentSize;
        int currentStart;
        if (queue.size() > thread_task_max) {
            currentSize = thread_task_max;
            currentStart = 0;
        } else {
            currentStart = thread_task_max;
            currentSize = queue.size();
        }

        for (int i = 1; i <= currentSize; i++) {
            String url = queue.poll();
            String fileName = "Leave" + leave + "_" + (currentStart + i) + ".mp4";
            GetRequest<File> request = OkGo.get(url);
            toTask(url, fileName, request, queue, leave, isRealAll);
        }
        Log.e("Charles", "size===" + queue.size());
    }

    private void toTask(final String url, String fileName, GetRequest<File> request, final Queue<String> queue, final int leave, final boolean isRealAll) {
        task = OkDownload.request(url, request)
                .register(new DownloadListener(url) {
                    @Override
                    public void onStart(Progress progress) {

                    }

                    @Override
                    public void onProgress(Progress progress) {
                    }

                    @Override
                    public void onError(Progress progress) {
                        Throwable throwable = progress.exception;
                        if (throwable != null) throwable.printStackTrace();
                    }

                    @Override
                    public void onFinish(File file, Progress progress) {
                        currentFinishTask++;
                        Log.e("Charles2", "当前完成第" + currentFinishTask + "个Video任务");
                        if (currentFinishTask == queue.size()) {
                            Log.e("Charles2", "进行第二次下载");
                            toDownVideo(queue, leave, isRealAll);
                        } else if (startQueueSize == currentFinishTask) {
                            Toast.makeText(getApplicationContext(), "下载完成", Toast.LENGTH_SHORT).show();
                            //leave_1完成后，下载leave_2
                            if (isRealAll) {
                                int currentLeave = leave + 1;
                                startQueueSize = UrlManager.getleaverForQueue(currentLeave).size();
                                toDownVideo(UrlManager.getleaverForQueue(currentLeave), currentLeave, true);
                            }
                        }
                    }

                    @Override
                    public void onRemove(Progress progress) {

                    }
                }).fileName(fileName).save();
        task.start();
    }

}
