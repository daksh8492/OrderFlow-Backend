package com.orderflow.controller;

import com.orderflow.dto.ItemDto;
import com.orderflow.entity.product.Item;
import com.orderflow.mapper.ItemMapper;
import com.orderflow.service.ItemService;
import org.aspectj.weaver.patterns.ITokenSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemMapper itemMapper;

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestBody ItemDto itemDto){
        return new ResponseEntity<>(itemMapper.itemToItemDto(itemService.addItem(itemMapper.itemDtoToItem(itemDto))), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAllItems(){
        return new ResponseEntity<>(itemMapper.itemsToItemDtos(itemService.getAllItems()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable UUID id){
        return new ResponseEntity<>(itemMapper.itemToItemDto(itemService.getItemById(id)), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable UUID id, @RequestBody ItemDto itemDto){
        return new ResponseEntity<>(itemMapper.itemToItemDto(itemService.updateItem(id, itemMapper.itemDtoToItem(itemDto))), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable UUID id){
        itemService.deleteItem(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<ItemDto> activateItem(@PathVariable UUID id){
        return new ResponseEntity<>(itemMapper.itemToItemDto(itemService.activateItem(id)), HttpStatus.OK);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<ItemDto> deactivateItem(@PathVariable UUID id){
        return new ResponseEntity<>(itemMapper.itemToItemDto(itemService.deactivateItem(id)), HttpStatus.OK);
    }

    @PutMapping("/{id}/discontinue")
    public ResponseEntity<ItemDto> discontinueItem(@PathVariable UUID id){
        return new ResponseEntity<>(itemMapper.itemToItemDto(itemService.discontinueItem(id)), HttpStatus.OK);
    }

    @PutMapping("/category/{category}")
    public ResponseEntity<List<ItemDto>> getItemsByCategory(@PathVariable Item.ItemCategory category){
        return new ResponseEntity<>(itemMapper.itemsToItemDtos(itemService.getItemsByCategory(category)), HttpStatus.OK);
    }

    @PutMapping("/status/{status}")
    public ResponseEntity<List<ItemDto>> getItemByStatus(@PathVariable Item.ItemStatus status){
        return new ResponseEntity<>(itemMapper.itemsToItemDtos(itemService.getItemsByStatus(status)), HttpStatus.OK);
    }

    @PutMapping("/source/{source}")
    public ResponseEntity<List<ItemDto>> getItemBySource(@PathVariable Item.InwardSource source){
        return new ResponseEntity<>(itemMapper.itemsToItemDtos(itemService.getItemsBySource(source)), HttpStatus.OK);
    }


}
