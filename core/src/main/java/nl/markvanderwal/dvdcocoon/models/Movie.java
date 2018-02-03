package nl.markvanderwal.dvdcocoon.models;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.*;
import nl.markvanderwal.dvdcocoon.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 1-2-2018
 */
@DatabaseTable(tableName = "Movies")
public class Movie {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String name;

    @DatabaseField
    private String label;

    @DatabaseField
    private String actors;

    @DatabaseField
    private String description;

    @DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh = true)
    private Medium medium;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Medium getMedium() {
        return medium;
    }

    public void setMedium(Medium medium) {
        this.medium = medium;
    }

    @Override
    public boolean equals(Object other) {
        if(other == null) return false;
        if(other == this) return true;
        if(!(other instanceof Movie)) return false;

        Movie otherMovie = (Movie)other;
        return otherMovie.getId() == getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }
}
