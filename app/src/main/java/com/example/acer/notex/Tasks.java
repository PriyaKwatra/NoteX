package com.example.acer.notex;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by acer on 18.07.2017.
 */

public class Tasks implements Parcelable {
        String taskName;
        int id;
        boolean done;
    int isAlarmSet;
    int important;

    public Tasks()
    {

    }

        public Tasks(String taskName, int id, boolean done,int isAlarmSet,int important) {
            this.taskName = taskName;
            this.id = id;
            this.done = done;
            this.isAlarmSet=isAlarmSet;
            this.important=important;
        }
        public Tasks(String taskName,  boolean done,int isAlarmSet,int important) {
            this.taskName = taskName;
             this.isAlarmSet=isAlarmSet;
            this.done = done;
            this.important=important;
        }


    protected Tasks(Parcel in) {
        taskName = in.readString();
        id = in.readInt();
        done = in.readByte() != 0;
    }


    public static final Creator<Tasks> CREATOR = new Creator<Tasks>() {
        @Override
        public Tasks createFromParcel(Parcel in) {
            return new Tasks(in);
        }

        @Override
        public Tasks[] newArray(int size) {
            return new Tasks[size];
        }
    };

    public Integer getImportant() {
        return important;
    }

    public void setImportant(Integer important) {
        this.important = important;
    }

    public Integer isAlarmSet() {
        return isAlarmSet;
    }

    public void setAlarmSet(Integer alarmSet) {
        isAlarmSet = alarmSet;
    }

    public String getTaskName() {
            return taskName;
        }

        public int getId() {
            return id;
        }

        public boolean isDone() {
            return done;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setDone(boolean done) {
            this.done = done;
        }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(taskName);
        dest.writeInt(id);
        dest.writeByte((byte) (done ? 1 : 0));
        dest.writeInt(isAlarmSet);
        dest.writeInt(important);
    }
}
