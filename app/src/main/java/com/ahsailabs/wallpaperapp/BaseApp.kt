package com.ahsailabs.wallpaperapp

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.util.CoilUtils
import coil.util.DebugLogger
import okhttp3.OkHttpClient

/**
 * Created by ahmad s on 10/12/21.
 */
class BaseApp: Application() {
    override fun onCreate() {
        super.onCreate()

        val imageLoader = ImageLoader.Builder(this)
            //.crossfade(true)
            //.placeholder(R.drawable.logo_puhba)
            //.error(R.drawable.logo_puhba)
            //.fallback(R.drawable.logo_puhba)
            .okHttpClient {
                OkHttpClient.Builder()
                    .cache(CoilUtils.createDefaultCache(this))
                    .build()
            }.let {
                if(BuildConfig.DEBUG){
                    it.logger(DebugLogger())
                }else{
                    it
                }
            }.build()
        Coil.setImageLoader(imageLoader)
    }
}