package com.feiling.dasong.ui.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.feiling.dasong.R
import com.feiling.dasong.ui.IpChangeActivity
import com.feiling.dasong.ui.my.ChangePwFragment
import com.feiling.dasong.uitils.LoginUtils
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView
import kotlinx.android.synthetic.main.home_my.view.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/2
 * @author ql
 */
class HomeMyController(context: Context) : HomeCommController(context) {


    override fun bindView(context: Context, layoutInflater: LayoutInflater): View? {
        return layoutInflater.inflate(R.layout.home_my, this);
    }

    override fun initWeight() {
        super.initWeight()
        topbar.setTitle("我的")

        val userModel = LoginUtils.getUserModel(context)
        userModel?.let {

            myUserCodeTv.text = it.code.orEmpty()
            myUserNameTv.text = it.name.orEmpty()
        }



        QMUIGroupListView.newSection(context)
            .addItemView(
                myListGroup.createItemView(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.my_change_pw
                    ),
                    "修改密码",
                    null,
                    QMUICommonListItemView.HORIZONTAL,
                    QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON
                )
            ) {
                mHomeControllerListener.startFragment(ChangePwFragment())
            }
            .addItemView(
                myListGroup.createItemView(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.my_change_pw
                    ),
                    "查看负责设备",
                    null,
                    QMUICommonListItemView.HORIZONTAL,
                    QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON
                )
            ) {

            }
            .addItemView(
                myListGroup.createItemView(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.my_change_pw
                    ),
                    "修改请求地址",
                    null,
                    QMUICommonListItemView.HORIZONTAL,
                    QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON
                )
            ) {
                mHomeControllerListener.startActivity(Intent(context, IpChangeActivity::class.java))
            }
            .addTo(myListGroup)


        myLogoutBtn.setOnClickListener {
            it.isEnabled = false;
            var loading = QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("退出登录中")
                .create()
            loading.show()
            it.postDelayed({
                if (loading.isShowing) {
                    loading.dismiss()
                }
                it.isEnabled = true;
                LoginUtils.setLogin(context, false)
                mHomeControllerListener.logout()
            }, 1_000)
        }
    }
}