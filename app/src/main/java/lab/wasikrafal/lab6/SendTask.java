package lab.wasikrafal.lab6;

import android.os.AsyncTask;
import android.util.Log;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Rafał on 20.04.2018.
 */

public class SendTask extends AsyncTask <String, Void, Boolean>
{
    private SendResponse delegate = null;

    public SendTask (SendResponse del)
    {
        delegate = del;
    }

    protected Boolean doInBackground(String... Params)
    {
        int responseCode = 404;
        try
        {
            URL service = new URL("http://e-biuro.net/android10/messages/" + Params[0]);
            HttpURLConnection connection=(HttpURLConnection) service.openConnection();
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            responseCode = connection.getResponseCode();
            Log.i("conRes", String.valueOf(connection.getResponseCode()));
            connection.disconnect();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return responseCode == 200;
    }

    protected void onPostExecute(Boolean result)
    {
        delegate.processSending(result);
    }
}
