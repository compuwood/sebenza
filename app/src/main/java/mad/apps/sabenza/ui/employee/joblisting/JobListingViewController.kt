package mad.apps.sabenza.ui.employee.joblisting

import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.android.synthetic.main.footer_button_split.view.*
import mad.apps.sabenza.R
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.bindView
import mad.apps.sabenza.ui.widget.CongratsModal
import mad.apps.sabenza.ui.widget.ErrorModal
import mad.apps.sabenza.ui.widget.FooterButtonSplit
import mad.apps.sabenza.ui.widget.LoadingScreen
import javax.inject.Inject
import android.view.ViewGroup.MarginLayoutParams

class JobListingViewController : BaseViewController(), JobListingViewInterface {

    override fun layout(): Int = R.layout.employee_job_listing

    @Inject lateinit var presenter: JobListingPresenterInterface

    val jobNameTextView by bindView<TextView>(R.id.job_name_textview)
    val jobDescriptionTextView by bindView<TextView>(R.id.job_description_textview)
    val jobAddressTextView by bindView<TextView>(R.id.job_address_textview)
    val fromDateTextView by bindView<TextView>(R.id.from_date_textview)
    val toDateTextView by bindView<TextView>(R.id.to_date_textview)
    val budgetTextView by bindView<TextView>(R.id.budget_textview)
    val bottomButtons by bindView<FooterButtonSplit>(R.id.footer_buttons_container)
    val successModal by bindView<CongratsModal>(R.id.success_modal)
    val errorModal by bindView<ErrorModal>(R.id.error_modal)
    val loadingScreen by bindView<LoadingScreen>(R.id.loading_screen)
    val qrContainer by bindView<LinearLayout>(R.id.qr_container)
    val qrImageView by bindView<ImageView>(R.id.qr_imageview)
    val mainScrollView by bindView<ScrollView>(R.id.main_scrollview)

    override fun initView(view: View) {
        DaggerService.getDaggerComponent(view.context).inject(this)
        presenter.updateJobId(fetchViewArgument<String>("jobid"))

        initialiseTextViews()
        setupFooterButtons()
    }

    private fun initialiseTextViews() {
        val job = presenter.getJobObject()

        jobNameTextView.text = job.title
        jobDescriptionTextView.text = job.description
        jobAddressTextView.text = job.getAddressAsString()
        fromDateTextView.text = job.getStartDateAsString()
        toDateTextView.text = job.getEndDateAsString()
        budgetTextView.text = "$" + job.budget.toString()
    }

    private fun setupFooterButtons() {
        val hasBeenAccepted = presenter.getHasBeenAcceptedForJob()
        if (hasBeenAccepted) {
            (mainScrollView.layoutParams as MarginLayoutParams).bottomMargin = 0
            bottomButtons.visibility = View.GONE
            qrContainer.visibility = View.VISIBLE

            val qrUrl = presenter.getQrCodeUrl()
            createQrCode(qrUrl)
        }
        else {
            (mainScrollView.layoutParams as MarginLayoutParams).bottomMargin = getContext().resources.getDimensionPixelSize(R.dimen.footer_height)
            bottomButtons.visibility = View.VISIBLE
            qrContainer.visibility = View.GONE

            var hasAppliedForJob = presenter.getHasAppliedForJob()
            if (hasAppliedForJob) {
                bottomButtons.setRightButtonText("Withdraw")
            } else {
                bottomButtons.setRightButtonText("Apply")
            }

            bottomButtons.left_button.setOnClickListener { presenter.messageEmployer() }
            bottomButtons.right_button.setOnClickListener {
                if (hasAppliedForJob) {
                    presenter.withdrawApplication()
                } else {
                    presenter.applyForJob()
                }

                bottomButtons.right_button.setOnClickListener{ }
            }
        }
    }

    private fun createQrCode(url : String) {
        val writer = QRCodeWriter()
        try {
            val bitMatrix = writer.encode(url, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0..width - 1) {
                for (y in 0..height - 1) {
                    bmp.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
                }
            }
            qrImageView.setImageBitmap(bmp)

        } catch (e: WriterException) {
            e.printStackTrace()
        }
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
        successModal.show()
        setupFooterButtons()
        idle()
    }

    override fun error(error: String) {
        idle()
        errorModal.show(error)
    }
}