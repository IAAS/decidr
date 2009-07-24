SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `decidrdb`;

CREATE SCHEMA IF NOT EXISTS `decidrdb` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;
USE `decidrdb`;

-- -----------------------------------------------------
-- Table `decidrdb`.`user`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `authKey` CHAR(64) NULL COMMENT 'Used to authenticate unregistered users within the system.Should be set to null if the user is registered.' ,
  `email` VARCHAR(255) NULL COMMENT 'Can be used to log into the system. Each email address can be assigned to at most one user. The official max email address length is 320 characters, but MySQL unique indices require VARCHAR.' ,
  `disabledSince` DATETIME NULL COMMENT 'By setting disabledSince to something other than null, the super admin can ban a user from using the system.' ,
  `unavailableSince` DATETIME NULL COMMENT 'Whether or not the user is available for workflow participation. Null indicates that the user is available.' ,
  `registeredSince` DATETIME NULL COMMENT 'If this field is null the user has never successfully registered.' ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `unique_email` (`email` ASC) ,
  INDEX `index_authKey` (`authKey` ASC) ,
  INDEX `index_enabled` (`disabledSince` ASC) ,
  INDEX `index_available` (`unavailableSince` ASC) )
ENGINE = InnoDB
COMMENT = 'Stores user related data.';


-- -----------------------------------------------------
-- Table `decidrdb`.`user_profile`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`user_profile` (
  `userId` BIGINT NOT NULL ,
  `username` VARCHAR(20) NOT NULL ,
  `passwordHash` CHAR(128) NOT NULL COMMENT 'The system does not store the password in plain text, but uses a double hash and a salt to make attacks on the hashed password much harder. This field stores:\n\nSHA512( SHA512(password) XOR passwordSalt )' ,
  `passwordSalt` CHAR(128) NOT NULL COMMENT 'The salt in hexit representation which is XORed with the first password hash.' ,
  `firstName` VARCHAR(50) NULL ,
  `lastName` VARCHAR(50) NULL ,
  `street` VARCHAR(100) NULL ,
  `postalCode` VARCHAR(15) NULL ,
  `city` VARCHAR(50) NULL ,
  PRIMARY KEY (`userId`) ,
  INDEX `fk_profile_user` (`userId` ASC) ,
  UNIQUE INDEX `unique_username` (`username` ASC) ,
  INDEX `index_firstName` (`firstName` ASC) ,
  INDEX `index_lastName` (`lastName` ASC) ,
  INDEX `index_street` (`street` ASC) ,
  INDEX `index_postalCode` (`postalCode` ASC) ,
  INDEX `index_city` (`city` ASC) ,
  CONSTRAINT `fk_profile_user`
    FOREIGN KEY (`userId` )
    REFERENCES `decidrdb`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
COMMENT = 'Represents a user profile (weak entity)';


-- -----------------------------------------------------
-- Table `decidrdb`.`file`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`file` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `fileName` VARCHAR(255) NOT NULL ,
  `mimeType` VARCHAR(150) NOT NULL COMMENT 'The content typ of the file as reported by the original uploader.' ,
  `mayPublicRead` BOOLEAN NOT NULL COMMENT 'Whether or not this file is accessible by anyone' ,
  `fileSizeBytes` BIGINT NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`tenant`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`tenant` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(50) NOT NULL ,
  `description` LONGTEXT NOT NULL ,
  `logoId` BIGINT NULL COMMENT 'Points to the tenant logo file' ,
  `simpleColorSchemeId` BIGINT NULL ,
  `advancedColorSchemeId` BIGINT NULL ,
  `currentColorSchemeId` BIGINT NULL ,
  `approvedSince` DATETIME NULL COMMENT 'If this value is null, the tenant still needs approval by the super admin.' ,
  `adminId` BIGINT NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `unique_name` (`name` ASC) ,
  INDEX `fk_tenant_logo` (`simpleColorSchemeId` ASC) ,
  INDEX `fk_tenant_simple_color_scheme_css` (`logoId` ASC) ,
  INDEX `fk_tenant_advanced_color_scheme_css` (`advancedColorSchemeId` ASC) ,
  INDEX `fk_tenant_current_color_scheme_css` (`currentColorSchemeId` ASC) ,
  INDEX `index_approved_since` (`approvedSince` ASC) ,
  INDEX `fk_tenant_admin` (`adminId` ASC) ,
  CONSTRAINT `fk_tenant_logo`
    FOREIGN KEY (`simpleColorSchemeId` )
    REFERENCES `decidrdb`.`file` (`id` )
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_tenant_simple_color_scheme_css`
    FOREIGN KEY (`logoId` )
    REFERENCES `decidrdb`.`file` (`id` )
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_tenant_advanced_color_scheme_css`
    FOREIGN KEY (`advancedColorSchemeId` )
    REFERENCES `decidrdb`.`file` (`id` )
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_tenant_current_color_scheme_css`
    FOREIGN KEY (`currentColorSchemeId` )
    REFERENCES `decidrdb`.`file` (`id` )
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_tenant_admin`
    FOREIGN KEY (`adminId` )
    REFERENCES `decidrdb`.`user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`user_is_member_of_tenant`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`user_is_member_of_tenant` (
  `userId` BIGINT NOT NULL ,
  `tenantId` BIGINT NOT NULL ,
  PRIMARY KEY (`userId`, `tenantId`) ,
  INDEX `fk_user_memberof_tenant_user` (`userId` ASC) ,
  INDEX `fk_user_memberof_tenant_tenant` (`tenantId` ASC) ,
  CONSTRAINT `fk_user_memberof_tenant_user`
    FOREIGN KEY (`userId` )
    REFERENCES `decidrdb`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_user_memberof_tenant_tenant`
    FOREIGN KEY (`tenantId` )
    REFERENCES `decidrdb`.`tenant` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`workflow_model`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`workflow_model` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `tenantId` BIGINT NOT NULL ,
  `name` VARCHAR(255) NOT NULL ,
  `description` LONGTEXT NOT NULL ,
  `published` BOOLEAN NOT NULL DEFAULT false ,
  `executable` BOOLEAN NOT NULL ,
  `creationDate` DATETIME NOT NULL ,
  `modifiedDate` DATETIME NOT NULL ,
  `dwdl` LONGBLOB NOT NULL ,
  `version` BIGINT NOT NULL ,
  `modifiedByUserId` BIGINT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_workflowModel_tenant` (`tenantId` ASC) ,
  INDEX `index_name` (`name` ASC) ,
  INDEX `index_creationDate` (`creationDate` ASC) ,
  INDEX `index_modifiedDate` (`modifiedDate` ASC) ,
  INDEX `index_version` (`version` ASC) ,
  INDEX `fk_workflow_model_modifier` (`modifiedByUserId` ASC) ,
  CONSTRAINT `fk_workflowModel_tenant`
    FOREIGN KEY (`tenantId` )
    REFERENCES `decidrdb`.`tenant` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_workflow_model_modifier`
    FOREIGN KEY (`modifiedByUserId` )
    REFERENCES `decidrdb`.`user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`deployed_workflow_model`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`deployed_workflow_model` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `tenantId` BIGINT NOT NULL ,
  `originalWorkflowModelId` BIGINT NULL COMMENT 'If this field is null, the original workflow model has been deleted or is no longer available.' ,
  `name` VARCHAR(255) NOT NULL ,
  `description` LONGTEXT NOT NULL ,
  `dwdl` LONGBLOB NOT NULL ,
  `wsdl` LONGBLOB NOT NULL ,
  `soapTemplate` LONGBLOB NOT NULL ,
  `deployDate` DATETIME NOT NULL ,
  `version` BIGINT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_deployedWorkflowModel_workflowModel` (`originalWorkflowModelId` ASC) ,
  INDEX `index_deployDate` (`deployDate` ASC) ,
  INDEX `index_name` (`name` ASC) ,
  INDEX `fk_deployedWorkflowModel_tenant` (`tenantId` ASC) ,
  INDEX `index_version` (`version` ASC) ,
  CONSTRAINT `fk_deployedWorkflowModel_workflowModel`
    FOREIGN KEY (`originalWorkflowModelId` )
    REFERENCES `decidrdb`.`workflow_model` (`id` )
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_deployedWorkflowModel_tenant`
    FOREIGN KEY (`tenantId` )
    REFERENCES `decidrdb`.`tenant` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`server_type`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`server_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(100) NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `unique_name` (`name` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`server`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`server` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `location` VARCHAR(255) NOT NULL ,
  `load` TINYINT UNSIGNED NOT NULL COMMENT 'Ranges from 0 to 100 percent' ,
  `lastLoadUpdate` DATETIME NULL ,
  `locked` BOOLEAN NOT NULL COMMENT 'Whether or not new workflow models may be deployed on this server.' ,
  `dynamicallyAdded` BOOLEAN NOT NULL ,
  `serverTypeId` BIGINT NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `unique_location` (`location` ASC) ,
  INDEX `fk_server_server_type` (`serverTypeId` ASC) ,
  CONSTRAINT `fk_server_server_type`
    FOREIGN KEY (`serverTypeId` )
    REFERENCES `decidrdb`.`server_type` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`workflow_instance`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`workflow_instance` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `deployedWorkflowModelId` BIGINT NOT NULL ,
  `odePid` VARCHAR(255) NOT NULL COMMENT 'The ID given to the workflow instance by the Apache ODE (is this required?)' ,
  `startConfiguration` LONGBLOB NOT NULL ,
  `startedDate` DATETIME NULL COMMENT 'if null, the workflow instance is waiting to be started' ,
  `completedDate` DATETIME NULL COMMENT 'if not null, the workflow instance is no longer running' ,
  `serverId` BIGINT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_workflowInstance_deployedWorkflowModel` (`deployedWorkflowModelId` ASC) ,
  INDEX `index_odePid` (`odePid` ASC) ,
  INDEX `index_completedDate` (`completedDate` ASC) ,
  UNIQUE INDEX `unqiue_odePid_within_deployed_workflow_model` (`deployedWorkflowModelId` ASC, `odePid` ASC) ,
  INDEX `fk_workflow_instance_server` (`serverId` ASC) ,
  INDEX `index_startedDate` (`startedDate` ASC) ,
  CONSTRAINT `fk_workflowInstance_deployedWorkflowModel`
    FOREIGN KEY (`deployedWorkflowModelId` )
    REFERENCES `decidrdb`.`deployed_workflow_model` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_workflow_instance_server`
    FOREIGN KEY (`serverId` )
    REFERENCES `decidrdb`.`server` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`work_item`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`work_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `userId` BIGINT NOT NULL ,
  `creationDate` DATETIME NOT NULL ,
  `workflowInstanceId` BIGINT NOT NULL ,
  `status` ENUM('Fresh','InProgress','Done') NOT NULL ,
  `data` LONGBLOB NOT NULL ,
  `name` VARCHAR(255) NOT NULL ,
  `description` LONGTEXT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_workItem_user` (`userId` ASC) ,
  INDEX `fk_workItem_workflowInstance` (`workflowInstanceId` ASC) ,
  INDEX `index_creationDate` (`creationDate` ASC) ,
  INDEX `index_status` (`status` ASC) ,
  INDEX `index_name` (`name` ASC) ,
  CONSTRAINT `fk_workItem_user`
    FOREIGN KEY (`userId` )
    REFERENCES `decidrdb`.`user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_workItem_workflowInstance`
    FOREIGN KEY (`workflowInstanceId` )
    REFERENCES `decidrdb`.`workflow_instance` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`user_administrates_workflow_model`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`user_administrates_workflow_model` (
  `userId` BIGINT NOT NULL COMMENT 'The tenant admin always implicitly administrates all workflow models.' ,
  `workflowModelId` BIGINT NOT NULL ,
  PRIMARY KEY (`userId`, `workflowModelId`) ,
  INDEX `fk_user_has_workflowModel_user` (`userId` ASC) ,
  INDEX `fk_user_has_workflowModel_workflowModel` (`workflowModelId` ASC) ,
  CONSTRAINT `fk_user_has_workflowModel_user`
    FOREIGN KEY (`userId` )
    REFERENCES `decidrdb`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_user_has_workflowModel_workflowModel`
    FOREIGN KEY (`workflowModelId` )
    REFERENCES `decidrdb`.`workflow_model` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`user_participates_in_workflow`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`user_participates_in_workflow` (
  `userId` BIGINT NOT NULL ,
  `workflowInstanceId` BIGINT NOT NULL ,
  PRIMARY KEY (`userId`, `workflowInstanceId`) ,
  INDEX `fk_user_has_workflowInstance_user` (`userId` ASC) ,
  INDEX `fk_user_has_workflowInstance_workflowInstance` (`workflowInstanceId` ASC) ,
  CONSTRAINT `fk_user_has_workflowInstance_user`
    FOREIGN KEY (`userId` )
    REFERENCES `decidrdb`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_user_has_workflowInstance_workflowInstance`
    FOREIGN KEY (`workflowInstanceId` )
    REFERENCES `decidrdb`.`workflow_instance` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`change_email_request`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`change_email_request` (
  `userId` BIGINT NOT NULL ,
  `newEmail` VARCHAR(255) NOT NULL ,
  `creationDate` DATETIME NOT NULL COMMENT 'Request issue date and time.' ,
  `authKey` CHAR(64) NOT NULL COMMENT 'Used to authenticate the email address change within the application. The user must know this key in order to perform the email address change.' ,
  PRIMARY KEY (`userId`) ,
  INDEX `fk_changeEmailRequest_user` (`userId` ASC) ,
  CONSTRAINT `fk_changeEmailRequest_user`
    FOREIGN KEY (`userId` )
    REFERENCES `decidrdb`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`registration_request`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`registration_request` (
  `userId` BIGINT NOT NULL ,
  `creationDate` DATETIME NOT NULL ,
  `authKey` CHAR(64) NOT NULL COMMENT 'Used to authenticate the email address during registration. The user must know this key in order to complete his registration with the system.' ,
  PRIMARY KEY (`userId`) ,
  INDEX `fk_registrationRequest_user` (`userId` ASC) ,
  CONSTRAINT `fk_registrationRequest_user`
    FOREIGN KEY (`userId` )
    REFERENCES `decidrdb`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`invitation`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`invitation` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `senderId` BIGINT NOT NULL ,
  `receiverId` BIGINT NOT NULL ,
  `participateInWorkflowInstanceId` BIGINT NULL COMMENT 'If not set to null the receiver is invited to participate in the given workflow instance. Furthermore the user will join the tenant that owns the workflow instance.' ,
  `joinTenantId` BIGINT NULL COMMENT 'If not set to null the receiver is invited to join a tenant as a normal tenant member.' ,
  `administrateWorkflowModelId` BIGINT NULL COMMENT 'If not set to null the receiver is invited to administrate the given workflow model. Furthermore the user will join the tenant that owns the workflow model' ,
  `creationDate` DATETIME NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_invitation_sender` (`senderId` ASC) ,
  INDEX `fk_invitation_receiver` (`receiverId` ASC) ,
  INDEX `fk_invitation_workflowInstance` (`participateInWorkflowInstanceId` ASC) ,
  INDEX `fk_invitation_tenant` (`joinTenantId` ASC) ,
  INDEX `fk_invitation_workflowModel` (`administrateWorkflowModelId` ASC) ,
  CONSTRAINT `fk_invitation_sender`
    FOREIGN KEY (`senderId` )
    REFERENCES `decidrdb`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_invitation_receiver`
    FOREIGN KEY (`receiverId` )
    REFERENCES `decidrdb`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_invitation_workflowInstance`
    FOREIGN KEY (`participateInWorkflowInstanceId` )
    REFERENCES `decidrdb`.`workflow_instance` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_invitation_tenant`
    FOREIGN KEY (`joinTenantId` )
    REFERENCES `decidrdb`.`tenant` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_invitation_workflowModel`
    FOREIGN KEY (`administrateWorkflowModelId` )
    REFERENCES `decidrdb`.`workflow_model` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`session`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`session` (
  `sessionId` CHAR(64) NOT NULL ,
  `app` VARCHAR(255) NULL ,
  `data` LONGBLOB NULL ,
  `lastAccessed` BIGINT NOT NULL ,
  `maxInactive` INT NOT NULL ,
  `valid` BOOLEAN NOT NULL ,
  PRIMARY KEY (`sessionId`) ,
  INDEX `index_app` (`app` ASC) ,
  INDEX `index_lastAccessed` (`lastAccessed` ASC) ,
  INDEX `index_maxInactive` (`maxInactive` ASC) ,
  INDEX `index_valid` (`valid` ASC) )
ENGINE = InnoDB
COMMENT = 'Used to store the Tomcat HTTP session.';


-- -----------------------------------------------------
-- Table `decidrdb`.`user_has_file_access`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`user_has_file_access` (
  `fileId` BIGINT NOT NULL ,
  `userId` BIGINT NOT NULL ,
  `mayRead` BOOLEAN NOT NULL ,
  `mayDelete` BOOLEAN NOT NULL ,
  `mayReplace` BOOLEAN NOT NULL ,
  PRIMARY KEY (`userId`, `fileId`) ,
  INDEX `fk_user_access_file_user` (`userId` ASC) ,
  INDEX `fk_user_access_file_file` (`fileId` ASC) ,
  CONSTRAINT `fk_user_access_file_user`
    FOREIGN KEY (`userId` )
    REFERENCES `decidrdb`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_user_access_file_file`
    FOREIGN KEY (`fileId` )
    REFERENCES `decidrdb`.`file` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`role`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(255) NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `unique_role` (`name` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`permission`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`permission` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(255) NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `unique_resource` (`name` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`role_has_permission`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`role_has_permission` (
  `roleId` BIGINT NOT NULL ,
  `resourceId` BIGINT NOT NULL ,
  PRIMARY KEY (`roleId`, `resourceId`) ,
  INDEX `fk_role_has_resource_role` (`roleId` ASC) ,
  INDEX `fk_role_has_resource_resource` (`resourceId` ASC) ,
  CONSTRAINT `fk_role_has_resource_role`
    FOREIGN KEY (`roleId` )
    REFERENCES `decidrdb`.`role` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_role_has_resource_resource`
    FOREIGN KEY (`resourceId` )
    REFERENCES `decidrdb`.`permission` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`user_administrates_workflow_instance`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`user_administrates_workflow_instance` (
  `userId` BIGINT NOT NULL ,
  `workflowInstanceId` BIGINT NOT NULL ,
  PRIMARY KEY (`userId`, `workflowInstanceId`) ,
  INDEX `fk_user_admins_workflowInstance_user` (`userId` ASC) ,
  INDEX `fk_user_admins_workflowInstance_workflowInstance` (`workflowInstanceId` ASC) ,
  CONSTRAINT `fk_user_admins_workflowInstance_user`
    FOREIGN KEY (`userId` )
    REFERENCES `decidrdb`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_user_admins_workflowInstance_workflowInstance`
    FOREIGN KEY (`workflowInstanceId` )
    REFERENCES `decidrdb`.`workflow_instance` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`known_web_service`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`known_web_service` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `namespace` VARCHAR(255) NOT NULL COMMENT 'This is the unique namespace of the web service' ,
  `wsdl` LONGBLOB NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `unique_name` (`namespace` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`start_configuration`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`start_configuration` (
  `id` BIGINT NOT NULL ,
  `startConfiguration` LONGBLOB NOT NULL ,
  `deployedWorkflowModelId` BIGINT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_name_deployedWorkflowModel` (`deployedWorkflowModelId` ASC) ,
  CONSTRAINT `fk_name_deployedWorkflowModel`
    FOREIGN KEY (`deployedWorkflowModelId` )
    REFERENCES `decidrdb`.`deployed_workflow_model` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`system_settings`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`system_settings` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `modifiedDate` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `autoAcceptNewTenants` BOOLEAN NOT NULL DEFAULT FALSE ,
  `systemName` VARCHAR(255) NOT NULL DEFAULT 'DecidR' ,
  `domain` VARCHAR(255) NOT NULL DEFAULT 'decidr.de' ,
  `systemEmailAddress` VARCHAR(255) NOT NULL DEFAULT 'system@decidr.de' ,
  `logLevel` VARCHAR(30) NOT NULL ,
  `superAdminId` BIGINT NULL ,
  `passwordResetRequestLifetimeSeconds` INT NOT NULL DEFAULT 259200 COMMENT 'Default is 72 hours' ,
  `registrationRequestLifetimeSeconds` INT NOT NULL DEFAULT 259200 COMMENT 'Default is 72 hours' ,
  `changeEmailRequestLifetimeSeconds` INT NOT NULL DEFAULT 259200 COMMENT 'Default is 72 hours' ,
  `invitationLifetimeSeconds` INT NOT NULL DEFAULT 259200 COMMENT 'Default is 72 hours' ,
  `mtaHostname` VARCHAR(255) NOT NULL DEFAULT 'localhost' COMMENT 'Mail Transfer Agent hostname' ,
  `mtaPort` INT NOT NULL DEFAULT 25 COMMENT 'Server port of the Mail Transfer Agent' ,
  `mtaUseTls` BOOLEAN NOT NULL DEFAULT true COMMENT 'Whether or not TLS should be used when connecting to the MTA.' ,
  `mtaUsername` VARCHAR(255) NOT NULL DEFAULT 'decidr' ,
  `mtaPassword` VARCHAR(255) NOT NULL DEFAULT 'gn!le42' ,
  `maxUploadFileSizeBytes` BIGINT NOT NULL DEFAULT 10485760 COMMENT 'Default is 10 MB.' ,
  `maxAttachmentsPerEmail` INT NOT NULL DEFAULT 10 ,
  `monitorUpdateIntervalSeconds` INT NOT NULL DEFAULT 60 COMMENT 'the monitor clients will send server load data this often to the load collector service' ,
  `monitorAveragingPeriodSeconds` INT NOT NULL DEFAULT 300 COMMENT 'server load data is averaged over this time period when calculating the server load' ,
  `serverPoolInstances` INT NOT NULL DEFAULT 5 COMMENT 'number of server instances that should never be shut down automatically by the monitor service' ,
  `minServerLoadForLock` TINYINT NOT NULL DEFAULT 80 COMMENT 'If server load goes above this value, the server will be locked.' ,
  `maxServerLoadForUnlock` TINYINT NOT NULL DEFAULT 80 COMMENT 'if server load goes below this value, the server will be unlocked\n' ,
  `maxServerLoadForShutdown` TINYINT NOT NULL DEFAULT 20 COMMENT 'If server load goes below this value, the load monitor will consider shutting down the affected server.' ,
  `minUnlockedServers` INT NOT NULL DEFAULT 1 COMMENT 'Number of servers that should remain unlocked even if their load is above minServerLoadForLock' ,
  `minWorkflowInstancesForLock` INT NOT NULL DEFAULT 10 COMMENT 'If a server has more than minWorkflowInstancesForLock workflow instances it will be locked by the load monitor' ,
  `maxWorkflowInstancesForUnlock` INT NOT NULL DEFAULT 8 ,
  `maxWorkflowInstancesForShutdown` INT NOT NULL DEFAULT 1 COMMENT 'if a server has less than maxWorkflowInstancesForShutdown workflow instances, the load monitor will consider shutting it down.' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_system_settings_user` (`superAdminId` ASC) ,
  CONSTRAINT `fk_system_settings_user`
    FOREIGN KEY (`superAdminId` )
    REFERENCES `decidrdb`.`user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`workflow_model_is_deployed_on_server`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`workflow_model_is_deployed_on_server` (
  `deployedWorkflowModelId` BIGINT NOT NULL ,
  `serverId` BIGINT NOT NULL ,
  PRIMARY KEY (`deployedWorkflowModelId`, `serverId`) ,
  INDEX `fk_deployed_workflow_model_has_server_deployed_workflow_model` (`deployedWorkflowModelId` ASC) ,
  INDEX `fk_deployed_workflow_model_has_server_server` (`serverId` ASC) ,
  CONSTRAINT `fk_deployed_workflow_model_has_server_deployed_workflow_model`
    FOREIGN KEY (`deployedWorkflowModelId` )
    REFERENCES `decidrdb`.`deployed_workflow_model` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_deployed_workflow_model_has_server_server`
    FOREIGN KEY (`serverId` )
    REFERENCES `decidrdb`.`server` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`log`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `prio` VARCHAR(15) NOT NULL ,
  `iprio` BIGINT NULL ,
  `category` VARCHAR(255) NULL ,
  `msg` VARCHAR(255) NULL ,
  `layoutMsg` VARCHAR(255) NULL ,
  `throwable` VARCHAR(2000) NULL ,
  `ndc` VARCHAR(255) NULL ,
  `mdc` VARCHAR(255) NULL ,
  `mdc2` VARCHAR(255) NULL ,
  `info` VARCHAR(255) NULL ,
  `addon` VARCHAR(255) NULL ,
  `logDate` TIMESTAMP NULL ,
  `createdBy` VARCHAR(50) NULL ,
  `thread` VARCHAR(30) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `index_msg` (`msg` ASC) ,
  INDEX `index_category` (`category` ASC) ,
  INDEX `index_layoutMsg` (`layoutMsg` ASC) ,
  INDEX `index_thread` (`thread` ASC) ,
  INDEX `index_logDate` (`logDate` ASC) ,
  INDEX `index_info` (`info` ASC) ,
  INDEX `index_prio` (`prio` ASC) ,
  INDEX `index_iprio` (`iprio` ASC) )
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `decidrdb`.`password_reset_request`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`password_reset_request` (
  `userId` BIGINT NOT NULL ,
  `creationDate` DATETIME NOT NULL ,
  `authKey` CHAR(64) NOT NULL ,
  PRIMARY KEY (`userId`) ,
  INDEX `fk_password_reset_request_user` (`userId` ASC) ,
  CONSTRAINT `fk_password_reset_request_user`
    FOREIGN KEY (`userId` )
    REFERENCES `decidrdb`.`user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`login`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`login` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `userId` BIGINT NOT NULL ,
  `loginDate` DATETIME NOT NULL ,
  `success` BOOLEAN NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_login_user` (`userId` ASC) ,
  INDEX `index_loginDate` (`loginDate` ASC) ,
  CONSTRAINT `fk_login_user`
    FOREIGN KEY (`userId` )
    REFERENCES `decidrdb`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Placeholder table for view `decidrdb`.`server_load_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `decidrdb`.`server_load_view` (`id` INT);

-- -----------------------------------------------------
-- Placeholder table for view `decidrdb`.`work_item_summary_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `decidrdb`.`work_item_summary_view` (`id` INT);

-- -----------------------------------------------------
-- Placeholder table for view `decidrdb`.`tenant_summary_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `decidrdb`.`tenant_summary_view` (`id` INT);

-- -----------------------------------------------------
-- Placeholder table for view `decidrdb`.`tenant_with_admin_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `decidrdb`.`tenant_with_admin_view` (`id` INT);

-- -----------------------------------------------------
-- Placeholder table for view `decidrdb`.`startable_workflow_model_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `decidrdb`.`startable_workflow_model_view` (`id` INT);

-- -----------------------------------------------------
-- Placeholder table for view `decidrdb`.`invitation_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `decidrdb`.`invitation_view` (`id` INT);

-- -----------------------------------------------------
-- View `decidrdb`.`server_load_view`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `decidrdb`.`server_load_view`;
#Retrieves a server load summary including the number of deployed workflow instances
CREATE VIEW `decidrdb`.`server_load_view` AS
SELECT s.*, t.name AS serverType ,COUNT(wi.id) AS `numInstances`
FROM `server` AS s 
JOIN `server_type` AS t ON t.id = s.serverTypeId
LEFT JOIN `workflow_instance` AS wi ON wi.serverId = s.id
GROUP BY s.id;

-- -----------------------------------------------------
-- View `decidrdb`.`work_item_summary_view`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `decidrdb`.`work_item_summary_view`;
#Retrieves a work item summary 
CREATE VIEW `decidrdb`.`work_item_summary_view` AS
SELECT DISTINCT w.id AS id, 
                w.`name` AS workItemName, 
                t.`name` AS tenantName, 
                w.creationDate AS creationDate, 
                w.`status` AS workItemStatus,
                w.userId AS userId,
                w.workflowInstanceId
FROM `user` AS u,
     `work_item` AS w, 
     `tenant` AS t, 
     `workflow_instance` as wfi, 
     `deployed_workflow_model` as dwfm
WHERE (w.userId = u.id) AND 
      (w.workflowInstanceId = wfi.id) AND 
      (wfi.deployedWorkflowModelId = dwfm.id) AND 
      (dwfm.tenantId = t.id);

-- -----------------------------------------------------
-- View `decidrdb`.`tenant_summary_view`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `decidrdb`.`tenant_summary_view`;
#Retrieves a tenant summary including the number of (deployed) workflow models, worfklow instances and users.

CREATE VIEW `decidrdb`.`tenant_summary_view` AS
SELECT DISTINCT t.id AS id, t.`name` AS tenantName,
                a.`firstName` AS adminFirstName, 
                a.`lastName` AS adminLastName,
                COUNT(wfm.id) AS numWorkflowModels, 
                COUNT(dwfm.id) AS numDeployedWorkflowModels, 
                COUNT(wfi.id) AS numWorkflowInstances, 
                COUNT(u.id) + 1 AS numMembers
FROM `tenant` AS t,
     `user_profile` AS a, 
     `user` AS u, 
     `workflow_model` AS wfm, 
     `deployed_workflow_model` AS dwfm,
     `workflow_instance` AS wfi,
     `user_is_member_of_tenant` AS member
WHERE (t.adminId = a.userId) AND 
      (member.userId = u.id) AND (member.tenantId = t.id) AND
      (wfm.tenantId = t.id) AND
      (dwfm.tenantId = t.id) AND
      (wfi.deployedWorkflowModelId = dwfm.id)
GROUP BY t.id, t.`name`;

-- -----------------------------------------------------
-- View `decidrdb`.`tenant_with_admin_view`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `decidrdb`.`tenant_with_admin_view`;
CREATE  OR REPLACE VIEW `decidrdb`.`tenant_with_admin_view` AS
SELECT 
t.*, 
u.username AS adminUsername, 
u.firstName AS adminFirstName, 
u.lastName AS adminLastName 
FROM tenant AS t 
LEFT JOIN user_profile AS u ON t.adminId = u.userId;

-- -----------------------------------------------------
-- View `decidrdb`.`startable_workflow_model_view`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `decidrdb`.`startable_workflow_model_view`;
# Only workflow models where creating a new workflow instance is allowed, 
# i.e. a deployed workflow model exists that has the same version. 

CREATE VIEW `decidrdb`.`startable_workflow_model_view` AS
SELECT w.* FROM workflow_model AS w 
JOIN deployed_workflow_model AS d ON 
  ( (d.originalWorkflowModelId = w.id) AND (d.version = w.version) )
WHERE (w.executable = true);

-- -----------------------------------------------------
-- View `decidrdb`.`invitation_view`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `decidrdb`.`invitation_view`;
# A view on invitations that includes the sender's full name
CREATE VIEW `decidrdb`.`invitation_view` AS
SELECT i.*, snd.firstName AS senderFirstName, snd.lastName AS senderLastName,
       rcv.firstName AS receiverFirstName, rcv. lastName AS receiverLastName,
       wfm.name AS administratedWorkflowModelName,
       dwfm.name AS participateWorfkwlowModelName,
       t1.name AS joinTenantName,
       t2.name AS workflowModelOwningTenantName,
       t3.name AS participateTenantName
FROM invitation AS i
LEFT JOIN user_profile AS snd ON (snd.userId = i.senderId)
LEFT JOIN user_profile AS rcv ON (rcv.userId = i.receiverId)
LEFT JOIN tenant AS t1 ON (t1.id = i.joinTenantId)
LEFT JOIN workflow_model AS wfm ON (wfm.id = i.administrateWorkflowModelId)
LEFT JOIN tenant AS t2 ON (t2.id = wfm.tenantId)
LEFT JOIN workflow_instance AS wfi ON (wfi.id = i.participateInWorkflowInstanceId)
LEFT JOIN deployed_workflow_model AS dwfm ON (dwfm.id = wfi.deployedWorkflowModelId)
LEFT JOIN tenant AS t3 ON (t3.id = dwfm.tenantId);
USE `decidrdb`;

DELIMITER //
CREATE TRIGGER check_unique_email_update
BEFORE UPDATE ON `user`
FOR EACH ROW BEGIN
    IF ( EXISTS( SELECT * FROM `change_email_request` c WHERE NEW.`email` =  c.`newEmail`) ) THEN
        CALL ERROR_CANNOT_CREATE_USER_EMAIL_MAYBE_TAKEN();
    END IF;
END;

//



CREATE TRIGGER check_unique_email_insert
BEFORE INSERT ON `user`
FOR EACH ROW BEGIN
    IF ( EXISTS( SELECT * FROM `change_email_request` c WHERE NEW.`email` =  c.`newEmail`) ) THEN
        CALL ERROR_CANNOT_CREATE_USER_EMAIL_MAYBE_TAKEN();
    END IF;
END;


//


DELIMITER ;

DELIMITER //
CREATE TRIGGER `check_tenant_name_min_length_update`
BEFORE UPDATE ON `tenant`
FOR EACH ROW BEGIN
    IF (CHAR_LENGTH(NEW.name) < 2) THEN
        CALL ERROR_TENANT_NAME_TOO_SHORT();
    END IF;
END;

//



CREATE TRIGGER `check_tenant_name_min_length_insert`
BEFORE INSERT ON `tenant`
FOR EACH ROW BEGIN
    IF (CHAR_LENGTH(NEW.name) < 2) THEN
        CALL ERROR_TENANT_NAME_TOO_SHORT();
    END IF;
END;
//


DELIMITER ;

DELIMITER //
CREATE TRIGGER check_unique_email_change_request_update
BEFORE UPDATE ON change_email_request
FOR EACH ROW BEGIN
    IF ( EXISTS( SELECT * FROM `user` u WHERE NEW.`newEmail` =  u.`email`) ) THEN
        CALL ERROR_CANNOT_CHANGE_TO_EXISTING_EMAIL_ADDRESS();
    END IF;
END;


//





CREATE TRIGGER check_unique_email_change_request_insert
BEFORE INSERT ON change_email_request
FOR EACH ROW BEGIN
    IF ( EXISTS( SELECT * FROM `user` u WHERE NEW.`newEmail` =  u.`email`) ) THEN
        CALL ERROR_CANNOT_CHANGE_TO_EXISTING_EMAIL_ADDRESS();
    END IF;
END;


//


DELIMITER ;

DELIMITER //
CREATE TRIGGER `check_invitation_insert`
BEFORE INSERT ON `invitation`
FOR EACH ROW BEGIN
    SET @nullCols = 0;
    
    IF (NEW.participateInWorkflowInstanceId = null) THEN
        SET @nullCols = @nullCols + 1;
    END IF;
    
    IF (NEW.joinTenantId = null) THEN
        SET @nullCols = @nullCols + 1;
    END IF;
    
    IF (NEW.administrateWorkflowModelId = null) THEN
        SET @nullCols = @nullCols + 1;
    END IF;
    
    IF (@nullCols <> 2) THEN
        CALL ERROR_ONLY_ONE_KIND_OF_INVITATION_ALLOWED();
    END IF;    
END;


//





CREATE TRIGGER `check_invitation_update`
BEFORE UPDATE ON `invitation`
FOR EACH ROW BEGIN
    SET @nullCols = 0;
    
    IF (NEW.participateInWorkflowInstanceId = null) THEN
        SET @nullCols = @nullCols + 1;
    END IF;
    
    IF (NEW.joinTenantId = null) THEN
        SET @nullCols = @nullCols + 1;
    END IF;
    
    IF (NEW.administrateWorkflowModelId = null) THEN
        SET @nullCols = @nullCols + 1;
    END IF;
    
    IF (@nullCols <> 2) THEN
        CALL ERROR_ONLY_ONE_KIND_OF_INVITATION_ALLOWED();
    END IF;   
END;

//


DELIMITER ;

DELIMITER //
CREATE TRIGGER `check_deployed_workflow_model_insert`
BEFORE INSERT ON `deployed_workflow_model`
FOR EACH ROW BEGIN
    #Check 1: mimic a unique index over (originalWorkflowModelId, version), ignoring orphaned deployed workflow models.
    IF ((NEW.originalWorkflowModelId <> NULL) AND 
        (EXISTS(SELECT * FROM `deployed_workfow_model` AS dwfm
                WHERE (dwfm.originalWorkflowModelId = NEW.originalWorkflowModelId) 
                AND (dwfm.version = NEW.version)))) THEN
        CALL ERROR_DWFM_VERSION_DUPLICATE();
    END IF;
    
    #Check 2: a deployed workflow model may not have a higher version than its corresponding workflow model
    IF ((NEW.originalWorkflowModelId <> NULL) AND 
        (EXISTS (SELECT * FROM `workflow_model` AS wfm
                 WHERE (wfm.version < NEW.version)
                 AND (wfm.id = NEW.originalWorkflowModelId)))) THEN
        CALL ERROR_DWFM_VERSION_HIGHER_THAN_ORIGINAL();
    END IF;
END;

//



CREATE TRIGGER `check_deployed_workflow_model_update`
BEFORE UPDATE ON `deployed_workflow_model`
FOR EACH ROW BEGIN
    #Check 1: mimic a unique index over (originalWorkflowModelId, version), ignoring orphaned deployed workflow models.
    IF ((NEW.originalWorkflowModelId <> NULL) AND 
        (EXISTS(SELECT * FROM `deployed_workfow_model` AS dwfm
                WHERE (dwfm.originalWorkflowModelId = NEW.originalWorkflowModelId) 
                AND (dwfm.version = NEW.version)))) THEN
        CALL ERROR_DWFM_VERSION_DUPLICATE();
    END IF;
    
    #Check 2: a deployed workflow model may not have a higher version than its corresponding workflow model
    IF ((NEW.originalWorkflowModelId <> NULL) AND 
        (EXISTS (SELECT * FROM `workflow_model` AS wfm
                 WHERE (wfm.version < NEW.version)
                 AND (wfm.id = NEW.originalWorkflowModelId)))) THEN
        CALL ERROR_DWFM_VERSION_HIGHER_THAN_ORIGINAL();
    END IF;
END;//


DELIMITER ;

-- -----------------------------------------------------
-- Data for table `decidrdb`.`server_type`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `server_type` (`id`, `name`) VALUES (1, 'Ode');
INSERT INTO `server_type` (`id`, `name`) VALUES (2, 'WebPortal');
INSERT INTO `server_type` (`id`, `name`) VALUES (3, 'Esb');
INSERT INTO `server_type` (`id`, `name`) VALUES (4, 'Storage');

COMMIT;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
