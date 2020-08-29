package com.feiling.dasong.ui

import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import com.feiling.dasong.DsAPI
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseFragment
import com.feiling.dasong.comm.getDsMsg
import com.feiling.dasong.comm.toBody
import com.feiling.dasong.exception.DsException
import com.feiling.dasong.http.RxApiManager
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.model.LoginModel
import com.feiling.dasong.model.UserModel
import com.feiling.dasong.role.RoleUtils
import com.feiling.dasong.uitils.LoginUtils
import com.qmuiteam.qmui.kotlin.onClick
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/8
 * @author ql
 */
class LoginFragment : BaseFragment() {


    override fun createView(): View {
        QMUIStatusBarHelper.setStatusBarLightMode(activity)
        return LinearLayout.inflate(context, R.layout.fragment_login, null)
    }

    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        loginBtn.setOnClickListener {
            actionToLogin()
        }
//        code: '0001',//账号
//        password: 'A685C29B84DC76',//密码
        loginAccountEt.setText("0001")
        loginPwEt.setText("123456")
        loginBtn.isEnabled = true;

        loginIPBtn.onClick {
            startActivity(Intent(context, IpChangeActivity::class.java))
        }
        loginAccountEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkBtnEnable()
            }
        })
        loginPwEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkBtnEnable()
            }

        })
        loginPwEt.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {
                    v?.clearFocus()
                    actionToLogin()
                    return true;
                }
                return false
            }

        })
    }

    private fun actionToLogin() {
        val disposable = Observable.create<LoginModel> {
            if (TextUtils.isEmpty(loginAccountEt.text)) {
                loginHintTv.text = "请输入账号"
                return@create
            }
            if (TextUtils.isEmpty(loginPwEt.text)) {
                loginHintTv.text = "请输入密码"
                return@create
            }
            val loginModel = LoginModel(loginAccountEt.text.toString(), loginPwEt.text.toString())
            loginBtn.isClickable = false;

            it.onNext(loginModel)
            it.onComplete()

        }
            .observeOn(Schedulers.io())
            .flatMap {
                if (!QMUIDisplayHelper.hasInternet(context)) {
                    throw DsException("当前没有网络")
                }
                activity?.runOnUiThread {
                    mDialogTipHelper.showLoading("登录中")
                }
                ServiceBuild.userService.login(it.toBody())
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    if (it.isFailed) {
                        throw DsException(it.code.toString(), it.message.orEmpty());
                    }
                    mDialogTipHelper.dismissLoading()
                    loginBtn.isClickable = true;
                    val userModel = it.getData(UserModel::class.java)
                    LoginUtils.setLogin(context, true)
                    LoginUtils.setUserModel(context, userModel)
                },
                {
                    it.printStackTrace()
                    loginBtn.post {
                        loginBtn.isClickable = true;
                        mDialogTipHelper.dismissLoading()
                        mDialogTipHelper.displayMsgDialog(msg = it.getDsMsg())
                    }

                }, {
                    DsAPI.role = RoleUtils.getRoleByType(LoginUtils.getUserModel(context))
                    val intent = Intent(context, HomeActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                })

        RxApiManager.instance.add(this@LoginFragment, disposable)
    }


    override fun onDestroy() {
        super.onDestroy()
    }

    private fun checkBtnEnable() {
        loginBtn.isEnabled = !(loginPwEt.text.isEmpty() || loginAccountEt.text.isEmpty())
    }
}
