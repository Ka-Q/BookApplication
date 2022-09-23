package com.example.kirjasovellus.tabBooks;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Pattern;

public class EmojiWatcher implements TextWatcher {

    // Ylläpitävät tietoa kahdesta viimeisimmästä merkkijonosta.
    String oldSymbolString = "";
    String newSymbolString = "";

    // Viittauksia, joiden avulla data palautetaan ylöspäin.
    String[] symbolArray;
    EditText etGenreSymbol;

    /**
     * Konstruktori EmojiWatcher:ille.
     * @param symbolArray viittaus listaan, jossa on käyttäjän kaksi viimeisintä syötettyä merkkijonoa
     * @param etGenreSymbol viittaus EditText-komponenttiin, josta luetaan käyttäjän syötettä.
     */
    public EmojiWatcher(String[] symbolArray, EditText etGenreSymbol) {
        this.symbolArray = symbolArray;
        this.etGenreSymbol = etGenreSymbol;
    }

    /**
     * Asettaa EditTextissä olevat merkit muttujaan oldSymbolString.
     * Parametreilla ei tehdä mitään.
     */
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        oldSymbolString = etGenreSymbol.getText().toString();
        System.out.println("VANHA " + oldSymbolString);
    }

    /**
     * Metodilla ei tehdä mitään.
     */
    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

    /**
     * Ottaa muutetun merkkijonon ja vertaa sitä vanhaan. Erotus on käyttäjän syöttämä uusi merkki
     * Erotus saadaan ottamalla substring uudesta merkkijonosta vanhan merkkijonon pituudella.
     * Toteutus näin, koska emojit voivat olla emojista riippuen 1-3 merkkiä pitkiä, eikä
     * yksinkertainen charAt() toimi ennaltaodotettavasti.
     * Parametreilla ei tehdä mitään.
     */
    @Override
    public void afterTextChanged(Editable editable) {

        if (etGenreSymbol.getText().length() > 1) {
            newSymbolString = etGenreSymbol.getText().toString();
            System.out.println("UUSI " + newSymbolString);

            String remaining = "";

            if (newSymbolString.length() > oldSymbolString.length()) {
                remaining = newSymbolString.substring(oldSymbolString.length());
            }
            else if (oldSymbolString.length() > newSymbolString.length()) {
                remaining = newSymbolString.substring(newSymbolString.length() - symbolArray[1].length());

            } else {
                remaining = "";
            }

            System.out.println("JÄLJELLÄ " + remaining);
            symbolArray[1] = symbolArray[0];
            symbolArray[0] = remaining;

        } else {
            symbolArray[0] = "";
            symbolArray[1] = symbolArray[0];
        }
    }

    // Regex-pattern emojien tunnistusta varten. Ei tue ihan kaikkia emojeja. Lähteenä: https://gist.github.com/cmkilger/b8f7dba3e76244a84e7e
    private static Pattern emojiPattern = Pattern.compile("^[\\s\n\r]*(?:(?:[\u00a9\u00ae\u203c\u2049\u2122\u2139\u2194-\u2199\u21a9-\u21aa\u231a-\u231b\u2328\u23cf\u23e9-\u23f3\u23f8-\u23fa\u24c2\u25aa-\u25ab\u25b6\u25c0\u25fb-\u25fe\u2600-\u2604\u260e\u2611\u2614-\u2615\u2618\u261d\u2620\u2622-\u2623\u2626\u262a\u262e-\u262f\u2638-\u263a\u2648-\u2653\u2660\u2663\u2665-\u2666\u2668\u267b\u267f\u2692-\u2694\u2696-\u2697\u2699\u269b-\u269c\u26a0-\u26a1\u26aa-\u26ab\u26b0-\u26b1\u26bd-\u26be\u26c4-\u26c5\u26c8\u26ce-\u26cf\u26d1\u26d3-\u26d4\u26e9-\u26ea\u26f0-\u26f5\u26f7-\u26fa\u26fd\u2702\u2705\u2708-\u270d\u270f\u2712\u2714\u2716\u271d\u2721\u2728\u2733-\u2734\u2744\u2747\u274c\u274e\u2753-\u2755\u2757\u2763-\u2764\u2795-\u2797\u27a1\u27b0\u27bf\u2934-\u2935\u2b05-\u2b07\u2b1b-\u2b1c\u2b50\u2b55\u3030\u303d\u3297\u3299\ud83c\udc04\ud83c\udccf\ud83c\udd70-\ud83c\udd71\ud83c\udd7e-\ud83c\udd7f\ud83c\udd8e\ud83c\udd91-\ud83c\udd9a\ud83c\ude01-\ud83c\ude02\ud83c\ude1a\ud83c\ude2f\ud83c\ude32-\ud83c\ude3a\ud83c\ude50-\ud83c\ude51\u200d\ud83c\udf00-\ud83d\uddff\ud83d\ude00-\ud83d\ude4f\ud83d\ude80-\ud83d\udeff\ud83e\udd00-\ud83e\uddff\udb40\udc20-\udb40\udc7f]|\u200d[\u2640\u2642]|[\ud83c\udde6-\ud83c\uddff]{2}|.[\u20e0\u20e3\ufe0f]+)+[\\s\n\r]*)+$");

    /**
     * Tarkistaa, koostuuko merkkijono ainoastaan emojeista.
     * Ottaa huomioon myös perinteiset unicode-merkit.
     * Palauttaa true tai false.
     * @param string Tarkistettava merkkijono
     * @return totuusarvo siitä, koostuuko merkkijono vain emojeista
     */
    public static Boolean isEmojiOnly(String string) {
        if (string == null) return false;
        if (string.length() == 0) return false;
        // Otetaan huomioon myös perinteiset unicode-merkit, joita ei välttämättä lasketa emojeiksi.
        if (emojiPattern.matcher(string).find() || Character.UnicodeBlock.of(string.charAt(0)) == Character.UnicodeBlock.EMOTICONS) {
            return true;
        } return false;
    }
}
