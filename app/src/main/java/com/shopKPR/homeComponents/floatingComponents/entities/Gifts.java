package com.shopKPR.homeComponents.floatingComponents.entities;

public class Gifts {
    private Long giftId;
    private String giftImage;
    private String giftDetails;

    public Gifts() {
    }

    public Gifts(Long giftId, String giftImage, String giftDetails) {
        this.giftId = giftId;
        this.giftImage = giftImage;
        this.giftDetails = giftDetails;
    }

    public Long getGiftId() {
        return giftId;
    }

    public void setGiftId(Long giftId) {
        this.giftId = giftId;
    }

    public String getGiftImage() {
        return giftImage;
    }

    public void setGiftImage(String giftImage) {
        this.giftImage = giftImage;
    }

    public String getGiftDetails() {
        return giftDetails;
    }

    public void setGiftDetails(String giftDetails) {
        this.giftDetails = giftDetails;
    }
}
