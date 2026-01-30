package com.garage.system.service;

import com.garage.system.model.SparePart;
import com.garage.system.repository.SparePartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private SparePartRepository sparePartRepository;

    public List<SparePart> getAllParts() {
        return sparePartRepository.findAll();
    }

    public SparePart addPart(SparePart part) {
        return sparePartRepository.save(part);
    }
}
