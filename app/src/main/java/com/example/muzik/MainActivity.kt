package com.example.muzik

import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.muzik.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.DelicateCoroutinesApi
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    private val mp = MediaPlayer()
    private var flag = false
    private fun setUpStream(streamURL: String?) {
        try {
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mp.setDataSource(streamURL)
            mp.prepareAsync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mp.setOnErrorListener(MediaPlayer.OnErrorListener { mediaPlayer: MediaPlayer, what: Int, extra: Int ->
            mediaPlayer.reset()
            false
        })
        mp.setOnPreparedListener(OnPreparedListener { mediaPlayer: MediaPlayer? ->
            flag = true
            Toast.makeText(applicationContext, flag.toString(), Toast.LENGTH_SHORT).show()
        })
    }

    companion object {
        private const val serverURL: String = "https://muzik-server-uet.onrender.com/api/"
        private var client = OkHttpClient()
        val quotesApi: QuotesApi = RetrofitHelper.getInstance().create(QuotesApi::class.java)

        @JvmStatic
        fun get(): String {
            val request: Request = Request.Builder()
                .url(serverURL + "album/get")
                .build()
            try {
                client.newCall(request).execute().use { response ->
                    return response.body!!.string()
                }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
    }

    private lateinit var response: String

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        binding.playButton.setOnClickListener { v ->
            if (flag) mp.start()
            Toast.makeText(applicationContext, flag.toString(), Toast.LENGTH_SHORT).show()
        }

        setUpStream("https://muzik-server-uet.onrender.com/api/song/4/stream")


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}