
package com.taskapp.management;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.taskapp.management.db.TaskStorage;
import com.taskapp.management.db.TaskSchema;

public class DashboardActivity extends AppCompatActivity {

    private TaskStorage taskStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        taskStorage = new TaskStorage(this);

        renderTaskList();
    }

    private void renderTaskList() {
        LinearLayout taskContainer = findViewById(R.id.task_container);
        taskContainer.removeAllViews();

        SQLiteDatabase db = taskStorage.getReadableDatabase();
        Cursor cursor = db.query(TaskSchema.TaskTable.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String taskName = cursor.getString(cursor.getColumnIndex(TaskSchema.TaskTable.COLUMN_NAME));
            addTaskView(taskContainer, taskName);
        }

        cursor.close();
    }

    private void addTaskView(LinearLayout container, String taskName) {
        View taskView = getLayoutInflater().inflate(R.layout.task_item, null);
        TextView taskText = taskView.findViewById(R.id.task_name_text);
        Button deleteButton = taskView.findViewById(R.id.delete_button);

        taskText.setText(taskName);
        deleteButton.setOnClickListener(v -> deleteTask(taskName));

        container.addView(taskView);
    }

    private void deleteTask(String taskName) {
        SQLiteDatabase db = taskStorage.getWritableDatabase();
        db.delete(TaskSchema.TaskTable.TABLE_NAME, TaskSchema.TaskTable.COLUMN_NAME + "=?", new String[]{taskName});
        renderTaskList();
        Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show();
    }

    private void addNewTask(String taskName) {
        SQLiteDatabase db = taskStorage.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskSchema.TaskTable.COLUMN_NAME, taskName);
        db.insert(TaskSchema.TaskTable.TABLE_NAME, null, values);
        renderTaskList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_task_menu) {
            showTaskInputDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showTaskInputDialog() {
        final EditText taskInput = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("New Task")
                .setMessage("What task would you like to add?")
                .setView(taskInput)
                .setPositiveButton("Save", (dialog, which) -> {
                    String taskName = taskInput.getText().toString().trim();
                    if (!taskName.isEmpty()) {
                        addNewTask(taskName);
                    } else {
                        Toast.makeText(this, "Task cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
