package com.feiling.dasong.ui.my

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseFragment
import kotlinx.android.synthetic.main.fragment_change_pw.*
import java.util.logging.Logger

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/14
 * @author ql
 */
class ChangePwFragment : BaseFragment() {

    val logger = Logger.getLogger(this::class.java.name)


    lateinit var topBarRightBtn: Button

    override fun createView(): View {
        return layoutInflater.inflate(R.layout.fragment_change_pw, null)
    }

    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        topbar.setTitle("修改密码")
        topbar.addLeftBackImageButton()
            .setOnClickListener {
                popBackStack()
            }

        topBarRightBtn = topbar.addRightTextButton("确定", R.id.topbar_right_yes_button)
        topBarRightBtn.isEnabled = false

        changePwEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                changePwHintTv.text =
                    if (changePwEt.text.toString() == changePwAgainEt.text.toString()) {
                        topBarRightBtn.isEnabled = true
                        ""
                    } else {
                        topBarRightBtn.isEnabled = false
                        "两次密码输入不相同，请检测重新输入"
                    }
            }
        })

        changePwAgainEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                changePwHintTv.text =
                    if (changePwEt.text.toString() == changePwAgainEt.text.toString()) {
                        topBarRightBtn.isEnabled = true
                        ""
                    } else {
                        topBarRightBtn.isEnabled = false
                        "两次密码输入不相同，请检测重新输入"
                    }
            }
        })


    }
}

