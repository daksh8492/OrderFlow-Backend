package com.orderflow.service;

import com.orderflow.dto.VendorDto;
import com.orderflow.entity.vendor.Vendor;
import com.orderflow.exceptions.UserNotFoundException;
import com.orderflow.exceptions.VendorNotFoundException;
import com.orderflow.mapper.VendorMapper;
import com.orderflow.repository.vendor.VendorRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VendorService {

    @Autowired
    private VendorRepo vendorRepo;
    @Autowired
    private VendorMapper vendorMapper;

    public VendorDto addVendor(VendorDto vendorDto){
        Vendor vendor = vendorMapper.vendorDtoToVendor(vendorDto);
        long count = vendorRepo.count();
        String code;
        do{
            code = "VND-"+String.format("%04d", count+1);
            count++;
        }while (vendorRepo.existsByVendorCode(code));
        vendor.setVendorCode(code);
        return vendorMapper.vendorToVendorDto(vendorRepo.save(vendor));
    }

    public List<VendorDto> getAllVendors(){return vendorMapper.vendorsToVendorDtos(vendorRepo.findAll());}

    public VendorDto getVendorByCode(String code){
        Optional<Vendor> vendor = vendorRepo.findByVendorCode(code);
        return vendorMapper.vendorToVendorDto(vendor.orElseThrow(()-> new VendorNotFoundException("Vendor not found")));
    }

    public VendorDto getVendorById(UUID id){
        Optional<Vendor> vendor = vendorRepo.findById(id);
        return vendorMapper.vendorToVendorDto(vendor.orElseThrow(()-> new VendorNotFoundException("Vendor not found")));
    }

    public VendorDto updateVendor(UUID id, VendorDto vendordDto){
        Vendor vendor = vendorMapper.vendorDtoToVendor(vendordDto);
        Vendor existingVendor = vendorRepo.findById(id).orElseThrow(()-> new VendorNotFoundException("Vendor not found"));
        BeanUtils.copyProperties(vendor, existingVendor, "vendorId", "vendorCode");
        if (vendor.getVendorName() != null) existingVendor.setVendorName(vendor.getVendorName());
        if (vendor.getAddress() != null) existingVendor.setAddress(vendor.getAddress());
        if (vendor.getTRN() != null) existingVendor.setTRN(vendor.getTRN());
        if (vendor.getCurrency() != null) existingVendor.setCurrency(vendor.getCurrency());
        if (vendor.getCity() != null) existingVendor.setCity(vendor.getCity());
        if (vendor.getVendorBrand() != null) existingVendor.setVendorBrand(vendor.getVendorBrand());
        if (vendor.getContactNumber() != null) existingVendor.setContactNumber(vendor.getContactNumber());
        if (vendor.getContactTelephone() != null) existingVendor.setContactTelephone(vendor.getContactTelephone());
        if (vendor.getPaymentTerms() != null) existingVendor.setPaymentTerms(vendor.getPaymentTerms());
        if (vendor.getMinimumOrderValue() != null) existingVendor.setMinimumOrderValue(vendor.getMinimumOrderValue());
        if (vendor.getStatus() != null) existingVendor.setStatus(vendor.getStatus());
        return vendorMapper.vendorToVendorDto(vendorRepo.save(existingVendor));
    }

    public void deleteVendor(UUID id){
        Vendor vendor = vendorRepo.findById(id).orElseThrow(()-> new VendorNotFoundException("Vendor not found"));
        vendorRepo.delete(vendor);
    }
}
