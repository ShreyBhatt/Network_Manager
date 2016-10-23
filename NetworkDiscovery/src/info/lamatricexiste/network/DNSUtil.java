package info.lamatricexiste.network;

import java.net.InetAddress;
import java.net.UnknownHostException;

import android.os.AsyncTask;

public class DNSUtil extends AsyncTask<String, Integer, String>
{
    @Override
    protected String doInBackground(String... params)
    {
        InetAddress addr = null;
        try
        {
            addr = InetAddress.getByName(params[0]);
        }

        catch (UnknownHostException e)
        {
                        e.printStackTrace();
        }
        return addr.getHostAddress();
    }
}