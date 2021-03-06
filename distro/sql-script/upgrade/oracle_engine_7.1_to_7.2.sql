-- add deployment.lock row to property table --
INSERT INTO ACT_GE_PROPERTY
  VALUES ('deployment.lock', '0', 1);

-- add revision column to incident table --
ALTER TABLE ACT_RU_INCIDENT
  ADD REV_ INTEGER;

-- set initial incident revision to 1 --
UPDATE
  ACT_RU_INCIDENT
SET
  REV_ = 1;

-- set incident revision column to not null --
ALTER TABLE ACT_RU_INCIDENT
  MODIFY (REV_ NOT NULL);

-- case management

ALTER TABLE ACT_RU_VARIABLE
  ADD CASE_EXECUTION_ID_ NVARCHAR2(64);

ALTER TABLE ACT_RU_VARIABLE
  ADD CASE_INST_ID_ NVARCHAR2(64);

ALTER TABLE ACT_RU_TASK
  ADD CASE_EXECUTION_ID_ NVARCHAR2(64);

ALTER TABLE ACT_RU_TASK
  ADD CASE_INST_ID_ NVARCHAR2(64);

ALTER TABLE ACT_RU_TASK
  ADD CASE_DEF_ID_ NVARCHAR2(64);

ALTER TABLE ACT_RU_EXECUTION
  ADD SUPER_CASE_EXEC_ NVARCHAR2(64);

ALTER TABLE ACT_RU_EXECUTION
  ADD CASE_INST_ID_ NVARCHAR2(64);

ALTER TABLE ACT_HI_OP_LOG
  ADD CASE_EXECUTION_ID_ NVARCHAR2(64);

ALTER TABLE ACT_HI_OP_LOG
  ADD CASE_INST_ID_ NVARCHAR2(64);

ALTER TABLE ACT_HI_OP_LOG
  ADD CASE_DEF_ID_ NVARCHAR2(64);

-- create case definition table --

create table ACT_RE_CASE_DEF (
    ID_ NVARCHAR2(64) NOT NULL,
    REV_ INTEGER,
    CATEGORY_ NVARCHAR2(255),
    NAME_ NVARCHAR2(255),
    KEY_ NVARCHAR2(255) NOT NULL,
    VERSION_ INTEGER NOT NULL,
    DEPLOYMENT_ID_ NVARCHAR2(64),
    RESOURCE_NAME_ NVARCHAR2(2000),
    primary key (ID_)
);

-- create case execution table --

create table ACT_RU_CASE_EXECUTION (
    ID_ NVARCHAR2(64) NOT NULL,
    REV_ INTEGER,
    CASE_INST_ID_ NVARCHAR2(64),
    SUPER_CASE_EXEC_ NVARCHAR2(64),
    BUSINESS_KEY_ NVARCHAR2(255),
    PARENT_ID_ NVARCHAR2(64),
    CASE_DEF_ID_ NVARCHAR2(64),
    ACT_ID_ NVARCHAR2(255),
    PREV_STATE_ INTEGER,
    CURRENT_STATE_ INTEGER,
    primary key (ID_)
);

-- create unique constraint on ACT_RE_CASE_DEF --
alter table ACT_RE_CASE_DEF
    add constraint ACT_UNIQ_CASE_DEF
    unique (KEY_,VERSION_);

-- create index on business key --
create index ACT_IDX_CASE_EXEC_BUSKEY on ACT_RU_CASE_EXECUTION(BUSINESS_KEY_);

-- create foreign key constraints on ACT_RU_CASE_EXECUTION --
create index ACT_IDX_CASE_EXE_CASE_INST on ACT_RU_CASE_EXECUTION(CASE_INST_ID_);
alter table ACT_RU_CASE_EXECUTION
    add constraint ACT_FK_CASE_EXE_CASE_INST
    foreign key (CASE_INST_ID_)
    references ACT_RU_CASE_EXECUTION(ID_);

create index ACT_IDX_CASE_EXE_PARENT on ACT_RU_CASE_EXECUTION(PARENT_ID_);
alter table ACT_RU_CASE_EXECUTION
    add constraint ACT_FK_CASE_EXE_PARENT
    foreign key (PARENT_ID_)
    references ACT_RU_CASE_EXECUTION(ID_);

create index ACT_IDX_CASE_EXE_CASE_DEF on ACT_RU_CASE_EXECUTION(CASE_DEF_ID_);
alter table ACT_RU_CASE_EXECUTION
    add constraint ACT_FK_CASE_EXE_CASE_DEF
    foreign key (CASE_DEF_ID_)
    references ACT_RE_CASE_DEF(ID_);

-- create foreign key constraints on ACT_RU_VARIABLE --
create index ACT_IDX_VAR_CASE_EXE on ACT_RU_VARIABLE(CASE_EXECUTION_ID_);
alter table ACT_RU_VARIABLE
    add constraint ACT_FK_VAR_CASE_EXE
    foreign key (CASE_EXECUTION_ID_)
    references ACT_RU_CASE_EXECUTION(ID_);

create index ACT_IDX_VAR_CASE_INST_ID on ACT_RU_VARIABLE(CASE_INST_ID_);
alter table ACT_RU_VARIABLE
    add constraint ACT_FK_VAR_CASE_INST
    foreign key (CASE_INST_ID_)
    references ACT_RU_CASE_EXECUTION(ID_);

-- create foreign key constraints on ACT_RU_TASK --
create index ACT_IDX_TASK_CASE_EXEC on ACT_RU_TASK(CASE_EXECUTION_ID_);
alter table ACT_RU_TASK
    add constraint ACT_FK_TASK_CASE_EXE
    foreign key (CASE_EXECUTION_ID_)
    references ACT_RU_CASE_EXECUTION(ID_);

create index ACT_IDX_TASK_CASE_DEF_ID on ACT_RU_TASK(CASE_DEF_ID_);
alter table ACT_RU_TASK
  add constraint ACT_FK_TASK_CASE_DEF
  foreign key (CASE_DEF_ID_)
  references ACT_RE_CASE_DEF(ID_);
