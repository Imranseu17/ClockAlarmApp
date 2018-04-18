package com.example.imran.alarmclockapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imran.alarmclockapp.data.AlarmRemainderContract;
import com.example.imran.alarmclockapp.data.AlarmRemainderDBHelper;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    AlarmCursorAdapter alarmCursorAdapter;
    AlarmRemainderDBHelper alarmRemainderDBHelper = new AlarmRemainderDBHelper(this);
    ListView remainderListView;
    ProgressDialog progressDialog;
    TextView remaindertext;
    private String alarmTitle = "";
    private static final  int   VEHICLE_LOADER = 0;

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
                Intent intent = new Intent(view.getContext(), AlarmRemainderActivity.class);
                startActivity(intent);
                addRemainderTitile();
            }

        });

        getLoaderManager().restartLoader(VEHICLE_LOADER, null, this);

        getLoaderManager().initLoader(0, null, this);


        remainderListView = findViewById(R.id.list);
        remaindertext = findViewById(R.id.remainder_text);
        View emptyView = findViewById(R.id.empty_view);
        remainderListView.setEmptyView(emptyView);

        alarmCursorAdapter = new AlarmCursorAdapter(this, null);
        remainderListView.setAdapter(alarmCursorAdapter);

        remainderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, AlarmRemainderActivity.class);
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
        if(data.getCount() > 0){
            remaindertext.setVisibility(View.VISIBLE);
        }

        else {
            remaindertext.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {


        alarmCursorAdapter.swapCursor(null);
    }

    public void addRemainderTitile(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Remainder Title");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(input.getText().toString().isEmpty()){
                    return;
                }

                alarmTitle = input.getText().toString();
                ContentValues contentValues = new ContentValues();
                contentValues.put(AlarmRemainderContract.AlarmRemainderEntry.KEY_TITLE,
                        alarmTitle);
                Uri newUri = getContentResolver().insert
                        (AlarmRemainderContract.AlarmRemainderEntry.CONTENT_URI,contentValues);

                restartLoader();

                if(newUri == null){
                    Toast.makeText(getApplicationContext(),"Setting Remainder title failed",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Title set Successfully",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

    }

    private void restartLoader() {
        getLoaderManager().restartLoader(VEHICLE_LOADER,null,this);
    }
}
