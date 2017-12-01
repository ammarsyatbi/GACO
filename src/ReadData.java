import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Scanner;


//Population size = 808; based on real data, cleaned and ready for calculation.
public class ReadData {

    public static ArrayList subjectList;
    public static ArrayList lecturerList;
    public static ArrayList tGroupList;
    public static ArrayList tLecturerList;
    public static ArrayList tKelasList;
    //so i dont have to do parameters passing hehehe
    public static Timetable group;
    public static Timetable kelas;
    public static  Timetable lecturer;
    public static  int populationSize;

    ReadData() throws IOException
    {

        //List of subject
        subjectList = new ArrayList();
        lecturerList = new ArrayList();
        tGroupList = new ArrayList();
        tLecturerList = new ArrayList();
        tKelasList = new ArrayList();
        assignSubjectlist();
        assignGroupSize();
        clearMissingValue();
        //printSublist();
        assignTimetable();
        System.out.println("Subject size = "+subjectList.size());
        readLecturer();
        readRoomlist();

        Subject tempSub;
        Group tempGroup;

/*
        //Go through subject
        for(int i=0; i < subjectList.size(); i++)
        {
            tempSub = (Subject)subjectList.get(i);

            //go through group in the Subject
            for(int j=0; j<tempSub.getGroupList().size(); j++)
            {
                tempGroup = (Group)tempSub.getGroupList().get(j);
            }
        }
*/

    }//End of Main

    public static void readRoomlist() throws IOException
    {
        Scanner scanner = new Scanner(new File(new File(".").getCanonicalPath()+"\\roomlist.csv"));
        scanner.useDelimiter(",|\\n");
        String kelas;
        int kelasCap;
        char kelasType;

        while(scanner.hasNext())
        {
            kelas = scanner.next();
            kelasCap = Integer.parseInt(scanner.next().trim());
            kelasType = scanner.next().trim().charAt(0);

            Kelas temp = new Kelas(kelas,kelasCap,kelasType);
            tKelasList.add(temp);
        }
    }

    public static void readLecturer() throws IOException
    {
        Scanner scanner = new Scanner(new File(new File(".").getCanonicalPath()+"\\lecturerSubject.csv"));
        scanner.useDelimiter(",|\\n");

        String subCode;
        String lecturer;
        //TODO: assign lecturer dalam timetable list, and list of subject dia ajar.

        while(scanner.hasNext())
        {
            lecturer = scanner.next().trim();
            subCode = scanner.next();


            //masukkan dalam timetable untuk overlapping
            if(!checkTimetableLecturerList(lecturer))
            {

                //add lecturer
                Timetable temp = new Timetable();
                temp.setName(lecturer);
                tLecturerList.add(temp);
            }

            //masukkan dalam list lain, untuk check subject yang diajaar
            if(!checkLecturerList(lecturer.trim()))
            {
                //System.out.println(lecturer);
                //add lecturer
                Lecturer temp = new Lecturer();
                temp.setLecturer(lecturer.trim());
                temp.addSubject(subCode);
                lecturerList.add(temp);
            }
            else
            {
                addLecturerSubject(subCode,lecturer);

            }



        }
    }

    public static void addLecturerSubject(String subCode,String name)
    {
        for(int i=0; i <lecturerList.size(); i++)
        {
            Lecturer temp = (Lecturer) lecturerList.get(i);
            if(temp.getLecturer().trim().equalsIgnoreCase(name.trim()))
            {
                temp.addSubject(subCode);
                lecturerList.set(i,temp);
            }
        }
    }

    public static boolean checkTimetableLecturerList(String name)
    {
        for(int i =0; i<tLecturerList.size(); i++)
        {
            Timetable temp = (Timetable) tLecturerList.get(i);
            if(name.trim().equalsIgnoreCase(temp.getName().trim()))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean checkLecturerList(String name)
    {
        for(int i =0; i<lecturerList.size(); i++)
        {
            Lecturer lecturer = (Lecturer) lecturerList.get(i);
            if(name.trim().equalsIgnoreCase(lecturer.getLecturer().trim()))
            {
                return true;
            }
        }
        return false;
    }
    //end of read lecturer
    public static void printSublist()
    {
        for(int i =0; i < subjectList.size(); i++)
        {
            Subject subject;
            subject = (Subject) subjectList.get(i);

            System.out.println(subject.toString());
            subject.printGroupList(subject.getGroupList());

        }
    }
    public static void assignTimeslot()
    {

    }
    public static void assignSubjectlist() throws IOException
    {
        //System.out.println(new File(".").getCanonicalPath());
        Scanner scanner = new Scanner(new File(new File(".").getCanonicalPath()+"\\subject-organized.csv"));
        scanner.useDelimiter(",|\\n");

        while(scanner.hasNext())
        {
            String subCode = scanner.next();
            String subName = scanner.next();
            String subM = scanner.next().trim();
            String subT = scanner.next().trim();
            String subS = scanner.next().trim();
            //System.out.println(subCode + " " + subM + " " + subT+ " " + subS);

            Subject subject = new Subject(subName,subCode,Integer.parseInt(subS),Integer.parseInt(subT),Integer.parseInt(subM));
            subject.assignGroup();
            subjectList.add(subject);
        }
        scanner.close();
    }

    public static void assignGroupSize() throws IOException
    {
        int countMatchedGroup = 0;
        for(int i=0; i < subjectList.size(); i++)
        {
            Subject subject = (Subject) subjectList.get(i);
            subject.assignGroupSize();
            subjectList.set(i,subject);
            countMatchedGroup += subject.getCountMatched();
        }
        System.out.println(" list of subgroups "+countMatchedGroup);
    }
    public static void clearMissingValue()
    {
        int countTotalGroup= 0;
        for(int i =0; i < subjectList.size(); i++)
        {
            Subject subject = (Subject) subjectList.get(i);
            ArrayList temp = subject.getGroupList();
            for(int j=0; j< temp.size(); j++)
            {

                Group group = (Group)  temp.get(j);
                if(group.getStudNum() == 0)
                {
                    temp.remove(j);
                    j--;
                }
            }

            subject.setGroupList(temp);
            subjectList.set(i,subject);
        }

        for(int i =0; i < subjectList.size(); i++)
        {
            Subject subject = (Subject) subjectList.get(i);

            countTotalGroup += subject.getGroupList().size();

        }
        populationSize = countTotalGroup; //cleaned
        System.out.println("Total cleaned group = " + countTotalGroup);
    }

    public static boolean checkGroupList(String groupname)
    {
        for(int i=0; i < tGroupList.size(); i++)
        {
            Timetable temp = (Timetable) tGroupList.get(i);
            if(temp.getName().trim().equalsIgnoreCase(groupname.trim()))
            {
                return false;
            }
        }

        return true;
    }

    public static void assignTimetable()//masukkan group  based on subject ke dalam arraylist
    {
        for(int i =0; i < subjectList.size(); i++)
        {
            Subject subject = (Subject) subjectList.get(i);
            ArrayList temp = subject.getGroupList();
            for(int j=0; j< temp.size(); j++)
            {
                Group groupTemp = (Group)  temp.get(j);//group dalam subject

                //check dalam grouplist dah ade belum
                if(checkGroupList(groupTemp.getCode()))
                {
                    group = new Timetable();
                    group.setName(groupTemp.getCode());
                    tGroupList.add(group);

                }

                //kelas = new Timetable();
                //lecturer = new Timetable();
            }

            subject.setGroupList(temp);
            subjectList.set(i,subject);
        }


    }


}
