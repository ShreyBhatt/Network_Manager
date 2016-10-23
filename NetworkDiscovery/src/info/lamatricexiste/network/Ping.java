package info.lamatricexiste.network;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.lamatricexiste.network.Utils.Help;
import info.lamatricexiste.network.Utils.HelpPing;

public class Ping extends BaseActivity {

	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	Button shareping = null;
	

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.ping);
			setTitle("Ping");

			navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load
			// titles
			// from
			// strings.xml

			navMenuIcons = getResources()
					.obtainTypedArray(R.array.nav_drawer_icons);// load icons from
			// strings.xml

			set(navMenuTitles, navMenuIcons);


			Button ip = (Button) findViewById(R.id.getip);
			final EditText ipadd = (EditText) findViewById(R.id.ip);
			final TextView ipdetail = (TextView) findViewById(R.id.setdetail);
			shareping = (Button)findViewById(R.id.shareping);
			
			Bundle extras = getIntent().getExtras();
            if (extras != null) {
                final String value = extras.getString("url");

                
                  final ProgressDialog dialog = ProgressDialog.show(Ping.this, "", "Loading. Please wait...", true);

                  new AsyncTask<Void, Void, String>()
                  {
                      @Override
                      protected String doInBackground(Void... params)
                      {
                          

                          String answer = PingIP.runPing("ping -c 5 " + value);  
                          Log.d("url", answer);
                          return answer;
                      }

                      @Override
                      protected void onPostExecute(String answer)
                      {
                          dialog.dismiss();
                          ipdetail.setText(answer); 

                      }
                  }.execute();
            }
	            
			
			
			ip.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
					String ipaddress = ipadd.getText().toString();
					 
					 ipaddress = ipaddress.replaceAll("\\s","");
					 ipaddress = ipaddress.toLowerCase();
					if(ipaddress.equals("google"))
						
						ipaddress = ipaddress+".com";
					
					 Pattern p = Pattern.compile("(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?");
			            Matcher m;
			            m=p.matcher(ipaddress);
				
				
			            if(m.matches()){

					final ProgressDialog dialog = ProgressDialog.show(Ping.this, "", "Loading. Please wait...", true);

					new AsyncTask<Void, Void, String>()
					{
						@Override
						protected String doInBackground(Void... params)
						{
							 String ipaddress = ipadd.getText().toString();
							 
							 ipaddress = ipaddress.replaceAll("\\s","");
							 ipaddress = ipaddress.toLowerCase();
							if(ipaddress.equals("google"))
								
								ipaddress = ipaddress+".com";
							String answer = PingIP.runPing("ping -c 5 " + ipaddress);
							Log.d("url", answer);
							return answer;
						}

						@Override
						protected void onPostExecute(String answer)
						{
							dialog.dismiss();
							ipdetail.setText(answer);

						}
					}.execute();
				}
				else if(ipadd.getText().toString().equals("")){
						ipdetail.setText("");

						Toast.makeText(Ping.this, "Please Enter Input", Toast.LENGTH_LONG).show();
				}
				else {
					ipdetail.setText("");

					Toast.makeText(Ping.this, "Please Enter Correct Input", Toast.LENGTH_LONG).show();
				}
				}
			});

			shareping.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					
					if(ipdetail.getText().toString().equals("")){
						
						Toast.makeText(Ping.this, "Generate output to share", Toast.LENGTH_LONG).show();

					}

				else{
				 

						String message = ipdetail.getText().toString();
						Intent share = new Intent(Intent.ACTION_SEND);
						share.setType("text/plain");
						share.putExtra(Intent.EXTRA_TEXT, message);
						share.putExtra(Intent.EXTRA_SUBJECT,"Ping Status");

						startActivity(Intent.createChooser(share, "Select From Below"));
				}

					
				}
			});


		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.top_menu, menu);
			menu.add(0, ActivityDiscovery.MENU_HELP, 0, R.string.preferences_help).setIcon(
	                android.R.drawable.ic_menu_help);
			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			final TextView ipdetail = (TextView) findViewById(R.id.setdetail);

			switch (item.getItemId()) {
				
				case R.id.file:
					if(ipdetail.getText().toString().equals("")){
						
						Log.d("Hello", "hello");
						
							Toast.makeText(Ping.this, "No data to save", Toast.LENGTH_LONG).show();

					
					
						
						}
				else{
					String now =new Date().toString();
					final TextView ipadd = (TextView) findViewById(R.id.ip);
					
					String url = ipadd.getText().toString();
					String pingdetail = ipdetail.getText().toString();
					
					String FILENAME = now+ url;
					FILENAME = FILENAME.replaceAll("\\s","");
					String message="Following ping result saved on :";
					try {
						
						
						
						FileOutputStream fos = openFileOutput(FILENAME, MODE_APPEND);
						
						fos.write(10);

						fos.write(message.getBytes());
						fos.write(now.getBytes());
						fos.write(10);
						fos.write(10);
						fos.write(pingdetail.getBytes());

						 
						 File path = Environment.getDataDirectory();
						 File path1 = this.getFilesDir();
						 fos.close();
						
						 SaveToFile.generateNoteOnSD(this,
								 FILENAME+".txt",pingdetail);
						 
						 FileInputStream fin = openFileInput(FILENAME);
			             int c;
			             String temp="";
			               
			              while( (c = fin.read()) != -1){
			                  temp = temp + Character.toString((char)c);
			               }
			               ipdetail.setText("File Stored at : /storage/emulated/0/Notes/"+FILENAME+"\n\n"+temp);
						
			               
			               
			               
			               
			               
					//	ipdetail.setText("file stored");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
					return true;
				case ActivityDiscovery.MENU_HELP:
					startActivity(new Intent(ctxt, HelpPing.class));
					return true;
			}
			return super.onOptionsItemSelected(item);
		}

		}

