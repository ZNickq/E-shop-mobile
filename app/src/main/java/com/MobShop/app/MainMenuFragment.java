package com.MobShop.app;


import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jfeinstein.jazzyviewpager.JazzyViewPager;
import com.jfeinstein.jazzyviewpager.OutlineContainer;

public class MainMenuFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private JazzyViewPager mJazzy;


    public static MainMenuFragment newInstance(int sectionNumber) {
        MainMenuFragment fragment = new MainMenuFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MainMenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
        setupJazziness(inflater, container, rootView, JazzyViewPager.TransitionEffect.Tablet);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    private void setupJazziness(LayoutInflater li, ViewGroup container, View forWhich, JazzyViewPager.TransitionEffect effect) {
        mJazzy = (JazzyViewPager) forWhich.findViewById(R.id.jazzy_pager);
        mJazzy.setTransitionEffect(effect);

        Integer[] images = new Integer[2];
        images[0] = R.drawable.blue_team;
        images[1] = R.drawable.cartbutton;

        mJazzy.setAdapter(new MainAdapter(images));
        mJazzy.setPageMargin(30);


        //mJazzy.addView(getImageView(li, container, R.drawable.cartbutton));
        //mJazzy.addView(getImageView(li, container, R.drawable.blue_team));

    }


    private class MainAdapter extends PagerAdapter {

        private Integer[] sources;
        public MainAdapter(Integer[] sources) {
            this.sources = sources;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            //TODO mJazzy.setFadeEnabled(false sau true);
            ImageView text = new ImageView(MainMenuFragment.this.getActivity());
            text.setImageResource(sources[position]);
            container.addView(text, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mJazzy.setObjectForPosition(text, position);
            return text;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object obj) {
            container.removeView(mJazzy.findViewFromObject(position));
        }
        @Override
        public int getCount() {
            return sources.length;
        }
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            if (view instanceof OutlineContainer) {
                return ((OutlineContainer) view).getChildAt(0) == obj;
            } else {
                return view == obj;
            }
        }
    }

}
