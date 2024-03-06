package com.example.shop.Controllers;

import com.example.shop.DTO.ResponseDTO;
import com.example.shop.DTO.SetOfProducts;
import com.example.shop.Entities.Receipt;
import com.example.shop.Entities.ReceiptProduct;
import com.example.shop.Repositories.ReceiptProductRepository;
import com.example.shop.Repositories.ReceiptRepository;
import com.example.shop.utils.RandomDateGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class MainController {

    private final ReceiptProductRepository receiptProductRepository;
    private final ReceiptRepository receiptRepository;

    //http://localhost:8080/insert_receipts?amount=100
    @GetMapping("/insert_receipts")
    public ResponseEntity insertReceipts(@RequestParam int amount) {
        long newReceiptID = receiptRepository.count() + 1;
        long newReceiptProductID = receiptProductRepository.count() + 1;

        for (int i = 0; i < amount; i++) {
            if(i%1000 == 0) System.out.println(i);
            int total = 0;
            int amountOfProductsInReceipt = (int) (Math.random() * 6 + 1);
            int[] productsPrices = new int[amountOfProductsInReceipt];
            int[] productIDS = new int[amountOfProductsInReceipt];
            int[] quantities = new int[amountOfProductsInReceipt];

            for (int j = 0; j < amountOfProductsInReceipt; j++) {
                int price = (int) (Math.random() * 6 + 1);
                productsPrices[j] = price;
            }

            for (int j = 0; j < amountOfProductsInReceipt; j++) {
                int quantity = (int) (Math.random() * 6 + 1);
                quantities[j] = quantity;
                total += productsPrices[j] * quantity;
            }

            int storeID = (int) (Math.random() * 2) + 1;

            for (int j = 0; j < amountOfProductsInReceipt; j++) {
                int productID = (int) (Math.random() * 9) + 1;
                productIDS[j] = productID;
            }

            Date date = RandomDateGenerator.generate();
            receiptRepository.saveNative(newReceiptID, date, total, storeID);
            for (int m = 0; m < productsPrices.length; m++) {
                receiptProductRepository.saveNative(newReceiptProductID, productsPrices[m], quantities[m], productIDS[m], newReceiptID);
                newReceiptProductID++;

            }
            newReceiptID++;
        }

        return ResponseEntity.ok("inserted");
    }
    //http://localhost:8080/sold_products_amount
    @GetMapping("/sold_products_amount")
    public ResponseEntity<ResponseDTO<Long>> getSoldProductsAmount() {
        long startTime = System.nanoTime();
        long amount = receiptProductRepository.sumQuantities().orElse(0L);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        ResponseDTO<Long> response = new ResponseDTO<>();
        response.setData(amount);
        response.setRequestDurationInMs(duration);
        return ResponseEntity.ok(response);
    }

    //http://localhost:8080/sold_products_cost
    @GetMapping("/sold_products_cost")
    public ResponseEntity<ResponseDTO<Long>> getSoldProductsCost() {
        long startTime = System.nanoTime();
        long cost = receiptProductRepository.totalProductsCost().orElse(0L);
        ;
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        ResponseDTO<Long> response = new ResponseDTO<>();
        response.setData(cost);
        response.setRequestDurationInMs(duration);
        return ResponseEntity.ok(response);
    }

//http://localhost:8080/sold_products_cost_total_field
    @GetMapping("/sold_products_cost_total_field")
    public ResponseEntity<ResponseDTO<Long>> getSoldProductsCostFromTotalField() {
        long startTime = System.nanoTime();
        long cost = receiptProductRepository.totalProductsCostFromTotalField().orElse(0L);
        ;
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        ResponseDTO<Long> response = new ResponseDTO<>();
        response.setData(cost);
        response.setRequestDurationInMs(duration);
        return ResponseEntity.ok(response);
    }

    //http://localhost:8080/sold_products_cost_for_a_period_of_time?from=2023-01-01&to=2023-05-05
    @GetMapping("/sold_products_cost_for_a_period_of_time")
    public ResponseEntity<ResponseDTO<Long>> getSoldProductsCostForPeriod(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to
    ) {
        long startTime = System.nanoTime();
        long cost = receiptProductRepository.totalProductsCostForPeriod(from, to).orElse(0L);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        ResponseDTO<Long> response = new ResponseDTO<>();
        response.setData(cost);
        response.setRequestDurationInMs(duration);
        return ResponseEntity.ok(response);
    }

    //http://localhost:8080/product_A_in_store_B_for_period_C?productID=1&storeID=1&from=2023-01-01&to=2023-05-05
    @GetMapping("/product_A_in_store_B_for_period_C")
    public ResponseEntity<ResponseDTO<Long>> getAmountOfProductAInStoreBForPeriodC(
            @RequestParam Long productID,
            @RequestParam Long storeID,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to
    ) {
        long startTime = System.nanoTime();
        long cost = receiptProductRepository.costOfProductAInStoreBForPeriodC(productID, storeID, from, to).orElse(0L);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        ResponseDTO<Long> response = new ResponseDTO<>();
        response.setData(cost);
        response.setRequestDurationInMs(duration);
        return ResponseEntity.ok(response);
    }

    //http://localhost:8080/product_A_for_period_C?productID=1&from=2022-02-02&to=2024-02-02
    @GetMapping("/product_A_for_period_C")
    public ResponseEntity<ResponseDTO<Long>> getAmountOfProductAForPeriodC(
            @RequestParam Long productID,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to
    ) {
        long startTime = System.nanoTime();
        long cost = receiptProductRepository.costOfProductAForPeriodC(productID, from, to).orElse(0L);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        ResponseDTO<Long> response = new ResponseDTO<>();
        response.setData(cost);
        response.setRequestDurationInMs(duration);
        return ResponseEntity.ok(response);
    }

    //http://localhost:8080/top_10_pairs_for_period_C?from=2022-02-02&to=2024-02-02
    @GetMapping("/top_10_pairs_for_period_C")
    public ResponseEntity<ResponseDTO<List<SetOfProducts>>> top10PairsOfProductsForPeriodC(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to
    ) {
        long startTime = System.currentTimeMillis();
        List<Object[]> res = receiptProductRepository.top10PairsOfProductsForPeriodC(from, to).orElse(new ArrayList<>());
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime) ;
        List<SetOfProducts> setOfProductsList = res.stream()
                .map(result -> new SetOfProducts(new Long[]{(Long) result[0], (Long) result[1]}, (Long) result[2]))
                .collect(Collectors.toList());
        ResponseDTO<List<SetOfProducts>> response = new ResponseDTO<>();
        response.setData(setOfProductsList);
        response.setRequestDurationInMs(duration);
        return ResponseEntity.ok(response);
    }

    //http://localhost:8080/top_10_trios_for_period_C?from=2022-02-02&to=2024-02-02
    @GetMapping("/top_10_trios_for_period_C")
    public ResponseEntity<ResponseDTO<List<SetOfProducts>>> top10TriosOfProductsForPeriodC(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to
    ) {
        long startTime = System.currentTimeMillis();
        List<Object[]> res = receiptProductRepository.top10TriosOfProductsForPeriodC(from, to).orElse(new ArrayList<>());
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime) ;
        List<SetOfProducts> setOfProductsList = res.stream()
                .map(result -> new SetOfProducts(new Long[]{(Long) result[0], (Long) result[1], (Long) result[2]}, (Long) result[3]))
                .collect(Collectors.toList());
        ResponseDTO<List<SetOfProducts>> response = new ResponseDTO<>();
        response.setData(setOfProductsList);
        response.setRequestDurationInMs(duration);
        return ResponseEntity.ok(response);
    }

    //http://localhost:8080/top_10_quadros_for_period_C?from=2022-02-02&to=2024-02-02
    @GetMapping("/top_10_quadros_for_period_C")
    public ResponseEntity<ResponseDTO<List<SetOfProducts>>> top10QuadrosOfProductsForPeriodC(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to
    ) {
        long startTime = System.currentTimeMillis();
        List<Object[]> res = receiptProductRepository.top10QuadrosOfProductsForPeriodC(from, to).orElse(new ArrayList<>());
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime) ;
        List<SetOfProducts> setOfProductsList = res.stream()
                .map(result -> new SetOfProducts(new Long[]{(Long) result[0], (Long) result[1], (Long) result[2], (Long) result[3]}, (Long) result[4]))
                .collect(Collectors.toList());
        ResponseDTO<List<SetOfProducts>> response = new ResponseDTO<>();
        response.setData(setOfProductsList);
        response.setRequestDurationInMs(duration);
        return ResponseEntity.ok(response);
    }

}
