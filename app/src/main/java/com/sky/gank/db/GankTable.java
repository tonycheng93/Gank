package com.sky.gank.db;

/**
 * Created by tonycheng on 2016/11/26.
 */

public class GankTable {

    public static final class GankContentTable {
        public static final String NAME = "gank";

        public static final class Cols {
            public static final String ID = "_id";
            public static final String DESC = "desc";
            public static final String PUBLISHED_AT = "publishedAt";
            public static final String TYPE = "type";
            public static final String URL = "url";
            public static final String WHO = "who";
            public static final String IMAGES = "images";


        }
    }
}
