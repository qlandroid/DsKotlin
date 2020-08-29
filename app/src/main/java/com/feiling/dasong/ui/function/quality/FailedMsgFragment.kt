package com.feiling.dasong.ui.function.quality

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
 * 创建时间 2020/4/27
 * @author ql
 */
class FailedMsgFragment : BaseFragment() {
    companion object {
        private const val KEY_VAL = "KEY_VAL"
        private const val KEY_TITLE = "key-title"

        fun instance(title: String?): FailedMsgFragment {
            var bundle = Bundle()
            bundle.putString(KEY_TITLE, title)

            var failedMsgFragment = FailedMsgFragment()

            failedMsgFragment.arguments = bundle;

            return failedMsgFragment
        }

        fun getData(data: Intent?): String? {
            return data?.getStringExtra(KEY_VAL);
        }
    }


    override fun createView(): View {
        return layoutInflater.inflate(R.layout.fragment_failed_msg, null);
    }

    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        topbar.setTitle("描述信息")
        arguments?.let {
            topbar.setTitle(it.getString(KEY_TITLE))
        }

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
                    rejectHintTv.text = getString(R.string.please_input_content)
                } else {
                    rejectBtn.isEnabled = true;
                    rejectHintTv.text = ""
                }
            }

        })

        rejectBtn.setOnClickListener {
            val text = rejectEt.text
            if (TextUtils.isEmpty(text)) {
                rejectHintTv.text = getString(R.string.please_input_content)
            }
            val intent = Intent()
            intent.putExtra(KEY_VAL, text.toString())
            setFragmentResult(RESULT_OK, intent)
            popBackStack()

        }
    }
}