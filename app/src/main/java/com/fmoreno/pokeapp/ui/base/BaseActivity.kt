package com.fmoreno.pokeapp.ui.base

import android.annotation.SuppressLint
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.fmoreno.pokeapp.R
import java.util.*

/**
 * @author Fabian Moreno
 * @description Clase genérica de la cual extienden los demás activities para reutilizar código.
 */
open class BaseActivity: AppCompatActivity() {
    private var modalLoadingLaunched = false
    var dialogoLoading: AlertDialog? = null

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
}