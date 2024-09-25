package app.ito.yomi.calenderalerm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AlarmActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val timerManager = TimerManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_alarm)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        when (intent.action) {
            Intent.ACTION_MY_PACKAGE_REPLACED -> setAlarm(this)
            Intent.ACTION_BOOT_COMPLETED -> setAlarm(this)
        }
    }

    private fun setAlarm(context: Context) {

        val dataList = viewModel.alarmAllData.value ?: return
        for (data in dataList) {
            timerManager.setAlarm(context, data)
        }
    }
}