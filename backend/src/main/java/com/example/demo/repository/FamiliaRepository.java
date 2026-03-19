package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Familia;

public interface FamiliaRepository extends JpaRepository<Familia, Long> {
}