package com.example.muzik.ui.chart_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.muzik.adapter.ListSongsPreviewAdapter
import com.example.muzik.databinding.FragmentChartBinding
import com.example.muzik.response_model.Song
import com.example.muzik.ui.player_view_fragment.PlayerViewModel
import com.example.muzik.utils.addDecorationForVerticalRcv
import com.example.muzik.utils.addSampleForRcv
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.coroutines.launch


class ChartFragment : Fragment() {
    private lateinit var viewModel: ChartViewModel
    private lateinit var binding: FragmentChartBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.fetchData()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChartBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ChartViewModel::class.java]
        val playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
        addDecorationForVerticalRcv(binding.rcvChartSongsList, requireActivity())
        addSampleForRcv(
            binding.rcvChartSongsList,
            ListSongsPreviewAdapter::class.java,
            Song::class.java,
            5
        )
        viewModel.chartSongsList.observe(viewLifecycleOwner) {
            val adapter = ListSongsPreviewAdapter(it).hasItemIndexTextView().setFragmentOwner(this)
                .setPlayerViewModel(playerViewModel)
            binding.rcvChartSongsList.adapter = adapter
        }

        var dataObjects = mutableListOf(Pair(1, 2), Pair(4, 6), Pair(10, 24))
        var entries = ArrayList<Entry>();
        for (data in dataObjects) {
            entries.add(Entry(data.first.toFloat(), data.second.toFloat()));
        }

        val chart = binding.chart

        val dataSet1 = LineDataSet(entries, "")
        dataObjects = mutableListOf(Pair(7, 10), Pair(10, 8), Pair(14, 44))
        entries = ArrayList();
        for (data in dataObjects) {
            entries.add(Entry(data.first.toFloat(), data.second.toFloat()));
        }

        val dataSet2 = LineDataSet(entries, "DataSet2")
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(dataSet1)
        dataSets.add(dataSet2)
        val lineData = LineData(dataSets)

        chart.data = lineData
        return binding.root
    }
}