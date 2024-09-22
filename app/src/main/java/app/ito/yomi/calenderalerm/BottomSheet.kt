package app.ito.yomi.calenderalerm

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

class BottomSheet(): BottomSheetDialogFragment() {

//    private lateinit var db: AlarmDatabase
    private lateinit var listener: NoticeBottomSheetListener

    private var change: Boolean = false
    private var id: Int = -1
    private lateinit var title: String
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    private var week: Int = 8
    private var hour: Int = 100
    private var minute: Int = 100

    private lateinit var view: View
    private lateinit var titleEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var timeEditText: EditText
    private lateinit var deleteButton: Button
    private lateinit var changeButton: Button


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_bottom_sheet,container,false)
        viewInit()

        val applicationContext = requireContext()
//        db = AlarmDatabase.getDatabase(applicationContext)

        getData()
        setData()

        setupDatePicker()
        setupTimePicker()

        deleteButton.setOnClickListener {
            Log.d("deleteID", id.toString())
            listener.onDeleteButtonClick()
//            db.alarmDataDao().deleteAlarmById(id)

            dismiss()
        }

        changeButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val date = dateEditText.text.toString()
            val time = timeEditText.text.toString()
            val year = date.split("/")[0].toInt()
            val month = date.split("/")[1].toInt()
            val day = date.split("/")[2].toInt()
            val hour = time.split(":")[0].toInt()
            val minute = time.split(":")[1].toInt()
            listener.onChangeButtonClick()

//            db.alarmDataDao().upsert(AlarmData(id, title, year, month, day, week, hour, minute))
            dismiss()
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        setupDatePicker()
        setupTimePicker()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as NoticeBottomSheetListener
    }

    fun viewInit() {
        titleEditText = view.findViewById<EditText>(R.id.title_edit_text)
        dateEditText = view.findViewById<EditText>(R.id.date_edit_text)
        timeEditText = view.findViewById<EditText>(R.id.time_edit_text)

        deleteButton = view.findViewById<Button>(R.id.delete_button)
        changeButton = view.findViewById<Button>(R.id.change_button)
    }

    fun getData() {
        change = requireArguments().getBoolean("change")
        id = requireArguments().getInt("id")
        title = requireArguments().getString("title") ?: ""
        year = requireArguments().getInt("year")
        month = requireArguments().getInt("month")
        day = requireArguments().getInt("day")
        Log.d("day", day.toString())
        week = requireArguments().getInt("week")
        hour = requireArguments().getInt("hour")
        minute = requireArguments().getInt("minute")
    }

    fun setData() {
        Log.d("setData", "setData")
        if (change) {
            titleEditText.setText(title)
            val date = "$year/$month/$day"
            val time = "$hour:$minute"
            dateEditText.setText(date)
            timeEditText.setText(time)

            deleteButton.visibility = View.VISIBLE
            changeButton.setText("変更")

            Log.d("change", "change")
            Log.d("binding", dateEditText.text.toString())
        } else {
            deleteButton.visibility = View.GONE
            changeButton.setText("追加")
            Log.d("add", "add")
        }
    }

    fun setupDatePicker() {
        dateEditText.setRawInputType(InputType.TYPE_NULL)
        var date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        if (day != 0) {
            date = "$year/$month/$day"
        }
        dateEditText.setText(date)
        dateEditText.setOnClickListener {
            val datePicker = DatePickerFragment(dateEditText)
            val fragmentManager = requireActivity().supportFragmentManager
            datePicker.show(fragmentManager, "datePickerDialog")
        }
    }

    fun setupTimePicker() {
        timeEditText.setRawInputType(InputType.TYPE_NULL)
        var time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        if (hour != 100) {
            time = "$hour:$minute"
        }
        timeEditText.setText(time)
        timeEditText.setOnClickListener {
            val timePicker = TimePickerFragment(timeEditText)
            val fragmentManager = requireActivity().supportFragmentManager
            timePicker.show(fragmentManager, "timePickerDialog")
        }
    }
}

class DatePickerFragment(private val editText: EditText): DialogFragment(), DatePickerDialog.OnDateSetListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val setLocalDate = LocalDate.of(year, month+1, dayOfMonth)
        val format = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val date = setLocalDate.format(format)
        editText.setText(date)
    }
}

class TimePickerFragment(private val editText: EditText): DialogFragment(), TimePickerDialog.OnTimeSetListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar: Calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(requireContext(), this, hour, minute, true)
    }

    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
        val setLocalTime = LocalTime.of(hour, minute)
        val format = DateTimeFormatter.ofPattern("HH:mm")
        val time = setLocalTime.format(format)
        editText.setText(time)
    }
}