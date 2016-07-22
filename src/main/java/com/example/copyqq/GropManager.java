package com.example.copyqq;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Tools.resource;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashMap;

public class GropManager extends AppCompatActivity {
    TextView ok;
    TextView addgroup;
    ListView grops;
    MyAdapter adapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grop_manager);
        ok = (TextView) findViewById(R.id.ok);
        addgroup = (TextView) findViewById(R.id.addgroup);
        grops = (ListView) findViewById(R.id.grops);
        adapter = new MyAdapter();
        grops.setAdapter(adapter);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "GropManager Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.copyqq/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "GropManager Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.copyqq/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return resource.gruops.size();
        }

        @Override
        public Object getItem(int position) {
            return resource.gruops.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(GropManager.this, R.layout.groupmanager_item, null);
            }

            HashMap<String, String> hm = (HashMap<String, String>) resource.gruops.get(position);

            String itemName = hm.get("group");
            TextView tv_name = (TextView) convertView.findViewById(R.id.groupname);
            RelativeLayout del_dra = (RelativeLayout) convertView.findViewById(R.id.del_dra);
            final ImageView san = (ImageView) convertView.findViewById(R.id.san);
            final Button del = (Button) convertView.findViewById(R.id.del);

            tv_name.setText(itemName + "");
            del.setVisibility(View.GONE);
            san.setVisibility(View.VISIBLE);
            convertView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (del.getVisibility() == View.VISIBLE) {
                        del.setVisibility(View.GONE);
                        san.setVisibility(View.VISIBLE);
                    }
                    return false;
                }
            });
            del_dra.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (del.getVisibility() == View.VISIBLE) {
                        del.setVisibility(View.GONE);
                        san.setVisibility(View.VISIBLE);
                    } else {
                        del.setVisibility(View.VISIBLE);
                        san.setVisibility(View.GONE);
                    }
                }
            });
            tv_name.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (del.getVisibility() == View.VISIBLE) {
                                del.setVisibility(View.GONE);
                                san.setVisibility(View.VISIBLE);
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                    }
                    return false;
                }
            });

            return convertView;
        }
    }
}
