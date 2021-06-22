package com.example.contactlist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.collection.ArraySet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // initialize variables
    RecyclerView recyclerView;
    ArrayList<ContactModel> arrayList = new ArrayList<ContactModel>();
    MainAdapter adapter;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        register = (Button)findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent send = new Intent(MainActivity.this, RegisterUser.class);
                startActivity(send);

            }
        });

        recyclerView = findViewById(R.id.recycler_view);


//        checkPermission();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CONTACTS},100);
        } else {
            getContactList();
        }
    }

    private void getContactList() {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;

        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC";

        Cursor cursor = getContentResolver().query(uri, null, null, null, sort);

        // initialize firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef;

        Long i = 0l;

        if ( cursor.getCount() > 0 ){
            while (cursor.moveToNext()){
                try {

                    String id_ = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    // initialize phone URI
                    Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                    // initialize selection
                    String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" =?";

                    Cursor phoneCursor = getContentResolver().query(uriPhone,null,selection,new String[]{id_}, null);
                    if (phoneCursor.moveToNext()){
                        String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        ContactModel model = new ContactModel();
                        model.setName(name);
                        model.setNumber(number);
                        arrayList.add(model);
                        phoneCursor.close();

                        // write to database
//                        writeToDatabase(name,);

                        myRef = database.getReference("name"+i);
                        i = i + 1l;
                        myRef.setValue(name);
                    }

                } catch (Exception e ) {
                    ;
                }


            }
            cursor.close();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MainAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);
    }

    private void writeToDatabase() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            // when permission is granted
            // call method
            getContactList();
        } else {
            // when permissions
            Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();

            checkPermission();
        }
    }
}