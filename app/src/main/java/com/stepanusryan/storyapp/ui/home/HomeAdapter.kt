package com.stepanusryan.storyapp.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stepanusryan.storyapp.databinding.LayoutStoryBinding
import com.stepanusryan.storyapp.model.ListStoryItem
import com.stepanusryan.storyapp.ui.home.detail.DetailActivity

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    private var listStory = ArrayList<ListStoryItem>()
    fun setStory(story:List<ListStoryItem>?){
        if (story == null) return
        this.listStory.clear()
        this.listStory.addAll(story)
//        this.listStory = story as ArrayList<ListStoryItem>
//        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val itemListAdapterStoryBinding = LayoutStoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HomeViewHolder(itemListAdapterStoryBinding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val story = listStory[position]
        holder.bind(story)
    }

    override fun getItemCount(): Int = listStory.size

    class HomeViewHolder(private val binding: LayoutStoryBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(story:ListStoryItem){
            with(binding){
                txtName.text = story.name
                txtDesc.text = story.description
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.NAME,story.name)
                    intent.putExtra(DetailActivity.IMAGE,story.photoUrl)
                    intent.putExtra(DetailActivity.DESCRIPTION,story.description)
                    itemView.context.startActivity(intent)
                }
            }
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(binding.imgStory)
        }
    }

}