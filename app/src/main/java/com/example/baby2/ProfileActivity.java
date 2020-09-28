package com.example.baby2;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    LinearLayout layout_view;
    LinearLayout layout_update;

    TextView nameView;
    TextView contentView;
    Button button_update;
    Button button_delete;
    Button button_connectble;
    Button button_disableble;

    EditText edit_name;
    EditText edit_content;
    Button button_req_update;

    String mac;
    String content;
    String name;
    String img_uri;

    ImageView image_update;
    ImageView image_view;
    Uri selectedImageUri=null;

    private final int GET_GALLERY_IMAGE = 200;

   Activity mcontext;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

         //   sendPicture(data.getData());
             selectedImageUri = data.getData();
             image_update.setImageURI(selectedImageUri);



        }

    }

    @Override
    protected void onResume() {
        super.onResume();


        if(PreferenceManager.getInt(getApplicationContext(),"video")==1)
        {
            Intent intent=new Intent(this,videoActivity.class);
            startActivity(intent);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        mcontext=this;
        Log.d("TTTTTTTTT","Profile Oncreate!!!!");

        image_update=findViewById(R.id.imageView3);
        image_view=findViewById(R.id.imageView);

        layout_view=findViewById(R.id.showView);
        layout_update=findViewById(R.id.updateView);
        nameView=findViewById(R.id.textView);
        contentView=findViewById(R.id.textView8);
        edit_name=findViewById(R.id.editTextTextPersonName22);
        edit_content=findViewById(R.id.editTextTextPersonName3);

        button_update=findViewById(R.id.button2);
        button_delete=findViewById(R.id.button3);
        button_connectble=findViewById(R.id.button4);
        button_disableble=findViewById(R.id.button6);

        button_req_update=findViewById(R.id.button22);

        Intent intent=getIntent();

        mac=intent.getStringExtra("mac");
        content=intent.getStringExtra("content");
        name=intent.getStringExtra("name");
        img_uri=intent.getStringExtra("uri");

        if(!img_uri.equals("")){

            image_view.setImageURI(Uri.parse(img_uri));
            image_update.setImageURI(Uri.parse(img_uri));
            selectedImageUri=Uri.parse(img_uri);
        }
        nameView.setText(name);
        contentView.setText(content);

        if(!PreferenceManager.getString(this,mac).equals("")){

            button_connectble.setVisibility(View.INVISIBLE);
            button_disableble.setVisibility(View.VISIBLE);
        } else{


            button_connectble.setVisibility(View.VISIBLE);
            button_disableble.setVisibility(View.INVISIBLE);
        };

        image_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(image_update.getVisibility()==View.VISIBLE){

                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, GET_GALLERY_IMAGE);


                }


            }
        });

        button_disableble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(button_disableble.getVisibility()==View.VISIBLE) {
                    PreferenceManager.removeKey(getApplicationContext(), mac);
                    int num=PreferenceManager.getInt(getApplicationContext(),"num");
                    PreferenceManager.setInt(getApplicationContext(),"num",num-1);
                    Intent intent = new Intent(getApplicationContext(), FADService.class);
                    intent.putExtra("mac", mac);
                    intent.putExtra("add", "f"); // 이걸 지울건가 추가할건가
                    ContextCompat.startForegroundService(getApplicationContext(), intent);


                    button_connectble.setVisibility(View.VISIBLE);
                    button_disableble.setVisibility(View.INVISIBLE);
                }
            }
        });

        button_connectble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(button_connectble.getVisibility()==View.VISIBLE) {
                    PreferenceManager.setString(getApplicationContext(), mac, "true");

                    int num=PreferenceManager.getInt(getApplicationContext(),"num");
                    PreferenceManager.setInt(getApplicationContext(),"num",num+1);

                    Intent intent = new Intent(getApplicationContext(), FADService.class);
                    intent.putExtra("mac", mac);
                    intent.putExtra("add", "t");
                    ContextCompat.startForegroundService(getApplicationContext(), intent);

                    button_connectble.setVisibility(View.INVISIBLE);
                    button_disableble.setVisibility(View.VISIBLE);

                    Toast.makeText(getApplicationContext(), name+" 블루투스가 연결되었습니다.", Toast.LENGTH_LONG).show();

                    setResult(RESULT_FIRST_USER);
                    finish();


                }
            }
        });


        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(button_delete.getVisibility()==View.VISIBLE) {



                    if (!PreferenceManager.getString(getApplicationContext(), mac).equals(""))
                        PreferenceManager.removeKey(getApplicationContext(), mac);

                    if(isServiceRunning("com.example.baby2.FADService")) {

                        Log.d("TTTTTTTTT","PROFILE:: FADSErivce is running!!!");

                        Intent intent = new Intent(getApplicationContext(), FADService.class);
                        intent.putExtra("add", "f");
                        intent.putExtra("mac", mac);
                        ContextCompat.startForegroundService(getApplicationContext(), intent);
                    }

                    // MainAcivity에서 지워야함
                    Intent intent_main = new Intent(getApplicationContext(), MainActivity.class);
                    intent_main.putExtra("mac", mac);
                    setResult(RESULT_OK, intent_main);
                    finish();
                }
            }
        });

        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(button_update.getVisibility()==View.VISIBLE) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                       mcontext.getWindow().setStatusBarColor(ContextCompat.getColor(mcontext, R.color.bluee));
                    }

                    edit_name.setText(name);
                    edit_content.setText(content);

                    layout_view.setVisibility(View.INVISIBLE);
                    layout_update.setVisibility(View.VISIBLE);
                }
            }
        });

        button_req_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(button_req_update.getVisibility()==View.VISIBLE) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("name", edit_name.getText().toString());
                    intent.putExtra("content", edit_content.getText().toString());
                    intent.putExtra("mac", mac);

                    if(selectedImageUri!=null)
                    intent.putExtra("uri",selectedImageUri.toString());
                    else
                        intent.putExtra("uri","");

                    setResult(RESULT_CANCELED, intent);
                    finish();
                }
            }
        });




    }


    @Override
    public void onBackPressed() {

        setResult(RESULT_FIRST_USER);
        finish();


    }

    public boolean isServiceRunning(String serviceName){
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo runServiceInfo : manager.getRunningServices(Integer.MAX_VALUE)){
            if(serviceName.equals(runServiceInfo.service.getClassName())){
                return true;
            }
        }
        return false;
    }


    private void sendPicture(Uri imgUri){

        String imagePath=getRealPathFromURI(imgUri);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
        image_update.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap src, float degree) {

        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }

    private String getRealPathFromURI(Uri contentUri){

        int column_index=0;
        String[] proj={MediaStore.Images.Media.DATA};
        Cursor cursor=getContentResolver().query(contentUri,proj,null,null,null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }


        return cursor.getString(column_index);

    }

}