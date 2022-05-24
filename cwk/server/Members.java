import java.util.ArrayList;

// Members class for handling client request of the member list
public class Members {
    private int maxListNum = 0;
    private int maxMemberNum = 0;
    private ArrayList<ArrayList<String>> lists = null;

    public Members(int maxListNum, int maxMemberNum) {
        this.maxListNum = maxListNum;
        this.maxMemberNum = maxMemberNum;
        // overall member lists
        lists = new ArrayList<ArrayList<String>>();
        // create the member list
        for (int i = 0; i < maxListNum; i++) {
            lists.add(new ArrayList<String>(maxMemberNum));
        }
    }

    // add member for the list
    public void addMember(int listIndex, String memberName) {
        lists.get(listIndex).add(memberName);
    }

    public int getMaxListNum() {
        return maxListNum;
    }

    public int getMaxMemberNum() {
        return maxMemberNum;
    }

    public int getMemberNuMForList(int listIndex) {
        return lists.get(listIndex).size();
    }

    public ArrayList<ArrayList<String>> getLists() {
        return lists;
    }
}
