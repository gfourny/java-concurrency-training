package fr.concurrency.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.concurrency.training.model.Customer;

/**
 * @author gfourny
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
