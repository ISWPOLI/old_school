-- phpMyAdmin SQL Dump
-- version 4.7.0
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 02-09-2017 a las 00:06:43
-- Versión del servidor: 10.1.25-MariaDB
-- Versión de PHP: 7.1.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `project_history`
--

-- -----------------------------------------------------
-- Schema project_history
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `project_history` DEFAULT CHARACTER SET latin1 ;
SHOW WARNINGS;
USE `project_history` ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `area`
--

CREATE TABLE `area` (
  `Id_Area` int(11) NOT NULL,
  `Nombre_Area` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

CREATE TABLE `cliente` (
  `Id_Cliente` int(11) NOT NULL,
  `Razon_Social` varchar(60) NOT NULL,
  `Nit` int(15),
  `Direccion` varchar(100),
  `Telefono` int(11),
  `Email` varchar(20),
  `Activo` bit(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Tabla donde se registran los clientes';

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `documento_asociado`
--

CREATE TABLE `documento_asociado` (
  `Id_Documento_Asociado` int(11) NOT NULL,
  `Nombre_Documento` varchar(50) NOT NULL,
  `Fecha_Cargue_Documento` datetime NOT NULL,
  `Id_Usuario` int(11) NOT NULL,
  `Id_Tipo_Documento` int(11) NOT NULL,
  `Id_Proyecto` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `proyecto`
--

CREATE TABLE `proyecto` (
  `Id_Proyecto` int(11) NOT NULL,
  `Nombre_Proyecto` varchar(40) NOT NULL,
  `Fecha_Creacion_Proyecto` datetime NOT NULL,
  `Id_Usuario` int(11) NOT NULL,
  `Id_Area` int(11) NOT NULL,
  `Id_Cliente` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rol`
--

CREATE TABLE `rol` (
  `Id_Rol` int(11) NOT NULL,
  `Nombre_Rol` varchar(30) NOT NULL,
  `Descripcion_Rol` varchar(50),
  `Activo` bit(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Listado de roles';

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rol_usuario`
--

CREATE TABLE `rol_usuario` (
  `Id_Usuario` int(11) NOT NULL,
  `Id_Rol` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo_documento`
--

CREATE TABLE `tipo_documento` (
  `Id_Tipo_Documento` int(11) NOT NULL,
  `Nombre_Tipo_Documento` varchar(30) NOT NULL,
  `Plantilla` blob
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `Id_usuario` int(11) NOT NULL,
  `Nombre_Usuario` varchar(40) NOT NULL,
  `Apellido` varchar(40) NOT NULL,
  `Genero` varchar(15) NOT NULL,
  `Login` varchar(20) NOT NULL,
  `Contraseña` varchar(100) NOT NULL,
  `Fecha_Ultimo_Acceso` datetime,
  `Activo` bit(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Tabla donde se almacen todos los usuarios ';

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `area`
--
ALTER TABLE `area`
  ADD PRIMARY KEY (`Id_Area`);

--
-- Indices de la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD PRIMARY KEY (`Id_Cliente`);

--
-- Indices de la tabla `documento_asociado`
--
ALTER TABLE `documento_asociado`
  ADD PRIMARY KEY (`Id_Documento_Asociado`),
  ADD KEY `Fk_documento_asociado_usuario` (`Id_Usuario`),
  ADD KEY `Fk_documento_asociado_proyecto` (`Id_Proyecto`),
  ADD KEY `Fk_documento_asociado_tipodocumento` (`Id_Tipo_Documento`);

--
-- Indices de la tabla `proyecto`
--
ALTER TABLE `proyecto`
  ADD PRIMARY KEY (`Id_Proyecto`),
  ADD KEY `FK_proyecto_usuario` (`Id_Usuario`),
  ADD KEY `Fk_proyecto_area` (`Id_Area`),
  ADD KEY `Fk_proyecto_cliente` (`Id_Cliente`);

--
-- Indices de la tabla `rol`
--
ALTER TABLE `rol`
  ADD PRIMARY KEY (`Id_Rol`);

--
-- Indices de la tabla `rol_usuario`
--
ALTER TABLE `rol_usuario`
  ADD KEY `FK_rol_usuario_Usuario` (`Id_Usuario`),
  ADD KEY `FK_rol_usuario_rol` (`Id_Rol`);

--
-- Indices de la tabla `tipo_documento`
--
ALTER TABLE `tipo_documento`
  ADD PRIMARY KEY (`Id_Tipo_Documento`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`Id_usuario`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `Id_usuario` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `area`
--
ALTER TABLE `area`
  MODIFY `Id_Area` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT de la tabla `cliente`
--
ALTER TABLE `cliente`
  MODIFY `Id_Cliente` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT de la tabla `documento_asociado`
--
ALTER TABLE `documento_asociado`
  MODIFY `Id_Documento_Asociado` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT de la tabla `proyecto`
--
ALTER TABLE `proyecto`
  MODIFY `Id_Proyecto` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT de la tabla `tipo_documento`
--
ALTER TABLE `tipo_documento`
  MODIFY `Id_Tipo_Documento` int(11) NOT NULL AUTO_INCREMENT;
--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `documento_asociado`
--
ALTER TABLE `documento_asociado`
  ADD CONSTRAINT `Fk_documento_asociado_proyecto` FOREIGN KEY (`Id_Proyecto`) REFERENCES `proyecto` (`Id_Proyecto`),
  ADD CONSTRAINT `Fk_documento_asociado_tipodocumento` FOREIGN KEY (`Id_Tipo_Documento`) REFERENCES `tipo_documento` (`Id_Tipo_Documento`),
  ADD CONSTRAINT `Fk_documento_asociado_usuario` FOREIGN KEY (`Id_Usuario`) REFERENCES `usuario` (`Id_usuario`);

--
-- Filtros para la tabla `proyecto`
--
ALTER TABLE `proyecto`
  ADD CONSTRAINT `FK_proyecto_usuario` FOREIGN KEY (`Id_Usuario`) REFERENCES `usuario` (`Id_usuario`),
  ADD CONSTRAINT `Fk_proyecto_area` FOREIGN KEY (`Id_Area`) REFERENCES `area` (`Id_Area`),
  ADD CONSTRAINT `Fk_proyecto_cliente` FOREIGN KEY (`Id_Cliente`) REFERENCES `cliente` (`Id_Cliente`);

--
-- Filtros para la tabla `rol_usuario`
--
ALTER TABLE `rol_usuario`
  ADD CONSTRAINT `FK_rol_usuario_Usuario` FOREIGN KEY (`Id_Usuario`) REFERENCES `usuario` (`Id_usuario`),
  ADD CONSTRAINT `FK_rol_usuario_rol` FOREIGN KEY (`Id_Rol`) REFERENCES `rol` (`Id_Rol`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;







/*INSERTS*/
INSERT INTO `project_history`.`rol` VALUES (1, 'Administrador', 'Este perfil tiene permisos sobre todos los módulos', 1);
INSERT INTO `project_history`.`rol` VALUES (2, 'Consultor', 'Este perfil tiene permisos para consultar', 1);
INSERT INTO `project_history`.`rol` VALUES (3, 'Aprobador', 'Este perfil tiene permisos para aprobar', 1);

INSERT INTO `project_history`.`usuario` VALUES (1, 'Joel', 'Pulido G', 'M', 'joel', 'c0b84d1ccb798a5d7b82a2af42203428', '2017-09-03 00:00:00', 1);

INSERT INTO `project_history`.`rol_usuario` VALUES (1, 1);
INSERT INTO `project_history`.`rol_usuario` VALUES (1, 2);
INSERT INTO `project_history`.`rol_usuario` VALUES (1, 3);
