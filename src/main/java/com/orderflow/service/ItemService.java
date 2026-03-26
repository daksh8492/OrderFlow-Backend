package com.orderflow.service;

import com.orderflow.entity.product.Item;
import com.orderflow.entity.product.Variant;
import com.orderflow.exceptions.ItemNotFoundException;
import com.orderflow.repository.product.ItemRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ItemService {

    @Autowired
    private ItemRepo itemRepo;

    public Item addItem(Item item){
        if (item.getStatus() == null) item.setStatus(Item.ItemStatus.DRAFT);
        return itemRepo.save(item);
    }

    public List<Item> getAllItems(){
        return itemRepo.findAll();
    }

    public Item getItemById(UUID id){
        return itemRepo.findById(id).orElseThrow(() -> new ItemNotFoundException("Item not found"));
    }

    public Item updateItem(UUID id, Item item){
        Item existingItem = getItemById(id);
        BeanUtils.copyProperties(item, existingItem, "itemId");
        return itemRepo.save(existingItem);
    }

    public void deleteItem(UUID id){
        Item item = getItemById(id);
        itemRepo.delete(item);
    }

    public Item activateItem(UUID id){
        Item item = getItemById(id);
        item.setStatus(Item.ItemStatus.ACTIVE);
        return itemRepo.save(item);
    }

    public Item deactivateItem(UUID id){
        Item item = getItemById(id);
        item.setStatus(Item.ItemStatus.INACTIVE);
        return itemRepo.save(item);
    }

    public Item discontinueItem(UUID id){
        Item item = getItemById(id);
        item.setStatus(Item.ItemStatus.DISCONTINUED);
        return itemRepo.save(item);
    }

    public List<Item> getItemsByCategory(Item.ItemCategory category){
        return itemRepo.findByCategory(category);
    }

    public List<Item> getItemsByStatus(Item.ItemStatus status){
        return itemRepo.findByStatus(status);
    }

    public List<Item> getItemsBySource(Item.InwardSource source){
        return itemRepo.findBySourceType(source);
    }


}
