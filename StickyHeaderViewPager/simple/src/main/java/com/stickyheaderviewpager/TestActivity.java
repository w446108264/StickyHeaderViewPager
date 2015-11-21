package com.stickyheaderviewpager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.library.StickHeaderViewPager;
import com.library.StickHeaderViewPager.StickHeaderViewPagerBuilder;
import com.library.tab.SlidingTabLayout;

public class TestActivity extends AppCompatActivity {

    StickHeaderViewPager shvp_content;
    SlidingTabLayout stl_stick;
    TextView tv_github;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        shvp_content = (StickHeaderViewPager) findViewById(R.id.shvp_content);
        stl_stick = (SlidingTabLayout) findViewById(R.id.stl_stick);
        tv_github = (TextView) findViewById(R.id.tv_github);

        tv_github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(tv_github.getText().toString());
                Intent intent = new  Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        setData();
    }

    private void setData() {
        shvp_content.getViewPager().setOffscreenPageLimit(5);

        StickHeaderViewPagerBuilder.stickTo(shvp_content)
                .setFragmentManager(getSupportFragmentManager())
                .addScrollFragments(
                        RecyclerViewSimpleFragment.newInstance("RecyclerView"),
                        RecyclerViewGridSimpleFragment.newInstance("GridView"),
                        ScrollViewSimpleFragment.newInstance("Scroll"),
                        WebViewSimpleFragment.newInstance("WebView"),
                        ListViewSimpleFragment.newInstance("ListView"))
                .notifyData();

        stl_stick.setViewPager(shvp_content.getViewPager());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, 0, 0, "Simple Activity").setIcon(R.drawable.ic_launcher);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case 0:
                SimpleActivity.openActivity(TestActivity.this);
                break;
        }
        return true;
    }
}
