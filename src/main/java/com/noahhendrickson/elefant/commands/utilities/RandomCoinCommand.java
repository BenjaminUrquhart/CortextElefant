package com.noahhendrickson.elefant.commands.utilities;

import com.noahhendrickson.elefant.CommandBundle;
import com.noahhendrickson.elefant.ICommand;

import java.util.Random;

/**
 * Created by Noah Hendrickson on 4/13/2020
 */
public class RandomCoinCommand implements ICommand {

    private final String[] results = new String[]{"Heads", "Tails"};

    @Override
    public void execute(CommandBundle bundle) {
        bundle.sendMessage("Result: `" + (results[new Random().nextInt(results.length)]) + "`");
    }

    @Override
    public String getCommand() {
        return "random.coin";
    }

    @Override
    public String getDescription() {
        return "Flips a coin and returns the result.";
    }
}
