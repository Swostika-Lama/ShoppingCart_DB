package model;

public class CartRecord {
    private int id;
    private int totalItems;
    private double totalCost;
    private String language;

    public int getTotalItems() { return totalItems; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
}