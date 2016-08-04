package com.runningoutofbreadth.boda.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Contains user information e.g. progress, achievements
 */
@Table(database = BodaDatabase.class)
public class UserProfile extends BaseModel{

    // identifier int within the db
    @PrimaryKey
    int sId;

    @Column
    String category;

    @Column
    int current_progress;

    @Column
    int item_total;

    @Column
    double percent;

    public int getsId() {
        return sId;
    }

    public void setsId(int sId) {
        this.sId = sId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCurrent_progress() {
        return current_progress;
    }

    public void setCurrent_progress(int current_progress) {
        this.current_progress = current_progress;
    }

    public int getItem_total() {
        return item_total;
    }

    public void setItem_total(int item_total) {
        this.item_total = item_total;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }
}
