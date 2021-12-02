create database drakkar_gestor_financiero;
use drakkar_gestor_financiero;

create table usuarios(
	id int primary key auto_increment,
    correo varbinary(100) not null,
    contrasenia varbinary(100) not null,
    nombre varchar(100) not null,
    apellido varchar(100) not null,
    gasto_total varchar(100) not null default '0.00',
    ingreso_total varchar(100) not null default '0.00',
    saldo_general varchar(100) not null default '0.00',
    codigo varchar(64) default null,
    activo int not null  default 0
);

create table transacciones(
	id int primary key auto_increment,
    idcategoria int not null,
    monto varchar(100) not null,
    descripcion varchar(100) not null,
	fecha date not null
);

create table categorias(
	id int primary key auto_increment,
    idusuario int not null,
    categoria varchar(100) not null,
    tipo varchar(5) not null
);

alter table transacciones add constraint fk_transacciones_idcategoria foreign key (idcategoria) references categorias(id);
alter table categorias add constraint fk_categorias_idusuario foreign key (idusuario) references usuarios(id);

select id, cast(aes_decrypt(correo, 'gNcaM7h3X9wm') as char) as correo, cast(aes_decrypt(contrasenia, 'gNcaM7h3X9wm') as char) as contrasenia, nombre, apellido, gasto_total, ingreso_total, saldo_general from usuarios;