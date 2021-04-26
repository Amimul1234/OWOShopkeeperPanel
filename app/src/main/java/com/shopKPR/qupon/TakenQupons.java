package com.shopKPR.qupon;

public class TakenQupons {
    private Long id;
    private Long quponId;

    public TakenQupons() {
    }

    public TakenQupons(Long id, Long quponId) {
        this.id = id;
        this.quponId = quponId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuponId() {
        return quponId;
    }

    public void setQuponId(Long quponId) {
        this.quponId = quponId;
    }
}
