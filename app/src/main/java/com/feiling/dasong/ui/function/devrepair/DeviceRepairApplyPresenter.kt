package com.feiling.dasong.ui.function.devrepair

import android.content.Context
import android.net.Uri
import com.feiling.dasong.comm.BasePresenter
import com.feiling.dasong.comm.getDsMsg
import com.feiling.dasong.comm.response
import com.feiling.dasong.comm.toBody
import com.feiling.dasong.exception.DsException
import com.feiling.dasong.http.HttpFileUtils
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.model.DevModel
import com.feiling.dasong.model.DevRepairModel
import com.feiling.dasong.model.FileModel
import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.uitils.LogUtils
import com.feiling.dasong.uitils.UploadFilesHelper
import com.feiling.dasong.uitils.UriUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/29
 * @author ql
 */
class DeviceRepairApplyPresenter : BasePresenter<DeviceRepairApplyController.View>(),
    DeviceRepairApplyController.Presenter {

    companion object {
        const val BASE_PATH = "dev"
    }

    private var mSelectDev: DevModel? = null;
    private var mSelectImgList: MutableList<String>? = null;
    override fun attach(view: DeviceRepairApplyController.View) {
        mView = view;
    }

    override fun actionSubmit() {
        if (mSelectDev == null) {
            mView?.showToast("请选择设备");
            return;
        }

        var inputRemark = mView?.getInputRemark()
        if (inputRemark.orEmpty().length < 10) {
            mView?.showToast("请填写异常描述，不能少于10个字")
            return;
        }

        mView?.showTipLoading()
        if (mSelectImgList == null || mSelectImgList!!.size == 0) {
            submit()
            return;
        }

        uploadFile(mSelectImgList!!) {
            submit(it as MutableList<FileModel>)
        }


    }

    fun submit(fileList: MutableList<FileModel>? = null) {
        var inputRemark = mView?.getInputRemark()
        var devRepairModel = DevRepairModel()
        devRepairModel.devFileList = fileList;
        devRepairModel.ceqcode = mSelectDev?.ceqcode
        devRepairModel.problemDesc = inputRemark


        var toBody = devRepairModel.toBody()
        var subscribe = ServiceBuild.deviceRepairService.actionInsert(toBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                if (it.isFailed) {
                    throw DsException(it);
                }
                it.isSuccess
            }
            .subscribe({
                mView?.cancelTipLoading()
                mView?.showTipSuccess("提交成功") {
                    it?.dismiss()
                    mView?.shutDown()
                }
            }, {
                mView?.cancelTipLoading()
                it.printStackTrace()
                mView?.displayMsgDialog(it.getDsMsg())
            })
        addDisposable(subscribe)
    }

    fun uploadFile(needFile: List<String>, block: (List<FileModel>) -> Unit) {
        val uploadFilesHelper = UploadFilesHelper(BASE_PATH, needFile);
        val uploadFiles = mutableListOf<FileModel>()
        var uploadStatus = false;
        uploadFilesHelper.onUploadListener = object : UploadFilesHelper.OnUploadListener {
            override fun onUploadStart(list: List<String>?) {
                super.onUploadStart(list)
                mView?.showTipLoading("正在上传第1张图片")
            }

            override fun onUploadFailed(
                list: List<String>?,
                position: Int?,
                e: Throwable?
            ): Boolean {

                mView?.cancelTipLoading()
                mView?.displayMsgDialog("上传图片失败")
                uploadStatus = false
                return false
            }

            override fun onUploadSuccess(
                list: List<String>?,
                position: Int?,
                file: FileModel?
            ): Boolean {
                uploadFiles.add(file!!)
                uploadStatus = true;
                val nextPosition = position!! + 2
                if (position >= list!!.size - 1) {
                    //结束
                    return true;
                }
                mView?.showTipLoading("正在上传第${nextPosition}张图片")
                return true
            }

            override fun onUploadEnd(list: List<String>?) {
                if (uploadStatus) {
                    mView?.showTipLoading("图片上传成功")
                    block(uploadFiles)
                }

            }
        }
        uploadFilesHelper.uploadFile()
    }


    override fun actionSelectDevice() {
        mView?.displayDeviceSelectMoreMenu();
    }

    override fun loadDeviceDetailsByCode(code: String?) {
        mView?.showTipLoading("加载设备");
        var subscribe = ServiceBuild.deviceService.getById(code)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                if (it.isFailed) {
                    throw DsException(it)
                }
                it.getData(DevModel::class.java)
            }
            .map {
                mSelectDev = it;
                val mutableListOf = mutableListOf<LabelTextModel>(
                    LabelTextModel("设备名称", it.ceqname.orEmpty()),
                    LabelTextModel("设备编号", it.ceqcode.orEmpty())
                )

                it.list.orEmpty().forEach { dev ->
                    val span = if ((dev.name + dev.value).length > 10) 4 else 2
                    mutableListOf.add(LabelTextModel(dev.name.orEmpty(), dev.value.orEmpty(), span))
                }

                mutableListOf
            }
            .subscribe({
                mView?.setDeviceView(it)
                mView?.cancelTipLoading()
            }, {
                it.printStackTrace()
                mView?.cancelTipLoading()
                mView?.displayMsgDialog(it.getDsMsg().toString(), title = "提交异常")
            }, {

            })
        addDisposable(subscribe)
    }

    override fun onResultSelectDevCode(ceqcode: String?) {
        loadDeviceDetailsByCode(ceqcode)
    }

    override fun clickLookDeviceDetails() {
        mSelectDev?.let {
            mView?.toDeviceDetails(it.ceqcode!!)
        }

    }

    override fun onResultSelectImg(mSelected: List<Uri>?, context: Context) {
        var list = mutableListOf<String>()
        mSelected?.forEach {
            list.add(UriUtil.getPath(context, it))
        }
        mSelectImgList = list;
        mView?.setShowImages(list)
    }

    data class UploadFile(var uploadPath: String, var servicePath: String)


}