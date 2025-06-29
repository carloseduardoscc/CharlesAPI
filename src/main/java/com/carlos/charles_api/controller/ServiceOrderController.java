package com.carlos.charles_api.controller;

import com.carlos.charles_api.dto.request.OpenServiceOrderRequestDTO;
import com.carlos.charles_api.dto.response.ServiceOrderDetailsDTO;
import com.carlos.charles_api.dto.response.ServiceOrderSummaryDTO;
import com.carlos.charles_api.service.ServiceOrderService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/serviceorder")
public class ServiceOrderController {

    @Autowired
    ServiceOrderService  service;

    @Transactional
    @PostMapping()
    public ResponseEntity open(@RequestBody @Valid OpenServiceOrderRequestDTO soData){
        Long osId = service.openNewServiceOrder(soData);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(osId)
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping()
    public ResponseEntity<List<ServiceOrderSummaryDTO>> listServiceOrders() {
        List<ServiceOrderSummaryDTO> serviceOrders = service.listServiceOrders();
        return ResponseEntity.ok(serviceOrders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceOrderDetailsDTO> serviceOrderDetails(@PathVariable Long id) {
        ServiceOrderDetailsDTO serviceOrderDetailsDTO = service.serviceOrderDetails(id);
        return ResponseEntity.ok(serviceOrderDetailsDTO);
    }
}
