package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import javax.security.auth.login.LoginException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello");
        try {
            Jdbi jdbi = Jdbi.create("jdbc:sqlite:bb8.db");
            jdbi.installPlugin(new SqlObjectPlugin());
            JDA api = JDABuilder.createDefault(args[0]).build();
            api.addEventListener(new DiscordListener(new CommandController(jdbi)));
        } catch (LoginException e) {
            System.out.println("Discord login failed");
        }

    }
}