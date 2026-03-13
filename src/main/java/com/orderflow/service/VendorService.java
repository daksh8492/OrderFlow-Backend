package com.orderflow.service;

import com.orderflow.entity.vendor.Vendor;
import com.orderflow.exceptions.UserNotFoundException;
import com.orderflow.exceptions.VendorNotFoundException;
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

    public Vendor addVendor(Vendor vendor){
        long count = vendorRepo.count();
        String code;
        do{
            code = "VND-"+String.format("%04d", count+1);
            count++;
        }while (vendorRepo.existsByVendorCode(code));
        vendor.setVendorCode(code);
        return vendorRepo.save(vendor);
    }

    public List<Vendor> getAllVendors(){return vendorRepo.findAll();}

    public Vendor getVendorByCode(String code){
        Optional<Vendor> vendor = vendorRepo.findByVendorCode(code);
        return vendor.orElseThrow(()-> new VendorNotFoundException("Vendor not found"));
    }

    public Vendor getVendorById(UUID id){
        Optional<Vendor> vendor = vendorRepo.findById(id);
        return vendor.orElseThrow(()-> new VendorNotFoundException("Vendor not found"));
    }

    public Vendor updateVendor(UUID id, Vendor vendor){
        Vendor existingVendor = vendorRepo.findById(id).orElseThrow(()-> new VendorNotFoundException("Vendor not found"));
        BeanUtils.copyProperties(vendor, existingVendor, "vendorId", "vendorCode");
        return vendorRepo.save(existingVendor);
    }

    public void deleteVendor(UUID id){
        Vendor vendor = vendorRepo.findById(id).orElseThrow(()-> new VendorNotFoundException("Vendor not found"));
        vendorRepo.delete(vendor);
    }
}
