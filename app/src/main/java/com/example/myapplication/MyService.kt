package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class MyService:Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            makeShowNotify("john")
            delay(10000)
            makeShowNotify("marry")

        }
        return START_NOT_STICKY
    }

    private fun makeShowNotify(s:String){
        val notifyManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel= NotificationChannel("mCounter","Channel Counter", NotificationManager.IMPORTANCE_HIGH)
        notifyManager.createNotificationChannel(channel)
        val myBuilder= NotificationCompat.Builder(this,"mCounter").apply{
            setContentTitle("到期通知")
            setContentText("通知:$s")
            setSubText("我在狀態列")
            setWhen(System.currentTimeMillis()-100)
            setChannelId("mCounter")
            setSmallIcon(R.drawable.penguin)
        }
        notifyManager.notify(1,myBuilder.build())
    }
}