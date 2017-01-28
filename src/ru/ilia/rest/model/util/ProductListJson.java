package ru.ilia.rest.model.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.ilia.rest.model.entity.Monitor;

import java.util.ArrayList;

/**
 * Created by ILIA on 28.01.2017.
 */
public class ProductListJson<T> {
    @JsonProperty("meta")
    private MetaPagination metaPagination;
    @JsonProperty("products")
    private ArrayList<T> list;

    public ProductListJson() {
    }

    public ProductListJson(MetaPagination metaPagination, ArrayList<T> list) {
        this.metaPagination = metaPagination;
        this.list = list;
    }

    public MetaPagination getMetaPagination() {
        return metaPagination;
    }

    public void setMetaPagination(MetaPagination metaPagination) {
        this.metaPagination = metaPagination;
    }

    public ArrayList<T> getList() {
        return list;
    }

    public void setList(ArrayList<T> list) {
        this.list = list;
    }
}
