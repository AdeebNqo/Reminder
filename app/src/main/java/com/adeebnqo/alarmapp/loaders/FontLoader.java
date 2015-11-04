package com.adeebnqo.alarmapp.loaders;

import android.graphics.Typeface;

import com.adeebnqo.alarmapp.utils.ApplicationData;


/**
 * Created by adeeb on 3/8/15.
 */

public class FontLoader {

    protected Typeface sourcecodepro;
    protected Typeface Aaargh;

    private static FontLoader INSTANCE = null;
    public static FontLoader getInstance(){
        if (INSTANCE==null){
            INSTANCE = new FontLoader();
        }
        return INSTANCE;
    }

    private FontLoader() {
        //loading fonts
        Aaargh = Typeface.createFromAsset(ApplicationData.getContext().getAssets(), "Aaargh.ttf");
        sourcecodepro = Typeface.createFromAsset(ApplicationData.getContext().getAssets(), "SourceCodePro-Regular.ttf");
    }

    public Typeface getSourcecodepro(){
        return sourcecodepro;
    }
    public Typeface getAaargh(){
        return Aaargh;
    }
}
