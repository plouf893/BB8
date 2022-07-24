package org.example;

import net.dv8tion.jda.api.entities.Message;
import org.jdbi.v3.core.Jdbi;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class CommandController {
    private final NotesDAO notesDAO;

    private final Map<String, BiConsumer<Message, String[]>> commands = new HashMap<>();

    CommandController(Jdbi jdbi) {
        this.notesDAO = jdbi.open().attach(NotesDAO.class);
        commands.put("ping", this::handlePing);
        commands.put("addnote", this::handleAddNote);
        commands.put("viewnote", this::handleViewNote);
        commands.put("delnote", this::handleDelNote);
        commands.put("rappel", this::handleRappel);
    }

    public void handleCommand(Message message, String[] args) {
        if (commands.containsKey(args[0])) {
            commands.get(args[0]).accept(message, args);
        } else {
            message.reply("Cette commande n'existe pas! Liste des commandes: " + String.join(" ", commands.keySet())).queue();
        }
    }

    public void handlePing(Message message, String[] args) {
        message.reply("Pong!").queue();
    }

    public void handleAddNote(Message message, String[] args) {
        try {
            notesDAO.insert(args[1], args[2]);
            message.reply("Ok!").queue();
        } catch (IndexOutOfBoundsException e) {
            message.reply("Usage: !addnote <cle> <valeur>").queue();
        }
    }

    public void handleViewNote(Message message, String[] args) {
        try {
            Optional<String> value = notesDAO.find(args[1]);
            message.reply(value.orElse("Je ne connais pas cela! ")).queue();
        } catch (IndexOutOfBoundsException e) {
            message.reply("Usage: !viewnote <cle>").queue();
        }
    }

    public void handleDelNote(Message message, String[] args) {
        try {
            notesDAO.delete(args[1]);
            message.reply("OK!").queue();
        } catch (IndexOutOfBoundsException e) {
            message.reply("Usage: !delnote <cle>").queue();
        }
    }

    public void handleRappel(Message message, String[] args) {
        try {
            message.reply(args[2]).queueAfter(Integer.parseInt(args[1]), TimeUnit.SECONDS);
            message.reply("Ok!").queue();
        } catch (NumberFormatException e) {
            message.reply("Le temps devrait être un nombre").queue();
        } catch (IndexOutOfBoundsException e) {
            message.reply("Usage: !rappel <temps en secondes> <message>").queue();
        }
    }

}