package com.feiling.dasong.http

import okhttp3.Interceptor

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/13
 * @author ql
 */
interface NetConfig {
    /**
     * 通用请求地址
     *
     * @return
     */
    fun configBaseUrl(): String?

    /**
     * 拦截器
     *
     * @return
     */
    fun configInterceptors(): Array<Interceptor?>?

    /**
     * 连接超时时间
     *
     * @return
     */
    fun configConnectTimeoutMills(): Long

    /**
     * 读取超时时间
     *
     * @return
     */
    fun configReadTimeoutMills(): Long


    /**
     * 是否调试模式
     *
     * @return
     */
    fun configLogEnable(): Boolean
}