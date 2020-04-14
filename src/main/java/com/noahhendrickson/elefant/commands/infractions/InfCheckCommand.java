package com.noahhendrickson.elefant.commands.infractions;

import com.noahhendrickson.elefant.CommandBundle;
import com.noahhendrickson.elefant.ICommand;
import com.noahhendrickson.elefant.infractions.InfractionsManager;

/**
 * Created by Noah Hendrickson on 4/13/2020
 */
public class InfCheckCommand implements ICommand {

    @Override
    public void execute(CommandBundle bundle) {
        InfractionsManager manager = new InfractionsManager(bundle.getUser());

        if (manager.getAllInfractions().size() == 0) bundle.sendMessage("No infractions found!");
        else bundle.sendMessage("You have `" + manager.getAllInfractions().size() +
                "` infractions right now!");


    }

    @Override
    public String getCommand() {
        return "inf.check";
    }

    @Override
    public String getDescription() {
        return "Checks how many infractions you have.";
    }
}
