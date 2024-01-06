package com.example.muzik.ui.bottom_sheet_dialog.stream_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muzik.R
import com.example.muzik.adapter.SongsAdapterVertical
import com.example.muzik.databinding.BottomSheetStreamListBinding
import com.example.muzik.ui.activity.main_activity.MainActivity
import com.example.muzik.ui.activity.stream_inside_activity.StreamShareActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.launch

class StreamListBottomSheet : BottomSheetDialogFragment() {

    private val binding by viewBinding(BottomSheetStreamListBinding::bind)
    private lateinit var adapter: SongsAdapterVertical
    private lateinit var viewModel: StreamListBottomSheetViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[StreamListBottomSheetViewModel::class.java]
        return inflater.inflate(R.layout.bottom_sheet_stream_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SongsAdapterVertical().removeMoreOptionShowed().removePlayOnClick()

        binding.streamListRcv.adapter = adapter.setFragmentOwner(fragmentOwner = this)
            .setPlayerViewModel(MainActivity.playerViewModel)
        binding.streamListRcv.layoutManager = LinearLayoutManager(context)

        binding.addMoreSongButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        viewModel.songs.observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }

        lifecycleScope.launch {
            viewModel.fetchAllSong((requireActivity() as StreamShareActivity).streamList)
        }
    }

    companion object {
        const val TAG = "StreamListBottomSheet"
    }
}