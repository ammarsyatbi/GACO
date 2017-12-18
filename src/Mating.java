import javax.sound.sampled.Line;
import java.util.Random;

public class Mating {


    private Population[] pop;

    private int parentA,parentB;
    private int fitnessA,fitnessB;

    private final static int segment1 = 4;
    private final static int segment2 = 5;

    public Mating (Population[] pop) throws InterruptedException {
        this.pop = pop;
        runCrossover();
    }

    public void runCrossover() throws InterruptedException {
        findParents();
    }

    public void findParents() throws InterruptedException
    {
        Timetable A;
        Timetable B;

        for(int i = 0; i < pop[0].gettGroupList().size(); i++)
        {
            //CARI PARENTS
            //FIND HIGHEST JE
            parentA =0;//first big
            fitnessA = ((Timetable)pop[0].gettGroupList().get(i)).getFitnessTimetable();

            parentB =1;//sec big
            fitnessB = ((Timetable)pop[1].gettGroupList().get(i)).getFitnessTimetable();

            //COMPARE FIRST SECOND
            if(((Timetable)pop[1].gettGroupList().get(i)).getFitnessTimetable() > ((Timetable)pop[0].gettGroupList().get(i)).getFitnessTimetable())
            {
                int temp = fitnessB;
                fitnessB = fitnessA;
                fitnessA = temp;

                temp = parentB;
                parentB = parentA;
                parentA = temp;
            }

            for(int p =2; p < GeneticAlgorithm.POPULATION_SIZE; p++)
            {
                if(fitnessA < ((Timetable)pop[p].gettGroupList().get(i)).getFitnessTimetable() )
                {
                    parentB = parentA;
                    fitnessB = fitnessA;

                    parentA = p;
                    fitnessA = ((Timetable)pop[p].gettGroupList().get(i)).getFitnessTimetable();
                }
            }

            //SO NANT PASS TIMETABLE JE #THECHOSENONE
            A = ((Timetable)pop[parentA].gettGroupList().get(i));
            B = ((Timetable)pop[parentB].gettGroupList().get(i));

            PMX(A,B);
        }
    }

    public Population[] getPop() {
        return pop;
    }

    public void PMX(Timetable parentA, Timetable parentB) throws InterruptedException {
        for(int day =0; day <5; day++)
        {
            // timeslotA == null
            if(parentA.getTimeslot(day,segment1) == null)
            {
                if(parentB.getTimeslot(day,segment1) != null)
                {
                    if(checkSegment(parentA,parentB.getTimeslot(day,segment1).getSubjectCode()))
                    {

                        System.out.println("MATING : REPLACING " + parentB.getTimeslot(day,segment1).getSubjectCode());
                        Thread.sleep(1000);
                        int dayB = parentB.getTimeslot(day,segment1).getDay();
                        int timeB = parentB.getTimeslot(day,segment1).getTime();
                        int block = parentB.getTimeslot(day,segment1).getSubjectHour();
                        //dia bukan satu set, so ko kene amik dari awal; maybe buat satu function dalam timetable, deteck subject code, clear based ON KEY INDEX,;
                        parentB.clearTimeslot(dayB,timeB,block);
                    }
                    else
                    {
                        //TODO: relationship mapping : B takde dalam segment A
                        String subject = parentB.getTimeslot(day,segment1).getSubjectCode();
                        relationshipMapping(parentA,parentB,subject,day);
                    }
                }
            }
            else //(parentA.getTimeslot(day,segment1) != null)
            {

            }
        }
    }

    private void relationshipMapping(Timetable parentA, Timetable parentB,String subject, int day) throws InterruptedException {
        int index;
        String subjectA,subjectB;
        index = day;//day yang nak kene replace, yang mula2 match
        subjectB = subject;// simpan untuk replace masa last

        do {
            System.out.println("Relationship mapping");

            if(parentA.getTimeslot(index, segment1)!=null) {

                subjectA = parentA.getTimeslot(index, segment1).getSubjectCode();
                if (checkSegment(parentB, subjectA))// A dalam segment  B , dia kene mapped lagi
                {
                    System.out.println("In segment");
                    //get new indexB
                    index = getIndex(parentB, subjectA);
                }
                else {
                    System.out.println("Not in segment");
                    int time = getIndexTime(parentB, subjectA);
                    int block = parentB.getTimeslot(index,time).getSubjectHour();
                    int dayIndex = parentB.getTimeslot(index,time).getDay();
                    int timeIndex = parentB.getTimeslot(index,time).getTime();
                    //Clear dulu sebelum replace
                    parentB.clearTimeslot(dayIndex,timeIndex,block);
                    //TODO:checkOverlap
                    //TODO:REPLACEMENT (setTimeslot)
                    parentB.setTimeslot(parentA.getTimeslot(index, segment1),dayIndex,timeIndex,block);

                    break;
                }
            }
            else
            {
                System.out.println("ParentA slot == Null : Initiate Mutation...");
                System.out.println("Clear choosen subject timeslot B dulu, baru ade space");
                int block = parentB.getTimeslot(index,segment1).getSubjectHour();
                int dayIndex = parentB.getTimeslot(index,segment1).getDay();
                int timeIndex = parentB.getTimeslot(index,segment1).getTime();

                Information info = parentB.getTimeslot(index,segment1);

                System.out.println("Clear timeslot : " + parentB.getName() + " - " + parentB.getTimeslot(dayIndex,timeIndex).getSubjectCode());

                Thread.sleep(3000);
                parentB.clearTimeslot(dayIndex,timeIndex,block);

                //TODO: MUTATION
               Mutation(parentB,info);
                break;
            }

        }while (checkSegment(parentB,subjectA));

        //find index
        for(int i=0; i <5; i++)
        {
            for(int j=0; j<10; j++)
            {

            }
        }
    }

    private int getIndex(Timetable parentB, String subject)
    {
        for(int i =0; i < 5; i++)
        {
            if(parentB.getTimeslot(i,segment1).getSubjectCode().equalsIgnoreCase(subject))
            {
                return i;
            }

        }

        return -1;
    }

    private int getIndexTime(Timetable timetable, String subject)
    {
        for(int i =0; i < 5; i++)
        {
            for(int j =0; j<10; j++)
            {
                if(timetable.getTimeslot(i,j).getSubjectCode().equalsIgnoreCase(subject))
                {
                    return j;
                }

            }


        }

        return -1;

    }

    private void Mutation(Timetable timetable, Information info)
    {
        Random rg = new Random();
        int block = info.getSubjectHour();
        int day = rg.nextInt(5);
        int time = rg.nextInt(10 - block);


        while(timetable.checkTimeslot(day,time,block))
        {
            System.out.println("\n\n"+timetable.getName()+"\n"+printTimetable(timetable) + "\n");
            System.out.println("Find random day/time that fit for mutation " + day + " " + time + " " + info.getSubjectCode() + " hour : " +info.getSubjectHour() );

            printTimetable(timetable);
            day = new Random().nextInt(5);
            time = new Random().nextInt(10 - block);
        }

        if(timetable.checkTimeslot(day,time,block))
        {
            timetable.setTimeslot(info,day,time,block);
        }

    }

    public String printTimetable(Timetable t)
    {
        String timetable = "";
        for(int day =0; day <5; day++)
        {
            timetable += "\n" + String.format("%-15s", GeneticAlgorithm.dayTranslate(day));
            //subjucet
            for(int time = 0; time <10; time++){

                if(t.getTimeslot(day,time) != null){

                    Information info = t.getTimeslot(day, time);
                    timetable +=  String.format("%-15s", info.getSubjectCode() );
                }
                else
                {
                    timetable += String.format("%-15s","EMPTY");
                }
            }
            timetable += "\n" + String.format("%-15s",GeneticAlgorithm.dayTranslate(day));
            //kelas
            for(int time = 0; time <10; time++){

                if(t.getTimeslot(day,time) != null)
                {
                    //count++;
                    Information info = t.getTimeslot(day, time);
                    timetable +=  String.format("%-15s", info.getKelas() );
                }
                else
                {
                    timetable += String.format("%-15s","KELAS");
                }
            }
            timetable += "\n";
        }

        return timetable;
    }


    private boolean checkSegment(Timetable timetable,String subcode)
    {
        //timetable A <-- the segment you want to check, subcode in segment A tak ?
        for(int day=0; day <5; day++)
        {
            if(timetable.getTimeslot(day, segment1) != null) {
                if (timetable.getTimeslot(day, segment1).checkSubCode(subcode)) {
                    return true;
                }
            }
        }

        return false;
    }

    //FALSE = OVERLAP;
    private boolean checkOverlap(int day, int time,Timetable timetable)
    {
        //TODO: checkTimeslot (True that it is empty)
        return false;
    }


}
