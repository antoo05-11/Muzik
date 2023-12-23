package com.example.muzik.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.muzik.R
import com.example.muzik.adapter.SongsAdapterVertical.SongPreviewHolder
import com.example.muzik.data_model.standard_model.Song
import com.example.muzik.ui.main_activity.MainActivity.Companion.muzikAPI
import com.example.muzik.ui.player_view_fragment.PlayerViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

open class SongsAdapterVertical(protected var songsPreviewList: List<Song>) :
    RecyclerView.Adapter<SongPreviewHolder>() {

    protected var playerViewModel: PlayerViewModel? = null
    private var hasItemIndexTextView = false
    private var hasViewsShowed = false
    private var fragmentOwner: Fragment? = null
    private var playingGifView: LottieAnimationView? = null

    fun hasItemIndexTextView(): SongsAdapterVertical {
        hasItemIndexTextView = true
        return this
    }

    fun hasViewsShowed(): SongsAdapterVertical {
        hasViewsShowed = true
        return this
    }

    fun setFragmentOwner(fragmentOwner: Fragment?): SongsAdapterVertical {
        this.fragmentOwner = fragmentOwner
        return this
    }

    fun setPlayerViewModel(playerViewModel: PlayerViewModel?): SongsAdapterVertical {
        this.playerViewModel = playerViewModel
        return this
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongPreviewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return SongPreviewHolder(view)
    }

    override fun onBindViewHolder(holder: SongPreviewHolder, position: Int) {
        val song = songsPreviewList[position]
        if (song.songID != null) {
            holder.tvSongName.text = song.name
            if (!hasViewsShowed) holder.artistNameSongPreviewTextview.text = song.artistName else {
                val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
                holder.artistNameSongPreviewTextview.text = numberFormat.format(song.views)
            }
            holder.artistNameSongPreviewTextview.setBackgroundColor(Color.TRANSPARENT)
            holder.tvSongName.setBackgroundColor(Color.TRANSPARENT)
            holder.shimmerArtistSongPreviewNameTextView.hideShimmer()
            holder.shimmerSongPreviewNameTextView.hideShimmer()
            if (song.imageURI == null) {
                holder.shimmerSongImageItem.hideShimmer()
                holder.songImageItem.setBackgroundResource(R.drawable.icons8_song_50)
            }
            Picasso.get()
                .load(song.imageURI)
                .into(holder.songImageItem, object : Callback {
                    override fun onSuccess() {
                        holder.shimmerSongImageItem.hideShimmer()
                    }

                    override fun onError(e: Exception) {}
                })
            if (hasItemIndexTextView) {
                holder.itemIndexTextView.text = (position + 1).toString()
            }
            if (playerViewModel!!.songMutableLiveData.value != null && playerViewModel!!.songMutableLiveData.value!!.songID == song.songID) {
                setPlayingEffect(holder)
            }
            holder.itemView.setOnClickListener {
                if (song.songURI == null) {
                    fragmentOwner?.lifecycleScope?.launch {
                        muzikAPI.getSong(song.songID, youtube = true).body()?.let {
                            song.songURI = Uri.parse(it.songURL)
                            song.duration = it.duration
                            if (playerViewModel!!.songMutableLiveData.value == null ||
                                playerViewModel!!.songMutableLiveData.value!!.songID != song.songID
                            ) {
                                playerViewModel!!.stop()
                                playerViewModel!!.setMedia(song.songURI!!)
                                playerViewModel!!.setSong(song)
                                setPlayingEffect(holder)
                            }
                        }
                    }
                } else {
                    if (playerViewModel!!.songMutableLiveData.value == null ||
                        playerViewModel!!.songMutableLiveData.value!!.songID != song.songID
                    ) {
                        playerViewModel!!.stop()
                        playerViewModel!!.setMedia(song.songURI!!)
                        playerViewModel!!.setSong(song)
                        setPlayingEffect(holder)
                    }
                }
            }
        }
    }

    @SuppressLint("CutPasteId")
    private fun setPlayingEffect(holder: SongPreviewHolder) {
        if (playingGifView != null) {
            ((playingGifView!!.parent as LinearLayout).findViewById<View>(R.id.song_preview_name_textview) as TextView).typeface =
                Typeface.defaultFromStyle(Typeface.NORMAL)
            ((playingGifView!!.parent as LinearLayout).findViewById<View>(R.id.song_preview_name_textview) as TextView).ellipsize =
                TextUtils.TruncateAt.END
            (playingGifView!!.parent as LinearLayout).removeView(playingGifView)
        }
        playingGifView = LottieAnimationView(fragmentOwner!!.requireContext())
        val widthInDp = 20
        val heightInDp = 20
        val rightMarginInDp = 10
        val layoutParams = LinearLayout.LayoutParams(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                widthInDp.toFloat(),
                fragmentOwner!!.resources.displayMetrics
            ).toInt(), TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                heightInDp.toFloat(),
                fragmentOwner!!.resources.displayMetrics
            ).toInt()
        )
        layoutParams.rightMargin = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            rightMarginInDp.toFloat(),
            fragmentOwner!!.resources.displayMetrics
        ).toInt()
        playingGifView!!.repeatCount = LottieDrawable.INFINITE
        playingGifView!!.layoutParams = layoutParams
        playingGifView!!.setAnimation(R.raw.playing_gif)
        playingGifView!!.playAnimation()
        (holder.tvSongName.parent.parent as LinearLayout).addView(playingGifView, 0)
        holder.tvSongName.ellipsize = TextUtils.TruncateAt.MARQUEE
        holder.tvSongName.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
    }

    override fun getItemCount(): Int {
        return songsPreviewList.size
    }

    class SongPreviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var shimmerSongImageItem: ShimmerFrameLayout
        var shimmerArtistSongPreviewNameTextView: ShimmerFrameLayout
        var shimmerSongPreviewNameTextView: ShimmerFrameLayout
        var artistNameSongPreviewTextview: TextView
        var tvSongName: TextView
        var songImageItem: ImageView
        var itemIndexTextView: TextView

        init {
            tvSongName = itemView.findViewById(R.id.song_preview_name_textview)
            shimmerSongImageItem = itemView.findViewById(R.id.shimmer_song_preview_image_item)
            shimmerArtistSongPreviewNameTextView =
                itemView.findViewById(R.id.shimmer_artist_name_song_preview_textview)
            shimmerSongPreviewNameTextView =
                itemView.findViewById(R.id.shimmer_song_preview_name_textview)
            artistNameSongPreviewTextview =
                itemView.findViewById(R.id.artist_name_song_preview_textview)
            songImageItem = itemView.findViewById(R.id.song_image_item)
            itemIndexTextView = itemView.findViewById(R.id.item_index_text_view)
            tvSongName.isSelected = true
        }
    }
}