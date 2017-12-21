import java.util.Arrays;

public class Timetable {

    private String name;
    private Information[][] timeslot = new Information[5][10];
    private int fitnessTimetable;



    public Timetable()
    {
       this.fitnessTimetable =0;
    }

    public Timetable(Timetable timetable)
    {

        this.name = timetable.getName();
        this.fitnessTimetable = timetable.getFitnessTimetable();
        setAllTimeslot(timetable);


    }
    String getName() {
        return name;
    }

    void setName(String name)
    {
        this.name = name;
    }

//    public Information[][] getTimeslot() {
//        return timeslot;
//    }

    public Information getTimeslot(int day,int time)
    {
        return this.timeslot[day][time];
    }

    boolean checkTimeslot(int day,int time,int block)
    {
        for(int i=time; i < time+block; i++)
        {
            if(timeslot[day][i] != null)
            {
                //System.out.println(name + " " +timeslot[day][i].getSubjectCode() + " TAKEN " + day +" "+ time + " - " + block);
                return false;
            }
        }
    return true; //true = empty, can go in la
    }

    boolean checkTimeslot(int day,int time)
    {
            if(timeslot[day][time] != null)
            {
                //System.out.println(name + " " +timeslot[day][i].getSubjectCode() + " TAKEN " + day +" "+ time + " - " + block);
                return false;//ade
            }
        return true; //true = empty, can go in la
    }
    void clearTimeslot(int day,int time,int block)
    {
        for(int i=time; i < time+block; i++)
        {
            //System.out.println("\t" +info.toString() + "Masuk");

            this.timeslot[day][i] = null;
        }

    }
    void clearAllTimeslot()
    {


        for(int i=0; i<5; i++)
        {
            for(int j=0; j<10; j++)
            {
                timeslot[i][j] = null;
            }
        }

    }

    void setTimeslot(Information info, int day, int time, int block)
    {

        for(int i=time; i < time+block; i++)
        {
            //System.out.println("\t" +info.toString() + "Masuk");
            this.timeslot[day][i] = new Information();

            this.timeslot[day][i].setFitness(info.getFitness());

            //Group
            this.timeslot[day][i].setGroup(info.getGroup());
            this.timeslot[day][i].setStudnum(info.getStudnum());

            //Kelas
            this.timeslot[day][i].setKelas(info.getKelas());
            this.timeslot[day][i].setKelasType(info.getKelasType());
            this.timeslot[day][i].setKelasCap(info.getKelasCap());

            //Lecturer
            this.timeslot[day][i].setLecturer(info.getLecturer());

            //Subject
            this.timeslot[day][i].setSubjectCode(info.getSubjectCode());
            this.timeslot[day][i].setSubjectHour(info.getSubjectHour());
            this.timeslot[day][i].setSubjectType(info.getSubjectType());


            //index
            this.timeslot[day][i].setDay(day);// berpindah day baru
            this.timeslot[day][i].setTime(time); // berpindah time baru
            this.timeslot[day][i].setGroupIndex(info.getGroupIndex());
            this.timeslot[day][i].setLecturerIndex(info.getLecturerIndex());
            this.timeslot[day][i].setKelasIndex(info.getKelasIndex());

        }
    }

    public void setAllTimeslot(Timetable timetable)
    {
        for(int i=0; i <5; i++)
        {
            for(int j =0; j<10; j++)
            {
                Information info = timetable.getTimeslot(i,j);

                if(info != null)
                {
                    //System.out.println("\t" +info.toString() + "Masuk");
                    this.timeslot[i][j] = new Information();

                    this.timeslot[i][j].setFitness(info.getFitness());

                    //Group
                    this.timeslot[i][j].setGroup(info.getGroup());
                    this.timeslot[i][j].setStudnum(info.getStudnum());

                    //Kelas
                    this.timeslot[i][j].setKelas(info.getKelas());
                    this.timeslot[i][j].setKelasType(info.getKelasType());
                    this.timeslot[i][j].setKelasCap(info.getKelasCap());

                    //Lecturer
                    this.timeslot[i][j].setLecturer(info.getLecturer());

                    //Subject
                    this.timeslot[i][j].setSubjectCode(info.getSubjectCode());
                    this.timeslot[i][j].setSubjectHour(info.getSubjectHour());
                    this.timeslot[i][j].setSubjectType(info.getSubjectType());


                    //index
                    this.timeslot[i][j].setDay(info.getDay());
                    this.timeslot[i][j].setTime(info.getTime());
                    this.timeslot[i][j].setGroupIndex(info.getGroupIndex());
                    this.timeslot[i][j].setLecturerIndex(info.getLecturerIndex());
                    this.timeslot[i][j].setKelasIndex(info.getKelasIndex());
                }
            }
        }
    }

    public void countFitness()
    {
        int count =0;
        for(int i=0; i<5; i++)
        {
            for(int j=0; j<10; j++)
            {
                if(this.timeslot[i][j] != null) {
                    count += this.timeslot[i][j].getFitness();
                    //System.out.println(name + " MASUK COUNT = "+ i+j+this.timeslot[i][j].getFitness() );
                }
            }
        }

        this.fitnessTimetable = count;
    }

    public void clearTimeslotFitness()
    {
        for(int i=0; i<5; i++)
        {
            for(int j=0; j<10; j++)
            {
                if(this.timeslot[i][j] != null) {
                    this.timeslot[i][j].setFitness(0);
                }
            }
        }
    }
    public void clearSubject(String subject, char subType)
    {
        for(int i=0; i<5; i++)
        {
            for(int j=0; j<10; j++)
            {
                if(timeslot[i][j] != null) {
                    if (
                            timeslot[i][j].getSubjectCode().equalsIgnoreCase(subject) &&
                                    (timeslot[i][j].getSubjectType() == subType)
                            )
                    {

                        timeslot[i][j] = null;

                    }
                }
            }
        }

    }
    public void calculateTimetableFitness()
    {

        for(int day =0; day <5; day++)
        {
            for(int time =0; time < 10; time++)
            {
                if(this.timeslot[day][time] != null)
                {
                    this.timeslot[day][time].setFitness( 0);//clear lu, sebab di additive nnt makin banyak
                }

                //pukul 12 - 2 denda
                if(time == 4 || time ==5)
                {
                    if(!checkTimeslot(day,time))
                    {

                        this.timeslot[day][time].addFitness();
                        //System.out.println(((Timetable) ReadData.tGroupList.get(t)).getTimeslot(day, time).getFitness() + " |  " + day + " - " + time +" In" + ((Timetable) ReadData.tGroupList.get(t)).getTimeslot(day, time).getSubjectCode());

                    }
                }

                //calculate gapss
                if(!checkTimeslot(day,time))
                {
                    //hok belakey
                    for(int i = time-1; i >=0; i--)
                    {
                        if(checkTimeslot(day,i))
                        {
                            int fit = this.timeslot[day][time].getFitness();

                            int gap = Math.abs(time-i-1);// -1 sebab kalau 8-7 = 1 , supposely dorang bersebalahan takde gap
                            //System.out.println( "GAP : " + gap);
                            //System.out.println( "\t In GAP : " + fit);
                            this.timeslot[day][time].setFitness( fit + gap) ;
                            fit = this.timeslot[day][time].getFitness();
                            //System.out.println( "\t out GAP : " + fit);
                            break;
                        }
                    }
                    //hok depey
                    for(int i = time+1; i <10; i++)
                    {
                        if(!checkTimeslot(day,i))
                        {
                            int fit = this.timeslot[day][time].getFitness();
                            int gap = Math.abs(i-1-time);
                            //System.out.println( "GAP : " + gap);
                            //System.out.println( "\t In GAP : " + fit);
                            this.timeslot[day][time].setFitness( fit + gap) ;
                            fit = this.timeslot[day][time].getFitness();
                            //System.out.println( "\t out GAP : " + fit);
                            break;
                        }
                    }
                } // end calculate gaps
            }
        }

    }

    public int getFitnessTimetable() {
        return fitnessTimetable;
    }

    public void setFitness(int fitness) {
        this.fitnessTimetable = fitness;
    }

    public void addTimeslotFitness(int day, int time)
    {
        this.timeslot[day][time].addFitness();
    }


    @Override
    public String toString() {
        return "Timetable{" +
                "name='" + name + '\'' +
                ", timeslot=" + Arrays.toString(timeslot) +
                '}';
    }
}
