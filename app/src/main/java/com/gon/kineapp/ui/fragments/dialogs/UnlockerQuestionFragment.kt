package com.gon.kineapp.ui.fragments.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.gon.kineapp.R
import com.gon.kineapp.mvp.presenters.QuestionPresenter
import com.gon.kineapp.mvp.views.QuestionView
import com.gon.kineapp.utils.LockerAppCallback
import com.gon.kineapp.utils.Utils
import com.gonanimationlib.animations.Animate
import com.google.android.gms.auth.api.signin.GoogleSignIn
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_secret_question.*
import java.util.concurrent.TimeUnit

class UnlockerQuestionFragment: BaseDialogFragment(), QuestionView {

    private var listener: ResponseListener? = null
    private var presenter = QuestionPresenter()

    interface ResponseListener {
        fun onSuccessResponse()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LockerAppCallback.TimerSessionClient.setUnlockedApp(context!!, true)
        isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_secret_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun onStart() {
        super.onStart()
        presenter.attachMvpView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachMvpView()
    }

    private fun initUI() {
        fabAnswer.setOnClickListener {
            GoogleSignIn.getLastSignedInAccount(context)?.idToken?.let {
                presenter.checkAnswer(it, 1, etAnswer.text.toString())
                Utils.hideKeyboardFrom(etAnswer)
            }
        }
    }

    fun setListener(listener: ResponseListener): UnlockerQuestionFragment {
        this.listener = listener
        return this
    }

    override fun onCheckedAnswer() {
        LockerAppCallback.TimerSessionClient.setUnlockedApp(context!!, false)
        listener?.onSuccessResponse()
        dismiss()
    }

    override fun onAnswerInvalid() {
        Animate.ZUMB().duration(Animate.DURATION_V_LARGE).startAnimation(etAnswer)
    }

    override fun onAttemptsLimit() {
        Toast.makeText(context, "Comela. Llama al centro de atención y recuperala", Toast.LENGTH_SHORT).show()
    }

    override fun onError(var1: Throwable) {
        Animate.ZUMB().duration(Animate.DURATION_V_LARGE).startAnimation(etAnswer)
    }

    override fun onNoInternetConnection() {
        Animate.ZUMB().duration(Animate.DURATION_V_LARGE).startAnimation(etAnswer)
    }

    override fun onErrorCode(message: String) {
        Animate.ZUMB().duration(Animate.DURATION_V_LARGE).startAnimation(etAnswer)
    }

    override fun showProgressView() {
        Animate.ALPHA(1f).duration(Animate.DURATION_MEDIUM).onStart { viewLoading.visibility = View.VISIBLE }.startAnimation(viewLoading)
    }

    override fun hideProgressView() {
        Animate.ALPHA(0f).duration(Animate.DURATION_MEDIUM).onEnd { viewLoading.visibility = View.GONE }.startAnimation(viewLoading)
    }

    override fun getMvpContext(): Context? {
        return context
    }
}