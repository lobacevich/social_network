CREATE TABLE mes.users ( 
	id BIGSERIAL PRIMARY KEY,
	password VARCHAR(255) NOT NULL,
	role VARCHAR(10) NOT NULL,
	username VARCHAR(23) NOT NULL UNIQUE,
	email VARCHAR(23) NOT NULL UNIQUE
 );
