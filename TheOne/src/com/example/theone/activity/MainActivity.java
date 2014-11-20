package com.example.theone.activity;



import com.example.fragment.TabFragment1;
import com.example.fragment.TabFragment2;
import com.example.fragment.TabFragment3;
import com.example.fragment.TabFragment4;
import com.example.theone.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initTabHost();
		initView();//初始化tabhost
	}

	private void initTabHost() {
		FragmentTabHost mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator(creatView("买卖", R.drawable.ic_tab_artists)),
                TabFragment1.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator(creatView("周边", R.drawable.ic_tab_albums)),
        		TabFragment2.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator(creatView("我的", R.drawable.ic_tab_songs)),
        		TabFragment3.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab4").setIndicator(creatView("更多", R.drawable.ic_tab_playlists)),
        		TabFragment4.class, null);
	}
	
	public View creatView(String tabname,int drawableselector){
		View view = getLayoutInflater().inflate(R.layout.tabhost_item, null);
	    TextView textView = (TextView) view.findViewById(R.id.textView1);
	    textView.setText(tabname);
	    ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);
	    imageView.setBackgroundResource(drawableselector);
		return view;
	}
	
	private void initView() {
		
	}

	

}
