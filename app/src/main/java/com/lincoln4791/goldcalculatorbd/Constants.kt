package com.lincoln4791.goldcalculatorbd

object Constants {
    const val GOLD_PRICE_LINK = "https://goldprice.org/"
    const val PLAY_STORE_APP_LINK = "https://play.google.com/store/apps/details?id=com.lincoln4791.goldcalculatorbd"
    const val APP_VERSION = "appVersion"
    const val BANNER_AD_INTERVAL = "banner_add_interval"
    const val INTER_AD_INTERVAL = "inter_add_interval"
    const val AD_TYPE_BANNER = "ad_type_banner"
    const val AD_TYPE_INTER = "ad_type_inter"

    const val SYNC_CHECKING_INTERVAL = 900000   //   15 minutes sync interval ::: 60000 milisecond =  minute
    const val INTERVAL = 120000   //  86400000 = 24hour hours interval ::: 60000 milisecond =  minute ::: Cancel,Grace,Paused,expired,Hold
    const val INTERVAL_SHORT = 120000   //  21600000 = 6 hours interval ::: 3600000 milisecond =  hour //::: Wrong User1
    const val INTERVAL_LONG = 120000   //  21600000 = 6 hours interval ::: 3600000 milisecond =  hour
    const val SUBSCRIPTION_STATUS_DIALOG_SHOWN_MAX_DAYS = 604800000   //  604800000 = 7 Day interval ::: 3600000 milisecond =  hour :::
    const val SUBSCRIPTION_STATUS_BANNER_SHOWN_INTERVAL_DAYS = 120000   //  300000  = 5 min interval ::: 3600000 milisecond =  hour :::
    const val INTERVAL_MONTHLY = 2592000000   //  300000  = 5 min interval ::: 3600000 milisecond =  hour :::
    const val INTERVAL_DAILY = 86400000   //  300000  = 5 min interval ::: 3600000 milisecond =  hour :::
    const val INTERVAL_WEEKLY = 604800000   //  300000  = 5 min interval ::: 3600000 milisecond =  hour :::
    const val INTERVAL_HOURLY= 3600000   //  300000  = 5 min interval ::: 3600000 milisecond =  hour :::

}