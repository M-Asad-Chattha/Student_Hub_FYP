package com.achba.studenthub;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.shashank.sony.fancyaboutpagelib.FancyAboutPage;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        FancyAboutPage fancyAboutPage=findViewById(R.id.fancyaboutpage);
        fancyAboutPage.setCoverTintColor(Color.BLUE);  //Optional
        fancyAboutPage.setCover(R.drawable.coverimg);
        fancyAboutPage.setName("Muhammad Asad Chattha");
        fancyAboutPage.setDescription("Google Certified Associate Android Developer | Android App, Game, Web and Software Developer.");
        fancyAboutPage.setAppIcon(R.drawable.ic_book_large_web); //Pass your app icon image
        fancyAboutPage.setAppName("Smart Student Hub");
        fancyAboutPage.setVersionNameAsAppSubTitle("1.0");
        fancyAboutPage.setAppDescription("Student Hub is an education domain app tp " +
                "Facilitate Students.\n\n" +
                "This app  " +
                "provide stuff which is helpful to students. " +
                "Provide all student needed function at one place!\n\n");
        fancyAboutPage.addEmailLink("m.asad.chatthaa@gmail.com");
        fancyAboutPage.addFacebookLink("https://www.facebook.com/shashanksinghal02");  //Add your facebook address url
        fancyAboutPage.addTwitterLink("https://twitter.com/shashank020597");
        fancyAboutPage.addLinkedinLink("https://www.linkedin.com/in/shashank-singhal-a87729b5/");
        fancyAboutPage.addGitHubLink("https://github.com/Shashank02051997");


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
