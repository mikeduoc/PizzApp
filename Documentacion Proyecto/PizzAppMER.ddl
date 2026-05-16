-- Generado por Oracle SQL Developer Data Modeler 24.3.1.351.0831
--   en:        2026-05-14 17:24:19 CLT
--   sitio:      Oracle Database 11g
--   tipo:      Oracle Database 11g



-- predefined type, no DDL - MDSYS.SDO_GEOMETRY

-- predefined type, no DDL - XMLTYPE

CREATE TABLE DETALLE_PEDIDO 
    ( 
     id_detalle  INTEGER  NOT NULL , 
     id_pedido   INTEGER  NOT NULL , 
     id_producto INTEGER  NOT NULL , 
     cantidad    INTEGER  NOT NULL , 
     total       NUMBER  NOT NULL 
    ) 
    LOGGING 
;

ALTER TABLE DETALLE_PEDIDO 
    ADD CONSTRAINT detalle_pedido_pk PRIMARY KEY ( id_detalle ) ;

CREATE TABLE INSUMO_PRODUCTO 
    ( 
     id_receta      INTEGER  NOT NULL , 
     id_producto    INTEGER  NOT NULL , 
     id_insumo      INTEGER  NOT NULL , 
     cantidad_usada INTEGER  NOT NULL 
    ) 
    LOGGING 
;

ALTER TABLE INSUMO_PRODUCTO 
    ADD CONSTRAINT receta_producto_pk PRIMARY KEY ( id_receta ) ;

CREATE TABLE INSUMOS 
    ( 
     id_insumo    INTEGER  NOT NULL , 
     nombre       VARCHAR2 (50 CHAR)  NOT NULL , 
     stock_actual INTEGER  NOT NULL , 
     unidad       INTEGER  NOT NULL 
    ) 
    LOGGING 
;

ALTER TABLE INSUMOS 
    ADD CONSTRAINT insumos_pk PRIMARY KEY ( id_insumo ) ;

CREATE TABLE PEDIDOS 
    ( 
     id_pedido    INTEGER  NOT NULL , 
     id_usuario   INTEGER  NOT NULL , 
     fecha        DATE  NOT NULL , 
     metodo_pago  VARCHAR2 (20 CHAR)  NOT NULL , 
     estado       VARCHAR2 (20 CHAR)  NOT NULL , 
     es_evento    CHAR (1)  NOT NULL , 
     total_pedido NUMBER  NOT NULL 
    ) 
    LOGGING 
;

ALTER TABLE PEDIDOS 
    ADD 
    CHECK (es_evento IN ('NO', 'SI')) 
;

ALTER TABLE PEDIDOS 
    ADD CONSTRAINT pedidos_pk PRIMARY KEY ( id_pedido ) ;

CREATE TABLE PRODUCTOS 
    ( 
     id_producto  INTEGER  NOT NULL , 
     nombre_pizza VARCHAR2 (50 CHAR)  NOT NULL , 
     precio_base  NUMBER  NOT NULL 
    ) 
    LOGGING 
;

ALTER TABLE PRODUCTOS 
    ADD CONSTRAINT productos_pk PRIMARY KEY ( id_producto ) ;

CREATE TABLE USUARIOS 
    ( 
     id_usuario     INTEGER  NOT NULL , 
     nombre_usuario VARCHAR2 (50 CHAR)  NOT NULL , 
     contrasena     VARCHAR2 (100 CHAR)  NOT NULL , 
     rol            VARCHAR2 (20 CHAR)  NOT NULL 
    ) 
    LOGGING 
;

ALTER TABLE USUARIOS 
    ADD CONSTRAINT usuarios_pk PRIMARY KEY ( id_usuario ) ;

ALTER TABLE DETALLE_PEDIDO 
    ADD CONSTRAINT det_pedido_fk FOREIGN KEY 
    ( 
     id_pedido
    ) 
    REFERENCES PEDIDOS 
    ( 
     id_pedido
    ) 
    NOT DEFERRABLE 
;

ALTER TABLE DETALLE_PEDIDO 
    ADD CONSTRAINT det_producto_fk FOREIGN KEY 
    ( 
     id_producto
    ) 
    REFERENCES PRODUCTOS 
    ( 
     id_producto
    ) 
    NOT DEFERRABLE 
;

ALTER TABLE PEDIDOS 
    ADD CONSTRAINT pedidos_usuarios_fk FOREIGN KEY 
    ( 
     id_usuario
    ) 
    REFERENCES USUARIOS 
    ( 
     id_usuario
    ) 
    NOT DEFERRABLE 
;

ALTER TABLE INSUMO_PRODUCTO 
    ADD CONSTRAINT rec_insumo_fk FOREIGN KEY 
    ( 
     id_insumo
    ) 
    REFERENCES INSUMOS 
    ( 
     id_insumo
    ) 
    NOT DEFERRABLE 
;

ALTER TABLE INSUMO_PRODUCTO 
    ADD CONSTRAINT rec_producto_fk FOREIGN KEY 
    ( 
     id_producto
    ) 
    REFERENCES PRODUCTOS 
    ( 
     id_producto
    ) 
    NOT DEFERRABLE 
;



-- Informe de Resumen de Oracle SQL Developer Data Modeler: 
-- 
-- CREATE TABLE                             6
-- CREATE INDEX                             0
-- ALTER TABLE                             12
-- CREATE VIEW                              0
-- ALTER VIEW                               0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- CREATE TRIGGER                           0
-- ALTER TRIGGER                            0
-- CREATE COLLECTION TYPE                   0
-- CREATE STRUCTURED TYPE                   0
-- CREATE STRUCTURED TYPE BODY              0
-- CREATE CLUSTER                           0
-- CREATE CONTEXT                           0
-- CREATE DATABASE                          0
-- CREATE DIMENSION                         0
-- CREATE DIRECTORY                         0
-- CREATE DISK GROUP                        0
-- CREATE ROLE                              0
-- CREATE ROLLBACK SEGMENT                  0
-- CREATE SEQUENCE                          0
-- CREATE MATERIALIZED VIEW                 0
-- CREATE MATERIALIZED VIEW LOG             0
-- CREATE SYNONYM                           0
-- CREATE TABLESPACE                        0
-- CREATE USER                              0
-- 
-- DROP TABLESPACE                          0
-- DROP DATABASE                            0
-- 
-- REDACTION POLICY                         0
-- 
-- ORDS DROP SCHEMA                         0
-- ORDS ENABLE SCHEMA                       0
-- ORDS ENABLE OBJECT                       0
-- 
-- ERRORS                                   0
-- WARNINGS                                 0
