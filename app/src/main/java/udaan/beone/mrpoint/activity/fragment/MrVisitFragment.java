package udaan.beone.mrpoint.activity.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import udaan.beone.mrpoint.R;
import udaan.beone.mrpoint.data.DataManager;
import udaan.beone.mrpoint.http.model.MrVisit;
import udaan.beone.mrpoint.http.util.HTTPCaller;
import udaan.beone.mrpoint.http.util.HTTPConst;
import udaan.beone.mrpoint.http.util.HTTPTask;
import udaan.beone.mrpoint.util.AndroidUtil;
import udaan.beone.mrpoint.util.BDUtil;
import udaan.beone.mrpoint.util.DataUtil;
import udaan.beone.mrpoint.util.EnumConst;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MrVisitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MrVisitFragment extends CFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter adapter;
    private ProgressBar progressBar;

    public MrVisitFragment() {
        // Required empty public constructor
    }

    public static MrVisitFragment newInstance() {
        return new MrVisitFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_mr_visit, container, false);

        // Initialize recycler view
        mRecyclerView = (RecyclerView) fragment.findViewById(R.id.mr_visit_view);
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

    public void initRefreshData() {
        if(DataManager.instance().isActiveVisitDirty() || DataManager.instance().getActiveVisit() == null) {

            if(DataManager.instance().getSelectedGroupAcNo() == 0l) {
                AndroidUtil.logout(getActivity());
                return;
            }

            List<String> urlArgs = new ArrayList<String>(2);
            urlArgs.add(Long.toString(DataManager.instance().getSelectedMemberAcNo()));
            urlArgs.add(Long.toString(DataManager.instance().getLoginMemberAcNo()));

            HTTPTask task = new HTTPTask(new MrStockCaller(getActivity(), HTTPConst.VISIT_GET_ACTIVE_VISITS_FOR_OWNER),
                    null, HTTPConst.VISIT_GET_ACTIVE_VISITS_FOR_OWNER, urlArgs);
            task.execute();

        } else {
            reloadData();
        }
    }

    protected void reloadData() {
        // Set already Loaded Data Directly
        if(DataManager.instance().getActiveVisit() != null) {

            if(adapter == null) {
                adapter = new MyRecyclerAdapter(getActivity(), DataManager.instance().getActiveVisit());
                mRecyclerView.setAdapter(adapter);
            }
            else {
                adapter.setMrVisit(DataManager.instance().getActiveVisit());
                adapter.notifyDataSetChanged();
            }
        }

        progressBar.setVisibility(View.GONE);
    }

    public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.CustomViewHolder> {
        private MrVisit mrVisit;
        private Activity mContext;

        public MyRecyclerAdapter(Activity context, MrVisit mrVisit) {
            this.mrVisit = mrVisit;
            this.mContext = context;
        }
        public void setMrVisit(MrVisit mrVisit) {
            this.mrVisit = mrVisit;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            CustomViewHolder viewHolder = null;

            switch (viewType) {
                case 0: {
                    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rc_row_mr_visit_common, null);
                    viewHolder = new CommonViewHolder(view);
                    break;
                }
                case 1: {
                    if (EnumConst.isVisitTypeNonMr(mrVisit.getVisitType())) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rc_row_mr_visit_non_mr, null);
                        viewHolder = new NonMrViewHolder(view);
                    } else {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rc_row_mr_visit_mr, null);
                        viewHolder = new MrViewHolder(view);
                    }
                    break;
                }
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {

            // Set Data for the Card List
            customViewHolder.setData(mrVisit, i);

            // Add OnClickListener for the Card List
//            customViewHolder.setOnClickListener(clickListener);

            // Set Tag for the Card List
            customViewHolder.setTag(customViewHolder);
        }

        @Override
        public int getItemCount() {
            return 2;
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }

        abstract public class CustomViewHolder extends RecyclerView.ViewHolder {
            public CustomViewHolder(View view) {
                super(view);
            }

            abstract public void setData(MrVisit mrVisit, int i);
            abstract public void setOnClickListener(View.OnClickListener clickListener);
            abstract public void setTag(CustomViewHolder customViewHolder);
        }

        public class CommonViewHolder extends CustomViewHolder {

            protected TextView rvGivenStockTV;
            protected TextView rvReturnStockTV;
            protected TextView rvSoldStockTV;
            protected TextView rvSoldStockDiscountTV;
            protected TextView rvOpeningStockTV;
            protected TextView rvClosingStockTV;

            protected TextView rvOpeningBalanceAmTV;
            protected TextView rvNewBalanceAmTV;
            protected TextView rvPaidBalanceAmTV;
            protected TextView rvClosingBalanceAmTV;

            protected TextView rvOpeningOutstandingAmTV;
            protected TextView rvSoldPendingAmTV;
            protected TextView rvSoldPaidAmTV;
            protected TextView rvClosingOutstandingAmTV;

            public CommonViewHolder(View view) {
                super(view);

                this.rvGivenStockTV = (TextView) view.findViewById(R.id.rv_given_stock_v);
                this.rvReturnStockTV = (TextView) view.findViewById(R.id.rv_return_stock_v);
                this.rvSoldStockTV = (TextView) view.findViewById(R.id.rv_sold_stock_v);
                this.rvSoldStockDiscountTV = (TextView) view.findViewById(R.id.rv_sold_stock_discount_v);
                this.rvOpeningStockTV = (TextView) view.findViewById(R.id.rv_opening_stock_v);
                this.rvClosingStockTV = (TextView) view.findViewById(R.id.rv_closing_stock_v);

                this.rvOpeningBalanceAmTV = (TextView) view.findViewById(R.id.rv_opening_balance_am_v);
                this.rvNewBalanceAmTV = (TextView) view.findViewById(R.id.rv_new_balance_am_v);
                this.rvPaidBalanceAmTV = (TextView) view.findViewById(R.id.rv_paid_balance_am_v);
                this.rvClosingBalanceAmTV = (TextView) view.findViewById(R.id.rv_closing_balance_am_v);

                this.rvOpeningOutstandingAmTV = (TextView) view.findViewById(R.id.rv_opening_outstanding_am_v);
                this.rvSoldPendingAmTV = (TextView) view.findViewById(R.id.rv_sold_pending_am_v);
                this.rvSoldPaidAmTV = (TextView) view.findViewById(R.id.rv_sold_paid_am_v);
                this.rvClosingOutstandingAmTV = (TextView) view.findViewById(R.id.rv_closing_outstanding_am_v);
            }

            public void setData(MrVisit mrVisit, int i){

                rvGivenStockTV.setText(DataUtil.stockToString(mrVisit.getGivenStockAm(), mrVisit.getGivenStockNo()));
                rvReturnStockTV.setText(DataUtil.stockToString(mrVisit.getReturnStockAm(), mrVisit.getReturnStockNo()));
                rvSoldStockTV.setText(DataUtil.stockToString(mrVisit.getSoldStockAm(), mrVisit.getSoldStockNo()));
                rvSoldStockDiscountTV.setText(DataUtil.stockToString(mrVisit.getSoldStockDiscountAm(), mrVisit.getSoldStockDiscountNo()));
                rvOpeningStockTV.setText(DataUtil.stockToString(mrVisit.getOpeningStockAm(), mrVisit.getOpeningStockNo()));
                rvClosingStockTV.setText(DataUtil.stockToString(mrVisit.getClosingStockAm(), mrVisit.getClosingStockNo()));

                BigDecimal openingBalanceAm = BDUtil.add(mrVisit.getOpeningOutstandingAm(),
                        BDUtil.add(mrVisit.getOpeningInterestPenaltyAm(),
                                BDUtil.add(mrVisit.getOpeningRegistrationAm(),
                                        BDUtil.add(mrVisit.getOpeningDepositAm(), mrVisit.getOpeningCollectedAm()))));

                BigDecimal newBalanceAm = BDUtil.add(mrVisit.getSoldPendingAm(), mrVisit.getSoldPaidAm());

                BigDecimal paidBalanceAm = BDUtil.add(mrVisit.getSoldPaidAm(),
                        BDUtil.add(mrVisit.getPaidInterestPenaltyAm(),
                                BDUtil.add(mrVisit.getPaidRegistrationAm(),
                                        BDUtil.add(mrVisit.getPaidDepositAm(), mrVisit.getPaidCollectedAm()))));

                BigDecimal closingBalanceAm = BDUtil.add(mrVisit.getClosingOutstandingAm(),
                        BDUtil.add(mrVisit.getClosingInterestPenaltyAm(),
                                BDUtil.add(mrVisit.getClosingRegistrationAm(),
                                        BDUtil.add(mrVisit.getClosingDepositAm(), mrVisit.getClosingCollectedAm()))));

                rvOpeningBalanceAmTV.setText(DataUtil.toString(openingBalanceAm));
                rvNewBalanceAmTV.setText(DataUtil.toString(newBalanceAm));
                rvPaidBalanceAmTV.setText(DataUtil.toString(paidBalanceAm));
                rvClosingBalanceAmTV.setText(DataUtil.toString(closingBalanceAm));

                rvOpeningOutstandingAmTV.setText(DataUtil.toString(mrVisit.getOpeningOutstandingAm()));
                rvSoldPendingAmTV.setText(DataUtil.toString(mrVisit.getSoldPendingAm()));
                rvSoldPaidAmTV.setText(DataUtil.toString(mrVisit.getSoldPaidAm()));
                rvClosingOutstandingAmTV.setText(DataUtil.toString(mrVisit.getClosingOutstandingAm()));
            }
            public void setOnClickListener(View.OnClickListener clickListener) {

                rvGivenStockTV.setOnClickListener(clickListener);
                rvReturnStockTV.setOnClickListener(clickListener);
                rvSoldStockTV.setOnClickListener(clickListener);
                rvSoldStockDiscountTV.setOnClickListener(clickListener);
                rvOpeningStockTV.setOnClickListener(clickListener);
                rvClosingStockTV.setOnClickListener(clickListener);

                rvOpeningBalanceAmTV.setOnClickListener(clickListener);
                rvNewBalanceAmTV.setOnClickListener(clickListener);
                rvPaidBalanceAmTV.setOnClickListener(clickListener);
                rvClosingBalanceAmTV.setOnClickListener(clickListener);

                rvOpeningOutstandingAmTV.setOnClickListener(clickListener);
                rvSoldPendingAmTV.setOnClickListener(clickListener);
                rvSoldPaidAmTV.setOnClickListener(clickListener);
                rvClosingOutstandingAmTV.setOnClickListener(clickListener);
            }
            public void setTag(CustomViewHolder customViewHolder) {

                rvGivenStockTV.setTag(customViewHolder);
                rvReturnStockTV.setTag(customViewHolder);
                rvSoldStockTV.setTag(customViewHolder);
                rvSoldStockDiscountTV.setTag(customViewHolder);
                rvOpeningStockTV.setTag(customViewHolder);
                rvClosingStockTV.setTag(customViewHolder);

                rvOpeningBalanceAmTV.setTag(customViewHolder);
                rvNewBalanceAmTV.setTag(customViewHolder);
                rvPaidBalanceAmTV.setTag(customViewHolder);
                rvClosingBalanceAmTV.setTag(customViewHolder);

                rvOpeningOutstandingAmTV.setTag(customViewHolder);
                rvSoldPendingAmTV.setTag(customViewHolder);
                rvSoldPaidAmTV.setTag(customViewHolder);
                rvClosingOutstandingAmTV.setTag(customViewHolder);
            }
        }

        public class MrViewHolder extends CustomViewHolder {

            protected TextView rvOpeningInterestPenaltyAmTV;
            protected TextView rvPaidInterestPenaltyAmTV;
            protected TextView rvClosingInterestPenaltyAmTV;

            protected TextView rvOpeningDepositAmTV;
            protected TextView rvPaidDepositAmTV;
            protected TextView rvReturnedDepositAmTV;
            protected TextView rvClosingDepositAmTV;

            protected TextView rvOpeningRegistrationAmTV;
            protected TextView rvPaidRegistrationAmTV;
            protected TextView rvClosingRegistrationAmTV;

            public MrViewHolder(View view) {
                super(view);

                this.rvOpeningInterestPenaltyAmTV = (TextView) view.findViewById(R.id.rv_opening_interest_penalty_am_v);
                this.rvPaidInterestPenaltyAmTV = (TextView) view.findViewById(R.id.rv_paid_interest_penalty_am_v);
                this.rvClosingInterestPenaltyAmTV = (TextView) view.findViewById(R.id.rv_closing_interest_penalty_am_v);

                this.rvOpeningDepositAmTV = (TextView) view.findViewById(R.id.rv_opening_deposit_am_v);
                this.rvPaidDepositAmTV = (TextView) view.findViewById(R.id.rv_paid_deposit_am_v);
                this.rvReturnedDepositAmTV = (TextView) view.findViewById(R.id.rv_returned_deposit_am_v);
                this.rvClosingDepositAmTV = (TextView) view.findViewById(R.id.rv_closing_deposit_am_v);

                this.rvOpeningRegistrationAmTV = (TextView) view.findViewById(R.id.rv_opening_registration_am_v);
                this.rvPaidRegistrationAmTV = (TextView) view.findViewById(R.id.rv_paid_registration_am_v);
                this.rvClosingRegistrationAmTV = (TextView) view.findViewById(R.id.rv_closing_registration_am_v);
            }

            public void setData(MrVisit mrVisit, int i){

                rvOpeningInterestPenaltyAmTV.setText(DataUtil.toString(mrVisit.getOpeningInterestPenaltyAm()));
                rvPaidInterestPenaltyAmTV.setText(DataUtil.toString(mrVisit.getPaidInterestPenaltyAm()));
                rvClosingInterestPenaltyAmTV.setText(DataUtil.toString(mrVisit.getClosingInterestPenaltyAm()));

                rvOpeningDepositAmTV.setText(DataUtil.toString(mrVisit.getOpeningDepositAm()));
                rvPaidDepositAmTV.setText(DataUtil.toString(mrVisit.getPaidDepositAm()));
                rvReturnedDepositAmTV.setText(DataUtil.toString(mrVisit.getReturnedDepositAm()));
                rvClosingDepositAmTV.setText(DataUtil.toString(mrVisit.getClosingDepositAm()));

                rvOpeningRegistrationAmTV.setText(DataUtil.toString(mrVisit.getOpeningRegistrationAm()));
                rvPaidRegistrationAmTV.setText(DataUtil.toString(mrVisit.getPaidRegistrationAm()));
                rvClosingRegistrationAmTV.setText(DataUtil.toString(mrVisit.getClosingRegistrationAm()));

            }
            public void setOnClickListener(View.OnClickListener clickListener) {

                rvOpeningInterestPenaltyAmTV.setOnClickListener(clickListener);
                rvPaidInterestPenaltyAmTV.setOnClickListener(clickListener);
                rvClosingInterestPenaltyAmTV.setOnClickListener(clickListener);

                rvOpeningDepositAmTV.setOnClickListener(clickListener);
                rvPaidDepositAmTV.setOnClickListener(clickListener);
                rvReturnedDepositAmTV.setOnClickListener(clickListener);
                rvClosingDepositAmTV.setOnClickListener(clickListener);

                rvOpeningRegistrationAmTV.setOnClickListener(clickListener);
                rvPaidRegistrationAmTV.setOnClickListener(clickListener);
                rvClosingRegistrationAmTV.setOnClickListener(clickListener);

            }
            public void setTag(CustomViewHolder customViewHolder) {

                rvOpeningInterestPenaltyAmTV.setTag(customViewHolder);
                rvPaidInterestPenaltyAmTV.setTag(customViewHolder);
                rvClosingInterestPenaltyAmTV.setTag(customViewHolder);

                rvOpeningDepositAmTV.setTag(customViewHolder);
                rvPaidDepositAmTV.setTag(customViewHolder);
                rvReturnedDepositAmTV.setTag(customViewHolder);
                rvClosingDepositAmTV.setTag(customViewHolder);

                rvOpeningRegistrationAmTV.setTag(customViewHolder);
                rvPaidRegistrationAmTV.setTag(customViewHolder);
                rvClosingRegistrationAmTV.setTag(customViewHolder);
            }
        }

        public class NonMrViewHolder extends CustomViewHolder {

            protected TextView rvOpeningCollectedAmTV;
            protected TextView rvReceivedCollectedAmTV;
            protected TextView rvPaidCollectedAmTV;
            protected TextView rvClosingCollectedAmTV;

            public NonMrViewHolder(View view) {
                super(view);

                this.rvOpeningCollectedAmTV = (TextView) view.findViewById(R.id.rv_opening_collected_am_v);
                this.rvReceivedCollectedAmTV = (TextView) view.findViewById(R.id.rv_received_collected_am_v);
                this.rvPaidCollectedAmTV = (TextView) view.findViewById(R.id.rv_paid_collected_am_v);
                this.rvClosingCollectedAmTV = (TextView) view.findViewById(R.id.rv_closing_collected_am_v);
            }

            public void setData(MrVisit mrVisit, int i){

                rvOpeningCollectedAmTV.setText(DataUtil.toString(mrVisit.getOpeningCollectedAm()));
                rvReceivedCollectedAmTV.setText(DataUtil.toString(mrVisit.getReceivedCollectedAm()));
                rvPaidCollectedAmTV.setText(DataUtil.toString(mrVisit.getPaidCollectedAm()));
                rvClosingCollectedAmTV.setText(DataUtil.toString(mrVisit.getClosingCollectedAm()));
            }
            public void setOnClickListener(View.OnClickListener clickListener) {

                rvOpeningCollectedAmTV.setOnClickListener(clickListener);
                rvReceivedCollectedAmTV.setOnClickListener(clickListener);
                rvPaidCollectedAmTV.setOnClickListener(clickListener);
                rvClosingCollectedAmTV.setOnClickListener(clickListener);
            }
            public void setTag(CustomViewHolder customViewHolder) {

                rvOpeningCollectedAmTV.setTag(customViewHolder);
                rvReceivedCollectedAmTV.setTag(customViewHolder);
                rvPaidCollectedAmTV.setTag(customViewHolder);
                rvClosingCollectedAmTV.setTag(customViewHolder);
            }
        }
    }

    protected class MrStockCaller extends HTTPCaller {
        public MrStockCaller(Activity activity, String call) {
            super(activity, call);
        }

        @Override
        public void httpSuccessCallback(String jsonResponce) {
            reloadData();
        }
    }

}
