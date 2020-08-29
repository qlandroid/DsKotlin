package com.feiling.dasong

import android.app.DownloadManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Binder
import android.os.Environment
import android.os.IBinder
import android.webkit.MimeTypeMap
import com.feiling.dasong.uitils.UploadAppUtils
import java.io.File


class DownloadAppService : Service() {
    companion object {
        const val ACTION_DOWNLOAD = "com.feiling.dasong.APK.DOWNLOAD"
        const val KEY_URL = "KEY-URL"
        const val KEY_VERSION_NAME = "KEY-VERSION-NAME"
        fun actionStartDownload(downloadUrl: String, versionName: String?): Intent {
            var intent = Intent(ACTION_DOWNLOAD)
            intent.putExtra(KEY_URL, downloadUrl);
            intent.putExtra(KEY_VERSION_NAME, versionName)
            return intent;
        }

        fun getUrl(intent: Intent): String {
            return intent.getStringExtra(KEY_URL);
        }

        fun getVersionName(intent: Intent): String {
            return intent.getStringExtra(KEY_VERSION_NAME)
        }

    }

    private var downloadFile: File? = null;
    private var mTaskId: Long? = null;

    private val uploadBinder = UploadBinder()

    override fun onCreate() {
        super.onCreate()
        if (downloadFile == null) {
            var rootDir = Environment.getExternalStorageDirectory()
            var packageDir = File(rootDir, "com.feiling.dasong")
            var apkDir = File(packageDir, "apk");
            if (apkDir.exists()) {
                apkDir.mkdirs();
            }
            downloadFile = File(apkDir, "rmm_app_new.apk");
        }

        registerReceiver(actionReceiver, IntentFilter(ACTION_DOWNLOAD))
    }


    override fun onBind(intent: Intent): IBinder {
        return uploadBinder
    }


    inner class UploadBinder : Binder() {
        //是否启动
        var isStartDownload: Boolean = false;

    }


    //使用系统下载器下载
    private fun downloadAPK(
        versionUrl: String,
        versionName: String
    ) { //创建下载任务
        uploadBinder.isStartDownload = true;
        val request = DownloadManager.Request(Uri.parse(versionUrl))
        request.setAllowedOverRoaming(false) //漫游网络是否可以下载
        //设置文件类型，可以在下载结束后自动打开该文件
        val mimeTypeMap = MimeTypeMap.getSingleton()
        val mimeString =
            mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(versionUrl))
        request.setMimeType(mimeString)
        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        request.setVisibleInDownloadsUi(true)
        //sdcard的目录下的download文件夹，必须设置
//        request.setDestinationInExternalPublicDir("/download/", versionName)
        request.setDestinationInExternalFilesDir(
            this,
            downloadFile!!.parentFile.absolutePath,
            downloadFile!!.name
        )//,也可以自己制定下载路径
//将下载请求加入下载队列
        var downloadManager: DownloadManager =
            getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        //加入下载队列后会给该任务返回一个long型的id，
//通过该id可以取消任务，重启任务等等，看上面源码中框起来的方法
        mTaskId = downloadManager.enqueue(request)
        //注册广播接收者，监听下载状态
        registerReceiver(
            receiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }


    //广播接受者，接收下载状态
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            checkDownloadStatus() //检查下载状态
        }
    }

    //检查下载状态
    private fun checkDownloadStatus() {
        var query = DownloadManager.Query();
        query.setFilterById(mTaskId!!);//筛选下载任务，传入任务ID，可变参数
        var downloadManager: DownloadManager =
            getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        var c = downloadManager.query(query);
        if (c.moveToFirst()) {
            var status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            when (status) {
                DownloadManager.STATUS_PAUSED -> {
//                    Logger.getLogger().i(">>>下载暂停");
                    uploadBinder.isStartDownload = false;
                }

                DownloadManager.STATUS_PENDING -> {
//                    MLog.i(">>>下载延迟");
                }

                DownloadManager.STATUS_RUNNING -> {
//                    MLog.i(">>>正在下载");

                }
                DownloadManager.STATUS_SUCCESSFUL -> {
//                    MLog.i(">>>下载完成");
                    //下载完成安装APK
                    //downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + versionName;
                    UploadAppUtils.installApk(this, downloadFile!!)
                    uploadBinder.isStartDownload = false;
                }

                DownloadManager.STATUS_FAILED -> {
//                    MLog.i(">>>下载失败");
                    uploadBinder.isStartDownload = false;
                }
            }
        }
    }

    private var actionReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                var downloadUrl = getUrl(intent)
                if (!uploadBinder.isStartDownload) {
                    downloadAPK(downloadUrl, getVersionName(intent))
                }

            }
        }
    }


}
