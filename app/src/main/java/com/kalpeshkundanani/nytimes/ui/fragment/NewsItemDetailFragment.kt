package com.kalpeshkundanani.nytimes.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.kalpeshkundanani.nytimes.R
import com.kalpeshkundanani.nytimes.data.models.NewsResult
import com.kalpeshkundanani.nytimes.ui.activity.MainActivity
import com.kalpeshkundanani.nytimes.ui.activity.NewsItemDetailActivity
import com.squareup.picasso.Picasso

import java.util.*

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [MainActivity]
 * in two-pane mode (on tablets) or a [NewsItemDetailActivity] on handsets.
 */
class NewsItemDetailFragment : Fragment() {

    private var mItem: NewsResult? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments?.containsKey(ARG_ITEM_ID) == true) {
            val argument = arguments?.getString(ARG_ITEM_ID) ?: return
            mItem = Gson().fromJson(argument, NewsResult::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.news_item_detail, container, false)
        if (mItem != null) {
            // News Image
            initNewsImageView(rootView)

            // Metadata: section subsection and date
            initMetaDataView(rootView)

            // title
            val textViewNewsTitle = rootView.findViewById<TextView?>(R.id.textView_news_title)
            textViewNewsTitle.text = mItem!!.title

            // content
            val textViewNewsAbstract = rootView.findViewById<TextView?>(R.id.textView_news_abstract)
            textViewNewsAbstract.text = mItem!!.resultAbstract
        }
        return rootView
    }

    private fun initNewsImageView(rootView: View) {
        val imageView = rootView.findViewById<ImageView?>(R.id.image_id)
        val mediaList = mItem!!.media!!
        if (mediaList.isNotEmpty()) {
            val media = mediaList[0]
            val mediaMetadata = media?.mediaMetadata!!
            val highestQualityIndex = mediaMetadata.size - 1
            if (highestQualityIndex > 0) {
                Picasso.get()
                        .load(mediaMetadata[highestQualityIndex]?.url)
                        .placeholder(R.drawable.progress_animation)
                        .fit()
                        .into(imageView)
            }
        }
    }

    private fun initMetaDataView(rootView: View) {
        val metaDataList = ArrayList<String?>()
        if (!TextUtils.isEmpty(mItem?.section)) metaDataList.add(mItem?.section)
        if (!TextUtils.isEmpty(mItem?.subsection)) metaDataList.add(mItem?.subsection)
        if (!TextUtils.isEmpty(mItem?.publishedDate)) metaDataList.add(mItem?.publishedDate)
        val textViewNewsMetaData = rootView.findViewById<TextView?>(R.id.textView_news_meta_data)
        textViewNewsMetaData.text = TextUtils.join(" | ", metaDataList)
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        val ARG_ITEM_ID: String? = "item_id"
    }
}