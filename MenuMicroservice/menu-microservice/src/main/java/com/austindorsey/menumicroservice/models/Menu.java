package com.austindorsey.menumicroservice.models;

import java.sql.Date;

public class Menu {
    int id;
    String name;
    String items;
    Date revisionDate;

    public Menu(int id, String name, String items, Date revisionDate) {
        this.id = id;
        this.name = name;
        this.items = items;
        this.revisionDate = revisionDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public Date getRevisionDate() {
        return revisionDate;
    }

    @Override
    public String toString() {
        return "Menu [id=" + id + ", items=" + items + ", name=" + name + ", revisionDate=" + revisionDate + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((items == null) ? 0 : items.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((revisionDate == null) ? 0 : revisionDate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Menu other = (Menu) obj;
        if (id != other.id)
            return false;
        if (items == null) {
            if (other.items != null)
                return false;
        } else if (!items.equals(other.items))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (revisionDate == null) {
            if (other.revisionDate != null)
                return false;
        } else if (!revisionDate.equals(other.revisionDate))
            return false;
        return true;
    }
}