package com.kalpeshkundanani.nytimes.ui.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.kalpeshkundanani.nytimes.R
import com.kalpeshkundanani.nytimes.data.models.NewsResult
import com.kalpeshkundanani.nytimes.ui.activity.MainActivity
import com.kalpeshkundanani.nytimes.ui.activity.NewsItemDetailActivity
import com.kalpeshkundanani.nytimes.ui.fragment.NewsItemDetailFragment
import com.kalpeshkundanani.nytimes.ui.viewholder.NewsItemViewHolder
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

import java.util.*

/**
 * Created by Kalpesh Kundanani on 11/11/20.
 */
class NewsRecyclerViewAdapter(private val mParentActivity: MainActivity?, private val mTwoPane: Boolean) : RecyclerView.Adapter<NewsItemViewHolder?>() {
    private var mValues: MutableList<NewsResult> = ArrayList()
    private val mOnClickListener: View.OnClickListener? = View.OnClickListener { view ->
        val item = view.tag as NewsResult
        val json = Gson().toJson(item)
        if (mTwoPane) {
            val arguments = Bundle()
            arguments.putString(NewsItemDetailFragment.Companion.ARG_ITEM_ID, json)
            val fragment = NewsItemDetailFragment()
            fragment.arguments = arguments
            mParentActivity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.item_detail_container, fragment)
                    ?.commit()
        } else {
            val context = view.context
            val intent = Intent(context, NewsItemDetailActivity::class.java)
            intent.putExtra(NewsItemDetailFragment.Companion.ARG_ITEM_ID, json)
            context.startActivity(intent)
        }
    }

    fun setData(items: MutableList<NewsResult>) {
        mValues = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.news_item_list_content, parent, false)
        return NewsItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        holder.itemView.tag = mValues[position]
        holder.itemView.setOnClickListener(mOnClickListener)
        val item: NewsResult = mValues[position]
        holder.mTextViewNewsTitle?.text = item.title
        holder.mTextViewByLine?.text = item.byline
        holder.mTextViewNewsDate?.text = item.publishedDate
        if (item.media!!.isNotEmpty()) {
            val media = item.media[0]
            if (media?.mediaMetadata?.isNotEmpty() == true) {
                val url = media.mediaMetadata[0]?.url
                Picasso.get()
                        .load(url)
                        .placeholder(R.drawable.progress_animation)
                        .transform(CropCircleTransformation())
                        .into(holder.mImageViewNews)
            }
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }
}