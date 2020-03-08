package udaan.beone.mrpoint.activity.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import udaan.beone.mrpoint.R;
import udaan.beone.mrpoint.activity.ItemListInfoActivity;
import udaan.beone.mrpoint.data.ItemListInfoDataHolder;
import udaan.beone.mrpoint.http.model.MrStockDisplay.MrStockDisplayItem;
import udaan.beone.mrpoint.http.util.HTTPCaller;
import udaan.beone.mrpoint.util.AndroidUtil;
import udaan.beone.mrpoint.util.DataUtil;

public abstract class StockFragment extends CFragment {

    protected TextView mTotalNo;
    protected TextView mTotalWSP;

    protected RecyclerView mRecyclerView;
    protected MyRecyclerAdapter adapter;
    protected ProgressBar progressBar;


    public StockFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_stock, container, false);

        mTotalNo = (TextView) fragment.findViewById(R.id.t_total_no);
        mTotalWSP = (TextView) fragment.findViewById(R.id.t_total_wsp);

        // Initialize recycler view
        mRecyclerView = (RecyclerView) fragment.findViewById(R.id.stock_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressBar = (ProgressBar) fragment.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        initRefreshData();

        return fragment;
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

//        if (activity instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) activity;
//        } else {
//            throw new RuntimeException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

    public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.CustomViewHolder> {
        private List<MrStockDisplayItem> displayItems;
        private Activity mContext;

        public MyRecyclerAdapter(Activity context, List<MrStockDisplayItem> displayItems) {
            this.displayItems = displayItems;
            this.mContext = context;
        }
        public void setDisplayItems(List<MrStockDisplayItem> displayItems) {
            this.displayItems = displayItems;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rc_row_stock, null);

            CustomViewHolder viewHolder = new CustomViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {

            //Setting text view title
            if(i == 0) {
                customViewHolder.brandTV.setText("Brand");
                customViewHolder.stockTypeTV.setText("Stock Type");
                customViewHolder.itemNoTV.setText("No");
                customViewHolder.itemMrpTV.setText("MRP");
                customViewHolder.itemMrTV.setText("WSP");
                customViewHolder.cumMrpTV.setText("Total");

                customViewHolder.brandTV.setBackgroundResource(R.drawable.rectangle_border_fill50);
                customViewHolder.stockTypeTV.setBackgroundResource(R.drawable.rectangle_border_fill50);
                customViewHolder.itemNoTV.setBackgroundResource(R.drawable.rectangle_border_fill50);
                customViewHolder.itemMrpTV.setBackgroundResource(R.drawable.rectangle_border_fill50);
                customViewHolder.itemMrTV.setBackgroundResource(R.drawable.rectangle_border_fill50);
                customViewHolder.cumMrpTV.setBackgroundResource(R.drawable.rectangle_border_fill50);

            } else {
                MrStockDisplayItem displayItem = displayItems.get(i-1);

                customViewHolder.brandTV.setText(displayItem.getBrand());
                customViewHolder.stockTypeTV.setText(displayItem.getStockType());
                customViewHolder.itemNoTV.setText(DataUtil.toString(displayItem.getCumNoStock()));
                customViewHolder.itemMrpTV.setText(DataUtil.toString(displayItem.getMrpPrice()));
                customViewHolder.itemMrTV.setText(DataUtil.toString(displayItem.getMrPrice()));
                customViewHolder.cumMrpTV.setText(DataUtil.toString(displayItem.getCumMrPrice()));

                customViewHolder.brandTV.setBackgroundResource(R.drawable.rectangle_border);
                customViewHolder.stockTypeTV.setBackgroundResource(R.drawable.rectangle_border);
                customViewHolder.itemNoTV.setBackgroundResource(R.drawable.rectangle_border);
                customViewHolder.itemMrpTV.setBackgroundResource(R.drawable.rectangle_border);
                customViewHolder.itemMrTV.setBackgroundResource(R.drawable.rectangle_border);
                customViewHolder.cumMrpTV.setBackgroundResource(R.drawable.rectangle_border);

                // Add OnClickListener for the Card List
                customViewHolder.brandTV.setOnClickListener(showItemListListener);
                customViewHolder.stockTypeTV.setOnClickListener(showItemListListener);
                customViewHolder.itemNoTV.setOnClickListener(showItemListListener);
                customViewHolder.itemMrpTV.setOnClickListener(showItemListListener);
                customViewHolder.itemMrTV.setOnClickListener(showItemListListener);
                customViewHolder.cumMrpTV.setOnClickListener(showItemListListener);

                customViewHolder.brandTV.setTag(customViewHolder);
                customViewHolder.stockTypeTV.setTag(customViewHolder);
                customViewHolder.itemNoTV.setTag(customViewHolder);
                customViewHolder.itemMrpTV.setTag(customViewHolder);
                customViewHolder.itemMrTV.setTag(customViewHolder);
                customViewHolder.cumMrpTV.setTag(customViewHolder);
            }
        }

        @Override
        public int getItemCount() {
            return (null != displayItems ? (displayItems.size() + 1) : 1);
        }

        public class CustomViewHolder extends RecyclerView.ViewHolder {
            protected TextView brandTV;
            protected TextView stockTypeTV;
            protected TextView itemNoTV;
            protected TextView itemMrpTV;
            protected TextView itemMrTV;
            protected TextView cumMrpTV;

            public CustomViewHolder(View view) {
                super(view);
                this.brandTV = (TextView) view.findViewById(R.id.r_brand);
                this.stockTypeTV = (TextView) view.findViewById(R.id.r_stock_type);
                this.itemNoTV = (TextView) view.findViewById(R.id.r_item_no);
                this.itemMrpTV = (TextView) view.findViewById(R.id.r_item_mrp);
                this.itemMrTV = (TextView) view.findViewById(R.id.r_item_mr);
                this.cumMrpTV = (TextView) view.findViewById(R.id.r_cum_mrp);
            }
        }

        View.OnClickListener showItemListListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomViewHolder holder = (CustomViewHolder) view.getTag();
                int position = holder.getAdapterPosition();

                if(position > 0) {
                    MrStockDisplayItem displayItem = displayItems.get(position - 1);

                    if(displayItem != null && displayItem.getItemDisplays() != null &&
                            !displayItem.getItemDisplays().isEmpty()) {

                        ItemListInfoDataHolder.instance().setItemDisplays(displayItem.getItemDisplays());
                        AndroidUtil.launchActivity(mContext, ItemListInfoActivity.class);
                    }
                }
            }
        };
    }

    protected class MrStockCaller extends HTTPCaller {
        public MrStockCaller(Activity activity, String call) {
            super(activity, call);
        }

        @Override
        public void httpSuccessCallback(String jsonResponce) {
            reloadData();
        }

        @Override
        public void httpFailureCallback(String message) {
            Toast.makeText(activity, "Loading Select Member is Failed!", Toast.LENGTH_SHORT).show();
//            AndroidUtil.launchActivity(activity, HomeActivity.class);
        }

        @Override
        public void httpErrorCallback(String message) {
            Toast.makeText(activity, "Loading Select Member is Failed!", Toast.LENGTH_SHORT).show();
//            AndroidUtil.launchActivity(activity, HomeActivity.class);
        }
    }

}
