package com.orderflow.controller;

import com.orderflow.dto.ItemDto;
import com.orderflow.entity.product.Item;
import com.orderflow.mapper.ItemMapper;
import com.orderflow.service.ItemService;
import org.aspectj.weaver.patterns.ITokenSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/items")
@PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER','WAREHOUSE_OPERATOR')")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER')")
    public ResponseEntity<ItemDto> createItem(@RequestBody ItemDto itemDto){
        return new ResponseEntity<>(itemService.addItem(itemDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable UUID id){
        return new ResponseEntity<>(itemService.getItemById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER')")
    public ResponseEntity<ItemDto> updateItem(@PathVariable UUID id, @RequestBody ItemDto itemDto){
        return new ResponseEntity<>(itemService.updateItem(id, itemDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> deleteItem(@PathVariable UUID id){
        itemService.deleteItem(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER')")
    public ResponseEntity<ItemDto> activateItem(@PathVariable UUID id){
        return new ResponseEntity<>(itemService.activateItem(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER')")
    public ResponseEntity<ItemDto> deactivateItem(@PathVariable UUID id){
        return new ResponseEntity<>(itemService.deactivateItem(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/discontinue")
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER')")
    public ResponseEntity<ItemDto> discontinueItem(@PathVariable UUID id){
        return new ResponseEntity<>(itemService.discontinueItem(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<ItemDto>> getAllItems(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(itemService.getAllItems(pageable));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<ItemDto>> getItemsByCategory(@PathVariable Item.ItemCategory category, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(itemService.getItemsByCategory(category, pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<ItemDto>> getItemByStatus(@PathVariable Item.ItemStatus status, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(itemService.getItemsByStatus(status, pageable));
    }

    @GetMapping("/source/{source}")
    public ResponseEntity<Page<ItemDto>> getItemBySource(@PathVariable Item.InwardSource source, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(itemService.getItemsBySource(source, pageable));
    }


}
