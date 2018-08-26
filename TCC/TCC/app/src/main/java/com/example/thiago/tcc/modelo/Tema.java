package com.example.julieti.tcc.modelo;

import android.os.Parcel;
import android.os.Parcelable;



public class Tema implements Parcelable {
    private long id;
    private String titulo;
    private long areaId;

    public Tema(long id, String titulo, long areaId) {
        this.id = id;
        this.titulo = titulo;
        this.areaId = areaId;
    }

    protected Tema(Parcel in) {
        this(in.readLong(), in.readString(), in.readLong());
    }

    public static final Creator<Tema> CREATOR = new Creator<Tema>() {
        @Override
        public Tema createFromParcel(Parcel in) {
            return new Tema(in);
        }

        @Override
        public Tema[] newArray(int size) {
            return new Tema[size];
        }
    };

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

    public long getAreaId() {
        return areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(titulo);
        dest.writeLong(areaId);
    }
}
