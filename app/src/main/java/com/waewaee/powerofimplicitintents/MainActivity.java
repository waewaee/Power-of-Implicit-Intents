package com.waewaee.powerofimplicitintents;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final int REQUEST_SELECT_CONTACT = 2;

    VideoView videoView;
    TextView tvContact;
    MaterialButton btnTimer, btnCalendar, btnContact, btnVideo, btnWebSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.vv_video);
        tvContact = findViewById(R.id.tv_contact);

        btnTimer = findViewById(R.id.btn_timer);
        btnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER);
                intent.putExtra(AlarmClock.EXTRA_MESSAGE, "Timer for workout");
                intent.putExtra(AlarmClock.EXTRA_LENGTH, "120");
                intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        btnCalendar = findViewById(R.id.btn_calendar_event);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setData(CalendarContract.Events.CONTENT_URI);
                intent.putExtra(CalendarContract.Events.TITLE, "Event for workout");
                intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Waee's home");
                intent.putExtra(CalendarContract.Events.DESCRIPTION, "Workout event to get cool abs");

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        btnVideo = findViewById(R.id.btn_video);
        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
                }
            }
        });

        btnContact = findViewById(R.id.btn_select_contact);
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_SELECT_CONTACT);
                }
            }
        });

        btnWebSearch = findViewById(R.id.btn_web_search);
        btnWebSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, "Justin Bieber");

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(uri);
            videoView.start();
        }

        else if (requestCode == REQUEST_SELECT_CONTACT && resultCode == RESULT_OK) {
            if (data != null) {
                Uri contactUri = data.getData();
                if (contactUri != null) {
                    Cursor cursor = getContentResolver().query(contactUri, null,
                            null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                        String name = cursor.getString(nameIndex);
                        tvContact.setText(name);

                        String contactId =
                                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

//                        Cursor phoneDetailColumn = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
//
//                        if (phoneDetailColumn != null) {
//                            while (phoneDetailColumn.moveToNext()) {
//                                String phoneNumber = phoneDetailColumn.getString(phoneDetailColumn.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                                tvContact.append(" Phone: " + phoneNumber);
//                            }
//                            phoneDetailColumn.close();
//                        }

                        cursor.close();
                    }
                }

            }
        }
    }
}