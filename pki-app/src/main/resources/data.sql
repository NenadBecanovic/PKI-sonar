

INSERT INTO role
VALUES (1, 'ROLE_USER');
INSERT INTO role
VALUES (2, 'ROLE_CA');
INSERT INTO role
VALUES (3, 'ROLE_ADMIN');

INSERT INTO users
VALUES (101, TRUE, 'admin1@gmail.com', 'Tim', '$2a$10$jUW5JeJEac6gqAsFZ3Su/O14C4PnZrsdpt/qCjVLNxkNbG4DkDhxC', 'Smith', 3);
INSERT INTO users
VALUES (102, TRUE, 'user1@gmail.com', 'Annie', '$2a$10$jUW5JeJEac6gqAsFZ3Su/O14C4PnZrsdpt/qCjVLNxkNbG4DkDhxC', 'Robinson', 1);
INSERT INTO users
VALUES (103, TRUE, 'user2@gmail.com', 'Cameron', '$2a$10$jUW5JeJEac6gqAsFZ3Su/O14C4PnZrsdpt/qCjVLNxkNbG4DkDhxC', 'Topaz', 2);
INSERT INTO users
VALUES (104, TRUE, 'user3@gmail.com', 'Mark', '$2a$10$jUW5JeJEac6gqAsFZ3Su/O14C4PnZrsdpt/qCjVLNxkNbG4DkDhxC', 'Anderson', 1);
INSERT INTO users
VALUES (105, TRUE, 'ca1@gmail.com', 'Brian', '$2a$10$jUW5JeJEac6gqAsFZ3Su/O14C4PnZrsdpt/qCjVLNxkNbG4DkDhxC', 'Milner', 2);
INSERT INTO users
VALUES (106, TRUE, 'ca2@gmail.com', 'Glenn', '$2a$10$jUW5JeJEac6gqAsFZ3Su/O14C4PnZrsdpt/qCjVLNxkNbG4DkDhxC', 'Potter', 2);
INSERT INTO users
VALUES (107, TRUE, 'ca3@gmail.com', 'Tommie', '$2a$10$jUW5JeJEac6gqAsFZ3Su/O14C4PnZrsdpt/qCjVLNxkNbG4DkDhxC', 'Findlay', 2);

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
VALUES ('Authority key identifier', 'KEY_USAGE');
INSERT INTO certificate_extension (name, extension_type)
VALUES ('Extended key usage', 'EXTENDED_KEY_USAGE');
INSERT INTO certificate_extension (name, extension_type)
VALUES ('Subject key identifier', 'SUBJECT_KEY_ID');
INSERT INTO certificate_extension (name, extension_type)
VALUES ('Authority key identifier', 'AUTHORITY_KEY_ID');

INSERT INTO certificate_chain (id, serial_number, signer_serial_number, revoked, common_name, user_id, date_from,
date_to, certificate_type, has_signing_permission)
VALUES (101, 5350930521431098546, 5350930521431098546, FALSE, 'aa', 101, '2022-04-09 18:27:25.949', '2022-04-30 00:00:00', 'ROOT', TRUE);
INSERT INTO certificate_chain (id, serial_number, signer_serial_number, revoked, common_name, user_id, date_from,
                               date_to, certificate_type, has_signing_permission)
VALUES (102, 5864156063556204784, 5350930521431098546, FALSE, 'bb', 103, '2022-04-09 18:27:55.747', '2022-05-01 00:00:00', 'INTERMEDIATE', TRUE);
INSERT INTO certificate_chain (id, serial_number, signer_serial_number, revoked, common_name, user_id, date_from,
                               date_to, certificate_type, has_signing_permission)
VALUES (103, 6546362447523901530, 5864156063556204784, FALSE, 'ca2@gmail.com', 106, '2022-04-09 18:28:28.045', '2022-05-20 00:00:00', 'END_ENTITY', FALSE);
INSERT INTO certificate_chain (id, serial_number, signer_serial_number, revoked, common_name, user_id, date_from,
                               date_to, certificate_type, has_signing_permission)
VALUES (104, 1496910453755217097, 1496910453755217097, FALSE, 'PKI', 101, '2022-05-17 15:27:47.623', '2022-05-31 00:00:00', 'ROOT', TRUE);

INSERT INTO permission
VALUES (1, 'read_certificate');
INSERT INTO permission
VALUES (2, 'create_root_certificate');
INSERT INTO permission
VALUES (3, 'create_inter_certificate');
INSERT INTO permission
VALUES (4, 'create_ee_certificate');
INSERT INTO permission
VALUES (5, 'revoke_certificate');

INSERT INTO role_permissions
VALUES (1, 1); /* user <- read_certificate*/
INSERT INTO role_permissions
VALUES (2, 1); /* ca <- read_certificate */
INSERT INTO role_permissions
VALUES (3, 1); /* admin <- read_certificate */
INSERT INTO role_permissions
VALUES (3, 2); /* admin <- create_root_certificate */
INSERT INTO role_permissions
VALUES (2, 3); /* ca <- create_inter_certificate */
INSERT INTO role_permissions
VALUES (3, 3); /* admin <- create_inter_certificate */
INSERT INTO role_permissions
VALUES (2, 4); /* ca <- create_ee_certificate */
INSERT INTO role_permissions
VALUES (3, 4); /* admin <- create_ee_certificate */
INSERT INTO role_permissions
VALUES (2, 5); /* ca <- revoke_certificate */
INSERT INTO role_permissions
VALUES (3, 5); /* admin <- revoke_certificate */
