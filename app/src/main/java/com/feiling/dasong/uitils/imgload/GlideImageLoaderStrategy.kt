package com.feiling.dasong.uitils.imgload

import android.content.Context
import com.bumptech.glide.Glide

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/8/11
 * @author ql
 */
class GlideImageLoaderStrategy : BaseImageLoaderStrategy<ImgLoadConfig> {
    override fun displayImage(context: Context?, config: ImgLoadConfig?) {
        var load = Glide.with(context!!).load(config!!.path)

        config!!.placeholderId?.let {
            load.placeholder(it)
        }
        config.errorId?.let { load.error(it) }

        config.imageRadius?.let {
            load.transform(CornerTransform(context, it))
        }
        load.into(config.imgView!!)
    }
}