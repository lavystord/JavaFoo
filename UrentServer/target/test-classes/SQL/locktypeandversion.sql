TRUNCATE  TABLE  `version`;
TRUNCATE TABLE  locktype;

INSERT INTO serverdb.version (id, major, minor, type, firmwareFileId, comment, createDate) VALUES (1, 1, 1, 101, '', '1111', '2015-10-23 11:00:15');
INSERT INTO serverdb.version (id, major, minor, type, firmwareFileId, comment, createDate) VALUES (2, 1, 1, 1, null, '', '2015-10-24 09:59:34');
INSERT INTO serverdb.version (id, major, minor, type, firmwareFileId, comment, createDate) VALUES (3, 1, 1, 2, 'test.bin', '', '2015-10-24 10:00:08');

INSERT INTO serverdb.locktype (id, name, hardwareVersionId, newestFirmwareVersionId, description, imageIds) VALUES (1, 'version1', 2, 3, '1', '111');
