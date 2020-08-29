package com.feiling.dasong.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.feiling.dasong.R
import com.feiling.dasong.comm.CommActivity
import com.feiling.dasong.db.IPModel
import com.feiling.dasong.db.service.DbServiceBuild
import com.feiling.dasong.decorator.DivUtils
import com.feiling.dasong.http.BaseApi
import com.feiling.dasong.http.DsNetConfig
import com.feiling.dasong.ui.adapter.NavDetailsAdapter
import com.feiling.dasong.ui.model.NavDetailsModel
import com.feiling.dasong.uitils.DateUtils
import com.qmuiteam.qmui.kotlin.onClick
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_ip_change.*

class IpChangeActivity : CommActivity() {

    private val mIpAdapter = NavDetailsAdapter();
    override fun createView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_ip_change)
    }


    override fun initWidget() {
        super.initWidget()
        topbar.setTitle("请求地址变更")
        val defaultModel = DbServiceBuild.ipService.getDefaultModel()
        ipAddressEt.setText(defaultModel?.address)

        topbar.addLeftBackImageButton().onClick { finish() }

        topbar.addRightTextButton("保存", R.id.topbar_right_yes_button)
            .onClick {
                var address = ipAddressEt.text.toString()
                if (!isUrl(address)) {
                    address = setDefaultStart(address)
                }
                DbServiceBuild.ipService.setDefaultAddress(address)
                BaseApi.registerConfig(DsNetConfig(address))

                finish()
            }

        ipRv.layoutManager = LinearLayoutManager(this)
        ipRv.addItemDecoration(DivUtils.getDivDefault(this))
        ipRv.adapter = mIpAdapter

        val ipModelAll = DbServiceBuild.ipService.getIpModelAll()

        mIpAdapter.setOnItemClickListener { baseQuickAdapter: BaseQuickAdapter<Any?, BaseViewHolder>, view: View, i: Int ->
            ipAddressEt.setText(ipModelAll[i].address)
        }

        val subscribe = Observable.fromIterable(ipModelAll)
            .map {

                val navDetailsModel = NavDetailsModel()
                navDetailsModel.title = it.address
                navDetailsModel.subTitle = DateUtils.replaceYYYY_MM_dd_HHmmss(it.date)
                if (it.state == IPModel.STATE_USE) {
                    navDetailsModel.hint = "当前使用"
                }
                navDetailsModel
            }
            .subscribe(
                {
                    mIpAdapter.addData(it)
                }, {

                }, {
                    mIpAdapter.notifyDataSetChanged()
                }
            )
    }


    private fun isUrl(url: String): Boolean {
        return url.trim().startsWith("https://") || url.trim().startsWith("http://")
    }

    private fun setDefaultStart(url: String): String {
        return "http://${url}"
    }

}
