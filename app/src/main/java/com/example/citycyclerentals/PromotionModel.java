package com.example.citycyclerentals;

public class PromotionModel {
    private int promotionId;
    private String title;
    private String timePeriod;
    private String description;
    private String promoCode;

    public PromotionModel(int promotionId, String title, String timePeriod, String description, String promoCode) {
        this.promotionId = promotionId;
        this.title = title;
        this.timePeriod = timePeriod;
        this.description = description;
        this.promoCode = promoCode;
    }

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }
}
