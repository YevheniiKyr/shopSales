package com.example.shop.Repositories;

import com.example.shop.DTO.SetOfProducts;
import com.example.shop.Entities.ReceiptProduct;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;

import java.beans.Transient;
import java.util.*;

@Repository
public interface ReceiptProductRepository extends JpaRepository<ReceiptProduct, Long> {

    @Query("SELECT SUM(rp.quantity) FROM ReceiptProduct rp")
    Optional<Long> sumQuantities();

    @Query("SELECT SUM(rp.quantity * rp.pricePerUnit) FROM ReceiptProduct rp")
    Optional<Long> totalProductsCost();
    @Query("SELECT SUM(r.totalPrice) FROM Receipt r")
    Optional<Long> totalProductsCostFromTotalField();
    @Query("SELECT SUM(rp.quantity * rp.pricePerUnit) FROM ReceiptProduct rp JOIN rp.receipt r WHERE r.date > :startDate AND r.date < :endDate")
    Optional<Long> totalProductsCostForPeriod(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT SUM(rp.quantity * rp.pricePerUnit) FROM ReceiptProduct rp JOIN rp.receipt r WHERE r.date > :startDate AND r.date < :endDate AND rp.product.id = :productID AND r.store.id = :storeID")
    Optional<Long> costOfProductAInStoreBForPeriodC(
            @Param("productID") Long productID,
            @Param("storeID") Long storeID,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    @Query("SELECT SUM(rp.quantity * rp.pricePerUnit) FROM ReceiptProduct rp JOIN rp.receipt r WHERE r.date > :startDate AND r.date < :endDate AND rp.product.id = :productID")
    Optional<Long> costOfProductAForPeriodC(
            @Param("productID") Long productID,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );


    @Query(value = """
            SELECT  rp1.product_id AS product_id_1, rp2.product_id AS product_id_2, COUNT(*) AS times_bought_together
            FROM receipt_product rp1
            JOIN receipt_product rp2 ON rp1.receipt_id = rp2.receipt_id AND rp1.product_id < rp2.product_id
            JOIN receipt ON receipt.id = rp1.receipt_id
            WHERE receipt.date > :startDate AND receipt.date < :endDate
            GROUP BY rp1.product_id, rp2.product_id
            ORDER BY times_bought_together DESC
            LIMIT 10
            """,
            nativeQuery = true)
    Optional<List<Object[]>> top10PairsOfProductsForPeriodC(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    @Query(value = """

            SELECT
                        rp1.product_id AS product_id_1,
                        rp2.product_id AS product_id_2,
                        rp3.product_id AS product_id_3,
                        COUNT(*) AS times_bought_together
                    FROM
                        receipt_product rp1
                    JOIN
                        receipt_product rp2 ON rp1.receipt_id = rp2.receipt_id AND rp1.product_id < rp2.product_id
                    JOIN
                        receipt_product rp3 ON rp1.receipt_id = rp3.receipt_id AND rp2.product_id < rp3.product_id
                    JOIN
                        receipt ON receipt.id = rp1.receipt_id
                    WHERE
                        receipt.date > :startDate AND receipt.date < :endDate
                    GROUP BY
                        rp1.product_id, rp2.product_id, rp3.product_id
                    ORDER BY
                        times_bought_together DESC
                    LIMIT 10
                
            """,
            nativeQuery = true)
    Optional<List<Object[]>> top10TriosOfProductsForPeriodC(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );
    @Query(value = """
            SELECT\s
                    rp1.product_id AS product_id_1,
                    rp2.product_id AS product_id_2,
                    rp3.product_id AS product_id_3,
                    rp4.product_id AS product_id_4,
                    COUNT(*) AS times_bought_together
                FROM
                    receipt_product rp1
                JOIN
                    receipt_product rp2 ON rp1.receipt_id = rp2.receipt_id AND rp1.product_id < rp2.product_id
                JOIN
                    receipt_product rp3 ON rp1.receipt_id = rp3.receipt_id AND rp2.product_id < rp3.product_id
                JOIN
                    receipt_product rp4 ON rp1.receipt_id = rp4.receipt_id AND rp3.product_id < rp4.product_id
                JOIN
                    receipt ON receipt.id = rp1.receipt_id
                WHERE
                    receipt.date > :startDate AND receipt.date < :endDate
                GROUP BY
                    rp1.product_id, rp2.product_id, rp3.product_id, rp4.product_id
                ORDER BY
                    times_bought_together DESC
                LIMIT 10
                
            """,
            nativeQuery = true)
    Optional<List<Object[]>> top10QuadrosOfProductsForPeriodC(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    @Modifying
    @Transactional
    @Query( value = """
    INSERT INTO receipt_product VALUES (:id, :pricePerUnit, :quantity, :productID, :receiptID) 
    """, nativeQuery = true)
    void saveNative(
            @Param("id") long newReceiptProductID,
            @Param("pricePerUnit") int pricePerUnit,
            @Param("quantity") int quantity,
            @Param("productID") int productID,
            @Param("receiptID") long receiptID);


}


//