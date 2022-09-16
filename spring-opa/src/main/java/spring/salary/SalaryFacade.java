package spring.salary;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class SalaryFacade {

    private final SalaryService salaryService;

    @PreAuthorize("@opa.isAllowed('read', T(java.util.Map).of('type', 'salary', 'owner', #username))")
    public Salary getSalaryByUsername(String username) {
        return salaryService.getSalaryByUsername(username);
    }
}
