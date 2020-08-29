package com.feiling.dasong.uitils

import com.feiling.dasong.comm.getDsMsg
import com.feiling.dasong.http.HttpFileUtils
import com.feiling.dasong.http.RxApiManager
import com.feiling.dasong.model.FileModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

/**
 * 描述：用于多文件 批量上传
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/8/22
 * @author ql
 */
class UploadFilesHelper {

    constructor(mkno: String, list: List<String>) {
        this.mkno = mkno
        this.needFileList = list;
    }

    private var needFileList: List<String>? = null;

    private var mkno: String? = null;

    var index: Int = 0;

    var onUploadListener: OnUploadListener? = null;

    public fun reset() {
        index = 0;
    }


    public fun uploadFile() {
        if (!hasNext()) {
            onUploadListener?.onUploadEnd(needFileList);
            return;
        }
        val next = next()
        onUploadListener?.onUploadStart(needFileList)
        val disposable = HttpFileUtils.uploadFile(mkno!!, File(next))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                val data = it.getData(FileModel::class.java)
                if (onUploadListener != null) {
                    val onUploadFailed =
                        onUploadListener!!.onUploadSuccess(needFileList, index, data)
                    if (onUploadFailed) {
                        uploadFile()
                    } else {
                        onUploadListener?.onUploadEnd(needFileList);
                    }
                }
            }, {
                it.printStackTrace()
                LogUtils.d(it.getDsMsg())
                if (onUploadListener != null) {
                    var onUploadFailed = onUploadListener!!.onUploadFailed(needFileList, index, it)
                    if (onUploadFailed) {
                        uploadFile()
                    } else {
                        onUploadListener?.onUploadEnd(needFileList);
                    }
                }
            })
        RxApiManager.instance.add(this, disposable)

    }


    private fun next(): String? {
        val path = needFileList?.get(index)
        index++;
        return path
    }

    private fun hasNext(): Boolean {
        if (needFileList == null) {
            return false;
        }
        return needFileList!!.size -1 >= index;
    }

    fun onDestroy() {
        RxApiManager.instance.cancel(this)
    }

    interface OnUploadListener {

        fun onUploadStart(list: List<String>?){}

        fun onUploadFailed(list: List<String>?, position: Int?, e: Throwable?): Boolean {
            return false;
        }

        /**
         * 上传失败
         * @param list 需要上传的列表
         * @param position 当前上传的角标
         * @return true 继续上传，false,取消上传
         */
        fun onUploadSuccess(list: List<String>?, position: Int?, file: FileModel?): Boolean {
            return true;
        }

        fun onUploadEnd(list: List<String>?)
    }


}