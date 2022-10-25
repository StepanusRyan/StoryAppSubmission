package com.stepanusryan.storyapp.ui.home.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.stepanusryan.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private var name : String? = ""
    private var image:String? = ""
    private var desc:String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)
        val extra = intent.extras
        if (extra != null){
            name = extra.getString(NAME)
            image = extra.getString(IMAGE)
            desc = extra.getString(DESCRIPTION)
        }
        detailBinding.txtDetailName.text = name.toString()
        detailBinding.txtDetailDescription.text = desc.toString()
        Glide.with(this)
            .load(image)
            .into(detailBinding.imgDetailImage)

    }

    companion object{
        const val NAME = "NAME"
        const val DESCRIPTION = "DESCRIPTION"
        const val IMAGE = "IMAGE"
    }
}