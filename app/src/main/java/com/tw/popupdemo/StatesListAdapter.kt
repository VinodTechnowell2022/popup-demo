package com.tw.popupdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class StatesListAdapter(list: List<StateTable>) : RecyclerView.Adapter<StatesListAdapter.ExampleViewHolder>() {

    interface OnItemClickListener1 {
        fun onItemClicked(position: Int, view: View)
    }

    private var mList: List<StateTable> = list
    var onItemClick: ((StateTable) -> Unit)? = null

    inner class ExampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.tvName)
        var code: TextView = itemView.findViewById(R.id.tvCode)

        init {
            itemView.setOnClickListener { v ->
                showPopUpMenu2( v )
            }
        }
    }

    private fun showPopUpMenu2(anchor: View?) {
        val popup = PopupMenu(anchor!!.context, anchor)
        //Inflating the Popup using xml file
        popup.menuInflater.inflate(R.menu.product_popup_menu, popup.menu)
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener { item ->

            if (item.itemId==R.id.addRating){
                //do your action here
                Toast.makeText(anchor.context, "You Clicked ${item.itemId}: " + item.title, Toast.LENGTH_SHORT).show()
            }
            else if (item.itemId==R.id.viewDetails){
                Toast.makeText(anchor.context, "You Clicked ${item.itemId}: " + item.title, Toast.LENGTH_SHORT).show()
            }
            true
        }
        popup.show() //showing popup menu
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.state_row, parent, false)
        return ExampleViewHolder(v)
    }

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        val currentItem: StateTable = mList[position]
        holder.name.text = currentItem.name
        holder.code.text = currentItem.code
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    init {
        notifyDataSetChanged()
    }
}