package lab.wasikrafal.lab6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Rafał on 28.04.2018.
 */

public class MessageAdapter extends BaseAdapter
{
    //private Context context;
    private List<String> messages;
    private LayoutInflater inflater;

    public MessageAdapter(Context context, List<String> messages)
    {
        //this.context = context;
        this.messages = messages;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return messages.size();
    }

    @Override
    public String getItem(int i)
    {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        if (view == null)
        {
            view = inflater.inflate(R.layout.list_item_message, viewGroup, false);
        }
        TextView messageBox = (TextView)view.findViewById(R.id.tv_message);
        messageBox.setText(getItem(i));

        return view;
    }
}
