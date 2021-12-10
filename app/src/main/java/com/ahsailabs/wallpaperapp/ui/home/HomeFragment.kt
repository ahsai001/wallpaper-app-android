package com.ahsailabs.wallpaperapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.ahsailabs.wallpaperapp.DetailActivity
import com.ahsailabs.wallpaperapp.databinding.FragmentHomeBinding
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import com.ahsailabs.wallpaperapp.data.ApiService
import com.ahsailabs.wallpaperapp.data.repositories.WallpaperRepository
import com.ahsailabs.wallpaperapp.data.sources.WallpaperRemoteSource
import com.ahsailabs.wallpaperapp.domain.models.response.Photo
import com.ahsailabs.wallpaperapp.domain.usecases.HomeUseCase
import com.ahsailabs.wallpaperapp.lib.ProcessState
import com.ahsailabs.wallpaperapp.lib.createWithFactory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private var homeAdapter: HomeAdapter? = null
    private var data: MutableList<Photo> = mutableListOf()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loadViewModel()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun loadViewModel() {
        val wallpaperRemoteSource = WallpaperRemoteSource(ApiService.client)
        val wallpaperRepository = WallpaperRepository(wallpaperRemoteSource)
        val homeUseCase = HomeUseCase(wallpaperRepository)
        val homeViewModel: HomeViewModel by lazy {
            ViewModelProvider(
                viewModelStore,
                createWithFactory {
                    HomeViewModel(homeUseCase)
                }
            )[HomeViewModel::class.java]
        }
        this.homeViewModel = homeViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvHome.layoutManager = GridLayoutManager(view.context,2)
        homeAdapter = HomeAdapter(view.context,data, object : OnItemClickListener {
            override fun onItemClick(imageView: ImageView, photo: Photo) {
                val detailActivity = Intent(view.context, DetailActivity::class.java)
                detailActivity.putExtra("wallpaper", photo.src?.large2x)
                detailActivity.putExtra("wallpaperOri", photo.src?.original)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), imageView, "wallpaper")
                view.context.startActivity(detailActivity, options.toBundle())
            }
        })
        binding.rvHome.adapter = homeAdapter

        /*
        homeViewModel.wallpapers.observe(viewLifecycleOwner, Observer {
            data.addAll(it)
            homeAdapter?.notifyItemRangeChanged(0,it.size)
        })
        */

        homeViewModel.homeState.getStateFlow().onEach {
            when (it) {
                is ProcessState.Initial -> {

                }
                is ProcessState.Loading -> {
                    showLoading()
                }
                is ProcessState.Success -> {
                    data.addAll(it.data)
                    homeAdapter?.notifyItemRangeChanged(0,it.data.size)
                    hideLoading()
                }
                is ProcessState.Error -> {
                    hideLoading()
                }
            }
        }.launchIn(lifecycleScope)

        homeViewModel.searchWallpapers("nature")
    }

    private fun showLoading(){
        binding.cpiLoading.visibility = View.VISIBLE
        binding.rvHome.visibility = View.GONE
    }

    private fun hideLoading(){
        binding.cpiLoading.visibility = View.GONE
        binding.rvHome.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}