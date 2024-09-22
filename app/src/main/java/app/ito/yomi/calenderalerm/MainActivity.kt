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
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter

class MainActivity : AppCompatActivity(), NoticeBottomSheetListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var calendarRecyclerView:RecyclerView
    private lateinit var monthText:TextView
    private lateinit var yearText:TextView
    private lateinit var selectedDate: LocalDate
    private var currentSelect: AlarmData? = null

    private var alarmData: MutableList<AlarmData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(root) }
        val viewModel: MainViewModel by viewModels()
        AndroidThreeTen.init(this)

        viewModel.alarmAllData.observe(this, Observer {
            setMonthView()
        })

        setupButton()
        initWidgets()
        selectedDate = LocalDate.now()
//        setMonthView()

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
            args.putInt("year", selectedDate.year)
            args.putInt("month", selectedDate.monthValue)
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

    override fun onDeleteButtonClick() {
        val viewModel: MainViewModel by viewModels()
        currentSelect?.let { data ->
            viewModel.delete(data.id)
        }
    }

    override fun onChangeButtonClick() {
        val viewModel: MainViewModel by viewModels()
        currentSelect?.let { data ->
            if (data.id == -1) {
                viewModel.insert(data)
            } else {
                viewModel.update(data)
            }
        }
    }

    private fun setupButton() {
        setupAddButton()
        setupChangeButton()
        setupChangeMonthButton()
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
    private fun initWidgets() {
        calendarRecyclerView = binding.rv
        monthText = binding.monthText
        yearText = binding.yearText
    }

    private fun setMonthView() {
        monthText.text = month(selectedDate)
        yearText.text = year(selectedDate)
        val daysInMonth = daysInMonthArray(selectedDate)
        Log.d("daysInMonth", daysInMonth.toString())
        val data = getData(daysInMonth)
        val calendarAdapter = RecyclerviewAdapter(data)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 7)
        calendarRecyclerView.layoutManager = layoutManager
        calendarRecyclerView.adapter = calendarAdapter

        calendarAdapter.setOnDataCellClickListener(object : RecyclerviewAdapter.OnDataCellClickListener {
            override fun onItemClick(data: AlarmData) {
                currentSelect = data
                Log.d("data", data.toString())
                if (data.title == "") {
                    binding.timeView.setText("予定なし")
                    binding.titleView.setText("")
                    binding.dateView.text = data.year.toString() + "/" + data.month.toString() + "/" + data.date.toString()
                } else {
                    binding.timeView.text = data.hour.toString() + ":" + data.minute.toString() + "アラーム"
                    binding.titleView.text = data.title
                    binding.dateView.text = data.year.toString() + "/" + data.month.toString() + "/" + data.date.toString()
                }

            }
        })
    }


    private fun daysInMonthArray(date: LocalDate?):ArrayList<Long> {
        val daysInMonthArray = ArrayList<Long>()
        // 年と月を取得
        val yearMonth = YearMonth.from(date)
        // 月の日数を取得
        val daysInMonth = yearMonth.lengthOfMonth()
        // 月初めの日付を取得
        val firstOfMonth = selectedDate.withDayOfMonth(1)
        // 月初めの曜日を取得
        val dayOfWeek = firstOfMonth.dayOfWeek.value

        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add(0)
            } else {
                daysInMonthArray.add((i - dayOfWeek).toLong())
            }
        }
        if (daysInMonthArray[6] == 0.toLong()) {
            for (i in 0..6) {
                daysInMonthArray.removeAt(0)
            }
        } else if (daysInMonthArray[35] == 0.toLong()) {
            for (i in 0..6) {
                daysInMonthArray.removeAt(daysInMonthArray.size - 1)
            }
        }
        return daysInMonthArray
    }

    private fun month(date: LocalDate?): String {
        val formatter = DateTimeFormatter.ofPattern("M月")
        return date!!.format(formatter)
    }
    private fun year(date: LocalDate?): String {
        val formatter = DateTimeFormatter.ofPattern("/yyyy")
        return date!!.format(formatter)
    }

    private fun clickChangeMonthButton(state: String) {
        initWidgets()
        if (state == "next") {
            selectedDate = selectedDate.plusMonths(1)
        } else {
            selectedDate = selectedDate.minusMonths(1)
        }
        setMonthView()
    }

    private fun getData(dates: ArrayList<Long>): ArrayList<AlarmData> {
        val dataList = ArrayList<AlarmData>()

        Log.d("size before", dates.size.toString())
        for (i in 0..dates.size - 1) {

            var id: Int = -1
            var title: String = ""
            var hour: Int = 100
            var minute: Int = 100
            val data = filteredAlarmData(selectedDate.year, selectedDate.monthValue, dates[i].toInt())
            if (data.size != 0) {
                id = data[0].id
                title = data[0].title
                hour = data[0].hour
                minute = data[0].minute
            }

            val alarmData = AlarmData(
                id = id,
                title = title,
                year = selectedDate.year,
                month = selectedDate.monthValue,
                date = dates[i].toInt(),
                week = selectedDate.dayOfWeek.value,
                hour = hour,
                minute = minute
            )
            dataList.add(alarmData)

        }
        Log.d("dataList", dataList.toString())
        Log.d("dataListSize", dataList.size.toString())
        alarmData = dataList
        return dataList
    }

    private fun filteredAlarmData(year: Int, month: Int, date: Int): List<AlarmData> {
        val data = alarmData.filter { it.year == year && it.month == month && it.date == date }
        return data
    }

}