package com.example.muzik.ui.bottom_sheet_dialog.playlists

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muzik.R
import com.example.muzik.adapter.playlists.PlaylistsAdapterVertical
import com.example.muzik.data_model.standard_model.Playlist
import com.example.muzik.data_model.standard_model.Song
import com.example.muzik.databinding.BottomSheetPlaylistsBinding
import com.example.muzik.storage.SharedPrefManager
import com.example.muzik.ui.main_activity.MainActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.launch

class PlaylistsBottomSheet : BottomSheetDialogFragment() {
    private val binding by viewBinding(BottomSheetPlaylistsBinding::bind)
    private lateinit var viewModel: PlaylistBottomSheetViewModel
    private lateinit var song: Song

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_playlists, container, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)

        viewModel = ViewModelProvider(this)[PlaylistBottomSheetViewModel::class.java]

        val adapter = PlaylistsAdapterVertical(listPlaylist = mutableListOf(), fragmentOwner = this)
        binding.playlistListRcv.adapter = adapter
        binding.playlistListRcv.layoutManager = LinearLayoutManager(context)

        MainActivity.userPlaylists.observe(this) {
            adapter.listPlaylist = it.toMutableList().apply { add(0, Playlist()) }
            adapter.notifyDataSetChanged()
        }

        viewModel.addSongToPlaylistSuccessfully.observe(viewLifecycleOwner) {
            if (it == PlaylistBottomSheetViewModel.StatusCode.SUCCESS) {
                Toast.makeText(context, "Song added to playlist", Toast.LENGTH_SHORT).show()
                viewModel.addSongToPlaylistSuccessfully.value =
                    PlaylistBottomSheetViewModel.StatusCode.NONE
                dismiss()
            }
        }
    }

    fun addSongToPlaylist(playlistID: Long) {
        SharedPrefManager.getInstance(requireContext()).accessToken?.let {
            lifecycleScope.launch {
                viewModel.addSongToPlaylist(
                    songID = song.requireSongID(),
                    playlistID = playlistID,
                    accessToken = it
                )
            }
        }

    }

    fun initData(song: Song) {
        this.song = song
    }

    companion object {
        const val TAG = "PlaylistsBottomSheet"
    }
}