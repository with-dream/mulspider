package com.example.core.db.rocksdb.jmx;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public abstract class RocksMetrics {
    private ObjectName name;

    public void register() {
        try {
            this.name = new ObjectName(this.getObjectName());
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            mBeanServer.registerMBean(this, name);
        } catch (JMException e) {
            System.err.println("Error while register the MBean " + name + " -- " + e.getMessage());
        }
    }

    public void unregister() {
        if (this.name != null) {
            try {
                MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
                mBeanServer.unregisterMBean(name);
            } catch (JMException e) {
                System.err.println("Unable to unregister the MBean " + name);
            } finally {
                this.name = null;
            }
        }
    }

    protected abstract String getObjectName();

    protected long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }
}
