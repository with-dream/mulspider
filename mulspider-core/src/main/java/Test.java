import com.example.core.annotation.ExtractMethod;
import com.example.core.db.rocksdb.QueueItem;
import com.example.core.db.rocksdb.RocksQueue;
import com.example.core.db.rocksdb.RocksStore;
import com.example.core.db.rocksdb.StoreOptions;
import com.example.core.db.rocksdb.exception.RocksQueueException;
import com.example.core.db.rocksdb.util.Bytes;
import com.example.core.utils.D;
import org.apache.commons.lang3.SerializationUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Test {
    String str = null;

    public static void main(String[] args) {
        Integer it = 1;
        byte[] b = SerializationUtils.serialize(it);
        D.d("==>" + SerializationUtils.deserialize(b));
    }


    private void tt() {
        str = new String("reflectInit==>{WallHaven=MethodReflect{name='WallHaven', enable=false, share=false, clazz=class");
        ttt(str);
    }

    private void ttt(String ss) {
        D.d(ss == str);
    }
}