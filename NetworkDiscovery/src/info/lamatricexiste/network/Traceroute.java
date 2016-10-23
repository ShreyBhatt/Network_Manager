package info.lamatricexiste.network;

import java.net.InetAddress;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Traceroute extends BaseActivity {
	
	  private String[] navMenuTitles;
	    private TypedArray navMenuIcons;
	private Button stop;
	private static int i= 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.traceroute);
		setTitle("Traceroute");

    
		stop = (Button) findViewById(R.id.stop);
		final Button traceroute = (Button) findViewById(R.id.trace);
		final EditText url = (EditText) findViewById(R.id.url);
		final TextView traceDetail = (TextView) findViewById(R.id.settracedetail);
		
		final TextView EnterUrl = (TextView) findViewById(R.id.textView1);
		
	    navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load
        // titles
        // from
        // strings.xml

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);// load icons from
        // strings.xml

        set(navMenuTitles, navMenuIcons);

		
		traceroute.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String webaddress = url.getText().toString();

				String netaddress = null;
				String answer = null;
				try {
					if (i == 0) {

						netaddress = new DNSUtil().execute(webaddress).get();

						TracerouteUtil tu = new TracerouteUtil();
						answer = tu.traceRoute(netaddress);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//Log.d("ipaddress", answer);
				traceDetail.setText(answer + netaddress);


			}
		});


		stop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				i = 1;
				traceroute.invalidate();
			}
		});



	}

}
