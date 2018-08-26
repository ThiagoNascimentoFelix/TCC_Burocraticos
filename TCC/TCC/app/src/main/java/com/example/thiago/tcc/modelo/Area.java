package com.example.julieti.tcc.modelo;

import android.os.Parcel;
import android.os.Parcelable;


public class Area implements Parcelable {
    private long id;
    private String titulo;

    public static final Creator<Area> CREATOR = new Creator<Area>() {
        @Override
        public Area createFromParcel(Parcel in) {
            return new Area(in);
        }

        @Override
        public Area[] newArray(int size) {
            return new Area[size];
        }
    };

    public Area(long id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }

    protected Area(Parcel in) {
        this(in.readLong(), in.readString());
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(titulo);
    }
}
