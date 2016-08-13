package com.runningoutofbreadth.boda.sectionactivities;

import android.os.Parcel;
import android.os.Parcelable;

import com.runningoutofbreadth.boda.db.Word;

/**
 * Created by SandD on 8/11/2016.
 */
public class ColoredResult implements Parcelable{
    Word word;
    String hangeul;
    String romanization;
    String translation;
    int colorResId;

    ColoredResult(Word word, int color) {
        this.word = word;
        this.colorResId = color;
        this.hangeul = word.getHangeul();
        this.romanization = word.getRomanization();
        this.translation = word.getTranslation();
    }

    protected ColoredResult(Parcel in) {
        hangeul = in.readString();
        romanization = in.readString();
        translation = in.readString();
        colorResId = in.readInt();
    }

    public static final Creator<ColoredResult> CREATOR = new Creator<ColoredResult>() {
        @Override
        public ColoredResult createFromParcel(Parcel in) {
            return new ColoredResult(in);
        }

        @Override
        public ColoredResult[] newArray(int size) {
            return new ColoredResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.hangeul);
        dest.writeString(this.romanization);
        dest.writeString(this.translation);
        dest.writeInt(this.colorResId);
    }
}
