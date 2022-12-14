package com.example.kirjasovellus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;

import com.example.kirjasovellus.database.Book;
import com.example.kirjasovellus.database.BookDao;
import com.example.kirjasovellus.database.BookDatabase;
import com.example.kirjasovellus.database.Day;
import com.example.kirjasovellus.database.DayDao;
import com.example.kirjasovellus.database.Genre;
import com.example.kirjasovellus.database.GenreDao;
import com.example.kirjasovellus.database.UserSettings;
import com.example.kirjasovellus.database.UserSettingsDao;
import com.example.kirjasovellus.tabBooks.BookDetailsFragment;
import com.example.kirjasovellus.tabToday.TodayFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Sovelluksen MainActivity. Alustaa tietokannan, FragmentManagerin ja lataus-ikonin.
 * Huolehtii näytön kääntämisestä ja testidatan generoinnista.
 */
public class MainActivity extends AppCompatActivity {

    public static BookDatabase bookDatabase;        // Tietokanta sovelluksen datan käsittelyyn ja säilömiseen.
    public static FragmentManager fragmentManager;  // FragmentManager, jolla vaihdetaan sovelluksessa näkyviä fragmentteja

    private static ImageView loadingIcon;
    private static ObjectAnimator loadingAnimation;

    public static Fragment currentFragment;


    /**
     * Rakentaa ja alustaa tietokannan, Daot ja lataus-ikonin
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Alustetaan tietokanta
        bookDatabase = Room.databaseBuilder(
                getApplicationContext(),
                BookDatabase.class,
                "bookDatabase").allowMainThreadQueries().build();
        BookDao bookDao = bookDatabase.bookDao();
        GenreDao genreDao = bookDatabase.genreDao();
        DayDao dayDao = bookDatabase.dayDao();
        UserSettingsDao usDao = bookDatabase.userSettingsDao();

        // Täyttää tietokantaan 0h dataa viimeiselle 28 päivälle, joilla ei ole ennestään dataa.
        // Tämä, jotta data-välilehden kuvaaja näyttää datan oikein.
        initializeDatabase(bookDao, genreDao, dayDao);

        // Annetaan FragmentManager julkiseksi
        fragmentManager = getSupportFragmentManager();

        // Lataus-ikoni ja sen animaatio
        loadingIcon = findViewById(R.id.ivLoadingIcon);
        loadingIcon.setBackgroundResource(R.drawable.loadingiconframe1);
        loadingIcon.setVisibility(View.GONE);

        loadingAnimation = ObjectAnimator.ofFloat(loadingIcon, "rotation", 0, 360);
        loadingAnimation.setDuration(1000);
        loadingAnimation.setRepeatCount(ObjectAnimator.INFINITE);
        loadingAnimation.setRepeatMode(ObjectAnimator.RESTART);

        // Palauttaa näkyviin fragmentin, joka oli näkyvissä näyttöä käännettäessä.
        updateRotation(currentFragment);
    }

    /**
     * Metodi näkymän säilyttämiseen näyttöä käännettäessä.
     * @param currentFragment Fragment, joka oli esillä näyttöä käännettäessä
     */
    private void updateRotation(Fragment currentFragment) {
        if (currentFragment == null) currentFragment = new TodayFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.contentContainer, currentFragment.getClass(), null)
                .addToBackStack("back")
                .commit();
    }

    /** Näyttää lataus-ikonin ja aloittaa animaation
     */
    public static void startLoading() {
        loadingIcon.setVisibility(View.VISIBLE);
        loadingAnimation.start();
    }

    /** Piilottaa lataus-ikonin ja pysäyttää animaation
     */
    public static void stopLoading() {
        loadingIcon.setVisibility(View.GONE);
        loadingAnimation.end();
    }

    /** Generoi viimeisen 28 päivän tyhjille päiville 0 tuntia
     *
     * @param bookDao bookDatabasen Dao kirjoille
     * @param genreDao bookDatabasen Dao genreille
     * @param dayDao bookDatabasen Dao päiville
     */
    private static void initializeDatabase(BookDao bookDao, GenreDao genreDao, DayDao dayDao) {
        for (int i = 0; i < 28; i++) {
            Date rawDate = Calendar.getInstance().getTime();
            Date d = new Date();
            d.setTime(0);
            d.setYear(rawDate.getYear());
            d.setMonth(rawDate.getMonth());
            d.setDate(rawDate.getDate());

            Date h24 = new Date();
            h24.setTime(0);
            h24.setHours(i * 24);
            d.setTime(d.getTime() - h24.getTime());
            d.setHours(6);

            Day day = dayDao.getDayOnDate(d);
            if (day == null) {
                day = new Day();
                day.date = d;
                day.hours = 0;
                dayDao.insertAll(day);
            }
        }
    }

    /**
     * Poistaa kaiken syötetyn datan tietokannasta. Kieliasetus säilyy
     */
    public static void nukeAllData(){
        BookDao bookDao = MainActivity.bookDatabase.bookDao();
        GenreDao genreDao = MainActivity.bookDatabase.genreDao();
        DayDao dayDao = MainActivity.bookDatabase.dayDao();

        bookDao.nukeTable();
        genreDao.nukeTable();
        dayDao.nukeTable();

        initializeDatabase(bookDao, genreDao, dayDao);
    }

    /**
     * Generoi ennaltamääritettyä ja satunnaista testidataa tietokantaan
     */
    public static void generateTestData() {
        BookDao bookDao = MainActivity.bookDatabase.bookDao();
        GenreDao genreDao = MainActivity.bookDatabase.genreDao();
        DayDao dayDao = MainActivity.bookDatabase.dayDao();

        Genre history = new Genre();
        history.genreId = 0;
        history.name = "History";
        history.symbol = "📜";

        Genre culture = new Genre();
        culture.genreId = 0;
        culture.name = "Culture";
        culture.symbol = "🎉";

        Genre math = new Genre();
        math.genreId = 0;
        math.name = "Mathematics";
        math.symbol = "🗿";

        Genre science = new Genre();
        science.genreId = 0;
        science.name = "Science";
        science.symbol = "🔬";

        Genre fantasy = new Genre();
        fantasy.genreId = 0;
        fantasy.name = "Fantasy";
        fantasy.symbol = "🧙";

        Genre thriller = new Genre();
        thriller.genreId = 0;
        thriller.name = "Thriller";
        thriller.symbol = "☎";

        Genre detective = new Genre();
        detective.genreId = 0;
        detective.name = "Detective";
        detective.symbol = "🔎";

        Genre romance = new Genre();
        romance.genreId = 0;
        romance.name = "Romance";
        romance.symbol = "\uD83D\uDC9E";

        Genre music = new Genre();
        music.genreId = 0;
        music.name = "Music";
        music.symbol = "🎵";

        Genre nature = new Genre();
        nature.genreId = 0;
        nature.name = "Nature";
        nature.symbol = "🌳";

        Genre humanity = new Genre();
        humanity.genreId = 0;
        humanity.name = "Humanity";
        humanity.symbol = "\uD83E\uDDCD";

        Genre programming = new Genre();
        programming.genreId = 0;
        programming.name = "Programming";
        programming.symbol = "💻";

        Genre space = new Genre();
        space.genreId = 0;
        space.name = "Space";
        space.symbol = "🚀";

        Genre crafts = new Genre();
        crafts.genreId = 0;
        crafts.name = "Crafts";
        crafts.symbol = "🛠";

        genreDao.insertAll(history, culture, math, science, fantasy, thriller, detective, romance, music, nature, humanity, programming, space, crafts);

        history = genreDao.getGenresOnName("History")[0];
        math = genreDao.getGenresOnName("Mathematics")[0];
        science = genreDao.getGenresOnName("Science")[0];
        fantasy = genreDao.getGenresOnName("Fantasy")[0];
        crafts = genreDao.getGenresOnName("Crafts")[0];
        space = genreDao.getGenresOnName("Space")[0];
        programming = genreDao.getGenresOnName("Programming")[0];
        humanity = genreDao.getGenresOnName("Humanity")[0];
        nature = genreDao.getGenresOnName("Nature")[0];
        thriller = genreDao.getGenresOnName("Thriller")[0];
        detective = genreDao.getGenresOnName("Detective")[0];
        romance = genreDao.getGenresOnName("Romance")[0];
        culture = genreDao.getGenresOnName("Culture")[0];

        Book ihminenJaluonto = new Book();
        ihminenJaluonto.BookId = 0;
        ihminenJaluonto.title = "Ihminen ja luonto";
        ihminenJaluonto.pageCount = 1734;
        ihminenJaluonto.finished = false;
        ihminenJaluonto.finishDate = null;
        ihminenJaluonto.genreIds = new int[]{humanity.genreId, nature.genreId};
        ihminenJaluonto.notes = "- Sivulla 134 on mielenkiintoinen kohta Suomen talvisodasta \n- Sivulla 1572 Alkaa hyvä luku luonnon vaikutuksesta ihmisyyteen";

        Book android101 = new Book();
        android101.BookId = 0;
        android101.title = "Android ohjelmointi 101";
        android101.pageCount = 893;
        android101.finished = true;
        android101.finishDate = Calendar.getInstance().getTime();
        android101.genreIds = new int[]{science.genreId, programming.genreId};

        Book huimaJannitysKertomus = new Book();
        huimaJannitysKertomus.BookId = 0;
        huimaJannitysKertomus.title = "Huima jännityskertomus";
        huimaJannitysKertomus.pageCount = 152;
        huimaJannitysKertomus.finished = false;
        huimaJannitysKertomus.finishDate = null;
        huimaJannitysKertomus.genreIds = new int[]{thriller.genreId, detective.genreId, romance.genreId};

        Book avaruudenMysteerit = new Book();
        avaruudenMysteerit.BookId = 0;
        avaruudenMysteerit.title = "Avaruuden mysteerit";
        avaruudenMysteerit.pageCount = 210;
        avaruudenMysteerit.finished = false;
        avaruudenMysteerit.finishDate = null;
        avaruudenMysteerit.genreIds = new int[]{science.genreId, space.genreId};

        Book lotr = new Book();
        lotr.BookId = 0;
        lotr.title = "Taru Sormusten Herrasta";
        lotr.pageCount = 768;
        lotr.finished = true;
        lotr.finishDate = Calendar.getInstance().getTime();
        lotr.genreIds = new int[]{fantasy.genreId};

        Book puusepanKasikirja = new Book();
        puusepanKasikirja.BookId = 0;
        puusepanKasikirja.title = "Puusepän käsikirja";
        puusepanKasikirja.pageCount = 210;
        puusepanKasikirja.finished = false;
        puusepanKasikirja.finishDate = null;
        puusepanKasikirja.genreIds = new int[]{crafts.genreId};

        Book encyclopedia = new Book();
        encyclopedia.BookId = 0;
        encyclopedia.title = "Encyclopedia of knowledge";
        encyclopedia.pageCount = 2442;
        encyclopedia.finished = true;
        encyclopedia.finishDate = Calendar.getInstance().getTime();
        encyclopedia.genreIds = new int[]{history.genreId, science.genreId};

        Book algo = new Book();
        algo.BookId = 0;
        algo.title = "Algorithms and you";
        algo.pageCount = 210;
        algo.finished = false;
        algo.finishDate = null;
        algo.genreIds = new int[]{math.genreId, programming.genreId};

        Book kirjaKirjojenHistoriasta = new Book();
        kirjaKirjojenHistoriasta.BookId = 0;
        kirjaKirjojenHistoriasta.title = "Kirja kirjojen historiasta";
        kirjaKirjojenHistoriasta.pageCount = 2000;
        kirjaKirjojenHistoriasta.finished = true;
        kirjaKirjojenHistoriasta.finishDate = Calendar.getInstance().getTime();
        kirjaKirjojenHistoriasta.genreIds = new int[]{culture.genreId, history.genreId};

        Book algo2 = new Book();
        algo2.BookId = 0;
        algo2.title = "Algorithms and me?";
        algo2.pageCount = 210;
        algo2.finished = true;
        algo2.finishDate = Calendar.getInstance().getTime();
        algo2.genreIds = new int[]{math.genreId, programming.genreId};

        Book puutarha = new Book();
        puutarha.BookId = 0;
        puutarha.title = "Puutarhanhoidon perusteet";
        puutarha.pageCount = 156;
        puutarha.finished = false;
        puutarha.finishDate = null;
        puutarha.genreIds = new int[]{nature.genreId, crafts.genreId};

        bookDao.insertAll(ihminenJaluonto, android101, huimaJannitysKertomus, avaruudenMysteerit, lotr, puusepanKasikirja, algo, encyclopedia, kirjaKirjojenHistoriasta, algo2, puutarha);

        for (int i = 0; i < 40; i++) {

            Date rawDate = Calendar.getInstance().getTime();
            Date d = new Date();
            d.setTime(0);
            d.setYear(rawDate.getYear());
            d.setMonth(rawDate.getMonth());
            d.setDate(rawDate.getDate());

            Date h24 = new Date();
            h24.setTime(0);
            h24.setHours(i*24);
            d.setTime(d.getTime() - h24.getTime());

            if (i * Math.random() > 1.00 * i/5) {
                Day day = new Day();
                day.hours = Math.random() * 3.5;
                day.date = d;
                dayDao.insertAll(day);
            }
        }

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 500; i++) {
                    Book b = new Book();
                    b.BookId = 0;
                    b.genreIds = new int[0];
                    b.finished = false;
                    b.finishDate = null;
                    b.title = "Kirja " + (i + 1);
                    b.notes = b.title + " on hyvä kirja";

                    bookDao.insertAll(b);
                }
            }
        });
        t.start();


        initializeDatabase(bookDao, genreDao, dayDao);
    }
}