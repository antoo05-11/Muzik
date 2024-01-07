package com.example.muzik.ui.fragment.stream_share_fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.muzik.data_model.standard_model.Song
import com.example.muzik.databinding.FragmentStreamShareBinding
import com.example.muzik.storage.SharedPrefManager
import com.example.muzik.ui.activity.login_activity.LoginActivity
import com.example.muzik.ui.activity.main_activity.MainActivity
import com.example.muzik.ui.activity.main_activity.MainActivity.Companion.musicService
import com.example.muzik.ui.activity.main_activity.MainActivity.Companion.streamList
import com.example.muzik.ui.activity.stream_inside_activity.StreamShareActivity
import com.example.muzik.ui.fragment.player_view_fragment.PlayerViewModel
import io.socket.client.Socket
import kotlinx.coroutines.launch
import org.json.JSONArray

class StreamShareFragment : Fragment() {

    private lateinit var viewModel: StreamShareViewModel
    private lateinit var binding: FragmentStreamShareBinding
    private lateinit var mSocket: Socket
    private lateinit var playerViewModel: PlayerViewModel
    private var userID: Long? = -1
    private lateinit var intent: Intent

    companion object {
        var inStreamShare = MutableLiveData(false)
        var roomID: String = ""
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        if (inStreamShare.value == true) binding.createRoomButton.text = "Return your room now"
        else binding.createRoomButton.text = "Create your new room"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStreamShareBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[StreamShareViewModel::class.java]
        playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
        mSocket = MainActivity.mSocket

        intent =
            Intent(requireActivity().applicationContext, StreamShareActivity::class.java).apply {
                addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT)
            }

        binding.createRoomButton.setOnClickListener {
            if (SharedPrefManager.getInstance(requireContext()).isAccessTokenExpired ||
                SharedPrefManager.getInstance(requireContext()).user.userID == -1L
            ) {
                val loginIntent = Intent(requireContext(), LoginActivity::class.java)
                requireActivity().startActivity(loginIntent)
            } else {
                if (inStreamShare.value == true) {
                    intent.putExtra("roomID", roomID)
                    requireActivity().startActivity(intent)
                } else {
                    userID =
                        SharedPrefManager.getInstance(requireActivity().applicationContext).user.userID
                    mSocket.emit("createRoom", userID)
                    inStreamShare.value = true
                }
            }
        }

        binding.joinButton.setOnClickListener {
            if (SharedPrefManager.getInstance(requireContext()).isAccessTokenExpired ||
                SharedPrefManager.getInstance(requireContext()).user.userID == -1L
            ) {
                val loginIntent = Intent(requireContext(), LoginActivity::class.java)
                requireActivity().startActivity(loginIntent)
            } else {
                if (inStreamShare.value == true && roomID == binding.roomIdInputText.text.toString()) {
                    intent.putExtra("roomID", roomID)
                    requireActivity().startActivity(intent)
                } else {
                    inStreamShare.value = true
                    userID = SharedPrefManager.getInstance(requireContext()).user.userID
                    roomID = binding.roomIdInputText.text.toString()
                    intent.putExtra("roomID", roomID)
                    mSocket.emit("joinRoom", roomID, userID)
                }
            }
        }

        mSocket.on("roomCreated") {
            activity?.runOnUiThread {
                inStreamShare.value = true
                userID = SharedPrefManager.getInstance(requireContext()).user.userID
                roomID = it[0].toString()
                intent.putExtra("roomID", roomID)
                mSocket.emit("joinRoom", roomID, userID)
            }
        }

        mSocket.on("roomJoin") {
            activity?.runOnUiThread {
                if (it[0] == false) {
                    inStreamShare.value = false
                    Toast.makeText(context, "Room does not exist", Toast.LENGTH_SHORT).show()
                } else {
                    intent.putExtra("roomSize", it[1].toString())
                    requireActivity().startActivity(intent)
                }
            }
        }

        mSocket.on("playSong") {
            activity?.runOnUiThread {
                lifecycleScope.launch {
                    try {
                        val song = MainActivity.muzikAPI.getSong(it[0].toString()).body()
                            ?.let { it1 -> Song.buildOnline(it1) }
                        if (song != null) {
//                            playerViewModel.stop()
//                            song.songURI?.let { songURI -> playerViewModel.setMedia(songURI) }
                            playerViewModel.setSong(song)
                        }
                    } catch (e: Throwable) {
                        Log.e("NETWORK_ERROR", "Network error!")
                    }
                }
            }
        }

        mSocket.on("updateSongList") {
            activity?.runOnUiThread {
                val jsonArray = it[0] as JSONArray

                val songList: MutableList<Long> = mutableListOf()
                for (i in 0 until jsonArray.length()) {
                    val songId = jsonArray.getLong(i)
                    songList.add(songId)
                }
                streamList.clear()
                streamList.addAll(songList)
            }
        }

        mSocket.on("continueSong") {
            activity?.runOnUiThread {
                lifecycleScope.launch {
                    try {
                        val song = MainActivity.muzikAPI.getSong(it[0].toString()).body()
                            ?.let { it1 -> Song.buildOnline(it1) }
                        if (song != null) {
                            playerViewModel.setSong(song)
                            musicService?.exoPlayer?.seekTo(it[1].toString().toLong())
                        }
                    } catch (e: Throwable) {
                        Log.e("NETWORK_ERROR", "Network error!")
                    }
                }
            }
        }

        return binding.root
    }
}