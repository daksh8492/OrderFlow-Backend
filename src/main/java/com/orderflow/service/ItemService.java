package com.orderflow.service;

import com.orderflow.dto.ItemDto;
import com.orderflow.entity.product.Item;
import com.orderflow.entity.product.Variant;
import com.orderflow.exceptions.ItemNotFoundException;
import com.orderflow.mapper.ItemMapper;
import com.orderflow.repository.product.ItemRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ItemService {

    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private ItemMapper itemMapper;

    public ItemDto addItem(ItemDto itemDto) {
        Item item = itemMapper.itemDtoToItem(itemDto);
        if (item.getStatus() == null) item.setStatus(Item.ItemStatus.DRAFT);
        return itemMapper.itemToItemDto(itemRepo.save(item));
    }

    public ItemDto getItemById(UUID id) {
        return itemMapper.itemToItemDto(itemRepo.findById(id).orElseThrow(() -> new ItemNotFoundException("Item not found")));
    }

    public ItemDto updateItem(UUID id, ItemDto itemDto) {
        Item existingItem = itemRepo.findById(id).orElseThrow(() -> new ItemNotFoundException("Item not found"));
        if (itemDto.getName() != null) existingItem.setName(itemDto.getName());
        if (itemDto.getSourceType() != null) existingItem.setSourceType(itemDto.getSourceType());
        if (itemDto.getCategory() != null) existingItem.setCategory(itemDto.getCategory());
        if (itemDto.getStatus() != null) existingItem.setStatus(itemDto.getStatus());
        return itemMapper.itemToItemDto(itemRepo.save(existingItem));
    }

    public void deleteItem(UUID id) {
        Item item = itemMapper.itemDtoToItem(getItemById(id));
        itemRepo.delete(item);
    }

    public ItemDto activateItem(UUID id) {
        Item item = itemRepo.findById(id).orElseThrow(() -> new ItemNotFoundException("Item not found"));
        item.setStatus(Item.ItemStatus.ACTIVE);
        return itemMapper.itemToItemDto(itemRepo.save(item));
    }

    public ItemDto deactivateItem(UUID id) {
        Item item = itemRepo.findById(id).orElseThrow(() -> new ItemNotFoundException("Item not found"));
        item.setStatus(Item.ItemStatus.INACTIVE);
        return itemMapper.itemToItemDto(itemRepo.save(item));
    }

    public ItemDto discontinueItem(UUID id) {
        Item item = itemRepo.findById(id).orElseThrow(() -> new ItemNotFoundException("Item not found"));
        item.setStatus(Item.ItemStatus.DISCONTINUED);
        return itemMapper.itemToItemDto(itemRepo.save(item));
    }

    public Page<ItemDto> getAllItems(Pageable pageable) {
        return itemRepo.findAll(pageable).map(itemMapper::itemToItemDto);
    }

    public Page<ItemDto> getItemsByCategory(Item.ItemCategory category, Pageable pageable) {
        return itemRepo.findByCategory(category, pageable).map(itemMapper::itemToItemDto);
    }

    public Page<ItemDto> getItemsByStatus(Item.ItemStatus status, Pageable pageable) {
        return itemRepo.findByStatus(status, pageable).map(itemMapper::itemToItemDto);
    }

    public Page<ItemDto> getItemsBySource(Item.InwardSource source, Pageable pageable) {
        return itemRepo.findBySourceType(source, pageable).map(itemMapper::itemToItemDto);
    }


}
