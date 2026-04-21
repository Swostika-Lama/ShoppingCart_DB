package model;

public class CartRecord {

    private int totalItems;
    private double totalCost;

    // FIX: use languageId instead of String
    private int languageId;

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    // NEW
    public int getLanguageId() {
        return languageId;
    }

    //NEW
    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }
}