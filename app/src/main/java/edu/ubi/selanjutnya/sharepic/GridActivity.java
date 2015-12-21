package edu.ubi.selanjutnya.sharepic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import edu.ubi.selanjutnya.sharepic.model.ImageAdapter;

/**
 * Created by Rifqi on 12/21/2015.
 */
public class GridActivity extends Activity {

    GridView gridView = null;
    ImageAdapter imageAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        imageAdapter = new ImageAdapter(GridActivity.this, ImageAdapter.GRID);

        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(imageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             // TODO open image?
            }
        });
    }
}
