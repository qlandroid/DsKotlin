package com.feiling.dasong.comm

import com.feiling.dasong.http.RxApiManager
import io.reactivex.disposables.Disposable

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/29
 * @author ql
 */
interface IPresenter<T> {


    fun attach(view: T)

    fun attachAction(action: IController.IAction?)

    fun addDisposable(disposable: Disposable) {
        RxApiManager.add(this, disposable)
    }

    fun detach() {
        RxApiManager.cancel(this)
    }



}