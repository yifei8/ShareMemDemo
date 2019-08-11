package com.vivo.hybrid.game.sharememdemo;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.vivo.hybrid.game.sharememdemo.client.MemFetchClient;
import com.vivo.hybrid.game.sharememdemo.remote.MemFetchService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MemFetchClient client;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = findViewById(R.id.iv);
        findViewById(R.id.btn_send_msg).setOnClickListener(this);
        findViewById(R.id.btn_take_snapshot).setOnClickListener(this);
        findViewById(R.id.btn_take_snapshot2).setOnClickListener(this);
        client = MemFetchClient.getInstance();

        bind();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_send_msg) {
            client.registerMsgCallback(new MemFetchClient.OnMessageCb() {
                @Override
                public void callback(String json) {
                    Toast.makeText(MainActivity.this, json, Toast.LENGTH_LONG).show();
                }
            });
            client.sendTestMessageForCallback();
        } else if (id == R.id.btn_take_snapshot) {
            client.takeSnapshot(new MemFetchClient.OnTakeSnapshotCb() {
                @Override
                public void callback(Bitmap bitmap) {
                    if (bitmap != null) {
                        iv.setImageBitmap(bitmap);
                    } else {
                        Toast.makeText(MainActivity.this, "bitmap is null", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else if (id == R.id.btn_take_snapshot2) {
            client.takeSnapshot2(new MemFetchClient.OnTakeSnapshotCb() {
                @Override
                public void callback(Bitmap bitmap) {
                    if (bitmap != null) {
                        iv.setImageBitmap(bitmap);
                    } else {
                        Toast.makeText(MainActivity.this, "bitmap is null", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        unbind();
        super.onDestroy();
    }

    private void bind() {
        Intent intent = new Intent(MainActivity.this, MemFetchService.class);
        bindService(intent, client.getClientConnection(), Service.BIND_AUTO_CREATE);
    }

    private void unbind() {
        unbindService(client.getClientConnection());
    }


}
