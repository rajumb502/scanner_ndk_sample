package com.qatapultt.tryscannerndk.models;

public class QCode {
    public final String id;
    public int orientation = -1;

    QCode(String id) {
        this.id = id;
    }

    public QCode(String id, int orientation) {
        this.id = id;
        this.orientation = orientation;
    }

    public boolean equals(QCode other) {
        return (this.id.equals(other.id));
    }

    public int hashCode() {
        return this.id.hashCode();
    }
}
