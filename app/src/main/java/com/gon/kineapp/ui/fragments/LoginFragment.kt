package com.gon.kineapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.gon.kineapp.R
import com.gon.kineapp.model.Question
import com.gon.kineapp.model.User
import com.gon.kineapp.mvp.presenters.LoginPresenter
import com.gon.kineapp.mvp.views.LoginView
import com.gon.kineapp.ui.activities.DashboardActivity
import com.gon.kineapp.ui.fragments.dialogs.RolSelectionFragment
import com.gon.kineapp.ui.fragments.dialogs.UnlockerQuestionFragment
import com.gon.kineapp.utils.LockerAppCallback
import com.gon.kineapp.utils.MyUser
import com.gon.kineapp.utils.QuestionsList
import com.gonanimationlib.animations.Animate
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment: BaseMvpFragment(), LoginView, AdapterView.OnItemSelectedListener {

    var presenter = LoginPresenter()

    private var googleSignInClient: GoogleSignInClient? = null
    private val RC_SIGN_IN = 1000

    private var isMedic = false
    private var questionSelectedId = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startPresenter()
        initUI()
    }

    private fun showRolDialog() {
        RolSelectionFragment().setListener(object : RolSelectionFragment.ResponseListener {
            override fun onProfessionalSelected() {
                isMedic = true
            }

            override fun onPatientSelected() {
                isMedic = false
                etLicense.visibility = View.GONE
                tvLicense.visibility = View.GONE
            }
        }).show(fragmentManager, "selector")
    }

    private fun initUI() {
        fabLogin.setOnClickListener {
            if (validateFields()) {
                presenter.registerUser(getGoogleAccount()?.idToken!!,
                    etName.text.toString(),
                    etLastName.text.toString(),
                    if (etLicense.text.toString().isEmpty()) null else etLicense.text.toString(),
                    etNumber.text.toString(), "1989-06-18",//etBirthday.text.toString(),
                    tvEmail.text.toString(), questionSelectedId, etAnswer.text.toString().toLowerCase())
            }
        }

        btnGoogleSignIn.setOnClickListener {
            if (checkPlayServices()) {
                val signInIntent = googleSignInClient?.signInIntent
                activity?.startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_client_id))
            .requestEmail()
            .requestProfile()
            .build()

        googleSignInClient = GoogleSignIn.getClient(activity!!, gso)
    }

    private fun validateFields(): Boolean {
        var mandatoryField = true
        if (etName.text.toString().isEmpty()) {
            etName.error = getString(R.string.mandatory_field)
            mandatoryField = false
        }
        if (etLastName.text.toString().isEmpty()) {
            etLastName.error = getString(R.string.mandatory_field)
            mandatoryField = false
        }
        if (etLicense.text.toString().isEmpty() && isMedic) {
            etLicense.error = getString(R.string.mandatory_field)
            mandatoryField = false
        }
        if (etAnswer.text.toString().isEmpty()) {
            etAnswer.error = getString(R.string.mandatory_field)
            mandatoryField = false
        }
        if (etBirthday.text.toString().isEmpty()) {
            etBirthday.error = getString(R.string.mandatory_field)
            mandatoryField = false
        }
        if (etNumber.text.toString().isEmpty()) {
            etNumber.error = getString(R.string.mandatory_field)
            mandatoryField = false
        }
        return mandatoryField
    }

    private fun checkPlayServices(): Boolean {
        val googleAPI = GoogleApiAvailability.getInstance()
        val result = googleAPI.isGooglePlayServicesAvailable(context)

        if (result != ConnectionResult.SUCCESS) {
            val PLAY_SERVICES_RESOLUTION_REQUEST = 1000
            //Google Play Services app is not available or version is not up to date. Error the
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(
                    activity, result,
                    PLAY_SERVICES_RESOLUTION_REQUEST
                ).show()
            }

            if (result == ConnectionResult.SERVICE_UPDATING)
                Toast.makeText(context, getString(R.string.generic_wait_message), Toast.LENGTH_SHORT).show()

            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == RC_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { presenter.userExists(it) }
            } catch (e: ApiException) {
                Toast.makeText(context, getString(R.string.generic_error_message), Toast.LENGTH_SHORT).show()
            }

        } else if (resultCode == AppCompatActivity.RESULT_CANCELED) {
            Toast.makeText(context, getString(R.string.generic_error_message), Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToHome() {
        activity?.startActivity(Intent(activity, DashboardActivity::class.java))
        activity?.finish()
    }

    override fun startPresenter() {
        presenter.attachMvpView(this)
    }

    override fun onDestroy() {
        presenter.detachMvpView()
        super.onDestroy()
    }

    override fun onUserRetrieved(myUser: User, questions: List<Question>) {
        context?.let {
            MyUser.set(it, myUser)
            QuestionsList.set(it, questions)
            UnlockerQuestionFragment()
                .setListener(object : UnlockerQuestionFragment.ResponseListener {
                    override fun onSuccessResponse() {
                        goToHome()
                    }
                })
                .show(fragmentManager, "question")
        }
    }

    override fun onUserCreated(myUser: User) {
        context?.let {
            LockerAppCallback.TimerSessionClient.registerPausedApp(it, System.currentTimeMillis())
            MyUser.set(it, myUser)
            goToHome()
        }
    }

    override fun onUserDoesntExists(questions: List<Question>) {
        QuestionsList.set(context!!, questions)

        showRolDialog()

        initSpinnerQuestions()

        etName.setText(getGoogleAccount()?.givenName)
        etLastName.setText(getGoogleAccount()?.familyName)
        tvEmail.text = getGoogleAccount()?.email

        Animate.ALPHA(0f).duration(Animate.DURATION_SHORT).onEnd { llGoogleSignIn.visibility = View.GONE }.startAnimation(llGoogleSignIn)
        Animate.ALPHA(1f).duration(Animate.DURATION_SHORT).onStart { llForm.visibility = View.VISIBLE }.startAnimation(llForm)
    }

    private fun initSpinnerQuestions() {
        val questions = QuestionsList.get(context!!)

        val spinnerArray = ArrayList<String>()
        questions.forEach { spinnerArray.add(it.description) }

        val adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item, spinnerArray)

        //val adapter = ArrayAdapter.createFromResource(context!!, list.toArray(array), android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(R.layout.spinner_text_arrow_white)
        spQuestions.adapter = adapter
        spQuestions.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        questionSelectedId = QuestionsList.get(context!!)[position].id
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}