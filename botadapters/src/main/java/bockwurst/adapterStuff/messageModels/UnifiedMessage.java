package bockwurst.adapterStuff.messageModels;

import bockwurst.adapterStuff.models.Platform;

public class UnifiedMessage {
    public Platform platform;
    public String text;
    public String command;
    public String[] args;
    public String[] argsLowerCase;
}
