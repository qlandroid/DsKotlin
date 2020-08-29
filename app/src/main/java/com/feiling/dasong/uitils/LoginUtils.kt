package com.feiling.dasong.uitils

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.feiling.dasong.model.UserModel
import com.ql.comm.utils.JsonUtils

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/8
 * @author ql
 */
object LoginUtils {

    fun isLogin(context: Context?): Boolean {
        val sharedPreferences = context?.getSharedPreferences("login", Context.MODE_PRIVATE)
        return sharedPreferences?.getBoolean("login", false)!!;
    }

    fun setLogin(context: Context?, isLogin: Boolean) {
        val sharedPreferences = context?.getSharedPreferences("login", Context.MODE_PRIVATE)
        sharedPreferences?.edit()?.putBoolean("login", isLogin)?.apply()
    }

    fun setUserModel(context: Context?, userModel: UserModel) {
        val sharedPreferences = context?.getSharedPreferences("login", Context.MODE_PRIVATE)
        sharedPreferences?.edit()?.putString("userDetails", JsonUtils.toJson(userModel))
            ?.apply()
    }

    fun getUserModel(context: Context?): UserModel? {
        val sharedPreferences = context?.getSharedPreferences("login", Context.MODE_PRIVATE)
        val userStr = sharedPreferences?.getString("userDetails", "")
        if (TextUtils.isEmpty(userStr)) {
            return null;
        }
        return JsonUtils.fromJson(userStr, UserModel::class.java)
    }

}