package com.feiling.dasong.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView
import androidx.dynamicanimation.animation.SpringAnimation


/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/7/12
 * @author ql
 */
class ReboundScrollView : NestedScrollView {
    private var startDragY: Float = 0f
    private val springAnim: SpringAnimation = SpringAnimation(this, SpringAnimation.TRANSLATION_Y, 0.0f)

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        //刚度 默认1200 值越大回弹的速度越快
        springAnim.spring.stiffness = 300.0f
        //阻尼 默认0.5 值越小，回弹之后来回的次数越多
        springAnim.spring.dampingRatio = 0.60f
    }



    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_MOVE ->
                if (scrollY <= 0) {
                    //顶部下拉
                    if (startDragY == 0f) {
                        startDragY = ev.rawY
                    }
                    if (ev.rawY - startDragY >= 0) {
                        translationY = (ev.rawY - startDragY) / 3
                        return true
                    } else {
                        startDragY = 0f
                        springAnim.cancel()
                        translationY = 0f
                    }
                } else if (scrollY + height >= getChildAt(0).measuredHeight) {
                    //底部上拉
                    if (startDragY == 0f) {
                        startDragY = ev.rawY
                    }
                    if (ev.rawY - startDragY <= 0) {
                        translationY = (ev.rawY - startDragY) / 3
                        return true
                    } else {
                        startDragY = 0f
                        springAnim.cancel()
                        translationY = 0f
                    }
                }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (translationY != 0f) {
                    springAnim.start()
                }
                startDragY = 0f
            }
        }
        return super.onTouchEvent(ev)
    }

}