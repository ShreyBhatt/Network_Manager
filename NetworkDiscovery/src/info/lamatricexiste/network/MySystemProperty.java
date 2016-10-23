package info.lamatricexiste.network;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import info.lamatricexiste.network.Utils.Help;
import info.lamatricexiste.network.Utils.HelpSysProperty;
import info.lamatricexiste.network.Utils.Prefs;

public class MySystemProperty extends BaseActivity{

    private String ipadd;
    private ListView list_view;
    String[] NAMES;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private Button sharesys= null;

    StringBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_property);
        setTitle("System Properties");

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);

        sharesys = (Button)findViewById(R.id.sharesys);

//        ipadd = getIntent().getExtras().getString("IPAddress");
        try {
            putIp();
        } catch (Exception e) {
            e.printStackTrace();
        }

        sharesys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        String message = "\nModel No:" + Build.MODEL + "\nHost:" + Build.HOST
                                + "\nOs Name:" +builder.toString() + "\nUser:" +Build.USER
                                +"\nSerial No:"+Build.SERIAL
                                +"\nProduct Name:"+Build.PRODUCT+"\nManufacturer:"+Build.MANUFACTURER
                                +"\nDevice Name:"+Build.DEVICE+"\nBrand Name:"+Build.BRAND
                                +"\nVersion:"+Build.VERSION.RELEASE+"\nAPI Level:"+Build.VERSION.SDK
                                +"\nHardware:"+Build.HARDWARE
                                +"\nID:"+Build.ID;
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_TEXT, message);
                        share.putExtra(Intent.EXTRA_SUBJECT,"System Properties");
                        startActivity(Intent.createChooser(share, "Select From Below"));

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
                startActivity(new Intent(ctxt, HelpSysProperty.class));
                return true;
//            case ActivityDiscovery.MENU_EXPORT:
//                export();
//                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void putIp(){

//        InetAddress ip;
        try {

//            ip = InetAddress.getLocalHost();
            String host = Build.HOST;
            String modelno = Build.MODEL;
            String user = Build.USER;
            String serialno = Build.SERIAL;
            String productname = Build.PRODUCT;
            String manufacturer = Build.MANUFACTURER;
            String devicename = Build.DEVICE;
            String brandname = Build.BRAND;
            String version = Build.VERSION.RELEASE;
            String hardware = Build.HARDWARE;
            String id = Build.ID;

            String apiLevel = Build.VERSION.SDK;


            String nameOS= System.getProperty("os.name");
            String osType= System.getProperty("os.arch");
//            String osVersion= System.getProperty("os.version");
//            long maxMemory = Runtime.getRuntime().maxMemory();
//            String hostname = ip.getHostName();
//            String Ipaddress = ip.getHostAddress();

            builder = new StringBuilder();
            builder.append("android : ").append(Build.VERSION.RELEASE);

            Field[] fields = Build.VERSION_CODES.class.getFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                int fieldValue = -1;

                try {
                    fieldValue = field.getInt(new Object());
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                if (fieldValue == Build.VERSION.SDK_INT) {
                    builder.append(" : ").append(fieldName).append(" : ");
                    builder.append("sdk=").append(fieldValue);
                }
            }




            NAMES = new String[]{
                    "Host             : " + host,
                    "OS Name     : " + builder.toString(),
                    "Model No    : " + modelno,
                    "User             : " + user,
                    "Serial No     : " + serialno,
                    "Product Name: " + productname,
                    "Manufacturer: " + manufacturer,
                    "Device Name : " + devicename,
                    "Brand Name  : " + brandname,
                    "Version         : " + version,
                    "API Level      : " + apiLevel,
                    "Hardware      : " + hardware,
                    "ID                   : " + id,


//                    "OS Type        : " + osType,
//                    "OS Version: " + osVersion,
//                System.getenv("PROCESSOR_IDENTIFIER"),
//                System.getenv("PROCESSOR_ARCHITECTURE"),
//                System.getenv("PROCESSOR_ARCHITEW6432"),
//                System.getenv("NUMBER_OF_PROCESSORS"),
//                    "Available processors (cores) : " + Runtime.getRuntime().availableProcessors(),
//                    "Free memory (bytes) : " + Runtime.getRuntime().freeMemory(),
//                    "Maximum memory (bytes) : " + (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory),
//                    "Total memory (bytes) : " + Runtime.getRuntime().totalMemory(),
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


