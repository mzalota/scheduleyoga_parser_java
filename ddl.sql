create table schedule( 
	id INT NOT NULL AUTO_INCREMENT, 	
 	studio_name VARCHAR(30), 
 	startTime DATETIME,
 	PRIMARY KEY(id));
 	
CREATE TABLE `events` (
  `id` int(11) NOT NULL auto_increment,
  `studio_id` int(11) default NULL,
  `start_time` datetime default NULL,
  `instructor_name` varchar(100) default NULL,
  `instructor_id` int(11) default NULL,
  `comments` varchar(100) default NULL,
  `created_on` datetime default NULL,
  `modified_on` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22196 DEFAULT CHARSET=latin1;
	
CREATE TABLE `events_styles` (
  `id` int(11) NOT NULL auto_increment,
  `event_id` int(11) default NULL,
  `name` varchar(100) default NULL,
  `created_on` datetime default NULL,
  `modified_on` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB;


create table events_staging (	
	id int not null auto_increment primary key, 
	studio_id int, 
	start_time datetime,
	instructor_name varchar(100), 
	comments varchar(100));
	
create table events_history (	
	id int not null auto_increment primary key, 
	studio_id int, 
	start_time datetime,
	instructor_name varchar(100), 
	comments varchar(100),
	created_on  datetime, 
	modified_on datetime, 
	archived_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
	);	
	
drop table schyoga1.studios;	
create table schyoga1.studios (	
	id int not null auto_increment primary key, 
	name varchar(100), 
	name_url varchar(100) unique,
	url_home varchar(255),
	url_schedule varchar(1024),	
	xpath varchar(1024),
	created_on  datetime, 
	modified_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP);		


drop table parsing_history;
create table parsing_history (	
	id int not null auto_increment primary key, 
	studio_id int, 
	last_crawling TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	calendar_html mediumtext
	);		
	
select * from parsing_history;	
	

CREATE TABLE instructors (
  `id` int(11) NOT NULL auto_increment,
  `instructor_name` varchar(150) NOT NULL,
  `name_url` varchar(150) unique NOT NULL,   
  `aliases` varchar(1000) default NULL,
  `created_on` datetime default NULL,
  `modified_on` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22196 DEFAULT CHARSET=latin1;


CREATE TABLE studios_instructors (
  `id` int(11) NOT NULL auto_increment,
  `studio_id` int(11) NOT NULL,
  `instructor_id` int(11) NOT NULL,
  `url` varchar(1024) default NULL,
  `created_on` datetime default NULL,
  `modified_on` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22196 DEFAULT CHARSET=latin1;

insert into schyoga1.studios (id, name, name_url, url_home, url_schedule, xpath, created_on) values 
(1, "Babtiste", "babtiste","http://www.baronbaptiste.com/", "https://clients.mindbodyonline.com/ASP/home.asp?studioid=1466", "/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[2]/td/table", NOW()),  
(2, "Kaia Yoga", "kaia-yoga", "http://kaiayoga.com/", "https://clients.mindbodyonline.com/ASP/home.asp?studioid=2148", "/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table", NOW()),
(6, "Om Yoga", "om-yoga", "http://www.omyoga.com", "http://www.omyoga.com/index.php?option=com_content&view=article&id=54&Itemid=104", "/html/body/div/div/div/div[6]/div[2]/div/div[2]/table/tbody/tr/td/table[2]/tbody/tr/td/table[2]", NOW());

//http://www.mindbodyonline.com/clients
insert into schyoga1.studios (name, name_url, url_home, url_schedule, xpath, created_on) values	
("Abhayayoga", "abhayayoga","http://www.abhayayoga.com/", "http://clients.mindbodyonline.com/ws.asp?studioid=10400","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW());


insert into schyoga1.studios (name, name_url, url_home, url_schedule, xpath, created_on) values	
("Ashtanga Yoga Upper West Side", "ashtanga-yoga-upper-west-side","http://www.ashtangayogaupperwestside.com/", "https://clients.mindbodyonline.com/ASP/home.asp?studioid=8603","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[2]/td/table",NOW());

insert into schyoga1.studios (name, name_url, url_home, url_schedule, xpath, created_on) values	
("Atmananda Yoga Sequence", "atmananda-yoga-sequence","http://www.atmananda.com/", "http://clients.mindbodyonline.com/ws.asp?studioid=1019","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW());

insert into schyoga1.studios (name, name_url, url_home, url_schedule, xpath, created_on) values	
("Bend & Bloom Yoga", "bend-and-bloom-yoga","http://www.bendandbloom.com/", "https://clients.mindbodyonline.com/ASP/home.asp?studioid=4186","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW());


insert into schyoga1.studios (name, name_url, url_home, url_schedule, xpath, created_on) values	
("Bikram Yoga Bay Ridge","bikram-yoga-bay-ridge","http://www.bikrambayridge.com/","http://clients.mindbodyonline.com/ws.asp?studioid=5545","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Bikram Yoga Grand Central","bikram-yoga-grand-central","http://www.bikramyogagrandcentral.com/","http://clients.mindbodyonline.com/ws.asp?studioid=11999","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Bikram Yoga Harlem","bikram-yoga-harlem","http://www.bikramyogaharlem.com/","http://clients.mindbodyonline.com/ws.asp?studioid=4003","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Bikram Yoga LBNY","bikram-yoga-lbny","http://www.bikramyogalbny.com/","http://clients.mindbodyonline.com/ws.asp?studioid=12407","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Bikram Yoga Union Square","bikram-yoga-union-square","http://bikramyogaunionsquare.com/","http://clients.mindbodyonline.com/ws.asp?studioid=6218","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Bikram Yoga Williamsburg","bikram-yoga-williamsburg","http://www.bikramwilliamsburg.com/","http://clients.mindbodyonline.com/ws.asp?studioid=6433","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Bodhisattva Yoga","bodhisattva-yoga","http://www.bodhisattvayoga.com/","http://clients.mindbodyonline.com/ws.asp?studioid=6044","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW());

insert into schyoga1.studios (name, name_url, url_home, url_schedule, xpath, created_on) values
("Golden Bridge NYC","golden-bridge-nyc","http://www.goldenbridgeyoga.com/","http://clients.mindbodyonline.com/ws.asp?studioid=1739","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[2]/td/table",NOW());

insert into schyoga1.studios (name, name_url, url_home, url_schedule, xpath, created_on) values
("Prana Power Yoga NYC & Brooklyn","prana-power-yoga-nyc-and-brooklyn","http://www.pranapoweryoga.com/","http://clients.mindbodyonline.com/ws.asp?studioid=1373","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[2]/td/table",NOW());


insert into schyoga1.studios (name, name_url, url_home, url_schedule, xpath, created_on) values
("Absolute Yoga & Wellness", "absolute-yoga-and-wellness","http://www.absoluteyoga.org/", "http://clients.mindbodyonline.com/ws.asp?studioid=13547","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW());

("Sivananda Yoga Centers NY (NYC)","sivananda-yoga-centers-nyc","http://www.sivananda.org/","http://clients.mindbodyonline.com/ws.asp?studioid=16473","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[2]/td/table",NOW()),
("Time For Yoga","time-for-yoga","http://timeforyoga.org/","http://clients.mindbodyonline.com/ws.asp?studioid=14739","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[5]/td/table",NOW()),
("Yoga to the People-Brooklyn","yoga-to-the-people-brooklyn","http://yogatothepeople.com/","http://clients.mindbodyonline.com/ws.asp?studioid=13193","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[2]/td/table",NOW()),

("Bread and Yoga","bread-and-yoga","http://www.breadandyoga.com/","http://clients.mindbodyonline.com/ws.asp?studioid=7450","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Center For Nia and Yoga","center-for-nia-and-yoga","http://www.nia-yoga.com/","http://clients.mindbodyonline.com/ws.asp?studioid=512","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Dharma Yoga Center","dharma-yoga-center","http://www.dharmayogacenter.com/","http://clients.mindbodyonline.com/ws.asp?studioid=3914","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Earth Yoga","earth-yoga","http://earthyoganyc.com/","http://clients.mindbodyonline.com/ws.asp?studioid=7399","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("East Yoga Center","east-yoga-center","http://www.eastyoga.com/","http://clients.mindbodyonline.com/ws.asp?studioid=703","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Elahi Yoga","elahi-yoga","http://www.elahiyoga.com/","http://clients.mindbodyonline.com/ws.asp?studioid=6238","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Excel Yoga Dance & Fitness","excel-yoga-dance-and-fitness","http://www.myexcelyoga.com/","http://clients.mindbodyonline.com/ws.asp?studioid=6702","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Finding Sukha Yoga School","finding-sukha-yoga-school","http://www.findingsukha.com/","http://clients.mindbodyonline.com/ws.asp?studioid=11155","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Go Yoga","go-yoga","http://www.goyoga.ws/","http://clients.mindbodyonline.com/ws.asp?studioid=9050","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Hosh Dou","hosh-dou","http://www.hoshyoga.com/home","http://clients.mindbodyonline.com/ws.asp?studioid=20811","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Hosh Yoga","hosh-yoga","http://hoshyoga.org/","http://clients.mindbodyonline.com/ws.asp?studioid=16844","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Hot Spot Yoga","hot-spot-yoga","http://brooklynshotspotyoga.blogspot.com/","http://clients.mindbodyonline.com/ws.asp?studioid=14535","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("ISHTA Yoga","ishta-yoga","http://www.ishtayoga.com/","http://clients.mindbodyonline.com/ws.asp?studioid=4101","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Iyengar Yoga Institute (NYC)","iyengar-yoga-institute-nyc","http://www.iyengarnyc.org/","http://clients.mindbodyonline.com/ws.asp?studioid=828","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Jai Yoga Arts","jai-yoga-arts","http://www.jaiyogaarts.com/","http://clients.mindbodyonline.com/ws.asp?studioid=15836","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Jaya Yoga Center","jaya-yoga-center","http://www.jayayogacenter.com/","http://clients.mindbodyonline.com/ws.asp?studioid=1222","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Karma Kids Yoga","karma-kids-yoga","http://www.karmakidsyoga.com/","http://clients.mindbodyonline.com/ws.asp?studioid=1075","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Keisha Bolden","keisha-bolden","http://www.thebodystylist.com/","http://clients.mindbodyonline.com/ws.asp?studioid=13747","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Kula Brooklyn","kula-brooklyn","http://www.kulayoga.com/","http://clients.mindbodyonline.com/ws.asp?studioid=12682","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Kula Yoga Project","kula-yoga-project","http://www.kulayoga.com/","http://clients.mindbodyonline.com/ws.asp?studioid=163","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Kundalini Yoga East","kundalini-yoga-east","http://www.kundaliniyogaeast.com/","http://clients.mindbodyonline.com/ws.asp?studioid=17","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Laughing Lotus","laughing-lotus","http://nyc.laughinglotus.com/","http://clients.mindbodyonline.com/ws.asp?studioid=137","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Living Room","living-room","http://www.livingroomsoho.com/","http://clients.mindbodyonline.com/ws.asp?studioid=20578","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Loom Studios","loom-studios","http://www.loomstudios.com/","http://clients.mindbodyonline.com/ws.asp?studioid=17650","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("MangOh Yoga","mangoh-yoga","http://www.mangohstudio.com/","http://clients.mindbodyonline.com/ws.asp?studioid=877","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("MindBodySoulYoga","mindbodysoulyoga","http://www.mindbodysoulyoga.com/","http://clients.mindbodyonline.com/ws.asp?studioid=6569","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Om Yoga Center","om-yoga-center","http://www.omyoga.com/","http://clients.mindbodyonline.com/ws.asp?studioid=-94","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Prenatal Yoga Center","prenatal-yoga-center","http://www.prenatalyogacenter.com/","http://clients.mindbodyonline.com/ws.asp?studioid=15689","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("PURE Yoga New York","pure-yoga-new-york","http://www.pureyoga.com/","http://clients.mindbodyonline.com/ws.asp?studioid=11160","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Reflections Yoga","reflections-yoga","http://www.reflectionsyoga.com/","http://clients.mindbodyonline.com/ws.asp?studioid=655","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Sangha Yoga Shala","sangha-yoga-shala","http://www.sanghayoganyc.com/","http://clients.mindbodyonline.com/ws.asp?studioid=5782","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Sankalpah Yoga","sankalpah-yoga","http://www.sankalpah.com/","http://clients.mindbodyonline.com/ws.asp?studioid=3164","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Sapere Studio","sapere-studio","http://www.saperestudio.com/","http://clients.mindbodyonline.com/ws.asp?studioid=13228","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Shambhala Yoga & Dance Center","shambhala-yoga-and-dance-center","http://www.shambhalayogadance.com/","http://clients.mindbodyonline.com/ws.asp?studioid=15409","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Shanti Baby Yoga","shanti-baby-yoga","http://www.shantibaby.com/","http://clients.mindbodyonline.com/ws.asp?studioid=17682","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Sonic Yoga","sonic-yoga","http://www.sonicyoga.com/","http://clients.mindbodyonline.com/ws.asp?studioid=88","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Studio 5 Yoga and Pilates","studio-5-yoga-and-pilates","http://www.studio5yogaandpilates.com","http://clients.mindbodyonline.com/ws.asp?studioid=14474","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Studio Anya","studio-anya","http://studioanya.com/","http://clients.mindbodyonline.com/ws.asp?studioid=8504","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("The Green Spa & Wellness Center","the-green-spa-and-wellness-center","http://www.greenspany.com/","http://clients.mindbodyonline.com/ws.asp?studioid=9558","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("The Shala Yoga House","the-shala-yoga-house","http://www.theshala.com/","http://clients.mindbodyonline.com/ws.asp?studioid=3571","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("The Yoga Collective","the-yoga-collective","http://yogacollectivenyc.com/","http://clients.mindbodyonline.com/ws.asp?studioid=18182","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("The Yoga Room","the-yoga-room","http://www.the-yoga-room.com/","http://clients.mindbodyonline.com/ws.asp?studioid=1261","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("usha veda yoga","usha-veda-yoga","http://ushavedayoga.com/","http://clients.mindbodyonline.com/ws.asp?studioid=11253","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Yoga High","yoga-high","http://www.yogahighnyc.com/","http://clients.mindbodyonline.com/ws.asp?studioid=3967","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Yoga Longbeach","yoga-longbeach","http://www.yogalongbeach.com/","http://clients.mindbodyonline.com/ws.asp?studioid=14209","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Yoga Mountain","yoga-mountain","http://www.yogamountain.com/","http://clients.mindbodyonline.com/ws.asp?studioid=3636","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Yoga People","yoga-people","http://www.yoga-people.com/","http://clients.mindbodyonline.com/ws.asp?studioid=5599","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Yoga Sutra NYC","yoga-sutra-nyc","http://www.yogasutranyc.com/","http://clients.mindbodyonline.com/ws.asp?studioid=759","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Yoga Union","yoga-union","http://www.yogaunion.com/","http://clients.mindbodyonline.com/ws.asp?studioid=20701","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Yoga Union Center For Backcare & Scoliosis","yoga-union-center-for-backcare-and-scoliosis","http://yogaunionbackcare.com/","http://clients.mindbodyonline.com/ws.asp?studioid=14218","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Yoga Vida NYC","yoga-vida-nyc","http://www.yogavidanyc.com/","http://clients.mindbodyonline.com/ws.asp?studioid=8521","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Yogamaya","yogamaya","http://www.yogamayanewyork.com/","http://clients.mindbodyonline.com/ws.asp?studioid=10761","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Yoganesh","yoganesh","http://www.yoganesh.com/","http://clients.mindbodyonline.com/ws.asp?studioid=13208","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Yogasole","yogasole","http://yogasole.com/","http://clients.mindbodyonline.com/ws.asp?studioid=9882","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),


//TODO ADD THESE studios to the DB

//problem, the page shows only todays classes
insert into schyoga1.studios (name, name_url, url_home, url_schedule, xpath, created_on) values
("Bikram Yoga NYC","bikram-yoga-nyc","http://www.bikramyoganyc.com/","http://clients.mindbodyonline.com/ws.asp?studioid=-71","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[4]/td/table",NOW());
("New York Yoga Studio","new-york-yoga-studio","http://www.newyorkyoga.com/","http://clients.mindbodyonline.com/ws.asp?studioid=1034","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("YogaWorks-NYC","yogaworks-nyc","http://www.yogaworks.com/","http://clients.mindbodyonline.com/ws.asp?studioid=-140","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW());

//problem, the page does not show classes
("Bikram Yoga Manhattan","bikram-yoga-manhattan","http://www.bikramyogamanhattan.com/","http://clients.mindbodyonline.com/ws.asp?studioid=869","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Integral Yoga Institute","integral-yoga-institute","http://www.iyiny.org/","http://clients.mindbodyonline.com/ws.asp?studioid=8633","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Buddha Body Yoga TM","buddha-body-yoga-tm","http://www.buddhabodyoga.com/","http://clients.mindbodyonline.com/ws.asp?studioid=19232","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),

//Different layout
("Mala Yoga","mala-yoga","http://www.malayoganyc.com/","http://clients.mindbodyonline.com/ws.asp?studioid=9495","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),


//Empty Schedule
("Caravan of Yogis","caravan-of-yogis","http://www.caravanofyogis.com/","http://clients.mindbodyonline.com/ws.asp?studioid=8697","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Global Sound & Body","global-sound-and-body","http://www.globalsoundandbody.com/","http://clients.mindbodyonline.com/ws.asp?studioid=13485","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Liza Laird","liza-laird","http://www.lizalaird.com/","http://clients.mindbodyonline.com/ws.asp?studioid=7103","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Moksha Yoga NYC","moksha-yoga-nyc","http://www.mokshayoganyc.com/","http://clients.mindbodyonline.com/ws.asp?studioid=17613","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Prana Mandir Yoga Studio","prana-mandir-yoga-studio","http://www.pranamandir.com/","http://clients.mindbodyonline.com/ws.asp?studioid=3185","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Sacred Brooklyn","sacred-brooklyn","http://www.sacredbrooklyn.com/","http://clients.mindbodyonline.com/ws.asp?studioid=14875","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("The Standard Hotel, New York (Yoga)","the-standard-hotel-new-york-yoga","http://standardhotel.com/","http://clients.mindbodyonline.com/ws.asp?studioid=9829","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Yoga Bliss NYC","yoga-bliss-nyc","http://www.yogablissnyc.com/","http://clients.mindbodyonline.com/ws.asp?studioid=4215","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW()),
("Art of Movement", "art-of-movement","http://www.artofmovement.net/", "http://clients.mindbodyonline.com/ws.asp?studioid=785","/html/body/div[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td/table",NOW());

Wrong columns being read for Shatnti Baby Yoga http://www.shantibaby.com/ https://clients.mindbodyonline.com/ASP/home.asp?studioid=17682





select * from schyoga1.studios;

select * from schyoga1.events;
