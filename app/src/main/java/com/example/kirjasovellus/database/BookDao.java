package com.example.kirjasovellus.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Dao kirjalle. Sisältää tietokantakyselyitä.
 */
@Dao
public interface BookDao {

    /**
     * Lisää tietokantaan annetut kirjat. Korvaa samalla BookId:llä olevan kirjan uudella.
     * @param books yksi tai useampi lisättävä kirja tietokantaan
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Book... books);

    /**
     * Hakee kaikki kirjat tietokannasta.
     * @return Lista kirjoja
     */
    @Query("SELECT * FROM book")
    Book[] getAllBooks();

    /**
     * Hakee BookId:tä vastaavat kirjat tietokannasta. Käytännössä palauttaa aina yhden kirjan.
     * @param id Haettavan kirjan BookId
     * @return Lista kirjoja
     */
    @Query("SELECT * FROM book WHERE BookId = :id")
    Book[] getBookOnId(int id);

    /**
     * Hakee title:ä vastaavat kirjat tietokannasta. Haku on "sumea",
     * eli kirjan nimen tulee sisältää haettava merkkijono vain jossain osassa nimeä.
     * @param title haettava nimi / nimen osa
     * @return Lista kirjoista
     */
    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%'")
    Book[] getBookOnTitle(String title);

    /**
     * Hakee title:ä vastaavat kirjat tietokannasta, joiden genre-lista sisältää määritetyn GenreId:n.
     * Haku on "sumea", eli kirjan nimen tulee sisältää haettava merkkijono vain jossain osassa nimeä.
     * @param title haettava nimi / nimen osa
     * @param id rajaavan genren GenreId
     * @return Lista kirjoista
     */
    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%' AND GenreIds LIKE '%' || :id || ',%'")
    Book[] getBookOnTitleAndGenreId(String title, int id);

    /**
     * Hakee title:ä vastaavat kirjat tietokannasta. Haku on "sumea",
     * eli kirjan nimen tulee sisältää haettava merkkijono vain jossain osassa nimeä.
     * Järjestää hakutuloksen nimen mukaan aakkosellisesti nousevasti.
     * @param title haettava nimi / nimen osa
     * @return Lista kirjoista järjestettynä aakkosellisesti nousevasti
     */
    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%' ORDER BY title ASC")
    Book[] getBookOnTitleSortedOnTitleAsc(String title);

    /**
     * Hakee title:ä vastaavat kirjat tietokannasta. Haku on "sumea",
     * eli kirjan nimen tulee sisältää haettava merkkijono vain jossain osassa nimeä.
     * Järjestää hakutuloksen nimen mukaan aakkosellisesti laskevasti.
     * @param title haettava nimi / nimen osa
     * @return Lista kirjoista järjestettynä aakkosellisesti laskevasti
     */
    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%' ORDER BY title DESC")
    Book[] getBookOnTitleSortedOnTitleDesc(String title);

    /**
     * Hakee title:ä vastaavat kirjat tietokannasta. Haku on "sumea",
     * eli kirjan nimen tulee sisältää haettava merkkijono vain jossain osassa nimeä.
     * Järjestää hakutuloksen BookId:n mukaan nousevasti.
     * @param title haettava nimi / nimen osa
     * @return Lista kirjoista järjestettynä BookId:n mukaan nousevasti
     */
    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%' ORDER BY BookId ASC")
    Book[] getBookOnTitleSortedOnIdAsc(String title);

    /**
     * Hakee title:ä vastaavat kirjat tietokannasta. Haku on "sumea",
     * eli kirjan nimen tulee sisältää haettava merkkijono vain jossain osassa nimeä.
     * Järjestää hakutuloksen BookId:n mukaan laskevasti.
     * @param title haettava nimi / nimen osa
     * @return Lista kirjoista järjestettynä BookId:n mukaan laskevasti
     */
    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%' ORDER BY BookId DESC")
    Book[] getBookOnTitleSortedOnIdDesc(String title);


    // Sorted queries title and genre
    /**
     * Hakee title:ä vastaavat kirjat tietokannasta, joiden genre-lista sisältää määritetyn GenreId:n.
     * Haku on "sumea", eli kirjan nimen tulee sisältää haettava merkkijono vain jossain osassa nimeä.
     * Järjestää hakutuloksen nimen mukaan aakkosellisesti nousevasti.
     * @param title haettava nimi / nimen osa
     * @param id rajaavan genren GenreId
     * @return Lista kirjoista järjestettynä aakkosellisesti nousevasti
     */
    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%' AND GenreIds LIKE '%' || :id || ',%' ORDER BY title ASC")
    Book[] getBookOnTitleAndGenreIdSortedOnTitleAsc(String title, int id);

    /**
     * Hakee title:ä vastaavat kirjat tietokannasta, joiden genre-lista sisältää määritetyn GenreId:n.
     * Haku on "sumea", eli kirjan nimen tulee sisältää haettava merkkijono vain jossain osassa nimeä.
     * Järjestää hakutuloksen nimen mukaan aakkosellisesti laskevasti.
     * @param title haettava nimi / nimen osa
     * @param id rajaavan genren GenreId
     * @return Lista kirjoista järjestettynä aakkosellisesti laskevasti
     */
    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%' AND GenreIds LIKE '%' || :id || ',%' ORDER BY title DESC")
    Book[] getBookOnTitleAndGenreIdSortedOnTitleDesc(String title, int id);

    /**
     * Hakee title:ä vastaavat kirjat tietokannasta, joiden genre-lista sisältää määritetyn GenreId:n.
     * Haku on "sumea", eli kirjan nimen tulee sisältää haettava merkkijono vain jossain osassa nimeä.
     * Järjestää hakutuloksen BookId:n mukaan nousevasti.
     * @param title haettava nimi / nimen osa
     * @param id rajaavan genren GenreId
     * @return Lista kirjoista järjestettynä BookId:n mukaan nousevasti
     */
    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%' AND GenreIds LIKE '%' || :id || ',%' ORDER BY BookId ASC")
    Book[] getBookOnTitleAndGenreIdSortedOnIdAsc(String title, int id);

    /**
     * Hakee title:ä vastaavat kirjat tietokannasta, joiden genre-lista sisältää määritetyn GenreId:n.
     * Haku on "sumea", eli kirjan nimen tulee sisältää haettava merkkijono vain jossain osassa nimeä.
     * Järjestää hakutuloksen BookId:n mukaan laskevasti.
     * @param title haettava nimi / nimen osa
     * @param id rajaavan genren GenreId
     * @return Lista kirjoista järjestettynä BookId:n mukaan laskevasti
     */
    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%' AND GenreIds LIKE '%' || :id || ',%' ORDER BY BookId DESC")
    Book[] getBookOnTitleAndGenreIdSortedOnIdDesc(String title, int id);


    // Sorted queries title LIVE QUERIES
    /**
     * Hakee title:ä vastaavat kirjat tietokannasta. Haku on "sumea",
     * eli kirjan nimen tulee sisältää haettava merkkijono vain jossain osassa nimeä.
     * Järjestää hakutuloksen nimen mukaan aakkosellisesti nousevasti.
     * Mahdollistaa asynkronisen haun.
     * @param title haettava nimi / nimen osa
     * @return LiveData lista kirjoista järjestettynä aakkosellisesti nousevasti
     */
    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%' ORDER BY title ASC")
    LiveData<List<Book>> getBookOnTitleSortedOnTitleAscLive(String title);

    /**
     * Hakee title:ä vastaavat kirjat tietokannasta. Haku on "sumea",
     * eli kirjan nimen tulee sisältää haettava merkkijono vain jossain osassa nimeä.
     * Järjestää hakutuloksen nimen mukaan aakkosellisesti laskevasti.
     * Mahdollistaa asynkronisen haun.
     * @param title haettava nimi / nimen osa
     * @return LiveData lista kirjoista järjestettynä aakkosellisesti laskevasti
     */
    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%' ORDER BY title DESC")
    LiveData<List<Book>> getBookOnTitleSortedOnTitleDescLive(String title);

    /**
     * Hakee title:ä vastaavat kirjat tietokannasta. Haku on "sumea",
     * eli kirjan nimen tulee sisältää haettava merkkijono vain jossain osassa nimeä.
     * Järjestää hakutuloksen BookId:n mukaan nousevasti.
     * Mahdollistaa asynkronisen haun.
     * @param title haettava nimi / nimen osa
     * @return LiveData lista kirjoista järjestettynä BookId:n mukaan nousevasti
     */
    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%' ORDER BY BookId ASC")
    LiveData<List<Book>> getBookOnTitleSortedOnIdAscLive(String title);

    /**
     * Hakee title:ä vastaavat kirjat tietokannasta. Haku on "sumea",
     * eli kirjan nimen tulee sisältää haettava merkkijono vain jossain osassa nimeä.
     * Järjestää hakutuloksen BookId:n mukaan laskevasti.
     * Mahdollistaa asynkronisen haun.
     * @param title haettava nimi / nimen osa
     * @return LiveData lista kirjoista järjestettynä BookId:n mukaan laskevasti
     */
    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%' ORDER BY BookId DESC")
    LiveData<List<Book>> getBookOnTitleSortedOnIdDescLive(String title);

    // Sorted queries title and genre LIVE QUERIES

    /**
     * Hakee title:ä vastaavat kirjat tietokannasta, joiden genre-lista sisältää määritetyn GenreId:n.
     * Haku on "sumea", eli kirjan nimen tulee sisältää haettava merkkijono vain jossain osassa nimeä.
     * Järjestää hakutuloksen nimen mukaan aakkosellisesti nousevasti.
     * Mahdollistaa asynkronisen haun.
     * @param title haettava nimi / nimen osa
     * @param id rajaavan genren GenreId
     * @return LiveData lista kirjoista järjestettynä nimen mukaan aakkosellisesti nousevasti
     */
    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%' AND GenreIds LIKE '%' || :id || ',%' ORDER BY title ASC")
    LiveData<List<Book>> getBookOnTitleAndGenreIdSortedOnTitleAscLive(String title, int id);

    /**
     * Hakee title:ä vastaavat kirjat tietokannasta, joiden genre-lista sisältää määritetyn GenreId:n.
     * Haku on "sumea", eli kirjan nimen tulee sisältää haettava merkkijono vain jossain osassa nimeä.
     * Järjestää hakutuloksen nimen mukaan aakkosellisesti laskevasti.
     * Mahdollistaa asynkronisen haun.
     * @param title haettava nimi / nimen osa
     * @param id rajaavan genren GenreId
     * @return LiveData lista kirjoista järjestettynä nimen mukaan aakkosellisesti laskevasti
     */
    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%' AND GenreIds LIKE '%' || :id || ',%' ORDER BY title DESC")
    LiveData<List<Book>> getBookOnTitleAndGenreIdSortedOnTitleDescLive(String title, int id);

    /**
     * Hakee title:ä vastaavat kirjat tietokannasta, joiden genre-lista sisältää määritetyn GenreId:n.
     * Haku on "sumea", eli kirjan nimen tulee sisältää haettava merkkijono vain jossain osassa nimeä.
     * Järjestää hakutuloksen BookId:n mukaan nousevasti.
     * Mahdollistaa asynkronisen haun.
     * @param title haettava nimi / nimen osa
     * @param id rajaavan genren GenreId
     * @return LiveData lista kirjoista järjestettynä BookId:n mukaan nousevasti
     */
    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%' AND GenreIds LIKE '%' || :id || ',%' ORDER BY BookId ASC")
    LiveData<List<Book>> getBookOnTitleAndGenreIdSortedOnIdAscLive(String title, int id);

    /**
     * Hakee title:ä vastaavat kirjat tietokannasta, joiden genre-lista sisältää määritetyn GenreId:n.
     * Haku on "sumea", eli kirjan nimen tulee sisältää haettava merkkijono vain jossain osassa nimeä.
     * Järjestää hakutuloksen BookId:n mukaan laskevasti.
     * Mahdollistaa asynkronisen haun.
     * @param title haettava nimi / nimen osa
     * @param id rajaavan genren GenreId
     * @return LiveData lista kirjoista järjestettynä BookId:n mukaan laskevasti
     */
    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%' AND GenreIds LIKE '%' || :id || ',%' ORDER BY BookId DESC")
    LiveData<List<Book>> getBookOnTitleAndGenreIdSortedOnIdDescLive(String title, int id);

    /**
     * Poistaa kaiken datan taulusta
     */
    @Query("DELETE FROM book")
    void nukeTable();

    /**
     * Poistaa taulusta kirjat, joiden BookId vastaa määritettyä id:tä. Käytännössä poistaa aina yhden kirjan
     * @param id Poistettavan/poistettavien kirjojen BookId
     * @return poistettujen kirjojen lukumäärä
     */
    @Query("DELETE FROM book WHERE BookId = :id")
    int deleteBook(int id);
}
