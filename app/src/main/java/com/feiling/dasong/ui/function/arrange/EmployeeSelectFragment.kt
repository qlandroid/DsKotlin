package com.feiling.dasong.ui.function.arrange

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.feiling.dasong.C
import com.feiling.dasong.R
import com.feiling.dasong.comm.CommSelectFragment
import com.feiling.dasong.exception.DsException
import com.feiling.dasong.http.RxApiManager
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.model.EmployeeModel
import com.feiling.dasong.model.DevModel
import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.ui.model.NavDetailsModel
import com.google.gson.JsonElement
import com.ql.comm.utils.JsonUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_comm_select.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/5
 * @author ql
 */
class EmployeeSelectFragment : CommSelectFragment() {
    val data: MutableList<DevModel> = mutableListOf<DevModel>()

    var mAllList: MutableList<EmployeeModel> = mutableListOf();

    var mShowList: MutableList<EmployeeModel> = mutableListOf();

    companion object {
        fun getEmployeeModel(data: Intent): EmployeeModel {
            return C.sGson.fromJson(data.getStringExtra("data"), EmployeeModel::class.java);
        }
    }


    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        topbar.setTitle("操作人员选择")
        comm_slt_pull_layout.setOnRefreshListener {
            loadData()
        }

        comm_search_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mShowList.clear()
                mCommAdapter.data.clear()
                val subscribe = Observable.fromIterable(mAllList)
                    .filter {
                        it.code!!.contains(s.toString()) || it.name.contains(s.toString())
                    }
                    .subscribe({
                        mShowList.add(it)
                        mCommAdapter.addData(
                            NavDetailsModel(
                                it.name,
                                mutableListOf(
                                    LabelTextModel(getString(R.string.element_code), it.code!!),
                                    LabelTextModel(getString(R.string.element_job), it.job!!)
                                )
                            )
                        )
                    }, {

                    }, {
                        mCommAdapter.notifyDataSetChanged()
                    })
                RxApiManager.instance.add(this@EmployeeSelectFragment, subscribe)
            }


        })
        comm_input_group.visibility = View.VISIBLE
        mCommAdapter.setOnItemClickListener { baseQuickAdapter: BaseQuickAdapter<Any?, BaseViewHolder>, view: View, i: Int ->
            val intent = Intent()
            intent.putExtra("data", C.sGson.toJson(mShowList[i]))
            setFragmentResult(RESULT_OK, intent)
            popBackStack()
        }
        emptyView.showEmptyView(loading = true)
        loadData()
    }

    private fun loadData() {
        val subscribe = ServiceBuild.employeeService.getEmployees()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap() { t ->
                if (t.isFailed) {
                    throw DsException(t.message!!)
                }
                if (comm_slt_pull_layout.isRefreshing) {
                    mCommAdapter.data.clear()
                    mAllList.clear()
                }
                Observable.fromIterable<JsonElement>(t.data!!.asJsonArray)
            }
            .map {
                JsonUtils.fromJson(it, EmployeeModel::class.java)
            }
            .subscribe({ employeeModel ->
                mAllList.add(employeeModel)
                mShowList.add(employeeModel)
                mCommAdapter.addData(
                    NavDetailsModel(
                        employeeModel.name,
                        mutableListOf(
                            LabelTextModel(getString(R.string.element_code), employeeModel.code!!),
                            LabelTextModel(getString(R.string.element_job), employeeModel.job!!)
                        )
                    )
                )
            }, {
                it.printStackTrace()
                emptyView.showFailed(msg = it.message) { emptyView ->
                    emptyView.showEmptyView();
                    loadData()
                }
            }, {
                if (emptyView.isShowing) {
                    emptyView.hideEmpty()
                }
                comm_slt_pull_layout.visibility = View.VISIBLE

                if (comm_slt_pull_layout.isRefreshing) {
                    comm_slt_pull_layout.isRefreshing = false;
                }
                mCommAdapter.notifyDataSetChanged()

                mCommAdapter.loadMoreModule?.loadMoreEnd()
            })

        RxApiManager.instance.add(this, subscribe)


    }


}