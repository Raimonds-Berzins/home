CREATE TABLE password_auth(
	id SERIAL PRIMARY KEY,
	password_hash VARCHAR(100),	
	person_id INTEGER REFERENCES person(id),
	isactive BOOLEAN,
	created TIMESTAMP(6),
	updated TIMESTAMP(6),
	eTag VARCHAR(50)
)