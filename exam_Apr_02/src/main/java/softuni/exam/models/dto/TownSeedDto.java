package softuni.exam.models.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class TownSeedDto {

    @Length(min = 2)
    @NotBlank
    private String townName;

    @Positive
    @NotNull
    private Integer population;

    public TownSeedDto() {
    }

    public String getName() {
        return townName;
    }

    public void setName(String name) {
        this.townName = name;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }
}
