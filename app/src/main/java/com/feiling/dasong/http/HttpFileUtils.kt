package com.feiling.dasong.http

import com.feiling.dasong.model.base.ResponseModel
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/8/22
 * @author ql
 */
object HttpFileUtils {

    /**
     * 用于单个文件上传
     * @param mkno 功能模块
     * @param file 需要上传的文件绝对路径
     */
    fun uploadFile(mkno: String, file: File): Observable<ResponseModel> {
        var create = RequestBody.create(MediaType.parse("image/*"), file)
        var createFormData = MultipartBody.Part.createFormData("file", file.name, create)
        return ServiceBuild.commService.uploadFile(mkno, createFormData)
    }

    fun replaceImgUrl(path: String): String {
        return "${BaseApi.mConfig.configBaseUrl()}/file/msgdowload?path=$path"
    }

}