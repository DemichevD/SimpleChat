package server;

public enum MessageCommand {
    QUIT("\\quit", " - disconnect from server"),
    USERS("\\users", " - list online users"),
    STOP("\\stop", " - stop server"),
    HELP("\\help", " - getting information about available command on the server");

    private String command;
    private String commandInfo;

    MessageCommand(String com, String info) {
        this.command = com;
        this.commandInfo = info;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommandInfo() {
        return commandInfo;
    }

    public void setCommandInfo(String commandInfo) {
        this.commandInfo = commandInfo;
    }

    public String getAllData() {
        return this.getCommand() + this.getCommandInfo();
    }
}
