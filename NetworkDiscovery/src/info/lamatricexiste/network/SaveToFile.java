package info.lamatricexiste.network;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class SaveToFile {

	public static void generateNoteOnSD(Context context, String sFileName, String sBody) {
	    try {
	        File root = new File(Environment.getExternalStorageDirectory(), "Notes");
	        if (!root.exists()) {
	            root.mkdirs();
	        }
	        File gpxfile = new File(root, sFileName);
	        FileWriter writer = new FileWriter(gpxfile,true);
	        String message="Following saved on :";
	    	String now =new Date().toString();
	        writer.write(10);
	        writer.write(10);
	        writer.write(10);
	        writer.write(message);
	        writer.write(now);
	        writer.append(sBody);
	        writer.write(10);
	        writer.flush();
	        writer.close();
	        Toast.makeText(context, "Saved", Toast.LENGTH_LONG).show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	
	
}
