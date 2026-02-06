package com.pm.orderservice.service;


import com.pm.orderservice.dto.InventoryResponse;
import com.pm.orderservice.dto.OrderLineItemsDto;
import com.pm.orderservice.dto.OrderRequest;
import com.pm.orderservice.model.Order;
import com.pm.orderservice.model.OrderLineItems;
import com.pm.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest)
    {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(orderLineItemsDto -> (OrderLineItems) maptoDto(orderLineItemsDto))
                .toList();
        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(orderLineItem -> orderLineItem.getSkucode())
                .toList();

       InventoryResponse[] inventoryResponsesArray =  webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

       boolean allProductsInStock =  Arrays.stream(inventoryResponsesArray)
               .allMatch(inventoryResponse -> inventoryResponse.isInStock());

       if(allProductsInStock && inventoryResponsesArray.length == skuCodes.size()){
           orderRepository.save(order);
       } else {
           throw new IllegalArgumentException("Product is not in stock, please try again later");
       }
    }

    private Object maptoDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkucode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }

}
