package udaan.beone.mrpoint.activity;
/**
 * Author Vivz
 * Date 15/06/15
 */

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import udaan.beone.mrpoint.R;
import udaan.beone.mrpoint.activity.dialog.ActionDialog;
import udaan.beone.mrpoint.activity.dialog.OkDialog;
import udaan.beone.mrpoint.activity.fragment.CFragment;
import udaan.beone.mrpoint.activity.fragment.MrVisitFragment;
import udaan.beone.mrpoint.activity.fragment.OwnerMrAccountShortFragment;
import udaan.beone.mrpoint.activity.fragment.OwnerMrVisitAcFragment;
import udaan.beone.mrpoint.activity.fragment.OwnerStockFragment;
import udaan.beone.mrpoint.data.DataManager;
import udaan.beone.mrpoint.http.model.s.MrVisitS;
import udaan.beone.mrpoint.http.util.HTTPCaller;
import udaan.beone.mrpoint.http.util.HTTPConst;
import udaan.beone.mrpoint.http.util.HTTPTask;
import udaan.beone.mrpoint.util.AndroidUtil;
import udaan.beone.mrpoint.util.EnumConst;


public class VisitActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String SELECTED_ITEM_ID = "selected_item_id";
    private static final String FIRST_TIME = "first_time";
    private Toolbar mToolbar;
    private NavigationView mDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mSelectedId;

    private TabLayout mTabLayout;
    private ViewPager mPager;
    private VisitPagerAdapter mAdapter;

//    private TextView mDLoginMember;
//    private TextView mDSelectedGroup;
//    private TextView mDSelectedMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Main Activity Init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit);

        // Toolbar Init
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);

        // Nav Drawer Init
        mDrawer = (NavigationView) findViewById(R.id.visit_menu_drawer);
        mDrawer.setNavigationItemSelectedListener(this);
        // Nav DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        hideDrawer();

//        mSelectedId = savedInstanceState == null ? R.id.navigation_member_stock : savedInstanceState.getInt(SELECTED_ITEM_ID);
        mSelectedId = -1;
        navigate(mSelectedId);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mPager = (ViewPager) findViewById(R.id.pager);

        mAdapter = new VisitPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mPager);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

//        mDLoginMember = (TextView) findViewById(R.id.d_login_member);
//        mDSelectedGroup = (TextView) findViewById(R.id.d_selected_group);
//        mDSelectedMember = (TextView) findViewById(R.id.d_selected_member);
    }

    @Override
    protected void onPostResume () {
        super.onPostResume();
    }

    private void showDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    private void hideDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    private void navigate(int mSelectedId) {

        switch (mSelectedId) {
            case R.id.navigation_auth_stock: {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                AndroidUtil.launchActivity(this, AuthStockActivity.class);
                break;
            }
            case  R.id.navigation_give_stock: {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                AndroidUtil.launchActivity(this, GiveStockActivity.class);
                break;
            }
            case  R.id.navigation_return_stock: {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                AndroidUtil.launchActivity(this, ReturnStockActivity.class);
                break;
            }
            case  R.id.navigation_check_stock: {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                AndroidUtil.launchActivity(this, CheckStockActivity.class);
                break;
            }
            case  R.id.navigation_sold_stock: {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                AndroidUtil.launchActivity(this, SoldStockActivity.class);
                break;
            }
            case  R.id.navigation_sold_all_stock: {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                AndroidUtil.launchActivity(this, SoldAllStockActivity.class);
                break;
            }
            case  R.id.navigation_transfer_pay: {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                AndroidUtil.launchActivity(this, ActionPayActivity.class);
                break;
            }
            case  R.id.navigation_credit_limit: {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                new OkDialog(this, "Page Not Ready",
                        "This page is currently underdevelopment and would be available soon!").show();
//                AndroidUtil.launchActivity(this, CreditLimitActivity.class);
                break;
            }
            case  R.id.navigation_other_charges: {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                new OkDialog(this, "Page Not Ready",
                        "This page is currently underdevelopment and would be available soon!").show();
//                AndroidUtil.launchActivity(this, OtherChargesActivity.class);
                break;
            }
            case  R.id.navigation_special_case: {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                new OkDialog(this, "Page Not Ready",
                        "This page is currently underdevelopment and would be available soon!").show();
//                AndroidUtil.launchActivity(this, SpecialCaseActivity.class);
                break;
            }
            case R.id.navigation_item_info: {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                AndroidUtil.launchActivity(this, ItemInfoActivity.class);
                break;
            }
            case  R.id.navigation_end_visit: {
                mDrawerLayout.closeDrawer(GravityCompat.START);

                new EndVisitDialog(this).show();
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

//        mDLoginMember.setText("Login Member : " + DataManager.instance().getDisplayLoginMemberAcNo());
//        mDSelectedGroup.setText("Selected Group : " + DataManager.instance().getDisplaySelectedGroupAcNo());
//        mDSelectedMember.setText("Selected Member : " + DataManager.instance().getDisplaySelectedMemberAcNo());

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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        menuItem.setChecked(true);
        mSelectedId = menuItem.getItemId();

        navigate(mSelectedId);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_ITEM_ID, mSelectedId);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    protected static CFragment getPageFragment(int position) {

        switch(position) {
            case 0:
                return MrVisitFragment.newInstance();

            case 1:
                return OwnerMrVisitAcFragment.newInstance();

            case 2:
                return OwnerStockFragment.newInstance();

            case 3:
                return OwnerMrAccountShortFragment.newInstance();
        }
        return null;
    }

    public static class EndVisitDialog extends ActionDialog {
        public EndVisitDialog(Activity context) {
            super(context, "End Visit", "Do you really want to End the Visit!", "Cancel", "End Visit");
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ac_button_1:
                    this.dismiss();
                    break;

                case R.id.ac_button_2:

                    MrVisitS mrVisitS = null;
                    if(DataManager.instance().getActiveVisit() != null) {
                        mrVisitS = DataManager.instance().getActiveVisit().buildMrVisitShort();
                    }

                    if(mrVisitS == null) {
                        AndroidUtil.logout(this.context);
                        return;
                    }
                    mrVisitS.setVisitStatus(EnumConst.VisitStatus_Ended);

                    // Load MrAccount
                    HTTPTask task = new HTTPTask(new EndVisitCaller(context, HTTPConst.VISIT_END_VISIT),
                            MrVisitS.JSONRepo.buildJSONString(mrVisitS),
                            HTTPConst.VISIT_END_VISIT, null);
                    task.execute();

                    this.dismiss();
                    break;
            }
        }
    }
    public static class EndVisitCaller extends HTTPCaller {
        EndVisitCaller(Activity activity, String call) {
            super(activity, call);
        }
        @Override
        public void httpSuccessCallback(String jsonResponce) {
            DataManager.instance().setMemberVisitAcDirty();
            AndroidUtil.launchActivity(activity, HomeActivity.class);
        }
    }
}

class VisitPagerAdapter extends FragmentStatePagerAdapter {

    private String[] tabName = {"Visit Info", "Visit Account", "Stock", "Account"};

    public VisitPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CFragment getItem(int position) {
        return VisitActivity.getPageFragment(position);
    }

    @Override
    public int getCount() {
        return tabName.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabName[position];
    }
}
