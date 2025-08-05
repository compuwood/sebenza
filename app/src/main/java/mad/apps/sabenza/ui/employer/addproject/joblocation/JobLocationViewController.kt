package mad.apps.sabenza.ui.employer.addproject.joblocation

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import mad.apps.sabenza.R
import mad.apps.sabenza.data.model.address.Address
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.bindView
import mad.apps.sabenza.ui.employer.addjob.AddJobViewModel
import javax.inject.Inject

open class JobLocationViewController : BaseViewController(), JobLocationViewInterface {

    override fun layout(): Int = R.layout.employer_job_location

    @Inject lateinit var presenter: JobLocationPresenterInterface

    val addressListRecycler by bindView<RecyclerView>(R.id.address_list_recycler)
    val addAddressButton by bindView<ImageView>(R.id.add_location_button)

    var isJob = false

    override fun initView(view: View) {
        DaggerService.getDaggerComponent(view.context).inject(this)

        presenter.updateWithAddJobModel(fetchViewArgument<AddJobViewModel>(AddJobViewModel.argName()))

        addAddressButton.setOnClickListener { presenter.addAddress() }

        val addressListRecyclerAdapter = AddressListRecyclerAdapter(presenter.getEmployerAddressList(),presenter.getEmployerDefaultAddressId())

        addressListRecyclerAdapter.setItemClickListener(object : AddressListRecyclerAdapter.ItemClickListener {
            override fun clickItem(index: Int, address: Address) {
                presenter.saveAddress(address)
                presenter.gotoDateTimeScreen(isJob)
            }
        })

        addressListRecycler.layoutManager = LinearLayoutManager(view.context, LinearLayout.VERTICAL, false)
        addressListRecycler.adapter = addressListRecyclerAdapter
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
    }

    override fun idle() {
    }


}