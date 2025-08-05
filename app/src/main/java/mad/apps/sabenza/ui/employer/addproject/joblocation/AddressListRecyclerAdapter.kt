package mad.apps.sabenza.ui.employer.addproject.joblocation

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import mad.apps.sabenza.R
import mad.apps.sabenza.data.model.address.Address
import mad.apps.sabenza.framework.ui.bindView


class AddressListRecyclerAdapter(private val dataSet: List<Address>, private val defaultAddressId: String) : RecyclerView.Adapter<AddressListRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressListRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.address_line_item_layout, parent, false) as LinearLayout
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setAddress(dataSet[position])
        if (dataSet[position].id == defaultAddressId) {
            holder.highlightAsDefault()
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val line1TextView by bindView<TextView>(R.id.line1_textview)
        val line2TextView by bindView<TextView>(R.id.line2_textview)
        val cityTextView by bindView<TextView>(R.id.city_textview)
        val postalCodeTextView by bindView<TextView>(R.id.postal_code_textview)
        val defaultTextView by bindView<TextView>(R.id.default_address_textview)

        init {
            itemView.setOnClickListener { itemClickListener.clickItem(adapterPosition,dataSet[adapterPosition]) }
        }

        fun setAddress(address: Address) {
            line1TextView.text = address.line1
            line2TextView.text = address.line2
            cityTextView.text = address.cityTown
            postalCodeTextView.text = address.postcode
        }

        fun highlightAsDefault() {
            line1TextView.setTextColor(ContextCompat.getColor(itemView.context ,R.color.jelly_bean))
            defaultTextView.visibility = View.VISIBLE
        }

    }

    //==================On Click Listener================================//
    private lateinit var itemClickListener: ItemClickListener

    interface ItemClickListener {
        fun clickItem(index: Int, address: Address)
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

}
