package info.lamatricexiste.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PingIP {

  public static String runPing(String command) {
	  
	  StringBuffer sb = new StringBuffer();
		try {
			Process p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader inputStream = new BufferedReader(
					new InputStreamReader(p.getInputStream()));

			String s ="";
			
			// reading output stream of the command
			while ((s = inputStream.readLine()) != null) {
				/*System.out.println(s);*/
				sb.append(s + "\n");
			}
				
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		String detail = sb.toString();
		return detail;
		
	}

/*	public static void main(String[] args) {
		
		String ip = "google.com";
		runSystemCommand("ping " +ip);

	
	}
*/}