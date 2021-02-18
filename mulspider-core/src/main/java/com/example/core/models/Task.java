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
