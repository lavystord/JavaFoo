# drop DATABASE IF EXISTS serverdb;
# create database serverdb default character set utf8;

grant all on serverdb.* to 'xc'@'localhost' identified by 'xc';
use serverdb;

drop table IF EXISTS `user`;
create table  `user`(
	id bigint primary key auto_increment,
	name varchar(64),
	password varchar(255) not null,
	mobile char(32) UNIQUE NOT NULL,
	nickname varchar(64),
	lastLoginDevice varchar(255),
	idCardNumber varchar(64) UNIQUE ,
  verificationImageId varchar(64),
	headerImageId varchar(64),
  gender enum('男', '女'),
	active tinyint,
  verificationStatus tinyint DEFAULT 0,
  messageOption TINYINT DEFAULT 1,
	createDate datetime,
	updateDate datetime,

  index user_name(name),
  index user_active(active),
  index user_createDate(createDate)
	) engine=innodb;


	insert into user(id, nickname, name, mobile, password, messageOption, active) values(1, "admin", "管理员", "13000000000", "filename", 1, TRUE);
  insert into user(id, nickname, name, mobile, password, messageOption, active) values(2, "lockKeeper", "锁操作员", "13000000001", "seeyounexttime", 1, TRUE);

drop table IF EXISTS role;
create table role(
  id bigint primary key auto_increment,
  name varchar(64) UNIQUE ,
  comment text,
  active BOOLEAN
) engine=innodb;

insert into role(id, name, comment, active) values(1, "管理员", null, true);
insert into role(id, name, comment, active) values(2, "锁操作员", null, true);
INSERT INTO role(id, name, comment, active) VALUES (-1, '任意角色', '任意角色，用于权限控制，请不要赋予任何用户', true);
INSERT INTO role(id, name, comment, active) VALUES (-2, '任何人', '任何人，用于权限控制，请不要赋予任何用户', true);

drop TABLE IF EXISTS userRole;
create TABLE userRole (
  id bigint primary key auto_increment,
  userId BIGINT,
  roleId BIGINT,

  UNIQUE INDEX userRole_userId_roleId(userId, roleId),
  INDEX userRole_roleId(roleId)
)ENGINE = innodb;

insert into userRole(userId, roleId) values(1, 1);
insert into userRole(userId, roleId) values(2, 2);

DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`(
  id bigint primary key,
  name varchar(255) not null,
  dest varchar(256),
  leaf boolean not null,
  parentId BIGINT,

  index menu_parentId(parentId)
)ENGINE = innodb;


insert into menu(id, name, dest, leaf) values(1, '用户管理', null, false);
insert into menu(id, name, dest, leaf, parentId) values(2, '用户管理', 'UserManage', true, 1);
insert into menu(id, name, dest, leaf, parentId) values(3, '角色管理', 'RoleManage', true, 1);
insert into menu(id, name, dest, leaf, parentId) values(4, '用户角色管理', 'UserRoleManage', true, 2);
insert into menu(id, name, dest, leaf, parentId) values(5, '用户角色管理', 'UserRoleManage', true, 3);
insert into menu(id, name, dest, leaf, parentId) values(6, '角色菜单管理', 'RoleMenuManage', true, 3);
insert into menu(id, name, dest, leaf, parentId) values(7, '角色URL管理', 'RoleUrlManage', true, 3);

insert into menu(id, name, dest, leaf) values(20, '菜单管理', null, false);
insert into menu(id, name, dest, leaf, parentId) values(21, '菜单管理', 'MenuManage', true, 20);
insert into menu(id, name, dest, leaf, parentId) values(22, 'URL管理', 'UrlManage', true, 20);
insert into menu(id, name, dest, leaf, parentId) values(23, '角色URL管理', 'RoleUrlManage', true, 22);

insert into menu(id, name, dest, leaf) values(40, '锁管理', null, false);
insert into menu(id, name, dest, leaf, parentId) values(41, '版本管理', 'VersionManage', true, 40);
insert into menu(id, name, dest, leaf, parentId) values(42, '锁类型管理', 'LockTypeManage', true, 40);
insert into menu(id, name, dest, leaf, parentId) values(43, '锁管理', 'LockManage', true, 40);


insert into menu(id, name, dest, leaf) values(60, '房屋管理', null, false);
insert into menu(id, name, dest, leaf, parentId) values(61, '地点管理', 'AddressManage', true, 60);
insert into menu(id, name, dest, leaf, parentId) values(62, '房屋管理', 'HouseManage', true, 60);


insert into menu(id, name, dest, leaf) values(80, '钥匙管理', null, false);
insert into menu(id, name, dest, leaf, parentId) values(81, '钥匙管理', 'KeyManage', true, 80);


insert into menu(id, name, dest, leaf) values(200, '日志管理', null, false);
insert into menu(id, name, dest, leaf, parentId) values(201, '更新日志管理', 'DataModificationLogManage', true, 200);
insert into menu(id, name, dest, leaf, parentId) values(202, '操作日志管理', 'KeyActionLogManage', true, 200);





DROP TABLE IF EXISTS `roleMenu`;
CREATE TABLE `roleMenu`(
  id BIGINT primary key auto_increment,
  roleId BIGINT not null,
  menuId BIGINT not null,
  `read` boolean default true,
  `create` boolean default false,
  `update` boolean default false,
  `deletee` boolean default false,

  unique index roleMenu_roleId_menuId(roleId, menuId)
)ENGINE = innodb;


insert into rolemenu(roleId, menuId, `read`, `create`, `update`, `deletee`) values(1, 2, true, false, false, false);
insert into rolemenu(roleId, menuId, `read`, `create`, `update`, `deletee`) values(1, 3, true, true, true, false);
insert into rolemenu(roleId, menuId, `read`, `create`, `update`, `deletee`) values(1, 4, true, true, false, true);
insert into rolemenu(roleId, menuId, `read`, `create`, `update`, `deletee`) values(1, 5, true, false, false, true);
insert into rolemenu(roleId, menuId, `read`, `create`, `update`, `deletee`) values(1, 6, true, true, false, true);
insert into rolemenu(roleId, menuId, `read`, `create`, `update`, `deletee`) values(1, 7, true, true, true, true);
insert into rolemenu(roleId, menuId, `read`, `create`, `update`, `deletee`) values(1, 21, true, false, false, false);
insert into rolemenu(roleId, menuId, `read`, `create`, `update`, `deletee`) values(1, 22, true, true, true, true);
insert into rolemenu(roleId, menuId, `read`, `create`, `update`, `deletee`) values(1, 23, true, true, true, true);
insert into rolemenu(roleId, menuId, `read`, `create`, `update`, `deletee`) values(1, 41, true, true, true, true);
insert into rolemenu(roleId, menuId, `read`, `create`, `update`, `deletee`) values(1, 42, true, true, true, true);
insert into rolemenu(roleId, menuId, `read`, `create`, `update`, `deletee`) values(1, 43, true, true, false, true);
insert into rolemenu(roleId, menuId, `read`, `create`, `update`, `deletee`) values(1, 61, true, true, true, true);
insert into rolemenu(roleId, menuId, `read`, `create`, `update`, `deletee`) values(1, 62, true, true, true, false);
insert into rolemenu(roleId, menuId, `read`, `create`, `update`, `deletee`) values(1, 81, true, true, false, false);
insert into rolemenu(roleId, menuId, `read`, `create`, `update`, `deletee`) values(1, 201, true, false, false, false);
insert into rolemenu(roleId, menuId, `read`, `create`, `update`, `deletee`) values(1, 202, true, false, false, false);


DROP TABLE IF EXISTS `url`;
CREATE TABLE `url`(
  id BIGINT primary key auto_increment,
  value varchar(128) UNIQUE
) ENGINE = innodb;

INSERT INTO `url`values(1, "/");
INSERT INTO `url`values(2, "/tryautologin");
INSERT INTO `url`values(3, "/login");
INSERT INTO `url`values(4, "/appmenu");
INSERT INTO `url`values(5, "/address");
INSERT INTO `url`values(6, "/address/{id}");
INSERT INTO `url`values(7, "/area");
INSERT INTO `url`values(8, "/area/{id}");
INSERT INTO `url`values(9, "/file");
INSERT INTO `url`values(10, "/house");
INSERT INTO `url`values(11, "/house/{id}");
INSERT INTO `url`values(12, "/key");
INSERT INTO `url`values(13, "/key/{id}");
INSERT INTO `url`values(14, "/lock");
INSERT INTO `url`values(15, "/lock/{id}");
INSERT INTO `url`values(16, "/lockType");
INSERT INTO `url`values(17, "/lockType/{id}");
INSERT INTO `url`values(18, "/role");
INSERT INTO `url`values(19, "/role/{id}");
INSERT INTO `url`values(20, "/roleMenu");
INSERT INTO `url`values(21, "/roleMenu/{id}");
INSERT INTO `url`values(22, "/user");
INSERT INTO `url`values(23, "/user/{id}");
INSERT INTO `url`values(24, "/userRole");
INSERT INTO `url`values(25, "/userRole/{id}");
INSERT INTO `url`values(26, "/version");
INSERT INTO `url`values(27, "/version/{id}");
INSERT INTO `url`values(28, "/register");
INSERT INTO `url`values(29, "/registerCode");
INSERT INTO `url`values(30, "/logout");
INSERT INTO `url`values(31, "/password");
INSERT INTO `url`values(32, "/headerImageFile");
INSERT INTO `url`values(33, "/url");
INSERT INTO `url`values(34, "/url/{id}");
INSERT INTO `url`values(35, "/roleUnrelated");
INSERT INTO `url`values(36, "/roleUrl");
INSERT INTO `url`values(37, "/roleUrl/{id}");
INSERT INTO `url`  VALUES (38, "/unrelatedMethods");
INSERT INTO `url` VALUES (39, "/allappmenu");
INSERT INTO `url` VALUES (40, "/key/activate");
INSERT INTO `url` VALUES (41, "/key/deactivate");
INSERT INTO `url` VALUES (42, "/dataModificationLog");
INSERT INTO `url` VALUES (43, "/dataModificationLog/{id}");
INSERT INTO `url` VALUES(44, "/me");
INSERT INTO `url` VALUES(45, "/mobile");
INSERT INTO `url` VALUES(46, "/verify");
INSERT INTO `url` VALUES(47, "/certify");
INSERT INTO `url` VALUES(48, "/lock/deactivate");
INSERT INTO `url` VALUES (49, "/keyActionLog");
INSERT INTO `url` VALUES (50, "/keyActionLog/{id}");
INSERT INTO `url` VALUES (51, "/checkCompatible");


INSERT INTO `url` VALUES (501, "/locateLock");
INSERT INTO `url` VALUES (502, "/locateUser");
INSERT INTO `url` VALUES (503, "/locateAddress");
INSERT INTO `url` VALUES (504, "/locateLockType");
INSERT INTO `url` VALUES (505, "/locateHouse");
INSERT INTO `url` VALUES (506, "/registerHouse");
INSERT INTO `url` VALUES (507, "/registerLock");
INSERT INTO `url` VALUES (508, "/setOwner");

INSERT INTO `url` VALUES (1000, "/myKey");
INSERT INTO `url` VALUES (1001, "/myKey/{id}");
INSERT INTO `url` VALUES (1002, "/myKey/keyWord");
INSERT INTO `url` VALUES (1003, "/myKey/keyUsed");
INSERT INTO `url` VALUES (1004, "/share/queryName");
INSERT INTO `url` VALUES (1005, "/share/{id}");
INSERT INTO `url` VALUES (1006, "/share");
INSERT INTO `url` VALUES (1007, "/myKey/dfu");
INSERT INTO `url` VALUES (1008, "/myKey/log");



INSERT INTO `url` VALUES (2000,"/user2");


DROP TABLE IF EXISTS `roleUrl`;
CREATE TABLE `roleUrl`(
  id BIGINT primary key auto_increment,
  urlId BIGINT NOT NULL,
  roleId BIGINT NOT NULL,
  method ENUM('POST', 'PUT', 'GET', 'DELETE') NOT NULL,
  COMMENT text,
  UNIQUE  INDEX roleUrl_index(urlId, roleId, method)
)ENGINE innodb;

INSERT INTO `roleUrl` values(1, 1, -2, 'GET',NULL);
INSERT INTO `roleUrl` values(2, 2, -2, 'POST',NULL);
INSERT INTO `roleUrl` values(3, 3, -2, 'POST',NULL);
INSERT INTO `roleUrl` values(4, 4, -1, 'GET',NULL);
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (5, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (5, 1, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (6, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (6, 1, 'PUT');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (6, 1, 'DELETE');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (7, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (7, 1, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (8, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (8, 1, 'PUT');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (8, 1, 'DELETE');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (9, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (9, 1, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (10, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (10, 1, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (11, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (11, 1, 'PUT');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (11, 1, 'DELETE');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (12, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (12, 1, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (13, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (14, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (14, 1, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (15, 1, 'GET');
-- INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (15, 1, 'PUT');--
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (15, 1, 'DELETE');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (16, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (16, 1, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (17, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (17, 1, 'PUT');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (17, 1, 'DELETE');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (18, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (18, 1, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (19, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (19, 1, 'PUT');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (19, 1, 'DELETE');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (20, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (20, 1, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (21, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (21, 1, 'PUT');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (21, 1, 'DELETE');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (22, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (22, 1, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (23, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (23, 1, 'PUT');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (23, 1, 'DELETE');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (24, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (24, 1, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (25, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (25, 1, 'PUT');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (25, 1, 'DELETE');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (26, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (26, 1, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (27, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (27, 1, 'PUT');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (28, -2, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (29, -2, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (30, -1, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (31, -1, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (32, -1, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (32, -1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (33, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (33, 1, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (34, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (34, 1, 'PUT');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (34, 1, 'DELETE');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (35, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (36, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (36, 1, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (37, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (37, 1, 'PUT');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (37, 1, 'DELETE');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (38, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (39, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (40, 1, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (41, 1, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (42, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (43, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (44, -1, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (45, -1, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (46, -2, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (47, -1, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (48, 1, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (49, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (50, 1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (51, 1, 'GET');




INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (501, 2, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (502, 2, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (503, 2, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (504, 2, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (505, 2, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (506, 2, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (507, 2, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (508, 2, 'POST');


INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (1000, -1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (1001, -1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (1002, -1, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (1003, -1, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (1001, -1, 'PUT');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (1001, -1, 'DELETE');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (1004, -1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (1006, -1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (1006, -1, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (1005, -1, 'PUT');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (1005, -1, 'DELETE');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (1007, -1, 'GET');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (1007, -1, 'POST');
INSERT INTO `roleUrl`(urlId, roleId, method) VALUES (1008, -1, 'GET');


INSERT  INTO `roleUrl`(urlId, roleId, method) VALUES (2000,-1,'GET');


DROP TABLE IF EXISTS `dataModificationLog`;
CREATE TABLE `dataModificationLog` (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  userId BIGINT,
  calledClassName VARCHAR(128),
  calledMethodName VARCHAR(128),
  method ENUM('GET', 'POST', 'PUT', 'DELETE'),
  bodyData text,
  remoteAddress VARCHAR(128),
  userAgent text,
  createDate DATETIME,

  INDEX dml_createDate_userId(createDate, userId)
)ENGINE = innodb;







DROP TABLE if EXISTS `locktype`;
CREATE TABLE `locktype` (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name varchar(64) NOT NULL,
  hardwareVersionId BIGINT NOT NULL,
  newestFirmwareVersionId BIGINT NOT NULL,
  description TEXT,
  imageIds TEXT
) ENGINE = innodb;


DROP TABLE IF EXISTS `version`;
CREATE TABLE `version`(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  major INT,
  minor INT,
  type INT,
  firmwareFileId VARCHAR(128),
  comment VARCHAR(256),
  createDate DATETIME,

  index version_type(type)
) ENGINE = innodb;



--
-- 测试数据一般生成在下面五张表以及User表上，而Version和LockType表的数据一般是手动生成
--


drop table IF EXISTS house;
create table house(
  id bigint primary key auto_increment,
  addressId BIGINT NOT NULL,
  ownerId BIGINT,
  building VARCHAR(32),
  unit VARCHAR(32),
  floor INT,
  number VARCHAR(64),

  index house_ownerId(ownerId)
) ENGINE = innodb;


drop TABLE IF EXISTS `key`;
create table `key` (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  lockId BIGINT NOT NULL ,
  ownerId BIGINT NOT NULL,
  sharedFromId BIGINT,
  expiredDate DATETIME,
  maxTimes INT,
  usedTimes INT,
  alias VARCHAR(64),
  type varchar(32),
  status SMALLINT NOT NULL ,
  maxSharedCount INT,
  createDate DATETIME,
  updateDate DATETIME,

  index key_lockId(lockId),
  index key_ownerId(ownerId),
  index key_sharedFromId(sharedFromId)
) ENGINE = innodb;


drop TABLE IF EXISTS `lock`;
create TABLE `lock` (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  houseId BIGINT,
  typeId BIGINT NOT NULL,
  gapAddress char(24) NOT NULL,
  active BOOLEAN,
  disposableEncryptWord char(16),
  constantEncryptWord char(16),
  constantKeyWord char(16),
  currentFirmwareVersionId BIGINT not null,
  constantKeyWordExpiredDate DATETIME,
  powerDensity TINYINT,
  createDate DATETIME,
  updateDate DATETIME,

  UNIQUE index lock_houseId(houseId),
  index lock_typeId(typeId),
  index lock_gapAddress(gapAddress)
) ENGINE = innodb;



DROP TABLE IF EXISTS address;
CREATE TABLE address(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  areaId BIGINT NOT NULL,
  subdistrict VARCHAR(64) NOT NULL,
  longitude FLOAT,
  latitude FLOAT
) ENGINE = InnoDB;


DROP TABLE IF EXISTS keyActionLog;
CREATE TABLE keyActionLog(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  keyId BIGINT NOT NULL ,
  lockId BIGINT NOT NULL ,
  managerId BIGINT NOT NULL ,
  lockGapAddress char(17),
  houseId BIGINT NOT NULL ,
  action smallint,
  data text,
  `time` DATETIME,
  createDate DATETIME,

  index kal_keyId(keyId),
  index kal_lockId(lockId),
  index kal_houseId(houseId),
  INDEX kal_time(time)
) ENGINE = innodb;