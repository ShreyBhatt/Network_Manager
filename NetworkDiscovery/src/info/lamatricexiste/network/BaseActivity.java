package info.lamatricexiste.network;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import info.lamatricexiste.network.Network.HostBean;
import info.lamatricexiste.network.Utils.Export;
import info.lamatricexiste.network.Utils.Help;
import info.lamatricexiste.network.Utils.Prefs;

public class BaseActivity extends ActionBarActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;


	protected RelativeLayout _completeLayout, _activityLayout;
	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;
	private List<HostBean> hosts = null;
	private static LayoutInflater mInflater;
	protected Context ctxt;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer);
		ctxt = getApplicationContext();
		mInflater = LayoutInflater.from(ctxt);
		// if (savedInstanceState == null) {
		// // on first time display view for first nav item
		// // displayView(0);
		// }
	}





//	public void callPhase2(ActivityMain activityMain){
//			Context ctxt = activityMain;
//			activityMain.phase2(ctxt);
//	}
	public void set(String[] navMenuTitles, TypedArray navMenuIcons) {
		mTitle = mDrawerTitle = getTitle();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items
		if (navMenuIcons == null) {
			for (int i = 0; i < navMenuTitles.length; i++) {
				navDrawerItems.add(new NavDrawerItem(navMenuTitles[i]));
			}
		} else {
			for (int i = 0; i < navMenuTitles.length; i++) {
				navDrawerItems.add(new NavDrawerItem(navMenuTitles[i],
						navMenuIcons.getResourceId(i, -1)));
			}
		}

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		// getSupportActionBar().setIcon(R.drawable.ic_drawer);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
				// accessibility
				R.string.app_name // nav drawer close - description for
		// accessibility
		) {
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				supportInvalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				supportInvalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

	}

	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// getSupportMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(0, ActivityDiscovery.MENU_SCAN_SINGLE, 0, R.string.scan_single_title).setIcon(
//				android.R.drawable.ic_menu_mylocation);
//		menu.add(0, ActivityDiscovery.MENU_EXPORT, 0, R.string.preferences_export).setIcon(
//				android.R.drawable.ic_menu_save);
//		menu.add(0, ActivityDiscovery.MENU_OPTIONS, 0, R.string.btn_options).setIcon(
//				android.R.drawable.ic_menu_preferences);
		menu.add(0, ActivityDiscovery.MENU_HELP, 0, R.string.preferences_help).setIcon(
				android.R.drawable.ic_menu_help);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
		}

		switch (item.getItemId()) {
			case ActivityDiscovery.MENU_SCAN_SINGLE:
				scanSingle(this, null);
				return true;
			case ActivityDiscovery.MENU_OPTIONS:
				startActivity(new Intent(ctxt, Prefs.class));
				return true;
			case ActivityDiscovery.MENU_HELP:
				startActivity(new Intent(ctxt, Help.class));
				return true;
			case ActivityDiscovery.MENU_EXPORT:
				export();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public static void scanSingle(final Context ctxt, String ip) {
		// Alert dialog
		View v = LayoutInflater.from(ctxt).inflate(R.layout.scan_single, null);
		final EditText txt = (EditText) v.findViewById(R.id.ip);
		if (ip != null) {
			txt.setText(ip);
		}
		AlertDialog.Builder dialogIp = new AlertDialog.Builder(ctxt);
		dialogIp.setTitle(R.string.scan_single_title);
		dialogIp.setView(v);
		dialogIp.setPositiveButton(R.string.btn_scan, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dlg, int sumthin) {
				// start scanportactivity
				Intent intent = new Intent(ctxt, ActivityPortscan.class);
				intent.putExtra(HostBean.EXTRA_HOST, txt.getText().toString());
				try {
					intent.putExtra(HostBean.EXTRA_HOSTNAME, (InetAddress.getByName(txt.getText()
							.toString()).getHostName()));
				} catch (UnknownHostException e) {
					intent.putExtra(HostBean.EXTRA_HOSTNAME, txt.getText().toString());
				}
				ctxt.startActivity(intent);
			}
		});
		dialogIp.setNegativeButton(R.string.btn_discover_cancel, null);
		dialogIp.show();
	}

	private void export() {
		final Export e = new Export(ctxt, hosts);
		final String file = e.getFileName();

		View v = mInflater.inflate(R.layout.dialog_edittext, null);
		final EditText txt = (EditText) v.findViewById(R.id.edittext);
		txt.setText(file);

		AlertDialog.Builder getFileName = new AlertDialog.Builder(this);
		getFileName.setTitle(R.string.export_choose);
		getFileName.setView(v);
		getFileName.setPositiveButton(R.string.export_save, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dlg, int sumthin) {
				final String fileEdit = txt.getText().toString();
				if (e.fileExists(fileEdit)) {
					AlertDialog.Builder fileExists = new AlertDialog.Builder(BaseActivity.this);
					fileExists.setTitle(R.string.export_exists_title);
					fileExists.setMessage(R.string.export_exists_msg);
					fileExists.setPositiveButton(R.string.btn_yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									if (e.writeToSd(fileEdit)) {
										makeToast(R.string.export_finished);
									} else {
										export();
									}
								}
							});
					fileExists.setNegativeButton(R.string.btn_no, null);
					fileExists.show();
				} else {
					if (e.writeToSd(fileEdit)) {
						makeToast(R.string.export_finished);
					} else {
						export();
					}
				}
			}
		});
		getFileName.setNegativeButton(R.string.btn_discover_cancel, null);
		getFileName.show();
	}

	public void makeToast(int msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}



	/***
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		// boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {

		switch (position) {
		case 0:
//			Intent intent = new Intent(this, IPcalculator.class);
//			startActivity(intent);
//			finish();// finishes the current activity
			Intent intent3 = new Intent(this, ActivityMain.class);
			startActivity(intent3);
			finish();
			break;
		case 1:
			 Intent intent1 = new Intent(this, Ping.class);
			 startActivity(intent1);
			 finish();// finishes the current activity
			break;
		case 2:
			Intent intent6 = new Intent(this, MultiPing.class);
			startActivity(intent6);
			finish();// finishes the current activity
			break;
		 case 3:
			 Intent intent2 = new Intent(this, DNSlookup.class);
			 startActivity(intent2);
			 finish();
		 break;
		 case 4:
//			 Intent intent3 = new Intent(this, ActivityDiscovery.class);
//			 startActivity(intent3);
//			 finish();
			 Intent intent = new Intent(this, IPcalculator.class);
			 startActivity(intent);
			 finish();// finishes the current activity
		 break;
		 case 5:
			 Intent intent4 = new Intent(this, TraceActivity.class);
			 startActivity(intent4);
			 finish();
			 break;
		 case 6:
			 Intent intent5 = new Intent(this, MySystemProperty.class);
			 startActivity(intent5);
			 finish();
		 break;
//		case 7:
//			Intent intent7 = new Intent(this, MainPageActivity.class);
//			startActivity(intent7);
//			finish();
//			break;
		case 7:
			Intent intent8 = new Intent(this, ActivityMain2.class);
			startActivity(intent8);
			finish();
			break;
		default:
			break;
		}

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
}
