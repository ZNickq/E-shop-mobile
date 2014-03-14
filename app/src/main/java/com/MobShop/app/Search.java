package com.MobShop.app;


import android.app.ListActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Created by haplea on 3/14/14.
 */
public class Search extends ListActivity {
    private EditText et;
    private ListView lv;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.menu.main);
        et = (EditText) findViewById(R.id.action_search);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                //textlength = et.getText().length();
                //array_sort.clear();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

}
