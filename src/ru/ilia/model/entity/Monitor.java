package ru.ilia.model.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by ILIA on 27.01.2017.
 */
@Entity
@Table(name = "monitors")
public class Monitor {
    @Id
    @Column(name = "id_monitor")
    @GeneratedValue
    private long id;
    @NotNull
    @Min(0)
    private int idPrice;
    @Transient
    private int price;
    private String name;
    private int inch;
    private String description;


    public Monitor() {
    }

    public Monitor(String name, int inch, int price, String description) {
        this.price = price;
        this.name = name;
        this.inch = inch;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    public int getIdPrice() {
        return idPrice;
    }

    public void setIdPrice(int idPrice) {
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
}
