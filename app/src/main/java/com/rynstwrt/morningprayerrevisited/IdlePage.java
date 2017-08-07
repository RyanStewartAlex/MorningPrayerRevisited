package com.rynstwrt.morningprayerrevisited;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.joda.time.DateTime;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class IdlePage extends AppCompatActivity {

    DateTime date;
    Document doc;
    boolean isPriest, isJubilate;
    int collectSpinnerChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idle_page);

        //date = new DateTime();
        date = new DateTime();

        isPriest = getIntent().getBooleanExtra("isPriest", true);
        isJubilate = getIntent().getBooleanExtra("isJubilate", true);;
        collectSpinnerChoice = getIntent().getIntExtra("collectSpinner", 0);

        new JsoupAsyncTask().execute();
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        String url;

        JsoupAsyncTask() {
            url = "http://prayer.forwardmovement.org/the_daily_readings.php?d=" + date.getDayOfMonth() + "&m=" + date.getMonthOfYear() + "&y=" + date.getYear();
            System.out.println(url);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            GlobalDocument.doc = doc;

            Intent getGenActivity = new Intent(IdlePage.this, GeneratedPage.class);

            getGenActivity.putExtra("isPriest", isPriest);
            getGenActivity.putExtra("isJubilate", isJubilate);
            getGenActivity.putExtra("collectSpinner", collectSpinnerChoice);

            startActivity(getGenActivity);
            finish();
        }

    }
}
