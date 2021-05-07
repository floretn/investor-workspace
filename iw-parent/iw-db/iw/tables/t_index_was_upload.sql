CREATE TABLE investor_workspace.t_index_was_upload
( 
	iwu_pk			serial,
	iwu_indx_fk		integer not null,
	iwu_date		date not null,
	iwu_check		boolean not null,
	PRIMARY KEY (iwu_pk),
	FOREIGN KEY (iwu_indx_fk) REFERENCES investor_workspace.t_indexes (indx_pk)
);