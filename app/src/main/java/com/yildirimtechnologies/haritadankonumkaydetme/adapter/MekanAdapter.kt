package com.yildirimtechnologies.haritadankonumkaydetme.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yildirimtechnologies.haritadankonumkaydetme.View.MapsActivity
import com.yildirimtechnologies.haritadankonumkaydetme.databinding.RecyclerRowBinding
import com.yildirimtechnologies.haritadankonumkaydetme.model.Mekan

class MekanAdapter(val mekanList : List<Mekan>) : RecyclerView.Adapter<MekanAdapter.MekanHolder>(){
    class MekanHolder(val recyclerRowBinding: RecyclerRowBinding) : RecyclerView.ViewHolder(recyclerRowBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MekanHolder {
        val recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MekanHolder(recyclerRowBinding)
    }

    override fun getItemCount(): Int {
        return mekanList.size
    }

    override fun onBindViewHolder(holder: MekanHolder, position: Int) {
        holder.recyclerRowBinding.RecyclerViewTextView.text = mekanList.get(position).isim
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context,MapsActivity::class.java)
            holder.itemView.context.startActivity(intent)
        }
    }

}