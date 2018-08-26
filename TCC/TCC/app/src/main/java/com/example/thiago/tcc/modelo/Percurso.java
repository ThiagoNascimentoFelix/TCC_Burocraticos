package com.example.julieti.tcc.modelo;

import android.os.Parcel;
import android.os.Parcelable;


public class Percurso implements Parcelable {
    private long id;
    private String titulo;
    private String status;
    private double latitude;
    private double longitude;
    private long temaId;

    public Percurso(String titulo, String status) {
        this(0, titulo, status);
    }

    public Percurso(long id, String titulo, String status) {
        this.id = id;
        this.titulo = titulo;
        this.status = status;
        this.latitude = 0;
        this.longitude = 0;
        this.temaId = 0;
    }

    protected Percurso(Parcel in) {
        id = in.readLong();
        titulo = in.readString();
        status = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        temaId = in.readLong();
    }

    public static final Creator<Percurso> CREATOR = new Creator<Percurso>() {
        @Override
        public Percurso createFromParcel(Parcel in) {
            return new Percurso(in);
        }

        @Override
        public Percurso[] newArray(int size) {
            return new Percurso[size];
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getTemaId() {
        return temaId;
    }

    public void setTemaId(long temaId) {
        this.temaId = temaId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(titulo);
        dest.writeString(status);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeLong(temaId);
    }
}
