package info.lamatricexiste.network;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
//import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Enumeration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import info.lamatricexiste.network.Utils.Help;
import info.lamatricexiste.network.Utils.HelpMultiPing;
import info.lamatricexiste.network.Utils.Prefs;

class ViewWrapper {
    View base;
    TextView hostip=null;
    TextView delay=null;

    ViewWrapper(View base) {
        this.base = base;
    }

    TextView getViewHostIp() {
        if (hostip==null) {
            hostip=(TextView)base.findViewById(R.id.hostip);
        }
        return hostip;
    }

    TextView getViewDelay() {
        if (delay==null) {
            delay=(TextView)base.findViewById(R.id.delay);
        }
        return delay;
    }
}

public class MultiPing extends BaseActivity {
    private static final String LOG_TAG = "MultiPing";
    private TextView myTextView = null;
    private EditText myEditText = null;

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;


    private class PingerItem {
        String hostname = null;
        InetAddress ia = null;
        long result_80 = MAXTIME; // connect 80
        long result_av = MAXTIME; // isAvailable
    }

    public class NameResolver implements Runnable {
        private String hostname;
        public NameResolver(String hostname) {
            this.hostname = hostname;
        }

        public void run() {
            Log.v("multping","NameResolver "+hostname);
            InetAddress ia;
            try {
                ia = InetAddress.getByName(hostname);

                for(int i=0; i<items.size(); i++)
                {
                    PingerItem pi = items.get(i);
                    if(pi.hostname.equals(hostname)) {
                        Log.v("multping","NameResolver "+hostname + " resolved:" + ia);
                        pi.ia = ia;
                        items.set(i,pi);
                    }
                }
            } catch (UnknownHostException e) {
                //
            } catch (Exception e) {
                //
            }
        }
    }

    public class Pinger80 implements Runnable {
        private InetAddress ia;
        public Pinger80(InetAddress ia) {
            this.ia = ia;
        }

        public void run() {
            Log.v("multiping", "Pinger80 " + ia + "start" );
            long t1 = System.nanoTime();
            // Try port 80
            try {
                long dt = TIMEOUT;
                try {
                    Socket socket = new Socket(ia, 80);
                    long t2 = System.nanoTime();
                    dt = (t2-t1)/1000000;
                    socket.close();
                } catch (IOException e) {
                    Log.v("multiping","Pinger80 " + e.toString());
                }

                for(int i=0; i<items.size(); i++)
                {
                    PingerItem pi = items.get(i);
                    if(pi.ia.equals(ia))
                    {
                        pi.result_80 = dt;
                        items.set(i,pi);
                        Log.v("multiping", "Pinger80 "+pi.hostname+" "+dt);
                    }
                }

            } catch (Exception e) {
                Log.v("multiping","Pinger80 " + e.toString());
            }
            Log.v("multiping", "Pinger80 " + ia + "end" );
        }
    }

    public class PingerAv implements Runnable {
        private InetAddress ia;
        public PingerAv(InetAddress ia) {
            this.ia = ia;
        }

        public void run() {
            Log.v("multiping", "PingerAv " + ia + "start" );
            // Try port 7
            for(int i=0; i<items.size(); i++)
            {
                try {
                    PingerItem pi = items.get(i);
                    if(pi.ia.equals(ia))
                    {
                        long t1 = System.nanoTime();
                        try {
                            if(pi.ia.isReachable(TIMEOUT))
                            {
                                long t2 = System.nanoTime();
                                long dt = (t2-t1)/1000000;

                                pi = items.get(i);
                                pi.result_av = dt;
                                items.set(i, pi);
                                Log.v("multiping", "PingerAv "+pi.hostname+" "+ pi.result_av);
                            }
                            else
                            {
                                pi = items.get(i);
                                pi.result_av = TIMEOUT;
                                items.set(i, pi);
                                Log.v("multiping", "PingerAv TIMEOUT "+pi.hostname+" "+ pi.result_av);
                            }
                        } catch (IOException e) {
                            pi = items.get(i);
                            pi.result_av = TIMEOUT;
                            items.set(i, pi);
                            Log.v("multiping","PingerAv " + e.toString());
                        }
                    }
                } catch (Exception e) {
                    Log.v("multiping","PingerAv " + e.toString());
                }
            }
            Log.v("multiping", "PingerAv " + ia + "end" );
        }
    }

    TextView selection;

    private final static String SAVEFILE="hosts";
    final static int MAXTIME = 100000;
    final static int TIMEOUT = 3000;
    final static long PERIOD = 5000;
    final static long UPDATE = 1000;
    final static int MAXHOSTS = 12;
    Activity content=null;
    int m_position = 0;

    Thread m_background=null;
    boolean isRunning=false;

    final ArrayList<PingerItem> items = new ArrayList<PingerItem>();
    PingItemAdapter pia=null;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String localIp = getLocalIpAddress();
            if (localIp == null) localIp = "unknown";
            myTextView.setText("Local IP : " + localIp);

            pia.notifyDataSetChanged();
        }
    };

    public String getLocalIpAddress() {
        String sLocalIpAddress="";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String sIpAddress = inetAddress.getHostAddress().toString();
                        if(sIpAddress.startsWith("fe80:")) {
                            // Ignore IPv6 Link local address
                        } else if(sIpAddress.startsWith("::127.") || sIpAddress.startsWith("::172.")) {
                            // Ignore local loopback address
                        } else {
                            sLocalIpAddress = sLocalIpAddress + " " + sIpAddress;
                        }
                    }
                }
            }
            return sLocalIpAddress;
        } catch (SocketException ex) {
            Log.e(LOG_TAG, ex.toString());
        }
        return null;
    }

    class PingItemAdapter extends ArrayAdapter<PingerItem> {
        Activity context;

        PingItemAdapter(Activity context) {
            super(context, R.layout.pingitem, items);

            this.context=context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View row=convertView;
            ViewWrapper wrapper=null;

            if(row==null) {
                LayoutInflater inflater = context.getLayoutInflater();
                row=inflater.inflate(R.layout.pingitem, null);
                wrapper=new ViewWrapper(row);
                row.setTag(wrapper);
            }
            else {
                wrapper=(ViewWrapper)row.getTag();
            }

            int textcolor;
            String textresult;
            PingerItem pi = items.get(position);

            long result;
            if(pi.result_80>pi.result_av)
                result=pi.result_av;
            else
                result=pi.result_80;

            if(result>=MAXTIME) {
                textcolor = Color.GRAY;
                textresult = "wait..";
            } else if (result>=TIMEOUT) {
                textcolor = Color.RED;
                textresult = "timeout";
            } else {
                textcolor = Color.WHITE;
                textresult = result + "ms";
            }

            String sIp;
            if(pi.ia==null)
                sIp = "0.0.0.0";
            else
                sIp = pi.ia.toString().replaceFirst(".*/", "");
            wrapper.getViewHostIp().setTextColor(textcolor);
            wrapper.getViewHostIp().setText(pi.hostname + "\n" + sIp);
            wrapper.getViewDelay().setTextColor(textcolor);
            wrapper.getViewDelay().setText(textresult);
            return row;
        }
    }

    private boolean AddHostName(String hostname) {
        PingerItem pi = new PingerItem();
        pi.hostname = hostname;
        items.add(0,pi);
        pia.notifyDataSetChanged();

        Thread t = new Thread(new NameResolver(hostname));
        t.start();

        if(items.size()>=MAXHOSTS) {
            myEditText.setText("Max number of host");
            myEditText.setEnabled(false);
        } else {
            myEditText.setText("");
        }

        return true;
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        content=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiping);
        setTitle("Multi Ping");

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load
        // titles
        // from
        // strings.xml

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);// load icons from
        // strings.xml

        set(navMenuTitles, navMenuIcons);
        myEditText = (EditText)findViewById(R.id.myEditText);
        myTextView = (TextView)findViewById(R.id.myTextView);
        final ListView myListView = (ListView)findViewById(R.id.myListView);
        final Button myButton = (Button) findViewById(R.id.myButton);



        String localIp = getLocalIpAddress();
        if (localIp == null){ localIp = "unknown";}
        myTextView.setText("Local IP : " + localIp);

        pia = new PingItemAdapter(this);
        myListView.setAdapter(pia);


        myListView.setOnItemClickListener(new OnItemClickListener(){
            public void onItemClick(AdapterView av, View v, int position, long arg) {

                m_position = position;

                new AlertDialog.Builder(content)
                        .setTitle("Confirm")
                        .setMessage("Delete '" + items.get(m_position).hostname + "'")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                items.remove(m_position);
                                pia.notifyDataSetChanged();
                                if(items.size()<MAXHOSTS) {
                                    myEditText.setText("");
                                    myEditText.setEnabled(true);
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //
                            }
                        })
                        .setCancelable(true)
                        .show();

            }
        });

        myEditText.setOnKeyListener(new OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction()==KeyEvent.ACTION_DOWN)
                {
                    if(keyCode==KeyEvent.KEYCODE_DPAD_CENTER || keyCode==KeyEvent.KEYCODE_ENTER) {
                        String hostname = myEditText.getText().toString();
                        hostname = hostname.replace('*', '.');
                        boolean result = AddHostName(hostname);
                        saveItems();
                        return result;
                    }
                }
                return false;
            }
        });

        myButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(myEditText.getText().toString().equals("")){

                    Toast.makeText(MultiPing.this, "Please Enter Input", Toast.LENGTH_LONG).show();


                }
                else {
                    String hostname = myEditText.getText().toString();
                    hostname = hostname.replace('*', '.');
                    AddHostName(hostname);
                    saveItems();
                    return;
                }
            }
        });

        loadItems();
    }

    public void onStart() {
        super.onStart();

        m_background = new Thread(new Runnable() {
            public void run() {
                while(isRunning)
                {
                    for(int i=0; i<items.size(); i++) {
                        PingerItem pi = items.get(i);
                        if(pi.ia!=null) {
//	    						pi.result_80 = -1;
//	    						pi.result_av = -1;
//	    						items.set(i,pi);    						
                            Thread t1 = new Thread(new Pinger80(pi.ia));
                            t1.setName("Pinger80 " + pi.hostname);
                            t1.start();
                            Thread t2 = new Thread(new PingerAv(pi.ia));
                            t2.setName("PingerAv " + pi.hostname);
                            t2.start();
                        }
                    }

                    try {
                        for(int to=0; to<PERIOD; to+=UPDATE) {
                            Thread.sleep(UPDATE);
                            handler.sendMessage(handler.obtainMessage());
                        }
                    } catch (InterruptedException e) {
                        Log.v("multiping","InterruptedException");
                        break;
                    }
                } // end of while
            }
        });

        isRunning=true;
        m_background.start();
    }

    private void loadItems() {
        // Read host from file
        try {
            InputStream in = openFileInput(SAVEFILE);

            if(in!=null) {
                InputStreamReader tmp=new InputStreamReader(in);
                BufferedReader reader=new BufferedReader(tmp);
                String hostname;
//	    			StringBuffer buf=new StringBuffer();
                while ((hostname=reader.readLine()) != null) {
//	    				Log.v("multiping","read hostname="+hostname);
                    AddHostName(hostname);
                }
                in.close();
                pia.notifyDataSetChanged();
                myEditText.setText("");
//	    			Toast.makeText(this, "saved file loaded", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Throwable t)
        {
            //
        }
    }

    private void saveItems() {
        try {
            OutputStreamWriter out=
                    new OutputStreamWriter(openFileOutput(SAVEFILE, 0));
            for(int i=items.size()-1; i>=0; i--) {
                out.write(items.get(i).hostname + "\n");
            }
            out.close();
        }
        catch (Throwable t) {
            Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onStop() {
        super.onStop();
        //saveItems();
        isRunning= false;
    }

    public void onPause() {
        super.onPause();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.quick, menu);
//        menu.add(0, ActivityDiscovery.MENU_HELP, 0, R.string.preferences_help).setIcon(
//                android.R.drawable.ic_menu_help);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.bugreport:
//                String versionName = "";
//                try {
//                    versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
//                } catch (NameNotFoundException e) {
//                    e.printStackTrace();
//                }
//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("message/rfc822");
//                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"softgear@gmail.com"});
//                i.putExtra(Intent.EXTRA_SUBJECT, "BugReport " +  getString(R.string.app_name) + " " + versionName );
//                startActivity( Intent.createChooser(i, "Select Email App"));
//                return true;
            case R.id.refresh:
                refresh();
                return true;
            case ActivityDiscovery.MENU_HELP:
                startActivity(new Intent(ctxt, HelpMultiPing.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refresh() {
        try {
            OutputStreamWriter out=
                    new OutputStreamWriter(openFileOutput(SAVEFILE, 0));
            for(int i=items.size()-1; i>=0; i--) {
                out.write(items.get(i).hostname + "\n");
            }
            out.close();
        }
        catch (Throwable t) {
            Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_SHORT).show();
        }

        // Restart
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void onBackPressed()
    {
        finish();
        return;
    }
}

