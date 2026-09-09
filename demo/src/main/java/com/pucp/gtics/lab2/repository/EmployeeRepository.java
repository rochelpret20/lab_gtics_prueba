package com.pucp.gtics.lab2.repository;

import com.pucp.gtics.lab2.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
// JpaRepository<Employee, Integer> ya nos da gratis:
// findAll(), findById(Integer), save(Employee), deleteById(Integer)
// No hace falta escribir nada más para la Pregunta 1.

}