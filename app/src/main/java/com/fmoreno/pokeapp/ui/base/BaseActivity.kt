package com.fmoreno.pokeapp.ui.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.fmoreno.pokeapp.R
import com.fmoreno.pokeapp.persistence.viewmodel.AppDatabaseViewModel
import java.util.*

/**
 * @author Fabian Moreno
 * @description Clase genérica de la cual extienden los demás activities para reutilizar código.
 */
open class BaseActivity: AppCompatActivity() {
    var isModalGenericLaunched = false
    private var modalLoadingLaunched = false
    var dialogoLoading: AlertDialog? = null

    public lateinit var appDatabaseViewModel: AppDatabaseViewModel

    @SuppressLint("InlinedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appDatabaseViewModel = run {
            ViewModelProviders.of(this).get(AppDatabaseViewModel::class.java)
        }
    }

    /**
     * Modal de error de conexión a los servicios.
     */
    @SuppressLint("NewApi")
    protected fun launchError() {
        hideLoading()
        Log.e("launchError", "launchError");
    }

    fun String.titlecaseFirstCharIfItIsLowercase() = replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }

    /**
     * Modal "Cargando"
     */
    @SuppressLint("InflateParams")
    protected fun launchLoading() {
        if (!modalLoadingLaunched) {
            modalLoadingLaunched = true
            runOnUiThread {
                val inflater = this.layoutInflater
                val dialogViewError = inflater.inflate(R.layout.activity_loading, null)
                dialogoLoading = AlertDialog.Builder(this, R.style.CustomDialog).apply {
                    setView(dialogViewError)
                    setCancelable(false)
                }.show()
            }
        }
    }

    /**
     * Cierra el modal "Cargando"
     */
    protected open fun hideLoading() {
        dialogoLoading?.dismiss()
        modalLoadingLaunched = false
    }

    fun isInternetAvailable(context: Context): Boolean {
        var isConnected: Boolean = false // Initial Value
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }

    protected fun <T : Activity>launchPopupConfirmReturnActivity(title: String,
                                                                 subTitle: String,
                                                                 description: String,
                                                                 source: Activity,
                                                                 destiny: Class<T>,
                                                                 isWithNavigation: Boolean,
                                                                 isCancelConfirm:Boolean){
        if (!isModalGenericLaunched) {
            isModalGenericLaunched = true
            runOnUiThread {
                val inflater = this.layoutInflater
                val popup = inflater.inflate(R.layout.fragment_modal_generic, null)
                var tvTitle: TextView = popup.findViewById(R.id.tvPopupTitle)
                var tvSubTitle: TextView = popup.findViewById(R.id.tvPopupSubtitle)
                var tvDescription: TextView = popup.findViewById(R.id.tvPopupDescription)
                var btnCancelar: ImageView = popup.findViewById(R.id.img_btn_cancel)
                var btnConfirmar: ImageView = popup.findViewById(R.id.img_btn_confirm)
                tvTitle.text = title
                if(!tvSubTitle.equals("")){
                    tvSubTitle.text = subTitle
                } else {
                    tvSubTitle.visibility = View.GONE
                }
                tvDescription.text = description
                if(isCancelConfirm){
                    btnCancelar.visibility = View.VISIBLE
                    btnConfirmar.visibility = View.VISIBLE
                } else {
                    btnCancelar.visibility = View.GONE
                    btnConfirmar.visibility = View.VISIBLE
                }
                val alertDialogoGeneric = AlertDialog.Builder(this, R.style.AlertDialogCustom).apply {
                    setView(popup)
                    setCancelable(false)
                }.show()
                btnCancelar.setOnClickListener {
                    isModalGenericLaunched = false
                    //hideLoading()
                    alertDialogoGeneric?.dismiss()
                }

                btnConfirmar.setOnClickListener {
                    isModalGenericLaunched = false
                    if (isWithNavigation) {
                        navigateAndFinishFromRight(source, destiny)
                    }
                    alertDialogoGeneric?.dismiss()
                }

            }
        }
    }

    fun launchPopupCancelConfirm(title: String,
                                 subTitle: String,
                                 description: String,
                                 isCancelConfirm:Boolean){
        if (!isModalGenericLaunched) {
            isModalGenericLaunched = true
            runOnUiThread {
                val inflater = this.layoutInflater
                val popup = inflater.inflate(R.layout.fragment_modal_generic, null)
                var tvTitle: TextView = popup.findViewById(R.id.tvPopupTitle)
                var tvSubTitle: TextView = popup.findViewById(R.id.tvPopupSubtitle)
                var tvDescription: TextView = popup.findViewById(R.id.tvPopupDescription)
                var btnCancelar: ImageView = popup.findViewById(R.id.img_btn_cancel)
                var btnConfirmar: ImageView = popup.findViewById(R.id.img_btn_confirm)
                tvTitle.text = title
                if(!tvSubTitle.equals("")){
                    tvSubTitle.text = subTitle
                } else {
                    tvSubTitle.visibility = View.GONE
                }
                tvDescription.text = description
                if(isCancelConfirm){
                    btnCancelar.visibility = View.VISIBLE
                    btnConfirmar.visibility = View.VISIBLE
                } else {
                    btnCancelar.visibility = View.GONE
                    btnConfirmar.visibility = View.VISIBLE
                }
                val alertDialogoGeneric = AlertDialog.Builder(this, R.style.AlertDialogCustom).apply {
                    setView(popup)
                    setCancelable(false)
                }.show()
                btnCancelar.setOnClickListener {
                    isModalGenericLaunched = false
                    //hideLoading()
                    alertDialogoGeneric?.dismiss()
                }

                btnConfirmar.setOnClickListener {
                    isModalGenericLaunched = false
                    //hideLoading()
                    alertDialogoGeneric?.dismiss()
                }

            }
        }
    }

    protected fun <T : Activity> navigateAndFinishFromRight(
        source: Activity,
        destiny: Class<T>
    ) {
        val intent = Intent(source, destiny)
        startActivity(intent)
        overridePendingTransition(
            R.anim.translate_left_side,
            R.anim.right_out
        )
        source.finish()
    }
}