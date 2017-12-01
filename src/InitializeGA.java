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
    public InitializeGA() throws IOException, InterruptedException
    {

        allTimeTable = new ArrayList<>();

        //assignTimetable();
        assignTT();
        initializeFitness();
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

        for(int i = 0; i < ReadData.subjectList.size(); i++)//pergi setiap subject
        {

            ArrayList temp = ((Subject) ReadData.subjectList.get(i)).getGroupList();
            for(int j=0; j< temp.size(); j++)//pergi setiap group dalam subject
            {
                Group groupTemp = (Group)  temp.get(j);//group dalam subject


               // assignTutorial();
                for(int x = 0; x < ReadData.tGroupList.size(); x++)//pergi setiap group dalam timetable.
                {
                    Timetable groupTimetable = (Timetable) ReadData.tGroupList.get(x);//group dari main
                    if(groupTemp.getCode().trim().equalsIgnoreCase(groupTimetable.getName().trim()))//check sama sebab nak amik based on subject.
                    {

                        int tutohour = ((Subject) ReadData.subjectList.get(i)).getTutoHour();
                        int lectureHour = ((Subject) ReadData.subjectList.get(i)).getLectHour();
                        int labHour = ((Subject) ReadData.subjectList.get(i)).getLabHour();
                        int dayTuto = new Random().nextInt(5 );
                        int timeTuto = new Random().nextInt(10 - tutohour);
                        int lectIndex = checkLecturer(((Subject) ReadData.subjectList.get(i)).getCode(),dayTuto,timeTuto,tutohour);

                        if((groupTimetable.checkTimeslot(dayTuto,timeTuto,tutohour) && (lectIndex != -1)) )//check group/lecturer nye slot
                        {
                            //------------------ASSIGN TUTORIAL---------------------------------------------------------------------
                            groupMatched += tutohour;//TODO: DIA 250 JE SEBAB ADE YANG TAKDE TUTORIAL STEWPIG hour = 0
                            Information info = new Information();
                            info.setGroup(groupTimetable.getName().trim());
                            info.setSubjectCode(((Subject) ReadData.subjectList.get(i)).getCode().trim());
                            info.setStudnum(groupTemp.getStudNum());
                            info.setLecturer(((Timetable) ReadData.tLecturerList.get(lectIndex)).getName());
                            ((Timetable) ReadData.tGroupList.get(x)).setTimeslot(info, dayTuto, timeTuto, tutohour);
                            //------------------END ASSIGN TUTORIAL---------------------------------------------------------------------
                            int dayLect = new Random().nextInt(5 );
                            int timeLect = new Random().nextInt(10 - lectureHour);
                            lectIndex = checkLecturer(((Subject) ReadData.subjectList.get(i)).getCode(),dayLect,timeLect,lectureHour);


                            if((groupTimetable.checkTimeslot(dayLect,timeLect,lectureHour) && (lectIndex != -1)))//check group/lecturer nye slot
                            {
                                //-------------------------ASSIGN LECTURER-------------------------------------------------------------
                                info = new Information();
                                info.setGroup(groupTimetable.getName().trim());
                                info.setSubjectCode(((Subject) ReadData.subjectList.get(i)).getCode().trim());
                                info.setStudnum(groupTemp.getStudNum());
                                info.setLecturer(((Timetable) ReadData.tLecturerList.get(lectIndex)).getName());
                                ((Timetable) ReadData.tGroupList.get(x)).setTimeslot(info, dayLect, timeLect, lectureHour);

                                //------------------------- END ASSIGN LECTURER-------------------------------------------------------------
                                int dayLab = new Random().nextInt(5 );
                                int timeLab = new Random().nextInt(10 - labHour);
                                lectIndex = checkLecturer(((Subject) ReadData.subjectList.get(i)).getCode(),dayLab,timeLab,labHour);
                                if((groupTimetable.checkTimeslot(dayLab,timeLab,labHour) && (lectIndex != -1)))
                                {
                                    //-------------------------ASSIGN LAB-------------------------------------------------------------

                                    info = new Information();
                                    info.setGroup(groupTimetable.getName().trim());
                                    info.setSubjectCode(((Subject) ReadData.subjectList.get(i)).getCode().trim());
                                    info.setStudnum(groupTemp.getStudNum());
                                    info.setLecturer(((Timetable) ReadData.tLecturerList.get(lectIndex)).getName());
                                    ((Timetable) ReadData.tGroupList.get(x)).setTimeslot(info, dayLab, timeLab, labHour);
                                    //------------------------- END ASSIGN LAB-------------------------------------------------------------
                                }
                                else{
                                    ((Timetable) ReadData.tGroupList.get(x)).clearTimeslot(dayLect, timeLect, tutohour);
                                    ((Timetable) ReadData.tGroupList.get(x)).clearTimeslot(dayTuto, timeTuto, tutohour);
                                    x--;
                                }
                            }
                            else{
                                ((Timetable) ReadData.tGroupList.get(x)).clearTimeslot(dayTuto, timeTuto, tutohour);
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

        do {
            notIn =0;

            new ReadData();
            for (int t = 0; t < ReadData.tGroupList.size(); t++) {
                for (int s = 0; s < ReadData.subjectList.size(); s++) {
                    for (int g = 0; g < ((Subject) ReadData.subjectList.get(s)).getGroupList().size(); g++) {
                        if (((Group) ((Subject) ReadData.subjectList.get(s)).getGroupList().get(g)).getCode().equalsIgnoreCase(((Timetable) ReadData.tGroupList.get(t)).getName().trim())) {

                            Random rg = new Random();
                            int dayTuto = rg.nextInt(5);
                            int timeTuto = rg.nextInt(11 - ((Subject) ReadData.subjectList.get(s)).getTutoHour());

                            int dayLect = rg.nextInt(5);
                            int timeLect = rg.nextInt(11 - ((Subject) ReadData.subjectList.get(s)).getLectHour());

                            int dayLab = rg.nextInt(5);
                            int timeLab = rg.nextInt(11 - ((Subject) ReadData.subjectList.get(s)).getLabHour());
                            Information info;

                            int kelasIndex;

                            int lectIndex = checkLecturer(((Subject) ReadData.subjectList.get(s)).getCode(), dayTuto, timeTuto, ((Subject) ReadData.subjectList.get(s)).getTutoHour());

                            //try 100 kali je
                            //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
                            for (int k = 0; (k < 100000 && !(((Timetable) ReadData.tGroupList.get(t)).checkTimeslot(dayTuto, timeTuto, ((Subject) ReadData.subjectList.get(s)).getTutoHour()))); k++) {
                                if (!(((Timetable) ReadData.tGroupList.get(t)).checkTimeslot(dayTuto, timeTuto, ((Subject) ReadData.subjectList.get(s)).getTutoHour()))) {
                                    dayTuto = new Random().nextInt(5);
                                    timeTuto = new Random().nextInt(11 - ((Subject) ReadData.subjectList.get(s)).getTutoHour());
                                }
                            }

                            kelasIndex = rg.nextInt(ReadData.tKelasList.size());
                            //TODO : Check null dalam kelas
                            while(!((Timetable)ReadData.tKelasList.get(kelasIndex)).checkTimeslot(dayTuto,timeTuto))
                            {
                                kelasIndex = rg.nextInt(ReadData.tKelasList.size());
                            }
                            if ((((Timetable) ReadData.tGroupList.get(t)).checkTimeslot(dayTuto, timeTuto, ((Subject) ReadData.subjectList.get(s)).getTutoHour()))) {
                                //------------------ASSIGN TUTORIAL---------------------------------------------------------------------
                                info = new Information();
                                info.setGroup(((Timetable) ReadData.tGroupList.get(t)).getName().trim());
                                info.setSubjectCode(((Subject) ReadData.subjectList.get(s)).getCode().trim());
                                info.setStudnum(((Group) ((Subject) ReadData.subjectList.get(s)).getGroupList().get(g)).getStudNum());

                                if (lectIndex != -1) {
                                    info.setLecturer(((Timetable) ReadData.tLecturerList.get(lectIndex)).getName());
                                    //TODO : KAT SINI WEH LANCO
                                    ((Timetable) ReadData.tLecturerList.get(lectIndex)).setTimeslot(info, dayTuto, timeTuto, ((Subject) ReadData.subjectList.get(s)).getTutoHour());
                                } else {
                                    info.setLecturer("PENDING");
                                }
                                ((Timetable) ReadData.tGroupList.get(t)).setTimeslot(info, dayTuto, timeTuto, ((Subject) ReadData.subjectList.get(s)).getTutoHour());
                                //------------------END ASSIGN TUTORIAL---------------------------------------------------------------------
                            } else {
                                System.out.print("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
                                System.out.println(((Timetable) ReadData.tGroupList.get(t)).getName().trim());
                                notIn += ((Subject) ReadData.subjectList.get(s)).getTutoHour();
                            }
                            lectIndex = checkLecturer(((Subject) ReadData.subjectList.get(s)).getCode(), dayLect, timeLect, ((Subject) ReadData.subjectList.get(s)).getLectHour());
                            //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
                            for (int k = 0; (k < 100000 && !(((Timetable) ReadData.tGroupList.get(t)).checkTimeslot(dayLect, timeLect, ((Subject) ReadData.subjectList.get(s)).getLectHour()))); k++) {
                                if (!(((Timetable) ReadData.tGroupList.get(t)).checkTimeslot(dayLect, timeLect, ((Subject) ReadData.subjectList.get(s)).getLectHour()))) {
                                    dayLect = new Random().nextInt(5);
                                    timeLect = new Random().nextInt(11 - ((Subject) ReadData.subjectList.get(s)).getLectHour());
                                }

                            }
                            if ((((Timetable) ReadData.tGroupList.get(t)).checkTimeslot(dayLect, timeLect, ((Subject) ReadData.subjectList.get(s)).getLectHour()))) {
                                //------------------ASSIGN LECTURE---------------------------------------------------------------------
                                info = new Information();
                                info = new Information();
                                info.setGroup(((Timetable) ReadData.tGroupList.get(t)).getName().trim());
                                info.setSubjectCode(((Subject) ReadData.subjectList.get(s)).getCode().trim());
                                info.setStudnum(((Group) ((Subject) ReadData.subjectList.get(s)).getGroupList().get(g)).getStudNum());
                                if ((lectIndex != -1)) {
                                    info.setLecturer(((Timetable) ReadData.tLecturerList.get(lectIndex)).getName());
                                    ((Timetable) ReadData.tLecturerList.get(lectIndex)).setTimeslot(info, dayLect, timeLect, ((Subject) ReadData.subjectList.get(s)).getLectHour());
                                } else {
                                    info.setLecturer("PENDING");
                                }
                                ((Timetable) ReadData.tGroupList.get(t)).setTimeslot(info, dayLect, timeLect, ((Subject) ReadData.subjectList.get(s)).getLectHour());
                                //------------------END ASSIGN LECTURE---------------------------------------------------------------------
                            } else {
                                System.out.print("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
                                System.out.println(((Timetable) ReadData.tGroupList.get(t)).getName().trim());
                                notIn += ((Subject) ReadData.subjectList.get(s)).getLectHour();
                            }

                            lectIndex = checkLecturer(((Subject) ReadData.subjectList.get(s)).getCode(), dayLect, timeLect, ((Subject) ReadData.subjectList.get(s)).getLectHour());
                            //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
                            for (int k = 0; (k < 100000 && !(((Timetable) ReadData.tGroupList.get(t)).checkTimeslot(dayLab, timeLab, ((Subject) ReadData.subjectList.get(s)).getLabHour()))); k++) {
                                if (!(((Timetable) ReadData.tGroupList.get(t)).checkTimeslot(dayLab, timeLab, ((Subject) ReadData.subjectList.get(s)).getLabHour()))) {
                                    dayLab = new Random().nextInt(5);
                                    timeLab = new Random().nextInt(11 - ((Subject) ReadData.subjectList.get(s)).getLabHour());
                                }
                            }
                            if ((((Timetable) ReadData.tGroupList.get(t)).checkTimeslot(dayLab, timeLab, ((Subject) ReadData.subjectList.get(s)).getLabHour()))) {
                                //------------------ASSIGN LAB---------------------------------------------------------------------
                                info = new Information();
                                info = new Information();
                                info.setGroup(((Timetable) ReadData.tGroupList.get(t)).getName().trim());
                                info.setSubjectCode(((Subject) ReadData.subjectList.get(s)).getCode().trim());
                                info.setStudnum(((Group) ((Subject) ReadData.subjectList.get(s)).getGroupList().get(g)).getStudNum());

                                if ((lectIndex != -1)) {
                                    info.setLecturer(((Timetable) ReadData.tLecturerList.get(lectIndex)).getName());
                                    ((Timetable) ReadData.tLecturerList.get(lectIndex)).setTimeslot(info, dayLab, timeLab, ((Subject) ReadData.subjectList.get(s)).getLabHour());
                                } else {
                                    info.setLecturer("PENDING");
                                }
                                ((Timetable) ReadData.tGroupList.get(t)).setTimeslot(info, dayLab, timeLab, ((Subject) ReadData.subjectList.get(s)).getLabHour());
                                //------------------END ASSIGN LAB---------------------------------------------------------------------
                            } else {
                                System.out.print("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
                                System.out.println(((Timetable) ReadData.tGroupList.get(t)).getName().trim());
                                notIn += ((Subject) ReadData.subjectList.get(s)).getLabHour();
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
            System.out.println(" Group subject not in : " + notIn);
        }while(notIn > 0);
    }//end assignTT

    public static int checkLecturer(String subcode, int day, int time,int block)
    {


        for(int i = 0; i< ReadData.lecturerList.size(); i++)
        {
            Lecturer lecturer = (Lecturer) ReadData.lecturerList.get(i);
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
        for(int i = 0; i< ReadData.tLecturerList.size(); i++)
        {
            Timetable temp = (Timetable) ReadData.tLecturerList.get(i);
            if(name.trim().equalsIgnoreCase(temp.getName().trim()))
            {

                if( temp.checkTimeslot(day,time,block))
                {
                    return true;
                }
                else
                {
                    System.out.println(temp.getName() + " " + day + " " + time);
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
        System.out.println("\n----------------------------PRINT TIMETABLE GROUP----------------------------------\n");
        int count =0;

        //slotted
        for(int i = 0; i < ReadData.tGroupList.size(); i++)
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
            Timetable group = (Timetable) ReadData.tGroupList.get(i);
            System.out.println(group.getName());
            String timetable = String.format("%-15s","DAY / TIME");
            for(int x = 0; x <10; x++) {

                timetable += String.format("%-15s",timeTranslate(x));

            }
            for(int day =0; day <5; day++)
            {
                timetable += "\n" + String.format("%-15s",dayTranslate(day));
                for(int time = 0; time <10; time++){

                    if(group.getTimeslot(day,time) != null){
                        count++;
                        Information info = (Information) group.getTimeslot(day, time);
                        timetable +=  String.format("%-15s", info.getSubjectCode() );
                    }
                    else
                    {
                        timetable += String.format("%-15s","EMPTY");
                    }
                }
            }
            //*******************************************
            System.out.println(timetable);
            System.out.println("Fitness : " + ((Timetable) ReadData.tGroupList.get(i)).getFitnessTimetable());
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

        System.out.print("total subject+group slotted : " + count + " | total distinct group  : "+ ReadData.tGroupList.size() );
    }

    //TODO: buat print for groupdulu
    public static void printGroupList()
    {
        System.out.println("\n----------------------PRINT GROUP LIST-----------------------\n");
        for(int i = 0; i < ReadData.tGroupList.size(); i++)
        {
            Timetable group = (Timetable) ReadData.tGroupList.get(i);

            System.out.println(group.getName());

        }

    }
    //OBJECTIVE FUNCTION IS TO MINIMIZE | FITNESSS POINT == PENALTY
    public static void initializeFitness()
    {

        for(int t=0; t<ReadData.tGroupList.size(); t++)
        {
            System.out.println(((Timetable) ReadData.tGroupList.get(t)).getName() );
            for(int day =0; day <5; day++)
            {
                for(int time =0; time < 10; time++)
                {

                    if(!(((Timetable)ReadData.tGroupList.get(t)).checkTimeslot(day,time)))
                    {
                        ((Timetable) ReadData.tGroupList.get(t)).getTimeslot(day,time).setFitness(0);            }

                }
            }
        }
    }
    public static void calculateFitness()
    {
        for(int t=0; t<ReadData.tGroupList.size(); t++)
        {
            int count = 0;
            System.out.println(((Timetable) ReadData.tGroupList.get(t)).getName() );
            for(int day =0; day <5; day++)
            {
                for(int time =0; time < 10; time++)
                {
                    //pukul 12 - 2 denda
                    if(time == 4 || time ==5)
                    {
                        if(!(((Timetable)ReadData.tGroupList.get(t)).checkTimeslot(day,time)))
                        {
                            count++;
                            Information temp = (Information) ((Timetable) ReadData.tGroupList.get(t)).getTimeslot(day,time);
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
                    if(!(((Timetable)ReadData.tGroupList.get(t)).checkTimeslot(day,time)))
                    {
                        //hok belakey
                        for(int i = time-1; i >=0; i--)
                        {
                            if(!(((Timetable)ReadData.tGroupList.get(t)).checkTimeslot(day,i)))
                            {
                                int fit = (((Timetable)ReadData.tGroupList.get(t)).getTimeslot(day,time)).getFitness();
                                int gap = Math.abs(time-i-1);// -1 sebab kalau 8-7 = 1 , supposely dorang bersebalahan takde gap
                                System.out.println( "GAP : " + gap);
                                System.out.println( "\t In GAP : " + fit);
                                (((Timetable)ReadData.tGroupList.get(t)).getTimeslot(day,time)).setFitness( fit + gap) ;
                                fit = (((Timetable)ReadData.tGroupList.get(t)).getTimeslot(day,time)).getFitness();
                                System.out.println( "\t out GAP : " + fit);
                               break;
                            }
                        }
                        //hok depey
                        for(int i = time+1; i <10; i++)
                        {
                            if(!(((Timetable)ReadData.tGroupList.get(t)).checkTimeslot(day,i)))
                            {
                                int fit = (((Timetable)ReadData.tGroupList.get(t)).getTimeslot(day,time)).getFitness();
                                int gap = Math.abs(i-1-time);
                                System.out.println( "GAP : " + gap);
                                System.out.println( "\t In GAP : " + fit);
                                (((Timetable)ReadData.tGroupList.get(t)).getTimeslot(day,time)).setFitness( fit + gap) ;
                                fit = (((Timetable)ReadData.tGroupList.get(t)).getTimeslot(day,time)).getFitness();
                                System.out.println( "\t out GAP : " + fit);
                                break;
                            }
                        }
                    } // end calculate gaps
                }
            }
            ((Timetable)ReadData.tGroupList.get(t)).countFitness();
            //System.out.println(((Timetable)ReadData.tGroupList.get(t)).getFitness()+" - " + count);
        }

    }
}
