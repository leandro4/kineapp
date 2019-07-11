package com.gon.kineapp.ui.fragments

import android.content.Intent
import android.os.Bundle

import com.gon.kineapp.ui.activities.SplashActivity
import kotlinx.android.synthetic.main.fragment_patient_list.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import android.view.*
import android.widget.Toast
import com.gon.kineapp.R
import com.gon.kineapp.model.Patient
import com.gon.kineapp.ui.activities.PatientDetailActivity
import com.gon.kineapp.utils.Constants

class PatientListFragment : BaseMvpFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.gon.kineapp.R.layout.fragment_patient_list, container, false)
        setHasOptionsMenu(true)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {

        patientSimulator.setOnClickListener {
            goToPatientDetail(Patient("14", "Masculino", "187231", "Juanitou", "Moralez", "123123", "juani@gmail.com"))
        }

        fabAddPatient.setOnClickListener {
            Toast.makeText(context, "agregar paciente", Toast.LENGTH_SHORT).show()
        }

        tvLogout.setOnClickListener {
            showProgressView()
            GoogleSignIn.getClient(activity!!, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).revokeAccess()
            GoogleSignIn.getClient(activity!!, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut()
                .addOnCompleteListener(activity!!) {
                    activity?.startActivity(Intent(context, SplashActivity::class.java))
                    activity?.finish()
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.videos -> {
                Toast.makeText(context, "crear video", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.profile -> {
                Toast.makeText(context, "time line", Toast.LENGTH_SHORT).show()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun goToPatientDetail(patient: Patient) {
        val intent = Intent(activity, PatientDetailActivity::class.java)
        intent.putExtra(Constants.PATIENT_EXTRA, patient)
        activity?.startActivity(intent)
    }

    override fun startPresenter() {

    }

    override fun onErrorCode(message: String) {

    }
}
