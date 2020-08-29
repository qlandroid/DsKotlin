package com.feiling.dasong

import androidx.multidex.MultiDexApplication
import com.feiling.dasong.db.service.DbServiceBuild
import com.feiling.dasong.http.BaseApi
import com.feiling.dasong.http.DsNetConfig
import com.feiling.dasong.model.base.BasePage
import com.feiling.dasong.model.base.PageModel
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager
import io.reactivex.plugins.RxJavaPlugins
import org.litepal.LitePal


/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/2
 * @author ql
 */
class App : MultiDexApplication() {
    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this;
        QMUISwipeBackActivityManager.init(this)
        LitePal.initialize(this);

        //初始化默认的ip地址
        DbServiceBuild.initIp()
        //获得当前默认使用的ip地址
        var defaultModel = DbServiceBuild.ipService.getDefaultModel()
        //配置当前使用的ip地址
        BaseApi.registerConfig(DsNetConfig(defaultModel?.address))

        //初始化分页
        PageModel.initPage(BasePage.PAGE_NO_1, BasePage.SIZE)

        Logger.addLogAdapter(AndroidLogAdapter())

        RxJavaPlugins.setErrorHandler{

        }
    }


}
