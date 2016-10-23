package info.lamatricexiste.network;


import info.lamatricexiste.network.SubnetUtils.SubnetInfo;
import info.lamatricexiste.network.Utils.HelpIpcalculator;
import info.lamatricexiste.network.Utils.Prefs;

import java.util.Arrays;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class IPcalculator extends BaseActivity {

    private EditText et1;
    private static ListView list_view;
    private String[] NAMES,EMPTY,list;
    private String temp=new String("");
    private Button share;
    

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ipcalculator);
        setTitle("IP Calculator");

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load
        // titles
        // from
        // strings.xml

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);// load icons from
        // strings.xml

        set(navMenuTitles, navMenuIcons);

        Button b1 = (Button) findViewById(R.id.b1);
        final Button share = (Button) findViewById(R.id.b2);
		 list_view = (ListView) findViewById(R.id.listView);

        EMPTY=new String[]{};
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.name_list, EMPTY);



        
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et1 = (EditText) findViewById(R.id.et1);

                String edit = et1.getText().toString();
                if(edit.equals("")){
                	
        			//share.setVisibility(View.INVISIBLE);
               	 list_view.setAdapter(null);
               	 temp="";
                    Toast.makeText(IPcalculator.this,"Enter IP in CIDR format", Toast.LENGTH_LONG).show();
                    
                }
                else {
                	try{
                    listview();
                	}catch(Exception e)
                	{
                        Toast.makeText(IPcalculator.this,"Enter CIDR between 1-32", Toast.LENGTH_LONG).show();
                      	 list_view.setAdapter(null);
                      	 temp="";

                	}
                   /* Object obj = list_view.getAdapter().getItem(0);
                    String value = obj.toString();
                    if(value.equals("")){
            			share.setVisibility(View.INVISIBLE);

                    }
                    else{
            			share.setVisibility(View.VISIBLE);

                    }*/
                }
            }
        });
        
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	try{
            	
             if(temp.equals("")){
            	 Log.i("HELLO","HELLO");
                 Toast.makeText(IPcalculator.this,"Generate output to share", Toast.LENGTH_LONG).show();
         	

             }
            else{
                	 /* et1 = (EditText) findViewById(R.id.et1);
                      String subnet = et1.getText().toString();
                    SubnetUtils utils = new SubnetUtils(subnet);*/

                 
                        //SubnetInfo info = utils.getInfo();
                        //String message =temp;
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_TEXT,temp);
                        share.putExtra(Intent.EXTRA_SUBJECT,"IPv4 Calculator");

                        startActivity(Intent.createChooser(share, "Select From Below"));
            }
            	}catch(Exception e){
             		e.printStackTrace();
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
                startActivity(new Intent(ctxt, HelpIpcalculator.class));
                return true;
//            case ActivityDiscovery.MENU_EXPORT:
//                export();
//                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void listview(){


         et1 = (EditText) findViewById(R.id.et1);


             final String subnet = et1.getText().toString();
             
             final SubnetUtils utils = new SubnetUtils(subnet);
         if(SubnetUtils.i == 0) {
/*             SubnetInfo info = utils.getInfo();

             NAMES = new String[]{"Subnet Information for" + subnet,
                     "--------------------------------------",
                     "IP Address:" + info.getAddress(),
                     "Netmask:" + info.getNetmask(),
                     "CIDR Representation:" + info.getCidrSignature(),
                     "Supplied IP Address:" + info.getAddress(),
                     "Network Address:" + info.getNetworkAddress(),
                     "Broadcast Address:" + info.getBroadcastAddress(),
                     "Low Address:" + info.getLowAddress(),
                     "High Address:" + info.getHighAddress(),
                     "Total usable addresses:" + Integer.valueOf(info.getAddressCount()),
                     "Address List: " + Arrays.toString(info.getAllAddresses())
             };


             list_view = (ListView) findViewById(R.id.listView);
             ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.name_list, NAMES);
             list_view.setAdapter(adapter);*/
try{
        	 final ProgressDialog dialog = ProgressDialog.show(IPcalculator.this, "", "Loading. Please wait...", true);

				new AsyncTask<Void, Void, String>()
				{
					@Override
					protected String doInBackground(Void... params)
					{
						 SubnetInfo info = utils.getInfo();

      NAMES = new String[]{"Subnet Information for" + subnet,
              "--------------------------------------",
              "IP Address:" + info.getAddress(),
              "Netmask:" + info.getNetmask(),
              "CIDR Representation:" + info.getCidrSignature(),
              "Supplied IP Address:" + info.getAddress(),
              "Network Address:" + info.getNetworkAddress(),
              "Broadcast Address:" + info.getBroadcastAddress(),
              "Low Address:" + info.getLowAddress(),
              "High Address:" + info.getHighAddress(),
              "Total usable addresses:" + Integer.valueOf(info.getAddressCount()),
              //"Address List: " + Arrays.toString(list)
      };   
      try{
      temp=Arrays.toString(NAMES);
      Log.i("Hello",temp);
     
      }catch(Exception e){
        	 list_view.setAdapter(null);
        	 temp="";
          Toast.makeText(IPcalculator.this,"Enter CIDR between 1-32", Toast.LENGTH_LONG).show();
      }
      return temp ;
					}

					@Override
					protected void onPostExecute(String answer)
					{
						dialog.dismiss();
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(IPcalculator.this, R.layout.name_list, NAMES);
					      try{
						list_view.setAdapter(adapter);}
					      catch(Exception e){
				               	 list_view.setAdapter(null);
				               	 temp="";
		                        Toast.makeText(IPcalculator.this,"Enter CIDR between 1-32", Toast.LENGTH_LONG).show();
					      }
					}
				}.execute();
				
				

}catch(Exception e){
	e.printStackTrace();
}
         }
         else {

        	list_view = (ListView) findViewById(R.id.listView);
             ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.name_list, EMPTY);
             list_view.setAdapter(adapter);
        	// list_view.setEmptyView(list_view);

             Toast.makeText(IPcalculator.this, "Enter IP in Correct Format", Toast.LENGTH_LONG).show();
             temp="";
             SubnetUtils.i = 0;

         }
    }
}