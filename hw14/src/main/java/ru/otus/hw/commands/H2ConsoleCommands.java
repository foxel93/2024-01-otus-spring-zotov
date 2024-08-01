package ru.otus.hw.commands;

import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import org.h2.tools.Console;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@RequiredArgsConstructor
@ShellComponent
public class H2ConsoleCommands {
    @ShellMethod(value = "H2 Console", key = "h2")
    public void h2Console() throws SQLException {
        Console.main();
    }
}
