package de.decidr.model.entities;

// Generated 04.07.2009 15:27:24 by Hibernate Tools 3.2.4.GA

import java.util.Date;

/**
 * InvitationView generated by hbm2java
 */
public class InvitationView implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private long id;
    private long senderId;
    private long receiverId;
    private Long participateInWorkflowInstanceId;
    private Long joinTenantId;
    private Long administrateWorkflowModelId;
    private Date creationDate;
    private String senderFirstName;
    private String senderLastName;
    private String receiverFirstName;
    private String receiverLastName;
    private String administratedWorkflowModelName;
    private String participateWorfkwlowModelName;
    private String joinTenantName;
    private String workflowModelOwningTenantName;
    private String participateTenantName;

    public InvitationView() {
    }

    public InvitationView(long id, long senderId, long receiverId,
            Date creationDate) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.creationDate = creationDate;
    }

    public InvitationView(long id, long senderId, long receiverId,
            Long participateInWorkflowInstanceId, Long joinTenantId,
            Long administrateWorkflowModelId, Date creationDate,
            String senderFirstName, String senderLastName,
            String receiverFirstName, String receiverLastName,
            String administratedWorkflowModelName,
            String participateWorfkwlowModelName, String joinTenantName,
            String workflowModelOwningTenantName, String participateTenantName) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.participateInWorkflowInstanceId = participateInWorkflowInstanceId;
        this.joinTenantId = joinTenantId;
        this.administrateWorkflowModelId = administrateWorkflowModelId;
        this.creationDate = creationDate;
        this.senderFirstName = senderFirstName;
        this.senderLastName = senderLastName;
        this.receiverFirstName = receiverFirstName;
        this.receiverLastName = receiverLastName;
        this.administratedWorkflowModelName = administratedWorkflowModelName;
        this.participateWorfkwlowModelName = participateWorfkwlowModelName;
        this.joinTenantName = joinTenantName;
        this.workflowModelOwningTenantName = workflowModelOwningTenantName;
        this.participateTenantName = participateTenantName;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSenderId() {
        return this.senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getReceiverId() {
        return this.receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public Long getParticipateInWorkflowInstanceId() {
        return this.participateInWorkflowInstanceId;
    }

    public void setParticipateInWorkflowInstanceId(
            Long participateInWorkflowInstanceId) {
        this.participateInWorkflowInstanceId = participateInWorkflowInstanceId;
    }

    public Long getJoinTenantId() {
        return this.joinTenantId;
    }

    public void setJoinTenantId(Long joinTenantId) {
        this.joinTenantId = joinTenantId;
    }

    public Long getAdministrateWorkflowModelId() {
        return this.administrateWorkflowModelId;
    }

    public void setAdministrateWorkflowModelId(Long administrateWorkflowModelId) {
        this.administrateWorkflowModelId = administrateWorkflowModelId;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getSenderFirstName() {
        return this.senderFirstName;
    }

    public void setSenderFirstName(String senderFirstName) {
        this.senderFirstName = senderFirstName;
    }

    public String getSenderLastName() {
        return this.senderLastName;
    }

    public void setSenderLastName(String senderLastName) {
        this.senderLastName = senderLastName;
    }

    public String getReceiverFirstName() {
        return this.receiverFirstName;
    }

    public void setReceiverFirstName(String receiverFirstName) {
        this.receiverFirstName = receiverFirstName;
    }

    public String getReceiverLastName() {
        return this.receiverLastName;
    }

    public void setReceiverLastName(String receiverLastName) {
        this.receiverLastName = receiverLastName;
    }

    public String getAdministratedWorkflowModelName() {
        return this.administratedWorkflowModelName;
    }

    public void setAdministratedWorkflowModelName(
            String administratedWorkflowModelName) {
        this.administratedWorkflowModelName = administratedWorkflowModelName;
    }

    public String getParticipateWorfkwlowModelName() {
        return this.participateWorfkwlowModelName;
    }

    public void setParticipateWorfkwlowModelName(
            String participateWorfkwlowModelName) {
        this.participateWorfkwlowModelName = participateWorfkwlowModelName;
    }

    public String getJoinTenantName() {
        return this.joinTenantName;
    }

    public void setJoinTenantName(String joinTenantName) {
        this.joinTenantName = joinTenantName;
    }

    public String getWorkflowModelOwningTenantName() {
        return this.workflowModelOwningTenantName;
    }

    public void setWorkflowModelOwningTenantName(
            String workflowModelOwningTenantName) {
        this.workflowModelOwningTenantName = workflowModelOwningTenantName;
    }

    public String getParticipateTenantName() {
        return this.participateTenantName;
    }

    public void setParticipateTenantName(String participateTenantName) {
        this.participateTenantName = participateTenantName;
    }

}
