package demo.salary;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/salary")
@RequiredArgsConstructor
class SalaryController {

    private final SalaryFacade salaryFacade;

    @GetMapping("/{username}")
    public Salary getSalary(@PathVariable String username) {
        return salaryFacade.getSalaryByUsername(username);
    }
}
