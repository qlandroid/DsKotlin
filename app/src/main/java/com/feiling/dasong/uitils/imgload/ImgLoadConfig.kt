package com.feiling.dasong.uitils.imgload

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/8/11
 * @author ql
 */
class ImgLoadConfig {
    var path: String? = null;
    var imgView: ImageView? = null;

    @DrawableRes
    var placeholderId: Int? = null;

    @DrawableRes
    var errorId: Int? = null;
    var imageRadius: Float? = null;

    constructor(builder: Builder) {
        path = builder.path
        imgView = builder.imgView
        imageRadius = builder.imageRadius
        errorId = builder.errorId
        placeholderId = builder.placeholderId
    }

    fun loadImg() {
        ImgLoad.loader?.displayImage(imgView!!.context, this)
    }


    class Builder(var context: Context) {
        var path: String? = null;
        var imgView: ImageView? = null;

        @DrawableRes
        var placeholderId: Int? = null;

        @DrawableRes
        var errorId: Int? = null;
        var imageRadius: Float? = null;

        fun load(path: String?): Builder {
            this.path = path;
            return this;
        }

        fun into(iv: ImageView?) {
            this.imgView = iv;
            ImgLoadConfig(this).loadImg()

        }

        fun placeholder(@DrawableRes placeholder: Int): Builder {
            this.placeholderId = placeholder
            return this;
        }

        fun error(@DrawableRes id: Int): Builder {
            this.errorId = id;
            return this;
        }

        fun radius(radius: Float): Builder {
            this.imageRadius = radius
            return this;
        }

    }
}