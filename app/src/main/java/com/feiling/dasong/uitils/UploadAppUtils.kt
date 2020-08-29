package com.feiling.dasong.uitils

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import java.io.File

/**
 * 描述：用于版本更新控制
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/23
 * @author ql
 */
object UploadAppUtils {
    private const val NOTIFY_CHANNEL_ID = "UPLOAD_ID"
    private const val NOTIFY_ID = 200;
    fun showUploadNotify(context: Context) {
        var notification = NotificationCompat.Builder(
            context,
            NOTIFY_CHANNEL_ID
        )
            .setContentTitle("RMM")
            .setContentText("正在更新")
            .setProgress(100, 20, true)
            .setAutoCancel(false)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC).build()

        var manager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;
        manager.notify(NOTIFY_ID, notification)
    }

    fun closeUploadNotify(context: Context) {
        var manager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;
        manager.cancel(NOTIFY_ID)
    }


    fun installApk(context: Context, file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        //放在此处
//由于没有在Activity环境下启动Activity,所以设置下面的标签
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        var apkUri: Uri? = null
        //判断版本是否是 7.0 及 7.0 以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            apkUri = FileProvider.getUriForFile(context, "com.feiling.dasong", file)
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            apkUri = Uri.fromFile(file)
        }
        intent.setDataAndType(
            apkUri,
            "application/vnd.android.package-archive"
        )
        context.startActivity(intent)
    }


}