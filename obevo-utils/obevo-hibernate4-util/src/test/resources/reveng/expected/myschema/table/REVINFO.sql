//// CHANGE name=change0
create table REVINFO (
        REV integer generated by default as identity,
        REVTSTMP bigint,
        primary key (REV)
    ) lock datarows
GO
