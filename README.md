# Team-Management-System
To run sucessfuly this project you need to insert your database name, username and password and create the tables like this:
And you need to insert the teams manually in your database.
If doesn't work, make sure that ypu have JDBC library in your project

-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Tempo de geração: 24-Ago-2023 às 23:03
-- Versão do servidor: 10.4.27-MariaDB
-- versão do PHP: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `testeatletas`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `athletes`
--

CREATE TABLE `athletes` (
  `id` int(11) NOT NULL,
  `athlete_name` varchar(100) NOT NULL,
  `athlete_number` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- --------------------------------------------------------

--
-- Estrutura da tabela `athletes_has_teams`
--

CREATE TABLE `athletes_has_teams` (
  `athletes_id` int(11) NOT NULL,
  `teams_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- --------------------------------------------------------

--
-- Estrutura da tabela `teams`
--

CREATE TABLE `teams` (
  `id` int(11) NOT NULL,
  `team_name` varchar(100) NOT NULL,
  `team_gender` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- --------------------------------------------------------

--
-- Estrutura da tabela `trainers`
--

CREATE TABLE `trainers` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- --------------------------------------------------------

--
-- Estrutura da tabela `trainers_has_teams`
--

CREATE TABLE `trainers_has_teams` (
  `trainers_id` int(11) NOT NULL,
  `teams_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



--
-- Índices para tabelas despejadas
--

--
-- Índices para tabela `athletes`
--
ALTER TABLE `athletes`
  ADD PRIMARY KEY (`id`);

--
-- Índices para tabela `athletes_has_teams`
--
ALTER TABLE `athletes_has_teams`
  ADD PRIMARY KEY (`athletes_id`,`teams_id`),
  ADD KEY `fk_athletes_has_teams_teams1_idx` (`teams_id`),
  ADD KEY `fk_athletes_has_teams_athletes_idx` (`athletes_id`);

--
-- Índices para tabela `teams`
--
ALTER TABLE `teams`
  ADD PRIMARY KEY (`id`);

--
-- Índices para tabela `trainers`
--
ALTER TABLE `trainers`
  ADD PRIMARY KEY (`id`);

--
-- Índices para tabela `trainers_has_teams`
--
ALTER TABLE `trainers_has_teams`
  ADD PRIMARY KEY (`trainers_id`,`teams_id`),
  ADD KEY `fk_trainers_has_teams_teams_idx` (`teams_id`);

--
-- AUTO_INCREMENT de tabelas despejadas
--

--
-- AUTO_INCREMENT de tabela `athletes`
--
ALTER TABLE `athletes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT de tabela `teams`
--
ALTER TABLE `teams`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de tabela `trainers`
--
ALTER TABLE `trainers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Restrições para despejos de tabelas
--

--
-- Limitadores para a tabela `athletes_has_teams`
--
ALTER TABLE `athletes_has_teams`
  ADD CONSTRAINT `fk_athletes_has_teams_athletes` FOREIGN KEY (`athletes_id`) REFERENCES `athletes` (`id`),
  ADD CONSTRAINT `fk_athletes_has_teams_teams1` FOREIGN KEY (`teams_id`) REFERENCES `teams` (`id`);

--
-- Limitadores para a tabela `trainers_has_teams`
--
ALTER TABLE `trainers_has_teams`
  ADD CONSTRAINT `fk_trainers_has_teams_teams` FOREIGN KEY (`teams_id`) REFERENCES `teams` (`id`),
  ADD CONSTRAINT `fk_trainers_has_teams_trainers` FOREIGN KEY (`trainers_id`) REFERENCES `trainers` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
