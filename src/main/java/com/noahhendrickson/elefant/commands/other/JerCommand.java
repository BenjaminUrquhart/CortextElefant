package com.noahhendrickson.elefant.commands.other;

import com.noahhendrickson.elefant.CommandBundle;
import com.noahhendrickson.elefant.ICommand;

/**
 * Created by Noah Hendrickson on 4/12/2020
 */
public class JerCommand implements ICommand {

    @Override
    public void execute(CommandBundle bundle) {
        bundle.sendMessage("<:java:585593753629229081>", "Learn Java First!");
    }

    @Override
    public String getCommand() {
        return "jer";
    }

    @Override
    public String getDescription() {
        return "A jer thing to say.";
    }
}
