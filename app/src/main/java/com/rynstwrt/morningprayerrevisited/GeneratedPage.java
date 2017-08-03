package com.rynstwrt.morningprayerrevisited;

import android.os.Bundle;
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
import java.util.List;
import java.util.Random;

public class GeneratedPage extends AppCompatActivity{

    ChristianEvents cevents;

    Document doc;

    TextView mainText;
    boolean isPriest, isJubilate;
    int collectSpinnerChoice;
    Random ran;

    String text = "";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_generated, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.action_audio: //TODO: takes two presses on initial play?
                if(item.getTitle() == "Read Aloud") {
                    item.setTitle("Stop Reading");
                    item.setIcon(R.drawable.ic_pause_black_24dp);
                    //play audio
                } else {
                    item.setTitle("Read Aloud");
                    item.setIcon(R.drawable.ic_play_arrow_black_24dp);
                    //pause it
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

        genBig(R.string.opensent); //TODO: remove br(); calls.
        br();br();

        if (cevents.inAdvent()) {
            pickTextWithVerse(R.array.openingadvent, R.array.openingadvent_chapters);
        } else if (cevents.isChristmas()) {
            pickTextWithVerse(R.array.openingchristmas, R.array.openingchristmas_chapters);
        } else if (cevents.isEpiphany()) {
            pickTextWithVerse(R.array.openingepiphany, R.array.openingepiphany_chapters);
        } else if (cevents.inLent()) {
            pickTextWithVerse(R.array.openinglent, R.array.openinglent_chapters);
        } else if (cevents.inHolyWeek()) {
            pickTextWithVerse(R.array.openingholyweek, R.array.openingholyweek_chapters);
        } else if (cevents.inEasterSeasonIncludingAscensionDay()) {
            gen(R.string.openingeasterasc_pt1);
            genItal(R.string.openingeasterasc_pt2);
            pickTextWithVerse(R.array.openingeasterasc, R.array.openingeasterasc_chapters);
        } else if (cevents.isTrinitySunday()) {
            gen(R.string.openingtrinity);
            br();
            genItal(R.string.revelation4_8);
        } else if (cevents.isAllSaints()) {
            pickTextWithVerse(R.array.openingsaint, R.array.openingsaint_chapters);
        } else {
            pickTextWithVerse(R.array.openinganytime, R.array.openinganytime_chapters);
        }

        br();br();br();
        if (isPriest) {
            genBig(R.string.confofsin);
            br();br();
            genItal(R.string.officiantsays);
            br();br();
            pickText(R.array.confessionofsin_pt1_priest);
            br();br();
            genItal(R.string.silence);
            br();br();
            genItal(R.string.togetherkneel);
            br();br();
            gen(R.string.confessionofsin_pt2_priest);
            br();br();
            genItal(R.string.prieststand);
            br();br();
            gen(R.string.confessionofsin_pt3_priest);
        } else {
            genBig(R.string.confofsin);
            br();br();
            genItal(R.string.officiantsays);
            br();br();
            pickText(R.array.confessionofsin_pt1_other);
            br();br();
            genItal(R.string.silence);
            br();br();
            genItal(R.string.togetherkneel);
            br();br();
            gen(R.string.confessionofsin_pt2_other);
            br();br();
            genItal(R.string.remainkneeling);
            br();br();
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
            pickText(R.array.antiphon_otherdays);
        }

        br();br();br();


        //psalms


        List<Object> bodyList = Arrays.asList(doc.select("h2:containsOwn(Morning Psalms) ~ *").toArray());
        bodyList = bodyList.subList(0, bodyList.indexOf(doc.select("h2:containsOwn(Evening Psalms)").first()));

        List<List<String>> psalms = new ArrayList<>();

        //get bodylist into string array w/ only usable lines
        List<String> stringList = new ArrayList<>();
        for (Object o : bodyList) {
            Element e = (Element) o;
            Node ns = e.nextSibling();
            if (ns != null && !(ns.toString().trim().isEmpty()) && !ns.toString().contains("<sup>")) {
                String s = ns.toString().trim();
                s = s.replaceAll("&nbsp;", "");
                s = s.replaceAll("</?\\w+>", "");

                stringList.add(s);
            }
        }

        //split string array into each psalm
        int start = 0;
        int end = 0;

        for (String s : stringList) {
            System.out.println(s);
            if (s.matches("Psalm \\d+.+") || s.matches("Evening Psalms")) {
                start = end;
                end = stringList.indexOf(s);
                psalms.add(stringList.subList(start, end));
            }
        }

        for (List<String> ls : psalms) {
            for (String s : ls) {
                //System.out.println(s);
            }
        }




        /*
        doc.select("p > em").remove();
        doc.select("sup").remove();

        String psalmBody = doc.body().toString();
        String psalmArea = psalmBody.substring(psalmBody.indexOf("<h2>Morning Psalms</h2>"), psalmBody.indexOf("<h2>Evening Psalms</h2>"));

        //replace nbsp and br
        psalmArea = psalmArea.replace("&nbsp;", "");

        //split psalms into own section
        String[] psalms = psalmArea.split("<p></p>");

        //split psalm lines into own section
        List<List<String>> psalmLines;
        for (String s : psalms) {

        }


        for (String psalm : psalms) {
            System.out.println(psalm + "and");
        }
        */


        br();br();
        gen(R.string.endofpsalmsappointed);

        br();br();br();


         /*

        Element lastItem = doc.select("*:contains(The Morning Psalms) ~ *:contains(The Lessons)").first();
        List<List<Object>> lessons = new ArrayList<>();
        List<String> lessonTitles = new ArrayList<>();
        Elements afterStrongsLesson = doc.select("#Lessons3 ~ h1,#Lessons3 ~ strong,#Lessons3 ~ b");


        for (int i = 0; i < afterStrongsLesson.size(); i++) {
            if (bodyList.indexOf(afterStrongsLesson.get(i)) < bodyList.indexOf(lastItem) && afterStrongsLesson.get(i).text().matches("The \\w+ Testament Lesson|The Gospel")) {
                List<Object> currentLesson;

                try {
                    if (bodyList.indexOf(afterStrongsLesson.get(i + 1)) < bodyList.indexOf(lastItem)) {
                        currentLesson = bodyList.subList(bodyList.indexOf(afterStrongsLesson.get(i)) + 1, bodyList.indexOf(afterStrongsLesson.get(i + 1)));
                    } else {
                        currentLesson = bodyList.subList(bodyList.indexOf(afterStrongsLesson.get(i)) + 1, bodyList.indexOf(lastItem));
                    }
                }catch(IndexOutOfBoundsException e) {
                    currentLesson = bodyList.subList(bodyList.indexOf(afterStrongsLesson.get(i)) + 1, bodyList.indexOf(lastItem));
                }
                lessons.add(currentLesson);
            }
        }

        List<List<Object>> selectedLessons = new ArrayList<>();

        for (List<Object> lo : selectedLessons) {
            Element firstItem = (Element) lo.get(1);
            lessonTitles.add(firstItem.text());
            List<Object> originalLo = lo;
            lo = lo.subList(2, lo.size());

            genBig(firstItem.text());
            br();br();

            for (Object o : lo) {
                Element e = (Element) o;
                e.text(e.text().replaceAll("\\d+\\s?([A-Za-z]+)", "$1"));
                gen(e.text());
                br();br();
            }
            pickText(R.array.canticle);
            //if not last lesson, br() x3.
            if (selectedLessons.indexOf(originalLo) != selectedLessons.size() - 1) {
                br();br();br();
            }
        }*/


        br();br();br();
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
        pickText(R.array.collect_endings);

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

    private void pickTextWithVerse(int anum, int bnum) {
        String[] a = idToArray(anum);
        String[] b = idToArray(bnum);
        int r = ran.nextInt(a.length);
        gen(a[r]);
        genSmall(b[r]);
    }

    private void pickText(int anum) {
        String[] a = idToArray(anum);
        int r = ran.nextInt(a.length);
        gen(a[r]);
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