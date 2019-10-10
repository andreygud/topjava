package ru.javawebinar.topjava.model;

abstract public class GenericObject {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isNew(){
        if (id == null)
            return true;
        return false;
    }

}
