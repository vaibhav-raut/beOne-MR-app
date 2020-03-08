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

public abstract class MrAccountFragment extends CFragment {

//    private OnFragmentInteractionListener mListener;

    protected RecyclerView mRecyclerView;
    protected MyRecyclerAdapter adapter;
    protected ProgressBar progressBar;


    public MrAccountFragment() {
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
                    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rc_row_mr_account_details, null);
                    viewHolder = new MemberDetailsViewHolder(view);
                    break;
                }

                case 1: {
                    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rc_row_mr_account_common, null);
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
                    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rc_row_mr_account_stats, null);
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

            protected TextView addressTV;
            protected TextView emailTV;

            protected TextView mobile1TV;
            protected TextView mobile2TV;
            protected TextView phoneTV;

            protected TextView joiningDateTV;
            protected TextView birth_dateTV;
            protected TextView annivarsaryTV;

            public MemberDetailsViewHolder(View view) {
                super(view);

                nameTV = (TextView) view.findViewById(R.id.rc_name_v);
                accountNoTV = (TextView) view.findViewById(R.id.rc_account_no_v);

                addressTV = (TextView) view.findViewById(R.id.rc_address_v);
                emailTV = (TextView) view.findViewById(R.id.rc_email_v);

                mobile1TV = (TextView) view.findViewById(R.id.rc_mobile_1_v);
                mobile2TV = (TextView) view.findViewById(R.id.rc_mobile_2_v);
                phoneTV = (TextView) view.findViewById(R.id.rc_phone_v);

                joiningDateTV = (TextView) view.findViewById(R.id.rc_joining_date_v);
                birth_dateTV = (TextView) view.findViewById(R.id.rc_birth_date_v);
                annivarsaryTV = (TextView) view.findViewById(R.id.rc_annivarsary_v);
            }

            public void setData(MrAccount mrAccount, int i){

                nameTV.setText(mrAccount.getMemberInfo().getMemberName());
                if(mrAccount.getMemberInfo().getMemberAcNo() == DataManager.instance().getSelectedMemberAcNo()) {
                    accountNoTV.setText(DataManager.instance().getDisplaySelectedMemberAcNo());
                } else {
                    accountNoTV.setText(DataManager.instance().getDisplayLoginMemberAcNo());
                }

                addressTV.setText(mrAccount.getMemberInfo().getDisplayAddress());

                if(mrAccount.getMemberInfo().getContacts() != null && !mrAccount.getMemberInfo().getContacts().isEmpty()) {

                    MemberContactREST contact = mrAccount.getMemberInfo().getContacts().get(0);
                    emailTV.setText(contact.getEmail());
                    mobile1TV.setText(contact.getPriMobile());
                    mobile2TV.setText(contact.getSecMobile());
                    phoneTV.setText(contact.getPhone());
                }

                joiningDateTV.setText(mrAccount.getMemberInfo().getDateOfEnroll());
                birth_dateTV.setText(mrAccount.getMemberInfo().getDateOfBirth());
                annivarsaryTV.setText(mrAccount.getMemberInfo().getDateOfAnni());
            }

            public void setOnClickListener(View.OnClickListener clickListener) {

//                nameTV.setOnClickListener(clickListener);
//                accountNoTV.setOnClickListener(clickListener);
//
//                addressTV.setOnClickListener(clickListener);
//                addressTV.setOnClickListener(clickListener);
//                emailTV.setOnClickListener(clickListener);
//
//                mobile1TV.setOnClickListener(clickListener);
//                mobile2TV.setOnClickListener(clickListener);
//                phoneTV.setOnClickListener(clickListener);
//
//                joiningDateTV.setOnClickListener(clickListener);
//                birth_dateTV.setOnClickListener(clickListener);
//                annivarsaryTV.setOnClickListener(clickListener);
            }
            public void setTag(CustomViewHolder customViewHolder) {

                nameTV.setTag(customViewHolder);
                accountNoTV.setTag(customViewHolder);

                addressTV.setTag(customViewHolder);
                addressTV.setTag(customViewHolder);
                emailTV.setTag(customViewHolder);

                mobile1TV.setTag(customViewHolder);
                mobile2TV.setTag(customViewHolder);
                phoneTV.setTag(customViewHolder);

                joiningDateTV.setTag(customViewHolder);
                birth_dateTV.setTag(customViewHolder);
                annivarsaryTV.setTag(customViewHolder);
            }
        }

        public class CommonViewHolder extends CustomViewHolder {

            protected final TextView rcRoleTV;
            protected final TextView rcStatusTV;
            protected final TextView rcTypeTV;
            protected final TextView rcSoldAmountPaidTV;
            protected final TextView rcSoldAmountPendingTV;
            protected final TextView rcCurrentStockAmTV;
            protected final TextView rcCurrentStockNoTV;
            protected final TextView rcCurrentStockDiscountAmTV;
            protected final TextView rcCurrentStockDiscountNoTV;
            protected final TextView rcCurrentStockUnsettledAmTV;
            protected final TextView rcCurrentStockUnsettledNoTV;

            public CommonViewHolder(View view) {
                super(view);

                this.rcRoleTV = (TextView) view.findViewById(R.id.rc_role_v);
                this.rcStatusTV = (TextView) view.findViewById(R.id.rc_status_v);
                this.rcTypeTV = (TextView) view.findViewById(R.id.rc_type_v);
                this.rcSoldAmountPaidTV = (TextView) view.findViewById(R.id.rc_sold_amount_paid_v);
                this.rcSoldAmountPendingTV = (TextView) view.findViewById(R.id.rc_sold_amount_pending_v);
                this.rcCurrentStockAmTV = (TextView) view.findViewById(R.id.rc_current_stock_am_v);
                this.rcCurrentStockNoTV = (TextView) view.findViewById(R.id.rc_current_stock_no_v);
                this.rcCurrentStockDiscountAmTV = (TextView) view.findViewById(R.id.rc_current_stock_discount_am_v);
                this.rcCurrentStockDiscountNoTV = (TextView) view.findViewById(R.id.rc_current_stock_discount_no_v);
                this.rcCurrentStockUnsettledAmTV = (TextView) view.findViewById(R.id.rc_current_stock_unsettled_am_v);
                this.rcCurrentStockUnsettledNoTV = (TextView) view.findViewById(R.id.rc_current_stock_unsettled_no_v);

            }

            public void setData(MrAccount mrAccount, int i){
                rcRoleTV.setText(mrAccount.getMrRole());
                rcStatusTV.setText(mrAccount.getMrStatus());
                rcTypeTV.setText(mrAccount.getMrType());
                rcSoldAmountPaidTV.setText(DataUtil.toString(mrAccount.getSoldPaidAm()));
                rcSoldAmountPendingTV.setText(DataUtil.toString(mrAccount.getSoldPendingAm()));
                rcCurrentStockAmTV.setText(DataUtil.toString(mrAccount.getCurrentStockAm()));
                rcCurrentStockNoTV.setText(DataUtil.toString(mrAccount.getCurrentStockNo()));
                rcCurrentStockDiscountAmTV.setText(DataUtil.toString(mrAccount.getCurrentStockDiscountAm()));
                rcCurrentStockDiscountNoTV.setText(DataUtil.toString(mrAccount.getCurrentStockDiscountNo()));
                rcCurrentStockUnsettledAmTV.setText(DataUtil.toString(mrAccount.getCurrentStockUnsettledAm()));
                rcCurrentStockUnsettledNoTV.setText(DataUtil.toString(mrAccount.getCurrentStockUnsettledNo()));
            }
            public void setOnClickListener(View.OnClickListener clickListener) {

//                rcRoleTV.setOnClickListener(clickListener);
//                rcStatusTV.setOnClickListener(clickListener);
//                rcTypeTV.setOnClickListener(clickListener);
//                rcSoldAmountPaidTV.setOnClickListener(clickListener);
//                rcSoldAmountPendingTV.setOnClickListener(clickListener);
//                rcCurrentStockAmTV.setOnClickListener(clickListener);
//                rcCurrentStockNoTV.setOnClickListener(clickListener);
//                rcCurrentStockDiscountAmTV.setOnClickListener(clickListener);
//                rcCurrentStockDiscountNoTV.setOnClickListener(clickListener);
//                rcCurrentStockUnsettledAmTV.setOnClickListener(clickListener);
//                rcCurrentStockUnsettledNoTV.setOnClickListener(clickListener);
            }
            public void setTag(CustomViewHolder customViewHolder) {

                rcRoleTV.setTag(customViewHolder);
                rcStatusTV.setTag(customViewHolder);
                rcTypeTV.setTag(customViewHolder);
                rcSoldAmountPaidTV.setTag(customViewHolder);
                rcSoldAmountPendingTV.setTag(customViewHolder);
                rcCurrentStockAmTV.setTag(customViewHolder);
                rcCurrentStockNoTV.setTag(customViewHolder);
                rcCurrentStockDiscountAmTV.setTag(customViewHolder);
                rcCurrentStockDiscountNoTV.setTag(customViewHolder);
                rcCurrentStockUnsettledAmTV.setTag(customViewHolder);
                rcCurrentStockUnsettledNoTV.setTag(customViewHolder);
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
            protected TextView rsVisitCounterTV;
            protected TextView rsGivenStockAmTV;
            protected TextView rsGivenStockNoTV;
            protected TextView rsReturnedStockAmTV;
            protected TextView rsReturnedStockNoTV;
            protected TextView rsSoldStockAmTV;
            protected TextView rsSoldStockNoTV;
            protected TextView rsSoldDiscountStockAmTV;
            protected TextView rsSoldDiscountStockNoTV;
            protected TextView rsMrSoldAmTV;
            protected TextView rsDamagedStockAmTV;
            protected TextView rsDamagedStockNoTV;

            public MrStatsViewHolder(View view) {
                super(view);

                this.rsTitleTV = (TextView) view.findViewById(R.id.rs_title_v);
                this.rsVisitCounterTV = (TextView) view.findViewById(R.id.rs_visit_counter_v);
                this.rsGivenStockAmTV = (TextView) view.findViewById(R.id.rs_given_stock_am_v);
                this.rsGivenStockNoTV = (TextView) view.findViewById(R.id.rs_given_stock_no_v);
                this.rsReturnedStockAmTV = (TextView) view.findViewById(R.id.rs_returned_stock_am_v);
                this.rsReturnedStockNoTV = (TextView) view.findViewById(R.id.rs_returned_stock_no_v);
                this.rsSoldStockAmTV = (TextView) view.findViewById(R.id.rs_sold_stock_am_v);
                this.rsSoldStockNoTV = (TextView) view.findViewById(R.id.rs_sold_stock_no_v);
                this.rsSoldDiscountStockAmTV = (TextView) view.findViewById(R.id.rs_sold_discount_stock_am_v);
                this.rsSoldDiscountStockNoTV = (TextView) view.findViewById(R.id.rs_sold_discount_stock_no_v);
                this.rsMrSoldAmTV = (TextView) view.findViewById(R.id.rs_mr_sold_am_v);
                this.rsDamagedStockAmTV = (TextView) view.findViewById(R.id.rs_damaged_stock_am_v);
                this.rsDamagedStockNoTV = (TextView) view.findViewById(R.id.rs_damaged_stock_no_v);
            }

            public void setData(MrAccount mrAccount, int i){

                // Correct Index
                i = (i-3);

                if(mrAccount.getMrStats() != null && i >= 0 && mrAccount.getMrStats().size() > i) {
                    MrAccount.MrStat mrStat = mrAccount.getMrStats().get(i);

                    rsTitleTV.setText(mrStat.getTitle());
                    rsVisitCounterTV.setText(DataUtil.toString(mrStat.getVisitCounter()));
                    rsGivenStockAmTV.setText(DataUtil.toString(mrStat.getStockAm()));
                    rsGivenStockNoTV.setText(DataUtil.toString(mrStat.getStockNo()));
                    rsReturnedStockAmTV.setText(DataUtil.toString(mrStat.getStockReturnedAm()));
                    rsReturnedStockNoTV.setText(DataUtil.toString(mrStat.getStockReturnedNo()));
                    rsSoldStockAmTV.setText(DataUtil.toString(mrStat.getStockSoldAm()));
                    rsSoldStockNoTV.setText(DataUtil.toString(mrStat.getStockSoldNo()));
                    rsSoldDiscountStockAmTV.setText(DataUtil.toString(mrStat.getStockSoldDiscountAm()));
                    rsSoldDiscountStockNoTV.setText(DataUtil.toString(mrStat.getStockSoldDiscountNo()));
                    rsMrSoldAmTV.setText(DataUtil.toString(mrStat.getStockMrSoldAm()));
                    rsDamagedStockAmTV.setText(DataUtil.toString(mrStat.getStockDamagedAm()));
                    rsDamagedStockNoTV.setText(DataUtil.toString(mrStat.getStockDamagedNo()));
                }
            }
            public void setOnClickListener(View.OnClickListener clickListener) {

//                rsTitleTV.setOnClickListener(clickListener);
//                rsVisitCounterTV.setOnClickListener(clickListener);
//                rsGivenStockAmTV.setOnClickListener(clickListener);
//                rsGivenStockNoTV.setOnClickListener(clickListener);
//                rsReturnedStockAmTV.setOnClickListener(clickListener);
//                rsReturnedStockNoTV.setOnClickListener(clickListener);
//                rsSoldStockAmTV.setOnClickListener(clickListener);
//                rsSoldStockNoTV.setOnClickListener(clickListener);
//                rsSoldDiscountStockAmTV.setOnClickListener(clickListener);
//                rsSoldDiscountStockNoTV.setOnClickListener(clickListener);
//                rsMrSoldAmTV.setOnClickListener(clickListener);
//                rsDamagedStockAmTV.setOnClickListener(clickListener);
//                rsDamagedStockNoTV.setOnClickListener(clickListener);

            }
            public void setTag(CustomViewHolder customViewHolder) {

                rsTitleTV.setTag(customViewHolder);
                rsVisitCounterTV.setTag(customViewHolder);
                rsGivenStockAmTV.setTag(customViewHolder);
                rsGivenStockNoTV.setTag(customViewHolder);
                rsReturnedStockAmTV.setTag(customViewHolder);
                rsReturnedStockNoTV.setTag(customViewHolder);
                rsSoldStockAmTV.setTag(customViewHolder);
                rsSoldStockNoTV.setTag(customViewHolder);
                rsSoldDiscountStockAmTV.setTag(customViewHolder);
                rsSoldDiscountStockNoTV.setTag(customViewHolder);
                rsMrSoldAmTV.setTag(customViewHolder);
                rsDamagedStockAmTV.setTag(customViewHolder);
                rsDamagedStockNoTV.setTag(customViewHolder);
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
