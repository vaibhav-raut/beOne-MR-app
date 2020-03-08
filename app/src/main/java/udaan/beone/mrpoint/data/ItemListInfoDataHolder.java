package udaan.beone.mrpoint.data;

import java.util.ArrayList;

import udaan.beone.mrpoint.http.model.MrStockDisplay.ItemDisplay;

/**
 * Created by Vaibhav on 18-06-2016.
 */
public class ItemListInfoDataHolder {

    private static ItemListInfoDataHolder holder;

    static {
        holder = new ItemListInfoDataHolder();
    }

    private ItemListInfoDataHolder() {
    }

    public static ItemListInfoDataHolder instance() {
        return holder;
    }

    private ArrayList<ItemDisplay> itemDisplays;

    public ArrayList<ItemDisplay> getItemDisplays() {
        return itemDisplays;
    }

    public void setItemDisplays(ArrayList<ItemDisplay> itemDisplays) {
        this.itemDisplays = itemDisplays;
    }
}
