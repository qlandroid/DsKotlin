package com.feiling.dasong.uitils.imgload

import android.content.Context


/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/8/11
 * @author ql
 */
interface BaseImageLoaderStrategy<in ImgLoadConfig> {
    fun displayImage(context: Context?, config: ImgLoadConfig?)
}