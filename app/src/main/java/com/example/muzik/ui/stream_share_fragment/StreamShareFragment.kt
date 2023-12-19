package com.example.muzik.ui.stream_share_fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.muzik.data_model.standard_model.Song
import com.example.muzik.databinding.FragmentStreamShareBinding
import com.example.muzik.storage.SharedPrefManager
import com.example.muzik.ui.main_activity.LoginActivity
import com.example.muzik.ui.main_activity.MainActivity
import com.example.muzik.ui.stream_inside_activity.StreamShareActivity
import com.example.muzik.utils.printLogcat
import io.socket.client.Socket
import kotlinx.coroutines.launch

class StreamShareFragment : Fragment() {

    private lateinit var viewModel: StreamShareViewModel
    private lateinit var binding: FragmentStreamShareBinding
    private lateinit var mSocket: Socket
    private lateinit var roomID: String
    private var userID: Long? = -1

    private var inStreamShare = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStreamShareBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[StreamShareViewModel::class.java]

        mSocket = MainActivity.mSocket

        binding.createRoomButton.setOnClickListener {
            if (SharedPrefManager.getInstance(requireContext()).isAccessTokenExpired ||
                SharedPrefManager.getInstance(requireContext()).user.userID == -1L
            ) {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                requireActivity().startActivity(intent)
            } else {
                printLogcat("createRoom" + requireActivity().toString())
                userID =
                    SharedPrefManager.getInstance(requireActivity().applicationContext).user.userID
                mSocket.emit("createRoom", userID)
            }
        }

        binding.joinButton.setOnClickListener {
            it.isClickable = false
            if (SharedPrefManager.getInstance(requireContext()).isAccessTokenExpired ||
                SharedPrefManager.getInstance(requireContext()).user.userID == -1L
            ) {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                requireActivity().startActivity(intent)
                it.isClickable = false
            } else {
                if (!inStreamShare) return@setOnClickListener
                userID = SharedPrefManager.getInstance(requireContext()).user.userID
                roomID = binding.roomIdInputText.text.toString()
                mSocket.emit("joinRoom", roomID, userID)
            }
        }

        mSocket.on("roomCreated") { args ->
            activity?.runOnUiThread {
                if (args.isNotEmpty()) roomID = args[0].toString()
                val intent =
                    Intent(
                        requireActivity().applicationContext,
                        StreamShareActivity::class.java
                    )
                intent.putExtra("roomID", roomID)
                requireActivity().startActivity(intent)
            }
        }

        mSocket.on("playSong") {
            activity?.runOnUiThread {
                lifecycleScope.launch {
                    try {
                        val song = MainActivity.muzikAPI.getSong(it[0].toString().toInt()).body()
                            ?.let { it1 -> Song.buildOnline(it1) }
                        printLogcat(song.toString())
                        if (song != null) {
                            MainActivity.musicService?.setSong(song)
                        }
                    } catch (e: Throwable) {
                        Log.e("NETWORK_ERROR", "Network error!")
                    }
                }
            }
        }

        mSocket.on("roomJoin") {
            inStreamShare = true
            activity?.runOnUiThread {
                if (it[0] == false) {
                    printLogcat("room not exist")
                } else {
                    val intent = Intent(requireContext(), StreamShareActivity::class.java).apply {
                        putExtra("roomID", roomID)
                    }
                    requireActivity().startActivity(intent)
                }
            }
        }

        mSocket.on("continueSong") {
            activity?.runOnUiThread {
                lifecycleScope.launch {
                    try {
                        val song = MainActivity.muzikAPI.getSong(it[0].toString().toInt()).body()
                            ?.let { it1 -> Song.buildOnline(it1) }
                        if (song != null) {
                            MainActivity.musicService?.setSong(song)
                            printLogcat(it[1].toString())
                            MainActivity.musicService?.exoPlayer?.seekTo(it[1].toString().toLong())
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