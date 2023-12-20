package com.example.muzik.ui.stream_inside_activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muzik.R
import com.example.muzik.adapter.CommentsAdapter
import com.example.muzik.data_model.standard_model.Comment
import com.example.muzik.databinding.ActivityStreamShareBinding
import com.example.muzik.storage.SharedPrefManager
import com.example.muzik.ui.main_activity.MainActivity
import com.example.muzik.ui.player_view_fragment.PlayerViewModel
import com.example.muzik.ui.stream_share_fragment.StreamShareFragment
import com.example.muzik.utils.PaletteUtils
import com.example.muzik.utils.printLogcat
import com.example.muzik.utils.setRotateAnimation
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.socket.client.Socket
import org.json.JSONObject


class StreamShareActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStreamShareBinding
    private lateinit var mSocket: Socket

    private lateinit var playerViewModel: PlayerViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStreamShareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerViewModel = MainActivity.playerViewModel

        binding.roomCreatorNameTextView.text =
            SharedPrefManager.getInstance(this).user.name.toString()

        mSocket = MainActivity.mSocket

        val roomID = intent.extras?.getString("roomID")
        binding.roomNameTextView.text = "Room ID: ${roomID}"

        val comments = mutableListOf<Comment>()
        val adapter = CommentsAdapter(comments)

        binding.streamViewTextView.text = "1"

        binding.sendButton.setOnClickListener {
            comments.add(
                Comment(
                    username = SharedPrefManager.getInstance(this).user.name.toString(),
                    comment = binding.commentEditText.text.toString()
                )
            )
            adapter.notifyDataSetChanged()
            binding.rcvComment.scrollToPosition(comments.size - 1)
            mSocket.emit(
                "messageToRoom",
                roomID,
                SharedPrefManager.getInstance(this).user.userID,
                binding.commentEditText.text
            )
            binding.commentEditText.text.clear()
        }

        binding.rcvComment.layoutManager = LinearLayoutManager(this)
        (binding.rcvComment.layoutManager as LinearLayoutManager).stackFromEnd = true
        binding.rcvComment.adapter = adapter

        mSocket.on("messageFromRoom") {
            runOnUiThread {
                val user = it[0] as JSONObject
                comments.add(
                    Comment(
                        username = user.getString("username"),
                        comment = it[1].toString()
                    )
                )
                adapter.notifyDataSetChanged()
                binding.rcvComment.scrollToPosition(comments.size - 1)
            }
        }

        val anim = setRotateAnimation(binding.activityTrackImage)

        binding.hideRoomButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.outRoomButton.setOnClickListener {
            StreamShareFragment.inStreamShare = false
            finish()
        }

        mSocket.on("newUser") {
            printLogcat("newUser" + it[0].toString())
            binding.streamViewTextView.text = it[1].toString()
        }


        binding.sb.isEnabled = false

        playerViewModel.songMutableLiveData.observe(this) {
            Picasso.get()
                .load(it.imageURI)
                .into(binding.activityTrackImage, object : Callback {
                    override fun onSuccess() {
                        binding.root.background = PaletteUtils.getDominantGradient(
                            (binding.activityTrackImage.drawable as BitmapDrawable).bitmap,
                            null,
                            null,
                            null
                        )
                    }

                    override fun onError(e: Exception?) {
                    }

                })

            binding.tvArtistName.text = it.artistName
        }
        binding.loveSongButton.setOnClickListener {
            val currentBackground = binding.loveSongButton.background

            if (currentBackground.constantState == ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.icon_heart
                )?.constantState
            ) {
                binding.loveSongButton.setBackgroundResource(R.drawable.icon_full_heart)
            } else {
                binding.loveSongButton.setBackgroundResource(R.drawable.icon_heart)
            }
        }

        playerViewModel.playingMutableLiveData.observe(this) {
            if (it) {
                anim.resume()
            } else {
                anim.pause()
            }
        }

        playerViewModel.songMutableLiveData.observe(this) {
            binding.tvTitle.text = it.name
            binding.sb.max = it.duration
        }

        playerViewModel.currentTimeMutableLiveData.observe(this) {
            binding.sb.progress = it
        }

        binding.sb.setOnSeekBarChangeListener(
            PlayerViewModel.OnSeekBarChangeListener(
                playerViewModel
            )
        )
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm =
                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}