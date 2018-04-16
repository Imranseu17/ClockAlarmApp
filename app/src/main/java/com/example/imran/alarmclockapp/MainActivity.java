package com.example.imran.alarmclockapp;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.imran.alarmclockapp.data.AlarmRemainderContract;
import com.example.imran.alarmclockapp.data.AlarmRemainderDBHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    AlarmCursorAdapter alarmCursorAdapter;
    AlarmRemainderDBHelper alarmRemainderDBHelper = new AlarmRemainderDBHelper(this);
    ListView remainderListView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddRemainderActivity.class);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(0,null,this);


        remainderListView = findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        remainderListView.setEmptyView(emptyView);

        alarmCursorAdapter = new AlarmCursorAdapter(this, null);
        remainderListView.setAdapter(alarmCursorAdapter);

        remainderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, AddRemainderActivity.class);
                Uri contentVehicleUri = ContentUris.withAppendedId(AlarmRemainderContract.
                        AlarmRemainderEntry.CONTENT_URI,id);
                intent.setData(contentVehicleUri);
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String [] projection = {
            AlarmRemainderContract.AlarmRemainderEntry._ID,
            AlarmRemainderContract.AlarmRemainderEntry.KEY_TITLE,
            AlarmRemainderContract.AlarmRemainderEntry.KEY_DATE,
            AlarmRemainderContract.AlarmRemainderEntry.KEY_TIME,
            AlarmRemainderContract.AlarmRemainderEntry.KEY_REPEAT,
            AlarmRemainderContract.AlarmRemainderEntry.KEY_REPEAT_NO,
            AlarmRemainderContract.AlarmRemainderEntry.KEY_REPEAT_TYPE,
            AlarmRemainderContract.AlarmRemainderEntry.KEY_ACTIVE


        };
        return new CursorLoader(this, AlarmRemainderContract.AlarmRemainderEntry.CONTENT_URI,
                projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        alarmCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {


        alarmCursorAdapter.swapCursor(null);
    }
}
