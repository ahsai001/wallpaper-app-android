package com.ahsailabs.wallpaperapp

import android.app.WallpaperManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import coil.load
import com.ahsailabs.wallpaperapp.databinding.ActivityDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
import java.net.URL

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val link = intent.getStringExtra("wallpaper")
        val linkOri = intent.getStringExtra("wallpaperOri")
        binding.ivHome.load(link)

        binding.mbSetWallpaper.setOnClickListener {
            val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(this@DetailActivity)
            lifecycleScope.launch(Dispatchers.IO) {
                val inputStream: InputStream = URL(linkOri).openStream()
                wallpaperManager.setStream(inputStream)
            }
        }

    }
}