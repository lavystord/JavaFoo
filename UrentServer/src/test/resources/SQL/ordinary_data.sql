
TRUNCATE  table house;
TRUNCATE  TABLE `key`;
TRUNCATE TABLE `lock`;
TRUNCATE TABLE `address`;
TRUNCATE TABLE `keyactionlog`;
TRUNCATE TABLE `user`;
TRUNCATE TABLE  `userrole`;


INSERT INTO serverdb.`key` (id, lockId, ownerId, sharedFromId, expiredDate, maxTimes, usedTimes, alias, type, status, maxSharedCount, createDate, updateDate) VALUES (1, 13, 6, null, null, null, null, 'admin第2把钥匙', 'primary', 1, 9, '2015-08-24 21:25:15', '2015-08-24 21:25:19');
INSERT INTO serverdb.`key` (id, lockId, ownerId, sharedFromId, expiredDate, maxTimes, usedTimes, alias, type, status, maxSharedCount, createDate, updateDate) VALUES (2, 10, 3, null, null, null, null, '灵隐街道浙大路38号求是村小区', 'primary', 1, 9, '2015-08-23 14:05:42', '2015-08-23 14:05:42');
INSERT INTO serverdb.`key` (id, lockId, ownerId, sharedFromId, expiredDate, maxTimes, usedTimes, alias, type, status, maxSharedCount, createDate, updateDate) VALUES (3, 11, 6, null, null, null, null, '灵隐街道天目山路139号山水人家', 'primary', 1, 9, '2015-08-24 11:57:33', '2015-08-24 11:57:33');
INSERT INTO serverdb.`key` (id, lockId, ownerId, sharedFromId, expiredDate, maxTimes, usedTimes, alias, type, status, maxSharedCount, createDate, updateDate) VALUES (4, 12, 4, null, null, null, null, '灵隐街道浙大路38号求是村小区', 'primary', 1, 9, '2015-08-24 11:58:06', '2015-08-24 11:58:06');
INSERT INTO serverdb.`key` (id, lockId, ownerId, sharedFromId, expiredDate, maxTimes, usedTimes, alias, type, status, maxSharedCount, createDate, updateDate) VALUES (5, 11, 5, 3, '2016-09-05 12:03:08', 80, null, '土木楼', 'slave', 1, 0, '2015-08-25 12:03:59', '2015-08-25 12:04:03');

INSERT INTO serverdb.`lock` (id, houseId, typeId, gapAddress, active, disposableEncryptWord, constantEncryptWord, constantKeyWord, constantKeyWordExpiredDate,currentFirmwareVersionId,powerDensity) VALUES (10, 6, 1, 'gapAddress2', 1, '4:1Q*i??.P?|??', 'ù?" ￥ípòC??', null, '2015-09-09 00:00:00',3,80);
INSERT INTO serverdb.`lock` (id, houseId, typeId, gapAddress, active, disposableEncryptWord, constantEncryptWord, constantKeyWord, constantKeyWordExpiredDate,currentFirmwareVersionId,powerDensity) VALUES (11, 5, 1, 'gapAddress1', 1, '???x?9¨r?~μ1?', 'Z§·???6ê?T<.?', '1234567890abcdef', '2015-09-25 00:00:00',3,80);
INSERT INTO serverdb.`lock` (id, houseId, typeId, gapAddress, active, disposableEncryptWord, constantEncryptWord, constantKeyWord, constantKeyWordExpiredDate,currentFirmwareVersionId,powerDensity) VALUES (12, 7, 1, 'gapAddress3', 1, '>use÷?s°-ü?u?7??', '?I?·??k?a*?èe?', null, '2015-08-25 00:00:00',3,80);
INSERT INTO serverdb.`lock` (id, houseId, typeId, gapAddress, active, disposableEncryptWord, constantEncryptWord, constantKeyWord, constantKeyWordExpiredDate,currentFirmwareVersionId,powerDensity) VALUES (13, 8, 1, 'gapAddress4', 1, '???ae?6ta?Bó?', '?ì2N\\e?J??F+(?×', null, '2015-09-23 00:00:00',3,80);

INSERT INTO serverdb.house (id, addressId, ownerId, building, unit, floor, number) VALUES (5, 5, 1, '1', '2', 3, '301');
INSERT INTO serverdb.house (id, addressId, ownerId, building, unit, floor, number) VALUES (6, 4, 3, '2', '2', 5, '501');
INSERT INTO serverdb.house (id, addressId, ownerId, building, unit, floor, number) VALUES (7, 4, 4, '3', '3', 6, '603');
INSERT INTO serverdb.house (id, addressId, ownerId, building, unit, floor, number) VALUES (8, 4, null, '4', '4', 2, '209');

INSERT INTO serverdb.address (id, areaId, subdistrict, longitude, latitude) VALUES (4, 330106012, '浙大路38号求是村小区', 159.36, 163.36);
INSERT INTO serverdb.address (id, areaId, subdistrict, longitude, latitude) VALUES (5, 330106012, '天目山路139号山水人家', 159.3, 15.36);
INSERT INTO serverdb.address (id, areaId, subdistrict, longitude, latitude) VALUES (8, 130632103, 'test4', 15, 36);


INSERT INTO serverdb.user (id, name, password, mobile, nickname, lastLoginDevice, idCardNumber, headerImageId, gender, active, messageOption, createDate, updateDate) VALUES (1, 'admin', 'filename', '13000000000', null, null, 'iamadministrater', null, null, 1, 1, null, null);
INSERT INTO serverdb.user (id, name, password, mobile, nickname, lastLoginDevice, idCardNumber, headerImageId, gender, active, messageOption, createDate, updateDate) VALUES (6, 'admin_test', 'filename', '18612345678', null, null, 'iamtestadmin', null, null, 1, 1, null, null);
INSERT INTO serverdb.user (id, name, password, mobile, nickname, lastLoginDevice, idCardNumber, headerImageId, gender, active, messageOption, createDate, updateDate) VALUES (2, 'lockKeeper', 'seeyounexttime', '13000000001', null, null, 'iamlockKeeper', null, null, 1, 1, null, null);
INSERT INTO serverdb.user (id, name, password, mobile, nickname, lastLoginDevice, idCardNumber, headerImageId, gender, active, messageOption, createDate, updateDate) VALUES (3, 'person2', '123456', '13000000002', '张三', null, '159632186302593690', null, null, 1, 1, null, null);
INSERT INTO serverdb.user (id, name, password, mobile, nickname, lastLoginDevice, idCardNumber, headerImageId, gender, active, messageOption, createDate, updateDate) VALUES (4, 'person3', '123456', '13000000003', '李四', null, '256365196203202569', null, null, 1, 1, null, null);
INSERT INTO serverdb.user (id, name, password, mobile, nickname, lastLoginDevice, idCardNumber, headerImageId, gender, active, messageOption, createDate, updateDate) VALUES (5, 'person4', '123456', '13000000004', '王五', null, '362159196203250692', null, null, 1, 1, null, null);

INSERT INTO `userrole`(id,userId,roleId) VALUES (1,1,1);
INSERT INTO `userrole`(id,userId,roleId) VALUES (2,2,2);
INSERT INTO `userrole`(id,userId,roleId) VALUES (3,6,1);
