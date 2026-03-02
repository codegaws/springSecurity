-- Password para todos los usuarios: 1234
-- NOTA: Los hashes BCrypt son diferentes cada vez que se generan, pero todos son válidos para la misma contraseña
insert into customers (email, pwd)
values ('account@debuggeandoieas.com', '$2a$10$wtPMrSN.Mq5uXJs0o3Ffnu0Pxx/N/SL2ZYBluK4x8vSHOloL7ZIri'),
       ('cards@debuggeandoieas.com', '$2a$10$wtPMrSN.Mq5uXJs0o3Ffnu0Pxx/N/SL2ZYBluK4x8vSHOloL7ZIri'),
       ('loans@debuggeandoieas.com', '$2a$10$wtPMrSN.Mq5uXJs0o3Ffnu0Pxx/N/SL2ZYBluK4x8vSHOloL7ZIri'),
       ('balance@debuggeandoieas.com', '$2a$10$wtPMrSN.Mq5uXJs0o3Ffnu0Pxx/N/SL2ZYBluK4x8vSHOloL7ZIri');

insert into roles(role_name, description, id_customer)
values ('ROLE_ADMIN', 'cant view account endpoint', 1),
       ('ROLE_ADMIN', 'cant view cards endpoint', 2),
       ('ROLE_USER', 'cant view loans endpoint', 3),
       ('ROLE_USER', 'cant view balance endpoint', 4);

-- Client Secret para el partner OAuth2: debuggeandoideas
-- NOTA: Los hashes BCrypt son diferentes cada vez que se generan, pero todos son válidos para la misma contraseña
insert into partners(client_id,
                     client_name,
                     client_secret,
                     scopes,
                     grant_types,
                     authentication_methods,
                     redirect_uri,
                     redirect_uri_logout)
values ('debuggeandoideas',
        'debuggeando ideas',
        '$2a$10$hxIJiEojBsuFZKNtwcgcvOCrCbcKApvBf8BP86CyBOno6A72vGkGu',
        'read,write',
        'authorization_code,refresh_token',
        'client_secret_basic,client_secret_jwt',
        'https://oauthdebugger.com/debug',
        'https://springone.io/authorized')