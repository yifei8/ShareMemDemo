package com.vivo.hybrid.game.sharememdemo.remote;

import android.graphics.Bitmap;
import android.os.MemoryFile;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

import com.vivo.hybrid.game.sharememdemo.IMemAIDL;
import com.vivo.hybrid.game.sharememdemo.IMemCallback;
import com.vivo.hybrid.game.sharememdemo.IMsgCallback;
import com.vivo.hybrid.game.sharememdemo.utils.BitmapUtil;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * []
 * author: yifei
 * created at 2019/8/10 下午8:21
 */
public class MemFetchStub extends IMemAIDL.Stub implements RemoteContract.ToClient {
    private static final String TAG = MemFetchStub.class.getSimpleName();

    private static final int MEM_FILE_SIZE = 1024 * 1024 * 100; //1M
    private static final String MEM_FILE_NAME = "test";
    private MemoryFile shareMem;
    private ParcelFileDescriptor parcelFileDescriptor;

    private RemoteContract.FromClient client;
    private IMsgCallback msgCallback;
    private IMemCallback snapshotCallback;

    public MemFetchStub(RemoteContract.FromClient client) {
        this.client = client;
        createMemFile();
    }

    /**
     * client 发送消息给 server
     *
     * @param json
     * @throws RemoteException
     */
    @Override
    public void sendMessage(String json) throws RemoteException {
        client.fromClientMessage(json);
    }

    /**
     * client 向 server 注册回调
     *
     * @param callback
     * @throws RemoteException
     */
    @Override
    public void onMessageCallback(IMsgCallback callback) throws RemoteException {
        msgCallback = callback;
    }


    @Override
    public void takeSnapshot(IMemCallback callback) throws RemoteException {
        snapshotCallback = callback;
    }

    @Override
    public void sendToClientMessage(String json) {
        if (msgCallback != null) {
            try {
                msgCallback.onMsgCallback(json);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSnapshotCallback(Bitmap bitmap) {
        if (snapshotCallback != null) {
            try {
                byte[] buffer = BitmapUtil.Bitmap2Bytes(bitmap);
                writeBitmap2Mem(buffer);
                snapshotCallback.onSnapshotCallback(parcelFileDescriptor, buffer.length);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeBitmap2Mem(byte[] buffer) {
        createMemFile();
        if (buffer != null && buffer.length > 0 && shareMem != null) {
            try {
                shareMem.writeBytes(buffer, 0, 0, buffer.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createMemFile() {
        try {
            if (shareMem == null) {
                shareMem = new MemoryFile(MEM_FILE_NAME, MEM_FILE_SIZE);
            }
            if (parcelFileDescriptor == null) {
                Method method = MemoryFile.class.getDeclaredMethod("getFileDescriptor");
                FileDescriptor fd = (FileDescriptor) method.invoke(shareMem);
                parcelFileDescriptor = ParcelFileDescriptor.dup(fd);
            }
            /**
             * 在设置了allowPurging为false之后，这个MemoryFile对应的Ashmem就会被标记成"pin"，
             * 那么即使在android系统内存不足的时候，也不会对这段内存进行回收。
             * 另外，由于Ashmem默认都是"unpin"的，因此申请的内存在某个时间点内都可能会被回收掉，这个时候是不可以再读写了
             *
             */
            /*if (shareMem != null) {
                shareMem.allowPurging(false);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
