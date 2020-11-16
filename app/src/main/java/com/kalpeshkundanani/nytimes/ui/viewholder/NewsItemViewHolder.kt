package com.kalpeshkundanani.nytimes.ui.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kalpeshkundanani.nytimes.R


/**
 * Created by Kalpesh Kundanani on 11/11/20.
 */
class NewsItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val mTextViewNewsTitle: TextView?
    val mTextViewByLine: TextView?
    val mTextViewNewsDate: TextView?
    val mImageViewNews: ImageView?

    init {
        mTextViewNewsTitle = view.findViewById<View?>(R.id.textView_news_title) as TextView
        mTextViewByLine = view.findViewById<View?>(R.id.textView_by_line) as TextView
        mTextViewNewsDate = view.findViewById<View?>(R.id.textView_news_date) as TextView
        mImageViewNews = view.findViewById<View?>(R.id.imageView_news_image) as ImageView
    }
}