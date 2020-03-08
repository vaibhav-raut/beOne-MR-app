package udaan.beone.mrpoint.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import udaan.beone.mrpoint.R;
import udaan.beone.mrpoint.activity.fragment.AuthMrAccountFragment;
import udaan.beone.mrpoint.activity.fragment.AuthStockFragment;
import udaan.beone.mrpoint.activity.fragment.CFragment;

public class AuthDetailedStockActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mPager;
    private AuthDetailedPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_stock);

        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mPager = (ViewPager) findViewById(R.id.pager);

        mAdapter = new AuthDetailedPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mPager);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_third, menu);
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
    protected void onPostResume () {
        super.onPostResume();
    }

    protected static CFragment getPageFragment(int position) {

        switch(position) {
            case 0:
                return AuthStockFragment.newInstance();

            case 1:
                return AuthMrAccountFragment.newInstance();
        }
        return null;
    }
}

class AuthDetailedPagerAdapter extends FragmentStatePagerAdapter {

    private String[] tabName = {"Stock", "Dashboard"};

    public AuthDetailedPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CFragment getItem(int position) {
        return AuthDetailedStockActivity.getPageFragment(position);
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

