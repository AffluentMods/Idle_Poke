package org.affluentproductions.idlepokemon;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Constants {

    public static final Color MAIN_COLOR =
            new Color(Integer.valueOf("02", 16), Integer.valueOf("00", 16), Integer.valueOf("FF", 16));
    public static final String PREFIX = "p";
    public static final String TEST_PREFIX = "pt";
    public static final String main_guild = "605889879787700263";
    public static final String TAB = "\u2004\u2004\u2004\u2004";
    public static final List<String> emoteGuilds = new ArrayList<>(Collections.singletonList("667859476094910483"));
    public static final String dbl_vote_auth = "IOIPrjXX5bh9Dc-3";
    public static final String dbl_token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
                                           ".eyJpZCI6IjY3MDIxNjM4NjgyNzc4MDEwNiIsImJvdCI6dHJ1ZSwiaWF0IjoxNTgzNTczNDUyfQ" +
                                           ".6ucuA7p6lBNmSJM65JSW8PI3IaCFfDZV-T4FQrx9Uic";
    public static final String minActive = (System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)) + "";
}