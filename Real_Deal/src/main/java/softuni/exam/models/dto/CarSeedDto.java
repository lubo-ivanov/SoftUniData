package softuni.exam.models.dto;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Positive;
import java.time.LocalDate;

public class CarSeedDto {

    @Expose
    @Length(min = 2, max = 20)
    private String model;

    @Expose
    @Length(min = 2, max = 20)
    private String make;

    @Expose
    @Positive
    private Integer kilometers;

    @Expose
    private String registeredOn;

    public CarSeedDto() {
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
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
}
