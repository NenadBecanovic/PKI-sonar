INSERT INTO role
VALUES (1, 'ROLE_USER');
INSERT INTO role
VALUES (2, 'ROLE_CA');
INSERT INTO role
VALUES (3, 'ROLE_ADMIN');

INSERT INTO users
VALUES (101, 'admin1@gmail.com', 'Tim', '$2a$10$jUW5JeJEac6gqAsFZ3Su/O14C4PnZrsdpt/qCjVLNxkNbG4DkDhxC', 'Smith', 2);
INSERT INTO users
VALUES (102, 'user1@gmail.com', 'Annie', '$2a$10$jUW5JeJEac6gqAsFZ3Su/O14C4PnZrsdpt/qCjVLNxkNbG4DkDhxC', 'Robinson', 1);
INSERT INTO users
VALUES (103, 'user2@gmail.com', 'Cameron', '$2a$10$jUW5JeJEac6gqAsFZ3Su/O14C4PnZrsdpt/qCjVLNxkNbG4DkDhxC', 'Topaz', 1);
INSERT INTO users
VALUES (104, 'user3@gmail.com', 'Mark', '$2a$10$jUW5JeJEac6gqAsFZ3Su/O14C4PnZrsdpt/qCjVLNxkNbG4DkDhxC', 'Anderson', 1);

INSERT INTO key_usage (name, key_usage_type)
VALUES ('Digital Signature', 'DIGITAL_SIGNATURE');
INSERT INTO key_usage (name, key_usage_type)
VALUES ('Non-repudiation', 'NON_REPUDIATION');
INSERT INTO key_usage (name, key_usage_type)
VALUES ('Key encipherment', 'KEY_ENCIPHERMENT');
INSERT INTO key_usage (name, key_usage_type)
VALUES ('Data encipherment', 'DATE_ENCIPHERMENT');
INSERT INTO key_usage (name, key_usage_type)
VALUES ('Key agreement', 'KEY_AGREEMENT');
INSERT INTO key_usage (name, key_usage_type)
VALUES ('Certificate signing', 'CERTIFICATE_SIGNING');
INSERT INTO key_usage (name, key_usage_type)
VALUES ('CRL signing', 'CRL_SIGNING');
INSERT INTO key_usage (name, key_usage_type)
VALUES ('Encipher only', 'ENCIPHER_ONLY');
INSERT INTO key_usage (name, key_usage_type)
VALUES ('Decipher only', 'DECIPHER_ONLY');


INSERT INTO extended_key_usage (name, key_usage_type)
VALUES ('Client authorization', 'CLIENT_AUTH');
INSERT INTO extended_key_usage (name, key_usage_type)
VALUES ('Server authorization', 'SERVER_AUTH');
INSERT INTO extended_key_usage (name, key_usage_type)
VALUES ('Email protection', 'EMAIL_PROTECTION');
INSERT INTO extended_key_usage (name, key_usage_type)
VALUES ('OCSP signing', 'OCSP_SIGNING');


INSERT INTO certificate_extension (name, extension_type)
VALUES ('Basic constraints', 'BASIC_CONSTRAINTS');
INSERT INTO certificate_extension (name, extension_type)
VALUES ('Extended key usage', 'EXTENDED_KEY_USAGE');
INSERT INTO certificate_extension (name, extension_type)
VALUES ('Subject key identifier', 'SUBJECT_KEY_ID');
INSERT INTO certificate_extension (name, extension_type)
VALUES ('Authority key identifier', 'AUTHORITY_KEY_ID');