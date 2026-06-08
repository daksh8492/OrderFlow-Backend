package com.orderflow.service;

import com.orderflow.repository.picking.PickingItemRepo;
import com.orderflow.repository.picking.PickingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PickingService {

    @Autowired
    private PickingRepo pickingRepo;

    @Autowired
    private PickingItemRepo pickingItemRepo;



}
