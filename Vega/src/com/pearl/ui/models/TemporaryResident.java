package com.pearl.ui.models;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class TemporaryResident.
 */
public class TemporaryResident implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The student visa. */
    private boolean studentVisa;
    
    /** The pre arranged emp or dependent. */
    private boolean preArrangedEmpOrDependent;
    
    /** The memberof diplomatic corps. */
    private boolean memberofDiplomaticCorps;
    
    /** The asa student. */
    private boolean asaStudent;
    
    /** The others. */
    private boolean others;

    /**
     * Checks if is student visa.
     *
     * @return the studentVisa
     */
    public boolean isStudentVisa() {
	return studentVisa;
    }

    /**
     * Sets the student visa.
     *
     * @param studentVisa the studentVisa to set
     */
    public void setStudentVisa(boolean studentVisa) {
	this.studentVisa = studentVisa;
    }

    /**
     * Checks if is pre arranged emp or dependent.
     *
     * @return the preArrangedEmpOrDependent
     */
    public boolean isPreArrangedEmpOrDependent() {
	return preArrangedEmpOrDependent;
    }

    /**
     * Sets the pre arranged emp or dependent.
     *
     * @param preArrangedEmpOrDependent the preArrangedEmpOrDependent to set
     */
    public void setPreArrangedEmpOrDependent(boolean preArrangedEmpOrDependent) {
	this.preArrangedEmpOrDependent = preArrangedEmpOrDependent;
    }

    /**
     * Checks if is memberof diplomatic corps.
     *
     * @return the memberofDiplomaticCorps
     */
    public boolean isMemberofDiplomaticCorps() {
	return memberofDiplomaticCorps;
    }

    /**
     * Sets the memberof diplomatic corps.
     *
     * @param memberofDiplomaticCorps the memberofDiplomaticCorps to set
     */
    public void setMemberofDiplomaticCorps(boolean memberofDiplomaticCorps) {
	this.memberofDiplomaticCorps = memberofDiplomaticCorps;
    }

    /**
     * Checks if is others.
     *
     * @return the others
     */
    public boolean isOthers() {
	return others;
    }

    /**
     * Sets the others.
     *
     * @param others the others to set
     */
    public void setOthers(boolean others) {
	this.others = others;
    }

    /**
     * Checks if is asa student.
     *
     * @return the asaStudent
     */
    public boolean isAsaStudent() {
	return asaStudent;
    }

    /**
     * Sets the asa student.
     *
     * @param asaStudent the asaStudent to set
     */
    public void setAsaStudent(boolean asaStudent) {
	this.asaStudent = asaStudent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "TemporaryResident [studentVisa=" + studentVisa
		+ ", preArrangedEmpOrDependent=" + preArrangedEmpOrDependent
		+ ", memberofDiplomaticCorps=" + memberofDiplomaticCorps
		+ ", asaStudent=" + asaStudent + ", others=" + others + "]";
    }
}
