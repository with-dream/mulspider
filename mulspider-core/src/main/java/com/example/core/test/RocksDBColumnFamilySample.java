package com.example.core.test;

import com.example.core.utils.D;
import org.rocksdb.*;

import java.util.ArrayList;
import java.util.List;

public class RocksDBColumnFamilySample {
    static {
        RocksDB.loadLibrary();
    }

    public static void main(final String[] args) {

    }

    public void tt() throws RocksDBException {
        final String db_path = "./test";

        System.out.println("RocksDBColumnFamilySample");
        List<byte[]> familyList = RocksDB.listColumnFamilies(new Options(), db_path);
        for (byte[] b : familyList)
            D.d("==>" + new String(b));
        // open DB with two column families
        final List<ColumnFamilyDescriptor> columnFamilyDescriptors =
                new ArrayList<>();
        // have to open default column family
        columnFamilyDescriptors.add(new ColumnFamilyDescriptor(
                RocksDB.DEFAULT_COLUMN_FAMILY, new ColumnFamilyOptions()));
        // open the new one, too
        columnFamilyDescriptors.add(new ColumnFamilyDescriptor(
                "new_cf".getBytes(), new ColumnFamilyOptions()));
        final List<ColumnFamilyHandle> columnFamilyHandles = new ArrayList<>();
        try (final DBOptions options = new DBOptions();
             final RocksDB db = RocksDB.open(options, db_path,
                     columnFamilyDescriptors, columnFamilyHandles)) {
            assert (db != null);

            try {
                // put and get from non-default column family
                db.put(columnFamilyHandles.get(1), new WriteOptions(), "key".getBytes(), "value".getBytes());

                byte[] res = db.get(columnFamilyHandles.get(1), "key".getBytes());
                D.d("==>" + new String(res));

                // atomic write
                try (final WriteBatch wb = new WriteBatch()) {
                    wb.put(columnFamilyHandles.get(0), "key2".getBytes(),
                            "value2".getBytes());
                    wb.put(columnFamilyHandles.get(1), "key3".getBytes(),
                            "value3".getBytes());
                    wb.delete(columnFamilyHandles.get(1), "key".getBytes());
                    db.write(new WriteOptions(), wb);
                }

                // drop column family
//                db.dropColumnFamily(columnFamilyHandles.get(1));
            } finally {
                for (final ColumnFamilyHandle handle : columnFamilyHandles) {
                    handle.close();
                }
                db.close();
            }
        }
    }
}