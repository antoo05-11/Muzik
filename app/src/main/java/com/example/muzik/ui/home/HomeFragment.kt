package com.example.muzik.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.muzik.databinding.FragmentHomeBinding
import kotlinx.coroutines.DelicateCoroutinesApi

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
       // val navController = findNavController()

//        val button: LinearLayout = binding.button;
//        button.setOnClickListener {
//            navController.navigate(R.id.navigation_fragment)
//        }

//        binding.playButton.setOnClickListener {
//            if (flag) mp.start()
//        }

//        GlobalScope.launch {
//            val result = MainActivity.muzikAPI.getSong(5)
//            binding.playingSongName.text = result.body()?.name
//            binding.playingSongImage.load(result.body()?.imageURL)
//            binding.playingSongArtist.text = if (result.body()?.artistName.isNullOrBlank()) {
//                "Artist name"
//            } else {
//                result.body()?.artistName
//            }
//        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}