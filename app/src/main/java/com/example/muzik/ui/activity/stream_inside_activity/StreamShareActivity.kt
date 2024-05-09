package com.example.muzik.ui.activity.stream_inside_activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import com.example.muzik.ui.activity.main_activity.MainActivity
import com.example.muzik.ui.bottom_sheet_dialog.stream_list.StreamListBottomSheet
import com.example.muzik.ui.fragment.player_view_fragment.PlayerViewModel
import com.example.muzik.ui.fragment.stream_share_fragment.StreamShareFragment
import com.example.muzik.utils.PaletteUtils
import com.example.muzik.utils.setRotateAnimation
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject


class StreamShareActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStreamShareBinding
    private lateinit var mSocket: Socket

    private lateinit var playerViewModel: PlayerViewModel

    private lateinit var comments: MutableList<Comment>
    private lateinit var commentsAdapter: CommentsAdapter

    private lateinit var streamListBottomSheet: StreamListBottomSheet

    val streamList = MainActivity.streamList

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStreamShareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sb.isEnabled = false

        playerViewModel = MainActivity.playerViewModel
        mSocket = MainActivity.mSocket

        binding.roomCreatorNameTextView.text =
            SharedPrefManager.getInstance(this).user.name.toString()

        val roomID = intent.extras?.getString("roomID")
        binding.roomNameTextView.text = "Room ID: $roomID"

        comments = mutableListOf()
        commentsAdapter = CommentsAdapter()

        binding.rcvComment.layoutManager = LinearLayoutManager(this)
        (binding.rcvComment.layoutManager as LinearLayoutManager).stackFromEnd = true
        binding.rcvComment.adapter = commentsAdapter

        configSocketListener()

        binding.sendButton.setOnClickListener {
            binding.rcvComment.scrollToPosition(comments.size - 1)
            val message = binding.commentEditText.text.toString()
            mSocket.emit(
                "messageToRoom",
                roomID,
                SharedPrefManager.getInstance(this).user.userID,
                message
            )
            binding.commentEditText.text.clear()
        }

        playerViewModel.songMutableLiveData.observe(this) {
            Picasso.get()
                .load(it.imageURI)
                .into(binding.activityTrackImage, object : Callback {
                    override fun onSuccess() {
                        binding.root.background = PaletteUtils.getDominantGradient(
                            bitmap = (binding.activityTrackImage.drawable as BitmapDrawable).bitmap
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

        streamListBottomSheet = StreamListBottomSheet()
        binding.openStreamListButton.setOnClickListener {
            streamListBottomSheet.show(supportFragmentManager, StreamListBottomSheet.TAG)
        }

        val anim = setRotateAnimation(binding.activityTrackImage)
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

        binding.hideRoomButton.setOnClickListener {
            setResult(1, Intent().apply {
                putExtra("streamList", streamList.toLongArray())
            })
            onBackPressedDispatcher.onBackPressed()
        }

        binding.outRoomButton.setOnClickListener {
            StreamShareFragment.inStreamShare.value = false
            finish()
        }

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun configSocketListener() {
        mSocket.on("updateSongList") {
            runOnUiThread {
                val jsonArray = it[0] as JSONArray

                val songList: MutableList<Long> = mutableListOf()
                for (i in 0 until jsonArray.length()) {
                    val songId = jsonArray.getLong(i)
                    songList.add(songId)
                }
                MainActivity.streamList.clear()
                MainActivity.streamList.addAll(songList)
            }
        }

        mSocket.on("messageFromRoom") {
            runOnUiThread {
                val user = it[0] as JSONObject
                comments.add(
                    Comment(
                        username = user.getString("username"),
                        comment = it[1].toString()
                    )
                )
                commentsAdapter.updateList(comments)
                binding.rcvComment.scrollToPosition(comments.size - 1)
            }
        }

        binding.streamViewTextView.text = intent.extras?.getString("roomSize", "1").toString()

        mSocket.on("newUser") {
            runOnUiThread {
                binding.streamViewTextView.text = it[1].toString()
            }
        }

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