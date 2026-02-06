package com.pm.inventoryservice.repository;

import com.pm.inventoryservice.model.Inventory;
import org.springframework.boot.autoconfigure.container.ContainerImageMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {


    List<Inventory>  findBySkuCodeIn(List<String> skucode);
}
