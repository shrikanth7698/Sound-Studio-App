package me.shrikanthravi.songstudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import me.shrikanthravi.songstudio.adapters.ViewPagerAdapter;
import me.shrikanthravi.songstudio.fragments.LibraryFragment;
import me.shrikanthravi.songstudio.fragments.SongsFragment;
import me.shrikanthravi.songstudio.utils.SimpleTabProvider;

public class HomeActivity extends AppCompatActivity {

    String TAG = "HomeActivity";

    //Views
    ViewPager viewPager;
    TextView titleTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //UI Init
        initUI();


    }

    //UI init
    void initUI(){
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        titleTV = findViewById(R.id.titleTV);
        titleTV.getPaint().setShader(new LinearGradient(0,0,0,titleTV.getLineHeight(),getResources().getColor(R.color.colorAccent),getResources().getColor(R.color.colorAccent1), Shader.TileMode.MIRROR));
        viewPager = findViewById(R.id.viewPager);
        setupViewPager();
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SongsFragment(), "Songs");
        adapter.addFragment(new LibraryFragment(),"Library");
        viewPager.setAdapter(adapter);
        SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);
        SimpleTabProvider tabProvider = new SimpleTabProvider(getApplicationContext(),R.layout.custom_tab,R.id.CustomTabText);
        viewPagerTab.setCustomTabView(tabProvider);
        viewPagerTab.setViewPager(viewPager);
        viewPager.setOffscreenPageLimit(5);
    }



}
