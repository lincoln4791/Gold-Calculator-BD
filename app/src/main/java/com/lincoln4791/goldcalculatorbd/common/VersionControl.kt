package com.lincoln4791.goldcalculatorbd.common

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.lincoln4791.goldcalculatorbd.*
import java.lang.Exception

object VersionControl {
    fun checkVersion(context : Context){
        Log.d("appVersion","Called")


        val firebaseRemoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        Log.d("Remote","Remote Config Inited")
        val configSettings  = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_cofig_defaults)

        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                val updated = it.result
                val version  = firebaseRemoteConfig.getString(Constants.APP_VERSION)
                Log.d("appVersion", "Remote : appVersion is : $version")

                if(!version.isNullOrEmpty()){
                    if(version != BuildConfig.VERSION_NAME){
                        showNewVersionAvailableDialog(context)
                    }
                    else{
                        Log.d("appVersion","App is already updated")
                    }
                }
                else{
                    Log.d("appVersion","No appVersion Data found in server")
                }



            } else {
                Log.d("appVersion", "fetch Failed")
            }
        }
    }

    fun showNewVersionAvailableDialog(context: Context){
        try {
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_new_version_available,null,false)
            val dialog = Dialog(context)
            dialog.setContentView(view)
            dialog.show()
            val prefManager = PrefManager(context)
            prefManager.lastAppVersionRemoteConfigDataFetchTime=System.currentTimeMillis()

            view.findViewById<ImageView>(R.id.ivClose).setOnClickListener { dialog.dismiss() }
            view.findViewById<Button>(R.id.btnUpdate).setOnClickListener {
                dialog.dismiss()
                Utils.goToPlayStore(context)
            }
        }
        catch (e:Exception){
            e.printStackTrace()
        }

    }
}
