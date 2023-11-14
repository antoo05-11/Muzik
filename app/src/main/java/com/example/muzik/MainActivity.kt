package com.example.muzik

//import android.media.AudioManager
//import android.media.MediaPlayer
//import android.os.Bundle
//import android.view.MenuItem
//import androidx.appcompat.app.AppCompatActivity
//import androidx.navigation.findNavController
//import androidx.navigation.ui.AppBarConfiguration
//import androidx.navigation.ui.setupActionBarWithNavController
//import androidx.navigation.ui.setupWithNavController
//import com.example.muzik.api_controller.MuzikAPI
//import com.example.muzik.api_controller.RetrofitHelper
//import com.example.muzik.databinding.ActivityMainBinding
//import com.google.android.material.bottomnavigation.BottomNavigationView
//import kotlinx.coroutines.DelicateCoroutinesApi
//
//
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityMainBinding
//
//    companion object {
//        val mp = MediaPlayer()
//        var flag = false
//
//
//        val muzikAPI: MuzikAPI = RetrofitHelper.getInstance().create(MuzikAPI::class.java)
//
//        @JvmStatic
//        fun setUpStream(streamURL: String?) {
//            try {
//                mp.setAudioStreamType(AudioManager.STREAM_MUSIC)
//                mp.setDataSource(streamURL)
//                mp.prepareAsync()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//            mp.setOnErrorListener { mediaPlayer: MediaPlayer, what: Int, extra: Int ->
//                mediaPlayer.reset()
//                false
//            }
//            mp.setOnPreparedListener { mediaPlayer: MediaPlayer? ->
//                flag = true
//            }
//        }
//    }
//
//    @OptIn(DelicateCoroutinesApi::class)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val navView: BottomNavigationView = binding.navView
//
//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
//
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//
//        setUpStream("https://muzik-server-uet.onrender.com/api/song/5/stream")
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == android.R.id.home) {
//            onBackPressed()
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }
//}

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.media3.exoplayer.ExoPlayer
import com.example.muzik.api_controller.MuzikAPI
import com.example.muzik.api_controller.RetrofitHelper
import com.example.muzik.databinding.ActivityMainBinding
import com.example.muzik.viewmodel.ArtistViewModel
import com.example.muzik.viewmodel.MainActivityViewModel
import com.example.muzik.viewmodel.PlayerViewModel
import com.example.muzik.viewmodel.SongViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var playerViewModel: PlayerViewModel

    private lateinit var mainActivityViewModel: MainActivityViewModel

    private lateinit var songViewModel: SongViewModel

    private lateinit var artistViewModel: ArtistViewModel

    companion object {
        val muzikAPI: MuzikAPI = RetrofitHelper.getInstance().create(MuzikAPI::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        playerViewModel = ViewModelProvider(this)[PlayerViewModel::class.java]
        mainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        songViewModel = ViewModelProvider(this)[SongViewModel::class.java]
        artistViewModel = ArtistViewModel(this)[ArtistViewModel::class.java]

        val player = ExoPlayer.Builder(this).build()
        playerViewModel.exoPlayerMutableLiveData.value = player
        playerViewModel.addListener()

        val storagePermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    songViewModel.fetchSong(this)
                } else {
                    Log.d("ActivityMain", "Failure")
                }
            }

        storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        setContentView(binding.root)
    }
}