package service;

import model.Item;
import java.util.ArrayList;
import java.util.List;

public class BillingService {
    private List<Item> items;

    public BillingService() {
        items = new ArrayList<>();
    }

    public void addItem(String name, int qty, double price) {
        items.add(new Item(name, qty, price));
    }

    public boolean removeItem(String name) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equalsIgnoreCase(name)) {
                items.remove(i);
                return true;
            }
        }
        return false;
    }

    public List<Item> getItems() {
        return items;
    }

    public double calculateTotal() {
        double subtotal = 0;

        for (Item item : items) {
            subtotal += item.getPrice() * item.getQuantity();
        }

        double gst = subtotal * 0.05; // 5% GST
        return subtotal + gst;
    }

    public double getGST() {
        double subtotal = 0;

        for (Item item : items) {
            subtotal += item.getPrice() * item.getQuantity();
        }

        return subtotal * 0.05;
    }
}