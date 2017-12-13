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
        calculateFitness();
        printTimetableGroup();
        ///printGroupList();
    }

    public static void assignTimetable()
    {

        //Timetable lecturer = (Timetable) reader.lecturer;
        //Timetable kelas = (Timetable) reader.kelas;

        Subject subject;
        int groupMatched =0;

        for(int i = 0; i < population.subjectList.size(); i++)//pergi setiap subject
        {

            ArrayList temp = ((Subject) population.subjectList.get(i)).getGroupList();
            for(int j=0; j< temp.size(); j++)//pergi setiap group dalam subject
            {
                Group groupTemp = (Group)  temp.get(j);//group dalam subject


               // assignTutorial();
                for(int x = 0; x < population.tGroupList.size(); x++)//pergi setiap group dalam timetable.
                {
                    Timetable groupTimetable = (Timetable) population.tGroupList.get(x);//group dari main
                    if(groupTemp.getCode().trim().equalsIgnoreCase(groupTimetable.getName().trim()))//check sama sebab nak amik based on subject.
                    {

                        int tutohour = ((Subject) population.subjectList.get(i)).getTutoHour();
                        int lectureHour = ((Subject) population.subjectList.get(i)).getLectHour();
                        int labHour = ((Subject) population.subjectList.get(i)).getLabHour();
                        int dayTuto = new Random().nextInt(5 );
                        int timeTuto = new Random().nextInt(10 - tutohour);
                        int lectIndex = checkLecturer(((Subject) population.subjectList.get(i)).getCode(),dayTuto,timeTuto,tutohour);

                        if((groupTimetable.checkTimeslot(dayTuto,timeTuto,tutohour) && (lectIndex != -1)) )//check group/lecturer nye slot
                        {
                            //------------------ASSIGN TUTORIAL---------------------------------------------------------------------
                            groupMatched += tutohour;//TODO: DIA 250 JE SEBAB ADE YANG TAKDE TUTORIAL STEWPIG hour = 0
                            Information info = new Information();
                            info.setGroup(groupTimetable.getName().trim());
                            info.setSubjectCode(((Subject) population.subjectList.get(i)).getCode().trim());
                            info.setStudnum(groupTemp.getStudNum());
                            info.setLecturer(((Timetable) population.tLecturerList.get(lectIndex)).getName());
                            ((Timetable) population.tGroupList.get(x)).setTimeslot(info, dayTuto, timeTuto, tutohour);
                            //------------------END ASSIGN TUTORIAL---------------------------------------------------------------------
                            int dayLect = new Random().nextInt(5 );
                            int timeLect = new Random().nextInt(10 - lectureHour);
                            lectIndex = checkLecturer(((Subject) population.subjectList.get(i)).getCode(),dayLect,timeLect,lectureHour);


                            if((groupTimetable.checkTimeslot(dayLect,timeLect,lectureHour) && (lectIndex != -1)))//check group/lecturer nye slot
                            {
                                //-------------------------ASSIGN LECTURER-------------------------------------------------------------
                                info = new Information();
                                info.setGroup(groupTimetable.getName().trim());
                                info.setSubjectCode(((Subject) population.subjectList.get(i)).getCode().trim());
                                info.setStudnum(groupTemp.getStudNum());
                                info.setLecturer(((Timetable) population.tLecturerList.get(lectIndex)).getName());
                                ((Timetable) population.tGroupList.get(x)).setTimeslot(info, dayLect, timeLect, lectureHour);

                                //------------------------- END ASSIGN LECTURER-------------------------------------------------------------
                                int dayLab = new Random().nextInt(5 );
                                int timeLab = new Random().nextInt(10 - labHour);
                                lectIndex = checkLecturer(((Subject) population.subjectList.get(i)).getCode(),dayLab,timeLab,labHour);
                                if((groupTimetable.checkTimeslot(dayLab,timeLab,labHour) && (lectIndex != -1)))
                                {
                                    //-------------------------ASSIGN LAB-------------------------------------------------------------

                                    info = new Information();
                                    info.setGroup(groupTimetable.getName().trim());
                                    info.setSubjectCode(((Subject) population.subjectList.get(i)).getCode().trim());
                                    info.setStudnum(groupTemp.getStudNum());
                                    info.setLecturer(((Timetable) population.tLecturerList.get(lectIndex)).getName());
                                    ((Timetable) population.tGroupList.get(x)).setTimeslot(info, dayLab, timeLab, labHour);
                                    //------------------------- END ASSIGN LAB-------------------------------------------------------------
                                }
                                else{
                                    ((Timetable) population.tGroupList.get(x)).clearTimeslot(dayLect, timeLect, tutohour);
                                    ((Timetable) population.tGroupList.get(x)).clearTimeslot(dayTuto, timeTuto, tutohour);
                                    x--;
                                }
                            }
                            else{
                                ((Timetable) population.tGroupList.get(x)).clearTimeslot(dayTuto, timeTuto, tutohour);
                                x--;
                            }
                        }
                        else{
                            x--;
                        }
                            //check dah masuk belum
//                            if(groupTimetable.checkTimeslot(day,time,hour))
//                            {
//                                //clear garbage
//                                System.out.println(info.toString() + " Tak Masuk");
//                                ((Timetable) ReadData.tGroupList.get(x)).setTimeslot(new Information(), day, time, hour);
//                                ((Timetable) ReadData.tGroupList.get(x)).setTimeslot(info, day, time, hour);
//
//                            }
//                            else{
//                                System.out.println(info.toString() + "Masuk");
//
//                                }
                            //lecturer.setTimeslot(info, day, time, hour);
                            //TODO: CHECK KENAPA TIMETABLE LIST PEGANG 250 JE ?

                            //allTimeTable.add(info);
                            //takpayah set dorang auto refer?
                            //ReadData.tGroupList.set(x, groupTimetable);
                            //ReadData.tLecturerList.set(lectIndex, lecturer);

                    }//end of assigning

                }//end of x groupList (timetable)
            }// end of j (subject - groupList)
        }// end of i (subjectlist)

        System.out.println(" Groupmatched subject == timetable : " + groupMatched);
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


                            int kelasIndex = rg.nextInt(population.tKelasList.size());

                            int lectIndex = checkLecturer(((Subject) population.subjectList.get(s)).getCode(), dayTuto, timeTuto, ((Subject) population.subjectList.get(s)).getTutoHour());

                            do {

                                 dayTuto = rg.nextInt(5);
                                 timeTuto = rg.nextInt(11 - ((Subject) population.subjectList.get(s)).getTutoHour());

                                 dayLect = rg.nextInt(5);
                                 timeLect = rg.nextInt(11 - ((Subject) population.subjectList.get(s)).getLectHour());

                                 dayLab = rg.nextInt(5);
                                 timeLab = rg.nextInt(11 - ((Subject) population.subjectList.get(s)).getLabHour());


                                 kelasIndex = rg.nextInt(population.tKelasList.size());

                                 lectIndex = checkLecturer(((Subject) population.subjectList.get(s)).getCode(), dayTuto, timeTuto, ((Subject) population.subjectList.get(s)).getTutoHour());

                                 System.out.println(String.format("%-15s","Iteration group") + String.format("%-15s","Tuto") + String.format("%-15s","Lecture") +String.format("%-15s","Lab") + String.format("%-15s","Kelas"));
                                System.out.println(String.format("%-15s",t) + String.format("%-15s",dayTuto+ " " + timeTuto) + String.format("%-15s",dayLect +" " + timeLect) +
                                                    String.format("%-15s",dayLab+" "+timeLab) + String.format("%-15s",kelasIndex));
                            }while( !(((Timetable) population.tGroupList.get(t)).checkTimeslot(dayTuto, timeTuto, ((Subject) population.subjectList.get(s)).getTutoHour())) && //tutogroup
                                    !((Timetable) population.tKelasList.get(kelasIndex)).checkTimeslot(dayTuto, timeTuto, ((Subject) population.subjectList.get(s)).getTutoHour()) &&//tutokelas
                                    !(((Timetable) population.tGroupList.get(t)).checkTimeslot(dayLect, timeLect, ((Subject) population.subjectList.get(s)).getLectHour())) &&//lecturegroup
                                    !((Timetable)population.tKelasList.get(kelasIndex)).checkTimeslot(dayLect,timeLect,((Subject) population.subjectList.get(s)).getLectHour()) &&//lecturekelas
                                    !(((Timetable) population.tGroupList.get(t)).checkTimeslot(dayLab, timeLab, ((Subject) population.subjectList.get(s)).getLabHour())) && // labgroup
                                    !((Timetable)population.tKelasList.get(kelasIndex)).checkTimeslot(dayLab,timeLab,((Subject) population.subjectList.get(s)).getLabHour()) //labkelas
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

                            //ASSIGN CLASS TIMESLOT
                            for (int k = 0; (k < 100000 && !((Timetable) population.tKelasList.get(kelasIndex)).checkTimeslot(dayTuto, timeTuto, ((Subject) population.subjectList.get(s)).getTutoHour())); k++)
                            {


                                if(!((Timetable)population.tKelasList.get(kelasIndex)).checkTimeslot(dayTuto,timeTuto,((Subject) population.subjectList.get(s)).getTutoHour()))
                                {
                                    kelasIndex = rg.nextInt(population.tKelasList.size());
                                }
                            }

//
//                            while(!((Timetable)ReadData.tKelasList.get(kelasIndex)).checkTimeslot(dayTuto,timeTuto,((Subject) ReadData.subjectList.get(s)).getTutoHour()))
//                                {
//                                    kelasIndex = rg.nextInt(ReadData.tKelasList.size());
//                                    System.out.println("TAK MASUK ----------------------------> " + ((Timetable) ReadData.tKelasList.get(kelasIndex)).getName().trim() + "  " +  dayTuto + "   " + timeTuto + " iteration " + t);
//                                }

                            if ((((Timetable) population.tGroupList.get(t)).checkTimeslot(dayTuto, timeTuto, ((Subject) population.subjectList.get(s)).getTutoHour()))) {
                                //------------------ASSIGN TUTORIAL---------------------------------------------------------------------
                                info = new Information();
                                info.setGroup(((Timetable) population.tGroupList.get(t)).getName().trim());
                                info.setSubjectCode(((Subject) population.subjectList.get(s)).getCode().trim());
                                info.setStudnum(((Group) ((Subject) population.subjectList.get(s)).getGroupList().get(g)).getStudNum());

                                //LECTURER SPECIAL CASE - NUMBER OF LECTURER IS NOT ENOUGH TO CATER ALL GROUP
                                if (lectIndex != -1) {
                                    info.setLecturer(((Timetable) population.tLecturerList.get(lectIndex)).getName());
                                    ((Timetable) population.tLecturerList.get(lectIndex)).setTimeslot(info, dayTuto, timeTuto, ((Subject) population.subjectList.get(s)).getTutoHour());
                                } else {
                                    info.setLecturer("PENDING");
                                }
                                //Class also special case , but can be solve be revoke initialization
                                if(((Timetable)population.tKelasList.get(kelasIndex)).checkTimeslot(dayTuto,timeTuto,((Subject) population.subjectList.get(s)).getTutoHour()))
                                {
                                    info.setKelas(((Timetable) population.tKelasList.get(kelasIndex)).getName());
                                    ((Timetable) population.tKelasList.get(kelasIndex)).setTimeslot(info, dayTuto, timeTuto, ((Subject) population.subjectList.get(s)).getTutoHour());
                                }
                                else
                                {
                                    info.setKelas("NOCLASS");
                                    System.out.println("TAK MASUK ----------------------------> " + ((Timetable) population.tKelasList.get(kelasIndex)).getName().trim());
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
                            lectIndex = checkLecturer(((Subject) population.subjectList.get(s)).getCode(), dayLect, timeLect, ((Subject) population.subjectList.get(s)).getLectHour());
                            kelasIndex = rg.nextInt(population.tKelasList.size());
                            for (int k = 0; (k < 100000 && !(((Timetable) population.tGroupList.get(t)).checkTimeslot(dayLect, timeLect, ((Subject) population.subjectList.get(s)).getLectHour()))); k++) {

                                if (!(((Timetable) population.tGroupList.get(t)).checkTimeslot(dayLect, timeLect, ((Subject) population.subjectList.get(s)).getLectHour())))
                                {
                                    dayLect = new Random().nextInt(5);
                                    timeLect = new Random().nextInt(11 - ((Subject) population.subjectList.get(s)).getLectHour());
                                }

                            }
                            //ASSIGN CLASS TIMESLOT
                            for (int k = 0; (k < 100000 && !((Timetable)population.tKelasList.get(kelasIndex)).checkTimeslot(dayLect,timeLect,((Subject) population.subjectList.get(s)).getLectHour())); k++) {

                                if(!((Timetable)population.tKelasList.get(kelasIndex)).checkTimeslot(dayLect,timeLect,((Subject) population.subjectList.get(s)).getLectHour()))
                                {
                                    kelasIndex = rg.nextInt(population.tKelasList.size());
                                }

                            }


                            if ((((Timetable) population.tGroupList.get(t)).checkTimeslot(dayLect, timeLect, ((Subject) population.subjectList.get(s)).getLectHour()))) {
                                //------------------ASSIGN LECTURE---------------------------------------------------------------------

                                info = new Information();
                                info.setGroup(((Timetable) population.tGroupList.get(t)).getName().trim());
                                info.setSubjectCode(((Subject) population.subjectList.get(s)).getCode().trim());
                                info.setStudnum(((Group) ((Subject) population.subjectList.get(s)).getGroupList().get(g)).getStudNum());


                                if ((lectIndex != -1)) {
                                    info.setLecturer(((Timetable) population.tLecturerList.get(lectIndex)).getName());
                                    ((Timetable) population.tLecturerList.get(lectIndex)).setTimeslot(info, dayLect, timeLect, ((Subject) population.subjectList.get(s)).getLectHour());
                                } else {
                                    info.setLecturer("PENDING");
                                }

                                //Class also special case , but can be solve be revoke initialization
                                if(((Timetable)population.tKelasList.get(kelasIndex)).checkTimeslot(dayLect,timeLect,((Subject) population.subjectList.get(s)).getLectHour()))
                                {
                                    info.setKelas(((Timetable) population.tKelasList.get(kelasIndex)).getName());
                                    ((Timetable) population.tKelasList.get(kelasIndex)).setTimeslot(info, dayLect, timeLect, ((Subject) population.subjectList.get(s)).getLectHour());
                                }
                                else
                                {
                                    info.setKelas("NOCLASS");
                                    System.out.println("TAK MASUK ----------------------------> " + ((Timetable) population.tKelasList.get(kelasIndex)).getName().trim());
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
                            lectIndex = checkLecturer(((Subject) population.subjectList.get(s)).getCode(), dayLect, timeLect, ((Subject) population.subjectList.get(s)).getLectHour());
                            kelasIndex = rg.nextInt(population.tKelasList.size());
                            for (int k = 0; (k < 100000 && !(((Timetable) population.tGroupList.get(t)).checkTimeslot(dayLab, timeLab, ((Subject) population.subjectList.get(s)).getLabHour()))); k++) {
                                if (!(((Timetable) population.tGroupList.get(t)).checkTimeslot(dayLab, timeLab, ((Subject) population.subjectList.get(s)).getLabHour()))) {
                                    dayLab = new Random().nextInt(5);
                                    timeLab = new Random().nextInt(11 - ((Subject) population.subjectList.get(s)).getLabHour());
                                }
                            }


                            for (int k = 0; (k < 100000 && !((Timetable)population.tKelasList.get(kelasIndex)).checkTimeslot(dayLab,timeLab,((Subject) population.subjectList.get(s)).getLabHour())); k++) {

                                //ASSIGN CLASS TIMESLOT
                                if(!((Timetable)population.tKelasList.get(kelasIndex)).checkTimeslot(dayLab,timeLab,((Subject) population.subjectList.get(s)).getLabHour()))
                                {
                                    kelasIndex = rg.nextInt(population.tKelasList.size());
                                }
                            }


                            if ((((Timetable) population.tGroupList.get(t)).checkTimeslot(dayLab, timeLab, ((Subject) population.subjectList.get(s)).getLabHour()))) {
                                info = new Information();
                                info.setGroup(((Timetable) population.tGroupList.get(t)).getName().trim());
                                info.setSubjectCode(((Subject) population.subjectList.get(s)).getCode().trim());
                                info.setStudnum(((Group) ((Subject) population.subjectList.get(s)).getGroupList().get(g)).getStudNum());
                                //info.setKelas(((Timetable) ReadData.tKelasList.get(kelasIndex)).getName());

                                if ((lectIndex != -1)) {
                                    info.setLecturer(((Timetable) population.tLecturerList.get(lectIndex)).getName());
                                    ((Timetable) population.tLecturerList.get(lectIndex)).setTimeslot(info, dayLab, timeLab, ((Subject) population.subjectList.get(s)).getLabHour());
                                } else {
                                    info.setLecturer("PENDING");
                                }

                                //Class also special case , but can be solve be revoke initialization
                                if(((Timetable)population.tKelasList.get(kelasIndex)).checkTimeslot(dayLab,timeLab,((Subject) population.subjectList.get(s)).getLabHour()))
                                {
                                    info.setKelas(((Timetable) population.tKelasList.get(kelasIndex)).getName());
                                    ((Timetable) population.tKelasList.get(kelasIndex)).setTimeslot(info, dayLab, timeLab, ((Subject) population.subjectList.get(s)).getLabHour());

                                }
                                else
                                {
                                    info.setKelas("NOCLASS");
                                    System.out.println("TAK MASUK ----------------------------> " + ((Timetable) population.tKelasList.get(kelasIndex)).getName().trim());
                                    notInKelas += ((Subject) population.subjectList.get(s)).getLectHour();

                                }

                                ((Timetable) population.tGroupList.get(t)).setTimeslot(info, dayLab, timeLab, ((Subject) population.subjectList.get(s)).getLabHour());
                                //------------------END ASSIGN LAB---------------------------------------------------------------------
                            } else {
                                System.out.print("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
                                System.out.println(((Timetable) population.tGroupList.get(t)).getName().trim());
                                notIn += ((Subject) population.subjectList.get(s)).getLabHour();
                            }
//
//                        if(((Timetable)ReadData.tGroupList.get(t)).checkTimeslot(dayTuto,timeTuto,((Subject) ReadData.subjectList.get(s)).getTutoHour()))
//                        {
//
//                        }
//                            if((((Timetable)ReadData.tGroupList.get(t)).checkTimeslot(dayLect,timeLect,((Subject) ReadData.subjectList.get(s)).getLectHour())))
//                            {
//
//                            }
//                            if(((Timetable)ReadData.tGroupList.get(t)).checkTimeslot(dayLab,timeLab,((Subject) ReadData.subjectList.get(s)).getLabHour()))
//                            {
//
//                            }
//
//                        else{
//                            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"+
//                                                 "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
//                                                "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
//                            System.out.println(((Timetable) ReadData.tGroupList.get(t)).getName());
//                            String timetable = String.format("%-15s","DAY / TIME");
//                            for(int x = 0; x <10; x++) {
//
//                                timetable += String.format("%-15s",timeTranslate(x));
//
//                            }
//                            for(int day =0; day <5; day++)
//                            {
//                                timetable += "\n" + String.format("%-15s",dayTranslate(day));
//                                for(int time = 0; time <10; time++){
//
//                                    if(((Timetable) ReadData.tGroupList.get(t)).getTimeslot(day,time) != null){
//                                        timetable +=  String.format("%-15s", ((Information) ((Timetable) ReadData.tGroupList.get(t)).getTimeslot(day, time)).getSubjectCode() );
//                                    }
//                                    else
//                                    {
//                                        timetable += String.format("%-15s","EMPTY");
//                                    }
//                                }
//                            }
//                            //*******************************************
//                            System.out.println(timetable);
//                            Thread.sleep(1000);
//                            //==========================================================
//                            ((Timetable) ReadData.tGroupList.get(t)).clearAllTimeslot();
//                            s=0;
//                            break;
//                        }

                        }//and of group matched
                    }
                }
            }
            //System.out.println(" Groupmatched subject == timetable : " + groupMatched);
            printTimetableGroup();
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
    private static String dayTranslate(int day)
    {
        if(day == 0)
        {
            return "Monday";
        }
        else if(day == 1){return "Tuesday";}
        else if(day == 2){return "Wednesday";}
        else if(day == 3){return "Thursday";}
        else if(day == 4){return "Friday";}
        else{return "Wrong day";}
    }
    private static String timeTranslate(int time)
    {
        if(time == 0){return "8.00am";}
        else if (time == 1){return "9.00am";}
        else if (time == 2){return "10.00am";}
        else if (time == 3){return "11.00am";}
        else if (time == 4){return "12.00pm";}
        else if (time == 5){return "1.00pm";}
        else if (time == 6){return "2.00pm";}
        else if (time == 7){return "3.00pm";}
        else if (time == 8){return "4.00pm";}
        else if (time == 9){return "5.00pm";}
        else if (time == 10){return "6.00pm";}
        else if (time == 11){return "7.00pm";}
        else if (time == 12){return "8.00pm";}
        else if (time == 13){return "9.00pm";}
        else {return "Wrong Time";}
    }


    public static void printTimetableGroup()
    {
        System.out.println("Group size : " + population.tGroupList.size() + " Kelas size : " + population.tKelasList.size() + " Lecturer size : " + population.tLecturerList.size() );
        System.out.println("\n----------------------------PRINT TIMETABLE GROUP----------------------------------\n");
        int count =0;

        //slotted
        for(int i = 0; i < population.tGroupList.size(); i++)
        {
//           Timetable group = (Timetable) ReadData.tGroupList.get(i);
//           Information info;
//           for(int day = 0; day<5; day++)
//           {
//               for(int time = 0; time<10; time++)
//               {
//                   if(group.getTimeslot(day,time) != null) {
//                       info = (Information) group.getTimeslot(day, time);
//                       System.out.println(info.toString() + " " + dayTranslate(day)+ " "+ timeTranslate(time));
//                       count++;
//                   }
//               }
//           }

            //*******************************************

            System.out.println("\n\n");
            Timetable group = (Timetable) population.tGroupList.get(i);
            System.out.println(group.getName());
            String timetable = String.format("%-15s","DAY / TIME");
            for(int x = 0; x <10; x++) {

                timetable += String.format("%-15s",timeTranslate(x));

            }
            for(int day =0; day <5; day++)
            {
                timetable += "\n" + String.format("%-15s",dayTranslate(day));
                //subjucet
                for(int time = 0; time <10; time++){

                    if(group.getTimeslot(day,time) != null){
                        count++;
                        Information info = group.getTimeslot(day, time);
                        timetable +=  String.format("%-15s", info.getSubjectCode() );
                    }
                    else
                    {
                        timetable += String.format("%-15s","EMPTY");
                    }
                }
                timetable += "\n" + String.format("%-15s",dayTranslate(day));
                //kelas
                for(int time = 0; time <10; time++){

                    if(group.getTimeslot(day,time) != null)
                    {
                        //count++;
                        Information info = group.getTimeslot(day, time);
                        timetable +=  String.format("%-15s", info.getKelas() );
                    }
                    else
                    {
                        timetable += String.format("%-15s","KELAS");
                    }
                }
            }
            //*******************************************
            System.out.println(timetable);
            System.out.println("Fitness : " + ((Timetable) population.tGroupList.get(i)).getFitnessTimetable());
        }
        //unslotted
//        for(int i=0; i < reader.tGroupList.size(); i++)
//        {
//            Timetable group = (Timetable) reader.tGroupList.get(i);
//            Information info;
//            for(int day = 0; day<5; day++)
//            {
//                for(int time = 0; time<10; time++)
//                {
//                    if(group.getTimeslot(day,time) == null) {
//                        //info = (Information) group.getTimeslot(day, time);
//                        //System.out.println(info.toString() + " " + dayTranslate(day)+ " "+ timeTranslate(time));
//                        System.out.println("Unslotted bitch - " + group.getName());
//                        //count++;
//                    }
//                }
//            }
//        }

        System.out.print("total subject+group slotted : " + count +" | total distinct group  : "+ population.tGroupList.size() );
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
    public static void calculateFitness()
    {
        for(int t=0; t<population.tGroupList.size(); t++)
        {
            int count = 0;
            System.out.println(((Timetable) population.tGroupList.get(t)).getName() );
            for(int day =0; day <5; day++)
            {
                for(int time =0; time < 10; time++)
                {
                    //pukul 12 - 2 denda
                    if(time == 4 || time ==5)
                    {
                        if(!(((Timetable)population.tGroupList.get(t)).checkTimeslot(day,time)))
                        {
                            count++;
                            Information temp = (Information) ((Timetable) population.tGroupList.get(t)).getTimeslot(day,time);
                            temp.addFitness();
                            //System.out.println(((Timetable) ReadData.tGroupList.get(t)).getTimeslot(day, time).getFitness() + " |  " + day + " - " + time +" In" + ((Timetable) ReadData.tGroupList.get(t)).getTimeslot(day, time).getSubjectCode());

                        }
                    }
//                    else if( time == 5)
//                    {
//                        if(!(((Timetable)ReadData.tGroupList.get(t)).checkTimeslot(day,time)) )
//                        {
//                            count++;
//                           ((Timetable) ReadData.tGroupList.get(t)).getTimeslot(day,time).setFitness(1);
//                            System.out.println(((Timetable) ReadData.tGroupList.get(t)).getTimeslot(day, time).getFitness() + " |  " + day + " - " + time +" In 5   "+ ((Timetable) ReadData.tGroupList.get(t)).getTimeslot(day, time).getSubjectCode());
//                        }
//                    }
//                    else if(!(((Timetable)ReadData.tGroupList.get(t)).checkTimeslot(day,time)))
//                    {
//
//                        System.out.println(((Timetable) ReadData.tGroupList.get(t)).getTimeslot(day, time).getFitness() + " |  " + day + " - " + time +" NotIn  " + ((Timetable) ReadData.tGroupList.get(t)).getTimeslot(day, time).getSubjectCode());
//                    }
////
                    //calculate gapss
                    if(!(((Timetable)population.tGroupList.get(t)).checkTimeslot(day,time)))
                    {
                        //hok belakey
                        for(int i = time-1; i >=0; i--)
                        {
                            if(!(((Timetable)population.tGroupList.get(t)).checkTimeslot(day,i)))
                            {
                                int fit = (((Timetable)population.tGroupList.get(t)).getTimeslot(day,time)).getFitness();
                                int gap = Math.abs(time-i-1);// -1 sebab kalau 8-7 = 1 , supposely dorang bersebalahan takde gap
                                //System.out.println( "GAP : " + gap);
                                //System.out.println( "\t In GAP : " + fit);
                                (((Timetable)population.tGroupList.get(t)).getTimeslot(day,time)).setFitness( fit + gap) ;
                                fit = (((Timetable)population.tGroupList.get(t)).getTimeslot(day,time)).getFitness();
                                //System.out.println( "\t out GAP : " + fit);
                               break;
                            }
                        }
                        //hok depey
                        for(int i = time+1; i <10; i++)
                        {
                            if(!(((Timetable)population.tGroupList.get(t)).checkTimeslot(day,i)))
                            {
                                int fit = (((Timetable)population.tGroupList.get(t)).getTimeslot(day,time)).getFitness();
                                int gap = Math.abs(i-1-time);
                                //System.out.println( "GAP : " + gap);
                                //System.out.println( "\t In GAP : " + fit);
                                (((Timetable)population.tGroupList.get(t)).getTimeslot(day,time)).setFitness( fit + gap) ;
                                fit = (((Timetable)population.tGroupList.get(t)).getTimeslot(day,time)).getFitness();
                                //System.out.println( "\t out GAP : " + fit);
                                break;
                            }
                        }
                    } // end calculate gaps
                }
            }
            ((Timetable)population.tGroupList.get(t)).countFitness();
            //System.out.println(((Timetable)ReadData.tGroupList.get(t)).getFitness()+" - " + count);
        }

    }
}
