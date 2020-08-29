package com.feiling.dasong.http

import com.feiling.dasong.uitils.LogUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/30
 * @author ql
 */
class RxDisposable : RxActionManager<Any> {

    private val maps: MutableMap<Any, CompositeDisposable?> by lazy { mutableMapOf<Any, CompositeDisposable?>() }

    override fun add(tag: Any, disposable: Disposable) {
        var comDisposable = maps[tag]
        if (comDisposable == null) {
            comDisposable = CompositeDisposable();
            maps[tag] = comDisposable;
        }
        comDisposable.add(disposable)
        LogUtils.d("添加监听->${tag.javaClass.name},${comDisposable.size()}")

    }

    override fun remove(tag: Any) {
        if (maps.isNotEmpty()) {
            maps.remove(tag);
        }
    }

    override fun cancel(tag: Any) {
        if (maps.isEmpty()) {
            return;
        }
        val comDisposable = maps[tag]

        if (comDisposable != null) {
            if (!comDisposable.isDisposed) {
                comDisposable.dispose()
            }
        }
        maps.remove(tag);
        LogUtils.d("cancel = ${maps.size} ,${tag.javaClass.name},${maps.keys}")

    }

    override fun cancelAll() {
        if (maps.isNotEmpty()) {
            maps.forEach {
                cancel(it.key)
            }
        }
    }
}