package com.feiling.dasong.ui.function.quality

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/27
 * @author ql
 */
interface IProcessQualityPresenter {

    //开工
    fun onProcessCheckStart()


    /**
     * 完工
     */
    fun onProcessCheckEnd()

    /**
     * 暂停
     */
    fun onProcessCheckPause()

}