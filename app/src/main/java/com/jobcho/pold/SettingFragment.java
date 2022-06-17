package com.jobcho.pold;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends PreferenceFragmentCompat {

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    SharedPreferences prefs;

    ListPreference font;
    ListPreference backColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.fragment_setting);
        font = findPreference("font_edit");
        backColor = findPreference("back_color");

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if(!prefs.getString("font_edit", "").equals("")){
            font.setSummary(prefs.getString("font_edit", "꽃집막내딸"));
        }
        if(!prefs.getString("back_color", "").equals("")){
            backColor.setSummary(prefs.getString("back_color", "파란색"));
        }

        prefs.registerOnSharedPreferenceChangeListener(prefListener);
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {

    }

    SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.equals("font_list")){
                font.setSummary(prefs.getString("font_edit", "꽃집막내딸"));
            }

            if(key.equals("back_list")){
                font.setSummary(prefs.getString("back_color", "파란색"));
            }
        }
    };
}