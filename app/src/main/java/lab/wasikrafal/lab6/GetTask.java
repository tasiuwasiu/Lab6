package lab.wasikrafal.lab6;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rafał on 27.04.2018.
 */

public class GetTask extends AsyncTask <Void,Void,Boolean>
{
    private GetResponse delegate = null;
    private List<String> response = null;

    public GetTask (GetResponse del)
    {
        delegate = del;
    }

    protected Boolean doInBackground(Void... Params)
    {
        response = new ArrayList<>();
        int responseCode = 404;
        try
        {
            URL service = new URL("http://e-biuro.net/android10/messages/");
            HttpURLConnection connection=(HttpURLConnection) service.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
            String json=reader.readLine();
            JSONObject jObject=new JSONObject(json);
            responseCode = connection.getResponseCode();
            //Log.i("json", jObject.toString());
            JSONArray messages = jObject.getJSONArray("Messages");
            for (int i=0; i < messages.length(); i++)
            {
                response.add(messages.getJSONObject(i).getString("message"));
            }
            Log.i("data", response.toString());
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
        delegate.processReceiving(result, response);
    }
}
