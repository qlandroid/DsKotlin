package com.feiling.dasong.comm

import android.content.Intent
import androidx.fragment.app.Fragment

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/7/7
 * @author ql
 */
interface IController {

    interface IAction {
        fun actionStartFragment(fragment: BaseFragment);
        fun actionStartFragmentForResult(fragment: BaseFragment, requestCode: Int)

    }

    interface ICallBack {
        fun onFragmentForResult(requestCode: Int?, resultCode: Int?, data: Intent?)
    }


}