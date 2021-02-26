package com.example.core.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Task implements Serializable, Comparable<Task> {
    private static final long serialVersionUID = 673293417482457124L;
    public Map<String, Object> meta = new HashMap<>();
    /**
     * head 1   tail 1000
     * especial rockDB only has high(<300) middle(300< >600) low(>600)
     */
    public int priority;

    public boolean ignore;

    public boolean exitThread;

    public void put(String key, Object value) {
        meta.put(key, value);
    }

    public <T> T getMeta(String key) {
        if (meta.containsKey(key))
            return (T) meta.get(key);
        return null;
    }

    public <T> T removeMeta(String key) {
        if (meta.containsKey(key))
            return (T) meta.remove(key);
        return null;
    }

    @Override
    public String toString() {
        return "Task{" +
                ", meta=" + meta +
                ", priority=" + priority +
                ", ignore='" + ignore + '\'' +
                '}';
    }

    @Override
    public int compareTo(Task o) {
        return this.priority - o.priority;
    }
}
