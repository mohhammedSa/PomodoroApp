package com.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import com.pomodoro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var timer: CountDownTimer
    private var startTimeInMinutes: Long = 25
    private var isTimerStart = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        chooseTimeUsingSeekBar()
        startBtnClick()
        resetBtnClick()
    }

    private fun startBtnClick() {
        binding.startBtn.setOnClickListener {
            if (!isTimerStart){
                startTimer()
                binding.headerText.text = resources.getText(R.string.header_Text_start)
                isTimerStart = true
                binding.seekBar.isEnabled = false
            }
        }
    }
    private fun resetBtnClick() {
        binding.resetBtn.setOnClickListener {
            timer.cancel()
            binding.headerText.text = resources.getText(R.string.header_Text)
            changeTimerTV(formattedTimerTv(convertTimeToMill(startTimeInMinutes)))
            isTimerStart = false
            binding.seekBar.isEnabled = true
        }
    }

    private fun chooseTimeUsingSeekBar() {
        changeTimerTV(formattedTimerTv(convertTimeToMill(startTimeInMinutes)))
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                startTimeInMinutes = progress.toLong()
                binding.timerTv.text = formattedTimerTv(convertTimeToMill(startTimeInMinutes))
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
    }

    private fun startTimer() {
        val totalTime = convertTimeToMill(startTimeInMinutes)
        timer = object : CountDownTimer(convertTimeToMill(startTimeInMinutes), 1000) {
            override fun onTick(timeLeft: Long) {
                changeTimerTV(formattedTimerTv(timeLeft))
                val progress = ((timeLeft.toDouble() / totalTime) * 100).toInt()
                binding.seekBarTimeLeft.progress = progress
            }

            override fun onFinish() {
                changeTimerTV("Finished")
                isTimerStart = false
                binding.seekBar.isEnabled = true
            }
        }.start()
    }

    private fun changeTimerTV(str: String) {
        binding.timerTv.text = str
    }

    private fun convertTimeToMill(timeInMinutes: Long): Long {
        return timeInMinutes * 60 * 1000
    }

    private fun formattedTimerTv(timeInMill: Long): String {
        val minutes = timeInMill / (1000 * 60)
        val seconds = timeInMill / 1000 % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}