package com.austindorsey.menumicroservice.models;

import java.sql.Date;

public class Menu {
    int id;
    String menuName;
    String items;
    Date revisionDate;

    public Menu(int id, String menuName, String items, Date revisionDate) {
        this.id = id;
        this.menuName = menuName;
        this.items = items;
        this.revisionDate = revisionDate;
    }

    public int getId() {
        return id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
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
        return "Menu [id=" + id + ", items=" + items + ", menuName=" + menuName + ", revisionDate=" + revisionDate + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((items == null) ? 0 : items.hashCode());
        result = prime * result + ((menuName == null) ? 0 : menuName.hashCode());
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
        if (menuName == null) {
            if (other.menuName != null)
                return false;
        } else if (!menuName.equals(other.menuName))
            return false;
        if (revisionDate == null) {
            if (other.revisionDate != null)
                return false;
        } else if (!revisionDate.equals(other.revisionDate))
            return false;
        return true;
    }
}