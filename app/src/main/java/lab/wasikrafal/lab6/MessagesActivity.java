package lab.wasikrafal.lab6;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class MessagesActivity extends AppCompatActivity
{
    SwipeRefreshLayout refreshLayout;
    Handler refreshHandler;
    Runnable refreshTask;
    ListView listView;
    MessageAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                addMessageDialog();
            }
        });

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                Log.i("refresh", "refreshing by swipe...");
                receiveMessages();
            }
        });

        listView = findViewById(R.id.list);
        listAdapter = new MessageAdapter(this, new ArrayList<String>());
        listView.setAdapter(listAdapter);

        refreshHandler = new Handler();
        refreshTask = new Runnable()
        {
            @Override
            public void run() {
                receiveMessages();
                refreshHandler.postDelayed(refreshTask, 60000);
            }
        };
    }

    protected void onResume()
    {
        super.onResume();
        refreshTask.run();
    }

    protected void onPause()
    {
        super.onPause();
        refreshHandler.removeCallbacks(refreshTask);
    }

    private void addMessageDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_add_message, null);

        builder.setView(layout)
                .setPositiveButton("Wyślij", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        EditText et = (EditText)layout.findViewById(R.id.et_message);
                        String message = et.getText().toString();
                        sendMessage(message);
                    }
                }).setNegativeButton("Anuluj", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            { }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void sendMessage(String msg)
    {
        SendTask task = new SendTask(new SendResponse()
        {
            @Override
            public void processSending(Boolean isSent)
            {
                if (isSent)
                {
                    Snackbar.make(findViewById(R.id.msg_layout), "Wysłano wiadomość", Snackbar.LENGTH_LONG).show();
                    receiveMessages();
                }
                else
                {
                    Snackbar.make(findViewById(R.id.msg_layout), "Błąd wysyłania", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        task.execute(msg);
    }

    private void receiveMessages()
    {
        GetTask task = new GetTask(new GetResponse()
        {
            @Override
            public void processReceiving(Boolean isReceived, List<String> messages)
            {
                if (isReceived)
                {
                    refreshMessages(messages);
                    Snackbar.make(findViewById(R.id.msg_layout), "Pobrano wiadomości", Snackbar.LENGTH_SHORT).show();
                }
                else
                {
                    Snackbar.make(findViewById(R.id.msg_layout), "Błąd pobierania", Snackbar.LENGTH_LONG).show();
                }
                refreshLayout.setRefreshing(false);
            }
        });
        task.execute();
    }

    private void refreshMessages(List<String> messages)
    {
        listAdapter = new MessageAdapter(this, messages);
        listView.setAdapter(listAdapter);
    }
}
