package com.sunlightlabs.qr;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainUpload extends Activity {
    private static final int PICK_PHOTO = 1;
    
	private EditText message;
	private ImageView photo;
	private Button pickPhoto, print;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        setupControls();
    }
    
    public void setupControls() {
    	message = (EditText) findViewById(R.id.message);
    	pickPhoto = (Button) findViewById(R.id.pick_photo);
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
    }
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case PICK_PHOTO:
			if (resultCode == RESULT_OK) {
				Uri selectedImage = data.getData();
	            String[] filePathColumn = {MediaStore.Images.Media.DATA};

	            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
	            cursor.moveToFirst();

	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            String filePath = cursor.getString(columnIndex);
	            cursor.close();
	            
	            Bitmap chosenPhoto = BitmapFactory.decodeFile(filePath);
	            photo.setVisibility(View.VISIBLE);
	            photo.setImageBitmap(chosenPhoto);
			}
			break;
		}
    }
    
}