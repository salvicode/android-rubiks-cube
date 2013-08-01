package com.cjsbox.rubikscube;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


public class MultyCubeGLActivity extends SherlockActivity {
	private static final String LOG_TAG = MultyCubeGLActivity.class.getSimpleName();
	private GLView glview;
	private ImageView backBtn;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
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
	    MenuInflater inflater = getSupportMenuInflater();
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