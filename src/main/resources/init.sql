use test_db;

create table users
(
    id           bigint primary key,
    username     varchar(255),
    passwordHash varchar(255),
    email        varchar(255),
    walletId     bigint unique
);

create table wallets
(
    id     bigint primary key,
    amount int,
    userId bigint,
    constraint foreign key (userId) references users (id)
);

alter table users
    add constraint foreign key (walletId) references wallets (id);

create table transactions
(
    id               varchar(255) primary key,
    transactionDate  datetime,
    status           varchar(255),
    type             varchar(255),
    amount           int,
    senderWalletId   bigint,
    receiverWalletId bigint,
    constraint foreign key (senderWalletId) references wallets (id),
    constraint foreign key (receiverWalletId) references wallets (id)
);