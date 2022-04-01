package softuni.exam.models.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD)
public class OfferSeedDto {

    @XmlElement(name = "price")
    @Positive
    private BigDecimal price;

    @XmlElement(name = "description")
    @Length(min = 5)
    private String description;

    @XmlElement(name = "has-gold-status")
    private Boolean hasGoldStatus;

    @XmlElement(name = "added-on")
    private String addedOn;

    @XmlElement(name = "car")
    private EntityIdDto car;

    @XmlElement(name = "seller")
    private EntityIdDto seller;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getHasGoldStatus() {
        return hasGoldStatus;
    }

    public void setHasGoldStatus(Boolean hasGoldStatus) {
        this.hasGoldStatus = hasGoldStatus;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public EntityIdDto getCar() {
        return car;
    }

    public void setCar(EntityIdDto car) {
        this.car = car;
    }

    public EntityIdDto getSeller() {
        return seller;
    }

    public void setSeller(EntityIdDto seller) {
        this.seller = seller;
    }
}
