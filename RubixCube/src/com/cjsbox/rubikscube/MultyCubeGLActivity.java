package com.cjsbox.rubikscube;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


public class MultyCubeGLActivity extends FragmentActivity {
	private static final String LOG_TAG = MultyCubeGLActivity.class.getSimpleName();
	public static final String SIZE = "size";
	private GLView glview;
	private ImageView backBtn;
	int size;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        size = getIntent().getExtras().getInt(SIZE);
        glview = (GLView)findViewById(R.id.glview);
        backBtn = (ImageView)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				glview.cancelMove();
			}
		});
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.game_menu, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mix:
			glview.mix();
			return true;
		case R.id.new2:
			glview.setCubeDim(2);
			return true;
		case R.id.new3:
			glview.setCubeDim(3);
			return true;
		case R.id.new4:
			glview.setCubeDim(4);
			return true;
		case R.id.new5:
			glview.setCubeDim(5);
			return true;
		case R.id.exit:
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}