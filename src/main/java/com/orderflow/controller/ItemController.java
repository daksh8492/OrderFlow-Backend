package com.orderflow.controller;

import com.orderflow.dto.ItemDto;
import com.orderflow.dto.ItemSummaryDto;
import com.orderflow.entity.product.Item;
import com.orderflow.mapper.ItemMapper;
import com.orderflow.service.ItemService;
import org.aspectj.weaver.patterns.ITokenSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public ResponseEntity<ItemDto> createItem(@RequestBody ItemDto itemDto) {
        return new ResponseEntity<>(itemService.addItem(itemDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable UUID id) {
        return new ResponseEntity<>(itemService.getItemById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER')")
    public ResponseEntity<ItemDto> updateItem(@PathVariable UUID id, @RequestBody ItemDto itemDto) {
        return new ResponseEntity<>(itemService.updateItem(id, itemDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> deleteItem(@PathVariable UUID id) {
        itemService.deleteItem(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER')")
    public ResponseEntity<ItemDto> activateItem(@PathVariable UUID id) {
        return new ResponseEntity<>(itemService.activateItem(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER')")
    public ResponseEntity<ItemDto> deactivateItem(@PathVariable UUID id) {
        return new ResponseEntity<>(itemService.deactivateItem(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}/discontinue")
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER')")
    public ResponseEntity<ItemDto> discontinueItem(@PathVariable UUID id) {
        return new ResponseEntity<>(itemService.discontinueItem(id), HttpStatus.OK);
    }

//    @GetMapping
//    public ResponseEntity<Page<ItemSummaryDto>> getAllItems(@PageableDefault(size = 20) Pageable pageable) {
//        return ResponseEntity.ok(itemService.getAllItems(pageable));
//    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<ItemSummaryDto>> getItemsByCategory(@PathVariable Item.ItemCategory category, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(itemService.getItemsByCategory(category, pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<ItemSummaryDto>> getItemByStatus(@PathVariable Item.ItemStatus status, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(itemService.getItemsByStatus(status, pageable));
    }

    @GetMapping("/source/{source}")
    public ResponseEntity<Page<ItemSummaryDto>> getItemBySource(@PathVariable Item.InwardSource source, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(itemService.getItemsBySource(source, pageable));
    }

    @GetMapping
    public Page<ItemSummaryDto> getItems(@RequestParam(required = false) String search, @RequestParam(required = false) Item.ItemCategory category, @RequestParam(required = false) Item.ItemStatus status, @RequestParam(required = false) Item.InwardSource sourceType, @PageableDefault(size = 20) Pageable pageable) {
        return itemService.searchItems(search, category, status, sourceType, pageable);
    }

}
