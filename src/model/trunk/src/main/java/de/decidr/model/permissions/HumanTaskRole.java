package de.decidr.model.permissions;

public class HumanTaskRole extends AbstractRole {

    protected static final Long HUMAN_TASK_ACTOR_ID = -1337L;
    
    public HumanTaskRole() {
        super(HUMAN_TASK_ACTOR_ID);
    }

}
