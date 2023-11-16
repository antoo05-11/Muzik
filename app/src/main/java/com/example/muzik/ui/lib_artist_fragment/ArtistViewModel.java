package com.example.muzik.ui.lib_artist_fragment;

import androidx.lifecycle.MutableLiveData;

import com.example.muzik.ui.main_activity.MainActivity;
import com.example.muzik.response_model.Artist;
import com.example.muzik.response_model.Song;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArtistViewModel {
    private final MutableLiveData<List<Song>> songsMutableLiveData = new MutableLiveData<>();
    private final List<Song> listSong = new ArrayList<>();
    private final MutableLiveData<Map<Long, Artist>> artistsLiveData = new MutableLiveData<>();
    private final Map<Long, Artist> mapArtist = new HashMap<>();

    public ArtistViewModel(@NotNull MainActivity mainActivity) {
        songsMutableLiveData.setValue(listSong);
        artistsLiveData.setValue(mapArtist);
    }

    @NotNull
    public ArtistViewModel get(@NotNull Class<ArtistViewModel> java) {
        return this;
    }
}
