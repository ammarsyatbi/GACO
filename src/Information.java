public class Information {
    String subjectCode;
    String group;
    int studnum;
    String lecturer;
    String kelas;
    String type;



    public Information (){
        this.subjectCode = "";
        this.group = "";
        this.studnum = 0;
        this.lecturer = "";
        this.kelas = "";
    }

    public Information(String subjectCode, String group, int studnum, String lecturer, String kelas) {
        this.subjectCode = subjectCode;
        this.group = group;
        this.studnum = studnum;
        this.lecturer = lecturer;
        this.kelas = kelas;
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
