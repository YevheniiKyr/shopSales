package com.example.shop.Repositories;

import com.example.shop.Entities.Receipt;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    @Modifying
    @Transactional
    @Query( value = """
    INSERT INTO receipt VALUES (:id, :date, :total, :storeID) 
    """, nativeQuery = true)
    void saveNative(
            @Param("id") long newReceiptID,
            @Param("date") Date date,
            @Param("total") int total,
            @Param("storeID") int storeID);
}
