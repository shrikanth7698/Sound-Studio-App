package me.shrikanthravi.songstudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class PortfolioActivity extends AppCompatActivity {

    CardView linkCV1,linkCV2,linkCV3;
    ImageView backIV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);
        initUI();
    }

    void initUI(){
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        linkCV1 = findViewById(R.id.linkCV1);
        linkCV2 = findViewById(R.id.linkCV2);
        linkCV3 = findViewById(R.id.linkCV3);
        backIV = findViewById(R.id.backIV);
        linkCV1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(getString(R.string.github_link));
            }
        });
        linkCV2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(getString(R.string.linkedin_link));
            }
        });
        linkCV3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(getString(R.string.portfolio_link));
            }
        });
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    void startIntent(String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}
