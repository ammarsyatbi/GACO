import com.sun.org.apache.regexp.internal.RE;
import javafx.beans.binding.BooleanExpression;

import javax.sound.sampled.Line;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class InitializeGA {


    private  Population population;

    ArrayList tGroupList;
    ArrayList tKelasList;
    ArrayList tLecturerList;

    ArrayList subjectList;
    ArrayList kelasList;
    ArrayList lecturerList;

    public InitializeGA() throws IOException, InterruptedException
    {

        population = new Population();
        tGroupList = population.gettGroupList();
        tKelasList= population.gettKelasList();
        tLecturerList = population.gettLecturerList();

        subjectList = population.getSubjectList();
        kelasList = population.getKelasList();
        lecturerList = population.getLecturerList();


        //assignTimetable();
        assignTT();
        population.countAllSlotted();
        //initializeFitness();//set all fitness to zero before calculate
        ///printGroupList();
    }

    public Population getPopulation() {
        return population;
    }

    public  void assignTT() throws InterruptedException, IOException {

        int notIn;
        int notInKelas;
        int slottedGroup;

        do {
            notIn =0;
            notInKelas =0;
            slottedGroup =0;

            population = new Population();
            tGroupList = population.gettGroupList();
            tKelasList= population.gettKelasList();
            tLecturerList = population.gettLecturerList();

            subjectList = population.getSubjectList();
            kelasList = population.getKelasList();



            for (int t = 0; t < tGroupList.size(); t++) {
                for (int s = 0; s < subjectList.size(); s++) {
                    for (int g = 0; g < ((Subject) subjectList.get(s)).getGroupList().size(); g++) {
                        if (((Group) ((Subject) subjectList.get(s)).getGroupList().get(g)).getCode().equalsIgnoreCase(((Timetable) tGroupList.get(t)).getName().trim())) {

                            Random rg = new Random();
                            Information info;
                            int dayTuto = rg.nextInt(5);
                            int timeTuto = rg.nextInt(11- ((Subject) subjectList.get(s)).getTutoHour());

                            int dayLect = rg.nextInt(5);
                            int timeLect = rg.nextInt(11 - ((Subject) subjectList.get(s)).getLectHour());

                            int dayLab = rg.nextInt(5);
                            int timeLab = rg.nextInt(11 - ((Subject) subjectList.get(s)).getLabHour());


                            int kelasIndexTuto = rg.nextInt(tKelasList.size());
                            int kelasIndexLect = rg.nextInt(tKelasList.size());
                            int kelasIndexLab = rg.nextInt(tKelasList.size());

                            int lectIndex = checkLecturer(((Subject) subjectList.get(s)).getCode(), dayTuto, timeTuto, ((Subject) subjectList.get(s)).getTutoHour());

                            do {

                                 dayTuto = rg.nextInt(5);
                                 timeTuto = rg.nextInt(11 - ((Subject) subjectList.get(s)).getTutoHour());

                                 dayLect = rg.nextInt(5);
                                 timeLect = rg.nextInt(11 - ((Subject) subjectList.get(s)).getLectHour());

                                 dayLab = rg.nextInt(5);
                                 timeLab = rg.nextInt(11 - ((Subject) subjectList.get(s)).getLabHour());


                                kelasIndexTuto = rg.nextInt(tKelasList.size());
                                kelasIndexLect = rg.nextInt(tKelasList.size());
                                kelasIndexLab = rg.nextInt(tKelasList.size());

                                 lectIndex = checkLecturer(((Subject) subjectList.get(s)).getCode(), dayTuto, timeTuto, ((Subject) subjectList.get(s)).getTutoHour());
//
//                                 System.out.println(String.format("%-15s","Iteration group") + String.format("%-15s","Tuto") + String.format("%-15s","Lecture") +String.format("%-15s","Lab") + String.format("%-15s","Kelas"));
//                                System.out.println(String.format("%-15s",t) + String.format("%-15s",dayTuto+ " " + timeTuto) + String.format("%-15s",dayLect +" " + timeLect) +
//                                                    String.format("%-15s",dayLab+" "+timeLab) + String.format("%-15s",kelasIndexTuto + " "+ kelasIndexLect + " " + kelasIndexLab));

                            }while( !(((Timetable) tGroupList.get(t)).checkTimeslot(dayTuto, timeTuto, ((Subject) subjectList.get(s)).getTutoHour())) && //tutogroup
                                    !((Timetable) tKelasList.get(kelasIndexTuto)).checkTimeslot(dayTuto, timeTuto, ((Subject) subjectList.get(s)).getTutoHour()) &&//tutokelas

                                    !(((Timetable) tGroupList.get(t)).checkTimeslot(dayLect, timeLect, ((Subject) subjectList.get(s)).getLectHour())) &&//lecturegroup
                                    !((Timetable)  tKelasList.get(kelasIndexLect)).checkTimeslot(dayLect,timeLect,((Subject) subjectList.get(s)).getLectHour()) &&//lecturekelas

                                    !(((Timetable) tGroupList.get(t)).checkTimeslot(dayLab, timeLab, ((Subject) subjectList.get(s)).getLabHour())) && // labgroup
                                    !((Timetable) tKelasList.get(kelasIndexLab)).checkTimeslot(dayLab,timeLab,((Subject) subjectList.get(s)).getLabHour()) //labkelas
                                    );//bruteforcing randomly, please work

                            //try 100 kali je
                            //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

                            //FIND GROUP TIME SLOT
                            for (int k = 0; (k < 100000 && !(((Timetable) tGroupList.get(t)).checkTimeslot(dayTuto, timeTuto, ((Subject) subjectList.get(s)).getTutoHour()))); k++) {
                                if (!(((Timetable) tGroupList.get(t)).checkTimeslot(dayTuto, timeTuto, ((Subject) subjectList.get(s)).getTutoHour()))) {
                                    dayTuto = new Random().nextInt(5);
                                    timeTuto = new Random().nextInt(11 - ((Subject) subjectList.get(s)).getTutoHour());
                                }
                            }
                            //FIND CLASS TIMESLOT
                            for (int k = 0; (k < 100000 && !((Timetable) tKelasList.get(kelasIndexTuto)).checkTimeslot(dayTuto, timeTuto, ((Subject) subjectList.get(s)).getTutoHour())); k++) {
                                if (!((Timetable) tKelasList.get(kelasIndexTuto)).checkTimeslot(dayTuto, timeTuto, ((Subject) subjectList.get(s)).getTutoHour())) {
                                    kelasIndexTuto = rg.nextInt(tKelasList.size());
                                }
                            }


                            if ((((Timetable) tGroupList.get(t)).checkTimeslot(dayTuto, timeTuto, ((Subject) subjectList.get(s)).getTutoHour())) &&
                                    ((Timetable) tKelasList.get(kelasIndexTuto)).checkTimeslot(dayTuto, timeTuto, ((Subject) subjectList.get(s)).getTutoHour())) {
                                //------------------ASSIGN TUTORIAL---------------------------------------------------------------------
                                info = new Information();

                                //setGroup
                                info.setGroup(((Timetable) tGroupList.get(t)).getName().trim());
                                info.setStudnum(((Group) ((Subject) subjectList.get(s)).getGroupList().get(g)).getStudNum());

                                //setSubject
                                info.setSubjectCode(((Subject) subjectList.get(s)).getCode().trim());
                                info.setSubjectHour(((Subject) subjectList.get(s)).getTutoHour());
                                char temp ='T';
                                info.setSubjectType(temp);

                                //setindex
                                info.setDay(dayTuto);
                                info.setTime(timeTuto);
                                info.setGroupIndex(t);
                                info.setKelasIndex(kelasIndexTuto);
                                info.setLecturerIndex(lectIndex);


                                //LECTURER SPECIAL CASE - NUMBER OF LECTURER IS NOT ENOUGH TO CATER ALL GROUP
                                if (lectIndex != -1) {
                                    info.setLecturer(((Timetable) tLecturerList.get(lectIndex)).getName());
                                    ((Timetable) tLecturerList.get(lectIndex)).setTimeslot(info, dayTuto, timeTuto, ((Subject) subjectList.get(s)).getTutoHour());
                                } else {
                                    info.setLecturer("PENDING");
                                }
                                //KELAS
                                if(((Timetable)tKelasList.get(kelasIndexTuto)).checkTimeslot(dayTuto,timeTuto,((Subject) subjectList.get(s)).getTutoHour()))
                                {

                                    //setKelas
                                    info.setKelas(((Timetable) tKelasList.get(kelasIndexTuto)).getName());
                                    info.setKelasType(((Kelas) kelasList.get(kelasIndexTuto)).getKelasType());
                                    info.setKelasCap(((Kelas) kelasList.get(kelasIndexTuto)).getKelasCap());

                                    ((Timetable) tKelasList.get(kelasIndexTuto)).setTimeslot(info, dayTuto, timeTuto, ((Subject) subjectList.get(s)).getTutoHour());

                                }
                                else
                                {
                                    info.setKelas("NOCLASS");
                                    System.out.println("TAK MASUK ----------------------------> " + ((Timetable) tKelasList.get(kelasIndexTuto)).getName().trim());
                                    notInKelas += ((Subject) subjectList.get(s)).getTutoHour();
                                }

                                ((Timetable) tGroupList.get(t)).setTimeslot(info, dayTuto, timeTuto, ((Subject) subjectList.get(s)).getTutoHour());
                                slottedGroup += ((Subject) subjectList.get(s)).getTutoHour();
                                //------------------END ASSIGN TUTORIAL---------------------------------------------------------------------
                            } else {
                                System.out.print("Group/Class not in after 10k Iteration - ");
                                System.out.println(((Timetable) tGroupList.get(t)).getName().trim());
                                notIn += ((Subject) subjectList.get(s)).getTutoHour();
                            }



                            //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

                            //LECTURER------------------------------------------------------------------------------------
                            lectIndex = checkLecturer(((Subject) subjectList.get(s)).getCode(), dayLect, timeLect, ((Subject) subjectList.get(s)).getLectHour());
                            for (int k = 0; (k < 100000 && !(((Timetable) tGroupList.get(t)).checkTimeslot(dayLect, timeLect, ((Subject) subjectList.get(s)).getLectHour()))); k++) {
                                if (!(((Timetable) tGroupList.get(t)).checkTimeslot(dayLect, timeLect, ((Subject) subjectList.get(s)).getLectHour()))) {
                                    dayLect = new Random().nextInt(5);
                                    timeLect = new Random().nextInt(11 - ((Subject) subjectList.get(s)).getLectHour());
                                }
                            }


                            //FIND CLASS TIMESLOT
                            for (int k = 0; (k < 100000 && !((Timetable)tKelasList.get(kelasIndexLect)).checkTimeslot(dayLect,timeLect,((Subject) subjectList.get(s)).getLectHour())); k++) {

                                if (!((Timetable) tKelasList.get(kelasIndexLect)).checkTimeslot(dayLect, timeLect, ((Subject) subjectList.get(s)).getLectHour())) {
                                    kelasIndexLect = rg.nextInt(tKelasList.size());
                                }
                            }

                            if ((((Timetable) tGroupList.get(t)).checkTimeslot(dayLect, timeLect, ((Subject) subjectList.get(s)).getLectHour())) &&//lecturegroup
                                    ((Timetable)tKelasList.get(kelasIndexLect)).checkTimeslot(dayLect,timeLect,((Subject) subjectList.get(s)).getLectHour())) {
                                //------------------ASSIGN LECTURE---------------------------------------------------------------------

                                info = new Information();
                                //setGroup
                                info.setStudnum(((Group) ((Subject) subjectList.get(s)).getGroupList().get(g)).getStudNum());
                                info.setGroup(((Timetable) tGroupList.get(t)).getName().trim());

                                //setSubject
                                info.setSubjectCode(((Subject) subjectList.get(s)).getCode().trim());
                                info.setSubjectHour(((Subject) subjectList.get(s)).getLectHour());
                                info.setSubjectType('L');

                                //setindex
                                info.setDay(dayLect);
                                info.setTime(timeLect);
                                info.setGroupIndex(t);
                                info.setKelasIndex(kelasIndexLect);
                                info.setLecturerIndex(lectIndex);

                                if ((lectIndex != -1)) {
                                    info.setLecturer(((Timetable) tLecturerList.get(lectIndex)).getName());
                                    ((Timetable) tLecturerList.get(lectIndex)).setTimeslot(info, dayLect, timeLect, ((Subject) subjectList.get(s)).getLectHour());
                                } else {
                                    info.setLecturer("PENDING");
                                }

                                //Class also special case , but can be solve be revoke initialization
                                if(((Timetable)tKelasList.get(kelasIndexLect)).checkTimeslot(dayLect,timeLect,((Subject) subjectList.get(s)).getLectHour()))
                                {
                                    //setKelas
                                    info.setKelas(((Timetable) tKelasList.get(kelasIndexLect)).getName());
                                    info.setKelasCap(((Kelas)kelasList.get(kelasIndexLect)).getKelasCap());
                                    info.setKelasType(((Kelas)kelasList.get(kelasIndexLect)).getKelasType());

                                    ((Timetable) tKelasList.get(kelasIndexLect)).setTimeslot(info, dayLect, timeLect, ((Subject) subjectList.get(s)).getLectHour());
                                }
                                else
                                {
                                    info.setKelas("NOCLASS");
                                    System.out.println("TAK MASUK ----------------------------> " + ((Timetable) tKelasList.get(kelasIndexLect)).getName().trim());
                                    notInKelas += ((Subject) subjectList.get(s)).getLectHour();
                                }

                                ((Timetable) tGroupList.get(t)).setTimeslot(info, dayLect, timeLect, ((Subject) subjectList.get(s)).getLectHour());
                                slottedGroup += ((Subject) subjectList.get(s)).getLectHour();
                                //------------------END ASSIGN LECTURE---------------------------------------------------------------------
                            } else {
                                System.out.print("Group/Class not in after 10k Iteration - ");
                                System.out.println(((Timetable) tGroupList.get(t)).getName().trim());
                                notIn += ((Subject) subjectList.get(s)).getLectHour();
                            }

                            //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

                            //LAB
                            lectIndex = checkLecturer(((Subject) subjectList.get(s)).getCode(), dayLab, timeLab, ((Subject) subjectList.get(s)).getLabHour());
                            kelasIndexLab = rg.nextInt(tKelasList.size());

                            for (int k = 0; (k < 100000 && !(((Timetable) tGroupList.get(t)).checkTimeslot(dayLab, timeLab, ((Subject) subjectList.get(s)).getLabHour()))); k++)
                            {
                                if (!(((Timetable) tGroupList.get(t)).checkTimeslot(dayLab, timeLab, ((Subject) subjectList.get(s)).getLabHour())))
                                {
                                    dayLab = new Random().nextInt(5);
                                    timeLab = new Random().nextInt(11 - ((Subject) subjectList.get(s)).getLabHour());
                                }
                            }



                            //FIND CLASS TIMESLOT
                            for (int k = 0; (k < 100000 && !((Timetable)tKelasList.get(kelasIndexLab)).checkTimeslot(dayLab,timeLab,((Subject) subjectList.get(s)).getLabHour())); k++)
                            {

                                if (!((Timetable) tKelasList.get(kelasIndexLab)).checkTimeslot(dayLab, timeLab, ((Subject) subjectList.get(s)).getLabHour())) {
                                    kelasIndexLab = rg.nextInt(tKelasList.size());
                                }
                            }

                            if ((((Timetable) tGroupList.get(t)).checkTimeslot(dayLab, timeLab, ((Subject) subjectList.get(s)).getLabHour())) && // labgroup
                                    ((Timetable)tKelasList.get(kelasIndexLab)).checkTimeslot(dayLab,timeLab,((Subject) subjectList.get(s)).getLabHour())) {
                                info = new Information();
                                //setGroup
                                info.setStudnum(((Group) ((Subject) subjectList.get(s)).getGroupList().get(g)).getStudNum());
                                info.setGroup(((Timetable) tGroupList.get(t)).getName().trim());

                                //setSubject
                                info.setSubjectCode(((Subject) subjectList.get(s)).getCode().trim());
                                info.setSubjectHour(((Subject) subjectList.get(s)).getLabHour());
                                info.setSubjectType('M');

                                //setindex
                                info.setDay(dayLab);
                                info.setTime(timeLab);
                                info.setGroupIndex(t);
                                info.setKelasIndex(kelasIndexLab);
                                info.setLecturerIndex(lectIndex);

                                if ((lectIndex != -1)) {
                                    info.setLecturer(((Timetable) tLecturerList.get(lectIndex)).getName());
                                    ((Timetable) tLecturerList.get(lectIndex)).setTimeslot(info, dayLab, timeLab, ((Subject) subjectList.get(s)).getLabHour());
                                } else {
                                    info.setLecturer("PENDING");
                                }

                                //Class also special case , but can be solve be revoke initialization
                                if(((Timetable)tKelasList.get(kelasIndexLab)).checkTimeslot(dayLab,timeLab,((Subject) subjectList.get(s)).getLabHour()))
                                {
                                    //setKelas
                                    info.setKelas(((Timetable) tKelasList.get(kelasIndexLab)).getName());
                                    info.setKelasType(((Kelas) kelasList.get(kelasIndexLab)).getKelasType());
                                    info.setKelasCap(((Kelas) kelasList.get(kelasIndexLab)).getKelasCap());

                                    ((Timetable) tKelasList.get(kelasIndexLab)).setTimeslot(info, dayLab, timeLab, ((Subject) subjectList.get(s)).getLabHour());
                                    //System.out.println( ((Timetable) tKelasList.get(kelasIndexLab)).getTimeslot(dayLab,timeLab).getSubjectType() );


                                }
                                else
                                {
                                    info.setKelas("NOCLASS");
                                    System.out.println("TAK MASUK ----------------------------> " + ((Timetable) tKelasList.get(kelasIndexLab)).getName().trim());
                                    notInKelas += ((Subject) subjectList.get(s)).getLectHour();

                                }

                                ((Timetable) tGroupList.get(t)).setTimeslot(info, dayLab, timeLab, ((Subject) subjectList.get(s)).getLabHour());
                                slottedGroup += ((Subject) subjectList.get(s)).getLabHour();
                                //------------------END ASSIGN LAB---------------------------------------------------------------------
                            } else {
                                System.out.print("Group/Class not in after 10k Iteration - ");
                                System.out.println(((Timetable) tGroupList.get(t)).getName().trim());
                                notIn += ((Subject) subjectList.get(s)).getLabHour();
                            }

                        }//and of group matched
                    }
                }
            }
            //System.out.println(" Groupmatched subject == timetable : " + groupMatched);
            //printTimetableGroup();
            System.out.println(" Group subject not in : " + notIn  + "  Kelas not slotted : " + notInKelas + " Total slotted : " + slottedGroup);
            System.out.println("Group size : " + tGroupList.size() + " Kelas size : " + tKelasList.size() + " Lecturer size : " + tLecturerList.size() );
        }while(notIn > 0 || notInKelas > 0);
    }//end assignTT

    public  int checkLecturer(String subcode, int day, int time,int block)
    {


        for(int i = 0; i< lecturerList.size(); i++)
        {
            Lecturer lecturer = (Lecturer) lecturerList.get(i);
            for(int j=0; j< lecturer.getSubject().size(); j++)// lecturer subject list
            {
                //System.out.println(lecturer.getSubject().get(j) + " - " + subcode);
                if(subcode.trim().equalsIgnoreCase(lecturer.getSubject().get(j).trim()))//cari subject sama
                {
                    //System.out.println("CHECK CHECK CHECK CHECK CHECK");
                    //check lecturer nye slot plak
                    if(checkLecturerTimeslot(lecturer.getLecturer(),day,time,block))
                    {
                        //return index lecturer timetable , kalau dia empty
                        return i;
                    }
                }
            }
        }
        return -1;
    }


    public  boolean checkLecturerTimeslot(String name, int day, int time,int block)
    {
        for(int i = 0; i< tLecturerList.size(); i++)
        {
            Timetable temp = (Timetable) tLecturerList.get(i);
            if(name.trim().equalsIgnoreCase(temp.getName().trim()))
            {

                if( temp.checkTimeslot(day,time,block))
                {
                    return true;
                }
                else
                {
                    //System.out.println(temp.getName() + " " + day + " " + time);///check lecturer mane full
                    //System.out.println(temp.getName() + " timeslot occupied : " + temp.getTimeslot(day, time).toString());
                    //takleh print ni sebab nanti null point exeception sebab dia by block , boleh satu block okay satu block.
                }
            }
        }

        return false;
    }


    //TODO: buat print for groupdulu
    public  void printGroupList()
    {
        System.out.println("\n----------------------PRINT GROUP LIST-----------------------\n");
        for(int i = 0; i < tGroupList.size(); i++)
        {
            Timetable group = (Timetable) tGroupList.get(i);

            System.out.println(group.getName());

        }

    }
    //OBJECTIVE FUNCTION IS TO MINIMIZE | FITNESSS POINT == PENALTY
    public void initializeFitness()
    {

        for(int t=0; t<tGroupList.size(); t++)
        {
            //System.out.println(((Timetable) ReadData.tGroupList.get(t)).getName() );
            for(int day =0; day <5; day++)
            {
                for(int time =0; time < 10; time++)
                {

                    if(!(((Timetable)tGroupList.get(t)).checkTimeslot(day,time)))
                    {
                        ((Timetable) tGroupList.get(t)).getTimeslot(day,time).setFitness(0);            }

                }
            }
        }
    }


}
