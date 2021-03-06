CREATE OR REPLACE PACKAGE PKG_CYCLE1
AS
    FUNCTION PKG_CYCLE1_FUNCA return integer;
END;

//// BODY
CREATE OR REPLACE PACKAGE BODY PKG_CYCLE1
AS
    FUNCTION PKG_CYCLE1_FUNCA
    RETURN integer IS
    mytest INT;
    BEGIN
        mytest := PKG_CYCLE2.PKG_CYCLE2_FUNCA();
        RETURN 1;
    END;
END;


ALTER PACKAGE PKG_CYCLE1 COMPILE;
ALTER PACKAGE PKG_CYCLE2 COMPILE;
ALTER PACKAGE PKG_CYCLE1 COMPILE BODY;
ALTER PACKAGE PKG_CYCLE2 COMPILE BODY;

select PKG_CYCLE2.PKG_CYCLE2_FUNCA() from dual;
