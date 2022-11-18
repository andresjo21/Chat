create database Users;

use Users;

create table Usuario(
	nombreUsuario varchar(15) not null,
	pass varchar(18) not null,
	nombre varchar(20) not null,
	estado bool not null,
	primary key(nombreUsuario)
);
create table Messages(
	receiver varchar(15) not null,
	sender varchar(15) not null,
	message varchar(1000) not null,
	FOREIGN KEY(receiver) REFERENCES Usuario(nombreUsuario),
	FOREIGN KEY(sender) REFERENCES Usuario(nombreUsuario)
);
