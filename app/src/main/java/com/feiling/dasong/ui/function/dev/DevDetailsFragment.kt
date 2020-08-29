package com.feiling.dasong.ui.function.dev

import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseFragment
import com.feiling.dasong.exception.DsException
import com.feiling.dasong.http.RxApiManager
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.model.AttrModel
import com.feiling.dasong.model.DevModel
import com.feiling.dasong.model.EmployeeModel
import com.feiling.dasong.model.GroupModel
import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.ui.model.NavDetailsModel
import com.feiling.dasong.uitils.ExceptionUtils
import com.qmuiteam.qmui.kotlin.onClick
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_device_details.*

/**
 * 描述：设备详情
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/21
 * @author ql
 */
class DevDetailsFragment : BaseFragment(), IDevDetailsPresenter {
    companion object {
        private const val KEY_DEVICE_CODE = "KEY-DEVICE-CODE"
        fun instance(devCode: String?): DevDetailsFragment {

            val bundle = Bundle()
            bundle.putString(KEY_DEVICE_CODE, devCode)

            val devDetailsFragment = DevDetailsFragment()
            devDetailsFragment.arguments = bundle
            return devDetailsFragment
        }
    }

    private lateinit var deviceView: IDevDetailsView
    private lateinit var mDevCode: String
    private lateinit var mDevDetails: DevModel

    override fun createView(): View {
        return layoutInflater.inflate(R.layout.fragment_device_details, null)
    }


    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        topbar.setTitle("设备详情")
        topbar.addLeftBackImageButton().onClick { popBackStack() }

        deviceView = devDetailsView
        devDetailsView.presenter = this;
        mDevCode = arguments!!.getString(KEY_DEVICE_CODE)!!

        emptyView.showEmptyView()
        loadData()

    }

    private fun loadData() {
        val subscribe = ServiceBuild.deviceService.getById(mDevCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                if (it.isFailed) {
                    throw DsException(it)
                }
                it.getData(DevModel::class.java)
            }
            .subscribe(
                {
                    mDevDetails = it;
                    deviceView.setBaseDetails(
                        mutableListOf(
                            LabelTextModel("设备编码", it.ceqcode),
                            LabelTextModel("设备名称", it.ceqname)
                        )
                    )
                    setAttrs(it.list.orEmpty())
                    setGroups(it.groupsList.orEmpty())
                    deviceView.showGroup(it.groupsList.orEmpty().isNotEmpty());
                    setPrincipals(it.personnelsList.orEmpty())
                    deviceView.showPrincipals(it.personnelsList.orEmpty().isNotEmpty())
                },
                {
                    it.printStackTrace()
                    topbar?.post {
                        emptyView.showFailed(msg = ExceptionUtils.getExceptionMessage(it)) {
                            this.loadData()
                        }
                    }
                }, {
                    emptyView.hideEmpty()
                })

        RxApiManager.instance.add(this, subscribe)
    }

    private fun setPrincipals(pList: List<EmployeeModel>?) {
        var mutableListOf = mutableListOf<NavDetailsModel>()
        val subscribe = Observable.fromIterable(pList)
            .map {
                val navDetailsModel = NavDetailsModel()
                navDetailsModel.title = it.firstname
                navDetailsModel.subTitle = it.code
                navDetailsModel.leftIcon = R.drawable.icon_group
                navDetailsModel.showRight = false
                navDetailsModel
            }
            .subscribe({
                mutableListOf.add(it)
            }, {}, {
                deviceView.setPrincipals(mutableListOf)
            })
        RxApiManager.instance.add(this, subscribe)

    }

    private fun setGroups(groupsList: List<GroupModel>?) {
        var mutableListOf = mutableListOf<NavDetailsModel>()
        val subscribe = Observable.fromIterable(groupsList)
            .map {
                val navDetailsModel = NavDetailsModel()
                navDetailsModel.title = it.gruopName
                navDetailsModel.subTitle = it.groupCode
                navDetailsModel.showRight = true;
                navDetailsModel.leftIcon = R.drawable.icon_group_member
                navDetailsModel
            }
            .subscribe({
                mutableListOf.add(it)
            }, {}, {
                deviceView.setGroups(mutableListOf)
            })
        RxApiManager.instance.add(this, subscribe)

    }

    private fun setAttrs(attrs: List<AttrModel>?) {
        var mutableListOf = mutableListOf<LabelTextModel>()
        val subscribe = Observable.fromIterable(attrs)
            .map {
                var span = 2
                if (it.value.orEmpty().length > 8) {
                    span = 4
                }
                LabelTextModel(it.name, it.value, span, Gravity.LEFT)
            }
            .subscribe({
                mutableListOf.add(it)
            }, {}, {
                deviceView.setAttrs(mutableListOf)
            })
        RxApiManager.instance.add(this, subscribe)

    }

    override fun actionLookGroupDetails(position: Int) {
        var groupModel = mDevDetails.groupsList?.get(position)
        mDialogTipHelper.showLoading("加载组信息")
        var subscribe = ServiceBuild.groupService.loadGroupDetails(groupModel?.groupCode!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                if (it.isFailed) {
                    throw DsException(it);
                }
                it.getData(GroupModel::class.java)
            }
            .subscribe({
                displayGroupDetails(it)
            }, {
                it.printStackTrace()
                topbar?.post {
                    mDialogTipHelper.displayMsgDialog(msg = ExceptionUtils.getExceptionMessage(it))
                    mDialogTipHelper.dismissLoading()
                }
            }, {
                mDialogTipHelper.dismissLoading()
            })
        RxApiManager.instance.add(this, subscribe)
    }

    private fun displayGroupDetails(group: GroupModel) {
        var build = QMUIBottomSheet.BottomListSheetBuilder(context)
            .setGravityCenter(true)
            .setTitle("${group.gruopName}(${group.groupCode})成员信息")
            .setRadius(40)
        var opGroupMembers = group.opGroupMembers
        opGroupMembers?.forEach {
            build
                .addItem(
                    R.drawable.icon_group_member,
                    "${it.firstname}(${it.opMemberCode})",
                    it.opMemberCode
                )
        }

        build.setAddCancelBtn(true)
        var bottomSheet = build.build()
        bottomSheet.show()

    }
}