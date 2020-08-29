package com.feiling.dasong.ui.comm

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseTopBarFragment
import com.feiling.dasong.uitils.LogUtils
import com.feiling.dasong.uitils.imgload.ImgLoadConfig
import com.qmuiteam.qmui.kotlin.onClick
import kotlinx.android.synthetic.main.fragment_img_page.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/8/11
 * @author ql
 */
class ImgPageFragment : BaseTopBarFragment() {
    var pageAdapter: PageAdapter = PageAdapter()

    var list: MutableList<String>? = null

    /**
     * 首次显示位置
     */
    var position: Int = 0;

    /**
     * 标题
     */
    var title: String? = null
    val mOnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            LogUtils.d("当前页数${position}")
            setTitle(title!!, position)
        }
    }

    companion object {
        const val KEY_URL_LIST = "KEY-URL-LIST"
        const val KEY_INDEX = "KEY-INDEX"
        const val KEY_TITLE = "KEY-TITLE"
        fun instance(
            list: MutableList<String>,
            position: Int = 0,
            title: String = "图片"
        ): ImgPageFragment {
            var imgPageFragment = ImgPageFragment()
            var bundle = Bundle()
            bundle.putInt(KEY_INDEX, position)
            bundle.putString(KEY_TITLE, title)
            imgPageFragment.arguments = bundle
            imgPageFragment.list = list;
            return imgPageFragment
        }
    }

    override fun createContentView(): View {
        return layoutInflater.inflate(R.layout.fragment_img_page, null)
    }


    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        mTopBar?.addLeftBackImageButton()?.onClick { popBackStack() }
        pageAdapter.setNewData(list)
        arguments?.apply {
            position = this.getInt(KEY_INDEX)
            title = this.getString(KEY_TITLE)
            setTitle(title!!, position)
        }
        imgPageVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        imgPageVp.adapter = pageAdapter;
        imgPageVp.postDelayed({
            imgPageVp.setCurrentItem(position, false)
        },100)

        imgPageVp.registerOnPageChangeCallback(mOnPageChangeCallback)
    }


    override fun onDestroyView() {
        imgPageVp?.unregisterOnPageChangeCallback(mOnPageChangeCallback)
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun setTitle(title: String, position: Int) {
        val total = list.orEmpty().size
        val index = position + 1;
        val showTitle = "$title ($index/$total)"
        mTopBar?.setTitle(showTitle)
    }

    class PageAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_photo) {
        override fun convert(helper: BaseViewHolder, item: String) {
            ImgLoadConfig.Builder(helper.itemView.context)
                .load(item)
                .into(helper.itemView as ImageView)
        }

    }
}