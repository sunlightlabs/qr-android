package com.sunlightlabs.qr;

import java.io.File;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainUpload extends Activity {
    private static final int PICK_PHOTO = 1;
    private static final int TAKE_PHOTO = 2;
    
	private EditText message;
	private ImageView photo;
	private Button pickPhoto, takePhoto, print;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        setupControls();
    }
    
    public void setupControls() {
    	message = (EditText) findViewById(R.id.message);
    	pickPhoto = (Button) findViewById(R.id.pick_photo);
    	takePhoto = (Button) findViewById(R.id.take_photo);
    	photo = (ImageView) findViewById(R.id.photo);
    	print = (Button) findViewById(R.id.print);
    	
    	pickPhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK, 
						android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);  
				startActivityForResult(intent, PICK_PHOTO);
			}
		});
    	
    	takePhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE")
					.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(tempFile())));
				startActivityForResult(intent, TAKE_PHOTO);
			}
		});
    }
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case PICK_PHOTO:
			if (resultCode == RESULT_OK) {
				Uri selectedImage = data.getData();
	            photo.setImageBitmap(bitmapFromUri(selectedImage));
			}
			break;
		case TAKE_PHOTO:
			if (resultCode == RESULT_OK) {
				Uri takenImage;
				File file = new File(tempFile());
                try {
                	String result = android.provider.MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), null, null);
                    takenImage = Uri.parse(result);
                    
                	if (!file.delete())
                        Toast.makeText(this, "Failed to delete " + file, Toast.LENGTH_SHORT);
                    
                } catch (FileNotFoundException e) {
                	takenImage = null;
                    Toast.makeText(this, "Photo file not found!", Toast.LENGTH_SHORT);
                }
                
                if (takenImage != null)
                	photo.setImageBitmap(bitmapFromUri(takenImage));
			}
			break;
		}
    }
    
    private String tempFile() {
    	return Environment.getExternalStorageDirectory().getPath() + "/tempImage";
    }
    
    private Bitmap bitmapFromUri(Uri uri) {
    	String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        
        return BitmapFactory.decodeFile(filePath);
    }
    
}