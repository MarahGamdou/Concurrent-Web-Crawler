/*
For parsing the commandline arguments
 */


import java.util.Scanner;

public class Parser {
    private String command;
    private static String timeParam = "-time";
    private static String inFileParam = "-input";
    private static String outFileParam = "-output";
    private static String numPageParam = "-storedPageNum";
    private Scanner s;

    public Parser(String cmd) {
        this.command = cmd;

    }

    public int getNumHours() {
        int indexOfParam = command.indexOf(timeParam)+timeParam.length();
        s = new Scanner(command.substring(indexOfParam));
        return Integer.parseInt(s.next().replace('h',' ').strip());

    }

    public String getinFile() {
        int indexOfParam = command.indexOf(inFileParam)+inFileParam.length();
        s = new Scanner(command.substring(indexOfParam));
        return s.next();

    }

    public String getoutFile() {
        int indexOfParam = command.indexOf(outFileParam)+outFileParam.length();
        s = new Scanner(command.substring(indexOfParam));
        return s.next();

    }

    public int getnumPages() {
        int indexOfParam = command.indexOf(numPageParam)+numPageParam.length();
        s = new Scanner(command.substring(indexOfParam));
        return Integer.parseInt(s.next());

    }


}
