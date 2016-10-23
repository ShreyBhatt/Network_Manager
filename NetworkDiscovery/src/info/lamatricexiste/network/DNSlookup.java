package info.lamatricexiste.network;

//import info.lamatricexiste.network.Utils.HelpDns;
import info.lamatricexiste.network.Utils.HelpDns;
import info.lamatricexiste.network.Utils.Prefs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DNSlookup extends BaseActivity  {
	
	private EditText et;
	private TextView tv,tv1;
	private Button btn;
	private Button sharedns;
	private Button btnping;
	String passtoping;

	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dnslookup);
		setTitle("DNS Lookup");

		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load
		// titles
		// from
		// strings.xml

		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);// load icons from
		// strings.xml

		set(navMenuTitles, navMenuIcons);
        
        et=(EditText)findViewById(R.id.editText1);
        tv=(TextView)findViewById(R.id.textView1);
        tv1=(TextView)findViewById(R.id.textView0);

        btn=(Button)findViewById(R.id.button1);
		sharedns=(Button)findViewById(R.id.sharedns);
		btnping=(Button)findViewById(R.id.btnping);


		
		



 btn.setOnClickListener(new View.OnClickListener() {

@Override
public void onClick(View v) {

	

	ConnectivityManager cm = (ConnectivityManager) ctxt.getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	boolean isConnected = activeNetwork != null &&
	                      activeNetwork.isConnectedOrConnecting();

	if (!isConnected){
		Toast.makeText(DNSlookup.this, "Not Connected to Network", Toast.LENGTH_LONG).show();

	}
	
else{	
	
	String hostname = et.getText().toString();
	hostname = hostname.replaceAll("\\s","");
	hostname = hostname.toLowerCase();
	passtoping=hostname;

	if(hostname.equals("google"))
	
		hostname = hostname+".com";
	

	
         Pattern p = Pattern.compile("(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?");
            Matcher m;
            m=p.matcher(hostname);
	
	
            if(m.matches()){
			String netAddress = null;
			try {
				netAddress = new DNSUtil().execute(hostname).get();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			tv1.setText("HostName : " + hostname);

			tv.setText("IP Address : " + netAddress);
			btnping.setVisibility(View.VISIBLE);
			//sharedns.setVisibility(View.VISIBLE);


			
			

		}
            else if(et.getText().toString().equals("")){
            	tv1.setText("");

				tv.setText("");
				btnping.setVisibility(View.INVISIBLE);
    			//sharedns.setVisibility(View.INVISIBLE);


    			Toast.makeText(DNSlookup.this, "Please Enter Input", Toast.LENGTH_LONG).show();
    			
    			
    		}
            else{
            	tv1.setText("");

				tv.setText("");
				btnping.setVisibility(View.INVISIBLE);
    			//sharedns.setVisibility(View.INVISIBLE);


    			Toast.makeText(DNSlookup.this, "Please Enter Correct Input", Toast.LENGTH_LONG).show();
    			
            }
}
}
	});


 
	

		sharedns.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {


					if(tv.getText().toString().equals("")){
	
		    			Toast.makeText(DNSlookup.this, "Generate output to share", Toast.LENGTH_LONG).show();

					}

			else{

					/*String hostname = et.getText().toString();
					String netAddress = null;
					try {
						netAddress = new DNSUtil().execute(hostname).get();
					} catch (Exception e1) {
						e1.printStackTrace();
					}*/
					String message =tv1.getText().toString() + "\n" + tv.getText().toString();
					Intent share = new Intent(Intent.ACTION_SEND);
					share.setType("text/plain");
					share.putExtra(Intent.EXTRA_TEXT, message);
                    share.putExtra(Intent.EXTRA_SUBJECT,"DNS lookup");


					startActivity(Intent.createChooser(share, "Select From Below"));

			}
				
			}
		});

		btnping.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
		        
		                try {
		                    Intent i = new Intent(getApplicationContext(), Ping.class);
		                    i.putExtra("url",passtoping);
		                    startActivity(i);
		         
		       } catch (Exception e1) {
		                    e1.printStackTrace();
		                }
		                }
				
				
			
		});
		
		
		
			}
	

    @Override
   	public boolean onOptionsItemSelected(MenuItem item) {

   		switch (item.getItemId()) {
   			case ActivityDiscovery.MENU_SCAN_SINGLE:
   				scanSingle(this, null);
   				return true;
   			case ActivityDiscovery.MENU_OPTIONS:
   				startActivity(new Intent(ctxt, Prefs.class));
   				return true;
   			case ActivityDiscovery.MENU_HELP:
   				startActivity(new Intent(ctxt, HelpDns.class));
   				return true;
//   			case ActivityDiscovery.MENU_EXPORT:
//   				export();
//   				return true;
   		}

   		return super.onOptionsItemSelected(item);
   	}
    
}


