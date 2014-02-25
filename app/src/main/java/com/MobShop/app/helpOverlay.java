package com.MobShop.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

public class HelpOverlay extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_overlay);


        View mainView = findViewById(R.id.imageView);
        Bitmap bd = prepareOriginalScreen(MainActivity.originalScreen);

        Drawable d = new BitmapDrawable(getResources(), bd);

        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            mainView.setBackgroundDrawable(d);
        } else {
            mainView.setBackgroundDrawable(d); //TODO change
        }
    }

    private Bitmap prepareOriginalScreen(Bitmap originalScreen) {
        int statusHeight = getStatusBarHeight();
        return Bitmap.createBitmap(originalScreen, 0, statusHeight, originalScreen.getWidth(), originalScreen.getHeight() - statusHeight);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.help_overlay, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        finish();
        return super.dispatchTouchEvent(ev);
    }

}
