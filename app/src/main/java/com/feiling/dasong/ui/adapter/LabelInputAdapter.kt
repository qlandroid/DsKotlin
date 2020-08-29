package com.feiling.dasong.ui.adapter

import android.widget.EditText
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.feiling.dasong.R

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/7/11
 * @author ql
 */
class LabelInputAdapter :
    AbsLabelInputAdapter<LabelInputModel, BaseViewHolder>(R.layout.item_input_value) {
    override fun convert(helper: BaseViewHolder, item: LabelInputModel) {
        addBindTextChange(R.id.input_value_et, item, helper);
        helper.setText(R.id.input_value_label_tv, item.label)
        var view = helper.getView<EditText>(R.id.input_value_et)
        item.value?.let {
            view.setText(it)
        }

        helper.setGone(R.id.input_value_end_tv, !item.showEndText)
            .setText(R.id.input_value_end_tv, item.endText)

    }

    override fun onTextChange(
        helper: BaseViewHolder,
        item: LabelInputModel,
        editView: EditText?,
        text: CharSequence?
    ) {
        super.onTextChange(helper, item, editView, text)
        item.value = text;
    }


}