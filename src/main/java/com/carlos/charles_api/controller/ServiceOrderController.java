package com.carlos.charles_api.controller;

import com.carlos.charles_api.dto.request.OpenServiceOrderRequestDTO;
import com.carlos.charles_api.dto.response.ServiceOrderDetailsDTO;
import com.carlos.charles_api.dto.response.ServiceOrderStatistcsDTO;
import com.carlos.charles_api.dto.response.ServiceOrderSummaryDTO;
import com.carlos.charles_api.model.entity.ServiceOrder;
import com.carlos.charles_api.service.ServiceOrderService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
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

    @PostMapping("/{id}/assign")
    public ResponseEntity assign(@PathVariable Long id){
        service.assignOs(id);
        return ResponseEntity.ok("Order assigned!");
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity cancel(@PathVariable Long id){
        service.cancelOs(id);
        return ResponseEntity.ok("Order cancelado com sucesso!");
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity complete(@PathVariable Long id){
        service.completeOs(id);
        return ResponseEntity.ok("Ordem completado com sucesso!");
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

    @GetMapping("/statistcs")
    public ResponseEntity<ServiceOrderStatistcsDTO> serviceOrderDetails() {
        ServiceOrderStatistcsDTO response = service.serviceOrderStatistcsDTO();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{so_id}/report")
    public ResponseEntity<byte[]> downloadReport(@PathVariable Long so_id) {
        byte[] pdf = service.generateSoReport(so_id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition
                .builder("attachment")
                .filename("relatorio_os_.pdf")
                .build());

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}
