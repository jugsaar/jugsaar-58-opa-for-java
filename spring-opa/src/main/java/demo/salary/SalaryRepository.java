package demo.salary;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface SalaryRepository extends Repository<Salary, Long> {
    Optional<Salary> findByUsername(String username);
}
