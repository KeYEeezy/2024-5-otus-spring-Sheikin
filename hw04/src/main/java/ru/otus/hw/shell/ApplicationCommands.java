package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent(value = "Application Commands")
@RequiredArgsConstructor
public class ApplicationCommands {

    private final StudentService studentService;

    private final TestRunnerService testRunnerService;

    private final LocalizedIOService ioService;

    @ShellMethod(value = "Start test", key = {"s", "start"})
    public void start() {
        var student = studentService.determineCurrentStudent();
        ioService.printFormattedLine("Добро пожаловать: %s", student.getFullName());
        testRunnerService.run(student);
    }

}
