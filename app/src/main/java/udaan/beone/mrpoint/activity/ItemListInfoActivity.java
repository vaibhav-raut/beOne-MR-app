package udaan.beone.mrpoint.activity;
/**
 * Author Vaibhav
 * Date 18/06/16
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import udaan.beone.mrpoint.R;
import udaan.beone.mrpoint.data.ItemListInfoDataHolder;
import udaan.beone.mrpoint.http.model.MrStockDisplay.ItemDisplay;
import udaan.beone.mrpoint.util.DataUtil;
import udaan.beone.mrpoint.util.KeyConst;


public class ItemListInfoActivity extends AppCompatActivity {

    protected TextView gsNoOfItemV;

    protected RecyclerView mRecyclerView;
    protected ItemInfoAdapter adapter;

    protected ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Main Activity Init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list_info);

        gsNoOfItemV = (TextView) findViewById(R.id.gs_no_of_items_v);

        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.item_list_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ItemInfoAdapter(this);
        mRecyclerView.setAdapter(adapter);

        gsNoOfItemV.setText(DataUtil.toString(ItemListInfoDataHolder.instance().getItemDisplays().size()));
    }

    @Override
    protected void onPostResume () {
        super.onPostResume();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public class ItemInfoAdapter extends RecyclerView.Adapter<ItemInfoAdapter.ItemInfoViewHolder> {

        private ItemListInfoActivity mContext;
        private List<ItemDisplay> items;

        public ItemInfoAdapter(ItemListInfoActivity context) {
            this.mContext = context;
            this.items = ItemListInfoDataHolder.instance().getItemDisplays();
        }

        public List<ItemDisplay> getItems() {
            return items;
        }

        @Override
        public ItemInfoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rc_row_stock_item, null);

            ItemInfoViewHolder viewHolder = new ItemInfoViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ItemInfoViewHolder viewHolder, int i) {
            ItemDisplay item = null;
            if(i > 0) {
                item = items.get(i - 1);
            }

            //Setting text view title
            viewHolder.setData(item, i);

            if(i > 0) {
                // Add OnClickListener for the Card List
                viewHolder.setOnClickListener();

                viewHolder.setTag(viewHolder);
            }
        }

        @Override
        public int getItemCount() {
            return (null != items ? (items.size() + 1) : 1);
        }

        View.OnClickListener showItemListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemInfoViewHolder holder = (ItemInfoViewHolder) view.getTag();
                int position = holder.getAdapterPosition();

                if (position > 0) {
                    ItemDisplay item = items.get(position - 1);

                    if (item != null) {
                        Intent itemIntent = new Intent(mContext, ItemInfoLActivity.class);
                        itemIntent.putExtra(KeyConst.STOCK_ITEM_ID, item.getStockItemId());
                        startActivity(itemIntent);
                    }
                }
            }
        };

        public class ItemInfoViewHolder extends RecyclerView.ViewHolder {
            protected TextView rsItemId;
            protected TextView rsStockType;
            protected TextView rsBrand;
            protected TextView rsItemMrp;
            protected TextView rsItemWsp;

            public ItemInfoViewHolder(View view) {
                super(view);
                this.rsItemId = (TextView) view.findViewById(R.id.rs_item_id);
                this.rsStockType = (TextView) view.findViewById(R.id.rs_stock_type);
                this.rsBrand = (TextView) view.findViewById(R.id.rs_brand);
                this.rsItemMrp = (TextView) view.findViewById(R.id.rs_item_mrp);
                this.rsItemWsp = (TextView) view.findViewById(R.id.rs_item_mr);
            }

            public void setData(ItemDisplay item, int i) {

                if(i == 0) {
                    //Setting text view title
                    this.rsItemId.setText("Item Id");
                    this.rsStockType.setText("StockType");
                    this.rsBrand.setText("Brand");
                    this.rsItemMrp.setText("MRP");
                    this.rsItemWsp.setText("WSP");

                    this.rsItemId.setBackgroundResource(R.drawable.rectangle_border_fill50);
                    this.rsStockType.setBackgroundResource(R.drawable.rectangle_border_fill50);
                    this.rsBrand.setBackgroundResource(R.drawable.rectangle_border_fill50);
                    this.rsItemMrp.setBackgroundResource(R.drawable.rectangle_border_fill50);
                    this.rsItemWsp.setBackgroundResource(R.drawable.rectangle_border_fill50);
                } else {
                    //Setting text view data
                    this.rsItemId.setText(DataUtil.toString(item.getStockItemId()));
                    this.rsStockType.setText(item.getStockType());
                    this.rsBrand.setText(item.getBrand());
                    this.rsItemMrp.setText(DataUtil.toString(item.getMrpPrice()));
                    this.rsItemWsp.setText(DataUtil.toString(item.getMrPrice()));
                }
            }

            public void setOnClickListener() {
                // Add OnClickListener for the Card List
                rsItemId.setOnClickListener(showItemListener);
                rsStockType.setOnClickListener(showItemListener);
                rsBrand.setOnClickListener(showItemListener);
                rsItemMrp.setOnClickListener(showItemListener);
                rsItemWsp.setOnClickListener(showItemListener);
            }

            public void setTag(ItemInfoViewHolder holder) {
                this.rsItemId.setTag(holder);
                this.rsStockType.setTag(holder);
                this.rsBrand.setTag(holder);
                this.rsItemMrp.setTag(holder);
                this.rsItemWsp.setTag(holder);
            }
        }
    }
}
