package com.feiling.dasong.ui.adapter

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/7/11
 * @author ql
 */
abstract class AbsLabelInputAdapter<T, VH : BaseViewHolder>(var layout: Int) :
    BaseQuickAdapter<T, VH>(layout) {
    private var bindTextChange: HashMap<EditText, BindTextChange> = HashMap()


    open fun onTextChange(
        helper: VH,
        item: T,
        editView: EditText?,
        text: CharSequence?
    ) {

    }

    public fun addBindTextChange(etId: Int,  item: T, helper: VH) {
        var view = helper.getView<EditText>(etId)
        addBindTextChange(view,  item, helper)
    }

    public fun addBindTextChange(editView: EditText, item: T, helper: VH) {
        var bindChange = bindTextChange[editView]
        if (bindChange == null) {
            bindChange = BindTextChange()
            bindTextChange[editView] = bindChange;
            editView.addTextChangedListener(bindChange)
        }
        var value = bindTextChange[editView]
        value?.item = item;
        value?.helper = helper;
        value?.editText = editView;
        value?.block =
            { vh: VH?, t: T?, et: EditText?, charSequence: CharSequence? ->
                onTextChange(vh!!, t!!, et, charSequence)
            }
    }


    inner class BindTextChange() : TextWatcher {
        var item: T? = null;
        var helper: VH? = null;
        var editText: EditText? = null;

        var position: Int? = null;
        var block: ((VH?, T?, EditText?, s: CharSequence?) -> Unit)? = null;
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            block?.let {
                it(helper, item, editText, s);
            }
        }

    }


}