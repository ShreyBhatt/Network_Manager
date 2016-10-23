package info.lamatricexiste.network;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class SystemProperty extends Activity{

    private String ipadd;
    private ListView list_view;
    String[] NAMES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_property);
        ipadd = getIntent().getExtras().getString("IPAddress");
        try {
            putIp(InetAddress.getByName(ipadd));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

   public void putIp(InetAddress ipaddress){

    InetAddress ip;
    try {

        ip = ipaddress;
        String nameOS= System.getProperty("os.name");
        String osType= System.getProperty("os.arch");
        String osVersion= System.getProperty("os.version");
        long maxMemory = Runtime.getRuntime().maxMemory();
//        String hostname = ip.getHostName();
        String Ipaddress = ip.getHostAddress();
        NAMES = new String[]{
//                "Current host name : " + hostname,
                "Current IP address : " + Ipaddress,
                "OS Name : " + nameOS,
                "OS Type : " + osType,
                "OS Version: " + osVersion,
//                System.getenv("PROCESSOR_IDENTIFIER"),
//                System.getenv("PROCESSOR_ARCHITECTURE"),
//                System.getenv("PROCESSOR_ARCHITEW6432"),
//                System.getenv("NUMBER_OF_PROCESSORS"),
                "Available processors (cores) : " + Runtime.getRuntime().availableProcessors(),
                "Free memory (bytes) : " + Runtime.getRuntime().freeMemory(),
                "Maximum memory (bytes) : " + (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory),
                "Total memory (bytes) : " + Runtime.getRuntime().totalMemory(),
        };

        list_view = (ListView)findViewById(R.id.systemlistView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.system_list,NAMES);
        list_view.setAdapter(adapter);


//        list_view.setOnItemClickListener(
//                new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        String value = (String) list_view.getItemAtPosition(position);
////                        Toast.makeText(IPcalculator.this, "Position" + position + "value: " + value, Toast.LENGTH_LONG).show();
//                    }
//                }
//
//        );

//        System.out.println("Current host name : " + ip.getHostName());
//        System.out.println("Current IP address : " + ip.getHostAddress());
//        String nameOS= System.getProperty("os.name");
//        System.out.println("Operating system Name=>"+ nameOS);
//        String osType= System.getProperty("os.arch");
//        System.out.println("Operating system type =>"+ osType);
//        String osVersion= System.getProperty("os.version");
//        System.out.println("Operating system version =>"+ osVersion);
//
//        System.out.println(System.getenv("PROCESSOR_IDENTIFIER"));
//        System.out.println(System.getenv("PROCESSOR_ARCHITECTURE"));
//        System.out.println(System.getenv("PROCESSOR_ARCHITEW6432"));
//        System.out.println(System.getenv("NUMBER_OF_PROCESSORS"));
//        /* Total number of processors or cores available to the JVM */
//    System.out.println("Available processors (cores): " +
//        Runtime.getRuntime().availableProcessors());
//
//    /* Total amount of free memory available to the JVM */
//    System.out.println("Free memory (bytes): " +
//        Runtime.getRuntime().freeMemory());
//
//    /* This will return Long.MAX_VALUE if there is no preset limit */
//    long maxMemory = Runtime.getRuntime().maxMemory();
//    /* Maximum amount of memory the JVM will attempt to use */
//    System.out.println("Maximum memory (bytes): " +
//        (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));
//
//    /* Total memory currently in use by the JVM */
//    System.out.println("Total memory (bytes): " +
//        Runtime.getRuntime().totalMemory());
//
//
//        NetworkInterface network = NetworkInterface.getByInetAddress(ip);
//
//        byte[] mac = network.getHardwareAddress();
//
//        System.out.print("Current MAC address : ");
//
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < mac.length; i++) {
//            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
//        }
//        System.out.println(sb.toString());

    }
//    catch (UnknownHostException e) {
//
//        e.printStackTrace();
//
//    } catch (SocketException e){
//
//        e.printStackTrace();
//
//    }
    catch (Exception e){
  
        e.printStackTrace();
  
    }
  
   }
  
}

