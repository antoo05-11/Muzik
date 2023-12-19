package com.example.muzik.ui.main_activity

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.muzik.R
import com.example.muzik.api_controller.MuzikAPI
import com.example.muzik.api_controller.RetrofitHelper
import com.example.muzik.databinding.ActivityMainBinding
import com.example.muzik.music_service.LocalMusicRepository
import com.example.muzik.music_service.MusicService
import com.example.muzik.storage.SharedPrefManager
import com.example.muzik.ui.lib_artist_fragment.ArtistViewModel
import com.example.muzik.ui.lib_song_fragment.SongViewModel
import com.example.muzik.ui.player_view_fragment.PlayerViewModel
import com.example.muzik.ui.search_fragment.SearchViewModel
import com.example.muzik.utils.printLogcat
import io.socket.client.IO
import io.socket.client.Socket


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    private lateinit var mainActivityViewModel: MainActivityViewModel

    private lateinit var songViewModel: SongViewModel

    private lateinit var artistViewModel: ArtistViewModel

    private lateinit var searchViewModel: SearchViewModel

    private lateinit var mainNavController: NavController


    companion object {
        val muzikAPI: MuzikAPI = RetrofitHelper.getInstance().create(MuzikAPI::class.java)
        var musicService: MusicService? = null
        val mSocket: Socket = IO.socket("http://10.0.2.2:6600")

        lateinit var playerViewModel: PlayerViewModel
    }

    private var isServiceConnected = false

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MyBinder
            musicService = binder.getService()
            isServiceConnected = true

            playerViewModel.exoPlayerMutableLiveData.value = musicService?.exoPlayer
            playerViewModel.addListener()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicService = null
            isServiceConnected = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSocket.connect()

        binding = ActivityMainBinding.inflate(layoutInflater)

        playerViewModel = ViewModelProvider(this)[PlayerViewModel::class.java]
        mainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        songViewModel = ViewModelProvider(this)[SongViewModel::class.java]
        artistViewModel = ArtistViewModel(this)[ArtistViewModel::class.java]
        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        val mainNavHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_main_nav) as NavHostFragment
        mainNavController = mainNavHostFragment.navController

        val storagePermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    LocalMusicRepository.fetchSong(applicationContext)
                }
            }

        storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val intent = Intent(this, MusicService::class.java).also { intent ->
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
        startService(intent)

        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()

        mSocket.disconnect()
        mSocket.off("off")
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.account_button_item -> {
                printLogcat(SharedPrefManager.getInstance(applicationContext).user.userID.toString())
                if (SharedPrefManager.getInstance(applicationContext).isAccessTokenExpired ||
                    SharedPrefManager.getInstance(applicationContext).user.userID == -1L
                ) {
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    this.startActivity(intent)
                }
            }

            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }
}