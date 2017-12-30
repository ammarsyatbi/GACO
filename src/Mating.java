import javax.sound.sampled.Line;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;

public class Mating {
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[1;31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";


    private ArrayList<Population> pop = new ArrayList<Population>();


    private final static int segment1 = 4;
    private final static int segment2 = 5;

    public Mating (ArrayList pop) throws InterruptedException {
        this.pop = pop;
        runCrossover();
    }

    public void runCrossover() throws InterruptedException {
        findParents();
    }

    public void findParents() throws InterruptedException
    {
        int parentA,parentB;
        int fitnessA,fitnessB;

        Timetable A;
        Timetable B;

        Population population = (Population)pop.get(0);
        ArrayList array = population.gettGroupList();
        int size = array.size();

        for(int i = 0; i < size; i++)
        {
            //CARI PARENTS
            //FIND HIGHEST JE


            parentA =0;//first big
            Population populationA = (Population)pop.get(parentA);
            ArrayList arrayA = populationA.gettGroupList();
            fitnessA = ((Timetable) array.get(i)).getFitnessTimetable();



            parentB =1;//sec big
            Population populationB = (Population)pop.get(parentB);
            ArrayList arrayB = populationB.gettGroupList();
            fitnessB = ((Timetable)  array.get(i)).getFitnessTimetable();

            //COMPARE FIRST SECOND
            if(((Timetable)((ArrayList)((Population)pop.get(1)).gettGroupList()).get(i)).getFitnessTimetable() > ((Timetable)((Population)pop.get(0)).gettGroupList().get(i)).getFitnessTimetable())
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
                if(fitnessA < ((Timetable) ((ArrayList)((Population)pop.get(p)).gettGroupList()).get(i)).getFitnessTimetable() )
                {
                    parentB = parentA;
                    fitnessB = fitnessA;

                    parentA = p;
                    fitnessA = ((Timetable) (((Population)pop.get(p)).gettGroupList().get(i))).getFitnessTimetable();
                }
            }

            populationA = (Population)pop.get(parentA);
            arrayA = populationA.gettGroupList();

            populationB = (Population)pop.get(parentB);
            arrayB = populationB.gettGroupList();

            //SO NANT PASS TIMETABLE JE #THECHOSENONE
            A = ((Timetable) arrayA.get(i));
            B = ((Timetable) arrayB.get(i));
            ArrayList kelasA =  populationA.gettKelasList();
            ArrayList kelasB =  populationB.gettKelasList();

            B = PMX(A,B,kelasA,kelasB,parentB);
            arrayB.set(i,B);
            populationB.settGroupList(arrayB);
            pop.set(parentB,populationB);
            //printTimetable(B);
            //Thread.sleep(5000);
        }
    }



    public Population getHighestPop()
    {
        //SURVIVAL SELECTION
        //TODO: return highest pop;
        int highest = ((Population)pop.get(0)).getPopulationFitness();
        Population highestPop = ((Population)pop.get(0));

        for(int i =0; i < GeneticAlgorithm.POPULATION_SIZE; i++)
        {
            if(highest < ((Population)pop.get(i)).getPopulationFitness())
            {
                highest = ((Population)pop.get(i)).getPopulationFitness();
                highestPop = ((Population)pop.get(i));
            }

        }
        return highestPop;
    }
    public Population getLowestPop()
    {
        int lowest = ((Population)pop.get(0)).getPopulationFitness();
        Population lowestPop = ((Population)pop.get(0));

        for(int i =0; i < GeneticAlgorithm.POPULATION_SIZE; i++)
        {
            if(lowest > ((Population)pop.get(i)).getPopulationFitness())
            {
                lowest = ((Population)pop.get(i)).getPopulationFitness();
                lowestPop = ((Population)pop.get(i));
            }

        }
        return lowestPop;
    }

    public Timetable PMX(Timetable parentA, Timetable parentB,ArrayList kelasA,ArrayList kelasB,int populationKey) throws InterruptedException {

        Timetable child = new Timetable(parentB);

        for(int day =0; day <5; day++)
        {
            // timeslotA == null
            if(parentA.getTimeslot(day,segment1) == null)
            {
                if(parentB.getTimeslot(day,segment1) != null)
                {
                    //current subject in segment B
                    Information infoB = parentB.getTimeslot(day,segment1);
                    String subjectB = parentB.getTimeslot(day,segment1).getSubjectCode();
                    char subTypeB = parentB.getTimeslot(day,segment1).getSubjectType();

                    if(checkSegment(parentA,subjectB,subTypeB))
                    {

                        String checkError = "[A == NULL | B != NULL | inSegment] \n" + " BEFORE " +  child.getName() +"\n"+ printTimetable(child);

                        if(!child.checkTimeslot(day,segment1))
                        {
                            Information infoC = child.getTimeslot(day,segment1);
                            int dayC = infoC.getDay();
                            int timeC = infoC.getTime();
                            int block = infoC.getSubjectHour();

                            System.out.println("MATING : REPLACING " + infoC.getSubjectCode());
                            //Thread.sleep(1000);

                            //dia bukan satu set, so ko kene amik dari awal; maybe buat satu function dalam timetable, deteck subject code, clear based ON KEY INDEX,;
                            child.clearTimeslot(dayC,timeC,block);
                            clearKelas(infoC,kelasB,populationKey);

                            child = Mutation(child,kelasB, infoC,populationKey);

                            //letak je dekat lain (RESERVE) , nnt bila A nak masuk segment, dia akan clearkan jugak : TODO(check kalau stuck tak cukup slot)
                        }

                        checkError += "\nAFTER " + child.getName() +"\n"+ printTimetable(child);

                        System.out.println(checkError);
                        //Thread.sleep(1000);

                    }
                    else
                    {
                        if(!child.checkTimeslot(day,segment1))
                        {
                            System.out.println("MATING : REPLACING " + parentB.getTimeslot(day, segment1).getSubjectCode() + " to NULL");
                            //Thread.sleep(1000);
                            int dayC = child.getTimeslot(day, segment1).getDay();
                            int timeC = child.getTimeslot(day, segment1).getTime();
                            int block = child.getTimeslot(day, segment1).getSubjectHour();
                            Information infoC = child.getTimeslot(day, segment1);
                            //dia bukan satu set, so ko kene amik dari awal; maybe buat satu function dalam timetable, deteck subject code, clear based ON KEY INDEX,;

                            String checkError = "[A == NULL | B != NULL | outSegment] \n" + " BEFORE " + child.getName() + "\n" + printTimetable(child);

                            child.clearTimeslot(dayC, timeC, block);
                            clearKelas(infoC,kelasB,populationKey);

                            child = Mutation(child,kelasB, infoC,populationKey);

                            checkError += "\nAFTER " + child.getName() + "\n" + printTimetable(child);
                            System.out.println(checkError);
                            //Thread.sleep(1000);
                            //elationshipMapping(parentA,parentB,child,infoB,day);
                        }
                    }
                }
            }
            else //(parentA.getTimeslot(day,segment1) != null)
            {
                Information infoA = parentA.getTimeslot(day,segment1);
                Information infoB = parentB.getTimeslot(day,segment1);
                int blockA = infoA.getSubjectHour();

                if(parentB.getTimeslot(day,segment1) != null) {
                    // LE : IMPORTANT VARIABLES NEEDED IN FUNCTION
                    //current subject in segment B
                    String subjectB = parentB.getTimeslot(day, segment1).getSubjectCode();
                    char subTypeB = parentB.getTimeslot(day, segment1).getSubjectType();


                    //check segment
                    if(checkSegment(parentA, subjectB, subTypeB))
                    {
                        if (child.getTimeslot(day, segment1) != null)
                        {
                            if (child.equalInfo(infoB, day, segment1))
                            {
                                Information infoC = child.getTimeslot(day,segment1);

                                //mandatory variables needed to do replacement or clearence: it gets the whole slots bebeh
                                int dayC = child.getTimeslot(day, segment1).getDay();
                                int timeC = child.getTimeslot(day, segment1).getTime();
                                int block = child.getTimeslot(day, segment1).getSubjectHour();

                                clearKelas(infoC,kelasB,populationKey);
                                child.clearTimeslot(dayC, timeC, block);

                                //check overlapping : BASED ON A SUBJECT THAT WANTED TO BE REPLACE
                                if (checkOverlap(infoA, child,kelasB, day, segment1))
                                {
                                    String checkError = "[A != NULL | B != NULL | inSegment | noOverlap] \n" + " BEFORE " + child.getName() + "\n" + printTimetable(child);

                                    child.clearSubject(infoA.getSubjectCode(), infoA.getSubjectType(),infoA.getGroup());
                                    clearKelas(infoA,kelasB,populationKey);

                                    child.setTimeslot(infoA, day, segment1, blockA);
                                    setKelas(infoA,day,segment1,kelasB,populationKey);

                                    checkError += "\nAFTER " + child.getName() + "\n" + printTimetable(child);
                                    System.out.println(checkError);
                                    //Thread.sleep(1000);

                                }
                                else
                                {
                                    //Mutation
                                    String checkError = "[A != NULL | B != NULL | inSegment | Overlap] \n" + " BEFORE " + child.getName() + "\n" + printTimetable(child);

                                    child = Mutation(child,kelasB, infoA,populationKey);

                                    checkError += "\nAFTER " + child.getName() + "\n" + printTimetable(child);
                                    System.out.println(checkError);
                                    //Thread.sleep(1000);
                                }

                                child = Mutation(child,kelasB,infoC,populationKey);
                            }
                            else
                            {
                                //TODO:KALAU C TAK SAMA B CAM MANE , sepatutnya clear slot , bagi A masuk C yang mutation. tapi malas

                                child = Mutation(child,kelasB, infoA,populationKey);
                            }
                        }
                        else
                        {

                            //KALAU KOSONG CAM MANE

                            if (checkOverlap(infoA, child,kelasB, day, segment1))
                            {
                                String checkError = "[A != NULL | B != NULL | inSegment | noOverlap] \n" + " BEFORE " + child.getName() + "\n" + printTimetable(child);

                                child.clearSubject(infoA.getSubjectCode(), infoA.getSubjectType(),infoA.getGroup());
                                clearKelas(infoA,kelasB,populationKey);

                                child.setTimeslot(infoA, day, segment1, blockA);
                                setKelas(infoA,day,segment1,kelasB,populationKey);

                                checkError += "\nAFTER " + child.getName() + "\n" + printTimetable(child);
                                System.out.println(checkError);
                                //Thread.sleep(1000);

                            }
                            else
                            {
                                //Mutation
                                String checkError = "[A != NULL | B != NULL | inSegment | Overlap] \n" + " BEFORE " + child.getName() + "\n" + printTimetable(child);
                                child = Mutation(child,kelasB, infoA,populationKey);

                                checkError += "\nAFTER " + child.getName() + "\n" + printTimetable(child);
                                System.out.println(checkError);
                                //Thread.sleep(1000);
                            }
                        }
                    }
                    else
                    {
                        // R.MAPPING || NI HAAA REPLACE TANPE MAP !
                        relationshipMapping(parentA,parentB,child,kelasB,infoB,day,populationKey);
                    }

                }
                else// B == Null
                {
                    //CHECK OVERLAPPING
                    if(checkOverlap(infoA,child, kelasB, day,segment1))
                    {
                        String checkError = "[A != NULL | B == NULL | inSegment | noOverlap] \n" + " BEFORE " +  child.getName() +"\n"+ printTimetable(child);

                        child.clearSubject(infoA.getSubjectCode(),infoA.getSubjectType() ,infoA.getGroup());
                        clearKelas(infoA,kelasB,populationKey);

                        child.setTimeslot(infoA,day,segment1,blockA);
                        setKelas(infoA,day,segment1,kelasB,populationKey);

                        checkError += "\nAFTER " + child.getName() +"\n"+ printTimetable(child);
                        System.out.println(checkError);
                        //Thread.sleep(1000);

                    }
                    else
                    {
                        //Mutation
                        String subjectA = infoA.getSubjectCode();
                        char subTypeA = infoA.getSubjectType();

                        String checkError = "[A != NULL | B == NULL | inSegment | Overlap] \n" + " BEFORE " +  child.getName() +"\n"+ printTimetable(child);

                        child.clearSubject(subjectA,subTypeA,infoA.getGroup());
                        clearKelas(infoA,kelasB,populationKey);

                        child =  Mutation(child,kelasB,infoA,populationKey);

                        checkError += "\nAFTER " + child.getName() +"\n"+ printTimetable(child);
                        System.out.printf(checkError);
                        //Thread.sleep(1000);
                    }
                }
            }
        }
//
//        parentB = child;
//        child = new Timetable();

        System.out.println("Parent B Fitness : " + parentB.getFitnessTimetable());

        child.calculateTimetableFitness();
        child.countFitness();

        System.out.println("Child Fitness : " +child.getFitnessTimetable());
        System.out.println("PARENT :- " );
        System.out.println(printTimetable(parentB));
        System.out.println("CHILD : " + child.getName());
        System.out.println(printTimetable(child));
        //Thread.sleep(1000);

        if(parentB.countTimeslot() != child.countTimeslot())
        {
            System.out.println("Child : " + child.countTimeslot() + " Timeslot Parent : " + parentB.countTimeslot() + " Timeslot");
            pressAnyKeyToContinue();
        }



        return child;

    }
    private void pressAnyKeyToContinue()
    {
        System.out.println("Press Enter key to continue...");
        try
        {
            System.in.read();
        }
        catch(Exception e)
        {}
    }

    private void relationshipMapping(Timetable parentA, Timetable parentB,Timetable child, ArrayList kelasB, Information replacement, int day,int populationKey) throws InterruptedException
    {
        int indexSegment;
        String subjectA,subjectB;
        char subTypeA;
        indexSegment = day;//day yang nak kene replace, yang mula2 match
        Information infoReplace = replacement;// simpan untuk replace masa last
        Information timeslotA = parentA.getTimeslot(day,segment1);

        subjectA = parentA.getTimeslot(indexSegment, segment1).getSubjectCode();
        subTypeA = parentA.getTimeslot(indexSegment, segment1).getSubjectType();

        do {

            System.out.println("Relationship mapping \"" + parentB.getName() +"\" \"" + replacement.getSubjectCode()+"\"");

            if(parentA.getTimeslot(indexSegment, segment1)!=null)
            {

                subjectA = parentA.getTimeslot(indexSegment, segment1).getSubjectCode();
                subTypeA = parentA.getTimeslot(indexSegment, segment1).getSubjectType();

                if (checkSegment(parentB, subjectA,subTypeA))// A dalam segment  B , dia kene mapped lagi
                {
                    if(parentA.getTimeslot(indexSegment,segment1).checkInfoEquality(parentB.getTimeslot(indexSegment,segment1)))//TODO:Check sama ke tak : kalau sama biarje
                    {
                        break;
                    }
                    else
                    {
                        System.out.println("Parent A"+printTimetable(parentA));
                        System.out.println( "In segment B - " + indexSegment + " " +subjectA);
                        System.out.println("Parent B"+printTimetable(parentB));
                        //get new indexB
                        indexSegment = getIndex(parentB, subjectA, subTypeA);
                    }

                }
                else {

                    System.out.println("Parent A"+printTimetable(parentA));
                    System.out.println("Not in segment - " +"\""+ subjectA +"\""+ " \"" + subTypeA+"\"");
                    System.out.println("Parent B"+printTimetable(parentB));

                    //it is important sebelum, sebab nnt index berubah
                    //Information infoA = parentA.getTimeslot(indexSegment, segment1);

                    //key id kat luar segment, kat mane A duduk dalam B
                    int targetTime = getIndexTime(parentB, subjectA, subTypeA);
                    int targetDay = getIndexDay(parentB,subjectA, subTypeA);


                    int block;
                    int dayIndex;
                    int timeIndex ;

                    Information infoB = parentB.getTimeslot(day,segment1);
                    Information infoA = parentA.getTimeslot(day,segment1);

                    //timeslot B (dalam segment) letak dalam mapping kat luar segment
                    //timeslot A (dalam segment) letak dalam segment B;

                    // STEP 1 Map location outside segment for B
                    //Done in the loop above
                    // STEP 2 Clear location outside segment B
                    block = parentB.getTimeslot(targetDay,targetTime).getSubjectHour();
                    dayIndex = parentB.getTimeslot(targetDay,targetTime).getDay();
                    timeIndex = parentB.getTimeslot(targetDay,targetTime).getTime();

                    System.out.println("Child before : "+printTimetable(child));

                    if(child.getTimeslot(targetDay,targetTime) == null)
                    {
                        if(checkOverlap(infoA,child,kelasB,targetDay,targetTime) )
                        {
                            child.clearSubject(infoA.getSubjectCode(),infoA.getSubjectType(),infoA.getGroup());
                            clearKelas(infoA,kelasB,populationKey);

                            child.setTimeslot(infoA,targetDay,targetTime,infoA.getSubjectHour());
                            setKelas(infoA,targetDay,targetTime,kelasB,populationKey);
                        }
                        else
                        {
                            child = Mutation(child,kelasB,infoA,populationKey);
                        }
                    }
                    else//c !=null
                    {
                        if(child.equalInfo(infoB,targetDay,targetTime))
                        {
                            int timeC = getIndexTime(child,child.getTimeslot(targetDay,targetTime).getSubjectCode(),child.getTimeslot(targetDay,targetTime).getSubjectType());
                            int dayC = getIndexDay(child,child.getTimeslot(targetDay,targetTime).getSubjectCode(),child.getTimeslot(targetDay,targetTime).getSubjectType());

                            child.clearSubject(infoA.getSubjectCode(),infoA.getSubjectType(),infoA.getGroup());
                            clearKelas(infoA,kelasB,populationKey);

                            child.clearSubject(infoB.getSubjectCode(),infoB.getSubjectType(),infoB.getGroup());
                            clearKelas(infoB,kelasB,populationKey);


                            child.setTimeslot(infoA,timeC,dayC,infoA.getSubjectHour());
                            setKelas(infoA,timeC,dayC,kelasB,populationKey);
                        }
                        else
                        {
                            child = Mutation(child,kelasB, infoA,populationKey);
                        }
                    }

                    System.out.println("Child after : "+printTimetable(child));
//----------------------------------------------------------------------------------------------------------------------

//                    child.clearTimeslot(dayIndex,timeIndex,block);
//
//                    // STEP 3 Move timeslot B to outside segment
//                    if(checkOverlap(infoB,child,dayIndex,timeIndex))
//                    {
//                        child.clearSubject(infoB.getSubjectCode(),infoB.getSubjectType());
//                        child.setTimeslot(infoB,dayIndex,timeIndex,block);//letak yang dalam segment kat luar segment
//                    }
//                    else
//                    {
//                        child = Mutation(child,infoB);
//                    }
//
//                    // STEP 4 Clear timeslot B inside Segment
//                    block = parentB.getTimeslot(day,segment1).getSubjectHour();
//                    dayIndex = parentB.getTimeslot(day,segment1).getDay();
//                    timeIndex = parentB.getTimeslot(day,segment1).getTime();
//                    child.clearTimeslot(dayIndex,timeIndex,block);
//
//                    // STEP 5 Move timslot A inside segment to B
//                    //letak A ke dalam B
//                    if(checkOverlap(timeslotA,child,day,segment1))
//                    {
//
//                        String checkError = "[A != NULL | RelationshipMapping ] \n" + " BEFORE " +  child.getName() +"\n"+ printTimetable(child);
//
//                        //setInfoA
//                        int blockA = timeslotA.getSubjectHour();
//                        child.clearSubject(timeslotA.getSubjectCode(),timeslotA.getSubjectType());
//                        child.setTimeslot(timeslotA,day,segment1,blockA);
//
//                        checkError += "\nAFTER " + child.getName() +"\n"+ printTimetable(child);
//                        System.out.println(checkError);
//                    }
//                    else
//                    {
//                        //Mutation
//                        String checkError = "[A != NULL | RelationshipMapping ] \n" + " BEFORE " +  child.getName() +"\n"+ printTimetable(child);
//
//                        child.clearSubject(timeslotA.getSubjectCode(),timeslotA.getSubjectType());
//                        child = Mutation(child,timeslotA);
//
//                        checkError += "\nAFTER " + child.getName() +"\n"+ printTimetable(child);
//                        System.out.println(checkError);
//                    }
                    break;
                }
            }
            else// parentA timeslot == null
            {
                //TODO : CHECK SINI , WHAT IF B != NULL ? | B tu kene check dulu dalam segment ke tak,kalau tak either biar or mutate.


                Information infoB = parentB.getTimeslot(day, segment1);
                Information infoA = parentA.getTimeslot(day,segment1);
                Information infoC = child.getTimeslot(day, segment1);

                System.out.println("ParentA slot == Null : Initiate Mutation...");
                System.out.println("Clear choosen subject timeslot Child dulu, baru ade space ");

                if(child.getTimeslot(day,segment1) != null)
                {
                    System.out.println(child.getName() + " " + child.getTimeslot(day, segment1).getSubjectCode());
                    System.out.println(" day " + child.getTimeslot(day, segment1).getDay());
                    System.out.println(" time " + child.getTimeslot(day, segment1).getDay());
                    System.out.println(printTimetable(child));


                    int block = infoC.getSubjectHour();
                    int dayIndex = infoC.getDay();
                    int timeIndex = infoC.getTime();

                    System.out.println("Clear timeslot : " + child.getName() + " - " + child.getTimeslot(dayIndex, timeIndex).getSubjectCode());

                    //Thread.sleep(3000);
                    child.clearTimeslot(dayIndex, timeIndex, block);
                    clearKelas(infoC,kelasB,populationKey);

                    if (checkOverlap(infoA, child, kelasB,day, segment1))//check overlap lu
                    {
                        child.clearSubject(infoA.getSubjectCode(),infoA.getSubjectType(),infoA.getGroup());
                        clearKelas(infoA,kelasB,populationKey);

                        child.setTimeslot(infoA, day, segment1, infoA.getSubjectHour());
                        setKelas(infoA,day,segment1,kelasB,populationKey);


                    } else {
                        child = Mutation(child,kelasB, infoA,populationKey);
                    }

                    //IF  paretnB in segment, IF child isEmpty
                    if(!checkSegment(parentA,infoC.getSubjectCode(),infoC.getSubjectType())) {
                        child = Mutation(child,kelasB,infoC,populationKey);//Replacement or letak balik dah clear
                    }
                    else
                    {
                        if(getIndex(parentA,infoC.getSubjectCode(),infoC.getSubjectType()) < day)// maknanye yang selepas je , akan di replace, kalau sebelum dia kene mutate sbb nak pastikan ade dalam timeslot
                        {
                            child = Mutation(child,kelasB,infoC,populationKey);
                        }
                    }


                    String checkError = "[A != NULL | RelationshipMapping ] \n" + " BEFORE " + child.getName() + "\n" + printTimetable(child);
                    checkError += "\nAFTER " + child.getName() + "\n" + printTimetable(child);
                    System.out.println(checkError);
                }
                break;
            }

        }while (checkSegment(parentB,subjectA,subTypeA));


    }

    private int getIndex(Timetable timetable, String subject, char subType)
    {
        for(int i =0; i < 5; i++)
        {
            if(timetable.getTimeslot(i,segment1) != null)
            {
                if(       timetable.getTimeslot(i,segment1).getSubjectCode().equalsIgnoreCase(subject)&&
                                (timetable.getTimeslot(i,segment1).getSubjectType() == subType)
                        )
                {
                    return i;
                }
            }
        }

        return -1;
    }

    private int getIndexTime(Timetable timetable, String subject,char subType)
    {
        for(int i =0; i < 5; i++)
        {
            for(int j =0; j<10; j++)
            {
                if(timetable.getTimeslot(i,j) != null)
                {
                    if(timetable.getTimeslot(i,j).getSubjectCode().equalsIgnoreCase(subject)&&
                            (timetable.getTimeslot(i,j).getSubjectType() == subType)
                            )
                    {
                        return j;
                    }
                }
            }
        }

        return -1;

    }

    private int getIndexDay(Timetable timetable, String subject,char subType)
    {
        for(int i =0; i < 5; i++)
        {
            for(int j =0; j<10; j++)
            {
                if(timetable.getTimeslot(i,j) != null)
                {
                    if(timetable.getTimeslot(i,j).getSubjectCode().equalsIgnoreCase(subject)&&
                            (timetable.getTimeslot(i,j).getSubjectType() == subType)
                            )
                    {
                        return i;
                    }
                }

            }
        }

        return -1;
    }

    private Timetable Mutation(Timetable timetable, ArrayList kelasList, Information info,int populationKey)
    {
        //INSERTION
        timetable.clearSubject(info.getSubjectCode(),info.getSubjectType(),info.getGroup());

        Timetable kelasSet = findKelas(info,kelasList);// ni untuk kelas yang nak set tu
        clearKelas(info,kelasList,populationKey);

        Random rg = new Random();
        int block = info.getSubjectHour();
        int day = rg.nextInt(5);
        int time = 0;//rg.nextInt(11);
        boolean kelasPass = false;


        while(!timetable.checkTimeslot(day,time,block) ||
               !kelasSet.checkTimeslot(day,time,block)
                )
        {
            kelasPass = false;
            System.out.println("\n Mutation... \n Child "+timetable.getName()+"\n"+printTimetable(timetable) + "\n");
            System.out.println("Kelas Child : " + kelasSet.getName() + " "+ printTimetable(kelasSet));
            System.out.println("Find random day/time that fit for mutation " + day + " " + time + " " + info.getSubjectCode() + " hour : " +info.getSubjectHour() + " " + info.getSubjectType());

            printTimetable(timetable);
            day = new Random().nextInt(5);
            time = new Random().nextInt(11 - block);

            //Making spaces : have to check for kelas as well
            if(timetable.getTimeslot(day,time) != null)
            {
                Timetable kelas = findKelas(timetable.getTimeslot(day,time),kelasList);// ni kelas yang nak gerak ke kiri je, based on info gak.
                //ke kiri

                if(timetable.isLeftEmpty(day,time))
                {
                    if(kelas.isLeftEmpty(day,time))
                    {
                        timetable.moveLeft(day,time);
                        kelas.moveLeft(day,time);//kat sini kene set balik kelas

                        //clearKelas(timetable.getTimeslot(day,time),kelasList,populationKey);
                        setKelas(kelas,kelasList,populationKey);
                    }
                }
                //timetable.moveLeft(day,time);
            }
            //end makeSpace

            //basically bila dah dapat empty space kat group, dia cari empty space kat kelas pulak.
            if(timetable.checkTimeslot(day,time,block))
            {
                System.out.println(ANSI_RED +"Group Found ! find empty Kelas..."+ ANSI_RED);
                //Thread.sleep(1000);
                if (!kelasSet.checkTimeslot(day, time, block))
                {
                    System.out.println(ANSI_RED +"Group Found ! find empty Kelas..."+ ANSI_RED);
                    Timetable emptyKelas = findEmptyKelas(day, time, block, kelasList);
                    if(emptyKelas.getName() != null)
                    {
                        System.out.println(ANSI_RED +"Empty kelas found !" + ANSI_RED);
                        kelasSet = emptyKelas;
                        info.changeKelasInfo(emptyKelas.getObject());
                    }
                    //info.changeKelasInfo(emptyKelas.getObject());
                }
            }


        }//endWhile

        if(timetable.checkTimeslot(day,time,block))
        {


            System.out.println(info.getSubjectHour() + " is setted on " + day + " " + time);
            timetable.setTimeslot(info,day,time,block);

            clearKelas(info,kelasList,populationKey);
            setKelas(info,day,time,kelasList,populationKey);
        }


        return timetable;
    }
    private Timetable makeSpace(Timetable timetable)
    {
        for(int day = 0; day < 5; day++)
        {
            for(int time=0; time <10; time++)
            {

            }
        }

        return timetable;
    }

    private String addSpace(int num)
    {
        String space = "";
        for(int i=0; i <num; i++)
        {
            space += " ";

        }

        return space;
    }

    public String printTimetable(Timetable t)
    {
        int spaceNum = 15;
        String numberOfSpaces = String.valueOf(spaceNum);
        String timetable = "";
        for(int day =0; day <5; day++)
        {
            timetable += "\n" + String.format("%-" + numberOfSpaces+"s", GeneticAlgorithm.dayTranslate(day));
            //subjucet
            for(int time = 0; time <10; time++){

                if(t.getTimeslot(day,time) != null){

                    Information info = t.getTimeslot(day, time);
                    String text = ""+ info.getSubjectCode() + " - "+ info.getSubjectType()+"";
                    timetable+= text + addSpace(spaceNum - text.length());
                    //timetable +=  String.format("%-"+String.valueOf(spaceNum - text.length())+"s", text);
                }
                else
                {
                    String text = "EMPTY";
                    timetable+= ANSI_RED +text+ ANSI_RESET + addSpace(spaceNum - text.length()) ;
                    //timetable += String.format("%-" + String.valueOf(spaceNum - text.length())+"s", " " + ANSI_RED + text + ANSI_RESET + " ");
                }
            }
            timetable += "\n" + String.format("%-" + numberOfSpaces+"s",GeneticAlgorithm.dayTranslate(day));
            //kelas
            for(int time = 0; time <10; time++){

                if(t.getTimeslot(day,time) != null)
                {
                    //count++;
                    Information info = t.getTimeslot(day, time);
                    String text =info.getKelas();
                    timetable+= text + addSpace(spaceNum - text.length());
                    //timetable +=  String.format("%-"+numberOfSpaces+ "s", info.getKelas() );
                }
                else
                {
                    String text = "KELAS";
                    timetable+= ANSI_YELLOW +text+ ANSI_RESET  + addSpace(spaceNum - text.length());
                    //timetable += String.format("%-"+numberOfSpaces+"s","KELAS");

                }
            }
            timetable += "\n";
        }

        return timetable;
    }


    private boolean checkSegment(Timetable timetable,String subcode,char subtype)
    {
        //TRUE = ade dalam segment
        //timetable A <-- the segment you want to check, subcode in segment A tak ?
        for(int day=0; day <5; day++)
        {
            if(timetable.getTimeslot(day, segment1) != null) {
                if (timetable.getTimeslot(day, segment1).checkSubCode(subcode) &&
                        (timetable.getTimeslot(day,segment1).getSubjectType() == subtype)
                        ) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkSegment(Timetable timetable,String subcode,char subtype,int hari)
    {
        //TRUE = ade dalam segment
        //timetable A <-- the segment you want to check, subcode in segment A tak ?
        for(int day=hari; day <5; day++)
        {
            if(timetable.getTimeslot(day, segment1) != null) {
                if (timetable.getTimeslot(day, segment1).checkSubCode(subcode) &&
                        (timetable.getTimeslot(day,segment1).getSubjectType() == subtype)
                        ) {
                    return true;
                }
            }
        }

        return false;
    }

    //FALSE = OVERLAP; TRUE THAT IT IS NOT OVERLAP
    private boolean checkOverlap(Information info,Timetable timetable,ArrayList kelasList, int day , int time)
    {
        Timetable kelas = findKelas(info,kelasList);
        //info = information that wanted to be sloted in timetable,
        //basically, info A to be slotted in timetable B
        if((info.getSubjectHour() + time) > 10)
        {
         return false;
        }
        else if(timetable.checkTimeslot(day,time,info.getSubjectHour()))
        {
            if(kelas.checkTimeslot(day,time,info.getSubjectHour()))
            {
                return true;
            }
        }

        //TODO: checkTimeslot (True that it is empty)
        return false;
    }

    public void clearKelas(Information infoC,ArrayList kelasList, int populationKey)
    {

//        Timetable kelas = findKelas(infoC,kelasList);
//        kelas.clearSubject(infoC.getSubjectCode(),infoC.getSubjectType(),infoC.getGroup());
//        //masukkan balik
//        setKelas(kelas,kelasList,populationKey);


        // KENE GO THROUGH SEMUA ! BUKAN BY TABLE, SEBAB TABLE TU YANG KELAS A NYE, BUKAN YANG B NYE
        Timetable kelas; // = new Timetable();

        for(int i =0; i < kelasList.size(); i++)
        {
            kelas = (Timetable) kelasList.get(i);
            kelas.clearSubject(infoC.getSubjectCode(),infoC.getSubjectType(),infoC.getGroup());

                kelasList.set(i,kelas);// ganti kelas dalam arraylist
                Population temp = pop.get(populationKey);
                temp.settKelasList(kelasList);//ganti arraylist dalam population
                pop.set(populationKey,temp);
        }

    }

    private Timetable findKelas(Information info, ArrayList kelasList)
    {
        Timetable kelas = new Timetable();

        for(int i =0; i < kelasList.size(); i++)
        {
            kelas = (Timetable) kelasList.get(i);

            if(info.getKelas().equalsIgnoreCase(kelas.getName()))
            {
                return kelas;
            }
        }

        return kelas;
    }

    private Timetable findEmptyKelas(int day,int time,int block,ArrayList kelasList)
    {
        for(int i=0; i < kelasList.size(); i++)
        {
            Timetable temp = (Timetable)kelasList.get(i);
            if(temp.checkTimeslot(day,time,block))
            {
                return temp;
            }
        }
        Timetable timetable = new Timetable();
        return  timetable;
    }

    private void setKelas(Timetable kelasBaru, ArrayList kelasList,int populationKey)
    {
        Timetable kelasLama; // = new Timetable();

        for(int i =0; i < kelasList.size(); i++)
        {
            kelasLama = (Timetable) kelasList.get(i);

            if(kelasBaru.getName().equalsIgnoreCase(kelasLama.getName()))
            {
                kelasList.set(i,kelasBaru);// ganti kelas dalam arraylist
                Population temp = pop.get(populationKey);
                temp.settKelasList(kelasList);//ganti arraylist dalam population
                pop.set(populationKey,temp);
            }
        }
    }
    private void setKelas(Information info,int day,int time, ArrayList kelasList,int populationKey)
    {
        Timetable kelasBaru = findKelas(info,kelasList);
        kelasBaru.clearSubject(info.getSubjectCode(),info.getSubjectType(),info.getGroup());
        kelasBaru.setTimeslot(info,day,time,info.getSubjectHour());

        Timetable kelasLama;
        for(int i =0; i < kelasList.size(); i++)
        {
            kelasLama = (Timetable) kelasList.get(i);
            if(kelasBaru.getName().equalsIgnoreCase(kelasLama.getName()))
            {
                kelasList.set(i,kelasBaru);
                Population temp = pop.get(populationKey);
                temp.settKelasList(kelasList);
                pop.set(populationKey,temp);
            }
        }
    }


}
