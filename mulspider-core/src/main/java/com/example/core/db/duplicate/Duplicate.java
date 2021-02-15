package com.example.core.db.duplicate;

import com.example.core.db.DBManager;
import com.example.core.models.Task;

public abstract class Duplicate {
    public abstract boolean duplicate(DBManager dbManager, Task task);
}
