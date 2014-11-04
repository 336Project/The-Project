package com.example.theone.activity;

import com.example.theone.R;
import com.example.theone.R.layout;
import com.example.theone.webaccess.impl.BaseAccess.WebServiceAccessListener;
import com.example.theone.webaccess.impl.GetInform;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    private void downMess(){
    	GetInform getInform=new GetInform(null, null, new WebServiceAccessListener() {
			
			@Override
			public void getAccessResult(boolean isAccessSuccess, Object objResult,
					String msg) {
				// TODO Auto-generated method stub
				
			}
		});
    	getInform.getInform(null, null, null, null);
    }
}
