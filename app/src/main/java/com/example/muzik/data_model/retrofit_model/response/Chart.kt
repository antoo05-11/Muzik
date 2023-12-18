package com.example.muzik.response_model;

import java.util.Date;
import java.util.List;

public class Chart {
    public static class SongWithView {
        private Song song;
        private List<SongView> songViews;

        public Song getSong() {
            return song;
        }

        public List<SongView> getSongViews() {
            return songViews;
        }
    }

    public static class SongView implements Comparable<SongView> {
        private int songID;
        private Date date;
        private int views;

        public int getSongID() {
            return songID;
        }

        public Date getDate() {
            return date;
        }

        public int getViews() {
            return views;
        }

        @Override
        public int compareTo(SongView o) {
            return date.compareTo(o.date);
        }
    }
}
