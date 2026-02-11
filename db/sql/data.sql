insert into customers (email, pwd)
values ('account@debuggeandoieas.com', 'to_be_encoded'),
       ('cards@debuggeandoieas.com', 'to_be_encoded'),
       ('loans@debuggeandoieas.com', 'to_be_encoded'),
       ('balance@debuggeandoieas.com', 'to_be_encoded');

insert into roles(role_name, description, id_customer)
values ('ROLE_ADMIN', 'cant view account endpoint', 1),
       ('ROLE_ADMIN', 'cant view cards endpoint', 2),
       ('ROLE_USER', 'cant view loans endpoint', 3),
       ('ROLE_USER', 'cant view balance endpoint', 4);

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
        'secret',
        'read,write',
        'authorization_code,refresh_token',
        'client_secret_basic,client_secret_jwt',
        'https://oauthdebugger.com/debug',
        'https://springone.io/authorized')