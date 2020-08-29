package com.feiling.dasong.http

import io.reactivex.disposables.Disposable


/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/13
 * @author ql
 */
object RxApiManager {
    val instance: RxActionManager<Any> = RxDisposable()


    fun add(tag: Any, disposable: Disposable) {
        instance.add(tag, disposable);
    }

    fun cancel(tag: Any) {
        instance.cancel(tag);
    }


}