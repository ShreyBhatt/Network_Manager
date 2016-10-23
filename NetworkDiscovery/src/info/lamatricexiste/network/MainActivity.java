package info.lamatricexiste.network;

import android.app.TabActivity;


import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends BaseActivity {

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load
        // titles
        // from
        // strings.xml

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);// load icons from
        // strings.xml


        set(navMenuTitles, navMenuIcons);
		Resources ressources = getResources(); 
//		TabHost tabHost = getTabHost();
		
		// Android tab
//		Intent intentAndroid = new Intent().setClass(this, Ping.class);
//		TabSpec tabSpecAndroid = tabHost
//		  .newTabSpec("Android")
//		  .setIndicator("", ressources.getDrawable(R.drawable.ip_address48))
//		  .setContent(intentAndroid);
//
//		// Apple tab
//		Intent intentApple = new Intent().setClass(this, ActivityMain.class );
//		TabSpec tabSpecApple = tabHost
//		  .newTabSpec("Apple")
//		  .setIndicator("", ressources.getDrawable(R.drawable.network48))
//		  .setContent(intentApple);
//
//		// Windows tab
//		Intent intentWindows = new Intent().setClass(this,  DNSlookup.class);
//		TabSpec tabSpecWindows = tabHost
//		  .newTabSpec("Windows")
//		  .setIndicator("", ressources.getDrawable(R.drawable.domain48))
//		  .setContent(intentWindows);
//
//		// Blackberry tab
//		Intent intentBerry = new Intent().setClass(this,  IPcalculator.class);
//		TabSpec tabSpecBerry = tabHost
//		  .newTabSpec("Berry")
//		  .setIndicator("", ressources.getDrawable(R.drawable.data_in_both_directions48))
//		  .setContent(intentBerry);
//
//		// add all tabs
//		tabHost.addTab(tabSpecAndroid);
//		tabHost.addTab(tabSpecApple);
//		tabHost.addTab(tabSpecWindows);
//		tabHost.addTab(tabSpecBerry);
//
//		//set Windows tab as default (zero based)
//		tabHost.setCurrentTab(0);
	}

}