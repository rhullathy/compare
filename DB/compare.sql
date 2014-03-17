DROP TABLE IF EXISTS `compare_vehicle`;
CREATE  TABLE IF NOT EXISTS `compare_vehicle` (
`vehid` INT(11) NOT NULL auto_increment,
`year` INT(4) NOT NULL ,
`make` VARCHAR(50) NOT NULL ,
`model` VARCHAR(50) NOT NULL ,
`modelid` VARCHAR(20) NOT NULL ,
`trim` VARCHAR(50) NOT NULL ,
`trimid` VARCHAR(20) NOT NULL ,
`imageurl` VARCHAR(200) ,
`substitutetrimid` VARCHAR(20), 
 PRIMARY KEY (`vehid`) ,  
  CONSTRAINT compare_vehicle_uc1 UNIQUE (year,make,modelid,trimid),
  INDEX `compare_vehicle_idx1` (`year`) ,
  INDEX `compare_vehicle_idx2` (`make`),
  INDEX `compare_vehicle_idx3` (`modelid`),
  INDEX `compare_vehicle_idx4` (`trimid`))
ENGINE = MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `compare_datapoint_type`;
CREATE  TABLE IF NOT EXISTS `compare_datapoint_type` (
`datapointid` INT(11) NOT NULL auto_increment,
`datapointtype` VARCHAR(100) NOT NULL ,
`datapointsubtype` VARCHAR(200) ,
`valuetype` VARCHAR(20) ,
 PRIMARY KEY (`datapointid`),
  CONSTRAINT compare_datapoint_type_uc1 UNIQUE (datapointtype,datapointsubtype),
  INDEX `compare_datapoint_type_idx1` (`datapointtype`) ,
  INDEX `compare_datapoint_type_idx2` (`datapointsubtype`))  
ENGINE = MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `compare_datapoint_value`;
CREATE  TABLE IF NOT EXISTS `compare_datapoint_value` (
`valueid` INT(11) NOT NULL auto_increment,
`trimid` VARCHAR(20) NOT NULL ,
`datapointid` int(11)  NOT NULL ,
`value` VARCHAR(300) ,
PRIMARY KEY (`valueid`) ,
  CONSTRAINT compare_datapoint_value_uc1 UNIQUE (trimid,datapointid),
  CONSTRAINT compare_datapoint_value_fk2 FOREIGN KEY (trimid) REFERENCES  compare_vehicle (trimid) ON UPDATE RESTRICT ON DELETE RESTRICT,
  CONSTRAINT compare_datapoint_value_fk3 FOREIGN KEY (datapointid) REFERENCES  compare_datapoint_type (datapointid) ON UPDATE RESTRICT ON DELETE RESTRICT,

  INDEX `compare_datapoint_value_idx1` (`trimid`) ,
  INDEX `compare_datapoint_value_idx2` (`datapointid`))
ENGINE = MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `compare_suggested_trim`;
CREATE  TABLE IF NOT EXISTS `compare_suggested_trim` (
`trimid` VARCHAR(20) NOT NULL ,
`suggestedtrimid` VARCHAR(20) NOT NULL ,
  PRIMARY KEY (`trimid`, `suggestedtrimid`) ,
  CONSTRAINT compare_suggested_trim_fk1 FOREIGN KEY (trimid) REFERENCES  compare_vehicle (trimid) ON UPDATE RESTRICT ON DELETE RESTRICT,
  CONSTRAINT compare_suggested_trim_fk2 FOREIGN KEY (suggestedtrimid) REFERENCES  compare_vehicle (trimid) ON UPDATE RESTRICT ON DELETE RESTRICT
  )
ENGINE = MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `compare_report_cache`;
CREATE  TABLE IF NOT EXISTS `compare_report_cache` (
`reportid` varchar(50) NOT NULL,
`reportdata`  LONGTEXT,
PRIMARY KEY (`reportid`) )
ENGINE = MyISAM DEFAULT CHARSET=utf8;


ALTER TABLE vwcom.compare_datapoint_type
 ADD typeorder INT AFTER valuetype,
 ADD subtypeorder INT;



delete from `compare_datapoint_type`;


insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (1,'MSRP/Fuel Economy','MSRP','lessthan',10,1);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (2,'MSRP/Fuel Economy','Destination Charge','lessthan',10,2);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (3,'MSRP/Fuel Economy','Comparably Equipped Price','lessthan',10,3);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (4,'MSRP/Fuel Economy','EPA Fuel Economy - City (mpg)','greaterthan',10,4);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (5,'MSRP/Fuel Economy','EPA Fuel Economy - Highway (mpg)','greaterthan',10,5);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (15,'Performance','Tachometer','standard',20,10);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (16,'Performance','Tires',null,20,11);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (17,'Performance','Transmission Speeds',null,20,12);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (18,'Performance','Transmission Type',null,20,13);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (19,'Performance','Suspension Type',null,20,14);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (111,'Performance','Adaptive Automatic Transmission',null,20,15);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (14,'Performance','Power Steering','standard',20,9);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (13,'Performance','Peak Torque (RPM)','greaterthan',20,8);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (12,'Performance','Peak Horsepower (RPM)','greaterthan',20,7);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (11,'Performance','Torque (lbs/ft)','greaterthan',20,6);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (10,'Performance','Horsepower','greaterthan',20,5);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (9,'Performance','Fuel Tank Capacity (gal)','greaterthan',20,4);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (8,'Performance','Drivetrain',null,20,3);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (7,'Performance','Cruise Control','standard',20,2);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (6,'Performance','Brakes - Type',null,20,1);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (44,'Safety','Front Airbags','standard',30,1);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (43,'Safety','Electronic Traction Control','standard',30,2);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (42,'Safety','Electronic Stability Control','standard',30,3);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (41,'Safety','Low Tire Pressure Indicator','standard',30,4);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (40,'Safety','Anti-Intrusion Side Door Beams','standard',30,5);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (38,'Safety','NHTSA Crash Test Rating - Side Rear','greaterthan',30,6);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (45,'Safety','Front Airbag Location - Driver','standard',30,7);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (46,'Safety','Front Airbag Location - Passenger','standard',30,8);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (47,'Safety','Front Passenger Airbag on-off sensor','standard',30,9);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (48,'Safety','Side Head Curtain Airbag Location - Front','standard',30,10);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (49,'Safety','Side Head Curtain Airbag Location - Rear/Second Row','standard',30,11);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (50,'Safety','Side Curtain Airbags','standard',30,12);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (51,'Safety','Front Foglights','standard',30,13);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (52,'Safety','Front Side Airbags','standard',30,14);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (37,'Safety','NHTSA Crash Test Rating - Side Front','greaterthan',30,15);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (36,'Safety','NHTSA Crash Test Rating - Rollover','greaterthan',30,16);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (35,'Safety','NHTSA Crash Test Rating - Passenger','greaterthan',30,17);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (114,'Safety','Automatic Headlights','standard',30,18);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (117,'Safety','Side Head Curtain Airbag Location - Third Row',null,30,19);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (30,'Safety','Anti-Theft Protection','standard',30,20);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (31,'Safety','4-Wheel ABS Brakes','standard',30,21);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (32,'Safety','Brake Assist System','standard',30,22);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (33,'Safety','Child Safety Door Locks','standard',30,23);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (34,'Safety','NHTSA Crash Test Rating - Front Driver','greaterthan',30,24);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (39,'Safety','Daytime Running Lights','standard',30,25);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (73,'Comfort and Convenience','Rear Window Wipers','standard',40,1);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (72,'Comfort and Convenience','Power Lock','standard',40,2);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (71,'Comfort and Convenience','Power Adjustable Exterior Mirrors','standard',40,3);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (70,'Comfort and Convenience','Remote Entry Key Fob','standard',40,4);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (69,'Comfort and Convenience','Illuminated Vanity Mirror','standard',40,5);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (68,'Comfort and Convenience','Driver Multi-Adjustable Power Seat','standard',40,6);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (67,'Comfort and Convenience','Ventilation System with Micro Filter','standard',40,7);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (74,'Comfort and Convenience','Power Steering Vehicle Speed Sensing','standard',40,8);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (75,'Comfort and Convenience','Power Sunroof/Moonroof','standard',40,9);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (76,'Comfort and Convenience','Power Windows','standard',40,10);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (120,'Comfort and Convenience','Privacy Glass','standard',40,11);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (115,'Comfort and Convenience','Rain Sensing Windshield Wipers',null,40,12);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (81,'Comfort and Convenience','Intermittent Windshield Wipers','standard',40,13);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (80,'Comfort and Convenience','Steering Wheel Mounted Controls','standard',40,14);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (79,'Comfort and Convenience','Tilting Steering Wheel Adjustment','standard',40,15);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (78,'Comfort and Convenience','Telescopic Steering Wheel Adjustment','standard',40,16);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (82,'Comfort and Convenience','Rear Window Defogger','standard',40,17);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (66,'Comfort and Convenience','Air Conditioning','standard',40,18);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (77,'Comfort and Convenience','Remote Trunk/Hatch Release','standard',40,19);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (94,'Exterior Dimensions','Rear Spoiler','standard',50,1);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (93,'Exterior Dimensions','Wheelbase (in)','greaterthan',50,2);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (96,'Exterior Dimensions','Heated Exterior Mirrors','standard',50,3);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (88,'Exterior Dimensions','Front Track (in)','greaterthan',50,4);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (89,'Exterior Dimensions','Overall Height (in)','lessthan',50,5);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (90,'Exterior Dimensions','Overall Length (in)','greaterthan',50,6);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (91,'Exterior Dimensions','Overall Width (in)','greaterthan',50,7);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (92,'Exterior Dimensions','Rear Track (in)','greaterthan',50,8);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (95,'Exterior Dimensions','Wheel Type',null,50,9);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (121,'Exterior Dimensions','Roof Rails',null,50,10);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (122,'Exterior Dimensions','Ground Clearance Unladen (in)',null,50,11);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (58,'Interior Dimensions','Interior Dimensions - Shoulder Room Rear (in)','greaterthan',60,1);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (53,'Interior Dimensions','Interior Dimensions - Headroom Front (in)','greaterthan',60,2);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (119,'Interior Dimensions','Interior Dimensions - Hip Room Rear (in)','greaterthan',60,3);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (118,'Interior Dimensions','Interior Dimensions - Hip Room Front (in)','greaterthan',60,4);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (60,'Interior Dimensions','Cargo Capacity - Rear Seats Down/Removed (cuft)','greaterthan',60,5);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (59,'Interior Dimensions','Cargo Capacity - Rear Seats Up (cuft)','greaterthan',60,6);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (57,'Interior Dimensions','Interior Dimensions - Shoulder Room Front (in)','greaterthan',60,7);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (56,'Interior Dimensions','Interior Dimensions - Legroom Rear (in)','greaterthan',60,8);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (55,'Interior Dimensions','Interior Dimensions - Legroom Front (in)','greaterthan',60,9);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (54,'Interior Dimensions','Interior Dimensions - Headroom Rear (in)','greaterthan',60,10);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (97,'Warranty','No-Charge Scheduled Maintenance','standard',70,1);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (105,'Warranty','Vehicle Warranty - Duration (months)','greaterthan',70,2);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (103,'Warranty','Powertrain Warranty - Duration (months)','greaterthan',70,3);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (102,'Warranty','Powertrain Warranty - Distance','greaterthan',70,4);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (100,'Warranty','Anti-Corrosion Warranty - Distance','greaterthan',70,5);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (101,'Warranty','Anti-Corrosion Warranty - Duration (months)','greaterthan',70,6);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (99,'Warranty','No-Charge Scheduled Maintenance - Number of Miles','greaterthan',70,7);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (98,'Warranty','No-Charge Scheduled Maintenance - Number of Months','greaterthan',70,8);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (104,'Warranty','Vehicle Warranty - Distance','greaterthan',70,9);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (64,'Seating','Second Row Seating Type',null,80,1);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (61,'Seating','Front Seat with Height Adjustment','standard',80,2);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (63,'Seating','Seating Capacity','greaterthan',80,3);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (62,'Seating','Seating Surface Material',null,80,4);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (65,'Seating','Heated Front Seats','standard',80,5);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (24,'Engine Specs','Engine Displacement (cc)','greaterthan',90,1);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (29,'Engine Specs','Engine Stroke','greaterthan',90,2);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (28,'Engine Specs','Engine Valves',null,90,3);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (27,'Engine Specs','Engine Cylinders',null,90,4);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (26,'Engine Specs','Engine',null,90,5);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (25,'Engine Specs','Engine Compression Ratio','greaterthan',90,6);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (23,'Engine Specs','Engine Bore','greaterthan',90,7);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (86,'Audio','Satellite Radio','standard',100,1);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (83,'Audio','Sound System with AM/FM Radio','standard',100,2);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (84,'Audio','Single CD Player','standard',100,3);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (85,'Audio','Auxiliary Input Jack','standard',100,4);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (87,'Audio','Number of Speakers','greaterthan',100,5);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (107,'Residual Value','Year 2 Residual','greaterthan',110,1);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (109,'Residual Value','Year 4 Residual','greaterthan',110,2);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (106,'Residual Value','Year 1 Residual','greaterthan',110,3);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (110,'Residual Value','Year 5 Residual','greaterthan',110,4);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (108,'Residual Value','Year 3 Residual','greaterthan',110,5);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (20,'Weights/Capacities','Fuel Economy - Combined (city/hwy)','greaterthan',120,1);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (113,'Weights/Capacities','GVWR Max (lbs)',null,120,2);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (21,'Weights/Capacities','Curb Weight - Manual Transmission (lbs)','lessthan',120,3);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (116,'Weights/Capacities','Towing Capacity (std)','greaterthan',120,4);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (112,'Weights/Capacities','Curb Weight - Automatic Transmission (lbs)',null,120,5);
insert into `compare_datapoint_type`(`datapointid`,`datapointtype`,`datapointsubtype`,`valuetype`,`typeorder`,`subtypeorder`) values (22,'Weights/Capacities','Maximum Payload (lbs)',null,120,6);
