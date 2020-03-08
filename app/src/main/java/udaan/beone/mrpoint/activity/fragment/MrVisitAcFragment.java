package udaan.beone.mrpoint.activity.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import udaan.beone.mrpoint.R;
import udaan.beone.mrpoint.http.model.MrAccount;
import udaan.beone.mrpoint.http.model.MrVisit;
import udaan.beone.mrpoint.http.model.MemberVisitAc;
import udaan.beone.mrpoint.http.util.HTTPCaller;
import udaan.beone.mrpoint.http.util.HTTPConst;
import udaan.beone.mrpoint.http.util.HTTPTask;
import udaan.beone.mrpoint.util.BDUtil;
import udaan.beone.mrpoint.util.DataUtil;
import udaan.beone.mrpoint.util.DateUtil;
import udaan.beone.mrpoint.util.EnumConst;

public abstract class MrVisitAcFragment extends CFragment {

    protected LinearLayout llAuth1;
    protected LinearLayout llAuth2;

    protected TextView rcFromV;
    protected TextView rcToV;
    protected TextView rcVisitsNoV;
    protected TextView rcAvgDurationMinV;

    protected TextView rcGivenV;
    protected TextView rcReturnV;
    protected TextView rcSoldV;
    protected TextView rcStockV;

    protected TextView rcCollectionV;
    protected TextView rcPaidV;
    protected TextView rcBalanceV;

    protected TextView rcAuthGivenV;
    protected TextView rcAuthReturnV;
    protected TextView rcAuthSoldV;

    protected RecyclerView mRecyclerView;
    protected MrVisitAcRecyclerAdapter adapter;
    protected ProgressBar progressBar;

    public MrVisitAcFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_mr_visit_ac, container, false);

        this.llAuth1 = (LinearLayout) fragment.findViewById(R.id.ll_auth_1);
        this.llAuth2 = (LinearLayout) fragment.findViewById(R.id.ll_auth_2);

        this.rcFromV = (TextView) fragment.findViewById(R.id.rc_from_v);
        this.rcToV = (TextView) fragment.findViewById(R.id.rc_to_v);
        this.rcVisitsNoV = (TextView) fragment.findViewById(R.id.rc_visits_no_v);
        this.rcAvgDurationMinV = (TextView) fragment.findViewById(R.id.rc_avg_duration_min_v);

        this.rcGivenV = (TextView) fragment.findViewById(R.id.rc_given_v);
        this.rcReturnV = (TextView) fragment.findViewById(R.id.rc_return_v);
        this.rcSoldV = (TextView) fragment.findViewById(R.id.rc_sold_v);
        this.rcStockV = (TextView) fragment.findViewById(R.id.rc_stock_v);

        this.rcAuthGivenV = (TextView) fragment.findViewById(R.id.rc_auth_given_v);
        this.rcAuthReturnV = (TextView) fragment.findViewById(R.id.rc_auth_return_v);
        this.rcAuthSoldV = (TextView) fragment.findViewById(R.id.rc_auth_sold_v);

        this.rcCollectionV = (TextView) fragment.findViewById(R.id.rc_auth_collection_v);
        this.rcPaidV = (TextView) fragment.findViewById(R.id.rc_auth_paid_v);
        this.rcBalanceV = (TextView) fragment.findViewById(R.id.rc_auth_balance_v);

        // Initialize recycler view
        mRecyclerView = (RecyclerView) fragment.findViewById(R.id.mr_account_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressBar = (ProgressBar) fragment.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        initRefreshData();

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void initRefreshData() {

        if(isMemberVisitAcDirty() || getMemberVisitAc() == null) {

            long durationOfVisitSearch = 0l;

            if (getMemberAccount() != null &&
                    getMemberAccount().getMrRole() != null &&
                    getMemberAccount().getMrRole().equals(EnumConst.MRole_Micro_Retailer)) {
                durationOfVisitSearch = System.currentTimeMillis() - (DateUtil.DAY * 30);
            } else {
                durationOfVisitSearch = System.currentTimeMillis() - (DateUtil.DAY * 3);
            }
            List<String> urlArgs = new ArrayList<String>(1);
            urlArgs.add(Long.toString(getMemberAcNo()));
            urlArgs.add(Long.toString(durationOfVisitSearch));
            urlArgs.add(Long.toString(System.currentTimeMillis()));

            HTTPTask task = new HTTPTask(new GetVisitsAcCaller(getActivity(), this, HTTPConst.VISIT_GET_VISITS_FOR_MEMBER),
                    null, HTTPConst.VISIT_GET_VISITS_FOR_MEMBER, urlArgs);
            task.execute();
        }
        else {
            reloadData();
        }
    }

    public abstract boolean isMemberVisitAcDirty();

    public abstract MemberVisitAc getMemberVisitAc();

    public abstract MrAccount getMemberAccount();

    public abstract long getMemberAcNo();

    public abstract void setMemberVisitAc(MemberVisitAc memberVisitAc);

    protected void reloadData() {
        // Set already Loaded Data Directly
        MemberVisitAc memberVisitAc = getMemberVisitAc();

        if(memberVisitAc != null && memberVisitAc.getMrVisits() != null && !memberVisitAc.getMrVisits().isEmpty()) {

            if (adapter == null) {
                adapter = new MrVisitAcRecyclerAdapter(getActivity(), memberVisitAc.getMrVisits(), getMemberAcNo());
                mRecyclerView.setAdapter(adapter);
            } else {
                adapter.setMrVisit(memberVisitAc.getMrVisits());
                adapter.notifyDataSetChanged();
            }

            float duration = 0;
            for(MrVisit mrVisit : memberVisitAc.getMrVisits()) {
                duration += (mrVisit.getEndTs() - mrVisit.getStartTs()) / DateUtil.MIN;
            }

            MrVisit lastMrVisit = memberVisitAc.getMrVisits().get(memberVisitAc.getMrVisits().size() - 1);

            rcFromV.setText(DateUtil.getDisplaySMSDateStr(memberVisitAc.getStartTs()));
            rcToV.setText(DateUtil.getDisplaySMSDateStr(memberVisitAc.getEndTs()));
            rcVisitsNoV.setText(DataUtil.toString(memberVisitAc.getTotalVisits()));
//            int avgMin = (int)(memberVisitAc.getDuration() /(DateUtil.MIN * memberVisitAc.getTotalVisits()));
            rcAvgDurationMinV.setText(DataUtil.toString((int)(duration/memberVisitAc.getTotalVisits())));

            rcGivenV.setText(DataUtil.stockToString(memberVisitAc.getGivenStockAm(), memberVisitAc.getGivenStockNo()));
            rcReturnV.setText(DataUtil.stockToString(memberVisitAc.getReturnStockAm(), memberVisitAc.getReturnStockNo()));
            rcSoldV.setText(DataUtil.stockToString(memberVisitAc.getSoldStockAm(), memberVisitAc.getSoldStockNo()));

            BigDecimal paidBalanceAm = BDUtil.add(memberVisitAc.getPaidSoldAm(),
                    BDUtil.add(memberVisitAc.getPaidInterestPenaltyAm(),
                            BDUtil.add(memberVisitAc.getPaidRegistrationAm(),
                                    BDUtil.add(memberVisitAc.getPaidDepositAm(),
                                            BDUtil.add(memberVisitAc.getPaidReturnedDepositAm(), memberVisitAc.getPaidCollectedAm())))));

            BigDecimal collectedBalanceAm = BDUtil.add(memberVisitAc.getCollectedSoldAm(),
                    BDUtil.add(memberVisitAc.getCollectedInterestPenaltyAm(),
                            BDUtil.add(memberVisitAc.getCollectedRegistrationAm(),
                                    BDUtil.add(memberVisitAc.getCollectedDepositAm(),
                                            BDUtil.add(memberVisitAc.getCollectedReturnedDepositAm(), memberVisitAc.getCollectedCollectedAm())))));

            rcPaidV.setText(DataUtil.toString(paidBalanceAm));
            rcCollectionV.setText(DataUtil.toString(collectedBalanceAm));

            if(getMemberAcNo() == lastMrVisit.getOwnerAcNo()) {
                rcStockV.setText(DataUtil.stockToString(lastMrVisit.getClosingStockAm(), lastMrVisit.getClosingStockNo()));

                BigDecimal closingBalanceAm = BDUtil.add(lastMrVisit.getClosingOutstandingAm(),
                        BDUtil.add(lastMrVisit.getClosingInterestPenaltyAm(),
                                BDUtil.add(lastMrVisit.getClosingRegistrationAm(),
                                        BDUtil.add(lastMrVisit.getClosingDepositAm(), lastMrVisit.getClosingCollectedAm()))));

                rcBalanceV.setText(DataUtil.toString(closingBalanceAm));
            }
            else if(getMemberAcNo() == lastMrVisit.getAuthAcNo()) {

                rcStockV.setText(DataUtil.stockToString(lastMrVisit.getAuthStockAm(), lastMrVisit.getAuthStockNo()));
                rcBalanceV.setText(DataUtil.toString(lastMrVisit.getAuthBalanceAm()));
            }

            if (memberVisitAc.getAuthVisits() > 0) {
                llAuth1.setVisibility(View.VISIBLE);
                llAuth2.setVisibility(View.VISIBLE);

                rcAuthGivenV.setText(DataUtil.stockToString(memberVisitAc.getGivenAsAuthStockAm(), memberVisitAc.getGivenAsAuthStockNo()));
                rcAuthReturnV.setText(DataUtil.stockToString(memberVisitAc.getReturnAsAuthStockAm(), memberVisitAc.getReturnAsAuthStockNo()));
                rcAuthSoldV.setText(DataUtil.stockToString(memberVisitAc.getSoldAsAuthStockAm(), memberVisitAc.getSoldAsAuthStockNo()));
            } else {
                llAuth1.setVisibility(View.GONE);
                llAuth2.setVisibility(View.GONE);
            }
        }

        progressBar.setVisibility(View.GONE);
    }

    public class MrVisitAcRecyclerAdapter extends RecyclerView.Adapter<MrVisitAcRecyclerAdapter.VisitItemViewHolder> {
        private Activity mContext;
        private List<MrVisit> mrVisits;
        private long selectedMemberAcNo;

        public MrVisitAcRecyclerAdapter(Activity context, List<MrVisit> mrVisits, long selectedMemberAcNo) {
            this.mContext = context;
            this.mrVisits = mrVisits;
            this.selectedMemberAcNo = selectedMemberAcNo;
        }
        public void setMrVisit(List<MrVisit> mrVisits) {
            this.mrVisits = mrVisits;
        }

        @Override
        public VisitItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            VisitItemViewHolder viewHolder = null;

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rc_row_mr_visit_ac, null);
            viewHolder = new VisitItemViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(VisitItemViewHolder customViewHolder, int i) {

            // Set Data for the Card List
            customViewHolder.setData(mrVisits.get(i), i);

            // Add OnClickListener for the Card List
            customViewHolder.setOnClickListener(clickListener);

            // Set Tag for the Card List
            customViewHolder.setTag(customViewHolder);
        }

        @Override
        public int getItemCount() {
            return ((mrVisits != null) ? mrVisits.size() : 0);
        }

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisitItemViewHolder holder = (VisitItemViewHolder) view.getTag();
                int position = holder.getAdapterPosition();

                if (position > 0) {
                    MrVisit mrVisit = mrVisits.get(position - 1);
                    showItemInfo(position, mrVisit);
                }
            }
        };
        public void showItemInfo(int position, MrVisit mrVisit) {
//            new SelectedItemInfoDialog(mContext, this, position, item).show();

        }

        public class VisitItemViewHolder extends RecyclerView.ViewHolder {

            protected TextView rcVisitNo;
            protected TextView rcStartTime;
            protected TextView rcDurationMin;
            protected TextView rcName;

            protected TextView rcGiven;
            protected TextView rcReturn;
            protected TextView rcSold;

            protected TextView rcPaid;
            protected TextView rcBalance;
            protected TextView rcStock;

            protected TextView rcVisitNoV;
            protected TextView rcStartTimeV;
            protected TextView rcDurationMinV;
            protected TextView rcNameV;

            protected TextView rcGivenV;
            protected TextView rcReturnV;
            protected TextView rcSoldV;

            protected TextView rcPaidV;
            protected TextView rcBalanceV;
            protected TextView rcStockV;

            protected LinearLayout rcStockTxLL1;
            protected LinearLayout rcStockTxLL2;

            public VisitItemViewHolder(View view) {
                super(view);

                this.rcVisitNo = (TextView) view.findViewById(R.id.rc_visit_no);
                this.rcStartTime = (TextView) view.findViewById(R.id.rc_start_time);
                this.rcDurationMin = (TextView) view.findViewById(R.id.rc_duration_min);
                this.rcName = (TextView) view.findViewById(R.id.rc_name);

                this.rcGiven = (TextView) view.findViewById(R.id.rc_given);
                this.rcReturn = (TextView) view.findViewById(R.id.rc_return);
                this.rcSold = (TextView) view.findViewById(R.id.rc_sold);

                this.rcPaid = (TextView) view.findViewById(R.id.rc_paid);
                this.rcBalance = (TextView) view.findViewById(R.id.rc_balance);
                this.rcStock = (TextView) view.findViewById(R.id.rc_stock);

                this.rcVisitNoV = (TextView) view.findViewById(R.id.rc_visit_no_v);
                this.rcStartTimeV = (TextView) view.findViewById(R.id.rc_start_time_v);
                this.rcDurationMinV = (TextView) view.findViewById(R.id.rc_duration_min_v);
                this.rcNameV = (TextView) view.findViewById(R.id.rc_name_v);

                this.rcGivenV = (TextView) view.findViewById(R.id.rc_given_v);
                this.rcReturnV = (TextView) view.findViewById(R.id.rc_return_v);
                this.rcSoldV = (TextView) view.findViewById(R.id.rc_sold_v);

                this.rcPaidV = (TextView) view.findViewById(R.id.rc_paid_v);
                this.rcBalanceV = (TextView) view.findViewById(R.id.rc_balance_v);
                this.rcStockV = (TextView) view.findViewById(R.id.rc_stock_v);

                this.rcStockTxLL1 = (LinearLayout) view.findViewById(R.id.rc_stock_tx_ll1);
                this.rcStockTxLL2 = (LinearLayout) view.findViewById(R.id.rc_stock_tx_ll2);
            }

            public void setData(MrVisit mrVisit, int i){

                if(mrVisit.getOwnerAcNo() == selectedMemberAcNo) {
                    rcName.setText(DataUtil.toString("Auth Name"));
                    rcNameV.setText(DataUtil.toString(mrVisit.getAuthName()));

                    this.rcVisitNo.setBackgroundResource(R.drawable.rectangle_border_fill100);
                    this.rcStartTime.setBackgroundResource(R.drawable.rectangle_border_fill100);
                    this.rcDurationMin.setBackgroundResource(R.drawable.rectangle_border_fill100);
                    this.rcName.setBackgroundResource(R.drawable.rectangle_border_fill100);

                    this.rcGiven.setBackgroundResource(R.drawable.rectangle_border_fill50);
                    this.rcReturn.setBackgroundResource(R.drawable.rectangle_border_fill50);
                    this.rcSold.setBackgroundResource(R.drawable.rectangle_border_fill50);

                    this.rcPaid.setBackgroundResource(R.drawable.rectangle_border_fill50);
                    this.rcBalance.setBackgroundResource(R.drawable.rectangle_border_fill50);
                    this.rcStock.setBackgroundResource(R.drawable.rectangle_border_fill50);

                    this.rcVisitNoV.setBackgroundResource(R.drawable.rectangle_border);
                    this.rcStartTimeV.setBackgroundResource(R.drawable.rectangle_border);
                    this.rcDurationMinV.setBackgroundResource(R.drawable.rectangle_border);
                    this.rcNameV.setBackgroundResource(R.drawable.rectangle_border);

                    this.rcGivenV.setBackgroundResource(R.drawable.rectangle_border);
                    this.rcReturnV.setBackgroundResource(R.drawable.rectangle_border);
                    this.rcSoldV.setBackgroundResource(R.drawable.rectangle_border);

                    this.rcPaidV.setBackgroundResource(R.drawable.rectangle_border);
                    this.rcBalanceV.setBackgroundResource(R.drawable.rectangle_border);
                    this.rcStockV.setBackgroundResource(R.drawable.rectangle_border);

                } else {

                    rcName.setText(DataUtil.toString("Owner Name"));
                    rcNameV.setText(DataUtil.toString(mrVisit.getOwnerName()));

                    this.rcVisitNo.setBackgroundResource(R.drawable.rectangle_border_lb_fill100);
                    this.rcStartTime.setBackgroundResource(R.drawable.rectangle_border_lb_fill100);
                    this.rcDurationMin.setBackgroundResource(R.drawable.rectangle_border_lb_fill100);
                    this.rcName.setBackgroundResource(R.drawable.rectangle_border_lb_fill100);

                    this.rcGiven.setBackgroundResource(R.drawable.rectangle_border_lb_fill50);
                    this.rcReturn.setBackgroundResource(R.drawable.rectangle_border_lb_fill50);
                    this.rcSold.setBackgroundResource(R.drawable.rectangle_border_lb_fill50);

                    this.rcPaid.setBackgroundResource(R.drawable.rectangle_border_lb_fill50);
                    this.rcBalance.setBackgroundResource(R.drawable.rectangle_border_lb_fill50);
                    this.rcStock.setBackgroundResource(R.drawable.rectangle_border_lb_fill50);

                    this.rcVisitNoV.setBackgroundResource(R.drawable.rectangle_border_lb);
                    this.rcStartTimeV.setBackgroundResource(R.drawable.rectangle_border_lb);
                    this.rcDurationMinV.setBackgroundResource(R.drawable.rectangle_border_lb);
                    this.rcNameV.setBackgroundResource(R.drawable.rectangle_border_lb);

                    this.rcGivenV.setBackgroundResource(R.drawable.rectangle_border_lb);
                    this.rcReturnV.setBackgroundResource(R.drawable.rectangle_border_lb);
                    this.rcSoldV.setBackgroundResource(R.drawable.rectangle_border_lb);

                    this.rcPaidV.setBackgroundResource(R.drawable.rectangle_border_lb);
                    this.rcBalanceV.setBackgroundResource(R.drawable.rectangle_border_lb);
                    this.rcStockV.setBackgroundResource(R.drawable.rectangle_border_lb);
                }

                rcVisitNoV.setText(DataUtil.toString(i + 1));
                rcStartTimeV.setText(DateUtil.getDisplayDateTimeStr(mrVisit.getStartTs()));
                rcDurationMinV.setText(DataUtil.toString((mrVisit.getEndTs() - mrVisit.getStartTs()) / DateUtil.MIN));

                if(mrVisit.getGivenStockNo() > 0 || mrVisit.getReturnStockNo() > 0 || mrVisit.getSoldStockNo() > 0) {
                    rcStockTxLL1.setVisibility(View.VISIBLE);
                    rcStockTxLL2.setVisibility(View.VISIBLE);

                    rcGivenV.setText(DataUtil.stockToString(mrVisit.getGivenStockAm(), mrVisit.getGivenStockNo()));
                    rcReturnV.setText(DataUtil.stockToString(mrVisit.getReturnStockAm(), mrVisit.getReturnStockNo()));
                    rcSoldV.setText(DataUtil.stockToString(mrVisit.getSoldStockAm(), mrVisit.getSoldStockNo()));
                } else {
                    rcStockTxLL1.setVisibility(View.GONE);
                    rcStockTxLL2.setVisibility(View.GONE);
                }

                if(mrVisit.getVisitType().equals(EnumConst.VisitType_Direct_Sold)) {

                    rcName.setText(DataUtil.toString("Direct Sold"));
                    rcNameV.setText(DataUtil.stockToString(mrVisit.getSoldStockAm(), mrVisit.getSoldStockNo()));

                    rcStockTxLL1.setVisibility(View.GONE);
                    rcStockTxLL2.setVisibility(View.GONE);
                }

                BigDecimal paidBalanceAm = BDUtil.add(mrVisit.getSoldPaidAm(),
                        BDUtil.add(mrVisit.getPaidInterestPenaltyAm(),
                                BDUtil.add(mrVisit.getPaidRegistrationAm(),
                                        BDUtil.add(mrVisit.getPaidDepositAm(), mrVisit.getPaidCollectedAm()))));
                BigDecimal closingBalanceAm = BDUtil.add(mrVisit.getClosingOutstandingAm(),
                        BDUtil.add(mrVisit.getClosingInterestPenaltyAm(),
                                BDUtil.add(mrVisit.getClosingRegistrationAm(),
                                        BDUtil.add(mrVisit.getClosingDepositAm(), mrVisit.getClosingCollectedAm()))));

                rcPaidV.setText(DataUtil.toString(paidBalanceAm));
                rcBalanceV.setText(DataUtil.toString(closingBalanceAm));
                rcStockV.setText(DataUtil.stockToString(mrVisit.getClosingStockAm(), mrVisit.getClosingStockNo()));
            }
            public void setOnClickListener(View.OnClickListener clickListener) {

                rcName.setOnClickListener(clickListener);
                rcVisitNoV.setOnClickListener(clickListener);
                rcStartTimeV.setOnClickListener(clickListener);
                rcDurationMinV.setOnClickListener(clickListener);
                rcNameV.setOnClickListener(clickListener);

                rcGivenV.setOnClickListener(clickListener);
                rcReturnV.setOnClickListener(clickListener);
                rcSoldV.setOnClickListener(clickListener);
                rcStockV.setOnClickListener(clickListener);

                rcPaidV.setOnClickListener(clickListener);
                rcBalanceV.setOnClickListener(clickListener);
            }
            public void setTag(VisitItemViewHolder customViewHolder) {

                rcName.setTag(customViewHolder);
                rcVisitNoV.setTag(customViewHolder);
                rcStartTimeV.setTag(customViewHolder);
                rcDurationMinV.setTag(customViewHolder);
                rcNameV.setTag(customViewHolder);

                rcGivenV.setTag(customViewHolder);
                rcReturnV.setTag(customViewHolder);
                rcSoldV.setTag(customViewHolder);
                rcStockV.setTag(customViewHolder);

                rcPaidV.setTag(customViewHolder);
                rcBalanceV.setTag(customViewHolder);
            }
        }
    }

    public class GetVisitsAcCaller extends HTTPCaller {
        private MrVisitAcFragment fragment;

        public GetVisitsAcCaller(Activity activity, MrVisitAcFragment fragment, String call) {
            super(activity, call);
            this.fragment = fragment;
        }
        public void httpSuccessCallback(String jsonResponce){

            if(jsonResponce != null && !jsonResponce.isEmpty()) {
                MemberVisitAc memberVisitAc = (MemberVisitAc) MemberVisitAc.JSONRepo.buildObject(jsonResponce);
                if(memberVisitAc != null) {
                    setMemberVisitAc(memberVisitAc);
                    fragment.reloadData();

                    Toast.makeText(activity, call + " is Successful!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(activity, "Visit Account Not Available!", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(activity, "Visit Account Not Available!", Toast.LENGTH_SHORT).show();
            }

            progressBar.setVisibility(View.GONE);
        }
        public void httpFailureCallback(String message) {
            progressBar.setVisibility(View.GONE);

            Log.e("Udaan", "Call: " + call + " is Failed!");
            Toast.makeText(activity, "Call: " + call + " is Failed! Please try Again!", Toast.LENGTH_SHORT).show();
        }
        public void httpErrorCallback(String message) {
            progressBar.setVisibility(View.GONE);

            Log.e("Udaan", "Call: " + call + " is Failed!");
            Toast.makeText(activity, "Call: " + call + " is Failed! Please try Again!", Toast.LENGTH_SHORT).show();
        }
    }

}
