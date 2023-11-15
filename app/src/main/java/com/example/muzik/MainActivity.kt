package com.example.muzik

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