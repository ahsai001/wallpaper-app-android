package com.ahsailabs.wallpaperapp.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.ahsailabs.wallpaperapp.databinding.HomeItemViewBinding
import com.ahsailabs.wallpaperapp.domain.models.response.Photo

class HomeAdapter(context: Context, var itemList: MutableList<Photo>, val listener: OnItemClickListener) : RecyclerView.Adapter<HomeViewHolder>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = HomeItemViewBinding.inflate(layoutInflater, parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(itemList[position], listener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}

class HomeViewHolder(private val binding: HomeItemViewBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(photo: Photo, listener: OnItemClickListener) {
        binding.ivHome.load(photo.src?.large2x){
            transformations(RoundedCornersTransformation(50f))
        }
        binding.ivHome.setOnClickListener {
            listener.onItemClick(binding.ivHome, photo)
        }
    }
}

interface OnItemClickListener{
    fun onItemClick(imageView: ImageView, photo: Photo)
}
