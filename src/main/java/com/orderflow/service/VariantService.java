package com.orderflow.service;

import com.orderflow.dto.VariantDto;
import com.orderflow.entity.product.Item;
import com.orderflow.entity.product.Variant;
import com.orderflow.exceptions.VariantNotFoundException;
import com.orderflow.mapper.ItemMapper;
import com.orderflow.mapper.VariantMapper;
import com.orderflow.repository.product.ItemRepo;
import com.orderflow.repository.product.VariantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VariantService {

    @Autowired
    private VariantRepo variantRepo;

    @Autowired
    private VariantMapper variantMapper;

    @Autowired
    private ItemRepo itemRepo;

    public VariantDto addVariant(VariantDto variantDto) {
        if (variantDto.getItemId() == null) {
            throw new IllegalArgumentException("Item ID is required");
        }

        Item item = itemRepo.findById(variantDto.getItemId()).orElseThrow(() -> new IllegalArgumentException("Item not found"));

        Variant variant = variantMapper.variantDtoToVariant(variantDto);
        variant.setItem(item);

        if (variant.getStatus() == null) {
            variant.setStatus(Variant.VariantStatus.ACTIVE);
        }

        variant.setSku(generateSku());
        variant.setBarcode(generateBarcode());

        return variantMapper.variantToVariantDto(variantRepo.save(variant));
    }

    public List<VariantDto> getAllVariants() {
        return variantMapper.variantsToVariantDtos(variantRepo.findAll());
    }

    public VariantDto getVariantById(UUID id) {
        return variantMapper.variantToVariantDto(variantRepo.findById(id).orElseThrow(() -> new VariantNotFoundException("Variant not found")));
    }

    public VariantDto updateVariant(UUID id, VariantDto variantDto) {
        Variant existingVariant = variantRepo.findById(id).orElseThrow(() -> new VariantNotFoundException("Variant not found"));

        if (variantDto.getName() != null) existingVariant.setName(variantDto.getName());
        if (variantDto.getSellingPrice() != null) existingVariant.setSellingPrice(variantDto.getSellingPrice());
        if (variantDto.getPurchasePrice() != null) existingVariant.setPurchasePrice(variantDto.getPurchasePrice());
        if (variantDto.getAttributes() != null) existingVariant.setAttributes(variantDto.getAttributes());
        if (variantDto.getImageUrls() != null) existingVariant.setImageUrls(variantDto.getImageUrls());
        if (variantDto.getStatus() != null) existingVariant.setStatus(variantDto.getStatus());

        return variantMapper.variantToVariantDto(variantRepo.save(existingVariant));
    }

    public void deleteVariant(UUID id) {
        Variant existingVariant = variantRepo.findById(id).orElseThrow(() -> new VariantNotFoundException("Variant not found"));

        if (existingVariant.getStatus() != Variant.VariantStatus.DISCONTINUED)
            throw new IllegalStateException("Variant is not discontinued");
        variantRepo.delete(existingVariant);
    }

    public VariantDto getVariantByBarcode(String barcode) {
        return variantMapper.variantToVariantDto(variantRepo.findByBarcode(barcode).orElseThrow(() -> new VariantNotFoundException("Variant not found")));
    }

    public VariantDto getVariantBySku(String sku) {
        return variantMapper.variantToVariantDto(variantRepo.findBySkuIgnoreCase(sku).orElseThrow(() -> new VariantNotFoundException("Variant not found")));
    }

    public List<VariantDto> getVariantsByItemId(UUID id) {
        return variantMapper.variantsToVariantDtos(variantRepo.findByItem_ItemId(id));
    }

    public VariantDto activateVariant(UUID id) {
        Variant variant = variantRepo.findById(id).orElseThrow(() -> new VariantNotFoundException("Variant not found"));
        variant.setStatus(Variant.VariantStatus.ACTIVE);
        return variantMapper.variantToVariantDto(variantRepo.save(variant));
    }

    public VariantDto deactivateVariant(UUID id) {
        Variant variant = variantRepo.findById(id).orElseThrow(() -> new VariantNotFoundException("Variant not found"));
        variant.setStatus(Variant.VariantStatus.INACTIVE);
        return variantMapper.variantToVariantDto(variantRepo.save(variant));
    }

    public VariantDto discontinueVariant(UUID id) {
        Variant variant = variantRepo.findById(id).orElseThrow(() -> new VariantNotFoundException("Variant not found"));
        variant.setStatus(Variant.VariantStatus.DISCONTINUED);
        return variantMapper.variantToVariantDto(variantRepo.save(variant));
    }

    private String generateSku() {
        String sku;
        do {
            String rdm = UUID.randomUUID().toString().replace("-", "").substring(0, 5).toUpperCase();
            sku = "ITM" + rdm;
        } while (variantRepo.existsBySku(sku));
        return sku;
    }

    private String generateBarcode() {
        String barcode;
        do {
            long nm = (long) (Math.random() * 1_000_000_000_000L);
            barcode = String.format("%012d", nm);
        } while (variantRepo.existsByBarcode(barcode));
        return barcode;
    }
}
