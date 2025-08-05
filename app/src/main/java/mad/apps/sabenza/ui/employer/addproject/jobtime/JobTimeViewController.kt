package mad.apps.sabenza.ui.employer.addproject.jobtime

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.view.View
import android.widget.*
import mad.apps.sabenza.R
import mad.apps.sabenza.di.DaggerService
import mad.apps.sabenza.framework.ui.BaseViewController
import mad.apps.sabenza.framework.ui.bindView
import mad.apps.sabenza.ui.employer.addjob.AddJobViewModel
import mad.apps.sabenza.ui.widget.ErrorModal
import mad.apps.sabenza.ui.widget.LoadingScreen
import java.util.*
import javax.inject.Inject


open class JobTimeViewController : BaseViewController(), JobTimeViewInterface {

    override fun layout(): Int = R.layout.employer_job_time

    @Inject lateinit var presenter: JobTimePresenterInterface

    val fromDateTextView by bindView<TextView>(R.id.from_date_textview)
    val toDateTextView by bindView<TextView>(R.id.to_date_textview)
    val fromDateLayout by bindView<LinearLayout>(R.id.from_date_layout)
    val toDateLayout by bindView<LinearLayout>(R.id.to_date_layout)

    val fromTimeTextView by bindView<TextView>(R.id.from_time_textview)
    val toTimeTextView by bindView<TextView>(R.id.to_time_textview)
    val fromTimeLayout by bindView<LinearLayout>(R.id.from_time_layout)
    val toTimeLayout by bindView<LinearLayout>(R.id.to_time_layout)

    val nextButton by bindView<FrameLayout>(R.id.next_button)
    val loadingScreen by bindView<LoadingScreen>(R.id.loading_screen)
    val errorModal by bindView<ErrorModal>(R.id.error_modal)

    val fromDate = Calendar.getInstance()!!
    val toDate = Calendar.getInstance()!!

    val DEFAULT_FROM_HOUR = 8
    val DEFAULT_TO_HOUR = 16
    val DEFAULT_FROM_MINUTE = 0
    val DEFAULT_TO_MINUTE = 0

    var fromHour = DEFAULT_FROM_HOUR
    var toHour = DEFAULT_TO_HOUR
    var fromMinute = DEFAULT_FROM_MINUTE
    var toMinute = DEFAULT_TO_MINUTE

    var isJob = false

    override fun initView(view: View) {
        DaggerService.getDaggerComponent(view.context).inject(this)

        presenter.updateAddJobModel(fetchViewArgument<AddJobViewModel>(AddJobViewModel.argName()))

        val datePickerFrom = DatePickerListener(dateType.FROM)
        val datePickerTo = DatePickerListener(dateType.TO)

        fromDateTextView.text = buildStringFromDate(fromDate)
        toDateTextView.text = buildStringFromDate(toDate)

        fromTimeTextView.text = buildStringFromTime(fromHour, fromMinute)
        toTimeTextView.text = buildStringFromTime(toHour, toMinute)

        fromDateLayout.setOnClickListener {
            val year = fromDate.get(Calendar.YEAR)
            val month = fromDate.get(Calendar.MONTH)
            val day = fromDate.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(view.context,
                    datePickerFrom,
                    year, month, day).show()
        }

        toDateLayout.setOnClickListener {
            val year = toDate.get(Calendar.YEAR)
            val month = toDate.get(Calendar.MONTH)
            val day = toDate.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(view.context,
                    datePickerTo,
                    year, month, day).show()
        }

        fromTimeLayout.setOnClickListener{
            TimePickerDialog(view.context, TimePickerListener(dateType.FROM),fromHour,fromMinute,false).show()
        }

        toTimeLayout.setOnClickListener {
            TimePickerDialog(view.context, TimePickerListener(dateType.TO),toHour,toMinute,false).show()
        }

        nextButton.setOnClickListener {
            if (validate()) {
                presenter.saveTimeRange(fromDate, toDate, fromHour, fromMinute, toHour, toMinute)
            }
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
        idle()
        presenter.gotoJobConfirmScreen(isJob)
    }

    override fun error(error: String) {
        idle()
        errorModal.show(error)
    }

    private fun buildStringFromTime(hour: Int, minute: Int) : String {
        var suffix = "AM"
        val minuteFormat = "%1$02d" // two digits

        var hourAsAmPm = hour
        if (hourAsAmPm > 12) {
            hourAsAmPm -= 12
            suffix = "PM"
        }
        val minuteFormatted = String.format(minuteFormat, minute)
        return "$hourAsAmPm:$minuteFormatted $suffix"
    }

    private fun buildStringFromDate(calendar: Calendar) : String {
        return "" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.YEAR)
    }


    private fun validate(): Boolean {
        if (fromDate > toDate) {
            fromDateTextView.error = "The job's starting date cannot be after the end date."
        }
        if (fromHour > toHour) {
            fromTimeTextView.error = "The job's daily starting time cannot be after it's ending time"
        } else if (fromHour == toHour && fromMinute > toMinute){
            fromTimeTextView.error = "The job's daily starting time cannot be after it's ending time"
        }

        if (fromDateTextView.error.isNullOrEmpty() && fromTimeTextView.error.isNullOrEmpty()){
            return true
        }
        return false
    }

    inner class DatePickerListener(val type: dateType) : DatePickerDialog.OnDateSetListener {

        override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
            when (type) {
                dateType.FROM -> {
                    fromDate.set(year, month, day)
                    fromDateTextView.text = buildStringFromDate(fromDate)
                    if (fromDate > toDate) {
                        toDate.set(year, month, day)
                        toDateTextView.text = buildStringFromDate(toDate)
                    }
                }
                dateType.TO -> {
                    toDate.set(year, month, day)
                    toDateTextView.text = buildStringFromDate(toDate)
                    if (toDate < fromDate) {
                        fromDate.set(year, month, day)
                        fromDateTextView.text = buildStringFromDate(fromDate)
                    }
                }
            }
        }
    }

    inner class TimePickerListener(val type: dateType) : TimePickerDialog.OnTimeSetListener {

        override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
            when (type) {
                dateType.FROM -> {
                    fromHour = hour
                    fromMinute = minute
                    fromTimeTextView.text = buildStringFromTime(hour,minute)
                }
                dateType.TO -> {
                    toHour = hour
                    toMinute = minute
                    toTimeTextView.text = buildStringFromTime(hour,minute)
                }
            }
        }
    }

    enum class dateType{ FROM, TO }
}