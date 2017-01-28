package ru.ilia.rest.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by ILIA on 27.01.2017.
 */
@Entity
@Table(name = "monitors")
@JsonRootName("product")
public class Monitor {
    @Id
    @Column(name = "id_monitor")
    @GeneratedValue
    private long id;
    @NotNull
    @Min(0)
    @Column(name = "id_price")
    @JsonIgnore
    private long idPrice;
    @Transient
    @JsonProperty("pricePer")
    private int price;
    private String name;
    private String img;
    private int inch;
    private String description;


    public Monitor() {
    }

    public Monitor(String name, int inch, int price, String img, String description) {
        this.price = price;
        this.name = name;
        this.inch = inch;
        this.img=img;
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("Monitor = id: %d | name: %s | idPrice: %d | price: %d | inch: %d | img: %s | des.: %s",
                            this.id, this.name, this.idPrice, this.price, this.inch, this.img, this.description);
    }

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    public long getIdPrice() {
        return idPrice;
    }

    public void setIdPrice(long idPrice) {
        this.idPrice = idPrice;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInch() {
        return inch;
    }

    public void setInch(int inch) {
        this.inch = inch;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
