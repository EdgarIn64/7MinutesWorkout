package com.example.a7minutesworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.example.a7minutesworkout.databinding.ActivityExerciseBinding
import com.example.a7minutesworkout.databinding.DialogCustomBackConfirmationBinding

class ExerciseActivity : AppCompatActivity() {
    private var binding: ActivityExerciseBinding? = null
    private var restTimer: CountDownTimer? = null
    private var exerciseTimer: CountDownTimer? = null
    private var restProgress = 0
    private var exerciseProgress = 0

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = 0

    private var player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        if(supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        exerciseList = Constants.defaultExerciseList()

        setupRestView()
    }

    override fun onBackPressed() {
        customDialogForBackButton()
    }

    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)
        val dialogBinding =
            DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnYes.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnCancel.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }

    private fun setupRestView() {
        try {
            val soundURI = Uri.parse(
                "android.resource://com.example.a7minutesworkout" + R.raw.start)
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
            player?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if(restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }
        var next = currentExercisePosition+1

        binding?.tvNext?.text = "Next: " + exerciseList!![next]?.getName()
        binding?.imageNext?.setImageResource(exerciseList!![next].getImage())

        binding?.imageExercise?.setImageResource(R.drawable.rest)
        setRestProgressBar()
    }

    private fun setupExerciseRestView() {
        binding?.flProgressBarExercise?.visibility = View.GONE
        binding?.tvNumberExercise?.visibility = View.INVISIBLE

        if(currentExercisePosition < 10) {
            binding?.flProgressBar?.visibility = View.VISIBLE
            binding?.tvReady?.text = "Rest for now"
            binding?.tvNext?.visibility = View.VISIBLE
            binding?.imageNext?.visibility = View.VISIBLE
            setupRestView()
        }
        else {
            finish()
            val intent = Intent(this, FinishActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupExerciseView() {
        if(exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        binding?.flProgressBar?.visibility = View.INVISIBLE
        binding?.flProgressBarExercise?.visibility = View.VISIBLE
        binding?.tvNumberExercise?.visibility = View.VISIBLE

        currentExercisePosition++

        binding?.tvNumberExercise?.text = "Exercise " + currentExercisePosition + " of 10"
        binding?.imageExercise?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvReady?.text = exerciseList!![currentExercisePosition]?.getName()
        binding?.tvNext?.visibility = View.INVISIBLE
        binding?.imageNext?.visibility = View.INVISIBLE

        if(currentExercisePosition >= 10) {
            binding?.tvNext?.visibility = View.VISIBLE
            binding?.imageNext?.visibility = View.VISIBLE
            binding?.tvNext?.text = "Finish"
            binding?.imageNext?.setImageResource(R.drawable.rest)
        }
        setExerciseProgressBar()
    }


    private fun setRestProgressBar() {
        binding?.progressBar?.progress = restProgress

        restTimer = object : CountDownTimer (10000, 1000) {
            override fun onTick(p0: Long) {
                restProgress++
                binding?.progressBar?.progress = 10 - restProgress
                binding?.tvTimer?.text = (10 - restProgress).toString()
            }

            override fun onFinish() {
                setupExerciseView()
            }
        }.start()
    }

    private fun setExerciseProgressBar() {
        binding?.progressBarExercise?.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer (30000, 1000) {
            override fun onTick(p0: Long) {
                exerciseProgress++
                binding?.progressBarExercise?.progress = 30 - exerciseProgress
                binding?.tvTimerExercise?.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() {
                setupExerciseRestView()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }
        if (exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        if (player != null) {
            player!!.stop()
        }

        binding = null
    }
}