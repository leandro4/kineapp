package com.gon.kineapp.ui.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.gon.kineapp.R
import com.gon.kineapp.utils.LockerAppCallback
import com.gonanimationlib.animations.Animate
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_secret_question.*
import java.util.concurrent.TimeUnit

class UnlockerQuestionFragment: BaseDialogFragment() {

    private var listener: ResponseListener? = null

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

    private fun initUI() {
        fabAnswer.setOnClickListener {
            showLoading()
            checkAnswer()
        }
    }

    fun setListener(listener: ResponseListener): UnlockerQuestionFragment {
        this.listener = listener
        return this
    }

    private fun checkAnswer() {
        Observable.just(etAnswer.text.toString()).delay(2, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it == "japon") {
                    LockerAppCallback.TimerSessionClient.setUnlockedApp(context!!, false)
                    listener?.onSuccessResponse()
                    dismiss()
                } else {
                    Toast.makeText(activity, "Respuesta incorrecta, intente nuevamente", Toast.LENGTH_SHORT).show()
                    hideLoading()
                    Animate.ZUMB().duration(Animate.DURATION_V_LARGE).startAnimation(etAnswer)
                }
            }
    }

    private fun showLoading() {
        Animate.ALPHA(1f).duration(Animate.DURATION_MEDIUM).onStart { viewLoading.visibility = View.VISIBLE }.startAnimation(viewLoading)
    }

    private fun hideLoading() {
        Animate.ALPHA(0f).duration(Animate.DURATION_MEDIUM).onEnd { viewLoading.visibility = View.GONE }.startAnimation(viewLoading)
    }
}