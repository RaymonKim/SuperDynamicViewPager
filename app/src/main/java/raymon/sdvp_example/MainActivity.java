package raymon.sdvp_example;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import raymon.superdynamicviewpager.SuperDynamicViewPager;

public class MainActivity extends AppCompatActivity {
    SuperDynamicViewPager mDefaultViewPager;
    SuperDynamicViewPager mWrapViewPager;
    SuperDynamicViewPager mRatioViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mDefaultViewPager = (SuperDynamicViewPager) findViewById(R.id.pager_default);
        mWrapViewPager = (SuperDynamicViewPager) findViewById(R.id.pager_wrap);
        mRatioViewPager = (SuperDynamicViewPager) findViewById(R.id.pager_ratio);

        mDefaultViewPager.setAdapter(new CommonPagerAdapter(this));
        mWrapViewPager.setAdapter(new CommonPagerAdapter(this));
        mRatioViewPager.setAdapter(new CommonPagerAdapter(this));
    }
}
