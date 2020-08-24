package com.aeternity.aecan.helpers;

import com.aeternity.aecan.models.Item;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SelectorModalHelper {

    public static Item getSingleItemSelected(ArrayList<Item> items, Integer position) {
        return items.get(position);
    }

    public static ArrayList<Item> setMultipleItemSelected(ArrayList<Item> items, ArrayList<Integer> positionsSelected) {
        unselectAllItems(items);
        for (Integer positionSelected : positionsSelected) {
            items.get(positionSelected).setSelected(true);
        }
        return items;
    }


    public static ArrayList<Item> setSingleItemSelected(ArrayList<Item> items, Integer positionSelected) {
        final int unselectedPosition = -1;
        unselectAllItems(items);
        if (positionSelected > unselectedPosition) {
            items.get(positionSelected).setSelected(true);
        }
        return items;
    }

    public static void unselectAllItems(ArrayList<Item> items) {
        for (Item item : items) {
            item.setSelected(false);
        }
    }

    public static ArrayList<Integer> getItemsIds(ArrayList<Item> items, ArrayList<Integer> positionsSelected) {
        ArrayList<Integer> itemIds = new ArrayList<>();
        for (Integer position : positionsSelected) {
            itemIds.add(items.get(position).getId());
        }
        return itemIds;
    }


}
