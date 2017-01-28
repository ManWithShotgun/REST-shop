package ru.ilia.rest.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by ILIA on 28.01.2017.
 */
@Entity
@Table(name = "cameras")
@JsonRootName("product")
public class Camera {
    @Id
    @Column(name = "id_camera")
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
    @JsonProperty("MP")
    private int MP;
    private String description;

    public Camera() {
    }

    public Camera(String name, int MP, int price, String img, String description) {
        this.price = price;
        this.name = name;
        this.MP = MP;
        this.img=img;
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("Camera = id: %d | name: %s | idPrice: %d | price: %d | MP: %d | img: %s | des.: %s",
                this.id, this.name, this.idPrice, this.price, this.MP, this.img, this.description);
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getMP() {
        return MP;
    }

    public void setMP(int MP) {
        this.MP = MP;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
