package glorydark.nukkit.data;

public enum MessageDecorationType {

    NORMAL("normal"),
    BOLD("bold"),
    ITALIC("italic"),
    UNDERLINED("underlined");

    private final String name;

    MessageDecorationType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public MessageDecorationType getType(String name){
        return MessageDecorationType.valueOf(name);
    }

}
