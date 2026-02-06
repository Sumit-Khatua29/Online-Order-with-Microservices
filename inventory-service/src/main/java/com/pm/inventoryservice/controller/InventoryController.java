package com.pm.inventoryservice.controller;


import com.pm.inventoryservice.dto.InventoryResponse;
import com.pm.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isinStock(@RequestParam List<String> skuCode){
        return inventoryService.isInStock(skuCode);
    }
}
