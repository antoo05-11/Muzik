package com.example.muzik.ui.stream_inside_activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.muzik.databinding.ActivityStreamShareBinding
import com.example.muzik.ui.main_activity.MainActivity
import com.example.muzik.utils.printLogcat
import io.socket.client.Socket

class StreamShareActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStreamShareBinding
    private lateinit var mSocket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStreamShareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mSocket = MainActivity.mSocket

        val roomID = intent.extras?.getString("roomID")
        binding.roomNameTextView.text = "Room ${roomID}"

        binding.quitRoomButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        mSocket.on("newUser") {
            printLogcat("newUser" + it[0].toString())
        }


    }
}