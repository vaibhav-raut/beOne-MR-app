package udaan.beone.mrpoint.activity;
/**
 * Author Vaibhav
 * Date 15/06/15
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import udaan.beone.mrpoint.R;
import udaan.beone.mrpoint.activity.adapter.PayItemAdapter;
import udaan.beone.mrpoint.data.DataManager;
import udaan.beone.mrpoint.http.model.MrVisit;
import udaan.beone.mrpoint.http.model.PayTransfer;
import udaan.beone.mrpoint.http.util.HTTPCaller;
import udaan.beone.mrpoint.http.util.HTTPConst;
import udaan.beone.mrpoint.http.util.HTTPTask;
import udaan.beone.mrpoint.util.AndroidUtil;
import udaan.beone.mrpoint.util.BDUtil;
import udaan.beone.mrpoint.util.DataUtil;

public class ActionPayActivity extends AppCompatActivity implements OnClickListener {

    protected TextView gsActionTitle;

    protected Button gsRefreshBn;
    protected Button gsCancelBn;
    protected Button gsActionBn;
    protected Button gsCalculateBn;

    protected EditText gsPayAmount;

    protected TextView openingBalance;
    protected TextView newBalance;
    protected TextView paidBalance;
    protected TextView closingBalance;

    protected PayTransfer payTransfer;
    protected BigDecimal paymentAmount;

    protected RecyclerView mRecyclerView;
    protected PayItemAdapter adapter;

    protected ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Main Activity Init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_pay);

        gsActionTitle = (TextView) findViewById(R.id.gs_action_title);

        gsRefreshBn = (Button) findViewById(R.id.gs_refresh_bn);
        gsCancelBn = (Button) findViewById(R.id.gs_cancel_bn);
        gsActionBn = (Button) findViewById(R.id.gs_action_bn);
        gsCalculateBn = (Button) findViewById(R.id.gs_calculate_bn);

        gsPayAmount = (EditText) findViewById(R.id.gs_pay_amount);

        openingBalance = (TextView) findViewById(R.id.rv_opening_balance_am_v);
        newBalance = (TextView) findViewById(R.id.rv_new_balance_am_v);
        paidBalance = (TextView) findViewById(R.id.rv_paid_balance_am_v);
        closingBalance = (TextView) findViewById(R.id.rv_closing_balance_am_v);

        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.selected_stock_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PayItemAdapter(this);
        mRecyclerView.setAdapter(adapter);

        gsRefreshBn.setOnClickListener(this);
        gsCancelBn.setOnClickListener(this);
        gsActionBn.setOnClickListener(this);
        gsCalculateBn.setOnClickListener(this);

        paymentAmount = DataUtil.ZERO_BIG_DECIMAL;

        refreshTransferPay();
        updateVisitData();
    }

    @Override
    public void onClick(View var1) {

        switch(var1.getId()) {

            case R.id.gs_refresh_bn: {
                refreshTransferPay();
                break;
            }

            case R.id.gs_cancel_bn: {
                clear();
                AndroidUtil.launchActivity(this, VisitActivity.class);
                break;
            }

            case R.id.gs_action_bn: {

                if(paymentAmount.compareTo(DataUtil.ZERO_BIG_DECIMAL) > 0 &&
                        adapter != null && adapter.getPayTxs() != null && adapter.getPayTxs().size() > 0) {

                    gsRefreshBn.setEnabled(false);
                    gsCancelBn.setEnabled(false);
                    gsActionBn.setEnabled(false);
                    gsCalculateBn.setEnabled(false);

                    progressBar = (ProgressBar) this.findViewById(R.id.progress_bar);
                    progressBar.setVisibility(View.VISIBLE);

                    PayTransfer payTransfer = new PayTransfer();
                    payTransfer.setAuthAcNo(DataManager.instance().getLoginMemberAcNo());
                    payTransfer.setOwnerAcNo(DataManager.instance().getSelectedMemberAcNo());
                    payTransfer.setMrVisitId(DataManager.instance().getActiveVisit().getMrVisitId());
                    payTransfer.setAmount(paymentAmount);
                    payTransfer.setPayTxs(adapter.getPayTxsToPay());

                    HTTPTask task = new HTTPTask(new TransferPayCaller(this, HTTPConst.VISIT_TRANSFER_PAY),
                            PayTransfer.JSONRepo.buildJSONString(payTransfer), HTTPConst.VISIT_TRANSFER_PAY, null);
                    task.execute();
                }
                else {
                    Toast.makeText(this, "No Payment Amount!", Toast.LENGTH_SHORT).show();
                }

                break;
            }

            case R.id.gs_calculate_bn: {

                String payAmounStr = gsPayAmount.getText().toString();
                if (payAmounStr == null || payAmounStr.isEmpty()) {
                    Toast.makeText(this, "Please enter Amount to Calculate!", Toast.LENGTH_SHORT).show();
                    return;
                }

                paymentAmount = new BigDecimal(payAmounStr);

                if (paymentAmount.compareTo(DataUtil.ZERO_BIG_DECIMAL) <= 0) {
                    Toast.makeText(this, "Invalid Payment Amount!", Toast.LENGTH_LONG).show();
                    return;
                }
                gsPayAmount.setText("");

                adapter.calculatePay(paymentAmount);
                break;
            }
        }
    }

    protected void refreshTransferPay() {

        gsRefreshBn.setEnabled(false);
        gsCancelBn.setEnabled(false);
        gsActionBn.setEnabled(false);
        gsCalculateBn.setEnabled(false);

        progressBar = (ProgressBar) this.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        List<String> urlArgs = new ArrayList<String>(1);
        urlArgs.add(Long.toString(DataManager.instance().getSelectedMemberAcNo()));

        HTTPTask task = new HTTPTask(new OutstandingPayCaller(this, HTTPConst.VISIT_GET_OUTSTANDING_PAY_TXS),
                null, HTTPConst.VISIT_GET_OUTSTANDING_PAY_TXS, urlArgs);
        task.execute();

    }

    protected void updateVisitData() {

        MrVisit mrVisit = DataManager.instance().getActiveVisit();

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

        openingBalance.setText(DataUtil.roundHalfUp(openingBalanceAm, 0).toString());
        newBalance.setText(DataUtil.roundHalfUp(newBalanceAm, 0).toString());
        paidBalance.setText(DataUtil.roundHalfUp(paidBalanceAm, 0).toString());
        closingBalance.setText(DataUtil.roundHalfUp(closingBalanceAm, 0).toString());
    }

    protected void updateTransferPay(PayTransfer payTransfer) {
        this.payTransfer = payTransfer;
        adapter.setPayTxs(payTransfer.getPayTxs());
    }

    @Override
    protected void onPostResume () {
        super.onPostResume();
    }
    @Override
    public void onBackPressed() {
    }

    private void clear() {

        gsRefreshBn = null;
        gsCancelBn = null;
        gsActionBn = null;
        gsCalculateBn = null;
        gsPayAmount = null;
        openingBalance = null;
        newBalance = null;
        paidBalance = null;
        closingBalance = null;
        mRecyclerView = null;
        adapter = null;
    }

    public class OutstandingPayCaller extends HTTPCaller {
        private ActionPayActivity activity;

        public OutstandingPayCaller(ActionPayActivity activity, String call) {
            super(activity, call);
            this.activity = activity;
        }
        public void httpSuccessCallback(String jsonResponce){

            if(jsonResponce != null && !jsonResponce.isEmpty()) {

                PayTransfer payTransfer = (PayTransfer)PayTransfer.JSONRepo.buildObject(jsonResponce);
                if(payTransfer != null) {
                    activity.updateTransferPay(payTransfer);
                }
            }

            progressBar.setVisibility(View.GONE);
            Toast.makeText(activity, gsActionTitle.getText().toString() + " is Successful!", Toast.LENGTH_SHORT).show();

            gsRefreshBn.setEnabled(true);
            gsCancelBn.setEnabled(true);
            gsActionBn.setEnabled(true);
            gsCalculateBn.setEnabled(true);
        }
        public void httpFailureCallback(String message) {
            progressBar.setVisibility(View.GONE);

            gsRefreshBn.setEnabled(true);
            gsCancelBn.setEnabled(true);
            gsActionBn.setEnabled(true);
            gsCalculateBn.setEnabled(true);

            Log.e("Udaan", "Call: " + call + " is Failed!");
            Toast.makeText(activity, "Call: " + call + " is Failed! Please try Again!", Toast.LENGTH_SHORT).show();
        }
        public void httpErrorCallback(String message) {
            progressBar.setVisibility(View.GONE);

            gsRefreshBn.setEnabled(true);
            gsCancelBn.setEnabled(true);
            gsActionBn.setEnabled(true);
            gsCalculateBn.setEnabled(true);

            Log.e("Udaan", "Call: " + call + " is Failed!");
            Toast.makeText(activity, "Call: " + call + " is Failed! Please try Again!", Toast.LENGTH_SHORT).show();
        }
    }

    public class TransferPayCaller extends HTTPCaller {
        private ActionPayActivity activity;

        public TransferPayCaller(ActionPayActivity activity, String call) {
            super(activity, call);
            this.activity = activity;
        }
        public void httpSuccessCallback(String jsonResponce){

            if(jsonResponce != null && !jsonResponce.isEmpty()) {

                PayTransfer payTransfer = (PayTransfer)PayTransfer.JSONRepo.buildObject(jsonResponce);
                if(payTransfer != null) {
                    activity.updateTransferPay(payTransfer);
                }

                DataManager.instance().setAllAccountDirty();

                List<String> urlArgs = new ArrayList<String>(2);
                urlArgs.add(Long.toString(DataManager.instance().getSelectedMemberAcNo()));
                urlArgs.add(Long.toString(DataManager.instance().getLoginMemberAcNo()));

                HTTPTask task = new HTTPTask(new ActiveVisitCaller(activity, HTTPConst.VISIT_GET_ACTIVE_VISITS_FOR_OWNER),
                        null, HTTPConst.VISIT_GET_ACTIVE_VISITS_FOR_OWNER, urlArgs);
                task.execute();
            }

            progressBar.setVisibility(View.GONE);
            Toast.makeText(activity, gsActionTitle.getText().toString() + " is Successful!", Toast.LENGTH_SHORT).show();

            gsRefreshBn.setEnabled(true);
            gsCancelBn.setEnabled(true);
            gsActionBn.setEnabled(true);
            gsCalculateBn.setEnabled(true);
        }
        public void httpFailureCallback(String message) {
            progressBar.setVisibility(View.GONE);

            gsRefreshBn.setEnabled(true);
            gsCancelBn.setEnabled(true);
            gsActionBn.setEnabled(true);
            gsCalculateBn.setEnabled(true);

            Log.e("Udaan", "Call: " + call + " is Failed!");
            Toast.makeText(activity, "Call: " + call + " is Failed! Please try Again!", Toast.LENGTH_SHORT).show();
        }
        public void httpErrorCallback(String message) {
            progressBar.setVisibility(View.GONE);

            gsRefreshBn.setEnabled(true);
            gsCancelBn.setEnabled(true);
            gsActionBn.setEnabled(true);
            gsCalculateBn.setEnabled(true);

            Log.e("Udaan", "Call: " + call + " is Failed!");
            Toast.makeText(activity, "Call: " + call + " is Failed! Please try Again!", Toast.LENGTH_SHORT).show();
        }
    }

    public class ActiveVisitCaller extends HTTPCaller.PassiveDummy {
        private ActionPayActivity activity;

        public ActiveVisitCaller(ActionPayActivity activity, String call) {
            super(activity, call);
            this.activity = activity;
        }
        public void httpSuccessCallback(String jsonResponce){
            updateVisitData();
        }
    }
}
