public class Information {
    String subjectCode;
    String group;
    int studnum;
    String lecturer;
    String kelas;
    int kelasCap;
    char kelasType;
    String type;

    int fitness;




    public Information (){
        this.subjectCode = "";
        this.group = "";
        this.studnum = 0;
        this.lecturer = "";
        this.kelas = "";
        this.fitness = 0;
        this.type = "";

    }


    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }
    public void addFitness()
    {
        System.out.println(subjectCode + " add call");
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
