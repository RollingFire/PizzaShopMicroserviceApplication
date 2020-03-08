package com.austindorsey.customermicroservice.model;

import java.sql.Date;

public class Customer {
    private int id;
    private String firstName;
    private String lastName;
    private int numberOfOrders;
    private Date memberSince;

    public Customer(int id, String firstName, String lastName, int numberOfOrders, Date memberSince) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.numberOfOrders = numberOfOrders;
        this.memberSince = memberSince;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getNumberOfOrders() {
        return numberOfOrders;
    }

    public void setNumberOfOrders(int numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }

    public Date getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(Date memberSince) {
        this.memberSince = memberSince;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + id;
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((memberSince == null) ? 0 : memberSince.hashCode());
        result = prime * result + numberOfOrders;
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
        Customer other = (Customer) obj;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (id != other.id)
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (memberSince == null) {
            if (other.memberSince != null)
                return false;
        } else if (!memberSince.equals(other.memberSince))
            return false;
        if (numberOfOrders != other.numberOfOrders)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Customer [firstName=" + firstName + ", id=" + id + ", lastName=" + lastName + ", memberSince="
                + memberSince + ", numberOfOrders=" + numberOfOrders + "]";
    }
}