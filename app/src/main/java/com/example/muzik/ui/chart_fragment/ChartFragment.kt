package com.example.muzik.ui.chart_fragment

import android.graphics.Color
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
import com.example.muzik.utils.printLogcat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.coroutines.launch


val lineChartColors =
    listOf(
        Color.parseColor("#3877f9"),
        Color.parseColor("#02d59a"),
        Color.parseColor("#e56a34")
    )

class ChartFragment : Fragment() {
    private lateinit var viewModel: ChartViewModel
    private lateinit var binding: FragmentChartBinding
    private lateinit var chartView: LineChart
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.fetchData()
        }
        lifecycleScope.launch {
            viewModel.fetchChartViewData()
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


        chartView = binding.chart

        val yAxisRight: YAxis = chartView.axisRight
        val yAxisLeft: YAxis = chartView.axisLeft
        val xAxis: XAxis = chartView.xAxis
        val l: Legend = chartView.legend

        yAxisLeft.isEnabled = false
        yAxisRight.isEnabled = false
        xAxis.textColor = Color.WHITE
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setLabelCount(10, true)
        yAxisLeft.setDrawGridLines(false)
        yAxisRight.setDrawGridLines(false)
        l.isEnabled = false
        chartView.description = Description()
        chartView.description.text = ""
        chartView.isDoubleTapToZoomEnabled = false

        chartView.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry, h: Highlight) {
                val x = e.x
                val y = e.y
            }

            override fun onNothingSelected() {}
        })

        viewModel.chart.observe(viewLifecycleOwner) {
            val dataSets = ArrayList<ILineDataSet>()
            for (i in 0..2) {
                val songWithView = it[i]
                val dataObjects = mutableListOf<Pair<Int, Int>>()
                songWithView.songViews.sort()
                for (j in 0..9) {
                    printLogcat(songWithView.songViews[j].date.date)
                    dataObjects.add(
                        Pair(
                            (songWithView.songViews[j].date.date),
                            songWithView.songViews[j].views
                        )
                    )
                }
                val entries = ArrayList<Entry>()
                for (data in dataObjects) {
                    entries.add(
                        Entry(
                            data.first.toFloat(),
                            data.second.toFloat()
                        )
                    )
                }
                val dataSet = LineDataSet(entries, songWithView.song.name)
                dataSet.color = lineChartColors[i]
                dataSet.setDrawHorizontalHighlightIndicator(false)
                dataSet.setDrawVerticalHighlightIndicator(false)
                dataSets.add(dataSet)
            }

            val lineData = LineData(dataSets)
            lineData.setDrawValues(false)
            chartView.data = lineData
            chartView.invalidate()
        }

        return binding.root
    }

    private fun resetChart() {
        chartView.fitScreen()
        chartView.data?.clearValues()
        chartView.xAxis.valueFormatter = null
        chartView.notifyDataSetChanged()
        chartView.clear()
        chartView.invalidate()
    }
}