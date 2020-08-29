package com.feiling.dasong.comm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.Unbinder
import com.qmuiteam.qmui.arch.QMUIActivity

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/3/25
 * @author ql
 */

abstract class CommActivity : QMUIActivity() {
    lateinit var unbinder: Unbinder;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 避免从桌面启动程序后，会重新实例化入口类的activity
        if (!this.isTaskRoot) {
            val intent = intent
            if (intent != null) {
                val action = intent.action
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN == action) {
                    finish()
                    return
                }
            }
        }

        createView(savedInstanceState)
        unbinder = ButterKnife.bind(this)
        initBar()
        initStatusBar()
        initData()
        init(savedInstanceState)
        initWidget()

    }

    open fun initWidget() {

    }

    open fun init(savedInstanceState: Bundle?) {
    }

    open fun initData() {
    }

    open fun initStatusBar() {
    }

    open fun initBar() {


    }

    abstract fun createView(savedInstanceState: Bundle?);


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (null != this.currentFocus) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            val mInputMethodManager: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager;
            return mInputMethodManager.hideSoftInputFromWindow(this.currentFocus.windowToken, 0);
        }
        return super.onTouchEvent(event);
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v!!.windowToken)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.height
            val right = left + v.width
            // 点击EditText的事件，忽略它。
            return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private fun hideKeyboard(token: IBinder?) {
        if (token != null) {
            val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    /**
     * @param savedInstanceState
     ******************************************************************/


    private var currentKJFragment: BaseFragment? = null;

    /** */

    fun myChangeFragment(resView: Int, targetFragment: BaseFragment) {
        if (targetFragment.equals(currentKJFragment)) {
            return
        }
        val transaction = supportFragmentManager
            .beginTransaction()
        //        transaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_in_right,R.anim.slide_out_left,R.anim.slide_out_right);
        if (!targetFragment.isAdded()) {
            transaction.add(
                resView, targetFragment
            )
        }
        if (targetFragment.isHidden()) {
            transaction.show(targetFragment)
            targetFragment.onChange()
        }
        if (currentKJFragment?.isVisible!!) {
            transaction.hide(currentKJFragment!!);
        }
        currentKJFragment = targetFragment
        transaction.commit()

    }


    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
    }

}
