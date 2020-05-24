package main.java;

public class Actor {
    String stagename;
    String dowstart;
    String dowend;
    String familyname;
    String firstname;
    String gender;
    String dob;
    String dod;
    String roletype;
    String origin;
    String picref;
    String relationships;
    String notes;
    public Actor() {
    }
    public Actor(String stagename, String firstname, String familyname, String dob){
        this.stagename = stagename;
        this.firstname = firstname;
        this.familyname = familyname;
        this.dob = dob;
    }

    public String getStagename() {
        return stagename;
    }

    public void setStagename(String stagename) {
        this.stagename = stagename;
    }

    public String getDowstart() {
        return dowstart;
    }

    public void setDowstart(String dowstart) {
        this.dowstart = dowstart;
    }

    public String getDowend() {
        return dowend;
    }

    public void setDowend(String dowend) {
        this.dowend = dowend;
    }

    public String getFamilyname() {
        return familyname;
    }

    public void setFamilyname(String familyname) {
        this.familyname = familyname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDod() {
        return dod;
    }

    public void setDod(String dod) {
        this.dod = dod;
    }

    public String getRoletype() {
        return roletype;
    }

    public void setRoletype(String roletype) {
        this.roletype = roletype;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getPicref() {
        return picref;
    }

    public void setPicref(String picref) {
        this.picref = picref;
    }

    public String getRelationships() {
        return relationships;
    }

    public void setRelationships(String relationships) {
        this.relationships = relationships;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Actor Details - ");
        sb.append("Stage Name:" + getStagename());
        sb.append(", ");
        sb.append("DOB:" + getDob());
        sb.append(", ");

        return sb.toString();
    }
}


