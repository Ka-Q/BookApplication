package com.example.kirjasovellus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kirjasovellus.database.Book;
import com.example.kirjasovellus.database.BookDao;
import com.example.kirjasovellus.database.BookDatabase;
import com.example.kirjasovellus.database.Day;
import com.example.kirjasovellus.database.DayDao;
import com.example.kirjasovellus.database.Genre;
import com.example.kirjasovellus.database.GenreDao;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static BookDatabase bookDatabase;
    public static FragmentManager fragmentManager;

    private static ImageView loadingIcon;
    private static ObjectAnimator loadingAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create database for books
        bookDatabase = Room.databaseBuilder(
                getApplicationContext(),
                BookDatabase.class,
                "bookDatabase").allowMainThreadQueries().build();
        BookDao bookDao = bookDatabase.bookDao();
        GenreDao genreDao = bookDatabase.genreDao();
        DayDao dayDao = bookDatabase.dayDao();


        //generateTestData(bookDao, genreDao, dayDao);
        initializeDatabase(bookDao, genreDao, dayDao);

        // Set a global FragmentManager
        fragmentManager = getSupportFragmentManager();

        // Loading icon
        loadingIcon = findViewById(R.id.ivLoadingIcon);
        loadingIcon.setBackgroundResource(R.drawable.loadingiconframe1);
        loadingIcon.setVisibility(View.GONE);

        loadingAnimation = ObjectAnimator.ofFloat(loadingIcon, "rotation", 0, 360);
        loadingAnimation.setDuration(1000);
        loadingAnimation.setRepeatCount(ObjectAnimator.INFINITE);
        loadingAnimation.setRepeatMode(ObjectAnimator.RESTART);
    }

    public static void startLoading() {
        loadingIcon.setVisibility(View.VISIBLE);
        loadingAnimation.start();
    }

    public static void stopLoading() {
        loadingIcon.setVisibility(View.GONE);
        loadingAnimation.end();
    }

    private static void initializeDatabase(BookDao bookDao, GenreDao genreDao, DayDao dayDao) {
        // Generoi viimeisen 28 päivän tyhjille päiville 0 tuntia
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

            Day day = dayDao.getDayOnDate(d);
            if (day == null) {
                day = new Day();
                day.date = d;
                day.hours = 0;
                dayDao.insertAll(day);
            }
        }
    }

    public static void nukeAllData(){
        BookDao bookDao = MainActivity.bookDatabase.bookDao();
        GenreDao genreDao = MainActivity.bookDatabase.genreDao();
        DayDao dayDao = MainActivity.bookDatabase.dayDao();

        bookDao.nukeTable();
        genreDao.nukeTable();
        dayDao.nukeTable();

        initializeDatabase(bookDao, genreDao, dayDao);
    }

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

        Genre sci = new Genre();
        sci.genreId = 0;
        sci.name = "Science";
        sci.symbol = "🔬";

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
        romance.symbol = "❤";

        Genre music = new Genre();
        music.genreId = 0;
        music.name = "Music";
        music.symbol = "🎵";

        genreDao.insertAll(history, culture, math, sci, fantasy, thriller, detective, romance, music);

        history = genreDao.getGenresOnName("History")[0];
        culture = genreDao.getGenresOnName("Culture")[0];
        math = genreDao.getGenresOnName("Mathematics")[0];
        sci = genreDao.getGenresOnName("Science")[0];
        fantasy = genreDao.getGenresOnName("Fantasy")[0];

        System.out.println(math.genreId);

        Book historyForNewbies = new Book();
        historyForNewbies.BookId = 0;
        historyForNewbies.title = "History for newbies";
        historyForNewbies.pageCount = 1734;
        historyForNewbies.finished = false;
        historyForNewbies.finishDate = null;
        historyForNewbies.genreIds = new int[]{history.genreId,culture.genreId};
        historyForNewbies.notes = "- Sivulla 134 on mielenkiintoinen kohta Suomen talvisodasta \n- Sivulla 1572 Alkaa hyvä luku mannerlaattojen historiasta";

        System.out.println(history.genreId);

        Book historyOfEarth = new Book();
        historyOfEarth.BookId = 0;
        historyOfEarth.title = "History of Earth";
        historyOfEarth.pageCount = 893;
        historyOfEarth.finished = true;
        historyOfEarth.finishDate = Calendar.getInstance().getTime();
        historyOfEarth.genreIds = new int[]{history.genreId};

        Book mathIsFun = new Book();
        mathIsFun.BookId = 0;
        mathIsFun.title = "Math Is Fun!!";
        mathIsFun.pageCount = 152;
        mathIsFun.finished = false;
        mathIsFun.finishDate = null;
        mathIsFun.genreIds = new int[]{math.genreId, sci.genreId};

        Book encyclopedia = new Book();
        encyclopedia.BookId = 0;
        encyclopedia.title = "Encyclopedia of knowledge";
        encyclopedia.pageCount = 2442;
        encyclopedia.finished = true;
        encyclopedia.finishDate = Calendar.getInstance().getTime();
        encyclopedia.genreIds = new int[]{history.genreId, sci.genreId};

        Book lotr = new Book();
        lotr.BookId = 0;
        lotr.title = "Lord Of The Rings";
        lotr.pageCount = 768;
        lotr.finished = true;
        lotr.finishDate = Calendar.getInstance().getTime();
        lotr.genreIds = new int[]{fantasy.genreId};

        Book algo = new Book();
        algo.BookId = 0;
        algo.title = "Algorithms and you";
        algo.pageCount = 210;
        algo.finished = false;
        algo.finishDate = null;
        algo.genreIds = new int[]{math.genreId};

        Book algo2 = new Book();
        algo2.BookId = 0;
        algo2.title = "Algorithms and you but with a long title ";
        algo2.pageCount = 210;
        algo2.finished = false;
        algo2.finishDate = null;
        algo2.genreIds = new int[]{math.genreId};

        Book algo3 = new Book();
        algo3.BookId = 0;
        algo3.title = "Algorithms and you but with a longest title am I right? HAHA XDDDDDDDDDDDDD";
        algo3.pageCount = 210;
        algo3.finished = false;
        algo3.finishDate = null;
        algo3.genreIds = new int[]{math.genreId};

        Book algo4 = new Book();
        algo4.BookId = 0;
        algo4.title = "Algorithms and you but with a longest title am I right? HAHA XDDDDDDDDDDDDD";
        algo4.pageCount = 210;
        algo4.finished = false;
        algo4.finishDate = null;
        algo4.genreIds = new int[]{math.genreId};

        bookDao.insertAll(historyForNewbies, historyOfEarth, mathIsFun, encyclopedia, lotr, algo, algo2, algo3, algo4);

        for (int i = 0; i < 100; i++) {
            Book b = new Book();
            b.title = "Kirja " + i;
            b.pageCount = (int)(i * Math.random() * 115);
            b.BookId = 0;
            b.finished = false;
            b.finishDate = null;
            b.genreIds = new int[]{math.genreId};
            bookDao.insertAll(b);
        }

        // Generoi eiliselle 3h

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

        initializeDatabase(bookDao, genreDao, dayDao);

    }
}