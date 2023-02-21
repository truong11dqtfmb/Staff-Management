-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th2 21, 2023 lúc 08:53 AM
-- Phiên bản máy phục vụ: 10.4.25-MariaDB
-- Phiên bản PHP: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `staff_management`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `tbl_staffs`
--

CREATE TABLE `tbl_staffs` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gender` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `salary` float NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `tbl_staffs`
--

INSERT INTO `tbl_staffs` (`id`, `email`, `gender`, `name`, `phone`, `photo`, `salary`) VALUES
(17, 'quoctruong11tv@gmail.com', b'0', 'dao quo', '0342162155', 'logo.jpg', 12333),
(18, 'quoctruong', b'0', 'dao ', '0342162155', 'logo.jpg', 0),
(3, '123', b'0', '123', '123', 'logo.jpg', 3123),
(4, '1312300', b'0', 'test 100', '1232100', 'CRUD.png', 31231200),
(7, '111122334455', b'1', 'test 111122334455', '111122334455', '1676692080693_dot_thuyen.jpg', 454543),
(8, '1111', b'1', 'test 1111', '1111', '1676631313915_anh2.jpg', 1111),
(9, '123123', b'1', 'alo test 123', '123123', '1676726019497_gau_truc.png', 123123),
(11, '678', b'0', '678', '678', '1676687849894_hinh-nen-4k-laptop-va-pc.jpg', 678);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `tbl_staffs`
--
ALTER TABLE `tbl_staffs`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `tbl_staffs`
--
ALTER TABLE `tbl_staffs`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
