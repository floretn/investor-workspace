CREATE TABLE investor_workspace.t_date_of_indexes_changes
( 
	doic_pk				serial,
	doic_indx_fk			integer not null,
	doic_date_chng			date not null,
	PRIMARY KEY (doic_pk),
	FOREIGN KEY (doic_indx_fk) REFERENCES investor_workspace.t_indexes (indx_pk)
);