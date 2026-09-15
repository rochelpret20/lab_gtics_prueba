package com.pucp.gtics.lab2.controller;

import com.pucp.gtics.lab2.model.Employee;
import com.pucp.gtics.lab2.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public String listar(Model model) {
        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        return "employees/list";
    }

    @GetMapping("/ver")
    public String ver(@RequestParam("id") Integer id, Model model) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        model.addAttribute("employee", employee);
        return "employees/detalle";
    }

    @GetMapping("/buscar")
    public String buscar(
            @RequestParam(value = "texto", required = false) String texto,
            @RequestParam(value = "puesto", required = false) String puesto,
            @RequestParam(value = "salarioMin", required = false) BigDecimal salarioMin,
            Model model) {
        List<Employee> resultado = employeeRepository.findAll();
        if (texto != null && !texto.isBlank()) {
            String t = texto.toLowerCase();
            resultado = resultado.stream()
                    .filter(e ->
                            (e.getFirstName() != null &&
                                    e.getFirstName().toLowerCase().contains(t)) ||
                                    (e.getLastName() != null &&
                                            e.getLastName().toLowerCase().contains(t)) ||
                                    (e.getEmail() != null &&
                                            e.getEmail().toLowerCase().contains(t)))
                    .collect(Collectors.toList());
        }
        if (puesto != null && !puesto.isBlank()) {
            resultado = resultado.stream()
                    .filter(e -> e.getJobId() != null &&
                            e.getJobId().equalsIgnoreCase(puesto))
                    .collect(Collectors.toList());
        }
        if (salarioMin != null) {
            resultado = resultado.stream()
                    .filter(e -> e.getSalary() != null &&
                            e.getSalary().compareTo(salarioMin) >= 0)
                    .collect(Collectors.toList());
        }
        model.addAttribute("employees", resultado);
        model.addAttribute("texto", texto);
        model.addAttribute("puesto", puesto);
        model.addAttribute("salarioMin", salarioMin);
        return "employees/list";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("employee", new Employee());
        return "employees/form";
    }

    @PostMapping("/guardar")
    public String guardar(Employee employee) {
        if (employee.getEmployeeId() == null) {
            Integer maxId = employeeRepository.findAll().stream()
                    .map(Employee::getEmployeeId)
                    .max(Integer::compareTo)
                    .orElse(0);
            employee.setEmployeeId(maxId + 1);
        }
        employeeRepository.save(employee);
        return "redirect:/employees";
    }

    @GetMapping("/editar")
    public String formularioEditar(@RequestParam("id") Integer id, Model model) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        model.addAttribute("employee", employee);
        return "employees/form";
    }
}