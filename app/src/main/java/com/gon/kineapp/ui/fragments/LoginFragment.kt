package com.gon.kineapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.gon.kineapp.R
import com.gon.kineapp.mvp.presenters.LoginPresenter
import com.gon.kineapp.mvp.views.LoginView
import com.gon.kineapp.ui.activities.PatientListActivity
import com.gon.kineapp.ui.fragments.dialogs.RolSelectionFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment: BaseMvpFragment(), LoginView {

    var presenter = LoginPresenter()

    private var googleSignInClient: GoogleSignInClient? = null
    private val RC_SIGN_IN = 1000

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startPresenter()
        initUI()
        showRolDialog()
    }

    private fun showRolDialog() {
        RolSelectionFragment().setListener(object : RolSelectionFragment.ResponseListener {
            override fun onProfessionalSelected() {

            }

            override fun onPatientSelected() {
                /*etLicense.visibility = View.GONE
                tvLicense.visibility = View.GONE
                etAddress.visibility = View.GONE
                tvAddress.visibility = View.GONE*/
            }
        }).show(fragmentManager, "selector")
    }

    private fun initUI() {
        fabLogin.setOnClickListener {
            if (checkPlayServices() && validateFields()) {
                val signInIntent = googleSignInClient?.signInIntent
                activity?.startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_client_id))
            .requestEmail()
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
        if (etLicense.text.toString().isEmpty()) {
            etLicense.error = getString(R.string.mandatory_field)
            mandatoryField = false
        }
        if (etAddress.text.toString().isEmpty()) {
            etAddress.error = getString(R.string.mandatory_field)
            mandatoryField = false
        }
        if (etPhone.text.toString().isEmpty()) {
            etPhone.error = getString(R.string.mandatory_field)
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

                Toast.makeText(context, account?.displayName, Toast.LENGTH_SHORT).show()

                //presenter.requestLogin()
                onLoginSuccess()

            } catch (e: ApiException) {
                Toast.makeText(context, getString(R.string.generic_error_message), Toast.LENGTH_SHORT).show()
            }

        } else if (resultCode == AppCompatActivity.RESULT_CANCELED) {
            Toast.makeText(context, getString(R.string.generic_error_message), Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToHome() {
        activity?.startActivity(Intent(activity, PatientListActivity::class.java))
        activity?.finish()
    }

    override fun startPresenter() {
        presenter.attachMvpView(this)
    }

    override fun onDestroy() {
        presenter.detachMvpView()
        super.onDestroy()
    }

    override fun onLoginSuccess() {
        goToHome()
    }

    override fun onLoginFailure() {
        showErrorMessage(getString(R.string.generic_error_message))
    }
}