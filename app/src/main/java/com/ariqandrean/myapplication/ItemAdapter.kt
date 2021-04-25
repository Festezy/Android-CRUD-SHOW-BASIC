package com.ariqandrean.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*

class ItemAdapter (
    val context: Context,
    val items : ArrayList<EmpModelClass>
        )  : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    class ViewHolder (view: View) : RecyclerView.ViewHolder(view){
        val llMain = view.llMain
        val tvName = view.tvNameRow
        val tvEmail = view.tvEmailRow
        val tvPhone = view.tvPhone
        val tvAddress = view.tvaddress

        val ivShowMore = view.ivShowMore
        val ivEdit = view.ivEdit
        val ivDelete = view.ivDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_row, parent, false)
        )
    }

    override fun getItemCount(): Int {
        //banyaknya data
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)

        holder.tvName.text = item.name
        holder.tvEmail.text = item.email
        holder.tvPhone.text = item.phone
        holder.tvAddress.text = item.address

        if (position % 2 == 0){
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLightGray))
        } else {
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.teal_200))
        }

        holder.ivShowMore.setOnClickListener {
            if (context is MainActivity){
                context.ShowRecordDialog(item)
            }
        }

        holder.ivEdit.setOnClickListener{
            if (context is MainActivity){
                context.updateRecordDialog(item)
            }
        }
        holder.ivDelete.setOnClickListener{
            if(context is MainActivity){
                context.deleteRecordAlertDialog(item)
            }
        }
    }
}