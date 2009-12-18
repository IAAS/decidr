SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `decidrdb`;

CREATE SCHEMA IF NOT EXISTS `decidrdb` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;
USE `decidrdb`;

-- -----------------------------------------------------
-- Table `decidrdb`.`file`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`file` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `fileName` VARCHAR(255) NOT NULL ,
  `mimeType` VARCHAR(150) NOT NULL COMMENT 'The content typ of the file as reported by the original uploader.' ,
  `mayPublicRead` TINYINT(1) NOT NULL DEFAULT false COMMENT 'Whether or not this file is accessible by anyone' ,
  `mayPublicReplace` TINYINT(1) NOT NULL DEFAULT false ,
  `mayPublicDelete` TINYINT(1) NOT NULL DEFAULT false ,
  `fileSizeBytes` BIGINT NOT NULL ,
  `data` LONGBLOB NULL COMMENT 'optional: the actual file data.' ,
  `creationDate` DATETIME NOT NULL COMMENT 'Date when this file entry was created.' ,
  `temporary` TINYINT(1) NOT NULL DEFAULT false COMMENT 'Temporary files are deleted after a certain time if they are not persisted.' ,
  PRIMARY KEY (`id`) ,
  INDEX `name_idx` (`fileName` ASC) ,
  INDEX `mimetype_idx` (`mimeType` ASC) ,
  INDEX `public_read_idx` (`mayPublicRead` ASC) ,
  INDEX `public_replace_idx` (`mayPublicReplace` ASC) ,
  INDEX `public_delete_idx` (`mayPublicDelete` ASC) ,
  INDEX `filesize_idx` (`fileSizeBytes` ASC) ,
  INDEX `creationdate_idx` (`creationDate` ASC) ,
  INDEX `temporary_idx` (`temporary` ASC) )
ENGINE = InnoDB
COMMENT = 'Represents a file that has been uploaded by a user.';


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
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`user`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `authKey` CHAR(64) NULL COMMENT 'Used to authenticate unregistered users within the system.Should be set to null if the user is registered.' ,
  `email` VARCHAR(255) NOT NULL COMMENT 'Can be used to log into the system. Each email address can be assigned to at most one user. The official max email address length is 320 characters, but MySQL unique indices require VARCHAR.' ,
  `disabledSince` DATETIME NULL COMMENT 'By setting disabledSince to something other than null, the super admin can ban a user from using the system.' ,
  `unavailableSince` DATETIME NULL COMMENT 'Whether or not the user is available for workflow participation. Null indicates that the user is available.' ,
  `registeredSince` DATETIME NULL COMMENT 'If this field is null the user has never successfully registered.' ,
  `creationDate` DATETIME NOT NULL COMMENT 'Date when the user was created' ,
  `currentTenantId` BIGINT NULL COMMENT 'null means that the user is currently in the default tenant' ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `unique_email` (`email` ASC) ,
  INDEX `index_authKey` (`authKey` ASC) ,
  INDEX `index_enabled` (`disabledSince` ASC) ,
  INDEX `index_available` (`unavailableSince` ASC) ,
  INDEX `fk_user_tenant1` (`currentTenantId` ASC) ,
  CONSTRAINT `fk_user_tenant1`
    FOREIGN KEY (`currentTenantId` )
    REFERENCES `decidrdb`.`tenant` (`id` )
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB
COMMENT = 'Stores user related data.';


-- -----------------------------------------------------
-- Table `decidrdb`.`user_profile`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`user_profile` (
  `id` BIGINT NOT NULL COMMENT 'pk must have the same name as referenced entity pk to work with hibernate.' ,
  `username` VARCHAR(20) NOT NULL ,
  `passwordHash` CHAR(128) NOT NULL COMMENT 'The system does not store the password in plain text, but uses an iterated hash and a salt to make attacks on the hashed password much harder.' ,
  `passwordSalt` CHAR(128) NOT NULL COMMENT 'The salt in hexit representation.' ,
  `firstName` VARCHAR(50) NULL ,
  `lastName` VARCHAR(50) NULL ,
  `street` VARCHAR(100) NULL ,
  `postalCode` VARCHAR(15) NULL ,
  `city` VARCHAR(50) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_profile_user` (`id` ASC) ,
  UNIQUE INDEX `unique_username` (`username` ASC) ,
  INDEX `index_firstName` (`firstName` ASC) ,
  INDEX `index_lastName` (`lastName` ASC) ,
  INDEX `index_street` (`street` ASC) ,
  INDEX `index_postalCode` (`postalCode` ASC) ,
  INDEX `index_city` (`city` ASC) ,
  CONSTRAINT `fk_profile_user`
    FOREIGN KEY (`id` )
    REFERENCES `decidrdb`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
COMMENT = 'Represents a user profile (weak entity)';


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
  `published` TINYINT(1) NOT NULL DEFAULT false ,
  `executable` TINYINT(1) NOT NULL ,
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
    ON DELETE SET NULL
    ON UPDATE CASCADE)
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
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`server_type`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`server_type` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(100) NOT NULL COMMENT 'unique web service identifier such as \"EmailProxy\"' ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `unique_name` (`name` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`server`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`server` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `location` VARCHAR(255) NOT NULL COMMENT 'ip and port of the server' ,
  `load` TINYINT NOT NULL COMMENT 'Ranges from 0 to 100 percent' ,
  `locked` TINYINT(1) NOT NULL COMMENT 'Whether or not new workflow models may be deployed on this server.' ,
  `dynamicallyAdded` TINYINT(1) NOT NULL ,
  `serverTypeId` BIGINT NOT NULL ,
  `lastLoadUpdate` DATETIME NULL ,
  PRIMARY KEY (`id`) ,
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
    ON DELETE CASCADE
    ON UPDATE CASCADE)
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
    ON DELETE CASCADE
    ON UPDATE CASCADE,
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
  `id` BIGINT NOT NULL ,
  `newEmail` VARCHAR(255) NOT NULL ,
  `creationDate` DATETIME NOT NULL COMMENT 'Request issue date and time.' ,
  `authKey` CHAR(64) NOT NULL COMMENT 'Used to authenticate the email address change within the application. The user must know this key in order to perform the email address change.' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_changeEmailRequest_user` (`id` ASC) ,
  CONSTRAINT `fk_changeEmailRequest_user`
    FOREIGN KEY (`id` )
    REFERENCES `decidrdb`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`registration_request`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`registration_request` (
  `id` BIGINT NOT NULL ,
  `creationDate` DATETIME NOT NULL ,
  `authKey` CHAR(64) NOT NULL COMMENT 'Used to authenticate the email address during registration. The user must know this key in order to complete his registration with the system.' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_registrationRequest_user` (`id` ASC) ,
  CONSTRAINT `fk_registrationRequest_user`
    FOREIGN KEY (`id` )
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
  `valid` TINYINT(1) NOT NULL ,
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
  `mayRead` TINYINT(1) NOT NULL ,
  `mayDelete` TINYINT(1) NOT NULL ,
  `mayReplace` TINYINT(1) NOT NULL ,
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
  `permissionId` BIGINT NOT NULL ,
  PRIMARY KEY (`roleId`, `permissionId`) ,
  INDEX `fk_role_has_resource_role` (`roleId` ASC) ,
  INDEX `fk_role_has_resource_resource` (`permissionId` ASC) ,
  CONSTRAINT `fk_role_has_resource_role`
    FOREIGN KEY (`roleId` )
    REFERENCES `decidrdb`.`role` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_role_has_resource_resource`
    FOREIGN KEY (`permissionId` )
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
  `name` VARCHAR(255) NOT NULL COMMENT 'This is the unique namespace of the web service' ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `unique_name` (`name` ASC) )
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
  `autoAcceptNewTenants` TINYINT(1) NOT NULL DEFAULT FALSE ,
  `systemName` VARCHAR(255) NOT NULL DEFAULT 'DecidR' ,
  `baseUrl` VARCHAR(255) NOT NULL DEFAULT 'decidr.de' COMMENT 'the base url where the application can be reached (no protocol) e.g. decidr.de/WebPortal' ,
  `systemEmailAddress` VARCHAR(255) NOT NULL DEFAULT 'system@decidr.de' ,
  `logLevel` VARCHAR(30) NOT NULL ,
  `superAdminId` BIGINT NOT NULL ,
  `passwordResetRequestLifetimeSeconds` INT UNSIGNED NOT NULL DEFAULT 259200 COMMENT 'Default is 72 hours' ,
  `registrationRequestLifetimeSeconds` INT UNSIGNED NOT NULL DEFAULT 259200 COMMENT 'Default is 72 hours' ,
  `changeEmailRequestLifetimeSeconds` INT UNSIGNED NOT NULL DEFAULT 259200 COMMENT 'Default is 72 hours' ,
  `invitationLifetimeSeconds` INT UNSIGNED NOT NULL DEFAULT 259200 COMMENT 'Default is 72 hours' ,
  `mtaHostname` VARCHAR(255) NOT NULL DEFAULT 'localhost' COMMENT 'Mail Transfer Agent hostname' ,
  `mtaPort` INT UNSIGNED NOT NULL DEFAULT 25 COMMENT 'Server port of the Mail Transfer Agent. 0 means default port.' ,
  `mtaUseTls` TINYINT(1) NOT NULL DEFAULT true COMMENT 'Whether or not TLS should be used when connecting to the MTA.' ,
  `mtaUsername` VARCHAR(255) NOT NULL DEFAULT 'decidr' ,
  `mtaPassword` VARCHAR(255) NOT NULL DEFAULT 'gn!le42' ,
  `maxUploadFileSizeBytes` BIGINT UNSIGNED NOT NULL DEFAULT 10485760 COMMENT 'Default is 10 MB.' ,
  `maxAttachmentsPerEmail` INT UNSIGNED NOT NULL DEFAULT 10 ,
  `monitorUpdateIntervalSeconds` INT UNSIGNED NOT NULL DEFAULT 60 COMMENT 'the monitor clients will send server load data this often to the load collector service' ,
  `monitorAveragingPeriodSeconds` INT UNSIGNED NOT NULL DEFAULT 300 COMMENT 'server load data is averaged over this time period when calculating the server load' ,
  `serverPoolInstances` INT UNSIGNED NOT NULL DEFAULT 5 COMMENT 'number of server instances that should never be shut down automatically by the monitor service' ,
  `minServerLoadForLock` TINYINT UNSIGNED NOT NULL DEFAULT 80 COMMENT 'If server load goes above this value, the server will be locked.' ,
  `maxServerLoadForUnlock` TINYINT UNSIGNED NOT NULL DEFAULT 80 COMMENT 'if server load goes below this value, the server will be unlocked\n' ,
  `maxServerLoadForShutdown` TINYINT UNSIGNED NOT NULL DEFAULT 20 COMMENT 'If server load goes below this value, the load monitor will consider shutting down the affected server.' ,
  `minUnlockedServers` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT 'Number of servers that should remain unlocked even if their load is above minServerLoadForLock' ,
  `minWorkflowInstancesForLock` INT UNSIGNED NOT NULL DEFAULT 10 COMMENT 'If a server has more than minWorkflowInstancesForLock workflow instances it will be locked by the load monitor' ,
  `maxWorkflowInstancesForUnlock` INT UNSIGNED NOT NULL DEFAULT 8 COMMENT 'If a server has less than maxWorkflowInstancesForLock workflow instances it will be unlocked by the load monitor' ,
  `maxWorkflowInstancesForShutdown` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT 'if a server has less than maxWorkflowInstancesForShutdown workflow instances, the load monitor will consider shutting it down.' ,
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
    ON DELETE CASCADE
    ON UPDATE CASCADE,
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
  `id` BIGINT NOT NULL ,
  `creationDate` DATETIME NOT NULL ,
  `authKey` CHAR(64) NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_password_reset_request_user` (`id` ASC) ,
  CONSTRAINT `fk_password_reset_request_user`
    FOREIGN KEY (`id` )
    REFERENCES `decidrdb`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`login`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`login` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `userId` BIGINT NOT NULL ,
  `loginDate` DATETIME NOT NULL ,
  `success` TINYINT(1) NOT NULL ,
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
-- Table `decidrdb`.`activity`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`activity` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `mapping` LONGBLOB NOT NULL ,
  `name` VARCHAR(255) NOT NULL ,
  `known_web_service_id` BIGINT NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `unique_name` (`name` ASC) ,
  INDEX `fk_activity_known_web_service` (`known_web_service_id` ASC) ,
  CONSTRAINT `fk_activity_known_web_service`
    FOREIGN KEY (`known_web_service_id` )
    REFERENCES `decidrdb`.`known_web_service` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `decidrdb`.`work_item_contains_file`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `decidrdb`.`work_item_contains_file` (
  `workItemId` BIGINT NOT NULL ,
  `fileId` BIGINT NOT NULL ,
  PRIMARY KEY (`workItemId`, `fileId`) ,
  INDEX `fk_work_item_has_file_work_item1` (`workItemId` ASC) ,
  INDEX `fk_work_item_has_file_file1` (`fileId` ASC) ,
  CONSTRAINT `fk_work_item_has_file_work_item1`
    FOREIGN KEY (`workItemId` )
    REFERENCES `decidrdb`.`work_item` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_work_item_has_file_file1`
    FOREIGN KEY (`fileId` )
    REFERENCES `decidrdb`.`file` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Placeholder table for view `decidrdb`.`invitation_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `decidrdb`.`invitation_view` (`id` INT, `senderId` INT, `receiverId` INT, `participateInWorkflowInstanceId` INT, `joinTenantId` INT, `administrateWorkflowModelId` INT, `creationDate` INT, `senderFirstName` INT, `senderLastName` INT, `receiverFirstName` INT, `receiverLastName` INT, `administratedWorkflowModelName` INT, `participateWorfkwlowModelName` INT, `joinTenantName` INT, `workflowModelOwningTenantName` INT, `participateTenantName` INT);

-- -----------------------------------------------------
-- Placeholder table for view `decidrdb`.`server_load_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `decidrdb`.`server_load_view` (`id` INT, `location` INT, `load` INT, `locked` INT, `dynamicallyAdded` INT, `serverTypeId` INT, `lastLoadUpdate` INT, `serverType` INT, `numInstances` INT);

-- -----------------------------------------------------
-- Placeholder table for view `decidrdb`.`tenant_summary_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `decidrdb`.`tenant_summary_view` (`id` INT, `tenantName` INT, `approvedSince` INT, `adminFirstName` INT, `adminLastName` INT, `numWorkflowModels` INT, `numDeployedWorkflowModels` INT, `numWorkflowInstances` INT, `numMembers` INT);

-- -----------------------------------------------------
-- Placeholder table for view `decidrdb`.`tenant_with_admin_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `decidrdb`.`tenant_with_admin_view` (`id` INT, `name` INT, `description` INT, `logoId` INT, `simpleColorSchemeId` INT, `advancedColorSchemeId` INT, `currentColorSchemeId` INT, `approvedSince` INT, `adminId` INT, `adminUsername` INT, `adminFirstName` INT, `adminLastName` INT);

-- -----------------------------------------------------
-- Placeholder table for view `decidrdb`.`work_item_summary_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `decidrdb`.`work_item_summary_view` (`id` INT, `workItemName` INT, `tenantName` INT, `creationDate` INT, `workItemStatus` INT, `userId` INT, `workflowInstanceId` INT);

-- -----------------------------------------------------
-- Placeholder table for view `decidrdb`.`startable_workflow_model_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `decidrdb`.`startable_workflow_model_view` (`id` INT, `tenantId` INT, `name` INT, `description` INT, `published` INT, `executable` INT, `creationDate` INT, `modifiedDate` INT, `dwdl` INT, `version` INT, `modifiedByUserId` INT);

-- -----------------------------------------------------
-- View `decidrdb`.`invitation_view`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `decidrdb`.`invitation_view`;
# A view on invitations that includes the sender's full name
CREATE  OR REPLACE VIEW `decidrdb`.`invitation_view` AS
SELECT i.*, snd.firstName AS senderFirstName, snd.lastName AS senderLastName,
       rcv.firstName AS receiverFirstName, rcv. lastName AS receiverLastName,
       wfm.name AS administratedWorkflowModelName,
       dwfm.name AS participateWorfkwlowModelName,
       t1.name AS joinTenantName,
       t2.name AS workflowModelOwningTenantName,
       t3.name AS participateTenantName
FROM invitation AS i
LEFT JOIN user_profile AS snd ON (snd.id = i.senderId)
LEFT JOIN user_profile AS rcv ON (rcv.id = i.receiverId)
LEFT JOIN tenant AS t1 ON (t1.id = i.joinTenantId)
LEFT JOIN workflow_model AS wfm ON (wfm.id = i.administrateWorkflowModelId)
LEFT JOIN tenant AS t2 ON (t2.id = wfm.tenantId)
LEFT JOIN workflow_instance AS wfi ON (wfi.id = i.participateInWorkflowInstanceId)
LEFT JOIN deployed_workflow_model AS dwfm ON (dwfm.id = wfi.deployedWorkflowModelId)
LEFT JOIN tenant AS t3 ON (t3.id = dwfm.tenantId);

-- -----------------------------------------------------
-- View `decidrdb`.`server_load_view`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `decidrdb`.`server_load_view`;
#Retrieves a server load summary including the number of deployed workflow instances
CREATE  OR REPLACE VIEW `decidrdb`.`server_load_view` AS
SELECT s.*, t.name AS serverType ,COUNT(wi.id) AS `numInstances`
FROM `server` AS s 
JOIN `server_type` AS t ON t.id = s.serverTypeId
LEFT JOIN `workflow_instance` AS wi ON wi.serverId = s.id
GROUP BY s.id;

-- -----------------------------------------------------
-- View `decidrdb`.`tenant_summary_view`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `decidrdb`.`tenant_summary_view`;
#Retrieves a tenant summary including the number of (deployed) workflow models, worfklow instances and users.

CREATE  OR REPLACE VIEW `decidrdb`.`tenant_summary_view` AS
SELECT t.id AS id, t.`name` AS tenantName,
                t.approvedSince AS approvedSince,
                a.`firstName` AS adminFirstName, 
                a.`lastName` AS adminLastName,
                (
                    SELECT COUNT(wfm.id) 
                    FROM  `workflow_model` AS wfm 
                    WHERE  (wfm.tenantId = t.id) 
                ) AS numWorkflowModels, 
                (
                    SELECT COUNT(dwfm.id)
                    FROM deployed_workflow_model AS dwfm 
                    WHERE (dwfm.tenantId = t.id)
                ) AS numDeployedWorkflowModels, 
                (
                    SELECT COUNT(wfi.id)
                    FROM workflow_instance AS wfi,
                              deployed_workflow_model AS dwfm
                    WHERE (wfi.deployedWorkflowModelId = dwfm.id)
                    AND (dwfm.tenantId = t.id)
                ) AS numWorkflowInstances , 
                (
                    SELECT COUNT(*) + 1 FROM 
                    user_is_member_of_tenant AS member 
                    WHERE (member.tenantId = t.id)
                ) AS numMembers
FROM tenant AS t
LEFT JOIN `user_profile` AS a ON (t.adminId = a.id);

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
LEFT JOIN user_profile AS u ON t.adminId = u.id;

-- -----------------------------------------------------
-- View `decidrdb`.`work_item_summary_view`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `decidrdb`.`work_item_summary_view`;
#Retrieves a work item summary 
CREATE  OR REPLACE VIEW `decidrdb`.`work_item_summary_view` AS
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
-- View `decidrdb`.`startable_workflow_model_view`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `decidrdb`.`startable_workflow_model_view`;
# Only workflow models where creating a new workflow instance is allowed, 
# i.e. a deployed workflow model exists that has the same version. 

CREATE  OR REPLACE VIEW `decidrdb`.`startable_workflow_model_view` AS
SELECT w.* FROM workflow_model AS w 
JOIN deployed_workflow_model AS d ON 
  ( (d.originalWorkflowModelId = w.id) AND (d.version = w.version) )
WHERE (w.executable = true);
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
    
    IF (NEW.participateInWorkflowInstanceId is null) THEN
        SET @nullCols = @nullCols + 1;
    END IF;
    
    IF (NEW.joinTenantId is null) THEN
        SET @nullCols = @nullCols + 1;
    END IF;
    
    IF (NEW.administrateWorkflowModelId is null) THEN
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




CREATE TRIGGER `check_deployed_workflow_model_update`
BEFORE UPDATE ON `deployed_workflow_model`
FOR EACH ROW BEGIN
    #Check 1: mimic a unique index over (originalWorkflowModelId, version), ignoring orphaned deployed workflow models.
    IF ((NEW.originalWorkflowModelId IS NOT NULL) AND 
        (EXISTS(SELECT * FROM `deployed_workflow_model` AS dwfm
                WHERE (dwfm.originalWorkflowModelId = NEW.originalWorkflowModelId) 
                AND (dwfm.version = NEW.version)
                AND (dwfm.id <> NEW.id)))) THEN
        CALL ERROR_DWFM_VERSION_DUPLICATE();
    END IF;
    
    #Check 2: a deployed workflow model may not have a higher version than its corresponding workflow model
    IF ((NEW.originalWorkflowModelId IS NOT NULL) AND 
        (EXISTS (SELECT * FROM `workflow_model` AS wfm
                 WHERE (wfm.version < NEW.version)
                 AND (wfm.id = NEW.originalWorkflowModelId)))) THEN
        CALL ERROR_DWFM_VERSION_HIGHER_THAN_ORIGINAL();
    END IF;
END;//

CREATE TRIGGER `check_deployed_workflow_model_insert`
BEFORE INSERT ON `deployed_workflow_model`
FOR EACH ROW BEGIN
    #Check 1: mimic a unique index over (originalWorkflowModelId, version), ignoring orphaned deployed workflow models.
    IF ((NEW.originalWorkflowModelId IS NOT NULL) AND 
        (EXISTS(SELECT * FROM `deployed_workflow_model` AS dwfm
                WHERE (dwfm.originalWorkflowModelId = NEW.originalWorkflowModelId) 
                AND (dwfm.version = NEW.version)
                AND (dwfm.id <> NEW.id)))) THEN
        CALL ERROR_DWFM_VERSION_DUPLICATE();
    END IF;
    
    #Check 2: a deployed workflow model may not have a higher version than its corresponding workflow model
    IF ((NEW.originalWorkflowModelId IS NOT NULL) AND 
        (EXISTS (SELECT * FROM `workflow_model` AS wfm
                 WHERE (wfm.version < NEW.version)
                 AND (wfm.id = NEW.originalWorkflowModelId)))) THEN
        CALL ERROR_DWFM_VERSION_HIGHER_THAN_ORIGINAL();
    END IF;
END;

//


DELIMITER ;

DELIMITER //
 
CREATE TRIGGER check_system_settings_update
BEFORE UPDATE ON system_settings
FOR EACH ROW BEGIN
    #checks constraints 
    IF ( "" = TRIM(NEW.baseUrl)  ) THEN
        CALL ERROR_EMPTY_BASE_URL();
    END IF;
END;//


DELIMITER ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
