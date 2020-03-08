package udaan.beone.mrpoint.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import udaan.beone.mrpoint.R;
import udaan.beone.mrpoint.data.DataManager;
import udaan.beone.mrpoint.http.model.LoginRequestREST;
import udaan.beone.mrpoint.http.util.HTTPCaller;
import udaan.beone.mrpoint.http.util.HTTPConst;
import udaan.beone.mrpoint.http.util.HTTPTask;
import udaan.beone.mrpoint.util.AndroidUtil;


public class SelectGroupActivity extends AppCompatActivity {

    private TextInputLayout mDitrictCodeLayout;
    private TextInputLayout mGroupNoLayout;
    private TextInputLayout mPasswordLayout;
    private EditText mInputDitrictCode;
    private EditText mInputGroupNo;
    private EditText mInputPassword;
    private HTTPTask task;
    private Button selectGroupBn;
    private ProgressBar progressBar;

//    private FloatingActionButton mFAB;
    private View.OnClickListener mSnackBarClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

//    private View.OnClickListener mFabClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Snackbar.make(mRoot, "FAB Clicked", Snackbar.LENGTH_SHORT)
//                    .show();
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_group);

        mDitrictCodeLayout = (TextInputLayout) findViewById(R.id.district_code_layout);
        mGroupNoLayout = (TextInputLayout) findViewById(R.id.group_no_layout);
        mPasswordLayout = (TextInputLayout) findViewById(R.id.password_layout);

        mInputDitrictCode = (EditText) findViewById(R.id.input_district_code);
        mInputGroupNo = (EditText) findViewById(R.id.input_group_no);
        mInputPassword = (EditText) findViewById(R.id.input_password);

        selectGroupBn = (Button) findViewById(R.id.select_group_bn);

//        mFAB = (FloatingActionButton) findViewById(R.id.fab);
//        mFAB.setOnClickListener(mFabClickListener);

        reloadUserLogin();
    }

    public void reloadUserLogin() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedGroup = sharedPreferences.getString(DataManager.ACCOUNT_SELECTED_GROUP, null);
        String password = sharedPreferences.getString(DataManager.ACCOUNT_PASSWORD, null);

        if(password != null) {
            mInputPassword.setText(password);
        }
        if(selectedGroup != null) {
            String[] selectedGroupParts = selectedGroup.split(DataManager.ACCOUNT_USERNAME_DELIMITER);
            if(selectedGroupParts.length == 2) {
                mInputDitrictCode.setText(selectedGroupParts[0]);
                mInputGroupNo.setText(selectedGroupParts[1]);
            }
        }
    }
    public void saveUserLogin() {

        String selectedGroup = mInputDitrictCode.getText().toString() + DataManager.ACCOUNT_USERNAME_DELIMITER +
                mInputGroupNo.getText().toString();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString(DataManager.ACCOUNT_SELECTED_GROUP, selectedGroup).apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void submit(View view) {

        boolean isDistrictCodeValid = isDistrictCodeValid();
        boolean isGroupNoValid = isGroupNoValid();
        boolean isPasswordValid = isPasswordValid();

        if(!isDistrictCodeValid) {
            mInputDitrictCode.setError("Invalid District Code!");
        }
        else if(!isGroupNoValid) {
            mInputGroupNo.setError("Invalid Group No!");
        }
        else if(!isPasswordValid) {
            mInputPassword.setError("Invalid Password!");
        }
        else {
            selectGroupBn.setEnabled(false);
            progressBar = (ProgressBar) this.findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.VISIBLE);

            LoginRequestREST request = DataManager.instance().getLoginRequestREST();

            request.setGroupDistrictCode(mInputDitrictCode.getText().toString());
            request.setSelectedGroupNo(Long.parseLong(mInputGroupNo.getText().toString()));
            request.setPasscode(mInputPassword.getText().toString());

            DataManager.instance().setLoginRequestREST(request);

            task = new HTTPTask(new SelectGroupCaller(this, HTTPConst.AUTH_SELECT_GROUP), LoginRequestREST.JSONRepo.buildJSONString(request),
                    HTTPConst.AUTH_SELECT_GROUP, null);

            task.execute();

            Log.d("Debug", "onClick: login is successful!");
        }
    }

    private boolean isDistrictCodeValid() {
        return (mInputDitrictCode.getText() != null  &&
                mInputDitrictCode.getText().toString() != null &&
                mInputDitrictCode.getText().toString().length() == 4);
    }

    private boolean isGroupNoValid() {
        return (mInputGroupNo.getText() != null  &&
                mInputGroupNo.getText().toString() != null &&
                mInputGroupNo.getText().toString().length() > 0);
    }

    private boolean isPasswordValid() {
        return (mInputPassword.getText() != null &&
                mInputPassword.getText().toString() != null &&
                mInputPassword.getText().toString().length() >= 6 &&
                mInputPassword.getText().toString().length() < 32);

    }

    protected class SelectGroupCaller extends HTTPCaller {

        public SelectGroupCaller(Activity activity, String call) {
            super(activity, call);
        }

        @Override
        public void httpSuccessCallback(String jsonResponce) {
            Toast.makeText(activity, "Select Group is Successful!", Toast.LENGTH_SHORT).show();
            saveUserLogin();

            // Launch Home Page
            AndroidUtil.launchActivity(activity, HomeActivity.class);
        }
        @Override
        public void httpFailureCallback(String message) {
            Log.e("Udaan", "Call: " + call + " is Failed!");
            Toast.makeText(activity, "Call: " + call + " is Failed!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            selectGroupBn.setEnabled(true);
        }
        @Override
        public void httpErrorCallback(String message) {
            Log.e("Udaan", "Call: " + call + " is Failed!");
            Toast.makeText(activity, "Call: " + call + " is Failed!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            selectGroupBn.setEnabled(true);
        }
    }
}
