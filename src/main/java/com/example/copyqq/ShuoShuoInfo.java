package com.example.copyqq;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Tools.HttpTools;

import org.json.JSONException;
import org.json.JSONObject;

public class ShuoShuoInfo extends AppCompatActivity {

    private String ssid;
    private TextView username, sstime, contact, todo, tv_dzusers;
    private ImageView iv_dz, iv_pl, iv_okpl;
    private RecyclerView rv_pl;
    private EditText pl;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 548:
                    //获取某条说说所有点赞的用户
                    Toast.makeText(ShuoShuoInfo.this, ""+msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 53:
                    //得到某条说说
                    try {
                        JSONObject job = new JSONObject((String)msg.obj);
                        JSONObject j = job.getJSONObject("result");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5464:
                    //得到所有评论
                    Toast.makeText(ShuoShuoInfo.this, ""+msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuo_shuo_info);
        ssid = getIntent().getStringExtra("ssid");
        if("".equals(ssid) || ssid == null){
            Toast.makeText(ShuoShuoInfo.this, "没找到该说说!!!", Toast.LENGTH_SHORT).show();
        }else{
            //找到各种控件
            username = (TextView) findViewById(R.id.username);
            sstime = (TextView) findViewById(R.id.sstime);
            contact = (TextView) findViewById(R.id.contact);
            todo = (TextView) findViewById(R.id.todo);
            tv_dzusers = (TextView) findViewById(R.id.tv_dzusers);
            iv_dz = (ImageView) findViewById(R.id.iv_dz);
            iv_pl = (ImageView) findViewById(R.id.iv_pl);
            iv_okpl = (ImageView) findViewById(R.id.iv_okpl);
            rv_pl = (RecyclerView) findViewById(R.id.rv_pl);
            pl = (EditText) findViewById(R.id.pl);
            //获取说说
            HttpTools.getShuoShuo("onlyOne", ssid, handler, false);
            //获取所有点赞用户
            HttpTools.getAllDZUser(handler, ssid);
            //获取所有评论
            HttpTools.getAllPL(handler, ssid);

        }
    }
}
