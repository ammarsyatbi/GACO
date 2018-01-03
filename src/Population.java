import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Scanner;


//Population size = 808; based on real data, cleaned and ready for calculation.
public class Population {

    private ArrayList subjectList;
    private ArrayList lecturerList;
    private ArrayList kelasList;

    private ArrayList tGroupList;
    private ArrayList tLecturerList;
    private ArrayList tKelasList;
    //so i dont have to do parameters passing hehehe
    private Timetable group;
    private Timetable kelas;
    private Timetable lecturer;
    private  int populationSize;
    private int populationFitness;

    private int slottedGroup;
    private int slottedKlas;
    private int slottedLect;

    Population() throws IOException
    {

        //List of subject
        subjectList = new ArrayList();
        lecturerList = new ArrayList();
        kelasList = new ArrayList();


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



    }//End of Main

    public ArrayList getLecturerList() {
        return this.lecturerList;
    }

    public void setLecturerList(ArrayList lecturerList) {
        this.lecturerList = lecturerList;
    }

    public ArrayList getKelasList() {
        return kelasList;
    }

    public void setKelasList(ArrayList kelasList) {
        this.kelasList = kelasList;
    }

    public Timetable getGroup() {
        return this.group;
    }

    public void setGroup(Timetable group) {
        this.group = group;
    }

    public Timetable getKelas() {
        return this.kelas;
    }

    public void setKelas(Timetable kelas) {
        this.kelas = kelas;
    }

    public Timetable getLecturer() {
        return this.lecturer;
    }

    public void setLecturer(Timetable lecturer) {
        this.lecturer = lecturer;
    }

    public ArrayList getSubjectList() {
        return this.subjectList;
    }

    public void setSubjectList(ArrayList subjectList) {
        this.subjectList = subjectList;
    }

    public ArrayList gettGroupList() {
        return tGroupList;
    }

    public void settGroupList(ArrayList tGroupList) {
        this.tGroupList = tGroupList;
    }

    public ArrayList gettLecturerList() {
        return this.tLecturerList;
    }

    public void settLecturerList(ArrayList tLecturerList) {
        this.tLecturerList = tLecturerList;
    }

    public ArrayList gettKelasList() {
        return this.tKelasList;
    }

    public void settKelasList(ArrayList tKelasList) {
        this.tKelasList = tKelasList;
    }

    public int getPopulationFitness() {
        return this.populationFitness;
    }

    public void setPopulationFitness(int populationFitness)
    {
        this.populationFitness = populationFitness;
    }




    //END OF SETTER AND GETTER -------------------------------------------------------------------------------------

    public void readRoomlist() throws IOException
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
            kelasList.add(temp);

            Timetable tTemp = new Timetable();
            tTemp.setName(kelas);
            tTemp.setObject(temp);
            tKelasList.add(tTemp);

        }
    }

    public void readLecturer() throws IOException
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

    public void addLecturerSubject(String subCode,String name)
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

    public boolean checkTimetableLecturerList(String name)
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
    public boolean checkLecturerList(String name)
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
    public void printSublist()
    {
        for(int i =0; i < subjectList.size(); i++)
        {
            Subject subject;
            subject = (Subject) subjectList.get(i);

            System.out.println(subject.toString());
            subject.printGroupList(subject.getGroupList());

        }
    }

    public void assignSubjectlist() throws IOException
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

    public void assignGroupSize() throws IOException
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
    public void clearMissingValue()
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

    public boolean checkGroupList(String groupname)
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

    public void assignTimetable()//masukkan group  based on subject ke dalam arraylist
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

    public void calculateGroupFitness()
    {
        int totalFitness = 0;
        for(int i =0; i < tGroupList.size(); i++)
        {
            totalFitness += ((Timetable)tGroupList.get(i)).getFitnessTimetable();
        }

        this.populationFitness = totalFitness;
    }

    public void countSlottedGroup()
    {
        int count = 0;
        for(int i =0; i < tGroupList.size(); i++)
        {
            Timetable timetable = (Timetable)tGroupList.get(i);

            for(int j=0; j <5; j++)
            {
                for(int k =0; k <10; k ++)
                {
                    if(timetable.getTimeslot(j,k) != null)
                    {
                        count++;
                    }
                }
            }
        }

        slottedGroup = count;
        // gg end
    }
    public void counteSlottedKelas()
    {

        int count = 0;
        for(int i =0; i < tKelasList.size(); i++)
        {
            Timetable timetable = (Timetable)tKelasList.get(i);

            for(int j=0; j <5; j++)
            {
                for(int k =0; k <10; k ++)
                {
                    if(timetable.getTimeslot(j,k) != null)
                    {
                        count++;
                    }
                }
            }
        }

        slottedKlas = count;


    }
    public void countSlottedLect()
    {
        int count = 0;
        for(int i =0; i < tLecturerList.size(); i++)
        {
            Timetable timetable = (Timetable)tLecturerList.get(i);

            for(int j=0; j <5; j++)
            {
                for(int k =0; k <10; k ++)
                {
                    if(timetable.getTimeslot(j,k) != null)
                    {
                        count++;
                    }
                }
            }
        }

        slottedLect = count;

    }

    public void countAllSlotted()
    {
        countSlottedGroup();
        counteSlottedKelas();
        countSlottedLect();
        System.out.println("Slotted:- \n Group : " + slottedGroup
                            + " \n Kelas : " + slottedKlas
                            + " \n Lecturer : " + slottedLect);
    }


}
