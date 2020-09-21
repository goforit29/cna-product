package com.example.product;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PolicyHandler {

    @Autowired
    ProductRepository productRepository;

    @StreamListener(Processor.INPUT)
    public void onEventByString(@Payload String productChanged){
        System.out.println(productChanged);
    }



    @StreamListener(Processor.INPUT)
    public void onEventByString(@Payload ProductChanged productChanged){
        if("ProductChanged".equals(productChanged.getEventType())){
            System.out.println("11"+ productChanged.getEventType());
            System.out.println("11"+ productChanged.getProductName());
            System.out.println("11"+ productChanged.getProductStock());
        }

    }
    @StreamListener(Processor.INPUT)
    public void onEventByObject(@Payload OrderPlaced orderPlaced){
        //orderplaced 데이터를 json->객체로파싱->해결
        if("OrderPlaced".equals(orderPlaced.getEventType())){

            /*
            Product p = new Product();
            p.setId((orderPlaced.getProductId()));
            p.setStock(orderPlaced.getQty());
            productRepository.save(p);
            */

            Optional<Product> productById = productRepository.findById(orderPlaced.getProductId());

            Product p = productById.get();
            p.setStock(p.getStock() - orderPlaced.getQty());
            productRepository.save(p);
        }

    }
}
