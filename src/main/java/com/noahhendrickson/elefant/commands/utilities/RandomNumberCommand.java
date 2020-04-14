package com.noahhendrickson.elefant.commands.utilities;

import com.noahhendrickson.elefant.CommandBundle;
import com.noahhendrickson.elefant.ICommand;

import java.util.Random;

/**
 * Created by Noah Hendrickson on 4/13/2020
 */
public class RandomNumberCommand implements ICommand {

    @Override
    public void execute(CommandBundle bundle) {

        int end = 0;
        int start = 11;

        if (bundle.areArgsMoreThan(2)) {
            try {
                end = Integer.parseInt(bundle.getArgAt(1));
                start = Integer.parseInt(bundle.getArgAt(2)) + 1;
            } catch (NumberFormatException e) {
                bundle.sendMessage("Please supply a valid number!");
            }
        }

        if (end >= start) {
            bundle.sendMessage("The ending number must be smaller than starting number!");
            return;
        }

        bundle.sendMessage("Result: `" + (new Random().nextInt(start - end) + end) + "`");
    }

    @Override
    public String getCommand() {
        return "random.number";
    }

    @Override
    public String getDescription() {
        return "Returns a random number from 0-10 or in a range if specified.";
    }

    @Override
    public String getUsage() {
        return "[end number] [start number]";
    }
}
