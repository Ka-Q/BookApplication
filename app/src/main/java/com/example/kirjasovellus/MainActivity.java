package com.example.kirjasovellus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;

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


        generateTestData(bookDao, genreDao, dayDao);

        //Get a global FragmentManager
        fragmentManager = getSupportFragmentManager();
    }


    private void generateTestData(BookDao bookDao, GenreDao genreDao, DayDao dayDao) {

        bookDao.nukeTable();
        genreDao.nukeTable();
        dayDao.nukeTable();

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

        /*Day d = new Day();
        d.hours = 3;
        d.date = Calendar.getInstance().getTime();

        Day d2 = new Day();
        d2.hours = 5;
        d2.date = Calendar.getInstance().getTime();
        d2.date.setTime(d.date.getTime() + 100000);

        Day d3 = new Day();
        d3.hours = 2;
        d3.date = Calendar.getInstance().getTime();
        d3.date.setTime(d.date.getTime() + 200000);

        Day d4 = new Day();
        d4.hours = 3.5;
        d4.date = Calendar.getInstance().getTime();
        d4.date.setTime(d.date.getTime() + 300000);

        Day d5 = new Day();
        d5.hours = 4.5;
        d5.date = Calendar.getInstance().getTime();
        d5.date.setTime(d.date.getTime() + 400000);

        Day d6 = new Day();
        d6.hours = 6.75;
        d6.date = Calendar.getInstance().getTime();
        d6.date.setTime(d.date.getTime() + 500000);

        Day d7 = new Day();
        d7.hours = 3.75;
        d7.date = Calendar.getInstance().getTime();
        d7.date.setTime(d.date.getTime() + 600000);


        Day d1 = new Day();
        d1.hours = 3;
        d1.date = Calendar.getInstance().getTime();

        Day d21 = new Day();
        d21.hours = 5;
        d21.date = Calendar.getInstance().getTime();
        d21.date.setTime(d1.date.getTime() + 100000);

        Day d31 = new Day();
        d31.hours = 2;
        d31.date = Calendar.getInstance().getTime();
        d31.date.setTime(d1.date.getTime() + 200000);

        Day d41 = new Day();
        d41.hours = 3.5;
        d41.date = Calendar.getInstance().getTime();
        d41.date.setTime(d1.date.getTime() + 300000);

        Day d51 = new Day();
        d51.hours = 4.5;
        d51.date = Calendar.getInstance().getTime();
        d51.date.setTime(d1.date.getTime() + 400000);

        Day d61 = new Day();
        d61.hours = 6.75;
        d61.date = Calendar.getInstance().getTime();
        d61.date.setTime(d1.date.getTime() + 500000);

        Day d71 = new Day();
        d71.hours = 3.75;
        d71.date = Calendar.getInstance().getTime();
        d71.date.setTime(d1.date.getTime() + 600000);*/

        for (int i = 0; i < 14; i++) {
            Day x = new Day();
            x.hours = Math.random() * 4;
            x.date = Calendar.getInstance().getTime();
            //x.date.setTime((long) (x.date.getTime() + i*Math.random()*100*Math.random()));
            x.date.setDate(x.date.getDate() - i);
            dayDao.insertAll(x);
        }

        //dayDao.insertAll(d, d2, d3, d4, d5, d6, d7);
        //dayDao.insertAll(d, d2, d3, d4, d5, d6, d7, d1, d21, d31, d41, d51, d61, d71);


    }
}