package com.mobile.image_gallery.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.image_gallery.R
import com.mobile.image_gallery.data.GalleryItem
import com.mobile.image_gallery.data.GalleryItems


class GalleryRecyclerAdapter(
    private val imgClickListener: OnImageClickListener,
    context: Context
) : RecyclerView.Adapter<GalleryRecyclerAdapter.ViewHolder>()
{
    private val mInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = mInflater.inflate(R.layout.image_item_cell, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(GalleryItems.galleryItems[position])
    }

    override fun getItemCount(): Int {
        return GalleryItems.galleryItems.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val galleryImage: ImageView = itemView.findViewById(R.id.imagePreview)

        fun bind(galleryItem: GalleryItem) {
            galleryImage.setImageResource(galleryItem.imageRes)
            galleryImage.setOnClickListener {
                imgClickListener.onImageClick(galleryItem)
            }
            println("adding image ${galleryItem.id}")
        }
    }
}

interface OnImageClickListener {
    fun onImageClick(galleryItem: GalleryItem)
}
