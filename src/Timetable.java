import java.util.Arrays;

public class Timetable {

    private String name;
    private Information[][] timeslot;
    private int fitnessTimetable;



    public Timetable()
    {
       timeslot = new Information[5][10];
       this.fitnessTimetable =0;
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
            this.timeslot[day][i].setLecturer(info.getLecturer());
            this.timeslot[day][i].setStudnum(info.getStudnum());
            this.timeslot[day][i].setSubjectCode(info.getSubjectCode());
            this.timeslot[day][i].setGroup(info.getGroup());
            this.timeslot[day][i].setType(info.getType());
            this.timeslot[day][i].setKelas(info.getKelas());
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
