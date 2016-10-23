package info.lamatricexiste.network;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

public class TracerouteUtil{
  private final String os = System.getProperty("os.name").toLowerCase();

  public  String traceRoute(String address) throws InterruptedException{
    String route = "";
    StringBuffer sb = new StringBuffer();
    try {
        Process traceRt = null;
        if(os.contains("win")) 
        {traceRt = Runtime.getRuntime().exec("/system/bin/busybox tracert " + InetAddress.getByName(address).getHostAddress());
        traceRt.waitFor();
        }
        //tracert
        // else traceRt = Runtime.getRuntime().exec("traceroute " + address.getHostAddress());

        // read the output from the command
        //route = convertStreamToString(traceRt.getInputStream());

        BufferedReader inputStream = new BufferedReader(
				new InputStreamReader(traceRt.getInputStream()));

		String s ="";
		
		// reading output stream of the command
		while ((s = inputStream.readLine()) != null) {
			/*System.out.println(s);*/
			sb.append(s + "\n");
		}

        
        
        // read any errors from the attempted command
        //String errors = convertStreamToString(traceRt.getErrorStream());
    }
    catch (IOException e) {
    	System.out.println(e);
    }
    String detail = sb.toString();
	return detail;  
  }
}