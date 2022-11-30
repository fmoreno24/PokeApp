package com.fmoreno.pokeapp.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.fmoreno.pokeapp.R
import com.fmoreno.pokeapp.databinding.ActivitySplashBinding
import java.lang.Exception
import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.util.*

class SplashActivity : AppCompatActivity(){
    private lateinit var binding: ActivitySplashBinding

    val segundos = 4
    val milisegundos = segundos * 1000
    val delay = 2

    var atg: Animation? = null
    var right_in: Animation? = null
    var zoom_forward_in: Animation? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        atg = AnimationUtils.loadAnimation(this, R.anim.atg)
        right_in = AnimationUtils.loadAnimation(this, R.anim.right_in)
        zoom_forward_in = AnimationUtils.loadAnimation(this, R.anim.zoom_forward_in)

        setFooterInfo()

        nextActivity()
    }


    /**
     * Metodo para ir al mainactiviy
     */
    private fun nextActivity() {
        try {
            object : CountDownTimer(milisegundos.toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    val mainActivity = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(mainActivity)
                    overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out)
                    finish()
                }
            }.start()
        } catch (ex: Exception) {

        }
    }

    private fun setFooterInfo() {
        try {
            val currentYear: String? = getCurrentYear()
            //String footerMessage = "\u00A9" + currentYear + " Fabian Moreno" R.string.str_name_complete;
            val footerMessage = "\u00A9" + currentYear + " | " + getStringFromResource(
                R.string.str_name,
                this
            )
            binding.footerInfo.setText(footerMessage)
        } catch (e: Exception) {
        }
    }

    fun getCurrentYear(): String? {
        return getCurrentime("yyyy")
    }
    /**
     * get Current Time with format parameter
     *
     * @param format
     * @return string time
     */
    fun getCurrentime(format: String?): String? {
        val cal = Calendar.getInstance()
        cal.time
        val sdf = SimpleDateFormat(format)
        return sdf.format(cal.time)
    }

    /**
     * this method get resource from any object who need it.
     *
     * @param idResource
     */
    fun getStringFromResource(idResource: Int, _context: Context): String? {
        // TODO Auto-generated method stub
        return try {
            _context.resources.getString(idResource)
        } catch (e: NullPointerException) {
            // TODO: handle exception
            ""
        }
    }
}