package com.example.shop.utils;

import com.example.shop.Repositories.ReceiptProductRepository;
import com.example.shop.Repositories.ReceiptRepository;
import lombok.RequiredArgsConstructor;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Random;

@RequiredArgsConstructor
public class CSVGenerator {
    private static final String RECEIPTS_CSV_FILE = "./receipts.csv";
    private static final String RECEIPT_PRODUCTS_CSV_FILE = "./receipt_products.csv";
    private static final Random random = new Random();


    public static void main(String[] args) {
        try {
            FileWriter receiptsWriter = new FileWriter(RECEIPTS_CSV_FILE);
            FileWriter receiptProductsWriter = new FileWriter(RECEIPT_PRODUCTS_CSV_FILE);

            // Write headers
            receiptsWriter.append("receipt_id,date,total,store_id\n");
            receiptProductsWriter.append("receipt_product_id,price,quantity,product_id,receipt_id\n");

            // Assume 10 products with random prices


            long newReceiptID = 1;
            long newReceiptProductID = 1;

            int amount = 10000; // Total receipts to generate
            for (int i = 0; i < amount; i++) {
                if (i % 1000 == 0) {
                    System.out.println(i);
                }

                int total = 0;
                int amountOfProductsInReceipt = 1 + random.nextInt(6);

                int storeID = 1 + random.nextInt(2);
                for (int j = 0; j < amountOfProductsInReceipt; j++) {
                    int price = 1 + random.nextInt(6);
                    int quantity = 1 + random.nextInt(6);
                    int productID = 1 + random.nextInt(10);

                    total += price * quantity;

                    receiptProductsWriter.append(String.format("%d,%d,%d,%d,%d\n", newReceiptProductID, price, quantity, productID, newReceiptID));
                    newReceiptProductID++;
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // For date only. Use "yyyy-MM-dd HH:mm:ss" for datetime
                Date date = RandomDateGenerator.generate();
                String formattedDate = dateFormat.format(date);

                receiptsWriter.append(String.format("%d,%s,%d,%d\n", newReceiptID, formattedDate, total, storeID));
                newReceiptID++;
            }

            receiptsWriter.flush();
            receiptsWriter.close();
            receiptProductsWriter.flush();
            receiptProductsWriter.close();

            System.out.println("CSV generation complete.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

