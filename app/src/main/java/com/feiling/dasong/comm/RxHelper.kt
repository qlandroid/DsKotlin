package com.feiling.dasong.comm

import com.feiling.dasong.exception.DsException
import com.feiling.dasong.model.base.ResponseModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/7/1
 * @author ql
 */
object RxHelper {


    public fun <T> takeThread(observable: Observable<T>): Observable<T> {
        return observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }

    public fun takeRequestData(observable: Observable<ResponseModel>): Observable<ResponseModel> {
        return takeThread(observable)
            .filter {
                if (it.isFailed) {
                    throw DsException(it)
                }
                it.isSuccess
            }.map {
                it
            }
    }
}