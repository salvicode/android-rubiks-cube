package com.salvicode.rubixcube;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

public class MainMenuActivity extends FragmentActivity {
	public static int size;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		size = 3;
		setContentView(R.layout.main_menu_layout);
		ImageButton newGameBtn = (ImageButton)findViewById(R.id.new_game_btn);
		ImageButton continueGameBtn = (ImageButton)findViewById(R.id.continue_btn);
		ImageButton cubeSize = (ImageButton)findViewById(R.id.cube_size_btn);
		newGameBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startCubeActivity();
			}
		});
		continueGameBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(MainMenuActivity.this, "Coming soon...", Toast.LENGTH_SHORT).show();
			}
		});
		cubeSize.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeSize();
			}
		});
	}
	
	  @Override
	  public void onResume() {
	    super.onResume();
	  }

	  @Override
	  public void onPause() {
	    super.onPause();
	  }

	  /** Called before the activity is destroyed. */
	  @Override
	  public void onDestroy() {
	    super.onDestroy();
	  }
	
	public void changeSize()
	{
		String items[] = new String[]{"2x2x2", "3x3x3", "4x4x4", "5x5x5"};
		new AlertDialog.Builder(this)
        .setSingleChoiceItems(items, 0, null)
        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                size = ((AlertDialog)dialog).getListView().getCheckedItemPosition() + 2;
            }
        })
        .show();
	}
	
	public void startCubeActivity()
	{
		Intent intent = new Intent(this, MultyCubeGLActivity.class);
		intent.putExtra(MultyCubeGLActivity.SIZE, size);
		startActivity(intent);
	}
}
