DROP DATABASE IF EXISTS sentinel;
CREATE DATABASE sentinel CHARACTER SET UTF8;
USE sentinel;
CREATE TABLE resource_role_qps
(
  id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  app_id VARCHAR(60),
  api   VARCHAR(60),
  limit_qps   INT,
  create_at   INT
);

INSERT INTO resource_role_qps(app_id,api,limit_qps,create_at) VALUES('default','/byResource',1,1);

SELECT * FROM resource_role_qps;
