package net.harutiro.campingsensingwear.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import net.harutiro.campingsensingwear.Entity.SensorItemDataClass
import net.harutiro.campingsensingwear.R

class SensorItemRViewAdapter(private val context: Context, private val listener: OnItemClickListner):
    RecyclerView.Adapter<SensorItemRViewAdapter.ViewHolder>() {

    //リサイクラービューに表示するリストを宣言する
    val items: MutableList<SensorItemDataClass> = mutableListOf()

    //データをcourseDateと結びつける？？
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val sendButton: Button = view.findViewById(R.id.send_button)
    }

    //はめ込むものを指定
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_sensor_list_cell,parent,false)
        return ViewHolder(view)
    }

    //itemsのposition番目の要素をviewに表示するコード
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.sendButton.setOnClickListener {
            listener.onItemClick(item)
        }

        holder.sendButton.text = item.date
    }

    //リストの要素数を返すメソッド
    override fun getItemCount(): Int {
        return items.size
    }

    // RecyclerViewの要素をタップするためのもの
    interface OnItemClickListner{
        fun onItemClick(item:SensorItemDataClass)
    }

    fun reView(){
        notifyDataSetChanged()
    }

    fun setList(list: List<SensorItemDataClass>){
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun getList(): List<SensorItemDataClass>{
        return items
    }
}