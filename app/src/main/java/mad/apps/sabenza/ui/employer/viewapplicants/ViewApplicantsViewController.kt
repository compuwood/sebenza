package mad.apps.sabenza.ui.employer.viewapplicants

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import mad.apps.sabenza.R
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.bindView
import mad.apps.sabenza.ui.widget.ErrorModal
import mad.apps.sabenza.ui.widget.LoadingScreen
import javax.inject.Inject

class ViewApplicantsViewController : BaseViewController(), ViewApplicantsViewInterface {

    override fun layout(): Int = R.layout.employer_applicants_list

    @Inject lateinit var presenter: ViewApplicantsPresenterInterface

    val applicantListRecycler by bindView<RecyclerView>(R.id.applicants_list_recycler)
    val errorModal by bindView<ErrorModal>(R.id.error_modal)
    val loadingScreen by bindView<LoadingScreen>(R.id.loading_screen)

    override fun initView(view: View) {
        DaggerService.getDaggerComponent(view.context).inject(this)

        val applicantListRecyclerAdapter = ApplicantListRecyclerAdapter(presenter.getApplicants())

        applicantListRecyclerAdapter.setItemClickListener(object : ApplicantListRecyclerAdapter.ItemClickListener {
            override fun clickItem(index: Int, employeeId: String) {
                presenter.gotoSelectedApplicantProfile(employeeId)
            }
        })

        applicantListRecycler.layoutManager = LinearLayoutManager(view.context, LinearLayout.VERTICAL, false)
        applicantListRecycler.adapter = applicantListRecyclerAdapter

    }



    override fun onAttach(view: View) {
        super.onAttach(view)
        presenter.takeView(this)
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        presenter.dropView()
    }

    override fun busy() {
        loadingScreen.show()
    }

    override fun idle() {
        loadingScreen.hide()
    }

    override fun success() {
        idle()
    }

    override fun error(error: String) {
        idle()
        errorModal.show(error)
    }
}