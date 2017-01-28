package ru.ilia.rest.model.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Created by ILIA on 28.01.2017.
 */
public class MetaPagination {
    private int limit;
    private int offset;
    @JsonProperty("total_count")
    private long totalCount;
    private String next;
    private String previous;

    public MetaPagination() {
    }

    public MetaPagination(int limit, int offset, long totalCount, String next, String previous) {
        this.limit = limit;
        this.offset = offset;
        this.totalCount = totalCount;
        this.next = next;
        this.previous = previous;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }
}
