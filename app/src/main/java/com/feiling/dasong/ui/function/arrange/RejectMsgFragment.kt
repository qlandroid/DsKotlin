package com.feiling.dasong.ui.function.arrange

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseFragment
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import kotlinx.android.synthetic.main.fragment_reject.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/5
 * @author ql
 */
class RejectMsgFragment : BaseFragment() {
    override fun createView(): View {
        return layoutInflater.inflate(R.layout.fragment_reject, null)
    }


    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        topbar.setTitle("工序异常驳回")
        topbar.addLeftBackImageButton().setOnClickListener {
            popBackStack()
        }

        rejectEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 0) {
                    rejectBtn.isEnabled = false;
                    rejectHintTv.setText(R.string.please_input_content)
                } else {
                    rejectBtn.isEnabled = true;
                    rejectHintTv.text = ""
                }
            }

        })

        rejectBtn.setOnClickListener {
            val text = rejectEt.text
            if (TextUtils.isEmpty(text)) {
                rejectHintTv.setText(R.string.please_input_content)
            }
            var loadingDialog = QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(getString(R.string.submiting))
                .create(false)
            loadingDialog.show()
            it.postDelayed({
                loadingDialog?.dismiss()
                var successTip = QMUITipDialog.Builder(context)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                    .setTipWord(getString(R.string.submit_success))
                    .create(false)
                successTip.show()
                it.postDelayed({
                    successTip?.dismiss()
                    popBackStack()
                }, 2_000)

            }, 2_000)
        }
    }
}