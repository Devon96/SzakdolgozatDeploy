INSERT INTO `continent` (`id`,`name`) VALUES (1,'Európa');
INSERT INTO `continent` (`id`,`name`) VALUES (2,'Ázsia');
INSERT INTO `continent` (`id`,`name`) VALUES (3,'Amerika');
INSERT INTO `continent` (`id`,`name`) VALUES (4,'Afrika');
INSERT INTO `continent` (`id`,`name`) VALUES (5,'Ausztrália');


INSERT INTO `country` (`id`,`description`,`name`,`continent_id`) VALUES ('magyarorszag','Magyarország állam Közép-Európában, a Kárpát-medence közepén, amely 1989 óta független parlamentáris köztársaság. Északról Szlovákia, északkeletről Ukrajna, keletről és délkeletről Románia, délről Szerbia és Horvátország, délnyugatról Szlovénia, nyugatról pedig Ausztria határolja. Területe 93 030 négyzetkilométer, népessége pedig közel tízmillió fő, így az Európai Unió közepes méretű és közepes népességű tagállamai közé tartozik. Hivatalos nyelve a magyar, ami a legnagyobb az uráli nyelvcsaládba tartozó nyelvek közül. Fővárosa és legnépesebb városa Budapest, amely világvárosnak számít.','Magyarország',1);
INSERT INTO `country` (`id`,`description`,`name`,`continent_id`) VALUES ('franciaorszag','Az idei programkínálat gazdagsága is jelzi, hogy Franciaország a világ legkedveltebb célpontjaként méltán őrizte meg vezető pozícióját Spanyolország és az Egyesült Államok előtt. Csak Párizsba közel 40 millió vendég érkezett 2018-ban, közülük 13 millióan külföldről, ez 11%-os növekedést jelent. Az USA-ból jöttek a legtöbben, őket a britek, a németek és a kínaiak követik. A Louvre látogatóinak száma is elképesztő rekordot döntött tavaly: 25%-os növekedéssel fennállása óta először haladta meg a 10 milliót. A Francia Idegenforgalmi Hivatal február 27-ei Focus on France rendezvényén elhangzott, hogy a zavargások hatása egyre kevésbé érződik, az utazásszervezők világszerte bizalommal tekintenek az országra. Ezt már a 2019-es téli szezon foglalási mutatói is megerősítik: a január eleji adatok 12%-os növekedését mutatnak az előző év azonos időszakához képest.','Franciaország',1);
INSERT INTO `country` (`id`,`description`,`name`,`continent_id`) VALUES ('hollandia','Fedezze fel Hollandia igazi arcát és tapasztalja meg mit rejt a holland kultúra. Ámuljon el a vibrálóan színes tulipánföldek látványán, kóstoljon meg díjnyertes sajtokat, pihenjen a várost átszelő csatornákon hajókázva, vagy fedezze fel a bájos, sík vidéket úgy, ahogy azt a helyiek is teszik – kerékpárral.','Hollandia',1);
INSERT INTO `country` (`id`,`description`,`name`,`continent_id`) VALUES ('spanyolorszag','Spanyolország a föld egyik legsokszínűbb országa. A turisták által kevésbé ismert, vadregényes észak, Baszkföld, Kantábria, Asztúria, Galícia tartományai középkori városokban, vallási emlékekben és csodás természeti látnivalókban egyaránt bővelkednek. A spanyol nagyvárosok forgataga megunhatatlan, a művészetek tobzódásával. Barcelona Gaudí és Picasso városa, Madrid a világ egyik legnagyobb szépművészeti múzeumának, a Pradónak otthona. Andalúzia, a bikaviadalok, a flamenco és a sherry hazája, Granada és Córdoba csodáival és mór emlékeivel egy külön világ. Sevilla nemcsak híres művészek (például Murillo, Velázquez) szülővárosa, számos világhírű opera cselekményének helyszíne is. Spanyolország szigetei mesés szépségűek.','Spanyolország',1);


INSERT INTO `city` (`id`,`description`,`name`,`country_id`) VALUES (1,'Budapest Magyarország fővárosa, az ország politikai, művelődési, ipari, kereskedelmi és közlekedési központja, valamint nevezetes gyógyfürdőváros. Az Európai Unió kilencedik legnépesebb városa, 1 702 000 fővel. A Duna két partján, az Alföld és a dombvidékek találkozásánál fekszik.','Budapest','magyarorszag');
INSERT INTO `city` (`id`,`description`,`name`,`country_id`) VALUES (2,'Szeged környéke az őskortól lakott hely, az Öthalom területén bukkantak a legrégibb emberi településnyomokra. A II. század közepéről, Ptolemaiosz leírásából ismerjük a település legrégibb nevét: Partiszkon. A későbbi vár helyén római őrállomás vigyázta a tiszai átkelőhelyet, a Maros menti só- és aranyszállítást. A nagyszéksósi hun fejedelmi lelet és számos történeti adat valószínűsíti, hogy Attila székhelye a környéken lehetett.','Szeged','magyarorszag');
INSERT INTO `city` (`id`,`description`,`name`,`country_id`) VALUES (3,'Pécs az ország dél-nyugati szélén, a horvát határ közelében található. Délebbi fele sík, míg az északi része a Mecsek hegység déli lankáira kúszik fel.\r\n\r\nTalán a rómaiak, akik a mediterránumból jöttek, nem véletlenül érezték magukat otthon ezen a vidéken, s bizonyára így vannak ezzel ma is Itáliából érkező vendégeink. Valóban: van-e olaszosabb városunk Pécsnél?','Pécs','magyarorszag');
INSERT INTO `city` (`id`,`description`,`name`,`country_id`) VALUES (4,'Debrecen (latinul: Debretinum, németül: Debrezin, szlovákul: Debrecín, románul: Debrețin, lengyelül: Debreczyn) Magyarország harmadik legnagyobb területű és második legnépesebb települése, Hajdú-Bihar megye és a Debreceni járás székhelye, megyei jogú város. A megye lakosságának mintegy 38,2%-a él itt. Debrecen a Tiszántúl legnagyobb városa. Időnként „a kálvinista Róma” néven vagy „cívisváros”-ként emlegetik.','Debrecen','magyarorszag');
INSERT INTO `city` (`id`,`description`,`name`,`country_id`) VALUES (6,'Győr (latinul Arrabona, Jaurinum, németül: Raab, horvátul: Jura, Đura) egy megyei jogú város Magyarországon, Nyugat-Dunántúl régió központja, Győr-Moson-Sopron megye és a Győri járás székhelye. Jelentős gazdasági, kulturális, egyetemi és sportközpont, az ország egyik legdinamikusabban fejlődő városa. A Bécs–Pozsony–Budapest innovatív tengelyen fekszik, kiváló közlekedési adottságokkal rendelkezik. Magyarország műemlékekben harmadik leggazdagabb városaként a barokk belváros rekonstrukciójának elismeréséül 1989-ben elnyerte a műemlékvédelmi Europa Nostra-díjat.','Győr','magyarorszag');


INSERT INTO `type` (`id`,`name`) VALUES (1,'MÚZEUM');

INSERT INTO `label` (`id`,`name`) VALUES (1,'nemzet');
INSERT INTO `label` (`id`,`name`) VALUES (2,'történelem');
INSERT INTO `label` (`id`,`name`) VALUES (3,'nemzeti');
INSERT INTO `label` (`id`,`name`) VALUES (4,'korona');
INSERT INTO `label` (`id`,`name`) VALUES (5,'kgb');

INSERT INTO `attraction` (`id`,`address`,`description`,`name`,`visit_time`,`website`,`city_id`,`type_id`) VALUES (1,'Múzeum krt. 14-16','1802. november 25-én gróf Széchényi Ferenc úgy döntött, hogy gazdag gyűjteményeit „édes hazámnak és a közösségnek hasznára és javára mindörökre és visszavonhatatlanul adományozom, átadom és átruházom”. A nagylelkű adományt uralkodói jóváhagyás erősítette meg. Bár az új intézmény az alapítólevélben változó nevekkel szerepelt (Nemzeti Könyvtár, Országos Múzeum, Nemzeti Múzeum), a kor fogalmai szerint Széchényi gróf a magyar nemzeti könyvtárat alapította meg – ám kezdettől fogva azzal a lehetőséggel, amit a nemes gesztusért köszönetet mondó 1807. évi 24. törvény így fogalmazott meg: az adományozó gróf „terjedelmes és válogatott könyvtárát, valamint kiváló gondossággal és költekezéssel gyüjtött ritka pénzérmeit s jeles családok czimereit, ugyszintén földabroszait, képeit és kéziratait a magyar nemzet használatára, teljes joggal, átirta és ezekkel egy fölállitandó nemzeti muzeum alapjait dicséretes buzgósággal lerakta”.','Magyar Nemzeti Múzeum',4,'mnm.hu',1,1);

INSERT INTO `attraction_labels` (`attraction_id`,`label_id`) VALUES (1,1);
INSERT INTO `attraction_labels` (`attraction_id`,`label_id`) VALUES (1,2);
INSERT INTO `attraction_labels` (`attraction_id`,`label_id`) VALUES (1,3);
INSERT INTO `attraction_labels` (`attraction_id`,`label_id`) VALUES (1,4);