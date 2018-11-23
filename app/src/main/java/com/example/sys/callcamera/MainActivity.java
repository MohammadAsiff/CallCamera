package com.example.sys.callcamera;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity {
    Button b1,b2,b3,b4,b5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        b1=findViewById( R.id.call );
        b2=findViewById( R.id.camera );
        b3=findViewById( R.id.message );
        b4=findViewById( R.id.email );
        b5=findViewById( R.id.gallery );
        b1.setOnClickListener( new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if(checkPermission( )){
                    Intent i=new Intent( Intent.ACTION_DIAL );
                    i.setData( Uri.parse( "tel:"+b1.getText().toString().trim() ));
                    startActivity( i);
                    }
                    else
                {
                    requestPermission( );
                    Toast.makeText(MainActivity.this, "not given", Toast.LENGTH_SHORT).show();
                }
            }
        } );
        b2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((checkPermission())){
                    Intent i=new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
                    startActivityForResult(i,200  );
                }
                else {
                    requestPermission( );
                    Toast.makeText(MainActivity.this, "not given", Toast.LENGTH_SHORT).show();
                }
            }
        } );

        b3.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent( (Intent.ACTION_SENDTO) );
                i.setData(Uri.parse("sms:"+b3.getText().toString().trim()));
               i.putExtra("sms_body", " ");
                startActivity(i);
            }
        } );

        b4.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] TO={b4.getText().toString().trim()};
                Intent emailIntent=new Intent(Intent.ACTION_SEND);

                emailIntent.setData(Uri.parse("mailto:asifmohammad153@gmail.com"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL,TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,"reminder");
                emailIntent.putExtra(Intent.EXTRA_TEXT,"HELLO");
                emailIntent.setPackage("com.google.android.gm");
                startActivity(emailIntent);
            }
        } );
        b5.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,300);
            }

            }
         );

    }

    private boolean checkPermission() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission( MainActivity.this,CALL_PHONE );
        int secondPermissionResult = ContextCompat.checkSelfPermission(MainActivity.this,CAMERA);
        if (FirstPermissionResult== PackageManager.PERMISSION_GRANTED&&secondPermissionResult==PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }
    private void requestPermission(){
        ActivityCompat.requestPermissions( MainActivity.this,new String[]
                {Manifest.permission.CALL_PHONE,Manifest.permission.CAMERA},100 );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

            switch (requestCode){
                case 100:
                    if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED
                            &&grantResults[1]==PackageManager.PERMISSION_GRANTED&&grantResults[2]==PackageManager.PERMISSION_GRANTED)
                        Log.e( "value","Permission Granted,Now you can use local Drive" );
                    else
                        Log.e( "value","Permissions Denied,You cannot use local Drive" );
                    break;

            }
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if(requestCode==200)
        {
            Bitmap bitmap=(Bitmap)data.getExtras().get( "data" );
            ImageView imageView=(ImageView)findViewById( R.id.image );
            imageView.setImageBitmap( bitmap );
        }
        else if (requestCode == 300 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.image1);
            imageView.setImageBitmap( BitmapFactory.decodeFile(picturePath));

        }
    }
}
