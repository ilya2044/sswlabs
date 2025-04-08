package com.creepins.homework_8.core.repository;

import com.creepins.homework_8.core.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
