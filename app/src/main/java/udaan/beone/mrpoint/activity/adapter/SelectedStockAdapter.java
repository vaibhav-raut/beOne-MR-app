package udaan.beone.mrpoint.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import udaan.beone.mrpoint.R;
import udaan.beone.mrpoint.activity.ActionStockActivity;
import udaan.beone.mrpoint.activity.dialog.SelectedItemInfoDialog;
import udaan.beone.mrpoint.http.model.StockItemL;
import udaan.beone.mrpoint.util.DataUtil;

/**
 * Created by Vaibhav on 22-05-2016.
 */

public class SelectedStockAdapter extends RecyclerView.Adapter<SelectedStockAdapter.SelectedStockViewHolder> {

    private LinkedList<StockItemL> items;
    private ActionStockActivity mContext;

    public SelectedStockAdapter(ActionStockActivity context) {
        items = new LinkedList<StockItemL>();
        this.mContext = context;
    }

    public List<StockItemL> getItems() {
        return items;
    }
    public boolean addItem(StockItemL item) {

        boolean duplicate = false;
        for(StockItemL itemL : items) {
            if(itemL.getStockItemId() == item.getStockItemId()) {
                duplicate = true;
                break;
            }
        }

        if(!duplicate) {
            this.items.add(item);
            notifyItemInserted(items.size());
        }

        return !duplicate;
    }
    public void deleteItem(int position) {
        this.items.remove(position - 1);
        notifyItemRemoved(position);
    }
    public void showItemInfo(int position, StockItemL item) {
        new SelectedItemInfoDialog(mContext, this, position, item).show();

    }
    public void clearAllItems() {
        if(items != null) {
            int itemCount = this.items.size();
            this.items.clear();
            notifyItemRangeRemoved(1, itemCount);
        }
    }

    @Override
    public SelectedStockViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rc_row_selected_stock, null);

        SelectedStockViewHolder viewHolder = new SelectedStockViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SelectedStockViewHolder viewHolder, int i) {
        StockItemL item = null;
        if(i > 0) {
            item = items.get(i - 1);
        }

        //Setting text view title
        viewHolder.setData(item, i);

        // Add OnClickListener for the Card List
        viewHolder.setOnClickListener();

        viewHolder.setTag(viewHolder);
    }

    @Override
    public int getItemCount() {
        return (null != items ? (items.size() + 1) : 1);
    }

    View.OnClickListener itemDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SelectedStockViewHolder holder = (SelectedStockViewHolder) view.getTag();
            int position = holder.getAdapterPosition();

            if (position > 0) {
                StockItemL item = items.get(position - 1);

                mContext.onSelectStockDelete(item);
                deleteItem(position);
           }
        }
    };

    View.OnClickListener itemInfoListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SelectedStockViewHolder holder = (SelectedStockViewHolder) view.getTag();
            int position = holder.getAdapterPosition();

            if (position > 0) {
                StockItemL item = items.get(position - 1);
                showItemInfo(position, item);
            }
        }
    };
    public class SelectedStockViewHolder extends RecyclerView.ViewHolder {
        protected TextView rsItemId;
        protected TextView rsStockType;
        protected TextView rsItemMrp;
        protected TextView rsItemWsp;
        protected TextView rsDiscountedWsp;
        protected TextView rsDeleteItem;

        public SelectedStockViewHolder(View view) {
            super(view);
            this.rsItemId = (TextView) view.findViewById(R.id.rs_item_id);
            this.rsStockType = (TextView) view.findViewById(R.id.rs_stock_type);
            this.rsItemMrp = (TextView) view.findViewById(R.id.rs_item_mrp);
            this.rsItemWsp = (TextView) view.findViewById(R.id.rs_item_mr);
            this.rsDiscountedWsp = (TextView) view.findViewById(R.id.rs_discounted_wsp);
            this.rsDeleteItem = (TextView) view.findViewById(R.id.rs_delete_item);
        }

        public void setData(StockItemL item, int i) {

            if(i == 0) {
                //Setting text view title
                this.rsItemId.setText("Item Id");
                this.rsStockType.setText("StockType");
                this.rsItemMrp.setText("MRP");
                this.rsItemWsp.setText("WSP");
                this.rsDiscountedWsp.setText("Dis");
                this.rsDeleteItem.setText("");

                this.rsItemId.setBackgroundResource(R.drawable.rectangle_border_fill50);
                this.rsStockType.setBackgroundResource(R.drawable.rectangle_border_fill50);
                this.rsItemMrp.setBackgroundResource(R.drawable.rectangle_border_fill50);
                this.rsItemWsp.setBackgroundResource(R.drawable.rectangle_border_fill50);
                this.rsDiscountedWsp.setBackgroundResource(R.drawable.rectangle_border_fill50);
                this.rsDeleteItem.setBackgroundResource(R.drawable.rectangle_border_fill50);
            } else {
                //Setting text view data
                this.rsItemId.setText(DataUtil.toString(item.getStockItemId()));
                this.rsStockType.setText(item.getStockTypeName());
                this.rsItemMrp.setText(DataUtil.toString(item.getDisMrpPrice()));
                this.rsItemWsp.setText(DataUtil.toString(item.getDisMrPrice()));
                this.rsDiscountedWsp.setText(DataUtil.toString(item.getDiscountAm()));
                this.rsDeleteItem.setText("X");

                this.rsItemId.setBackgroundResource(R.drawable.rectangle_border);
                this.rsStockType.setBackgroundResource(R.drawable.rectangle_border);
                this.rsItemMrp.setBackgroundResource(R.drawable.rectangle_border);
                this.rsItemWsp.setBackgroundResource(R.drawable.rectangle_border);
                this.rsDiscountedWsp.setBackgroundResource(R.drawable.rectangle_border);
                this.rsDeleteItem.setBackgroundResource(R.drawable.rectangle_border);
            }
        }

        public void setOnClickListener() {

            // Add OnClickListener for the Card List
            rsItemId.setOnClickListener(itemInfoListener);
            rsStockType.setOnClickListener(itemInfoListener);
            rsItemMrp.setOnClickListener(itemInfoListener);
            rsItemWsp.setOnClickListener(itemInfoListener);
            rsDiscountedWsp.setOnClickListener(itemInfoListener);
            rsDeleteItem.setOnClickListener(itemDeleteListener);
        }

        public void setTag(SelectedStockViewHolder holder) {

            this.rsItemId.setTag(holder);
            this.rsStockType.setTag(holder);
            this.rsItemMrp.setTag(holder);
            this.rsItemWsp.setTag(holder);
            this.rsDiscountedWsp.setTag(holder);
            this.rsDeleteItem.setTag(holder);
        }
    }

    public interface OnSelectStockListener {
        public void onSelectStockDelete(StockItemL item);
    }
}

