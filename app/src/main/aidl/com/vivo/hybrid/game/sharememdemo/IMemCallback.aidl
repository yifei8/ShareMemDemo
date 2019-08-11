// IMemoryAidlInterface.aidl
package com.vivo.hybrid.game.sharememdemo;

interface IMemCallback {

     void onSnapshotCallback(in ParcelFileDescriptor data,in int length);

}
