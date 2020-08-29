package com.feiling.dasong.uitils.imgload

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/8/11
 * @author ql
 */
object ImgLoad {

    var loader: BaseImageLoaderStrategy<in ImgLoadConfig>? = null
        get() {
            if (field == null) {
                return GlideImageLoaderStrategy() as BaseImageLoaderStrategy<ImgLoadConfig>
            }
            return field
        }

}