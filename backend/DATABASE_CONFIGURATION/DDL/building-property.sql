CREATE TABLE GENERAL.Building_property(
	Building_id BIGINT,
    Prop_key varchar(255),
	Prop_val varchar(255),
	Level INTEGER,
	PRIMARY KEY(Building_id,Prop_key, Prop_val, Level),
    FOREIGN KEY (Building_id) REFERENCES GENERAL.BUILDING(Building_id),
    FOREIGN KEY (Prop_key , Prop_val) REFERENCES GENERAL.PROPERTY(Prop_key , Prop_val)
);