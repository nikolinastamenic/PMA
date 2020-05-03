package com.example.myapplication.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;

public class ReportActivity extends Activity {


    ListView listView;
    String itemTitle[] = {"Tiles in kitchen are broken", "Window is broken"};
    String itemDescription[] = {"Tiles broken description", "Window broken description"};
    int images[] = {R.drawable.broken_tiles, R.drawable.broken_window};
    @Override
    protected void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);

        listView = findViewById(R.id.listViewReport);

        MyAdapter adapter = new MyAdapter(this,itemTitle, itemDescription, images);
        listView.setAdapter(adapter);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
    }

    class  MyAdapter extends ArrayAdapter<String> {

        Context context;
        String  title[];
        String description[];
        int images[];

        MyAdapter(Context c, String title [], String[] description, int[] images){
                super(c,R.layout.item, R.id.reportItemTitle, title);
                this.context = c;
                this.title = title;
                this.description = description;
                this.images = images;

         }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View item = layoutInflater.inflate(R.layout.item,parent, false);
            ImageView images1 = item.findViewById(R.id.image);
            TextView title1 = item.findViewById(R.id.reportItemTitle);
            TextView description1 = item.findViewById(R.id.reportItemDescription);


            images1.setImageResource(images[position]);
            title1.setText(title[position]);
            description1.setText(description[position]);

            return item;
        }
    }

    public void onClickNewItem(View view) {
        Intent intent = new Intent(ReportActivity.this, NewItemActivity.class);

        startActivity(intent);
    }
}
