package me.shrikanthravi.songstudio.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import static android.view.View.NO_ID;

public class SimpleTabProvider implements SmartTabLayout.TabProvider {

    private final LayoutInflater inflater;
    private final int tabViewLayoutId;
    private final int tabViewTextViewId;
    TextView tabTitleView = null;
    public SimpleTabProvider(Context context, int layoutResId, int textViewId) {
        inflater = LayoutInflater.from(context);
        tabViewLayoutId = layoutResId;
        tabViewTextViewId = textViewId;
    }

    @Override
    public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
        View tabView = null;
        if (tabViewLayoutId != NO_ID) {
            tabView = inflater.inflate(tabViewLayoutId, container, false);
        }

        if (tabViewTextViewId != NO_ID && tabView != null) {
            tabTitleView = (TextView) tabView.findViewById(tabViewTextViewId);
        }

        if (tabTitleView == null && TextView.class.isInstance(tabView) && ImageView.class.isInstance(tabView)) {
            tabTitleView = (TextView) tabView;
        }

        if (tabTitleView != null) {
            tabTitleView.setText(adapter.getPageTitle(position));
        }

        return tabView;
    }
}
