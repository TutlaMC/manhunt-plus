package net.tutla.manhuntPlus.commandsystem;

import java.util.List;

public class CommandTabAutoComplete {
    public String name;
    public List<CommandTabAutoComplete> childAutoCompletes;
    public String value;
    public List<String> values;

    public CommandTabAutoComplete(String name, List<CommandTabAutoComplete> childAutoCompletes, String value){
        this.name = name;
        this.childAutoCompletes = childAutoCompletes;
        this.value = value;
    }

    public CommandTabAutoComplete setValues(List<String> values){
        this.values = values;
        return this;
    }
}
