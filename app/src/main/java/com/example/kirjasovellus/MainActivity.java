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
        generateTestData(bookDao, genreDao);

        //Get a global FragmentManager
        fragmentManager = getSupportFragmentManager();
    }


    private void generateTestData(BookDao bookDao, GenreDao genreDao) {

        bookDao.nukeTable();
        genreDao.nukeTable();

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

        genreDao.insertAll(history, culture, math, sci, fantasy);

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

    }
}