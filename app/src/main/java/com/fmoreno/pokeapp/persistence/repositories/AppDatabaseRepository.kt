package com.fmoreno.pokeapp.persistence.repositories

import android.app.Application
import android.content.Context
import android.util.Log
import com.asesoftware.previsora.autogestion.database.generatebackup.RoomBackup
import com.fmoreno.pokeapp.persistence.AppDatabase

class AppDatabaseRepository(application: Application) {
    var db = AppDatabase.getInstance(application)!!
    var mContext: Context = application

    fun exportDataBase() {
        try {
            val roomBackup = RoomBackup()
            roomBackup.context(mContext)
            roomBackup.database(db)
            roomBackup.enableLogDebug(false)
            roomBackup.backupIsEncrypted(false)
            roomBackup.customEncryptPassword("MainActivity.SECRET_PASSWORD")
            roomBackup.useExternalStorage(true)
            roomBackup.maxFileCount(5)
            roomBackup.backup()
        } catch (ex: Exception) {
            Log.e("exportData", ex.toString())
        }
    }
}