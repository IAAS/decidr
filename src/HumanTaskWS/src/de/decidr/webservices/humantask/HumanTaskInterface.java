
/**
 * HumanTaskInterface.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */
    package de.decidr.webservices.humantask;
    /**
     *  HumanTaskInterface java skeleton interface for the axisService
     */
    public interface HumanTaskInterface {
     
         
        /**
         * Auto generated method signature
         * 
                                    * @param removeTask
         */

        
                public de.decidr.webservices.humantask.RemoveTaskResponse removeTask
                (
                  de.decidr.webservices.humantask.RemoveTask removeTask
                 )
            ;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param taskCompleted
         */

        
                public de.decidr.webservices.humantask.TaskCompletedResponse taskCompleted
                (
                  de.decidr.webservices.humantask.TaskCompleted taskCompleted
                 )
            ;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param createTask
         */

        
                public de.decidr.webservices.humantask.CreateTaskResponse createTask
                (
                  de.decidr.webservices.humantask.CreateTask createTask
                 )
            ;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param removeTasks
         */

        
                public de.decidr.webservices.humantask.RemoveTasksResponse removeTasks
                (
                  de.decidr.webservices.humantask.RemoveTasks removeTasks
                 )
            ;
        
         }
    