package com.austindorsey.menumicroservice.models;

import java.sql.Date;

public class MenuItem {
    int id;
    String catagory;
    String name;
    String discription;
    Number cost;
    Date revisionDate;

    public MenuItem(int id, String catagory, String name, String discription, Number cost, Date revisionDate) {
        this.id = id;
        this.catagory = catagory;
        this.name = name;
        this.discription = discription;
        this.cost = cost;
        this.revisionDate = revisionDate;
    }

    public int getId() {
        return id;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public Number getCost() {
        return cost;
    }

    public void setCost(Number cost) {
        this.cost = cost;
    }

    public Date getRevisionDate() {
        return revisionDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((catagory == null) ? 0 : catagory.hashCode());
        result = prime * result + ((cost == null) ? 0 : cost.hashCode());
        result = prime * result + ((discription == null) ? 0 : discription.hashCode());
        result = prime * result + id;
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
        MenuItem other = (MenuItem) obj;
        if (catagory == null) {
            if (other.catagory != null)
                return false;
        } else if (!catagory.equals(other.catagory))
            return false;
        if (cost == null) {
            if (other.cost != null)
                return false;
        } else if (!cost.equals(other.cost))
            return false;
        if (discription == null) {
            if (other.discription != null)
                return false;
        } else if (!discription.equals(other.discription))
            return false;
        if (id != other.id)
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

    @Override
    public String toString() {
        return "MenuItem [catagory=" + catagory + ", cost=" + cost + ", discription=" + discription + ", id=" + id
                + ", name=" + name + ", revisionDate=" + revisionDate + "]";
    }
}