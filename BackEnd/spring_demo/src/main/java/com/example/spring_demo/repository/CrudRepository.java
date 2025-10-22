package com.example.spring_demo.repository;
import com.example.spring_demo.entity.CrudEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrudRepository extends JpaRepository<CrudEntity,Long> {
}




