package com.example .myapplication

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import java.time.LocalDate

class MainActivity : AppCompatActivity(),SensorEventListener {
    private lateinit var sensor:Sensor
    private lateinit var sensorManger:SensorManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManger=getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor=sensorManger.getDefaultSensor(Sensor.TYPE_LIGHT)
        sensorManger.registerListener(this,sensor,SensorManager.SENSOR_DELAY_UI)

        val btnStart=findViewById<Button>(R.id.BTN1)
        val btnEnd=findViewById<Button>(R.id.BTN2)
        btnStart.setOnClickListener(myClick)
        btnEnd.setOnClickListener(myClick)

    }
    override fun onResume() {
        super.onResume()
        sensorManger.registerListener(this,sensor,SensorManager.SENSOR_DELAY_UI)
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManger.unregisterListener(this)
    }

    private val myClick= View.OnClickListener {
        val myTAG="tag"
        val intent=Intent(this,MyService::class.java)
        when(it.id){
            R.id.BTN1->{
                Log.d(myTAG,"${R.id.BTN1}")
                startService(intent)
                makeShowNotify("set")
            }
            R.id.BTN2->{
                Log.d(myTAG,"${R.id.BTN2}")
                startService(intent)
            }
        }
    }

    private fun makeShowNotify(s:String){
        val notifyManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel=NotificationChannel("mCounter","Channel Counter",NotificationManager.IMPORTANCE_DEFAULT)
        notifyManager.createNotificationChannel(channel)
        val myBuilder=NotificationCompat.Builder(this,"mCounter").apply{
            setContentTitle("到期通知")
            setContentText("通知:$s")
            setSubText("我在狀態列")
            setWhen(System.currentTimeMillis())
            setChannelId("mCounter")
            setSmallIcon(R.drawable.penguin)
        }
        notifyManager.notify(1,myBuilder.build())
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if (p0?.sensor?.type==Sensor.TYPE_LIGHT){
            if (p0.values[0]>10000){
                val notifyManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val channel=NotificationChannel("mCounter","Channel Counter",NotificationManager.IMPORTANCE_DEFAULT)
                notifyManager.createNotificationChannel(channel)
                val myBuilder=NotificationCompat.Builder(this,"mCounter").apply{
                    setContentTitle("到期通知")
                    setContentText("light:${p0.values[0]}")
                    setSubText("light lev")
                    setWhen(System.currentTimeMillis()-100)
                    setChannelId("mCounter")
                    setSmallIcon(R.drawable.penguin)
                }
                notifyManager.notify(2,myBuilder.build())
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
}