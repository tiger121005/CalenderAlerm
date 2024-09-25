package app.ito.yomi.calenderalerm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi

class AlarmManagerReceiver: BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent?) {
        // Alarmの区別と送信するデータの番号
        val id = intent?.getIntExtra("id", 0)

        Intent(context, AlarmActivity::class.java).apply {
            putExtra("id", id)
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(this)
        }
//        Toast.makeText(context, "アラーム番号　：「{$id}」が実行されました", Toast.LENGTH_LONG).show()
    }
}