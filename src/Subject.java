import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Subject {

    String name;
    String code;

    int lectHour; //hours of lecture
    int tutoHour; // hourse of tuto
    int labHour;

    ArrayList groupList = new ArrayList();

    public Subject(String name, String code, int lectHour, int tutoHour, int labHour) {
        this.name = name;
        this.code = code;
        this.lectHour = lectHour;
        this.tutoHour = tutoHour;
        this.labHour = labHour;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getLectHour() {
        return lectHour;
    }

    public void setLectHour(int lectHour) {
        this.lectHour = lectHour;
    }

    public int getTutoHour() {
        return tutoHour;
    }

    public void setTutoHour(int tutoHour) {
        this.tutoHour = tutoHour;
    }

    public int getLabHour() {
        return labHour;
    }

    public void setLabHour(int labHour) {
        this.labHour = labHour;
    }

    public ArrayList getGroupList() {
        return groupList;
    }

    public void setGroupList(ArrayList groupList) {
        this.groupList = groupList;
    }

    public void assignGroup() throws FileNotFoundException
    {

        String fac, hari, mula, tamat, subCode, subName, groupCode, bilik, lectName, lectNum, subType;

        Scanner scanner = new Scanner(new File("A:\\Codes\\Mars Java\\GACO\\jadual.csv"));
        scanner.useDelimiter(",|\\n");
        while(scanner.hasNext()) {

            fac = scanner.next();
            hari = scanner.next();
            mula = scanner.next();
            tamat = scanner.next();
            subCode = scanner.next().trim();
            subName = scanner.next();
            groupCode = scanner.next().trim();
            bilik = scanner.next();
            lectName = scanner.next();
            lectNum = scanner.next();
            subType = scanner.next();

           //
            // System.out.println(code + " " + subCode);
            if (subCode.equalsIgnoreCase(code.trim())) {

                if(!groupList.isEmpty()) {
                    if(checkGroup(groupList,groupCode))
                    {
                        Group tempGroup = new Group();
                        //System.out.println(groupCode);
                        tempGroup.setCode(groupCode);
                        tempGroup.setTeachBy(lectName);
                        groupList.add(tempGroup);
                    }
                }
                else{

                    Group tempGroup = new Group();
                    //System.out.println(groupCode);
                    tempGroup.setCode(groupCode);
                    tempGroup.setTeachBy(lectName);
                    groupList.add(tempGroup);

                }

            }//end of assigngroup
        }//end of scanner
        scanner.close();

    }
    int countMatched =0;

    public void assignGroupSize() throws FileNotFoundException
    {
        //assign group size
        Scanner scanner = new Scanner(new File("A:\\Codes\\Mars Java\\GACO\\group-organized.csv"));
        scanner.useDelimiter(",|\\n");


        while(scanner.hasNext())
        {

            String group = scanner.next();
            String course = scanner.next();
            String groupSize = scanner.next();

            for(int i =0; i <groupList.size(); i++)
            {
                Group tempGroup = (Group) groupList.get(i);
                if((tempGroup.getCode()).trim().equalsIgnoreCase(group.trim()))
                {
                    //System.out.println(group + " = " + groupSize);
                    tempGroup.setStudNum(Integer.parseInt(groupSize.trim()));
                    groupList.set(i,tempGroup);

                    countMatched++;
                }
            }
        }

    }

    public int getCountMatched()
    {
        return countMatched;
    }

    public boolean checkGroup(ArrayList group, String groupCode)
    {
        Group tempGroup;
        for(int i =0; i < group.size(); i++)
        {
            tempGroup = (Group) group.get(i);
            if(groupCode.equalsIgnoreCase(tempGroup.getCode()))
            {
                return false;
            }
        }

        return true;
    }
    public void printGroupList(ArrayList groupList)
    {
        //System.out.println("in print grouplist");
        for(int i =0; i < groupList.size(); i++)
        {
            Group tempGroup = (Group) groupList.get(i);
            System.out.println(tempGroup.toString());

            ;
        }
    }

    @Override
    public String toString() {
        return "Subject{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", lectHour=" + lectHour +
                ", tutoHour=" + tutoHour +
                ", labHour=" + labHour +
                '}';
    }
}
