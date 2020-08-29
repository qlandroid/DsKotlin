package com.feiling.dasong.http

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/13
 * @author ql
 */
/**
 * 基类  API
 */
class BaseApi private constructor() {


    /**
     * 获取Retrofit
     *
     * @return 获取Retrofit
     */
    var retrofit: Retrofit? = null
        get() {
            if (field == null) {
                val builder = Retrofit.Builder()
                    .baseUrl(mConfig.configBaseUrl()) //配置BaseUrl
                    .client(httpClient) // 设置client
                    .addConverterFactory(GsonConverterFactory.create()) //gson转换器
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                field = builder.build()
            }
            return field!!
        }

    /**
     * 获取httpclient
     *
     * @return OkHttpClient
     */
    private var httpClient: OkHttpClient? = null
        get() {
            if (field == null) {
                val builder = OkHttpClient.Builder()
                // 连接超时时间
                builder.connectTimeout(
                    if (mConfig.configConnectTimeoutMills() != 0L) mConfig.configConnectTimeoutMills() else DEFAULT_CONNECT_TIMEOUT_MILLS,
                    TimeUnit.MILLISECONDS
                )
                // 读取超时时间
                if (mConfig.configReadTimeoutMills() != 0L) mConfig.configReadTimeoutMills() else DEFAULT_READ_TIMEOUT_MILLS?.let {
                    builder.readTimeout(
                        it,
                        TimeUnit.MILLISECONDS
                    )
                }
                // 拦截器
                val interceptors: Array<Interceptor?>? =
                    mConfig.configInterceptors()
                if (interceptors != null && interceptors.isNotEmpty()) {
                    for (interceptor in interceptors) {
                        builder.addInterceptor(interceptor)
                    }
                }
                if (mConfig.configLogEnable()) { //配置打印
                    val logInterceptor = HttpLoggingInterceptor()
                    logInterceptor.level = HttpLoggingInterceptor.Level.BODY
                    builder.addInterceptor(logInterceptor)
                }
                field = builder.build()
            }
            return field
        }

    companion object {
        /**
         * 网络配置项
         */
        lateinit var mConfig: NetConfig
        /**
         * 默认连接超时时间
         */
        private const val DEFAULT_CONNECT_TIMEOUT_MILLS = 40 * 1000L
        /**
         * 默认读取超时时间
         */
        private const val DEFAULT_READ_TIMEOUT_MILLS = 40 * 1000L
        /**
         * 实例
         */
        private var instance: BaseApi? = null

        /**
         * 创建 Retrofit
         *
         * @return Retrofit
         */
        @Synchronized
        fun createRetrofit(): Retrofit {
            return getInstance()!!.retrofit!!
        }

        /**
         * 单例 获取
         *
         * @return BaseApi
         */
        @Synchronized
        fun getInstance(): BaseApi? {
            if (instance == null) instance = BaseApi()
            return instance
        }

        /**
         * 创建class
         *
         * @param service 服务class
         * @param <C>     类泛型
         * @return 泛型
        </C> */
        operator fun <C> get(service: Class<C>?): C {
            return getInstance()!!.retrofit!!.create(service)
        }

        /**
         * 注册配置
         *
         * @param config
         */
        fun registerConfig(config: NetConfig?) {
            if (config != null) {
                mConfig = config
            }
            //赋值为空
            if (instance != null) {
                instance?.httpClient = null;
                instance?.retrofit = null;
            }
            ServiceBuild.reset();
        }
    }

}

