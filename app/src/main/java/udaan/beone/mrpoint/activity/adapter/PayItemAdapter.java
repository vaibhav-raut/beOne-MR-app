package udaan.beone.mrpoint.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import udaan.beone.mrpoint.R;
import udaan.beone.mrpoint.activity.ActionPayActivity;
import udaan.beone.mrpoint.data.DataManager;
import udaan.beone.mrpoint.http.model.PayTransfer.PayTx;
import udaan.beone.mrpoint.http.model.StockItemL;
import udaan.beone.mrpoint.util.BDUtil;
import udaan.beone.mrpoint.util.DataUtil;
import udaan.beone.mrpoint.util.EnumConst;

/**
 * Created by Vaibhav on 22-05-2016.
 */

public class PayItemAdapter extends RecyclerView.Adapter<PayItemAdapter.PayItemViewHolder> {

    private List<PayTx> payTxs;
    private ActionPayActivity mContext;
    private BigDecimal currentPayment;

    public PayItemAdapter(ActionPayActivity context) {
        payTxs = new LinkedList<PayTx>();
        this.mContext = context;
        currentPayment = DataUtil.ZERO_BIG_DECIMAL;
    }

    public List<PayTx> getPayTxs() {
        return payTxs;
    }
    public List<PayTx> getPayTxsToPay() {
        List<PayTx> toPayTxs = new ArrayList<PayTx>(payTxs.size());
        for(PayTx payTx: payTxs) {
            if(payTx.getPayAmount().compareTo(DataUtil.ZERO_BIG_DECIMAL) > 0) {
                toPayTxs.add(payTx);
            }
        }
        return toPayTxs;
    }
    public void addPayTx(PayTx payTx) {
        if(payTxs == null) {
            payTxs = new LinkedList<PayTx>();
        }
        this.payTxs.add(payTx);
        notifyItemInserted(payTxs.size());
    }
    public void setPayTxs(List<PayTx> payTxs) {
        this.payTxs = payTxs;
        notifyDataSetChanged();
    }
    public void deleteItem(int position) {
        this.payTxs.remove(position - 1);
        notifyItemRemoved(position);
    }
    public void calculatePay(BigDecimal currentPayment) {
        this.currentPayment = currentPayment;
        calculatePay();
    }
    protected void calculatePay() {
        BigDecimal balancePayment = currentPayment;

        if(balancePayment.compareTo(DataUtil.ZERO_BIG_DECIMAL) > 0) {
            boolean balanceExhausted = false;

            if(payTxs != null && !payTxs.isEmpty()) {
                for (PayTx payTx : payTxs) {
                    if (balanceExhausted) {
                        payTx.setPayAmount(DataUtil.ZERO_BIG_DECIMAL);
                    } else if (balancePayment.compareTo(payTx.getOutstandingAmount()) >= 0) {
                        payTx.setPayAmount(payTx.getOutstandingAmount());
                        balancePayment = BDUtil.sub(balancePayment, payTx.getOutstandingAmount());

                        if (balancePayment.compareTo(DataUtil.ZERO_BIG_DECIMAL) == 0) {
                            balanceExhausted = true;
                        }
                    } else {
                        payTx.setPayAmount(balancePayment);
                        balancePayment = DataUtil.ZERO_BIG_DECIMAL;
                        balanceExhausted = true;
                    }
                }
            }

            if(!balanceExhausted) {
                boolean advanceAllocated = false;
                if(payTxs != null && !payTxs.isEmpty()) {
                    for (PayTx payTx : payTxs) {
                        if (DataManager.instance().getOwnerAccount().getMrRole().equals(EnumConst.MRole_Micro_Retailer)) {
                            if (payTx.getStockTxType().equals(EnumConst.StockTxType_Sold_Paid)) {
                                payTx.setPayAmount(BDUtil.add(payTx.getPayAmount(), balancePayment));
                                advanceAllocated = true;
                            }
                        } else {
                            if (payTx.getStockTxType().equals(EnumConst.StockTxType_Collection_Paid)) {
                                payTx.setPayAmount(BDUtil.add(payTx.getPayAmount(), balancePayment));
                                advanceAllocated = true;
                            }
                        }
                    }
                }
                if(!advanceAllocated) {
                    PayTx payTx = new PayTx();
                    payTx.setOutstandingAmount(DataUtil.ZERO_BIG_DECIMAL);
                    payTx.setPayAmount(balancePayment);

                    if(DataManager.instance().getOwnerAccount().getMrRole().equals(EnumConst.MRole_Micro_Retailer)) {
                        payTx.setStockTxType(EnumConst.StockTxType_Sold_Paid);
                        payTx.setDescription("Advance added to Sold Paid");
                    }
                    else {
                        payTx.setStockTxType(EnumConst.StockTxType_Collection_Paid);
                        payTx.setDescription("Advance added to Sold Paid");
                    }
                    addPayTx(payTx);
                }
            }
        }
        notifyDataSetChanged();
    }
    protected void showItemInfo(int position, PayTx payTx) {
//        new SelectedItemInfoDialog(mContext, this, position, payTx).show();
    }

    @Override
    public PayItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rc_row_pay_item, null);

        PayItemViewHolder viewHolder = new PayItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PayItemViewHolder viewHolder, int i) {
        PayTx payTx = null;
        if(i > 0) {
            payTx = payTxs.get(i - 1);
        }

        //Setting text view title
        viewHolder.setData(payTx, i);

        // Add OnClickListener for the Card List
        viewHolder.setOnClickListener();

        viewHolder.setTag(viewHolder);
    }

    @Override
    public int getItemCount() {
        return (null != payTxs ? (payTxs.size() + 1) : 1);
    }

    View.OnClickListener itemDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PayItemViewHolder holder = (PayItemViewHolder) view.getTag();
            int position = holder.getAdapterPosition();

            if (position > 0) {
                PayTx payTx = payTxs.get(position - 1);
                deleteItem(position);
                calculatePay();
            }
        }
    };

    View.OnClickListener itemInfoListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PayItemViewHolder holder = (PayItemViewHolder) view.getTag();
            int position = holder.getAdapterPosition();

            if (position > 0) {
                PayTx payTx = payTxs.get(position - 1);
                showItemInfo(position, payTx);
            }
        }
    };

    public class PayItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView rsPayType;
        protected TextView rsOutstanding;
        protected TextView rsPay;
        protected TextView rsBalance;
        protected TextView rsDeleteItem;

        public PayItemViewHolder(View view) {
            super(view);
            this.rsPayType = (TextView) view.findViewById(R.id.rs_pay_type);
            this.rsOutstanding = (TextView) view.findViewById(R.id.rs_outstnding);
            this.rsPay = (TextView) view.findViewById(R.id.rs_pay);
            this.rsBalance = (TextView) view.findViewById(R.id.rs_balance);
            this.rsDeleteItem = (TextView) view.findViewById(R.id.rs_delete_item);
        }

        public void setData(PayTx payTx, int i) {

            if(i == 0) {
                //Setting text view title
                this.rsPayType.setText("Type");
                this.rsOutstanding.setText("Old");
                this.rsPay.setText("Pay");
                this.rsBalance.setText("Balance");
                this.rsDeleteItem.setText("");

                this.rsPayType.setBackgroundResource(R.drawable.rectangle_border_fill50);
                this.rsOutstanding.setBackgroundResource(R.drawable.rectangle_border_fill50);
                this.rsPay.setBackgroundResource(R.drawable.rectangle_border_fill50);
                this.rsBalance.setBackgroundResource(R.drawable.rectangle_border_fill50);
                this.rsDeleteItem.setBackgroundResource(R.drawable.rectangle_border_fill50);
            } else {
                //Setting text view data
                this.rsPayType.setText(payTx.getStockTxType());
                this.rsOutstanding.setText(DataUtil.toString(payTx.getOutstandingAmount()));
                this.rsPay.setText(DataUtil.toString(payTx.getPayAmount()));
                this.rsBalance.setText(DataUtil.toString(BDUtil.sub(payTx.getOutstandingAmount(), payTx.getPayAmount())));
                this.rsDeleteItem.setText("X");

                this.rsPayType.setBackgroundResource(R.drawable.rectangle_border);
                this.rsOutstanding.setBackgroundResource(R.drawable.rectangle_border);
                this.rsPay.setBackgroundResource(R.drawable.rectangle_border);
                this.rsBalance.setBackgroundResource(R.drawable.rectangle_border);
                this.rsDeleteItem.setBackgroundResource(R.drawable.rectangle_border);
            }
        }

        public void setOnClickListener() {

            // Add OnClickListener for the Card List
            rsPayType.setOnClickListener(itemInfoListener);
            rsOutstanding.setOnClickListener(itemInfoListener);
            rsPay.setOnClickListener(itemInfoListener);
            rsBalance.setOnClickListener(itemInfoListener);
            rsDeleteItem.setOnClickListener(itemDeleteListener);
        }

        public void setTag(PayItemViewHolder holder) {

            this.rsPayType.setTag(holder);
            this.rsOutstanding.setTag(holder);
            this.rsPay.setTag(holder);
            this.rsBalance.setTag(holder);
            this.rsDeleteItem.setTag(holder);
        }
    }
}

