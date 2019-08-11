package com.vivo.hybrid.game.sharememdemo.remote;

import android.graphics.Bitmap;

/**
 * [description]
 * author: yifei
 * created at 2019/8/10 下午9:54
 */
public interface RemoteContract {

    interface FromClient {
        void fromClientMessage(String json);
    }

    interface ToClient {
        void sendToClientMessage(String json);

        void onSnapshotCallback(Bitmap bitmap);
    }
}
