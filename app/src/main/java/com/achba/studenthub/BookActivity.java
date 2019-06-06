package com.achba.studenthub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.thefinestartist.finestwebview.FinestWebView;

public class BookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onClickHEC(View view) {
        new FinestWebView.Builder(this)
        .show("http://digitallibrary.edu.pk");
    }

    public void onClickOpenLibrary(View view) {
        new FinestWebView.Builder(this)
                .show("https://openlibrary.org");
    }

    public void onClickStanford(View view) {
        new FinestWebView.Builder(this)
                .show("https://library.stanford.edu/science/collections/chemistry-and-chemical-engineering-collection/ebooks");
    }

    public void onClickPunjabLibrary(View view) {
        new FinestWebView.Builder(this)
                .show("https://elibrary.punjab.gov.pk");
    }
}
