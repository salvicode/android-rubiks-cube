package com.cjsbox.rubikscube;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class MainMenuActivity extends FragmentActivity {
	public static int size;
	private AdView adView;
	final String ADMOB_ID = "a153356fcfcb935";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		size = 3;
		setContentView(R.layout.main_menu_layout);
		ImageButton newGameBtn = (ImageButton)findViewById(R.id.new_game_btn);
		ImageButton continueGameBtn = (ImageButton)findViewById(R.id.continue_btn);
		ImageButton cubeSize = (ImageButton)findViewById(R.id.cube_size_btn);
		ImageButton moreGamesBtn = (ImageButton)findViewById(R.id.more_games_btn);
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
		moreGamesBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.outreteam.wooxel"));
			    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			    startActivity(i);
			}
		});
		
		LayoutParams adParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		adView = new AdView(this);
		adView.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				super.onAdLoaded();

				MainMenuActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
					//	adView.setBackgroundColor(Color.BLACK);
					}
				});
			}
		});
		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId(ADMOB_ID);
		adView.setLayoutParams(new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT,
				Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM));
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("6195E9DFF3F2DC1E7A36D28C120C4766").build();
		adView.loadAd(adRequest);
		adView.setVisibility(View.VISIBLE);
		//adView.setBackgroundColor(Color.BLACK);
		LinearLayout layout = (LinearLayout)findViewById(R.id.linear_layout);
		layout.addView(adView, adParams);
//		ScrollView scrollView = (ScrollView)findViewById(R.id.scrollView1);
//		scrollView.addView(adView, adParams);
	}
	
	  @Override
	  public void onResume() {
	    super.onResume();
	    if (adView != null) {
	      adView.resume();
	    }
	  }

	  @Override
	  public void onPause() {
	    if (adView != null) {
	      adView.pause();
	    }
	    super.onPause();
	  }

	  /** Called before the activity is destroyed. */
	  @Override
	  public void onDestroy() {
	    // Destroy the AdView.
	    if (adView != null) {
	      adView.destroy();
	    }
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
