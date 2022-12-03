package com.lincoln4791.goldcalculatorbd

import android.content.Context
import android.content.SharedPreferences

class PrefManager(val context : Context) {

    private var pref: SharedPreferences
    private var editor: SharedPreferences.Editor
    private var PRIVATE_MODE = 0

    init {
        pref = context.getSharedPreferences("GoldCalculatorBD", PRIVATE_MODE)
        editor = pref.edit()
    }


    var fcmToken:String?
        get() = pref.getString("fcmToken","")
        set(value) {editor.putString("fcmToken",value).commit()}


    var isAppInstalledFirstTime:Boolean?
        get() = pref.getBoolean("isAppInstalledFirstTime",true)
        set(value) {editor.putBoolean("isAppInstalledFirstTime", value!!).commit()}

    var appVersion:String?
        get() = pref.getString("appVersion","1")
        set(value) {editor.putString("appVersion",value).commit()}

    var appInstallDate:String
        get() = pref.getString("appInstallDate","")!!
        set(value) {editor.putString("appInstallDate",value).commit()}

    var versionControlCheckLastDate:String
        get() = pref.getString("versionControlCheckLastDate", "2021-08-10")!!
        set(value) {editor.putString("versionControlCheckLastDate",value).commit()}

    var bannerAdInterval:Long
        get() = pref.getLong("bannerAdInterval", 60000)
        set(value) {editor.putLong("bannerAdInterval",value).commit()}

    var interAdInterval:Long
        get() = pref.getLong("interAdInterval", 300000)
        set(value) {editor.putLong("interAdInterval",value).commit()}






    // ads
    var lastBannerAdShownHomeF:Long
        get() = pref.getLong("lastBannerAdShownHomeF",1633026748000)
        set(value) {editor.putLong("lastBannerAdShownHomeF",value).commit()}

    var lastBannerAdShownDB:Long
        get() = pref.getLong("lastBannerAdShownDB",1633026748000)
        set(value) {editor.putLong("lastBannerAdShownDB",value).commit()}

    var lastBannerAdShownAdSub:Long
        get() = pref.getLong("lastBannerAdShownAdSub",1633026748000)
        set(value) {editor.putLong("lastBannerAdShownAdSub",value).commit()}

    var lastBannerAdShownDollarRate:Long
        get() = pref.getLong("lastBannerAdShownAdDollarRate",1633026748000)
        set(value) {editor.putLong("lastBannerAdShownAdDollarRate",value).commit()}

    var lastBannerAdShownGBP:Long
        get() = pref.getLong("lastBannerAdShownAdGBP",1633026748000)
        set(value) {editor.putLong("lastBannerAdShownAdGBP",value).commit()}

    var lastBannerAdShownGPB:Long
        get() = pref.getLong("lastBannerAdShownAdGPB",1633026748000)
        set(value) {editor.putLong("lastBannerAdShownAdGPB",value).commit()}

    var lastBannerAdShownGPI:Long
        get() = pref.getLong("lastBannerAdShownAdGPI",1633026748000)
        set(value) {editor.putLong("lastBannerAdShownAdGPI",value).commit()}

    var lastBannerAdShownGSP:Long
        get() = pref.getLong("lastBannerAdShownAdGSP",1633026748000)
        set(value) {editor.putLong("lastBannerAdShownAdGSP",value).commit()}

    var lastBannerAdShownWC:Long
        get() = pref.getLong("lastBannerAdShownAdWC",1633026748000)
        set(value) {editor.putLong("lastBannerAdShownAdWC",value).commit()}




    var lastInterstitialAdShown:Long
        get() = pref.getLong("lastInterstitialAdShown",300000)
        set(value) {editor.putLong("lastInterstitialAdShown",value).commit()}

/*    var lastInterstitialAdShownHome:String
        get() = pref.getString("lastInterstitialAdShownHome","")!!
        set(value) {editor.putString("lastInterstitialAdShownHome",value).commit()}

    var lastRewardedAdHomeShownTime:Long
        get() = pref.getLong("lastRewardedAdHomeShownTime", 1640973600000)// 1st January 2022
        set(value) {editor.putLong("lastRewardedAdHomeShownTime",value).commit()}

    var lastRewardedAdASubscriptionShownTime:Long
        get() = pref.getLong("lastRewardedAdSubscriptionShownTime", 1640973600000)// 1st January 2022
        set(value) {editor.putLong("lastRewardedAdSubscriptionShownTime",value).commit()}*/













    //Common
    var lastCommonRemoteConfigDataFetchTime:Long
        get() = pref.getLong("lastCommonRemoteConfigDataFetchTime", 1640973600000)// 1st January 2022
        set(value) {editor.putLong("lastCommonRemoteConfigDataFetchTime",value).commit()}

    var lastAppVersionRemoteConfigDataFetchTime:Long
        get() = pref.getLong("lastAppVersionRemoteConfigDataFetchTime", 1640973600000)// 1st January 2022
        set(value) {editor.putLong("lastAppVersionRemoteConfigDataFetchTime",value).commit()}



}