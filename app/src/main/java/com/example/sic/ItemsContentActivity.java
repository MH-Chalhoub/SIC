package com.example.sic;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class ItemsContentActivity extends AppCompatActivity {

    ViewPager viewPager;
    ViewPagerAdapter adapter;
    TextView item_description, item_location, item_price, user_name, posted_time;
    LinearLayout user_location;
    Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.toolbar_layout);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        item_description = findViewById(R.id.itemDescription);
        item_location = findViewById(R.id.location);
        item_price = findViewById(R.id.price);
        user_name = findViewById(R.id.userName);
        posted_time = findViewById(R.id.postedTime);
        user_location = findViewById(R.id.locationWrapper);

        item = getIntent().getExtras().getParcelable("item");
        //Toast.makeText(ItemsContentActivity.this, item.getLocation(), Toast.LENGTH_SHORT).show();
        item_description.setText(item.getDescription());
        item_location.setText(item.getLocation());
        item_price.setText(item.getPrice() + "$");
        user_name.setText(item.getName());

        String pattern = "yyyy MMMM dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(item.getPosted_time());
        posted_time.setText(date);

        user_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + item.getLocation());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        final TextView text = (TextView) findViewById(R.id.textView);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                text.setText((i+1) + "");
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        adapter = new ViewPagerAdapter(ItemsContentActivity.this,item.getImages());
        viewPager.setAdapter(adapter);

        getSupportActionBar().setTitle(item.getTitle());
        getSupportActionBar().setSubtitle("subtitle");
    }
}
