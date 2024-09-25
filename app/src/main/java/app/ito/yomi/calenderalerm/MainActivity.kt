package app.ito.yomi.calenderalerm

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.ito.yomi.calenderalerm.databinding.ActivityMainBinding
import com.jakewharton.threetenabp.AndroidThreeTen
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

class MainActivity : AppCompatActivity(), NoticeBottomSheetListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var calendarRecyclerView:RecyclerView
    private lateinit var monthText:TextView
    private lateinit var yearText:TextView
    private lateinit var showDate: LocalDate
    private var currentSelect: AlarmData? = null

    private val calenderManager = CalenderManager()

    private var alarmData: MutableList<AlarmData> = mutableListOf()

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(root) }
        val viewModel: MainViewModel by viewModels()
        AndroidThreeTen.init(this)

        viewModel.alarmAllData.observe(this, Observer { value ->
            alarmData = value.toMutableList()
            setMonthView()
            reloadMemoView()
        })

        setupButton()
        initWidgets()
        showDate = LocalDate.now()

        binding.nextButton.setOnClickListener {
            clickChangeMonthButton("next")
        }
        binding.previousButton.setOnClickListener {
            clickChangeMonthButton("previous")
        }
        binding.addButton.setOnClickListener {
            val myBottomSheet = BottomSheet()
            val args = Bundle()
            args.putBoolean("change", false)
            args.putInt("id", -1)
            args.putString("title", "")
            args.putInt("year", showDate.year)
            args.putInt("month", showDate.monthValue)
            args.putInt("day", 0)
            args.putInt("week", 8)
            args.putInt("hour", 100)
            args.putInt("minute", 100)
            myBottomSheet.setArguments(args)
            myBottomSheet.show(supportFragmentManager,"navigation_bottom_sheet")
        }
        binding.changeButton.setOnClickListener {
            currentSelect?.let { data ->
                val myBottomSheet = BottomSheet()
                val args = Bundle()
                Log.d("changeID", data.id.toString())
                args.putBoolean("change", true)
                args.putInt("id", data.id)
                args.putString("title", data.title)
                args.putInt("year", data.year)
                args.putInt("month", data.month)
                args.putInt("day", data.date)
                args.putInt("week", data.week)
                args.putInt("hour", data.hour)
                args.putInt("minute", data.minute)
                myBottomSheet.setArguments(args)
                myBottomSheet.show(supportFragmentManager,"navigation_bottom_sheet")
            }
        }
    }

    override fun onDeleteButtonClick(id: Int) {
        viewModel.delete(id)
    }

    override fun onChangeButtonClick(data: AlarmData) {
        Log.d("tap change", "tapped")
        Log.d("currentSelect", currentSelect.toString())

        val addData = AlarmData(
            title = data.title,
            year = data.year,
            month = data.month,
            date = data.date,
            week = data.week,
            hour = data.hour,
            minute = data.minute
        )
        addData(addData)
        setAlarm(addData)
    }

    private fun setupButton() {
        setupAddButton()
        setupChangeButton()
        setupChangeMonthButton()
    }

    private fun initWidgets() {
        calendarRecyclerView = binding.rv
        monthText = binding.monthText
        yearText = binding.yearText
    }

    private fun setMonthView() {
        setYearMonthText()
        setCalenderRecyclerView()
    }

    private fun addData(data: AlarmData) {
        currentSelect?.also { select ->
            if (select.id == -1) {
                Log.d("before insert data", data.toString())
                viewModel.insert(data)
            } else {
                Log.d("before update data", data.toString())
                viewModel.update(data, select.id)
            }
        } ?: run {
            Log.d("before insert data current null", data.toString())
            viewModel.insert(data)
        }
    }

    private fun setAlarm(data: AlarmData) {
        val timeDiff = calculateTimeDifference(data.year, data.month, data.date, data.hour, data.minute)
        val settingAlarmManager = SettingAlarmManager()
        settingAlarmManager.settingAlarmManager(this, timeDiff, data.id)
    }

    private fun setupAddButton() {
        binding.addButton.backgroundTintList = null
        binding.addButton.stateListAnimator = null
        binding.addButton.elevation = 15f
    }

    private fun setupChangeButton() {
        binding.changeButton.backgroundTintList = null
    }

    private fun setupChangeMonthButton() {
        binding.previousButton.backgroundTintList = null
        binding.nextButton.backgroundTintList = null

    }

    private fun setYearMonthText() {
        monthText.text = calenderManager.month(showDate)
        yearText.text = calenderManager.year(showDate)
    }

    private fun setCalenderRecyclerView() {
        val daysInMonth = calenderManager.daysInMonthArray(showDate)
        val data = calenderManager.getData(daysInMonth, showDate, alarmData)
        val calendarAdapter = RecyclerviewAdapter(data)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 7)
        calendarRecyclerView.layoutManager = layoutManager
        calendarRecyclerView.adapter = calendarAdapter

        calendarAdapter.setOnDataCellClickListener(object : RecyclerviewAdapter.OnDataCellClickListener {
            override fun onItemClick(data: AlarmData) {
                setMemoViewText(data)
            }
        })
    }

    private fun setMemoViewText(data: AlarmData) {
        currentSelect = data
        binding.dateView.text = calenderManager.formatDate(data.year, data.month, data.date)
        binding.titleView.setText(data.title)
        if (data.id == -1) {
            binding.timeView.setText(R.string.no_plan)
        } else {
            val timeText = calenderManager.formatTime(data.hour, data.minute) + "アラーム"
            binding.timeView.setText(timeText)
        }
        Log.d("data", data.toString())
    }

    private fun reloadCurrentData() {
        currentSelect?.let { old ->
            val year = old.year
            val month = old.month
            val date = old.date

            var new = alarmData.filter { it.year == year && it.month == month && it.date == date }
            if (new.size == 0) {
                currentSelect = AlarmData(-1, "", year, month, date, 8, 100, 100)
            } else {
                currentSelect = new[0]
            }
        }
    }

    private fun reloadMemoView() {
        reloadCurrentData()
        currentSelect?.let { data ->
            setMemoViewText(data)
        }
    }

    private fun clickChangeMonthButton(state: String) {
        initWidgets()
        if (state === "next") {
            showDate = showDate.plusMonths(1)
        } else {
            showDate = showDate.minusMonths(1)
        }
        setMonthView()
    }

    private fun calculateTimeDifference(year: Int, month: Int, day: Int, hour: Int, minute: Int): Long {
        val alarmTime = LocalDateTime.of(year, month, day, hour, minute)
        val currentTime = LocalDateTime.now()
        return Duration.between(currentTime, alarmTime).toMillis()
    }
}