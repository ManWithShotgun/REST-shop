package ru.ilia.rest.model.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.ilia.rest.model.entity.Monitor;

import java.util.ArrayList;

/**
 * Created by ILIA on 28.01.2017.
 */
public class MonitorListJson {
    @JsonProperty("meta")
    private MetaPagination metaPagination;
    @JsonProperty("products")
    private ArrayList<Monitor> list;

    public MonitorListJson() {
    }

    public MonitorListJson(MetaPagination metaPagination, ArrayList<Monitor> list) {
        this.metaPagination = metaPagination;
        this.list = list;
    }

    public MetaPagination getMetaPagination() {
        return metaPagination;
    }

    public void setMetaPagination(MetaPagination metaPagination) {
        this.metaPagination = metaPagination;
    }

    public ArrayList<Monitor> getList() {
        return list;
    }

    public void setList(ArrayList<Monitor> list) {
        this.list = list;
    }
}
