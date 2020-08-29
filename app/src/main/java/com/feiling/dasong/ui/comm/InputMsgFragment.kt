package com.feiling.dasong.ui.comm

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseFragment
import kotlinx.android.synthetic.main.fragment_reject.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/5
 * @author ql
 */
class InputMsgFragment : BaseFragment() {
    companion object {
        private const val KEY_TITLE = "key-TITLE"
        private const val KEY_CONTENT = "KEY-CONTENT"
        const val RESULT_MSG = "RESULT_MSG"

        fun instance(title: String? = "", normalMsg: String? = ""): InputMsgFragment {
            var bundle = Bundle()
            bundle.putString(KEY_TITLE, title)
            bundle.putString(KEY_CONTENT, normalMsg)
            var inputMsgFragment = InputMsgFragment()
            inputMsgFragment.arguments = bundle;

            return inputMsgFragment
        }

        fun getInputMsg(data: Intent?): String? {
            return data?.getStringExtra(RESULT_MSG)
        }
    }

    override fun createView(): View {
        return layoutInflater.inflate(R.layout.fragment_reject, null)
    }


    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        var title = arguments?.getString(KEY_TITLE)
        var content = arguments?.getString(KEY_CONTENT)
        if (title.isNullOrEmpty()) {
            title = "编辑内容"
        }
        topbar.setTitle(title)
        topbar.addLeftBackImageButton().setOnClickListener {
            popBackStack()
        }
        rejectEt.setText(content);

        rejectEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 0) {
                    rejectBtn.isEnabled = false;
                    rejectHintTv.text = "请输入内容"
                } else {
                    rejectBtn.isEnabled = true;
                    rejectHintTv.text = ""
                }
            }

        })

        rejectBtn.setOnClickListener {
            val text = rejectEt.text
            if (TextUtils.isEmpty(text)) {
                rejectHintTv.text = "请输入内容"
            }
            var intent = Intent()
            intent.putExtra(RESULT_MSG, rejectEt.text.toString())
            setFragmentResult(RESULT_OK, intent)
            popBackStack()
        }
    }
}