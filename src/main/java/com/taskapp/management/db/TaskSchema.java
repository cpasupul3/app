
package com.taskapp.management.db;

import android.provider.BaseColumns;

public final class TaskSchema {
    public static final class TaskTable implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_NAME = "name";
    }
}
