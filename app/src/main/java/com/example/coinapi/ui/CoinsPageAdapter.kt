package com.example.coinapi.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coinapi.R
import com.example.coinapi.data.repository.NetworkState
import com.example.coinapi.data.vo.Coin
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import kotlinx.android.synthetic.main.coins_view.view.*
import kotlinx.android.synthetic.main.network_state_item.view.*

class CoinsPageAdapter(public val context: Context) : PagedListAdapter<Coin, RecyclerView.ViewHolder>(CoinsDiffCallback()) {

    val COINS_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2
    val POSITION_FIVE = 3

    private var networkState: NetworkState? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == COINS_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.coins_view, parent, false)
            return MovieItemViewHolder(view)
        }else if (viewType == POSITION_FIVE){
            view = layoutInflater.inflate(R.layout.coins_view_5, parent, false)
            return EveryFivePositionItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (getItemViewType(position) == COINS_VIEW_TYPE) {

            (holder as MovieItemViewHolder).bind(getItem(position),context)
        } else if (getItemViewType(position) == POSITION_FIVE){

            (holder as EveryFivePositionItemViewHolder).bind(getItem(position),context)
        }
        else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }

    }


    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
//        return 10

    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else if (position>1 && (position+1) % 5 == 0){
            POSITION_FIVE
        } else {
            COINS_VIEW_TYPE
        }
    }

    class MovieItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        fun bind(coin: Coin?,context: Context) {
            itemView.name.text = coin?.name
            itemView.description.text =  coin?.iconUrl

            val coinsIconUrl = coin?.iconUrl
            val uriIcon = Uri.parse(coinsIconUrl)
//            Glide.with(itemView.context)
//                .load(coinsIconUrl)
//                .disallowHardwareConfig()
//                .into(itemView.icon)

            GlideToVectorYou
                .init()
                .with(context)
                .load(uriIcon, itemView.icon);

            Log.d("aaa", coin?.iconUrl);
        }

    }

    class NetworkStateItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {

//        fun bind(coin: Coin?,context: Context){
//            itemView.name.text = coin?.name
//            itemView.description.text =  coin?.description
//
////            val coinsIconUrl = coin?.iconUrl
////            Glide.with(itemView.context)
////                .load(coinsIconUrl)
////                .into(itemView.icon);
//
//        }
        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                itemView.progress_bar_item.visibility = View.VISIBLE;
            }
            else  {
                itemView.progress_bar_item.visibility = View.GONE;
            }

            if (networkState != null && networkState == NetworkState.ERROR) {
                itemView.error_msg_item.visibility = View.VISIBLE;
                itemView.error_msg_item.text = networkState.msg;
            }
            else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                itemView.error_msg_item.visibility = View.VISIBLE;
                itemView.error_msg_item.text = networkState.msg;
            }
            else {
                itemView.error_msg_item.visibility = View.GONE;
            }
        }
    }

    class EveryFivePositionItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        fun bind(coin: Coin?,context: Context) {
            itemView.name.text = coin?.name
            itemView.description.text =  coin?.description

//            val coinsIconUrl = coin?.iconUrl
//            Glide.with(itemView.context)
//                .load(coinsIconUrl)
//                .into(itemView.icon);


        }

    }

    class CoinsDiffCallback : DiffUtil.ItemCallback<Coin>() {
        override fun areItemsTheSame(oldItem: Coin, newItem: Coin): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Coin, newItem: Coin): Boolean {
            return oldItem == newItem
        }

    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {                             //hadExtraRow is true and hasExtraRow false
                notifyItemRemoved(super.getItemCount())    //remove the progressbar at the end
            } else {                                       //hasExtraRow is true and hadExtraRow false
                notifyItemInserted(super.getItemCount())   //add the progressbar at the end
            }
        } else if (hasExtraRow && previousState != newNetworkState) { //hasExtraRow is true and hadExtraRow true and (NetworkState.ERROR or NetworkState.ENDOFLIST)
            notifyItemChanged(itemCount - 1)       //add the network message at the end
        }

    }
}

