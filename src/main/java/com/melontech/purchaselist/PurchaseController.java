package com.melontech.purchaselist;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @GetMapping
    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Purchase> getPurchaseById(@PathVariable UUID id) {
        Optional<Purchase> purchase = purchaseRepository.findById(id);
        return purchase.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Purchase> cancelPurchase(@PathVariable UUID id) {
        Optional<Purchase> optionalPurchase = purchaseRepository.findById(id);

        if (optionalPurchase.isPresent()) {
            Purchase purchase = optionalPurchase.get();
            if ("PENDING".equals(purchase.getStatus())) {
                purchase.setStatus("CANCELLED");
                return ResponseEntity.ok(purchaseRepository.save(purchase));
            }
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.notFound().build();
    }
    @PatchMapping("/{id}/uncancel")
    public ResponseEntity<Purchase> uncancelPurchase(@PathVariable UUID id) {
        Optional<Purchase> optionalPurchase = purchaseRepository.findById(id);

        if (optionalPurchase.isPresent()) {
            Purchase purchase = optionalPurchase.get();
            if ("CANCELLED".equals(purchase.getStatus())) {
                purchase.setStatus("PENDING");
                return ResponseEntity.ok(purchaseRepository.save(purchase));
            }
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.notFound().build();
}
}
