package com.feiling.dasong.comm

import android.content.Intent
import android.os.Handler
import com.feiling.dasong.http.RxApiManager

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/29
 * @author ql
 */
abstract class BasePresenter<IView> : IPresenter<IView> ,IController.ICallBack{
    var mView: IView? = null;
    var mHandler: Handler? = null;

    var iController: IController.IAction? = null;

    override fun attach(view: IView) {
        mView = view;
        mHandler = Handler();

    }

    override fun attachAction(action: IController.IAction?) {
        this.iController = action;
    }

    override fun detach() {
        mView = null;
        mHandler?.removeCallbacks(null)
        mHandler = null;
        iController = null;
        RxApiManager.cancel(this)
    }

    override fun onFragmentForResult(requestCode: Int?, resultCode: Int?, data: Intent?) {

    }
}