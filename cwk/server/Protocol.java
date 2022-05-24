import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

// Protocol class for defining the communication protocol
public class Protocol {
    private Members members = null;
    private String ipAddress = null;

    public Protocol(Members members, String ipAddress) {
        this.members = members;
        this.ipAddress = ipAddress;
    }

    // check if each character is number
    public static boolean checkAllNumbers(String string) {
        boolean allNumber = true;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) < '0' | string.charAt(i) > '9') {
                allNumber = false;
                break;
            }
        }
        return allNumber;
    }

    // log client request to the file
    public static void logRequest(String request, String ipAddress) throws IOException {
        // get the file path
        File directory = new File("");
        String path = directory.getCanonicalPath();
        File log = new File(path, "log.txt");
        if (!log.exists()) {
            log.createNewFile();
        }

        FileOutputStream output = new FileOutputStream(log, true);
        OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");

        // get the date
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        writer.append(date.format(formatter));
        writer.append("|");

        // get the time
        LocalTime time = LocalTime.now();
        formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        writer.append(time.format(formatter));
        writer.append("|");

        // store the ip address and the request
        writer.append(ipAddress);
        writer.append("|");
        writer.append(request);
        writer.append("\n");

        writer.close();
        output.close();
    }

    // process the client request
    public String process(String input) throws IOException {
        StringBuilder output = new StringBuilder();
        String[] split = input.split(" ");
        int maxListNumber = members.getMaxListNum();
        int maxMemberNumber = members.getMaxMemberNum();
        ArrayList<ArrayList<String>> lists = members.getLists();
        String request = "";
        if (split[0].equals("1")) {
            // handle total
            if (split[1].equals("totals")) {
                logRequest("totals", ipAddress);
                output.append("Number of List: ").append(maxListNumber).append("\n");
                output.append("Maximum size of the list: ").append(maxMemberNumber).append("\n");
                for (int i = 0; i < maxListNumber; i++) {
                    output.append("List ").append(i + 1).append(": ");
                    output.append(members.getMemberNuMForList(i)).append(" Member(s).\n");
                }
            } else {
                output = new StringBuilder("Errors: For one argument, " +
                        "we only support totals command");
            }
        } else if (split[0].equalsIgnoreCase("2")) {
            if (split[1].equalsIgnoreCase("list")) {
                // handle list n
                if (!checkAllNumbers(split[2])) {
                    output = new StringBuilder("Errors: The number of list should be " +
                            "positive integer");
                } else {
                    request = "list " + split[2];
                    logRequest(request, ipAddress);
                    // check the entered number is smaller
                    // than the maximum list number
                    int listNum = Integer.parseInt(split[2]);
                    if (listNum > maxListNumber + 1) {
                        output = new StringBuilder("Errors: There are only " +
                                maxListNumber + " lists\n");
                    } else if (listNum == 0) {
                        output = new StringBuilder("The first list is list 1!\n");
                    } else {
                        int memberNum = lists.get(listNum - 1).size();
                        if (memberNum == 0) {
                            output = new StringBuilder("No member in this list\n");
                        } else {
                            for (int i = 0; i < memberNum; i++) {
                                output.append(lists.get(listNum - 1).get(i)).append("\n");
                            }
                        }
                    }
                }
            } else {
                output = new StringBuilder("Errors: For two arguments, " +
                        "we only support list n command");
            }
        } else if (split[0].equalsIgnoreCase("3")) {
            if (split[1].equalsIgnoreCase("join")) {
                // handle join
                if (!checkAllNumbers(split[2])) {
                    output = new StringBuilder("Errors: The number of list should be " +
                            "positive integer");
                } else {
                    StringBuilder memberName = new StringBuilder();
                    for (int i = 3; i < split.length; i++) {
                        memberName.append(split[i]);
                        memberName.append(" ");
                    }
                    memberName = new StringBuilder(memberName.toString().trim());
                    request += "join " + split[2] + " " + memberName;
                    logRequest(request, ipAddress);
                    // check the entered number is smaller
                    // than the maximum list number
                    int listNum = Integer.parseInt(split[2]);
                    if (listNum > maxListNumber + 1) {
                        output = new StringBuilder("Errors: There are only " +
                                (maxListNumber) + " lists\n");
                    } else if (listNum == 0) {
                        output = new StringBuilder("The first list is list 1!\n");
                    } else {
                        // check whether the list is full
                        if (members.getLists().get(listNum - 1).size() < maxMemberNumber) {
                            members.addMember(listNum - 1, memberName.toString());
                            output = new StringBuilder("Success");
                        } else {
                            output = new StringBuilder("Failed\n");
                        }
                    }
                }
            } else {
                output = new StringBuilder("Errors: For three arguments, " +
                        "we only support join n name");
            }
        } else {
            output = new StringBuilder("Errors: We only support commands " +
                    "with 1 or 2 or 3 arguments\n");
        }
        return output.toString();
    }
}
