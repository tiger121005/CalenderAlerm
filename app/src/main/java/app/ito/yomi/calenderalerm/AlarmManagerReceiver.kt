package app.ito.yomi.calenderalerm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi

class AlarmManagerReceiver: BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {

        // Alarmの区別と送信するデータの番号
        val id = intent?.getIntExtra("id", 0)

        Toast.makeText(context, "アラーム番号　：「{$id}」が実行されました", Toast.LENGTH_LONG).show()
    }
}