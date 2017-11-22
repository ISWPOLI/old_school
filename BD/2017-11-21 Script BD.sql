CREATE DATABASE  IF NOT EXISTS `project_history` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `project_history`;

-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: project_history
-- ------------------------------------------------------
-- Server version	5.7.17-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `area`
--

DROP TABLE IF EXISTS `area`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `area` (
  `Id_Area` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre_Area` varchar(30) NOT NULL,
  PRIMARY KEY (`Id_Area`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cliente` (
  `Id_Cliente` int(11) NOT NULL AUTO_INCREMENT,
  `Razon_Social` varchar(60) NOT NULL,
  `Nit` int(15) DEFAULT NULL,
  `Direccion` varchar(100) DEFAULT NULL,
  `Telefono` int(11) DEFAULT NULL,
  `Email` varchar(20) DEFAULT NULL,
  `Activo` bit(2) NOT NULL,
  PRIMARY KEY (`Id_Cliente`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 COMMENT='Tabla donde se registran los clientes';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `documento`
--

DROP TABLE IF EXISTS `documento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `documento` (
  `id_documento` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_documento` varchar(50) DEFAULT NULL,
  `fecha_cargue` datetime DEFAULT NULL,
  `fecha_aprobacion` datetime DEFAULT NULL,
  `documento` longblob,
  `tamanio_documento` int(15) DEFAULT NULL,
  `tipo_archivo` varchar(10) DEFAULT NULL,
  `estado` char(1) DEFAULT NULL,
  `id_documento_asociado` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_documento`),
  KEY `id_documento_asociado_idx` (`id_documento_asociado`),
  CONSTRAINT `documento_asociado_fk` FOREIGN KEY (`id_documento_asociado`) REFERENCES `documento_asociado` (`Id_Documento_Asociado`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `documento_asociado`
--

DROP TABLE IF EXISTS `documento_asociado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `documento_asociado` (
  `Id_Documento_Asociado` int(11) NOT NULL AUTO_INCREMENT,
  `Id_Usuario` int(11) NOT NULL,
  `Id_Tipo_Documento` int(11) NOT NULL,
  `Id_Proyecto` int(11) NOT NULL,
  `fecha_modificacion` datetime DEFAULT NULL,
  PRIMARY KEY (`Id_Documento_Asociado`),
  KEY `Fk_documento_asociado_usuario` (`Id_Usuario`),
  KEY `Fk_documento_asociado_proyecto` (`Id_Proyecto`),
  KEY `Fk_documento_asociado_tipodocumento` (`Id_Tipo_Documento`),
  CONSTRAINT `Fk_documento_asociado_proyecto` FOREIGN KEY (`Id_Proyecto`) REFERENCES `proyecto` (`Id_Proyecto`),
  CONSTRAINT `Fk_documento_asociado_tipodocumento` FOREIGN KEY (`Id_Tipo_Documento`) REFERENCES `tipo_documento` (`Id_Tipo_Documento`),
  CONSTRAINT `Fk_documento_asociado_usuario` FOREIGN KEY (`Id_Usuario`) REFERENCES `usuario` (`Id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `proyecto`
--

DROP TABLE IF EXISTS `proyecto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `proyecto` (
  `Id_Proyecto` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre_Proyecto` varchar(40) NOT NULL,
  `descripcion` varchar(200) DEFAULT NULL,
  `Fecha_Creacion_Proyecto` datetime NOT NULL,
  `Id_Usuario` int(11) NOT NULL,
  `Id_Area` int(11) NOT NULL,
  `Id_Cliente` int(11) NOT NULL,
  `estado` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`Id_Proyecto`),
  KEY `FK_proyecto_usuario` (`Id_Usuario`),
  KEY `Fk_proyecto_area` (`Id_Area`),
  KEY `Fk_proyecto_cliente` (`Id_Cliente`),
  CONSTRAINT `FK_proyecto_usuario` FOREIGN KEY (`Id_Usuario`) REFERENCES `usuario` (`Id_usuario`),
  CONSTRAINT `Fk_proyecto_area` FOREIGN KEY (`Id_Area`) REFERENCES `area` (`Id_Area`),
  CONSTRAINT `Fk_proyecto_cliente` FOREIGN KEY (`Id_Cliente`) REFERENCES `cliente` (`Id_Cliente`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `reporteplantillas`
--

DROP TABLE IF EXISTS `reporteplantillas`;
/*!50001 DROP VIEW IF EXISTS `reporteplantillas`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `reporteplantillas` AS SELECT 
 1 AS `Id_Tipo_Documento`,
 1 AS `Nombre_Tipo_Documento`,
 1 AS `cantidad`,
 1 AS `Id_Proyecto`,
 1 AS `Nombre_Proyecto`,
 1 AS `Fecha_Creacion_Proyecto`,
 1 AS `Id_Cliente`,
 1 AS `Razon_Social`,
 1 AS `Nit`,
 1 AS `Id_Area`,
 1 AS `Nombre_Area`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rol`
--

DROP TABLE IF EXISTS `rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rol` (
  `Id_Rol` int(11) NOT NULL,
  `Nombre_Rol` varchar(30) NOT NULL,
  `Descripcion_Rol` varchar(50) DEFAULT NULL,
  `Activo` bit(2) NOT NULL,
  PRIMARY KEY (`Id_Rol`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Listado de roles';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rol_usuario`
--

DROP TABLE IF EXISTS `rol_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rol_usuario` (
  `Id_Usuario` int(11) NOT NULL,
  `Id_Rol` int(11) NOT NULL,
  KEY `FK_rol_usuario_Usuario` (`Id_Usuario`),
  KEY `FK_rol_usuario_rol` (`Id_Rol`),
  CONSTRAINT `FK_rol_usuario_Usuario` FOREIGN KEY (`Id_Usuario`) REFERENCES `usuario` (`Id_usuario`),
  CONSTRAINT `FK_rol_usuario_rol` FOREIGN KEY (`Id_Rol`) REFERENCES `rol` (`Id_Rol`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tipo_documento`
--

DROP TABLE IF EXISTS `tipo_documento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_documento` (
  `Id_Tipo_Documento` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre_Tipo_Documento` varchar(30) NOT NULL,
  `Plantilla` longblob,
  `tipo_archivo` varchar(10) DEFAULT NULL,
  `fecha_modificacion` datetime DEFAULT NULL,
  `tamanio_documento` int(15) DEFAULT NULL,
  PRIMARY KEY (`Id_Tipo_Documento`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario` (
  `Id_usuario` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre_Usuario` varchar(40) NOT NULL,
  `Apellido` varchar(40) NOT NULL,
  `Genero` varchar(15) NOT NULL,
  `Login` varchar(20) NOT NULL,
  `Contraseña` varchar(100) NOT NULL,
  `Fecha_Ultimo_Acceso` datetime DEFAULT NULL,
  `Activo` bit(3) NOT NULL,
  PRIMARY KEY (`Id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=latin1 COMMENT='Tabla donde se almacen todos los usuarios ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Final view structure for view `reporteplantillas`
--

/*!50001 DROP VIEW IF EXISTS `reporteplantillas`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `reporteplantillas` AS select `td`.`Id_Tipo_Documento` AS `Id_Tipo_Documento`,`td`.`Nombre_Tipo_Documento` AS `Nombre_Tipo_Documento`,count(`td`.`Id_Tipo_Documento`) AS `cantidad`,`p`.`Id_Proyecto` AS `Id_Proyecto`,`p`.`Nombre_Proyecto` AS `Nombre_Proyecto`,`p`.`Fecha_Creacion_Proyecto` AS `Fecha_Creacion_Proyecto`,`c`.`Id_Cliente` AS `Id_Cliente`,`c`.`Razon_Social` AS `Razon_Social`,`c`.`Nit` AS `Nit`,`a`.`Id_Area` AS `Id_Area`,`a`.`Nombre_Area` AS `Nombre_Area` from ((((`tipo_documento` `td` join `documento_asociado` `da` on((`td`.`Id_Tipo_Documento` = `da`.`Id_Tipo_Documento`))) join `proyecto` `p` on((`da`.`Id_Proyecto` = `p`.`Id_Proyecto`))) join `cliente` `c` on((`p`.`Id_Cliente` = `c`.`Id_Cliente`))) join `area` `a` on((`p`.`Id_Area` = `a`.`Id_Area`))) group by `td`.`Id_Tipo_Documento` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-11-18 23:25:13


/*DATA INICIAL*/
INSERT INTO `area` VALUES (1,'IBM WebSphere Portal'),(2,'IBM BPM'),(3,'Mega'),(4,'Dirección general');
INSERT INTO `rol` VALUES (1,'Administrador','Este perfil tiene permisos sobre todos los módulos',1),(2,'Consultor','Este perfil tiene permisos para consultar',1),(3,'Aprobador','Este perfil tiene permisos para aprobar',1);
INSERT INTO `usuario` VALUES (1,'Joel','Pulido G','M','joel','c0b84d1ccb798a5d7b82a2af42203428','2017-10-03 11:43:32',1);
INSERT INTO `rol_usuario` VALUES (1,3),(1,2),(1,1);