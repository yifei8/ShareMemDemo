// IMemoryAidlInterface.aidl
package com.vivo.hybrid.game.sharememdemo;

import com.vivo.hybrid.game.sharememdemo.IMsgCallback;
import com.vivo.hybrid.game.sharememdemo.IMemCallback;

interface IMemAIDL {

     void sendMessage(String json);

     void onMessageCallback(IMsgCallback callback);

     void takeSnapshot(IMemCallback callback);
}
