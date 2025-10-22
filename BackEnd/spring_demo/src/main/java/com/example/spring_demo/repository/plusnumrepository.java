package com.example.spring_demo.repository;
import com.example.spring_demo.entity.plusnumentity;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spring_demo.entity.plusnumentity;
public interface plusnumrepository extends JpaRepository<plusnumentity,Long> {
}
