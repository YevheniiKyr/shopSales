package com.example.shop;

import com.example.shop.Controllers.MainController;
import com.example.shop.Repositories.ReceiptProductRepository;
import com.example.shop.Repositories.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class ShopApplication implements CommandLineRunner {

    private final ReceiptProductRepository receiptProductRepository;
    private final ReceiptRepository receiptRepository;

    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }
    private final MainController mainController;

    @Override
    public void run(String... args) throws Exception {

//        mainController.insertReceipts(10000);
        long startTime = System.nanoTime();
        long amount = receiptProductRepository.sumQuantities().orElse(0L);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        System.out.println("1 query " + duration);

        startTime = System.nanoTime();
        long cost = receiptProductRepository.totalProductsCost().orElse(0L);
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1_000_000;
        System.out.println("2 query " + duration);


        Date from = Date.valueOf(LocalDate.of(2020, 1, 1));
        Date to = Date.valueOf(LocalDate.of(2024, 1, 1));
        Long productID = 1L;
        Long storeID = 1L;

         startTime = System.nanoTime();
         cost = receiptProductRepository.totalProductsCostForPeriod(from, to).orElse(0L);
         endTime = System.nanoTime();
         duration = (endTime - startTime) / 1_000_000;
        System.out.println("3 query " + duration);

        startTime = System.nanoTime();
        cost = receiptProductRepository.costOfProductAInStoreBForPeriodC(productID, storeID, from, to).orElse(0L);
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1_000_000;
        System.out.println("4 query " + duration);


        startTime = System.nanoTime();
        cost = receiptProductRepository.costOfProductAForPeriodC(productID, from, to).orElse(0L);
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1_000_000;
        System.out.println("5 query " + duration);


        startTime = System.currentTimeMillis();
        receiptProductRepository.top10PairsOfProductsForPeriodC(from, to);
        endTime = System.currentTimeMillis();
        duration = (endTime - startTime);
        System.out.println("6 query " + duration);


        startTime = System.currentTimeMillis();
        receiptProductRepository.top10TriosOfProductsForPeriodC(from, to).orElse(new ArrayList<>());
        endTime = System.currentTimeMillis();
        duration = (endTime - startTime);
        System.out.println("7 query " + duration);

        startTime = System.currentTimeMillis();
        receiptProductRepository.top10QuadrosOfProductsForPeriodC(from, to).orElse(new ArrayList<>());
        endTime = System.currentTimeMillis();
        duration = (endTime - startTime);
        System.out.println("8 query " + duration);

    }
}
