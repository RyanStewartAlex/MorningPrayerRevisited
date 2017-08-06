package com.rynstwrt.morningprayerrevisited;

import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class GeneratedPage extends AppCompatActivity{

    ChristianEvents cevents;

    Document doc;

    TextView mainText;
    boolean isPriest, isJubilate;
    int collectSpinnerChoice;
    Random ran;
    TextToSpeech tts;

    String text = "";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_generated, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.action_audio:
                if (item.getIcon().getConstantState().equals(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp).getConstantState())) {
                    item.setTitle("Stop Reading");
                    //item.setIcon(R.drawable.ic_pause_black_24dp);
                    item.setIcon(R.drawable.ic_stop_black_24dp);

                    //play audio
                    String[] textSections = text.split("</?br\\s?/?>");

                    String[] ts = textSections;

                    for(String s : ts) {

                        s = s.replaceAll("</?\\w+\\s?/?>", "").trim();
                        s = s.replaceAll("\\*", "");
                        s = s.replaceAll("Jubilate", "Jubilatey");
                        s = s.replaceAll("Venite", "Venitey");
                        s = s.replaceAll("(\\d+)\\s?-\\s?(\\d+)", "$1 through $2");
                        System.out.println(s);

                        tts.speak(s, TextToSpeech.QUEUE_ADD, null);


                    }

                } else {
                    item.setTitle("Read Aloud");
                    item.setIcon(R.drawable.ic_play_arrow_black_24dp);

                    //pause it
                    tts.stop();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.generated_page);

        doc = GlobalDocument.doc;

        ran = new Random();
        cevents = new ChristianEvents();
        mainText = (TextView) findViewById(R.id.mainText);

        isPriest = getIntent().getBooleanExtra("isPriest", true);
        isJubilate = getIntent().getBooleanExtra("isJubilate", true);
        collectSpinnerChoice = getIntent().getIntExtra("collectSpinner", 0);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                    tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {

                        }

                        @Override
                        public void onDone(String utteranceId) {

                        }

                        @Override
                        public void onError(String utteranceId) {

                        }
                    });
                }
            }
        });

        genBig(R.string.opensent); //TODO: remove br(); calls.
        br();br();

        if (cevents.inAdvent()) {
            List<String> cl = pickTextWithVerse(R.array.openingadvent, R.array.openingadvent_chapters);
            gen(cl.get(0));
            genSmall(cl.get(1));
        } else if (cevents.isChristmas()) {
            List<String> cl = pickTextWithVerse(R.array.openingchristmas, R.array.openingchristmas_chapters);
            gen(cl.get(0));
            genSmall(cl.get(1));
        } else if (cevents.isEpiphany()) {
            List<String> cl = pickTextWithVerse(R.array.openingepiphany, R.array.openingepiphany_chapters);
            gen(cl.get(0));
            genSmall(cl.get(1));
        } else if (cevents.inLent()) {
            List<String> cl = pickTextWithVerse(R.array.openinglent, R.array.openinglent_chapters);
            gen(cl.get(0));
            genSmall(cl.get(1));
        } else if (cevents.inHolyWeek()) {
            List<String> cl = pickTextWithVerse(R.array.openingholyweek, R.array.openingholyweek_chapters);
            gen(cl.get(0));
            genSmall(cl.get(1));
        } else if (cevents.inEasterSeasonIncludingAscensionDay()) {
            gen(R.string.openingeasterasc_pt1);
            genItal(R.string.openingeasterasc_pt2);
            List<String> cl = pickTextWithVerse(R.array.openingeasterasc, R.array.openingeasterasc_chapters);
            gen(cl.get(0));
            genSmall(cl.get(1));
        } else if (cevents.isTrinitySunday()) {
            gen(R.string.openingtrinity);
            br();
            genItal(R.string.revelation4_8);
        } else if (cevents.isAllSaints()) {
            List<String> cl = pickTextWithVerse(R.array.openingsaint, R.array.openingsaint_chapters);
            gen(cl.get(0));
            genSmall(cl.get(1));
        } else {
            List<String> cl = pickTextWithVerse(R.array.openinganytime, R.array.openinganytime_chapters);
            gen(cl.get(0));
            genSmall(cl.get(1));
        }

        br();br();br();

            genBig(R.string.confofsin);
            br();br();
            genItal(R.string.officiantsays);
            br();br();
            gen(pickText(R.array.confessionofsin_pt1));
            br();br();
            genItal(R.string.silence);
            br();br();
            genItal(R.string.togetherkneel);
            br();br();
            gen(R.string.confessionofsin_pt2);
            br();br();
            genItal(R.string.prieststand);
            br();br();
        if (isPriest) {
            gen(R.string.confessionofsin_pt3_priest);
        } else {
            gen(R.string.confessionofsin_pt3_other);
        }

        br();br();br();
        genBig(R.string.invandsalt);
        br();br();
        genItal(R.string.stand);
        br();br();
        gen(R.string.invandsalt_pt1);
        br();
        gen(R.string.invandsalt_pt2);
        br();br();
        genItal(R.string.officiantandpeople);
        br();br();
        gen((cevents.inLent()) ? R.string.invandsalt_pt3_lent : R.string.invandsalt_pt3);

        br();br();br();
        if (isJubilate) {
            genBig(R.string.jubilate_title);
            br();br();
            gen(R.string.jubilate);
            br();
            genItal(R.string.psalm100);
        } else {
            genBig(R.string.venite_title);
            br();
            br();
            gen(R.string.venite);
            br();
            genItal(R.string.psalm95_1);
        }

        br();br();
        if (cevents.isAllSaints()) {
            if (cevents.inEasterSeasonIncludingAscensionDay()) {
                gen(R.string.antiphon_allsaints_easter);
            } else {
                gen(R.string.antiphon_allsaints);
            }
        } else if (cevents.isChristmas()) { //feast of incarnation
            if (cevents.inEasterSeasonIncludingAscensionDay()) {
                gen(R.string.antiphon_incarnation_easter);
            } else {
                gen(R.string.antiphon_incarnation);
            }
        } else {
            gen(pickText(R.array.antiphon_otherdays));
        }

        br();br();br();


        //psalms
        
        List<Object> bodyList = Arrays.asList(doc.select("#theDailyPrayers > h3:eq(18) ~ *").toArray());
        bodyList = bodyList.subList(0, bodyList.indexOf(doc.select("#theDailyPrayers > h2:eq(91)").first()));

        List<List<String>> psalms = new ArrayList<>();

        List<String> stringList = new ArrayList<>();
        for (Object o : bodyList) {
            Element e = (Element) o;
            Node ns = e.nextSibling();

            if (e.toString().matches(".+Psalm \\d+.+")) {
                String s = e.toString().trim();
                s = s.replaceAll("&nbsp;", "");
                s = s.replaceAll("</?\\w+>", "");

                stringList.add(s);
            } else if (ns != null && !ns.toString().contains("<sup>")) {
                String s = ns.toString().trim();
                s = s.replaceAll("&nbsp;", "");
                s = s.replaceAll("</?\\w+>", "");

                if (!(ns.toString().trim().isEmpty())) {
                    stringList.add(s);
                }

            }
        }

        int start = 0;
        int end = 0;

        for (String s : stringList) {
            if (!s.trim().isEmpty() && (s.matches("Psalm \\d+.+") || s.matches("The Lessons"))) {
                start = end;
                end = stringList.indexOf(s);

                List<String> section = stringList.subList(start, end);
                if (!section.isEmpty()) {
                    psalms.add(section);
                }
            }
        }

        List<String> psalmTitles = new ArrayList<>();

        for (List<String> sl : psalms) {
            String title = sl.get(0);
            psalmTitles.add(title);
        }

        for (List<String> ls : psalms) {
            genBig(psalmTitles.get(psalms.indexOf(ls)));
            br();br();
            for (String s : ls) {
                if (!psalmTitles.contains(s)) {
                    gen(s);
                    br();
                    if (!s.contains("*")) {
                        br();
                    }
                }
            }
            if (psalms.indexOf(ls) != (psalms.size() - 1)) {
                br();
            }
        }

        gen(R.string.endofpsalmsappointed);

        br();br();br();



        //lessons

        /*


        List<Object> lessonArea = Arrays.asList(doc.select("").toArray());
        lessonArea = bodyList.subList(0, bodyList.indexOf(doc.select("h2:containsOwn(The Lessons)").first()));

        List<List<String>> lessons = new ArrayList<>();

        List<String> lessonStringList = new ArrayList<>();

        for (Object o : lessonArea) {
            Element e = (Element) o;
            Node ns = e.nextSibling();

            if (e.toString().matches(".+Psalm \\d+.+")) {
                String s = e.toString().trim();
                s = s.replaceAll("&nbsp;", "");
                s = s.replaceAll("</?\\w+>", "");

                lessonStringList.add(s);
            } else if (ns != null && !ns.toString().contains("<sup>")) {
                String s = ns.toString().trim();
                s = s.replaceAll("&nbsp;", "");
                s = s.replaceAll("</?\\w+>", "");

                if (!(ns.toString().trim().isEmpty())) {
                    lessonStringList.add(s);
                }

            }
        }

        int lessonStart = 0;
        int lessonEnd = 0;

        for (String s : lessonStringList) {
            if (!s.trim().isEmpty() && (s.matches("Psalm \\d+.+") || s.matches("The Lessons"))) {
                lessonStart = lessonEnd;
                lessonEnd = lessonStringList.indexOf(s);

                List<String> section = lessonStringList.subList(start, end);
                if (!section.isEmpty()) {
                    lessons.add(section);
                }
            }
        }

        List<String> lessonTitles = new ArrayList<>();

        for (List<String> sl : lessons) {
            String title = sl.get(0);
            lessonTitles.add(title);
        }

        for (List<String> ls : lessons) {
            genBig(lessonTitles.get(lessons.indexOf(ls)));
            br();br();
            for (String s : ls) {
                if (!lessonTitles.contains(s)) {
                    gen(s);
                    br();
                    if (!s.contains("*")) {
                        br();
                    }
                }
            }
            if (lessons.indexOf(ls) != (lessons.size() - 1)) {
                br();
            }
        }
        */



        genBig(R.string.creed);
        br();br();
        genItal(R.string.officiantandpeople);
        br();br();
        gen(R.string.apostlescreed);

        br();br();br();
        genBig(R.string.prayers);
        br();br();
        genItal(R.string.standorkneel);
        br();br();
        gen(R.string.theprayers_pt1);
        br();
        gen(R.string.theprayers_pt2);
        br();
        gen(R.string.theprayers_pt3);
        br();br();
        genItal(R.string.officiantandpeople);
        br();br();
        if (isPriest)
            gen(R.string.theprayers_pt4_priest);
        else
            gen(R.string.theprayers_pt4_other);

        br();br();br();
        genBig(R.string.collectday);
        br();br();
        switch (collectSpinnerChoice) {
            case 0:
                gen(R.string.collect_sundays);
                break;
            case 1:
                gen(R.string.collect_saturdays);
                break;
            case 2:
                gen(R.string.collect_fridays);
                break;
            case 3:
                gen(R.string.collect_peace);
                break;
            case 4:
                gen(R.string.collect_renewaloflife);
                break;
            case 5:
                gen(R.string.collect_grace);
                break;
            case 6:
                gen(R.string.collect_guidance);
                break;
        }
        br();br();
        gen(pickText(R.array.collect_endings));

        br();br();br();
        genBig(R.string.thanksgiving);
        br();br();
        genItal(R.string.officiantandpeople);
        br();br();
        gen(R.string.thanksgiving_pt1);
        br();br();
        gen(R.string.thanksgiving_pt2);
        br();
        genItal(R.string.thanksgiving_pt3);



        mainText.setText(Html.fromHtml(text));
    }

    private String[] idToArray(int in) {
        String[] s = getResources().getStringArray(in);
        return s;
    }

    private void br() {
        text += "<br/>";
    }

    private List<String> pickTextWithVerse(int anum, int bnum) {
        String[] a = idToArray(anum);
        String[] b = idToArray(bnum);
        int r = ran.nextInt(a.length);

        List<String> ml = new ArrayList<>();
        ml.add(a[r]);
        ml.add(b[r]);
        return ml;
    }

    private String pickText(int anum) {
        String[] a = idToArray(anum);
        int r = ran.nextInt(a.length);
        return a[r];
    }

    private void gen(String s) {
        text += s;
    }

    private void gen(int id) {
        text += getString(id);
    }

    private void genSmall(String s) {
        br();
        text += ("<small>" + s + "</small>");
    }

    private void genBig(int s) {
        String str = getString(s);
        text += ("<b>" + str + "</b>");
    }

    private void genBig(String s) {
        text += ("<b>" + s + "</b>");
    }

    private void genItal(int s) {
        text += ("<i><small>" + getString(s) + "</small></i>");
    }

}