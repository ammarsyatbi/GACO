public class Information {

    //subject variable
    String subjectCode;
    int subjectHour;

    //group variable
    String group;
    int studnum;

    //lecturer variable
    String lecturer;

    //kelas variables
    String kelas;
    int kelasCap;
    char kelasType;
    String type;

    int fitness;

    //indexes
    //ID timeslot
    //key index
    int day;
    int time;
    int kelasIndex;
    int groupIndex;
    int lecturerIndex;




    public Information (){
        this.subjectCode = "";
        this.group = "";
        this.studnum = 0;
        this.lecturer = "";
        this.kelas = "";
        this.fitness = 0;
        this.type = "";

    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getKelasIndex() {
        return kelasIndex;
    }

    public void setKelasIndex(int kelasIndex) {
        this.kelasIndex = kelasIndex;
    }

    public int getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(int groupIndex) {
        this.groupIndex = groupIndex;
    }

    public int getLecturerIndex() {
        return lecturerIndex;
    }

    public void setLecturerIndex(int lecturerIndex) {
        this.lecturerIndex = lecturerIndex;
    }

    public int getSubjectHour() {
        return subjectHour;
    }

    public void setSubjectHour(int subjectHour) {
        this.subjectHour = subjectHour;
    }

    public int getKelasCap() {
        return kelasCap;
    }

    public void setKelasCap(int kelasCap) {
        this.kelasCap = kelasCap;
    }

    public char getKelasType() {
        return kelasType;
    }

    public void setKelasType(char kelasType) {
        this.kelasType = kelasType;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }
    public void addFitness()
    {
        //System.out.println(subjectCode + " add call");
        this.fitness++;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getStudnum() {
        return studnum;
    }

    public void setStudnum(int studnum) {
        this.studnum = studnum;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public boolean checkSubCode(String subcode)
    {
        if(subcode.equalsIgnoreCase(this.subjectCode))
        {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "Information{" +
                "subjectCode='" + subjectCode + '\'' +
                ", group='" + group + '\'' +
                ", studnum=" + studnum +
                ", lecturer='" + lecturer + '\'' +
                ", kelas='" + kelas + '\'' +
                '}';
    }
}
