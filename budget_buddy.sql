-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 24, 2025 at 05:21 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `rplbo`
--

-- --------------------------------------------------------

--
-- Table structure for table `budgets`
--

CREATE TABLE `budgets` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `month` int(11) NOT NULL,
  `year` int(11) NOT NULL,
  `limit` decimal(12,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `export_logs`
--

CREATE TABLE `export_logs` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `tgl_export` datetime DEFAULT current_timestamp(),
  `filename` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `log_transaksi`
--

CREATE TABLE `log_transaksi` (
  `id_l` int(11) NOT NULL,
  `transaksi_id` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `action` enum('edited','deleted','inserted') NOT NULL,
  `note_before` text DEFAULT NULL,
  `note_after` text DEFAULT NULL,
  `tgl_update` datetime DEFAULT current_timestamp(),
  `tgl_delete` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `transaksi`
--

CREATE TABLE `transaksi` (
  `id_t` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `kategori` enum('Makanan & Minuman','Gaji','Tabungan','Belanja') NOT NULL,
  `type` enum('Pemasukan','Pengeluaran') NOT NULL,
  `amount` decimal(12,2) NOT NULL,
  `note` text DEFAULT NULL,
  `tgl_transaksi` date NOT NULL,
  `tgl_penulisan` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Triggers `transaksi`
--
DELIMITER $$
CREATE TRIGGER `trg_after_update_transaksi` AFTER UPDATE ON `transaksi` FOR EACH ROW BEGIN
    INSERT INTO log_transaksi (
        transaksi_id,
        user_id,
        action,
        note_before,
        note_after,
        tgl_update,
        tgl_delete
    ) VALUES (
        OLD.id_t,
        OLD.user_id,
        'edited',
        CONCAT(
            '{"Kategori" :"', OLD.kategori,
            '", "type" :"', OLD.type,
            '", "Jumlah" :', OLD.amount,
            ', "note" :"', IFNULL(OLD.note, ''),
            '", "date" :"', OLD.tgl_transaksi, '"}'
        ),
        CONCAT(
            '{"Kategori" :"', NEW.kategori,
            '", "type" :"', NEW.type,
            '", "Jumlah" :', NEW.amount,
            ', "note" :"', IFNULL(NEW.note, ''),
            '", "date" :"', NEW.tgl_transaksi, '"}'
        ),
        NOW(),
        NULL
    );
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `trg_before_delete_transaksi` BEFORE DELETE ON `transaksi` FOR EACH ROW BEGIN
    INSERT INTO log_transaksi (
        transaksi_id,
        user_id,
        action,
        note_before,
        note_after,
        tgl_update,
        tgl_delete
    ) VALUES (
        OLD.id_t,
        OLD.user_id,
        'deleted',
         CONCAT(
            '{"Kategori" :"', OLD.kategori,
            '", "type" :"', OLD.type,
            '", "Jumlah" :', OLD.amount,
            ', "note" :"', IFNULL(OLD.note, ''),
            '", "date" :"', OLD.tgl_transaksi, '"}'
        ),
        NULL,
        NULL,
        NOW()
    );
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id_u` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL,
  `tgl_akun` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id_u`, `username`, `password`, `email`, `tgl_akun`) VALUES
(1, 'admin', 'admin', 'admin@gmail.com', '2025-05-24 19:36:48');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `budgets`
--
ALTER TABLE `budgets`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `user_id` (`user_id`,`month`,`year`);

--
-- Indexes for table `export_logs`
--
ALTER TABLE `export_logs`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `log_transaksi`
--
ALTER TABLE `log_transaksi`
  ADD PRIMARY KEY (`id_l`),
  ADD KEY `transaksi_id` (`transaksi_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD PRIMARY KEY (`id_t`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id_u`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `budgets`
--
ALTER TABLE `budgets`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `export_logs`
--
ALTER TABLE `export_logs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `log_transaksi`
--
ALTER TABLE `log_transaksi`
  MODIFY `id_l` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `transaksi`
--
ALTER TABLE `transaksi`
  MODIFY `id_t` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id_u` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `budgets`
--
ALTER TABLE `budgets`
  ADD CONSTRAINT `budgets_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id_u`) ON DELETE CASCADE;

--
-- Constraints for table `export_logs`
--
ALTER TABLE `export_logs`
  ADD CONSTRAINT `export_logs_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id_u`) ON DELETE CASCADE;

--
-- Constraints for table `log_transaksi`
--
ALTER TABLE `log_transaksi`
  ADD CONSTRAINT `log_transaksi_ibfk_1` FOREIGN KEY (`transaksi_id`) REFERENCES `transaksi` (`id_t`) ON DELETE SET NULL,
  ADD CONSTRAINT `log_transaksi_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id_u`) ON DELETE CASCADE;

--
-- Constraints for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD CONSTRAINT `transaksi_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id_u`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
