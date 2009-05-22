package de.decidr.model.permissions;

public class HumanTaskRole extends AbstractRole {

    private static final Long HUMAN_TASK_ACTOR_ID = -1337L;
    private static HumanTaskRole singleton = new HumanTaskRole();

    private HumanTaskRole() {
        super(HUMAN_TASK_ACTOR_ID);
    }
    
    public static Role getInstance(){
        return singleton;
    }
}
