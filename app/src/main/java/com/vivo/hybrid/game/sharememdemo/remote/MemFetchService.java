package com.vivo.hybrid.game.sharememdemo.remote;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.vivo.hybrid.game.sharememdemo.R;

public class MemFetchService extends Service implements RemoteContract.FromClient {
    private static final String TAG = MemFetchService.class.getSimpleName();

    private RemoteContract.ToClient toClient;

    public MemFetchService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        IBinder iBinder = new MemFetchStub(this);
        if (iBinder instanceof RemoteContract.ToClient) {
            toClient = (RemoteContract.ToClient) iBinder;
        }
        return iBinder;
    }

    @Override
    public void fromClientMessage(String json) {
        Log.d(TAG, "fromClientMessage: " + json);
        if (toClient == null) {
            return;
        }
        if ("takeSnapshot".endsWith(json)) {
            Bitmap bitmap = takeSnapshot();
            if (bitmap != null) {
                toClient.onSnapshotCallback(bitmap);
            }
        } else if ("getMessage".endsWith(json)) {
            String message = getServiceMessage();
            if (!TextUtils.isEmpty(message)) {
                toClient.sendToClientMessage(message);
            }
        } else {
            Toast.makeText(this, "action is not match", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap takeSnapshot() {
        return BitmapFactory.decodeResource(getResources(), R.drawable.tt);
    }

    private String getServiceMessage() {
        return "from service message";
    }

}
