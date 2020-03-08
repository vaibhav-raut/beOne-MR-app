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

import udaan.beone.mrpoint.R;
import udaan.beone.mrpoint.data.DataManager;
import udaan.beone.mrpoint.http.model.MemberContactREST;
import udaan.beone.mrpoint.http.model.MrAccount;
import udaan.beone.mrpoint.http.util.HTTPCaller;
import udaan.beone.mrpoint.util.DataUtil;
import udaan.beone.mrpoint.util.EnumConst;

public abstract class MrAccountShortFragment extends CFragment {

//    private OnFragmentInteractionListener mListener;

    protected RecyclerView mRecyclerView;
    protected MyRecyclerAdapter adapter;
    protected ProgressBar progressBar;


    public MrAccountShortFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_mr_account, container, false);

        // Initialize recycler view
        mRecyclerView = (RecyclerView) fragment.findViewById(R.id.mr_account_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0,0);

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
        private MrAccount mrAccount;
        private Activity mContext;

        public MyRecyclerAdapter(Activity context, MrAccount mrAccount) {
            this.mrAccount = mrAccount;
            this.mContext = context;
        }
        public void setMrAccount(MrAccount mrAccount) {
            this.mrAccount = mrAccount;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            CustomViewHolder viewHolder = null;

            switch(viewType) {
                case 0: {
                    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rc_row_mr_account_details_s, null);
                    viewHolder = new MemberDetailsViewHolder(view);
                    break;
                }

                case 1: {
                    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rc_row_mr_account_common_s, null);
                    viewHolder = new CommonViewHolder(view);
                    break;
                }

                case 2: {
                    if (mrAccount.getMrRole().equals(EnumConst.MRole_Micro_Retailer)) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rc_row_mr_account_mr, null);
                        viewHolder = new MrViewHolder(view);
                    } else {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rc_row_mr_account_non_mr, null);
                        viewHolder = new NonMrViewHolder(view);
                    }
                    break;
                }

                case 3:
                case 4:
                case 5:
                {
                    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rc_row_mr_account_stats_s, null);
                    viewHolder = new MrStatsViewHolder(view);
                    break;
                }
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {

            // Set Data for the Card List
            customViewHolder.setData(mrAccount, i);

            // Add OnClickListener for the Card List
//            customViewHolder.setOnClickListener(clickListener);

            // Set Tag for the Card List
            customViewHolder.setTag(customViewHolder);
        }

        @Override
        public int getItemCount() {
            int count = (null != mrAccount.getMrStats() ? (mrAccount.getMrStats().size() + 3) : 3);
            return count;
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }

        abstract public class CustomViewHolder extends RecyclerView.ViewHolder {
            public CustomViewHolder(View view) {
                super(view);
            }

            abstract public void setData(MrAccount mrAccount, int i);
            abstract public void setOnClickListener(View.OnClickListener clickListener);
            abstract public void setTag(CustomViewHolder customViewHolder);
        }


        public class MemberDetailsViewHolder extends CustomViewHolder {

            protected TextView nameTV;
            protected TextView accountNoTV;

            protected TextView mobile1TV;
            protected TextView mobile2TV;
            protected TextView phoneTV;

            public MemberDetailsViewHolder(View view) {
                super(view);

                nameTV = (TextView) view.findViewById(R.id.rc_name_v);
                accountNoTV = (TextView) view.findViewById(R.id.rc_account_no_v);

                mobile1TV = (TextView) view.findViewById(R.id.rc_mobile_1_v);
                mobile2TV = (TextView) view.findViewById(R.id.rc_mobile_2_v);
                phoneTV = (TextView) view.findViewById(R.id.rc_phone_v);
            }

            public void setData(MrAccount mrAccount, int i){

                nameTV.setText(mrAccount.getMemberInfo().getMemberName());
                if(mrAccount.getMemberInfo().getMemberAcNo() == DataManager.instance().getSelectedMemberAcNo()) {
                    accountNoTV.setText(DataManager.instance().getDisplaySelectedMemberAcNo());
                } else {
                    accountNoTV.setText(DataManager.instance().getDisplayLoginMemberAcNo());
                }

                if(mrAccount.getMemberInfo().getContacts() != null && !mrAccount.getMemberInfo().getContacts().isEmpty()) {

                    MemberContactREST contact = mrAccount.getMemberInfo().getContacts().get(0);
                    mobile1TV.setText(contact.getPriMobile());
                    mobile2TV.setText(contact.getSecMobile());
                    phoneTV.setText(contact.getPhone());
                }
            }

            public void setOnClickListener(View.OnClickListener clickListener) {

//                nameTV.setOnClickListener(clickListener);
//                accountNoTV.setOnClickListener(clickListener);
//
//                mobile1TV.setOnClickListener(clickListener);
//                mobile2TV.setOnClickListener(clickListener);
//                phoneTV.setOnClickListener(clickListener);
            }
            public void setTag(CustomViewHolder customViewHolder) {

                nameTV.setTag(customViewHolder);
                accountNoTV.setTag(customViewHolder);

                mobile1TV.setTag(customViewHolder);
                mobile2TV.setTag(customViewHolder);
                phoneTV.setTag(customViewHolder);
            }
        }

        public class CommonViewHolder extends CustomViewHolder {

            protected final TextView rcRoleTV;
            protected final TextView rcStatusTV;
            protected final TextView rcTypeTV;
            protected final TextView rcSoldAmountTV;
            protected final TextView rcCurrentStockTV;

            public CommonViewHolder(View view) {
                super(view);

                this.rcRoleTV = (TextView) view.findViewById(R.id.rc_role_v);
                this.rcStatusTV = (TextView) view.findViewById(R.id.rc_status_v);
                this.rcTypeTV = (TextView) view.findViewById(R.id.rc_type_v);
                this.rcSoldAmountTV = (TextView) view.findViewById(R.id.rc_sold_amount_v);
                this.rcCurrentStockTV = (TextView) view.findViewById(R.id.rc_current_stock_v);

            }

            public void setData(MrAccount mrAccount, int i){
                rcRoleTV.setText(mrAccount.getMrRole());
                rcStatusTV.setText(mrAccount.getMrStatus());
                rcTypeTV.setText(mrAccount.getMrType());
                rcSoldAmountTV.setText(DataUtil.toString(mrAccount.getSoldPaidAm(), mrAccount.getSoldPendingAm()));
                rcCurrentStockTV.setText(DataUtil.stockToString(mrAccount.getCurrentStockAm(), mrAccount.getCurrentStockNo()));
            }
            public void setOnClickListener(View.OnClickListener clickListener) {

//                rcRoleTV.setOnClickListener(clickListener);
//                rcStatusTV.setOnClickListener(clickListener);
//                rcTypeTV.setOnClickListener(clickListener);
//                rcSoldAmountTV.setOnClickListener(clickListener);
//                rcCurrentStockTV.setOnClickListener(clickListener);
            }
            public void setTag(CustomViewHolder customViewHolder) {

                rcRoleTV.setTag(customViewHolder);
                rcStatusTV.setTag(customViewHolder);
                rcTypeTV.setTag(customViewHolder);
                rcSoldAmountTV.setTag(customViewHolder);
                rcCurrentStockTV.setTag(customViewHolder);
            }
        }

        public class MrViewHolder extends CustomViewHolder {

            protected TextView rmRegistrationAmPaidTV;
            protected TextView rmRegistrationAmPendingTV;
            protected TextView rmDepositAmPaidTV;
            protected TextView rmDepositAmPendingTV;
            protected TextView rmDepositAmReturnedTV;
            protected TextView rmCreditLimitTV;
            protected TextView rmLastVisitOnTV;
            protected TextView rmIndexPerformanceTV;
            protected TextView rmIndexReturnTV;
            protected TextView rmIndexSalesTV;

            public MrViewHolder(View view) {
                super(view);

                this.rmRegistrationAmPaidTV = (TextView) view.findViewById(R.id.rm_registration_am_paid_v);
                this.rmRegistrationAmPendingTV = (TextView) view.findViewById(R.id.rm_registration_am_pending_v);
                this.rmDepositAmPaidTV = (TextView) view.findViewById(R.id.rm_deposit_am_paid_v);
                this.rmDepositAmPendingTV = (TextView) view.findViewById(R.id.rm_deposit_am_pending_v);
                this.rmDepositAmReturnedTV = (TextView) view.findViewById(R.id.rm_deposit_am_returned_v);
                this.rmCreditLimitTV = (TextView) view.findViewById(R.id.rm_credit_limit_v);
                this.rmLastVisitOnTV = (TextView) view.findViewById(R.id.rm_last_visit_on_v);
                this.rmIndexPerformanceTV = (TextView) view.findViewById(R.id.rm_index_performance_v);
                this.rmIndexReturnTV = (TextView) view.findViewById(R.id.rm_index_return_v);
                this.rmIndexSalesTV = (TextView) view.findViewById(R.id.rm_index_sales_v);
            }

            public void setData(MrAccount mrAccount, int i){

                rmRegistrationAmPaidTV.setText(DataUtil.toString(mrAccount.getRegistrationPaidAm()));
                rmRegistrationAmPendingTV.setText(DataUtil.toString(mrAccount.getRegistrationPendingAm()));
                rmDepositAmPaidTV.setText(DataUtil.toString(mrAccount.getDepositPaidAm()));
                rmDepositAmPendingTV.setText(DataUtil.toString(mrAccount.getDepositPendingAm()));
                rmDepositAmReturnedTV.setText(DataUtil.toString(mrAccount.getDepositReturnAm()));
                rmCreditLimitTV.setText(DataUtil.toString(mrAccount.getCreditLimitAm()));
                rmLastVisitOnTV.setText(DataUtil.convertTimeToDisplayDate(mrAccount.getLastVisitOn()));
                rmIndexPerformanceTV.setText(Float.toString(mrAccount.getPerformanceIndex()));
                rmIndexReturnTV.setText(Float.toString(mrAccount.getReturnIndex()));
                rmIndexSalesTV.setText(Float.toString(mrAccount.getSalesIndex()));

            }
            public void setOnClickListener(View.OnClickListener clickListener) {

//                rmRegistrationAmPaidTV.setOnClickListener(clickListener);
//                rmRegistrationAmPendingTV.setOnClickListener(clickListener);
//                rmDepositAmPaidTV.setOnClickListener(clickListener);
//                rmDepositAmPendingTV.setOnClickListener(clickListener);
//                rmDepositAmReturnedTV.setOnClickListener(clickListener);
//                rmCreditLimitTV.setOnClickListener(clickListener);
//                rmLastVisitOnTV.setOnClickListener(clickListener);
//                rmIndexPerformanceTV.setOnClickListener(clickListener);
//                rmIndexReturnTV.setOnClickListener(clickListener);
//                rmIndexSalesTV.setOnClickListener(clickListener);

            }
            public void setTag(CustomViewHolder customViewHolder) {

                rmRegistrationAmPaidTV.setTag(customViewHolder);
                rmRegistrationAmPendingTV.setTag(customViewHolder);
                rmDepositAmPaidTV.setTag(customViewHolder);
                rmDepositAmPendingTV.setTag(customViewHolder);
                rmDepositAmReturnedTV.setTag(customViewHolder);
                rmCreditLimitTV.setTag(customViewHolder);
                rmLastVisitOnTV.setTag(customViewHolder);
                rmIndexPerformanceTV.setTag(customViewHolder);
                rmIndexReturnTV.setTag(customViewHolder);
                rmIndexSalesTV.setTag(customViewHolder);
            }
        }

        public class NonMrViewHolder extends CustomViewHolder {

            protected TextView rmTotalCollectedAmTV;
            protected TextView rmTotalPaidCollectedAmTV;
            protected TextView rmCurrentCollectedAmTV;

            public NonMrViewHolder(View view) {
                super(view);

                this.rmTotalCollectedAmTV = (TextView) view.findViewById(R.id.rm_total_collected_am_v);
                this.rmTotalPaidCollectedAmTV = (TextView) view.findViewById(R.id.rm_total_paid_collected_am_v);
                this.rmCurrentCollectedAmTV = (TextView) view.findViewById(R.id.rm_current_collected_am_v);
            }

            public void setData(MrAccount mrAccount, int i){

                rmTotalCollectedAmTV.setText(DataUtil.toString(mrAccount.getTotalCollectedAm()));
                rmTotalPaidCollectedAmTV.setText(DataUtil.toString(mrAccount.getTotalPaidCollectedAm()));
                rmCurrentCollectedAmTV.setText(DataUtil.toString(mrAccount.getCurrentCollectedAm()));
            }
            public void setOnClickListener(View.OnClickListener clickListener) {

//                rmTotalCollectedAmTV.setOnClickListener(clickListener);
//                rmTotalPaidCollectedAmTV.setOnClickListener(clickListener);
//                rmCurrentCollectedAmTV.setOnClickListener(clickListener);
            }
            public void setTag(CustomViewHolder customViewHolder) {

                rmTotalCollectedAmTV.setTag(customViewHolder);
                rmTotalPaidCollectedAmTV.setTag(customViewHolder);
                rmCurrentCollectedAmTV.setTag(customViewHolder);
            }
        }

        public class MrStatsViewHolder extends CustomViewHolder {

            protected TextView rsTitleTV;
            protected TextView rsGivenStockTV;
            protected TextView rsReturnedStockTV;
            protected TextView rsSoldStockTV;

            public MrStatsViewHolder(View view) {
                super(view);

                this.rsTitleTV = (TextView) view.findViewById(R.id.rs_title_v);
                this.rsGivenStockTV = (TextView) view.findViewById(R.id.rs_given_stock_v);
                this.rsReturnedStockTV = (TextView) view.findViewById(R.id.rs_returned_stock_v);
                this.rsSoldStockTV = (TextView) view.findViewById(R.id.rs_sold_stock_v);
            }

            public void setData(MrAccount mrAccount, int i){

                // Correct Index
                i = (i-3);

                if(mrAccount.getMrStats() != null && i >= 0 && mrAccount.getMrStats().size() > i) {
                    MrAccount.MrStat mrStat = mrAccount.getMrStats().get(i);

                    rsTitleTV.setText(mrStat.getTitle());
                    rsGivenStockTV.setText(DataUtil.stockToString(mrStat.getStockAm(), mrStat.getStockNo()));
                    rsReturnedStockTV.setText(DataUtil.stockToString(mrStat.getStockReturnedAm(), mrStat.getStockReturnedNo()));
                    rsSoldStockTV.setText(DataUtil.stockToString(mrStat.getStockSoldAm(), mrStat.getStockSoldNo()));
                }
            }
            public void setOnClickListener(View.OnClickListener clickListener) {

//                rsTitleTV.setOnClickListener(clickListener);
//                rsVisitCounterTV.setOnClickListener(clickListener);
//                rsGivenStockTV.setOnClickListener(clickListener);
//                rsReturnedStockTV.setOnClickListener(clickListener);
//                rsSoldStockTV.setOnClickListener(clickListener);

            }
            public void setTag(CustomViewHolder customViewHolder) {

                rsTitleTV.setTag(customViewHolder);
                rsGivenStockTV.setTag(customViewHolder);
                rsReturnedStockTV.setTag(customViewHolder);
                rsSoldStockTV.setTag(customViewHolder);
            }
        }
    }

    protected class MrStockCaller extends HTTPCaller {
        public MrStockCaller(Activity activity, String call) {
            super(activity, call);
        }

        @Override
        public void httpSuccessCallback(String jsonResponce) {
            // Reload Data
            reloadData();
        }
    }

}
