package de.decidr.model.entities;

// Generated 30.10.2009 13:07:36 by Hibernate Tools 3.2.4.GA

import java.util.Date;

/**
 * Log generated by hbm2java
 */
public class Log implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long id;
    private String prio;
    private Long iprio;
    private String category;
    private String msg;
    private String layoutMsg;
    private String throwable;
    private String ndc;
    private String mdc;
    private String mdc2;
    private String info;
    private String addon;
    private Date logDate;
    private String createdBy;
    private String thread;

    public Log() {
        //default empty JavaBean constructor
    }

    public Log(String prio) {
        //generated minimal constructor
        this.prio = prio;
    }

    public Log(String prio, Long iprio, String category, String msg,
            String layoutMsg, String throwable, String ndc, String mdc,
            String mdc2, String info, String addon, Date logDate,
            String createdBy, String thread) {
        //generated full constructor
        this.prio = prio;
        this.iprio = iprio;
        this.category = category;
        this.msg = msg;
        this.layoutMsg = layoutMsg;
        this.throwable = throwable;
        this.ndc = ndc;
        this.mdc = mdc;
        this.mdc2 = mdc2;
        this.info = info;
        this.addon = addon;
        this.logDate = logDate;
        this.createdBy = createdBy;
        this.thread = thread;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrio() {
        return this.prio;
    }

    public void setPrio(String prio) {
        this.prio = prio;
    }

    public Long getIprio() {
        return this.iprio;
    }

    public void setIprio(Long iprio) {
        this.iprio = iprio;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLayoutMsg() {
        return this.layoutMsg;
    }

    public void setLayoutMsg(String layoutMsg) {
        this.layoutMsg = layoutMsg;
    }

    public String getThrowable() {
        return this.throwable;
    }

    public void setThrowable(String throwable) {
        this.throwable = throwable;
    }

    public String getNdc() {
        return this.ndc;
    }

    public void setNdc(String ndc) {
        this.ndc = ndc;
    }

    public String getMdc() {
        return this.mdc;
    }

    public void setMdc(String mdc) {
        this.mdc = mdc;
    }

    public String getMdc2() {
        return this.mdc2;
    }

    public void setMdc2(String mdc2) {
        this.mdc2 = mdc2;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getAddon() {
        return this.addon;
    }

    public void setAddon(String addon) {
        this.addon = addon;
    }

    public Date getLogDate() {
        return this.logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getThread() {
        return this.thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

}
