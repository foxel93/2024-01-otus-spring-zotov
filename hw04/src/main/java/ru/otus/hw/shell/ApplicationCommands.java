package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.hw.domain.Student;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.ResultService;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestService;

@ShellComponent
@RequiredArgsConstructor
public class ApplicationCommands {
    private final StudentService studentService;

    private final TestService testService;

    private final ResultService resultService;

    private final LocalizedIOService ioService;

    private Student student;

    private boolean testPassed;

    @ShellMethod(key = "start", value = "Start test")
    @ShellMethodAvailability({"startTestAvailability"})
    public void startTest() {
        var result = testService.executeTestFor(student);
        resultService.showResult(result);
        testPassed = true;
    }

    @ShellMethod(key = "login", value = "Login")
    @ShellMethodAvailability({"loginAvailability"})
    public String login() {
        student = studentService.determineCurrentStudent();
        return ioService.getMessage("ApplicationCommands.login.success");
    }

    public Availability startTestAvailability() {
        if (testPassed) {
            return Availability.unavailable(ioService.getMessage("ApplicationCommands.test.completed"));
        }
        return student == null
            ? Availability.unavailable(ioService.getMessage("ApplicationCommands.login.not.registered"))
            : Availability.available();
    }

    public Availability loginAvailability() {
        return student == null
            ? Availability.available()
            : Availability.unavailable(ioService.getMessage("ApplicationCommands.login.registered"));
    }
}