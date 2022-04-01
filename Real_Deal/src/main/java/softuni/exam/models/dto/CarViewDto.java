package softuni.exam.models.dto;

import softuni.exam.models.entity.Picture;

import java.util.Set;

public class CarViewDto {
    private String make;
    private String model;
    private Integer kilometers;
    private String registeredOn;
    private Set<Picture> pictures;

    public CarViewDto() {
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getKilometers() {
        return kilometers;
    }

    public void setKilometers(Integer kilometers) {
        this.kilometers = kilometers;
    }

    public String getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(String registeredOn) {
        this.registeredOn = registeredOn;
    }

    public Set<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(Set<Picture> pictures) {
        this.pictures = pictures;
    }

    @Override
    public String toString() {
        return String.format("""
                Car make - %s, model - %s
                	Kilometers - %d
                	Registered on - %s
                	Number of pictures - %d
                
                """, this.getMake(), this.getModel(),
                this.getKilometers(),
                this.getRegisteredOn(),
                this.getPictures().size());
    }
}
