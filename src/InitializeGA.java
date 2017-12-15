import com.sun.org.apache.regexp.internal.RE;
import javafx.beans.binding.BooleanExpression;

import javax.sound.sampled.Line;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class InitializeGA {

    //public static ReadData reader;
    public static ArrayList allTimeTable;
    public static Population population;
    public InitializeGA(Population population) throws IOException, InterruptedException
    {

        this.population = population;

        allTimeTable = new ArrayList<>();

        //assignTimetable();
        assignTT();
        initializeFitness();//set all fitness to zero before calculate
        ///printGroupList();
    }

    public static void assignTT() throws InterruptedException, IOException {

        int notIn;
        int notInKelas;

        do {
            notIn =0;
            notInKelas =0;

            population = new Population();
            for (int t = 0; t < population.tGroupList.size(); t++) {
                for (int s = 0; s < population.subjectList.size(); s++) {
                    for (int g = 0; g < ((Subject) population.subjectList.get(s)).getGroupList().size(); g++) {
                        if (((Group) ((Subject) population.subjectList.get(s)).getGroupList().get(g)).getCode().equalsIgnoreCase(((Timetable) population.tGroupList.get(t)).getName().trim())) {

                            Random rg = new Random();
                            Information info;
                            int dayTuto = rg.nextInt(5);
                            int timeTuto = rg.nextInt(11 - ((Subject) population.subjectList.get(s)).getTutoHour());

                            int dayLect = rg.nextInt(5);
                            int timeLect = rg.nextInt(11 - ((Subject) population.subjectList.get(s)).getLectHour());

                            int dayLab = rg.nextInt(5);
                            int timeLab = rg.nextInt(11 - ((Subject) population.subjectList.get(s)).getLabHour());


                            int kelasIndexTuto = rg.nextInt(population.tKelasList.size());
                            int kelasIndexLect = rg.nextInt(population.tKelasList.size());
                            int kelasIndexLab = rg.nextInt(population.tKelasList.size());

                            int lectIndex = checkLecturer(((Subject) population.subjectList.get(s)).getCode(), dayTuto, timeTuto, ((Subject) population.subjectList.get(s)).getTutoHour());

                            do {

                                 dayTuto = rg.nextInt(5);
                                 timeTuto = rg.nextInt(11 - ((Subject) population.subjectList.get(s)).getTutoHour());

                                 dayLect = rg.nextInt(5);
                                 timeLect = rg.nextInt(11 - ((Subject) population.subjectList.get(s)).getLectHour());

                                 dayLab = rg.nextInt(5);
                                 timeLab = rg.nextInt(11 - ((Subject) population.subjectList.get(s)).getLabHour());


                                kelasIndexTuto = rg.nextInt(population.tKelasList.size());
                                kelasIndexLect = rg.nextInt(population.tKelasList.size());
                                kelasIndexLab = rg.nextInt(population.tKelasList.size());

                                 lectIndex = checkLecturer(((Subject) population.subjectList.get(s)).getCode(), dayTuto, timeTuto, ((Subject) population.subjectList.get(s)).getTutoHour());
//
//                                 System.out.println(String.format("%-15s","Iteration group") + String.format("%-15s","Tuto") + String.format("%-15s","Lecture") +String.format("%-15s","Lab") + String.format("%-15s","Kelas"));
//                                System.out.println(String.format("%-15s",t) + String.format("%-15s",dayTuto+ " " + timeTuto) + String.format("%-15s",dayLect +" " + timeLect) +
//                                                    String.format("%-15s",dayLab+" "+timeLab) + String.format("%-15s",kelasIndexTuto + " "+ kelasIndexLect + " " + kelasIndexLab));

                            }while( !(((Timetable) population.tGroupList.get(t)).checkTimeslot(dayTuto, timeTuto, ((Subject) population.subjectList.get(s)).getTutoHour())) && //tutogroup
                                    !((Timetable) population.tKelasList.get(kelasIndexTuto)).checkTimeslot(dayTuto, timeTuto, ((Subject) population.subjectList.get(s)).getTutoHour()) &&//tutokelas

                                    !(((Timetable) population.tGroupList.get(t)).checkTimeslot(dayLect, timeLect, ((Subject) population.subjectList.get(s)).getLectHour())) &&//lecturegroup
                                    !((Timetable)population.tKelasList.get(kelasIndexLect)).checkTimeslot(dayLect,timeLect,((Subject) population.subjectList.get(s)).getLectHour()) &&//lecturekelas

                                    !(((Timetable) population.tGroupList.get(t)).checkTimeslot(dayLab, timeLab, ((Subject) population.subjectList.get(s)).getLabHour())) && // labgroup
                                    !((Timetable)population.tKelasList.get(kelasIndexLab)).checkTimeslot(dayLab,timeLab,((Subject) population.subjectList.get(s)).getLabHour()) //labkelas
                                    );//bruteforcing randomly, please work

                            //try 100 kali je
                            //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

                            //FIND GROUP TIME SLOT
                            for (int k = 0; (k < 100000 && !(((Timetable) population.tGroupList.get(t)).checkTimeslot(dayTuto, timeTuto, ((Subject) population.subjectList.get(s)).getTutoHour()))); k++) {
                                if (!(((Timetable) population.tGroupList.get(t)).checkTimeslot(dayTuto, timeTuto, ((Subject) population.subjectList.get(s)).getTutoHour()))) {
                                    dayTuto = new Random().nextInt(5);
                                    timeTuto = new Random().nextInt(11 - ((Subject) population.subjectList.get(s)).getTutoHour());
                                }
                            }
                            //FIND CLASS TIMESLOT
                            for (int k = 0; (k < 100000 && !((Timetable) population.tKelasList.get(kelasIndexTuto)).checkTimeslot(dayTuto, timeTuto, ((Subject) population.subjectList.get(s)).getTutoHour())); k++) {
                                if (!((Timetable) population.tKelasList.get(kelasIndexTuto)).checkTimeslot(dayTuto, timeTuto, ((Subject) population.subjectList.get(s)).getTutoHour())) {
                                    kelasIndexTuto = rg.nextInt(population.tKelasList.size());
                                }
                            }


                            if ((((Timetable) population.tGroupList.get(t)).checkTimeslot(dayTuto, timeTuto, ((Subject) population.subjectList.get(s)).getTutoHour())) &&
                                    ((Timetable) population.tKelasList.get(kelasIndexTuto)).checkTimeslot(dayTuto, timeTuto, ((Subject) population.subjectList.get(s)).getTutoHour())) {
                                //------------------ASSIGN TUTORIAL---------------------------------------------------------------------
                                info = new Information();

                                //setGroup
                                info.setGroup(((Timetable) population.tGroupList.get(t)).getName().trim());
                                info.setStudnum(((Group) ((Subject) population.subjectList.get(s)).getGroupList().get(g)).getStudNum());

                                //setSubject
                                info.setSubjectCode(((Subject) population.subjectList.get(s)).getCode().trim());
                                info.setSubjectHour(((Subject) population.subjectList.get(s)).getTutoHour());

                                //setindex
                                info.setGroupIndex(t);
                                info.setKelasIndex(kelasIndexTuto);
                                info.setLecturerIndex(lectIndex);


                                //LECTURER SPECIAL CASE - NUMBER OF LECTURER IS NOT ENOUGH TO CATER ALL GROUP
                                if (lectIndex != -1) {
                                    info.setLecturer(((Timetable) population.tLecturerList.get(lectIndex)).getName());
                                    ((Timetable) population.tLecturerList.get(lectIndex)).setTimeslot(info, dayTuto, timeTuto, ((Subject) population.subjectList.get(s)).getTutoHour());
                                } else {
                                    info.setLecturer("PENDING");
                                }
                                //KELAS
                                if(((Timetable)population.tKelasList.get(kelasIndexTuto)).checkTimeslot(dayTuto,timeTuto,((Subject) population.subjectList.get(s)).getTutoHour()))
                                {

                                    //setKelas
                                    info.setKelas(((Timetable) population.tKelasList.get(kelasIndexTuto)).getName());
                                    info.setKelasType(((Kelas) population.kelasList.get(kelasIndexTuto)).getKelasType());
                                    info.setKelasCap(((Kelas) population.kelasList.get(kelasIndexTuto)).getKelasCap());

                                    ((Timetable) population.tKelasList.get(kelasIndexTuto)).setTimeslot(info, dayTuto, timeTuto, ((Subject) population.subjectList.get(s)).getTutoHour());

                                }
                                else
                                {
                                    info.setKelas("NOCLASS");
                                    System.out.println("TAK MASUK ----------------------------> " + ((Timetable) population.tKelasList.get(kelasIndexTuto)).getName().trim());
                                    notInKelas += ((Subject) population.subjectList.get(s)).getTutoHour();
                                }

                                ((Timetable) population.tGroupList.get(t)).setTimeslot(info, dayTuto, timeTuto, ((Subject) population.subjectList.get(s)).getTutoHour());
                                //------------------END ASSIGN TUTORIAL---------------------------------------------------------------------
                            } else {
                                System.out.print("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
                                System.out.println(((Timetable) population.tGroupList.get(t)).getName().trim());
                                notIn += ((Subject) population.subjectList.get(s)).getTutoHour();
                            }



                            //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

                            //LECTURER------------------------------------------------------------------------------------
                            lectIndex = checkLecturer(((Subject) population.subjectList.get(s)).getCode(), dayLect, timeLect, ((Subject) population.subjectList.get(s)).getLectHour());
                            for (int k = 0; (k < 100000 && !(((Timetable) population.tGroupList.get(t)).checkTimeslot(dayLect, timeLect, ((Subject) population.subjectList.get(s)).getLectHour()))); k++) {
                                if (!(((Timetable) population.tGroupList.get(t)).checkTimeslot(dayLect, timeLect, ((Subject) population.subjectList.get(s)).getLectHour()))) {
                                    dayLect = new Random().nextInt(5);
                                    timeLect = new Random().nextInt(11 - ((Subject) population.subjectList.get(s)).getLectHour());
                                }
                            }


                            //FIND CLASS TIMESLOT
                            for (int k = 0; (k < 100000 && !((Timetable)population.tKelasList.get(kelasIndexLect)).checkTimeslot(dayLect,timeLect,((Subject) population.subjectList.get(s)).getLectHour())); k++) {

                                if (!((Timetable) population.tKelasList.get(kelasIndexLect)).checkTimeslot(dayLect, timeLect, ((Subject) population.subjectList.get(s)).getLectHour())) {
                                    kelasIndexLect = rg.nextInt(population.tKelasList.size());
                                }
                            }

                            if ((((Timetable) population.tGroupList.get(t)).checkTimeslot(dayLect, timeLect, ((Subject) population.subjectList.get(s)).getLectHour())) &&//lecturegroup
                                    ((Timetable)population.tKelasList.get(kelasIndexLect)).checkTimeslot(dayLect,timeLect,((Subject) population.subjectList.get(s)).getLectHour())) {
                                //------------------ASSIGN LECTURE---------------------------------------------------------------------

                                info = new Information();
                                //setGroup
                                info.setStudnum(((Group) ((Subject) population.subjectList.get(s)).getGroupList().get(g)).getStudNum());
                                info.setGroup(((Timetable) population.tGroupList.get(t)).getName().trim());

                                //setSubject
                                info.setSubjectCode(((Subject) population.subjectList.get(s)).getCode().trim());
                                info.setSubjectHour(((Subject) population.subjectList.get(s)).getTutoHour());

                                //setindex
                                info.setGroupIndex(t);
                                info.setKelasIndex(kelasIndexLect);
                                info.setLecturerIndex(lectIndex);

                                if ((lectIndex != -1)) {
                                    info.setLecturer(((Timetable) population.tLecturerList.get(lectIndex)).getName());
                                    ((Timetable) population.tLecturerList.get(lectIndex)).setTimeslot(info, dayLect, timeLect, ((Subject) population.subjectList.get(s)).getLectHour());
                                } else {
                                    info.setLecturer("PENDING");
                                }

                                //Class also special case , but can be solve be revoke initialization
                                if(((Timetable)population.tKelasList.get(kelasIndexLect)).checkTimeslot(dayLect,timeLect,((Subject) population.subjectList.get(s)).getLectHour()))
                                {
                                    //setKelas
                                    info.setKelas(((Timetable) population.tKelasList.get(kelasIndexLect)).getName());
                                    info.setKelasCap(((Kelas)population.kelasList.get(kelasIndexLect)).getKelasCap());
                                    info.setKelasType(((Kelas)population.kelasList.get(kelasIndexLect)).getKelasType());

                                    ((Timetable) population.tKelasList.get(kelasIndexLect)).setTimeslot(info, dayLect, timeLect, ((Subject) population.subjectList.get(s)).getLectHour());
                                }
                                else
                                {
                                    info.setKelas("NOCLASS");
                                    System.out.println("TAK MASUK ----------------------------> " + ((Timetable) population.tKelasList.get(kelasIndexLect)).getName().trim());
                                    notInKelas += ((Subject) population.subjectList.get(s)).getLectHour();
                                }

                                ((Timetable) population.tGroupList.get(t)).setTimeslot(info, dayLect, timeLect, ((Subject) population.subjectList.get(s)).getLectHour());
                                //------------------END ASSIGN LECTURE---------------------------------------------------------------------
                            } else {
                                System.out.print("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
                                System.out.println(((Timetable) population.tGroupList.get(t)).getName().trim());
                                notIn += ((Subject) population.subjectList.get(s)).getLectHour();
                            }

                            //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

                            //LAB
                            lectIndex = checkLecturer(((Subject) population.subjectList.get(s)).getCode(), dayLect, timeLect, ((Subject) population.subjectList.get(s)).getLectHour());
                            kelasIndexLab = rg.nextInt(population.tKelasList.size());

                            for (int k = 0; (k < 100000 && !(((Timetable) population.tGroupList.get(t)).checkTimeslot(dayLab, timeLab, ((Subject) population.subjectList.get(s)).getLabHour()))); k++)
                            {
                                if (!(((Timetable) population.tGroupList.get(t)).checkTimeslot(dayLab, timeLab, ((Subject) population.subjectList.get(s)).getLabHour())))
                                {
                                    dayLab = new Random().nextInt(5);
                                    timeLab = new Random().nextInt(11 - ((Subject) population.subjectList.get(s)).getLabHour());
                                }
                            }



                            //FIND CLASS TIMESLOT
                            for (int k = 0; (k < 100000 && !((Timetable)population.tKelasList.get(kelasIndexLab)).checkTimeslot(dayLab,timeLab,((Subject) population.subjectList.get(s)).getLabHour())); k++)
                            {

                                if (!((Timetable) population.tKelasList.get(kelasIndexLab)).checkTimeslot(dayLab, timeLab, ((Subject) population.subjectList.get(s)).getLabHour())) {
                                    kelasIndexLab = rg.nextInt(population.tKelasList.size());
                                }
                            }

                            if ((((Timetable) population.tGroupList.get(t)).checkTimeslot(dayLab, timeLab, ((Subject) population.subjectList.get(s)).getLabHour())) && // labgroup
                                    ((Timetable)population.tKelasList.get(kelasIndexLab)).checkTimeslot(dayLab,timeLab,((Subject) population.subjectList.get(s)).getLabHour())) {
                                info = new Information();
                                //setGroup
                                info.setStudnum(((Group) ((Subject) population.subjectList.get(s)).getGroupList().get(g)).getStudNum());
                                info.setGroup(((Timetable) population.tGroupList.get(t)).getName().trim());

                                //setSubject
                                info.setSubjectCode(((Subject) population.subjectList.get(s)).getCode().trim());
                                info.setSubjectHour(((Subject) population.subjectList.get(s)).getTutoHour());

                                //setindex
                                info.setGroupIndex(t);
                                info.setKelasIndex(kelasIndexLab);
                                info.setLecturerIndex(lectIndex);

                                if ((lectIndex != -1)) {
                                    info.setLecturer(((Timetable) population.tLecturerList.get(lectIndex)).getName());
                                    ((Timetable) population.tLecturerList.get(lectIndex)).setTimeslot(info, dayLab, timeLab, ((Subject) population.subjectList.get(s)).getLabHour());
                                } else {
                                    info.setLecturer("PENDING");
                                }

                                //Class also special case , but can be solve be revoke initialization
                                if(((Timetable)population.tKelasList.get(kelasIndexLab)).checkTimeslot(dayLab,timeLab,((Subject) population.subjectList.get(s)).getLabHour()))
                                {
                                    //setKelas
                                    info.setKelas(((Timetable) population.tKelasList.get(kelasIndexLab)).getName());
                                    info.setKelasType(((Kelas) population.kelasList.get(kelasIndexLab)).getKelasType());
                                    info.setKelasCap(((Kelas) population.kelasList.get(kelasIndexLab)).getKelasCap());

                                    ((Timetable) population.tKelasList.get(kelasIndexLab)).setTimeslot(info, dayLab, timeLab, ((Subject) population.subjectList.get(s)).getLabHour());

                                }
                                else
                                {
                                    info.setKelas("NOCLASS");
                                    System.out.println("TAK MASUK ----------------------------> " + ((Timetable) population.tKelasList.get(kelasIndexLab)).getName().trim());
                                    notInKelas += ((Subject) population.subjectList.get(s)).getLectHour();

                                }

                                ((Timetable) population.tGroupList.get(t)).setTimeslot(info, dayLab, timeLab, ((Subject) population.subjectList.get(s)).getLabHour());
                                //------------------END ASSIGN LAB---------------------------------------------------------------------
                            } else {
                                System.out.print("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
                                System.out.println(((Timetable) population.tGroupList.get(t)).getName().trim());
                                notIn += ((Subject) population.subjectList.get(s)).getLabHour();
                            }

                        }//and of group matched
                    }
                }
            }
            //System.out.println(" Groupmatched subject == timetable : " + groupMatched);
            //printTimetableGroup();
            System.out.println(" Group subject not in : " + notIn  + "  Kelas not slotted : " + notInKelas);
            System.out.println("Group size : " + population.tGroupList.size() + " Kelas size : " + population.tKelasList.size() + " Lecturer size : " + population.tLecturerList.size() );
        }while(notIn > 0 || notInKelas > 0);
    }//end assignTT

    public static int checkLecturer(String subcode, int day, int time,int block)
    {


        for(int i = 0; i< population.lecturerList.size(); i++)
        {
            Lecturer lecturer = (Lecturer) population.lecturerList.get(i);
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
        // takde lecturer yang boleh, cater.
        return -1;
    }


    public static boolean checkLecturerTimeslot(String name, int day, int time,int block)
    {
        for(int i = 0; i< population.tLecturerList.size(); i++)
        {
            Timetable temp = (Timetable) population.tLecturerList.get(i);
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
    public static void printGroupList()
    {
        System.out.println("\n----------------------PRINT GROUP LIST-----------------------\n");
        for(int i = 0; i < population.tGroupList.size(); i++)
        {
            Timetable group = (Timetable) population.tGroupList.get(i);

            System.out.println(group.getName());

        }

    }
    //OBJECTIVE FUNCTION IS TO MINIMIZE | FITNESSS POINT == PENALTY
    public static void initializeFitness()
    {

        for(int t=0; t<population.tGroupList.size(); t++)
        {
            //System.out.println(((Timetable) ReadData.tGroupList.get(t)).getName() );
            for(int day =0; day <5; day++)
            {
                for(int time =0; time < 10; time++)
                {

                    if(!(((Timetable)population.tGroupList.get(t)).checkTimeslot(day,time)))
                    {
                        ((Timetable) population.tGroupList.get(t)).getTimeslot(day,time).setFitness(0);            }

                }
            }
        }
    }


}
