package com.garage.system.controller;

import com.garage.system.model.SparePart;
import com.garage.system.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public List<SparePart> getAllParts() {
        return inventoryService.getAllParts();
    }

    @PostMapping
    public SparePart addPart(@RequestBody SparePart part) {
        return inventoryService.addPart(part);
    }
}
