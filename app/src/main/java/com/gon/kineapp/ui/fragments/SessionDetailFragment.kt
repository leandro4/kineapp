package com.gon.kineapp.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import com.gon.kineapp.R
import com.gon.kineapp.model.Patient
import com.gon.kineapp.model.Session
import com.gon.kineapp.ui.activities.BaseActivity
import kotlinx.android.synthetic.main.fragment_patient_detail.*

class SessionDetailFragment : BaseMvpFragment() {

    private lateinit var session: Session

    companion object {
        fun newInstance(session: Session): SessionDetailFragment {
            val frag = SessionDetailFragment()
            frag.session = session
            return frag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.gon.kineapp.R.layout.fragment_session_detail, container, false)
        setHasOptionsMenu(true)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        (activity as BaseActivity).setToolbarTitle(session.patientName)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_session, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.add_photo -> {
                Toast.makeText(context, "agregar foto", Toast.LENGTH_SHORT).show()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun startPresenter() {

    }

    override fun onErrorCode(message: String) {

    }
}
