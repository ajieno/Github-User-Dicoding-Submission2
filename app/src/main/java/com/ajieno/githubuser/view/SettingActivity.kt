package com.ajieno.githubuser.view

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.ajieno.githubuser.R
import com.ajieno.githubuser.alarm.AlarmReceiver
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    companion object{
        const val PREFS_NAME = "SettingPref"
        private const val DAILY = "daily"
    }

    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var mSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        alarmReceiver = AlarmReceiver()
        mSharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        setSwitch()
        sw_setting.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                alarmReceiver.setDailyReminder(
                        this,
                        AlarmReceiver.TYPE_DAILY,
                        "Let's find favorite user on Github"
                )
            }else{
                alarmReceiver.cancelAlarm(this)
            }
        }

    }

    private fun setSwitch() {
       sw_setting.isChecked = mSharedPreferences.getBoolean(DAILY, false)
    }

    private fun saveChange(value: Boolean){
        val editor = mSharedPreferences.edit()
        editor.putBoolean(DAILY, value)
        editor.apply()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}