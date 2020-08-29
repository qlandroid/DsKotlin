package com.feiling.dasong.http

import com.feiling.dasong.App
import com.feiling.dasong.model.LoginModel
import com.feiling.dasong.uitils.LoginUtils
import com.ql.comm.utils.JsonUtils
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/13
 * @author ql
 */
class DsNetConfig(var address: String?) : NetConfig {
    override fun configBaseUrl(): String? {
//        return "http://192.168.200.209:8096";
        return address
    }

    override fun configInterceptors(): Array<Interceptor?>? {
        return arrayOf<Interceptor?>(
            HttpLoggingInterceptor() {

            },
            object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request()
                    val build = request.newBuilder()
                        .method(request.method(), request.body())

                    if (LoginUtils.isLogin(App.instance)) {
                        val userModel = LoginUtils.getUserModel(App.instance)
                        userModel?.let {
                            build.addHeader("userId", userModel?.code)
                        }

                    }
                    val newRequest = build.build()
                    return chain.proceed(newRequest);
                }

            })
    }

    override fun configConnectTimeoutMills(): Long {
        return 45 * 1000
    }

    override fun configReadTimeoutMills(): Long {
        return 45 * 1000
    }

    override fun configLogEnable(): Boolean {
        return true
    }
}