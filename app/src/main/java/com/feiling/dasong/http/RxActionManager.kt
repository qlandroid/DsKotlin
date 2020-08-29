package com.feiling.dasong.http

import io.reactivex.disposables.Disposable


/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/13
 * @author ql
 */
interface RxActionManager<T> {
    fun add(tag: T, disposable: Disposable)
    fun remove(tag: T)

    fun cancel(tag: T)

    fun cancelAll()
}