(ns clj-etl-utils.ref-data)

(def *us-states*
     (partition 2
                ["AL" "ALABAMA"
                 "AK" "ALASKA"
                 "AS" "AMERICAN SAMOA"
                 "AZ" "ARIZONA"
                 "AR" "ARKANSAS"
                 "CA" "CALIFORNIA"
                 "CO" "COLORADO"
                 "CT" "CONNECTICUT"
                 "DE" "DELAWARE"
                 "DC" "DISTRICT OF COLUMBIA"
                 "FM" "FEDERATED STATES OF MICRONESIA"
                 "FL" "FLORIDA"
                 "GA" "GEORGIA"
                 "GU" "GUAM"
                 "HI" "HAWAII"
                 "ID" "IDAHO"
                 "IL" "ILLINOIS"
                 "IN" "INDIANA"
                 "IA" "IOWA"
                 "KS" "KANSAS"
                 "KY" "KENTUCKY"
                 "LA" "LOUISIANA"
                 "ME" "MAINE"
                 "MH" "MARSHALL ISLANDS"
                 "MD" "MARYLAND"
                 "MA" "MASSACHUSETTS"
                 "MI" "MICHIGAN"
                 "MN" "MINNESOTA"
                 "MS" "MISSISSIPPI"
                 "MO" "MISSOURI"
                 "MT" "MONTANA"
                 "NE" "NEBRASKA"
                 "NV" "NEVADA"
                 "NH" "NEW HAMPSHIRE"
                 "NJ" "NEW JERSEY"
                 "NM" "NEW MEXICO"
                 "NY" "NEW YORK"
                 "NC" "NORTH CAROLINA"
                 "ND" "NORTH DAKOTA"
                 "MP" "NORTHERN MARIANA ISLANDS"
                 "OH" "OHIO"
                 "OK" "OKLAHOMA"
                 "OR" "OREGON"
                 "PW" "PALAU"
                 "PA" "PENNSYLVANIA"
                 "PR" "PUERTO RICO"
                 "RI" "RHODE ISLAND"
                 "SC" "SOUTH CAROLINA"
                 "SD" "SOUTH DAKOTA"
                 "TN" "TENNESSEE"
                 "TX" "TEXAS"
                 "UT" "UTAH"
                 "VT" "VERMONT"
                 "VI" "VIRGIN ISLANDS"
                 "VA" "VIRGINIA"
                 "WA" "WASHINGTON"
                 "WV" "WEST VIRGINIA"
                 "WI" "WISCONSIN"
                 "WY" "WYOMING"
                 "AE" "Armed Forces Africa"
                 "AA" "Armed Forces Americas"
                 "AE" "Armed Forces Europe"
                 "AE" "Armed Forces Middle East"
                 "AP" "Armed Forces Pacific"]))

(def *us-airport-codes*
     (partition
      3
      ["SD" "Aberdeen" "ABR"
       "TX" "Abilene" "ABI"
       "AK" "Adak Island" "ADK"
       "AK" "Akiachak" "KKI"
       "AK" "Akiak" "AKI"
       "OH" "Akron/Canton" "CAK"
       "AK" "Akuton" "KQA"
       "AK" "Alakanuk" "AUK"
       "NM" "Alamogordo" "ALM"
       "CO" "Alamosa" "ALS"
       "NY" "Albany" "ALB"
       "OR - Bus service" "Albany" "CVO"
       "OR - Bus service" "Albany" "QWY"
       "NM" "Albuquerque" "ABQ"
       "AK" "Aleknagik" "WKK"
       "LA" "Alexandria" "AEX"
       "AK" "Allakaket" "AET"
       "PA" "Allentown" "ABE"
       "NE" "Alliance" "AIA"
       "MI" "Alpena" "APN"
       "PA" "Altoona" "AOO"
       "TX" "Amarillo" "AMA"
       "AK" "Ambler" "ABL"
       "AK" "Anaktueuk" "AKP"
       "AK" "Anchorage" "ANC"
       "AK" "Angoon" "AGN"
       "AK" "Aniak" "ANI"
       "AK" "Anvik" "ANV"
       "WI" "Appleton" "ATW"
       "CA" "Arcata" "ACV"
       "AK" "Arctic Village" "ARC"
       "NC" "Asheville" "AVL"
       "KY/Huntington, WV" "Ashland" "HTS"
       "CO" "Aspen" "ASE"
       "GA" "Athens" "AHN"
       "AK" "Atka" "AKB"
       "GA" "Atlanta" "ATL"
       "NJ" "Atlantic City" "AIY"
       "AK" "Atqasuk" "ATK"
       "GA" "Augusta" "AGS"
       "ME" "Augusta" "AUG"
       "TX" "Austin" "AUS"
       "CA" "Bakersfield" "BFL"
       "MD" "Baltimore" "BWI"
       "ME" "Bangor" "BGR"
       "ME" "Bar Harbour" "BHB"
       "AK" "Barrow" "BRW"
       "AK" "Barter Island" "BTI"
       "LA" "Baton Rouge" "BTR"
       "MI" "Bay City" "MBS"
       "TX" "Beaumont/Port Arthur" "BPT"
       "CO - Van service" "Beaver Creek" "ZBV"
       "AK" "Beaver" "WBQ"
       "WV" "Beckley" "BKW"
       "MA" "Bedford" "BED"
       "IL" "Belleville" "BLV"
       "WA" "Bellingham" "BLI"
       "MN" "Bemidji" "BJI"
       "MI" "Benton Harbor" "BEH"
       "AK" "Bethel" "BET"
       "PA" "Bethlehem" "ABE"
       "AK" "Bettles" "BTT"
       "MT" "Billings" "BIL"
       "MS" "Biloxi/Gulfport" "GPT"
       "NY" "Binghamton" "BGM"
       "AK" "Birch Creek" "KBC"
       "AL" "Birmingham" "BHM"
       "ND" "Bismarck" "BIS"
       "RI" "Block Island" "BID"
       "IL" "Bloomington" "BMI"
       "WV" "Bluefield" "BLF"
       "ID" "Boise" "BOI"
       "MA" "Boston" "BOS"
       "CO - Bus service" "Boulder" "XHH"
       "CO - Hiltons Har H" "Boulder" "WHH"
       "CO - Municipal Airport" "Boulder" "WBU"
       "AK" "Boundary" "BYA"
       "KY" "Bowling Green" "BWG"
       "MT" "Bozeman" "BZN"
       "PA" "Bradford" "BFD"
       "MN" "Brainerd" "BRD"
       "TX" "Brawnwood" "BWD"
       "CO - Van service" "Breckonridge" "QKB"
       "VA" "Bristol" "TRI"
       "SD" "Brookings" "BKX"
       "AK" "Brooks Lodge" "RBH"
       "TX" "Brownsville" "BRO"
       "GA" "Brunswick" "BQK"
       "AK" "Buckland" "BKC"
       "NY" "Buffalo" "BUF"
       "AZ" "Bullhead City/Laughlin" "IFP"
       "CA" "Burbank" "BUR"
       "IA" "Burlington" "BRL"
       "VT" "Burlington" "BTV"
       "MT" "Butte" "BTM"
       "OH" "Canton/Akron" "CAK"
       "MO" "Cape Girardeau" "CGI"
       "AK" "Cape Lisburne" "LUR"
       "AK" "Cape Newenham" "EHM"
       "IL" "Carbondale" "MDH"
       "CA" "Carlsbad" "CLD"
       "NM" "Carlsbad" "CNM"
       "CA" "Carmel" "MRY"
       "WY" "Casper" "CPR"
       "UT" "Cedar City" "CDC"
       "IA" "Cedar Rapids" "CID"
       "AK" "Central" "CEM"
       "NE" "Chadron" "CDR"
       "AK" "Chalkyitsik" "CIK"
       "IL" "Champaign/Urbana" "CMI"
       "SC" "Charleston" "CHS"
       "WV" "Charleston" "CRW"
       "NC" "Charlotte" "CLT"
       "VA" "Charlottesville" "CHO"
       "TN" "Chattanooga" "CHA"
       "AK" "Chefornak" "CYF"
       "AK" "Chevak" "VAK"
       "WY" "Cheyenne" "CYS"
       "IL - Meigs" "Chicago" "CGX"
       "IL - All airports" "Chicago" "CHI"
       "IL - Midway" "Chicago" "MDW"
       "IL - O'Hare" "Chicago" "ORD"
       "AK" "Chicken" "CKX"
       "CA" "Chico" "CIC"
       "AK - Fisheries" "Chignik" "KCG"
       "AK -" "Chignik" "KCQ"
       "AK - Lagoon" "Chignik" "KCL"
       "AK" "Chisana" "CZN"
       "MN" "Chisholm/Hibbing" "HIB"
       "AK" "Chuathbaluk" "CHU"
       "OH" "Cincinnati" "CVG"
       "AK" "Circle Hot Springs" "CHP"
       "AK" "Circle" "IRC"
       "AK" "Clarks Point" "CLP"
       "WV" "Clarksburg" "CKB"
       "FL" "Clearwater/St Petersburg" "PIE"
       "OH" "Cleveland" "CLE"
       "NM" "Clovis" "CVN"
       "WY" "Cody/Yellowstone" "COD"
       "AK" "Coffee Point" "CFA"
       "AK" "Coffman Cove" "KCC"
       "AK" "Cold Bay" "CDB"
       "TX" "College Station" "CLL"
       "CO" "Colorado Springs" "COS"
       "MO" "Columbia" "COU"
       "SC" "Columbia" "CAE"
       "GA" "Columbus" "CSG"
       "MS" "Columbus" "GTR"
       "OH" "Columbus" "CMH"
       "CA" "Concord" "CCR"
       "KS" "Concordia" "CNK"
       "CO - Van service" "Copper Mountain" "QCE"
       "AK" "Cordova" "CDV"
       "TX" "Corpus Christi" "CRP"
       "CO" "Cortez" "CEZ"
       "AK" "Craig" "CGA"
       "CA" "Crescent City" "CEC"
       "AK" "Crooked Creek" "CKO"
       "AK" "Cube Cove" "CUW"
       "MD" "Cumberland" "CBE"
       "TX" "Dallas/Fort Worth" "DFW"
       "OH" "Dayton" "DAY"
       "FL" "Daytona Beach" "DAB"
       "IL" "Decatur" "DEC"
       "AK" "Deering" "DRG"
       "AK" "Delta Junction" "DJN"
       "CO - International" "Denver" "DEN"
       "CO - Longmont Bus service" "Denver" "QWM"
       "IA" "Des Moines" "DSM"
       "MI - All airports" "Detroit" "DTT"
       "MI - Metro/Wayne County" "Detroit" "DTW"
       "ND" "Devil's Lake" "DVL"
       "ND" "Dickinson" "DIK"
       "AK" "Dillingham" "DLG"
       "KS" "Dodge City" "DDC"
       "AL" "Dothan" "DHN"
       "PA" "Dubois" "DUJ"
       "IA" "Dubuque" "DBQ"
       "MN" "Duluth" "DLH"
       "CO" "Durango" "DRO"
       "NC" "Durham" "RDU"
       "NC" "Durham/Raleigh" "RDU"
       "AK" "Dutch Harbor" "DUT"
       "PA" "Easton" "ABE"
       "WI" "Eau Claire" "EAU"
       "AK" "Edna Bay" "EDA"
       "AK" "Eek" "EEK"
       "AK" "Ekuk" "KKU"
       "AK" "Ekwok" "KEK"
       "CA" "El Centro" "IPL"
       "AR" "El Dorado" "ELD"
       "TX" "El Paso" "ELP"
       "AK" "Elfin Cove" "ELV"
       "AK" "Elim" "ELI"
       "NV" "Elko" "EKO"
       "NY" "Elmira" "ELM"
       "MN" "Ely" "LYU"
       "AK" "Emmonak" "EMK"
       "NY" "Endicott" "BGM"
       "OK" "Enid" "WDG"
       "PA" "Erie" "ERI"
       "MI" "Escanaba" "ESC"
       "OR" "Eugene" "EUG"
       "CA" "Eureka/Arcata" "ACV"
       "NV" "Eureka" "EUE"
       "IN" "Evansville" "EVV"
       "AK" "Fairbanks" "FAI"
       "ND" "Fargo" "FAR"
       "AR - Municipal/Drake" "Fayetteville" "FYV"
       "AR - Northwest Arkansas Regional" "Fayetteville" "XNA"
       "NC" "Fayetteville" "FAY"
       "AZ" "Flagstaff" "FLG"
       "MI" "Flint" "FNT"
       "SC" "Florence" "FLO"
       "AL" "Florence/Muscle Shoals/Sheffield" "MSL"
       "CO - Municipal Airport" "Fort Collins/Loveland" "FNL"
       "CO - Bus service" "Fort Collins/Loveland" "QWF"
       "IA" "Fort Dodge" "FOD"
       "FL" "Fort Lauderdale" "FLL"
       "MO" "Fort Leonard Wood" "TBN"
       "FL" "Fort Myers" "RSW"
       "AR" "Fort Smith" "FSM"
       "FL" "Fort Walton Beach" "VPS"
       "IN" "Fort Wayne" "FWA"
       "TX" "Fort Worth/Dallas" "DFW"
       "PA" "Franklin" "FKL"
       "CA" "Fresno" "FAT"
       "FL" "Gainesville" "GNV"
       "NM" "Gallup" "GUP"
       "KS" "Garden City" "GCK"
       "IN" "Gary" "GYY"
       "WY" "Gillette" "GCC"
       "TX" "Gladewater/Kilgore" "GGG"
       "MT" "Glasgow" "GGW"
       "MT" "Glendive" "GDV"
       "AK" "Golovin" "GLV"
       "AK" "Goodnews Bay" "GNU"
       "AZ - Heliport" "Grand Canyon" "JGC"
       "AZ - National Park" "Grand Canyon" "GCN"
       "ND" "Grand Forks" "GFK"
       "NE" "Grand Island" "GRI"
       "CO" "Grand Junction" "GJT"
       "MI" "Grand Rapids" "GRR"
       "MN" "Grand Rapids" "GPZ"
       "AK" "Grayling" "KGX"
       "MT" "Great Falls" "GTF"
       "WI" "Green Bay" "GRB"
       "NC" "Greensboro" "GSO"
       "MS" "Greenville" "GLH"
       "NC" "Greenville" "PGV"
       "SC" "Greenville/Spartanburg" "GSP"
       "CT" "Groton/New London" "GON"
       "MS" "Gulfport" "GPT"
       "CO" "Gunnison" "GUC"
       "AK" "Gustavus" "GST"
       "MD" "Hagerstown" "HGR"
       "ID" "Hailey" "SUN"
       "AK" "Haines" "HNS"
       "VA" "Hampton" "PHF"
       "HI - Island of Maui" "Hana" "HNM"
       "HI" "Hanapepe" "PAK"
       "MI" "Hancock" "CMX"
       "NH" "Hanover" "LEB"
       "TX" "Harlingen" "HRL"
       "PA" "Harrisburg" "MDT"
       "AR" "Harrison" "HRO"
       "CT" "Hartford" "BDL"
       "AZ" "Havasupai" "HAE"
       "MT" "Havre" "HVR"
       "CO" "Hayden" "HDN"
       "KS" "Hays" "HYS"
       "AK" "Healy Lake" "HKB"
       "MT" "Helena" "HLN"
       "NC" "Hendersonville" "AVL"
       "MN" "Hibbing/Chisholm" "HIB"
       "NC" "Hickory" "HKY"
       "NC" "High Point" "GSO"
       "HI - Island of Hawaii" "Hilo" "ITO"
       "SC" "Hilton Head" "HHH"
       "NM" "Hobbs" "HBB"
       "AK" "Hollis" "HYL"
       "AK" "Holy Cross" "HCR"
       "AK" "Homer" "HOM"
       "HI - Island of Oahu" "Honolulu" "HNL"
       "HI - Island of Molokai" "Hoolehua" "MKK"
       "AK" "Hoonah" "HNH"
       "AK" "Hooper Bay" "HPB"
       "AR" "Hot Springs" "HOT"
       "TX - All airports" "Houston" "HOU"
       "TX - Hobby" "Houston" "HOU"
       "TX - Intercontinental" "Houston" "IAH"
       "AK" "Hughes" "HUS"
       "WV/Ashland, KY" "Huntington" "HTS"
       "AL" "Huntsville" "HSV"
       "SD" "Huron" "HON"
       "AK" "Huslia" "HSL"
       "MA" "Hyannis" "HYA"
       "AK" "Hydaburg" "HYG"
       "ID" "Idaho Falls" "IDA"
       "AK" "Igiugig" "IGG"
       "AK" "Iliamna" "ILI"
       "CA" "Imperial" "IPL"
       "IN" "Indianapolis" "IND"
       "MN" "International Falls" "INL"
       "CA" "Inyokern" "IYK"
       "MI" "Iron Mountain" "IMT"
       "MI" "Ironwood" "IWD"
       "NY" "Islip" "ISP"
       "NY" "Ithaca" "ITH"
       "WY" "Jackson Hole" "JAC"
       "MS" "Jackson" "JAN"
       "TN" "Jackson" "MKL"
       "FL" "Jacksonville" "JAX"
       "NC" "Jacksonville" "OAJ"
       "ND" "Jamestown" "JMS"
       "NY" "Jamestown" "JHW"
       "WI" "Janesville" "JVL"
       "NY" "Johnson City" "BGM"
       "TN" "Johnson City" "TRI"
       "PA" "Johnstown" "JST"
       "AR" "Jonesboro" "JBR"
       "MO" "Joplin" "JLN"
       "AK" "Juneau" "JNU"
       "HI - Island of Maui," "Kahului" "OGG"
       "AK" "Kake" "KAE"
       "AK" "Kakhonak" "KNK"
       "MI" "Kalamazoo" "AZO"
       "HI - Island of Molokai," "Kalaupapa" "LUP"
       "AK" "Kalskag" "KLG"
       "AK" "Kaltag" "KAL"
       "HI - Island of Hawaii," "Kamuela" "MUE"
       "MO" "Kansas City" "MCI"
       "HI - Island of Maui," "Kapalua" "JHM"
       "AK" "Kasaan" "KXA"
       "AK" "Kasigluk" "KUK"
       "HI" "Kauai Island/Lihue" "LIH"
       "NE" "Kearney" "EAR"
       "NH" "Keene" "EEN"
       "AK" "Kenai" "ENA"
       "AK" "Ketchikan" "KTN"
       "FL" "Key West" "EYW"
       "CO - Van service" "Keystone" "QKS"
       "AK" "Kiana" "IAN"
       "TX" "Kilgore/Gladewater" "GGG"
       "TX" "Killeen" "ILE"
       "AK" "King Cove" "KVC"
       "AK" "King Salmon" "AKN"
       "AZ" "Kingman" "IGM"
       "TN" "Kingsport" "TRI"
       "AK" "Kipnuk" "KPN"
       "MO" "Kirksville" "IRK"
       "AK" "Kivalina" "KVL"
       "OR" "Klamath Falls" "LMT"
       "AK" "Klawock" "KLW"
       "TN" "Knoxville" "TYS"
       "AK" "Kobuk" "OBU"
       "AK" "Kodiak" "ADQ"
       "HI - Island of Hawaii" "Kona" "KOA"
       "AK" "Kongiganak" "KKH"
       "AK" "Kotlik" "KOT"
       "AK" "Kotzebue" "OTZ"
       "AK" "Koyukuk" "KYU"
       "AK" "Kwethluk" "KWT"
       "AK" "Kwigillingok" "KWK"
       "WI" "La Crosse" "LSE"
       "IN" "Lafayette" "LAF"
       "LA" "Lafayette" "LFT"
       "LA" "Lake Charles" "LCH"
       "AZ" "Lake Havasu City" "HII"
       "AK" "Lake Minchumina" "LMA"
       "HI - Island of Lanai" "Lanai City" "LNY"
       "PA" "Lancaster" "LNS"
       "MI" "Lansing" "LAN"
       "WY" "Laramie" "LAR"
       "TX" "Laredo" "LRD"
       "NV" "Las Vegas" "LAS"
       "PA" "Latrobe" "LBE"
       "MS" "Laurel" "PIB"
       "OK" "Lawton" "LAW"
       "NH" "Lebanon" "LEB"
       "AK" "Levelock" "KLL"
       "WV" "Lewisburg" "LWB"
       "ID" "Lewiston" "LWS"
       "MT" "Lewistown" "LWT"
       "KY" "Lexington" "LEX"
       "KS" "Liberal" "LBL"
       "HI - Island of Kaui" "Lihue" "LIH"
       "NE" "Lincoln" "LNK"
       "AR" "Little Rock" "LIT"
       "CA" "Long Beach" "LGB"
       "TX" "Longview" "GGG"
       "WA" "Lopez Island" "LPS"
       "CA" "Los Angeles" "LAX"
       "KY" "Louisville" "SDF"
       "CO - Municipal Airport" "Loveland/Fort Collins" "FNL"
       "CO - Bus service" "Loveland/Fort Collins" "QWF"
       "TX" "Lubbock" "LBB"
       "GA" "Macon" "MCN"
       "WI" "Madison" "MSN"
       "OR" "Madras" "MDJ"
       "NH" "Manchester" "MHT"
       "KS" "Manhattan" "MHK"
       "MI" "Manistee" "MBL"
       "MN" "Mankato" "MKT"
       "AK" "Manley Hot Springs" "MLY"
       "AK" "Manokotak" "KMO"
       "OH/Parkersburg, WV" "Marietta" "PKB"
       "IL" "Marion" "MWA"
       "MI" "Marquette" "MQT"
       "AK" "Marshall" "MLL"
       "MA" "Martha's Vineyard" "MVY"
       "PA" "Martinsburg" "AOO"
       "IA" "Mason City" "MCW"
       "NY" "Massena" "MSS"
       "HI" "Maui" "OGG"
       "TX" "Mcallen" "MFE"
       "NE" "Mccook" "MCK"
       "AK" "Mcgrath" "MCG"
       "OR" "Medford" "MFR"
       "AK" "Mekoryuk" "MYU"
       "FL" "Melbourne" "MLB"
       "TN" "Memphis" "MEM"
       "CA" "Merced" "MCE"
       "MS" "Meridian" "MEI"
       "AK" "Metlakatla" "MTM"
       "AK" "Meyers Chuck" "WMK"
       "FL - International" "Miami" "MIA"
       "FL - Sea Plane Base" "Miami" "MPB"
       "MI" "Midland" "MBS"
       "TX" "Midland/Odessa" "MAF"
       "MT" "Miles City" "MLS"
       "WI" "Milwaukee" "MKE"
       "MN" "Minneapolis" "MSP"
       "ND" "Minot" "MOT"
       "AK" "Minto" "MNT"
       "TX" "Mission" "MFE"
       "MT" "Missoula" "MSO"
       "UT" "Moab" "CNY"
       "AL" "Mobile" "MOB"
       "CA" "Modesto" "MOD"
       "IL" "Moline" "MLI"
       "LA" "Monroe" "MLU"
       "CA" "Monterey" "MRY"
       "AL" "Montgomery" "MGM"
       "CO" "Montrose" "MTJ"
       "WV" "Morgantown" "MGW"
       "WA" "Moses Lake" "MWH"
       "AR" "Mountain Home" "WMH"
       "AK" "Mountain Village" "MOU"
       "AL" "Muscle Shoals" "MSL"
       "MI" "Muskegon" "MKG"
       "SC" "Myrtle Beach" "MYR"
       "MA" "Nantucket" "ACK"
       "AK" "Napakiak" "WNA"
       "AK" "Napaskiak" "PKA"
       "FL" "Naples" "APF"
       "TN" "Nashville" "BNA"
       "AK" "Naukiti" "NKI"
       "AK" "Nelson Lagoon" "NLG"
       "AK" "New Chenega" "NCN"
       "CT" "New Haven" "HVN"
       "AK" "New Koliganek" "KGK"
       "AK" "New Koliganek" "KGK"
       "LA" "New Orleans" "MSY"
       "AK" "New Stuyahok" "KNW"
       "NY - All airports" "New York" "NYC"
       "NY - Kennedy" "New York" "JFK"
       "NY - La Guardia" "New York" "LGA"
       "NJ" "Newark" "EWR"
       "NY" "Newburgh/Stewart Field" "SWF"
       "VA" "Newport News" "PHF"
       "AK" "Newtok" "WWT"
       "AK" "Nightmute" "NME"
       "AK" "Nikolai" "NIB"
       "AK" "Nikolski" "IKO"
       "AK" "Noatak" "WTK"
       "AK" "Nome" "OME"
       "AK" "Nondalton" "NNL"
       "AK" "Noorvik" "ORV"
       "NE" "Norfolk" "OFK"
       "VA" "Norfolk" "ORF"
       "OR" "North Bend" "OTH"
       "NE" "North Platte" "LBF"
       "AK" "Northway" "ORT"
       "AK" "Nuiqsut" "NUI"
       "AK" "Nulato" "NUL"
       "AK" "Nunapitchuk" "NUP"
       "CA" "Oakland" "OAK"
       "TX" "Odessa/Midland" "MAF"
       "NY" "Ogdensburg" "OGS"
       "OK" "Oklahoma City" "OKC"
       "NE" "Omaha" "OMA"
       "CA" "Ontario" "ONT"
       "CA" "Orange County" "SNA"
       "FL - Herndon" "Orlando" "ORL"
       "FL - International" "Orlando" "MCO"
       "WI" "Oshkosh" "OSH"
       "IA" "Ottumwa" "OTM"
       "KY" "Owensboro" "OWB"
       "CA" "Oxnard/Ventura" "OXR"
       "KY" "Paducah" "PAH"
       "AZ" "Page" "PGA"
       "CA" "Palm Springs" "PSP"
       "FL" "Panama City" "PFN"
       "WV/Marietta, OH" "Parkersburg" "PKB"
       "WA" "Pasco" "PSC"
       "AK" "Pedro Bay" "PDB"
       "AK" "Pelican" "PEC"
       "MI" "Pellston" "PLN"
       "OR" "Pendleton" "PDT"
       "FL" "Pensacola" "PNS"
       "IL" "Peoria" "PIA"
       "AK" "Perryville" "KPV"
       "AK" "Petersburg" "PSG"
       "PA - International" "Philadelphia" "PHL"
       "PA - Trenton/Mercer NJ" "Philadelphia" "TTN"
       "AZ" "Phoenix" "PHX"
       "SD" "Pierre" "PIR"
       "AK - Ugashnik Bay" "Pilot Point" "UGB"
       "AK" "Pilot Point" "PIP"
       "AK" "Pilot Station" "PQS"
       "PA" "Pittsburgh" "PIT"
       "AK" "Platinum" "PTU"
       "NY" "Plattsburgh" "PLB"
       "ID" "Pocatello" "PIH"
       "AK" "Point Baker" "KPB"
       "AK" "Point Hope" "PHO"
       "AK" "Point Lay" "PIZ"
       "OK" "Ponca City" "PNC"
       "Puerto Rico" "Ponce" "PSE"
       "AK" "Port Alsworth" "PTA"
       "WA" "Port Angeles" "CLM"
       "TX" "Port Arthur/Beaumont" "BPT"
       "AK" "Port Clarence" "KPC"
       "AK" "Port Heiden" "PTH"
       "AK" "Port Moller" "PML"
       "AK" "Port Protection" "PPV"
       "AK" "Portage Creek" "PCA"
       "ME" "Portland" "PWM"
       "OR" "Portland" "PDX"
       "NH" "Portsmouth" "PSM"
       "NY" "Poughkeepsie" "POU"
       "AZ" "Prescott" "PRC"
       "ME" "Presque Isle" "PQI"
       "WV" "Princeton" "BLF"
       "RI" "Providence" "PVD"
       "MA" "Provincetown" "PVC"
       "AK" "Prudhoe Bay/Deadhorse" "SCC"
       "CO" "Pueblo" "PUB"
       "WA" "Pullman" "PUW"
       "IL" "Quincy" "UIN"
       "AK" "Quinhagak" "KWN"
       "NC" "Raleigh/Durham" "RDU"
       "AK" "Rampart" "RMP"
       "SD" "Rapid City" "RAP"
       "PA" "Reading" "RDG"
       "AK" "Red Devil" "RDV"
       "CA" "Redding" "RDD"
       "OR" "Redmond" "RDM"
       "NV" "Reno" "RNO"
       "WI," "Rhinelander" "RHI"
       "VA" "Richmond" "RIC"
       "WY" "Riverton" "RIW"
       "VA" "Roanoke" "ROA"
       "WA" "Roche Harbor" "RCE"
       "MN" "Rochester" "RST"
       "NY" "Rochester" "ROC"
       "WY" "Rock Springs" "RKS"
       "IL - Park&Ride Bus" "Rockford" "ZRF"
       "IL - Van Galder Bus" "Rockford" "ZRK"
       "ME" "Rockland" "RKD"
       "WA" "Rosario" "RSJ"
       "NM" "Roswell" "ROW"
       "AK" "Ruby" "RBY"
       "AK" "Russian Mission" "RSH"
       "VT" "Rutland" "RUT"
       "CA" "Sacramento" "SMF"
       "MI" "Saginaw" "MBS"
       "MN" "Saint Cloud" "STC"
       "AK" "Saint George Island" "STG"
       "UT" "Saint George" "SGU"
       "MO" "Saint Louis" "STL"
       "AK" "Saint Mary's" "KSM"
       "AK" "Saint Michael" "SMK"
       "AK" "Saint Paul Island" "SNP"
       "OR" "Salem" "SLE"
       "KS" "Salina" "SLN"
       "MD" "Salisbury-Ocean City" "SBY"
       "UT" "Salt Lake City" "SLC"
       "TX" "San Angelo" "SJT"
       "TX" "San Antonio" "SAT"
       "CA" "San Diego" "SAN"
       "CA" "San Francisco" "SFO"
       "CA" "San Jose" "SJC"
       "Puerto Rico" "San Juan" "SJU"
       "CA" "San Luis Obispo" "SBP"
       "AK" "Sand Point" "SDP"
       "CA" "Santa Ana" "SNA"
       "CA" "Santa Barbara" "SBA"
       "NM" "Santa Fe" "SAF"
       "CA" "Santa Maria" "SMX"
       "CA" "Santa Rosa" "STS"
       "NY" "Saranac Lake" "SLK"
       "FL" "Sarasota" "SRQ"
       "MI," "Sault Ste Marie" "CIU"
       "GA" "Savannah" "SAV"
       "AK" "Savoonga" "SVA"
       "AK" "Scammon Bay" "SCM"
       "NE" "Scottsbluff" "BFF"
       "PA" "Scranton" "AVP"
       "WA - Lake Union SPB" "Seattle" "LKE"
       "WA - Seattle/Tacoma International" "Seattle" "SEA"
       "AK" "Selawik" "WLK"
       "AK" "Seward" "SWD"
       "AK" "Shageluk" "SHX"
       "AK" "Shaktoolik" "SKK"
       "AL" "Sheffield/Florence/Muscle Shoals" "MSL"
       "AK" "Sheldon Point" "SXP"
       "WY" "Sheridan" "SHR"
       "AK" "Shishmaref" "SHH"
       "LA" "Shreveport" "SHV"
       "AK" "Shungnak" "SHG"
       "NM" "Silver City" "SVC"
       "IA" "Sioux City" "SUX"
       "SD" "Sioux Falls" "FSD"
       "AK" "Sitka" "SIT"
       "AK" "Skagway" "SGY"
       "AK" "Sleetmore" "SLQ"
       "IN" "South Bend" "SBN"
       "AK" "South Naknek" "WSN"
       "NC" "Southern Pines" "SOP"
       "SC" "Spartanburg/Greenville" "GSP"
       "WA" "Spokane" "GEG"
       "IL" "Springfield" "SPI"
       "MO" "Springfield" "SGF"
       "FL" "St Petersburg/Clearwater" "PIE"
       "PA" "State College/University Park" "SCE"
       "VA" "Staunton" "SHD"
       "CO" "Steamboat Springs" "SBS"
       "AK" "Stebbins" "WBB"
       "WI" "Stevens Point/Wausau" "CWA"
       "AK" "Stevens Village" "SVS"
       "NY" "Stewart Field/Newburgh" "SWF"
       "CA" "Stockton" "SCK"
       "AK" "Stony River" "SRV"
       "ID" "Sun Valley" "SUN"
       "NY" "Syracuse" "SYR"
       "AK" "Takotna" "TCT"
       "AK" "Talkeetna" "TKA"
       "FL" "Tallahassee" "TLH"
       "FL" "Tampa" "TPA"
       "AK" "Tanana" "TAL"
       "NM" "Taos" "TSM"
       "AK" "Tatitlek" "TEK"
       "AK" "Teller Mission" "KTS"
       "CO" "Telluride" "TEX"
       "AK" "Tenakee Springs" "TKE"
       "IN" "Terre Haute" "HUF"
       "AK" "Tetlin" "TEH"
       "AR" "Texarkana" "TXK"
       "MN" "Thief River Falls" "TVF"
       "AK" "Thorne Bay" "KTB"
       "AK" "Tin City" "TNC"
       "AK" "Togiak Village" "TOG"
       "AK" "Tok" "TKJ"
       "AK" "Toksook Bay" "OOK"
       "OH" "Toledo" "TOL"
       "KS" "Topeka" "FOE"
       "MI" "Traverse City" "TVC"
       "NJ" "Trenton/Mercer" "TTN"
       "AZ" "Tucson" "TUS"
       "OK" "Tulsa" "TUL"
       "AK" "Tuluksak" "TLT"
       "AK" "Tuntutuliak" "WTL"
       "AK" "Tununak" "TNK"
       "MS" "Tupelo" "TUP"
       "AL" "Tuscaloosa" "TCL"
       "ID" "Twin Falls" "TWF"
       "AK" "Twin Hills" "TWA"
       "TX" "Tyler" "TYR"
       "AK" "Unalakleet" "UNK"
       "IL" "Urbana/Champaign" "CMI"
       "NY" "Utica" "UCA"
       "AK" "Utopia Creek" "UTO"
       "CO - Eagle County Airport" "Vail" "EGE"
       "CO - Van service" "Vail" "QBF"
       "AK" "Valdez" "VDZ"
       "GA" "Valdosta" "VLD"
       "FL" "Valparaiso" "VPS"
       "AK" "Venetie" "VEE"
       "CA" "Ventura/Oxnard" "OXR"
       "UT" "Vernal" "VEL"
       "TX" "Victoria" "VCT"
       "CA" "Visalia" "VIS"
       "TX" "Waco" "ACT"
       "AK" "Wainwright" "AIN"
       "AK" "Wales" "WAA"
       "WA" "Walla Walla" "ALW"
       "WA" "Walla Walla" "ALW"
       "WA" "Walla Walla" "ALW"
       "WA" "Walla Walla" "ALW"
       "AK" "Waterfall" "KWF"
       "IA" "Waterloo" "ALO"
       "NY" "Watertown" "ART"
       "SD" "Watertown" "ATY"
       "WI" "Wausau/Stevens Point" "CWA"
       "WA" "Wenatchee" "EAT"
       "FL" "West Palm Beach" "PBI"
       "MT" "West Yellowstone" "WYS"
       "NY" "Westchester County" "HPN"
       "RI" "Westerly" "WST"
       "WA" "Westsound" "WSX"
       "AK" "Whale Pass" "WWP"
       "AK" "White Mountain" "WMO"
       "VT" "White River" "LEB"
       "TX" "Wichita Falls" "SPS"
       "KS" "Wichita" "ICT"
       "PA" "Wilkes Barre" "AVP"
       "VA" "Williamsburg" "PHF"
       "PA" "Williamsport" "IPT"
       "ND" "Williston" "ISN"
       "NC" "Wilmington" "ILM"
       "CT" "Windsor Locks" "BDL"
       "MA" "Worcester" "ORH"
       "WY" "Worland" "WRL"
       "AK" "Wrangell" "WRG"
       "WA" "Yakima" "YKM"
       "AK" "Yakutat" "YAK"
       "WY" "Yellowstone/Cody" "COD"
       "OH" "Youngstown" "YNG"
       "AZ" "Yuma" "YUM"
       ]))

;; from: http://www.bennetyee.org/ucsd-pages/area.html
(def *us-area-code-detail*
     '(("Area Code" "Region" "Time Zone Offset" "Description")
       ("52 55" "MX" "-6" "   Mexico: Mexico City area (country code + city code)")
       ("201" "NJ" "-5" "   N New Jersey: Jersey City, Hackensack (see split 973, overlay 551)")
       ("202" "DC" "-5" "   Washington, D.C.")
       ("203" "CT" "-5" "   Connecticut: Fairfield County and New Haven County; Bridgeport, New Haven (see 860)")
       ("204" "MB" "-6" "   Canada: Manitoba")
       ("205" "AL" "-6" "   Central Alabama (including Birmingham; excludes the southeastern corner of Alabama and the deep south; see splits 256 and 334)")
       ("206" "WA" "-8" "   W Washington state: Seattle and Bainbridge Island (see splits 253, 360, 425; overlay 564)")
       ("207" "ME" "-5" "   Maine")
       ("208" "ID" "-7/-8" "   Idaho")
       ("209" "CA" "-8" "   Cent. California: Stockton (see split 559)")
       ("210" "TX" "-6" "   S Texas: San Antonio (see also splits 830, 956)")
       ("211" "--" "--" "   Local community info / referral services")
       ("212" "NY" "-5" "   New York City, New York (Manhattan; see 646, 718)")
       ("213" "CA" "-8" "   S California: Los Angeles (see 310, 323, 626, 818)")
       ("214" "TX" "-6" "   Texas: Dallas Metro (overlays 469/972)")
       ("215" "PA" "-5" "   SE Pennsylvania: Philadelphia (see overlays 267)")
       ("216" "OH" "-5" "   Cleveland (see splits 330, 440)")
       ("217" "IL" "-6" "   Cent. Illinois: Springfield")
       ("218" "MN" "-6" "   N Minnesota: Duluth")
       ("219" "IN" "-6/-5" "   NW Indiana: Gary (see split 574, 260)")
       ("224" "IL" "-6" "   Northern NE Illinois:  Evanston, Waukegan, Northbrook (overlay on 847, eff 1/5/02)")
       ("225" "LA" "-6" "   Louisiana: Baton Rouge, New Roads, Donaldsonville, Albany, Gonzales, Greensburg, Plaquemine, Vacherie (split from 504)")
       ("226" "ON" "-5" "   Canada: SW Ontario: Windsor (overlaid on 519; eff 6/06)")
       ("228" "MS" "-6" "   S Mississippi (coastal areas, Biloxi, Gulfport; split from 601)")
       ("229" "GA" "-5" "   SW Georgia: Albany (split from 912; see also 478; perm 8/1/00)")
       ("231" "MI" "-5" "   W Michigan: Northwestern portion of lower Peninsula; Traverse City, Muskegon, Cheboygan, Alanson")
       ("234" "OH" "-5" "   NE Ohio: Canton, Akron (overlaid on 330; perm 10/30/00)")
       ("236" "VA" "-5" "   Virginia (region unknown) / Unassigned?")
       ("239" "FL" "-5" "   Florida (Lee, Collier, and Monroe Counties, excl the Keys; see 305; eff 3/11/02; mand 3/11/03)")
       ("240" "MD" "-5" "   W Maryland: Silver Spring, Frederick, Gaithersburg (overlay, see 301)")
       ("242" "--" "-5" "   Bahamas")
       ("246" "--" "-4" "   Barbados")
       ("248" "MI" "-5" "   Michigan: Oakland County, Pontiac (split from 810; see overlay 947)")
       ("250" "BC" "-8/-7" "   Canada: British Columbia (see 604)")
       ("251" "AL" "-6" "   S Alabama: Mobile and coastal areas, Jackson, Evergreen, Monroeville (split from 334, eff 6/18/01; see also 205, 256)")
       ("252" "NC" "-5" "   E North Carolina (Rocky Mount; split from 919)")
       ("253" "WA" "-8" "   Washington: South Tier - Tacoma, Federal Way (split from 206, see also 425; overlay 564)")
       ("254" "TX" "-6" "   Central Texas (Waco, Stephenville; split, see 817, 940)")
       ("256" "AL" "-6" "   E and N Alabama (Huntsville, Florence, Gadsden; split from 205; see also 334)")
       ("260" "IN" "-5" "   NE Indiana: Fort Wayne (see 219)")
       ("262" "WI" "-6" "   SE Wisconsin: counties of Kenosha, Ozaukee, Racine, Walworth, Washington, Waukesha (split from 414)")
       ("264" "--" "-4" "   Anguilla (split from 809)")
       ("267" "PA" "-5" "   SE Pennsylvania: Philadelphia (see 215)")
       ("268" "--" "-4" "   Antigua and Barbuda (split from 809)")
       ("269" "MI" "-5" "   SW Michigan: Kalamazoo, Saugatuck, Hastings, Battle Creek, Sturgis to Lake Michigan (split from 616)")
       ("270" "KY" "-6" "   W Kentucky: Bowling Green, Paducah (split from 502)")
       ("276" "VA" "-5" "   S and SW Virginia: Bristol, Stuart, Martinsville (split from 540; perm 9/1/01, mand 3/16/02)")
       ("278" "MI" "-5" "   Michigan (overlaid on 734, SUSPENDED)")
       ("281" "TX" "-6" "   Texas: Houston Metro (split 713; overlay 832)")
       ("283" "OH" "-5" "   SW Ohio: Cincinnati (cancelled: overlaid on 513)")
       ("284" "--" "-4" "   British Virgin Islands (split from 809)")
       ("289" "ON" "-5" "   Canada: S Cent. Ontario: Greater Toronto Area -- Durham, Halton, Hamilton-Wentworth, Niagara, Peel, York, and southern Simcoe County (excluding Toronto -- overlaid on 905, eff 6/9/01)")
       ("301" "MD" "-5" "   W Maryland: Silver Spring, Frederick, Camp Springs, Prince George's County (see 240)")
       ("302" "DE" "-5" "   Delaware")
       ("303" "CO" "-7" "   Central Colorado: Denver (see 970, also 720 overlay)")
       ("304" "WV" "-5" "   West Virginia")
       ("305" "FL" "-5" "   SE Florida: Miami, the Keys (see 786, 954; 239)")
       ("306" "SK" "-6/-7*" "   Canada: Saskatchewan")
       ("307" "WY" "-7" "   Wyoming")
       ("308" "NE" "-6/-7" "   W Nebraska: North Platte")
       ("309" "IL" "-6" "   W Cent. Illinois: Peoria")
       ("310" "CA" "-8" "   S California: Beverly Hills, West Hollywood, West Los Angeles (see split 562; overlay 424)")
       ("311" "--" "--" "   Reserved for special applications")
       ("312" "IL" "-6" "   Illinois: Chicago (downtown only -- in the loop; see 773; overlay 872)")
       ("313" "MI" "-5" "   Michigan: Detroit and suburbs (see 734, overlay 679)")
       ("314" "MO" "-6" "   SE Missouri: St Louis city and parts of the metro area only (see 573, 636, overlay 557)")
       ("315" "NY" "-5" "   N Cent. New York: Syracuse")
       ("316" "KS" "-6" "   S Kansas: Wichita (see split 620)")
       ("317" "IN" "-5" "   Cent. Indiana: Indianapolis (see 765)")
       ("318" "LA" "-6" "   N Louisiana: Shreveport, Ruston, Monroe, Alexandria (see split 337)")
       ("319" "IA" "-6" "   E Iowa: Cedar Rapids (see split 563)")
       ("320" "MN" "-6" "   Cent. Minnesota: Saint Cloud (rural Minn, excl St. Paul/Minneapolis)")
       ("321" "FL" "-5" "   Florida: Brevard County, Cape Canaveral area; Metro Orlando (split from 407)")
       ("323" "CA" "-8" "   S California: Los Angeles (outside downtown: Hollywood; split from 213)")
       ("325" "TX" "-6" "   Central Texas: Abilene, Sweetwater, Snyder, San Angelo (split from 915)")
       ("330" "OH" "-5" "   NE Ohio: Akron, Canton, Youngstown; Mahoning County, parts of Trumbull/Warren counties (see splits 216, 440, overlay 234)")
       ("331" "IL" "-6" "   W NE Illinois, western suburbs of Chicago (part of what used to be 708; overlaid on 630; eff 7/07)")
       ("334" "AL" "-6" "   S Alabama: Auburn/Opelika, Montgomery and coastal areas (part of what used to be 205; see also 256, split 251)")
       ("336" "NC" "-5" "   Cent. North Carolina: Greensboro, Winston-Salem, High Point (split from 910)")
       ("337" "LA" "-6" "   SW Louisiana: Lake Charles, Lafayette (see split 318)")
       ("339" "MA" "-5" "   Massachusetts: Boston suburbs, to the south and west (see splits 617, 508; overlaid on 781, eff 5/2/01)")
       ("340" "VI" "-4*" "   US Virgin Islands (see also 809)")
       ("341" "CA" "-8" "   (overlay on 510; SUSPENDED)")
       ("345" "--" "-5" "   Cayman Islands")
       ("347" "NY" "-5" "   New York (overlay for 718: NYC area, except Manhattan)")
       ("351" "MA" "-5" "   Massachusetts: north of Boston to NH, 508, and 781 (overlaid on 978, eff 4/2/01)")
       ("352" "FL" "-5" "   Florida: Gainesville area, Ocala, Crystal River (split from 904)")
       ("360" "WA" "-8" "   W Washington State: Olympia, Bellingham (area circling 206, 253, and 425; split from 206; see overlay 564)")
       ("361" "TX" "-6" "   S Texas: Corpus Christi (split from 512; eff 2/13/99)")
       ("369" "CA" "-8" "   Solano County (perm 12/2/00, mand 6/2/01)")
       ("380" "OH" "-5" "   Ohio: Columbus (overlaid on 614; assigned but not in use)")
       ("385" "UT" "-7" "   Utah: Salt Lake City Metro (split from 801, eff 3/30/02 POSTPONED; see also 435)")
       ("386" "FL" "-5" "   N central Florida: Lake City (split from 904, perm 2/15/01, mand 11/5/01)")
       ("401" "RI" "-5" "   Rhode Island")
       ("402" "NE" "-6" "   E Nebraska: Omaha, Lincoln")
       ("403" "AB" "-7" "   Canada: Southern Alberta (see 780, 867)")
       ("404" "GA" "-5" "   N Georgia: Atlanta and suburbs (see overlay 678, split 770)")
       ("405" "OK" "-6" "   W Oklahoma: Oklahoma City (see 580)")
       ("406" "MT" "-7" "   Montana")
       ("407" "FL" "-5" "   Central Florida: Metro Orlando (see overlay 689, eff 7/02; split 321)")
       ("408" "CA" "-8" "   Cent. Coastal California: San Jose (see overlay 669)")
       ("409" "TX" "-6" "   SE Texas: Galveston, Port Arthur, Beaumont (splits 936, 979)")
       ("410" "MD" "-5" "   E Maryland: Baltimore, Annapolis, Chesapeake Bay area, Ocean City (see 443)")
       ("411" "--" "--" "   Reserved for special applications")
       ("412" "PA" "-5" "   W Pennsylvania: Pittsburgh (see split 724, overlay 878)")
       ("413" "MA" "-5" "   W Massachusetts: Springfield")
       ("414" "WI" "-6" "   SE Wisconsin: Milwaukee County (see splits 920, 262)")
       ("415" "CA" "-8" "   California: San Francisco County and Marin County on the north side of the Golden Gate Bridge, extending north to Sonoma County (see 650)")
       ("416" "ON" "-5" "   Canada: S Cent. Ontario: Toronto (see overlay 647, eff 3/5/01)")
       ("417" "MO" "-6" "   SW Missouri: Springfield")
       ("418" "QC" "-5/-4" "   Canada: NE Quebec: Quebec")
       ("419" "OH" "-5" "   NW Ohio: Toledo (see overlay 567, perm 1/1/02)")
       ("423" "TN" "-5" "   E Tennessee, except Knoxville metro area: Chattanooga, Bristol, Johnson City, Kingsport, Greeneville (see split 865; part of what used to be 615)")
       ("424" "CA" "-8" "   S California: Los Angeles (see split 562; overlaid on 310 mand 7/26/06)")
       ("425" "WA" "-8" "   Washington: North Tier - Everett, Bellevue (split from 206, see also 253; overlay 564)")
       ("430" "TX" "-6" "   NE Texas: Tyler (overlaid on 903, eff 7/20/02)")
       ("432" "TX" "-7/-6" "   W Texas: Big Spring, Midland, Odessa (split from 915, eff 4/5/03)")
       ("434" "VA" "-5" "   E Virginia: Charlottesville, Lynchburg, Danville, South Boston, and Emporia (split from 804, eff 6/1/01; see also 757)")
       ("435" "UT" "-7" "   Rural Utah outside Salt Lake City metro (see split 801)")
       ("438" "QC" "-5" "   Canada: SW Quebec: Montreal city (overlaid on 514, [delayed until 6/06] eff 10/10/03, mand 2/7/04)")
       ("440" "OH" "-5" "   Ohio: Cleveland metro area, excluding Cleveland (split from 216, see also 330)")
       ("441" "--" "-4" "   Bermuda (part of what used to be 809)")
       ("442" "CA" "-8" "   Far north suburbs of San Diego (Oceanside, Escondido, SUSPENDED -- originally perm 10/21/00, mand 4/14/01)")
       ("443" "MD" "-5" "   E Maryland: Baltimore, Annapolis, Chesapeake Bay area, Ocean City (overlaid on 410)")
       ("450" "QC" "-5/-4" "   Canada: Southeastern Quebec; suburbs outside metro Montreal")
       ("456" "--" "--" "   Inbound International")
       ("464" "IL" "-6" "   Illinois: south suburbs of Chicago (see 630; overlaid on 708)")
       ("469" "TX" "-6" "   Texas: Dallas Metro (overlays 214/972)")
       ("470" "GA" "-5" "   Georgia: Greater Atlanta Metropolitan Area (overlaid on 404/770/678; mand 9/2/01)")
       ("473" "--" "-4" "   Grenada (\"new\" -- split from 809)")
       ("475" "CT" "-5" "   Connecticut: New Haven, Greenwich, southwestern (postponed; was perm 1/6/01; mand 3/1/01???)")
       ("478" "GA" "-5" "   Central Georgia: Macon (split from 912; see also 229; perm 8/1/00; mand 8/1/01)")
       ("479" "AR" "-6" "   NW Arkansas:  Fort Smith, Fayetteville, Springdale, Bentonville (SPLIt from 501, perm 1/19/02, mand 7/20/02)")
       ("480" "AZ" "-7*" "   Arizona: East Phoenix (see 520; also Phoenix split 602, 623)")
       ("484" "PA" "-5" "   SE Pennsylvania: Allentown, Bethlehem, Reading, West Chester, Norristown (see 610)")
       ("500" "--" "--" "   Personal Communication Service")
       ("501" "AR" "-6" "   Central Arkansas: Little Rock, Hot Springs, Conway (see split 479)")
       ("502" "KY" "-5" "   N Central Kentucky: Louisville (see 270)")
       ("503" "OR" "-8" "   Oregon (see 541, 971)")
       ("504" "LA" "-6" "   E Louisiana: New Orleans metro area (see splits 225, 985)")
       ("505" "NM" "-7" "   North central and northwestern New Mexico (Albuquerque, Santa Fe, Los Alamos; see split 575, eff 10/07/07)")
       ("506" "NB" "-4" "   Canada: New Brunswick")
       ("507" "MN" "-6" "   S Minnesota: Rochester, Mankato, Worthington")
       ("508" "MA" "-5" "   Cent. Massachusetts: Framingham; Cape Cod (see split 978, overlay 774)")
       ("509" "WA" "-8" "   E and Central Washington state: Spokane, Yakima, Walla Walla, Ellensburg")
       ("510" "CA" "-8" "   California: Oakland, East Bay (see 925)")
       ("511" "--" "--" "   Nationwide travel information")
       ("512" "TX" "-6" "   S Texas: Austin (see split 361; overlay 737, perm 11/10/01)")
       ("513" "OH" "-5" "   SW Ohio: Cincinnati (see split 937; overlay 283 cancelled)")
       ("514" "QC" "-5" "   Canada: SW Quebec: Montreal city (see overlay 438, eff 10/10/03, mand 2/7/04)")
       ("515" "IA" "-6" "   Cent. Iowa: Des Moines (see split 641)")
       ("516" "NY" "-5" "   New York: Nassau County, Long Island; Hempstead (see split 631)")
       ("517" "MI" "-5" "   Cent. Michigan: Lansing (see split 989)")
       ("518" "NY" "-5" "   NE New York: Albany")
       ("519" "ON" "-5" "   Canada: SW Ontario: Windsor (see overlay 226)")
       ("520" "AZ" "-7*" "   SE Arizona: Tucson area (split from 602; see split 928)")
       ("530" "CA" "-8" "   NE California: Eldorado County area, excluding Eldorado Hills itself: incl cities of Auburn, Chico, Redding, So. Lake Tahoe, Marysville, Nevada City/Grass Valley (split from 916)")
       ("540" "VA" "-5" "   Western and Southwest Virginia: Shenandoah and Roanoke valleys: Fredericksburg, Harrisonburg, Roanoke, Salem, Lexington and nearby areas (see split 276; split from 703)")
       ("541" "OR" "-8/-7" "   Oregon: Eugene, Medford (split from 503; 503 retains NW part [Portland/Salem], all else moves to 541; eastern oregon is UTC-7)")
       ("551" "NJ" "-5" "   N New Jersey: Jersey City, Hackensack (overlaid on 201)")
       ("555" "--" "?" "   Reserved for directory assistance applications")
       ("557" "MO" "-6" "   SE Missouri: St Louis metro area only (cancelled: overlaid on 314)")
       ("559" "CA" "-8" "   Central California: Fresno (split from 209)")
       ("561" "FL" "-5" "   S. Central Florida: Palm Beach County (West Palm Beach, Boca Raton, Vero Beach; see split 772, eff 2/11/02; mand 11/11/02)")
       ("562" "CA" "-8" "   California: Long Beach (split from 310 Los Angeles)")
       ("563" "IA" "-6" "   E Iowa: Davenport, Dubuque (split from 319, eff 3/25/01)")
       ("564" "WA" "-8" "   W Washington State: Olympia, Bellingham (overlaid on 360; see also 206, 253, 425; assigned but not in use)")
       ("567" "OH" "-5" "   NW Ohio: Toledo (overlaid on 419, perm 1/1/02)")
       ("570" "PA" "-5" "   NE and N Central Pennsylvania: Wilkes-Barre, Scranton (see 717)")
       ("571" "VA" "-5" "   Northern Virginia: Arlington, McLean, Tysons Corner (to be overlaid on 703 3/1/00; see earlier split 540)")
       ("573" "MO" "-6" "   SE Missouri: excluding St Louis metro area, includes Central/East Missouri, area between St. Louis and Kansas City")
       ("574" "IN" "-5" "   N Indiana: Elkhart, South Bend (split from 219)")
       ("575" "NM" "-7" "   New Mexico (Las Cruces, Alamogordo, Roswell; split from 505, eff 10/07/07)")
       ("580" "OK" "-6" "   W Oklahoma (rural areas outside Oklahoma City; split from 405)")
       ("585" "NY" "-5" "   NW New York: Rochester (split from 716)")
       ("586" "MI" "-5" "   Michigan: Macomb County (split from 810; perm 9/22/01, mand 3/23/02)")
       ("600" "--" "--" "   Canadian Services")
       ("601" "MS" "-6" "   Mississippi: Meridian, Jackson area (see splits 228, 662; overlay 769)")
       ("602" "AZ" "-7*" "   Arizona: Phoenix (see 520; also Phoenix split 480, 623)")
       ("603" "NH" "-5" "   New Hampshire")
       ("604" "BC" "-8" "   Canada: British Columbia: Greater Vancouver (overlay 778, perm 11/3/01; see 250)")
       ("605" "SD" "-6/-7" "   South Dakota")
       ("606" "KY" "-5/-6" "   E Kentucky: area east of Frankfort: Ashland (see 859)")
       ("607" "NY" "-5" "   S Cent. New York: Ithaca, Binghamton; Catskills")
       ("608" "WI" "-6" "   SW Wisconsin: Madison")
       ("609" "NJ" "-5" "   S New Jersey: Trenton (see 856)")
       ("610" "PA" "-5" "   SE Pennsylvania: Allentown, Bethlehem, Reading, West Chester, Norristown (see overlays 484, 835)")
       ("611" "--" "--" "   Reserved for special applications")
       ("612" "MN" "-6" "   Cent. Minnesota: Minneapolis (split from St. Paul, see 651; see splits 763, 952)")
       ("613" "ON" "-5" "   Canada: SE Ontario: Ottawa")
       ("614" "OH" "-5" "   SE Ohio: Columbus (see overlay 380)")
       ("615" "TN" "-6" "   Northern Middle Tennessee: Nashville metro area (see 423, 931)")
       ("616" "MI" "-5" "   W Michigan: Holland, Grand Haven, Greenville, Grand Rapids, Ionia (see split 269)")
       ("617" "MA" "-5" "   Massachusetts: greater Boston (see overlay 857)")
       ("618" "IL" "-6" "   S Illinois: Centralia")
       ("619" "CA" "-8" "   S California: San Diego (see split 760; overlay 858, 935)")
       ("620" "KS" "-6" "   S Kansas: Wichita (split from 316; perm 2/3/01)")
       ("623" "AZ" "-7*" "   Arizona: West Phoenix (see 520; also Phoenix split 480, 602)")
       ("626" "CA" "-8" "   E S California: Pasadena (split from 818 Los Angeles)")
       ("627" "CA" "-8" "   No longer in use [was Napa, Sonoma counties (perm 10/13/01, mand 4/13/02); now 707]")
       ("628" "CA" "-8" "   (Region unknown; perm 10/21/00)")
       ("630" "IL" "-6" "   W NE Illinois, western suburbs of Chicago (part of what used to be 708; overlay 331)")
       ("631" "NY" "-5" "   New York: Suffolk County, Long Island; Huntington, Riverhead (split 516)")
       ("636" "MO" "-6" "   Missouri: W St. Louis metro area of St. Louis county, St. Charles County, Jefferson County area south (between 314 and 573)")
       ("641" "IA" "-6" "   Iowa: Mason City, Marshalltown, Creston, Ottumwa (split from 515; perm 7/9/00)")
       ("646" "NY" "-5" "   New York (overlay 212/917) NYC: Manhattan only")
       ("647" "ON" "-5" "   Canada: S Cent. Ontario: Toronto (overlaid on 416; eff 3/5/01)")
       ("649" "--" "-5" "   Turks &amp; Caicos Islands")
       ("650" "CA" "-8" "   California: Peninsula south of San Francisco -- San Mateo County, parts of Santa Clara County (split from 415)")
       ("651" "MN" "-6" "   Cent. Minnesota: St. Paul (split from Minneapolis, see 612)")
       ("660" "MO" "-6" "   N Missouri (split from 816)")
       ("661" "CA" "-8" "   California: N Los Angeles, Mckittrick, Mojave, Newhall, Oildale, Palmdale, Taft, Tehachapi, Bakersfield, Earlimart, Lancaster (split from 805)")
       ("662" "MS" "-6" "   N Mississippi: Tupelo, Grenada (split from 601)")
       ("664" "--" "-4" "   Montserrat (split from 809)")
       ("669" "CA" "-8" "   Cent. Coastal California: San Jose (rejected was: overlaid on 408)")
       ("670" "MP" "+10*" "   Commonwealth of the Northern Mariana Islands (CNMI, US Commonwealth)")
       ("671" "GU" "+10*" "   Guam")
       ("678" "GA" "-5" "   N Georgia: metropolitan Atlanta (overlay; see 404, 770)")
       ("679" "MI" "-5/-6" "   Michigan: Dearborn area (overlaid on 313; assigned but not in use)")
       ("682" "TX" "-6" "   Texas: Fort Worth areas (perm 10/7/00, mand 12/9/00)")
       ("684" "--" "-11" "   American Samoa")
       ("689" "FL" "-5" "   Central Florida: Metro Orlando (see overlay 321; overlaid on 407, assigned but not in use)")
       ("700" "--" "--" "   Interexchange Carrier Services")
       ("701" "ND" "-6" "   North Dakota")
       ("702" "NV" "-8" "   S. Nevada: Clark County, incl Las Vegas (see 775)")
       ("703" "VA" "-5" "   Northern Virginia: Arlington, McLean, Tysons Corner (see split 540; overlay 571)")
       ("704" "NC" "-5" "   W North Carolina: Charlotte (see split 828, overlay 980)")
       ("705" "ON" "-5" "   Canada: NE Ontario: Sault Ste. Marie/N Ontario: N Bay, Sudbury")
       ("706" "GA" "-5" "   N Georgia: Columbus, Augusta (see overlay 762)")
       ("707" "CA" "-8" "   NW California: Santa Rosa, Napa, Vallejo, American Canyon, Fairfield")
       ("708" "IL" "-6" "   Illinois: southern and western suburbs of Chicago (see 630; overlay 464)")
       ("709" "NL" "-4/-3.5" "   Canada: Newfoundland and Labrador")
       ("710" "--" "?" "   US Government")
       ("711" "--" "--" "   Telecommunications Relay Services")
       ("712" "IA" "-6" "   W Iowa: Council Bluffs")
       ("713" "TX" "-6" "   Mid SE Texas: central Houston (split, 281; overlay 832)")
       ("714" "CA" "-8" "   North and Central Orange County (see split 949)")
       ("715" "WI" "-6" "   N Wisconsin: Eau Claire, Wausau, Superior")
       ("716" "NY" "-5" "   NW New York: Buffalo (see split 585)")
       ("717" "PA" "-5" "   E Pennsylvania: Harrisburg (see split 570)")
       ("718" "NY" "-5" "   New York City, New York (Queens, Staten Island, The Bronx, and Brooklyn; see 212, 347)")
       ("719" "CO" "-7" "   SE Colorado: Pueblo, Colorado Springs")
       ("720" "CO" "-7" "   Central Colorado: Denver (overlaid on 303)")
       ("724" "PA" "-5" "   SW Pennsylvania (areas outside metro Pittsburgh; split from 412)")
       ("727" "FL" "-5" "   Florida Tampa Metro: Saint Petersburg, Clearwater (Pinellas and parts of Pasco County; split from 813)")
       ("731" "TN" "-6" "   W Tennessee: outside Memphis metro area (split from 901, perm 2/12/01, mand 9/17/01)")
       ("732" "NJ" "-5" "   Cent. New Jersey: Toms River, New Brunswick, Bound Brook (see overlay 848)")
       ("734" "MI" "-5" "   SE Michigan: west and south of Detroit -- Ann Arbor, Monroe (split from 313)")
       ("737" "TX" "-6" "   S Texas: Austin (overlaid on 512, suspended; see also 361)")
       ("740" "OH" "-5" "   SE Ohio (rural areas outside Columbus; split from 614)")
       ("747" "CA" "-8" "   S California: Los Angeles, Agoura Hills, Calabasas, Hidden Hills, and Westlake Village (see 818; implementation suspended)")
       ("754" "FL" "-5" "   Florida: Broward County area, incl Ft. Lauderdale (overlaid on 954; perm 8/1/01, mand 9/1/01)")
       ("757" "VA" "-5" "   E Virginia: Tidewater / Hampton Roads area -- Norfolk, Virginia Beach, Chesapeake, Portsmouth, Hampton, Newport News, Suffolk (part of what used to be 804)")
       ("758" "--" "-4" "   St. Lucia (split from 809)")
       ("760" "CA" "-8" "   California: San Diego North County to Sierra Nevada (split from 619)")
       ("762" "GA" "-5" "   N Georgia: Columbus, Augusta (overlaid on 706)")
       ("763" "MN" "-6" "   Minnesota: Minneapolis NW (split from 612; see also 952)")
       ("764" "CA" "-8" "   (overlay on 650; SUSPENDED)")
       ("765" "IN" "-5" "   Indiana: outside Indianapolis (split from 317)")
       ("767" "--" "-4" "   Dominica (split from 809)")
       ("769" "MS" "-6" "   Mississippi: Meridian, Jackson area (overlaid on 601; perm 7/19/04, mand 3/14/05)")
       ("770" "GA" "-5" "   Georgia: Atlanta suburbs: outside of I-285 ring road (part of what used to be 404; see also overlay 678)")
       ("772" "FL" "-5" "   S. Central Florida: St. Lucie, Martin, and Indian River counties (split from 561; eff 2/11/02; mand 11/11/02)")
       ("773" "IL" "-6" "   Illinois: city of Chicago, outside the loop (see 312; overlay 872)")
       ("774" "MA" "-5" "   Cent. Massachusetts: Framingham; Cape Cod (see split 978, overlaid on 508, eff 4/2/01)")
       ("775" "NV" "-8" "   N. Nevada: Reno (all of NV except Clark County area; see 702)")
       ("778" "BC" "-8" "   Canada: British Columbia: Greater Vancouver (overlaid on 604, per 11/3/01; see also 250)")
       ("779" "IL" "-6" "   NW Illinois: Rockford, Kankakee (overlaid on 815; eff 8/19/06, mand 2/17/07)")
       ("780" "AB" "-7" "   Canada: Northern Alberta, north of Lacombe (see 403)")
       ("781" "MA" "-5" "   Massachusetts: Boston surburbs, to the north and west (see splits 617, 508; overlay 339)")
       ("784" "--" "-4" "   St. Vincent &amp; Grenadines (split from 809)")
       ("785" "KS" "-6" "   N &amp; W Kansas: Topeka (split from 913)")
       ("786" "FL" "-5" "   SE Florida, Monroe County (Miami; overlaid on 305)")
       ("787" "PR" "-4*" "   Puerto Rico (see overlay 939, perm 8/1/01)")
       ("800" "--" "?" "   US/Canada toll free (see 888, 877, 866, 855, 844, 833, 822)")
       ("801" "UT" "-7" "   Utah: Salt Lake City Metro (see split 385, eff 3/30/02; see also split 435)")
       ("802" "VT" "-5" "   Vermont")
       ("803" "SC" "-5" "   South Carolina: Columbia, Aiken, Sumter (see 843, 864)")
       ("804" "VA" "-5" "   E Virginia: Richmond (see splits 757, 434)")
       ("805" "CA" "-8" "   S Cent. and Cent. Coastal California: Ventura County, Santa Barbara County: San Luis Obispo, Thousand Oaks, Carpinteria, Santa Barbara, Santa Maria, Lompoc, Santa Ynez Valley / Solvang (see 661 split)")
       ("806" "TX" "-6" "   Panhandle Texas: Amarillo, Lubbock")
       ("807" "ON" "-5/-6" "   Canada: W Ontario: Thunder Bay region to Manitoba border")
       ("808" "HI" "-10*" "   Hawaii")
       ("809" "--" "-4" "   Dominican Republic (see splits 264, 268, 284, 340, 441, 473, 664, 758, 767, 784, 868, 876; overlay 829)")
       ("810" "MI" "-5" "   E Michigan: Flint, Pontiac (see 248; split 586)")
       ("811" "--" "--" "   Reserved for special applications")
       ("812" "IN" "-6/-5" "   S Indiana: Evansville, Cincinnati outskirts in IN, Columbus, Bloomington (mostly GMT-5)")
       ("813" "FL" "-5" "   SW Florida: Tampa Metro (splits 727 St. Petersburg, Clearwater, and 941 Sarasota)")
       ("814" "PA" "-5" "   Cent. Pennsylvania: Erie")
       ("815" "IL" "-6" "   NW Illinois: Rockford, Kankakee (see overlay 779; eff 8/19/06, mand 2/17/07)")
       ("816" "MO" "-6" "   N Missouri: Kansas City (see split 660, overlay 975)")
       ("817" "TX" "-6" "   N Cent. Texas: Fort Worth area (see 254, 940)")
       ("818" "CA" "-8" "   S California: Los Angeles: San Fernando Valley (see 213, 310, 562, 626, 747)")
       ("819" "QC" "-5" "   NW Quebec: Trois Rivieres, Sherbrooke, Outaouais (Gatineau, Hull), and the Laurentians (up to St Jovite / Tremblant)
(see 867)")
       ("822" "--" "?" "   US/Canada toll free (proposed, may not be in use yet)")
       ("828" "NC" "-5" "   W North Carolina: Asheville (split from 704)")
       ("829" "--" "-4" "   Dominican Republic (perm 1/31/05; mand 8/1/05; overlaid on 809)")
       ("830" "TX" "-6" "   Texas: region surrounding San Antonio (split from 210)")
       ("831" "CA" "-8" "   California: central coast area from Santa Cruz through Monterey County")
       ("832" "TX" "-6" "   Texas: Houston (overlay 713/281)")
       ("833" "--" "?" "   US/Canada toll free (proposed, may not be in use yet)")
       ("835" "PA" "-5" "   SE Pennsylvania: Allentown, Bethlehem, Reading, West Chester, Norristown (overlaid on 610, eff 5/1/01; see also 484)")
       ("843" "SC" "-5" "   South Carolina, coastal area: Charleston, Beaufort, Myrtle Beach (split from 803)")
       ("844" "--" "?" "   US/Canada toll free (proposed, may not be in use yet)")
       ("845" "NY" "-5" "   New York: Poughkeepsie; Nyack, Nanuet, Valley Cottage, New City, Putnam, Dutchess, Rockland, Orange, Ulster and parts of Sullivan counties in New York's lower Hudson Valley and Delaware County in the Catskills (see 914; perm 6/5/00)")
       ("847" "IL" "-6" "   Northern NE Illinois: northwestern suburbs of chicago (Evanston, Waukegan, Northbrook; see overlay 224)")
       ("848" "NJ" "-5" "   Cent. New Jersey: Toms River, New Brunswick, Bound Brook (see overlay 732)")
       ("850" "FL" "-6/-5" "   Florida panhandle, from east of Tallahassee to Pensacola (split from 904); western panhandle (Pensacola, Panama City) are UTC-6")
       ("855" "--" "?" "   US/Canada toll free (proposed, may not be in use yet)")
       ("856" "NJ" "-5" "   SW New Jersey: greater Camden area, Mt Laurel (split from 609)")
       ("857" "MA" "-5" "   Massachusetts: greater Boston (overlaid on 617, eff 4/2/01)")
       ("858" "CA" "-8" "   S California: San Diego (see split 760; overlay 619, 935)")
       ("859" "KY" "-5" "   N and Central Kentucky: Lexington; suburban KY counties of Cincinnati OH metro area; Covington, Newport, Ft. Thomas, Ft. Wright, Florence (split from 606)")
       ("860" "CT" "-5" "   Connecticut: areas outside of Fairfield and New Haven Counties (split from 203, overlay 959)")
       ("862" "NJ" "-5" "   N New Jersey: Newark Paterson Morristown (overlaid on 973)")
       ("863" "FL" "-5" "   Florida: Lakeland, Polk County (split from 941)")
       ("864" "SC" "-5" "   South Carolina, upstate area: Greenville, Spartanburg (split from 803)")
       ("865" "TN" "-5" "   E Tennessee: Knoxville, Knox and adjacent counties (split from 423; part of what used to be 615)")
       ("866" "--" "?" "   US/Canada toll free")
       ("867" "YT" "-5/-6/-7/-8" "   Canada: Yukon, Northwest Territories, Nunavut (split from 403/819)")
       ("868" "--" "-4" "   Trinidad and Tobago (\"new\" -- see 809)")
       ("869" "--" "-4" "   St. Kitts &amp; Nevis")
       ("870" "AR" "-6" "   Arkansas: areas outside of west/central AR: Jonesboro, etc")
       ("872" "IL" "-6" "   Illinois: Chicago (downtown only -- in the loop; see 773; overlaid on 312 and 773)")
       ("876" "--" "-5" "   Jamaica (split from 809)")
       ("877" "--" "?" "   US/Canada toll free")
       ("878" "PA" "-5" "   Pittsburgh, New Castle (overlaid on 412, perm 8/17/01, mand t.b.a.)")
       ("880" "--" "--" "   Paid Toll-Free Service")
       ("881" "--" "--" "   Paid Toll-Free Service")
       ("882" "--" "--" "   Paid Toll-Free Service")
       ("888" "--" "?" "   US/Canada toll free")
       ("898" "--" "?" "   VoIP service")
       ("900" "--" "?" "   US toll calls -- prices vary with the number called")
       ("901" "TN" "-6" "   W Tennessee: Memphis metro area (see 615, 931, split 731)")
       ("902" "NS" "-4" "   Canada: Nova Scotia, Prince Edward Island")
       ("903" "TX" "-6" "   NE Texas: Tyler (see overlay 430, eff 7/20/02)")
       ("904" "FL" "-5" "   N Florida: Jacksonville (see splits 352, 386, 850)")
       ("905" "ON" "-5" "   Canada: S Cent. Ontario: Greater Toronto Area -- Durham, Halton, Hamilton-Wentworth, Niagara, Peel, York, and southern Simcoe County (excluding Toronto -- see overlay 289 [eff 6/9/01], splits 416, 647)")
       ("906" "MI" "-6/-5" "   Upper Peninsula Michigan: Sault Ste. Marie, Escanaba, Marquette (UTC-6 towards the WI border)")
       ("907" "AK" "-9" "   Alaska")
       ("908" "NJ" "-5" "   Cent. New Jersey: Elizabeth, Basking Ridge, Somerville, Bridgewater, Bound Brook")
       ("909" "CA" "-8" "   California: Inland empire: San Bernardino (see split 951), Riverside")
       ("910" "NC" "-5" "   S Cent. North Carolina: Fayetteville, Wilmington (see 336)")
       ("911" "--" "--" "   Emergency")
       ("912" "GA" "-5" "   SE Georgia: Savannah (see splits 229, 478)")
       ("913" "KS" "-6" "   Kansas: Kansas City area (see 785)")
       ("914" "NY" "-5" "   S New York: Westchester County (see 845)")
       ("915" "TX" "-7/-6" "   W Texas: El Paso (see splits 325 eff 4/5/03; 432, eff 4/5/03)")
       ("916" "CA" "-8" "   NE California: Sacramento, Walnut Grove, Lincoln, Newcastle and El Dorado Hills (split to 530)")
       ("917" "NY" "-5" "   New York: New York City (cellular, see 646)")
       ("918" "OK" "-6" "   E Oklahoma: Tulsa")
       ("919" "NC" "-5" "   E North Carolina: Raleigh (see split 252, overlay 984)")
       ("920" "WI" "-6" "   NE Wisconsin: Appleton, Green Bay, Sheboygan, Fond du Lac (from Beaver Dam NE to Oshkosh, Appleton, and Door County; part of what used to be 414)")
       ("925" "CA" "-8" "   California: Contra Costa area: Antioch, Concord, Pleasanton, Walnut Creek (split from 510)")
       ("927" "FL" "-5" "   Florida: Cellular coverage in Orlando area")
       ("928" "AZ" "-7*" "   Central and Northern Arizona: Prescott, Flagstaff, Yuma (split from 520)")
       ("931" "TN" "-6" "   Middle Tennessee: semi-circular ring around Nashville (split from 615)")
       ("935" "CA" "-8" "   S California: San Diego (see split 760; overlay 858, 619; assigned but not in use)")
       ("936" "TX" "-6" "   SE Texas: Conroe, Lufkin, Nacogdoches, Crockett (split from 409, see also 979)")
       ("937" "OH" "-5" "   SW Ohio: Dayton (part of what used to be 513)")
       ("939" "PR" "-4*" "   Puerto Rico (overlaid on 787, perm 8/1/01)")
       ("940" "TX" "-6" "   N Cent. Texas: Denton, Wichita Falls (split from 254, 817)")
       ("941" "FL" "-5" "   SW Florida: Sarasota and Manatee counties (part of what used to be 813; see split 863)")
       ("947" "MI" "-5/-6" "   Michigan: Oakland County (overlays 248, perm 5/5/01)")
       ("949" "CA" "-8" "   California: S Coastal Orange County (split from 714)")
       ("951" "CA" "-8" "   California: W Riverside County (split from 909; eff 7/17/04)")
       ("952" "MN" "-6" "   Minnesota: Minneapolis SW, Bloomington (split from 612; see also 763)")
       ("954" "FL" "-5" "   Florida: Broward County area, incl Ft. Lauderdale (part of what used to be 305, see overlay 754)")
       ("956" "TX" "-6" "   Texas: Valley of Texas area; Harlingen, Laredo (split from 210)")
       ("957" "NM" "-7" "   New Mexico (pending; region unknown)")
       ("959" "CT" "-5" "   Connecticut: Hartford, New London (postponed; was overlaid on 860 perm 1/6/01; mand 3/1/01???)")
       ("970" "CO" "-7" "   N and W Colorado (part of what used to be 303)")
       ("971" "OR" "-8" "   Oregon:  Metropolitan Portland, Salem/Keizer area, incl Cricket Wireless (see 503; perm 10/1/00)")
       ("972" "TX" "-6" "   Texas: Dallas Metro (overlays 214/469)")
       ("973" "NJ" "-5" "   N New Jersey: Newark, Paterson, Morristown (see overlay 862; split from 201)")
       ("975" "MO" "-6" "   N Missouri: Kansas City (overlaid on 816)")
       ("976" "--" "--" "   Unassigned")
       ("978" "MA" "-5" "   Massachusetts: north of Boston to NH (see split 978 -- this is the northern half of old 508; see overlay 351)")
       ("979" "TX" "-6" "   SE Texas: Bryan, College Station, Bay City (split from 409, see also 936)")
       ("980" "NC" "-5" "   North Carolina: (overlay on 704; perm 5/1/00, mand 3/15/01)")
       ("984" "NC" "-5" "   E North Carolina: Raleigh (overlaid on 919, perm 8/1/01, mand 2/5/02 POSTPONED)")
       ("985" "LA" "-6" "   E Louisiana: SE/N shore of Lake Pontchartrain: Hammond, Slidell, Covington, Amite, Kentwood, area SW of New Orleans, Houma, Thibodaux, Morgan City (split from 504; perm 2/12/01; mand 10/22/01)")
       ("989" "MI" "-5" "   Upper central Michigan: Mt Pleasant, Saginaw (split from 517; perm 4/7/01)")
       ("999" "--" "--" "   Often used by carriers to indicate that the area code information is unavailable for CNID, even though the rest of the number is present")))

(def *us-area-codes* (map #(nth % 0)
                          (drop 2 *us-area-code-detail*)))

(def *iso-3-country-codes*
     [["ABW" "Aruba"]
      ["AFG" "Afghanistan"]
      ["AGO" "Angola"]
      ["AIA" "Anguilla"]
      ["ALA" "Åland Islands"]
      ["ALB" "Albania"]
      ["AND" "Andorra"]
      ["ANT" "Netherlands Antilles"]
      ["ARE" "United Arab Emirates"]
      ["ARG" "Argentina"]
      ["ARM" "Armenia"]
      ["ASM" "American Samoa"]
      ["ATA" "Antarctica"]
      ["ATF" "French Southern Territories"]
      ["ATG" "Antigua and Barbuda"]
      ["AUS" "Australia"]
      ["AUT" "Austria"]
      ["AZE" "Azerbaijan"]
      ["BDI" "Burundi"]
      ["BEL" "Belgium"]
      ["BEN" "Benin"]
      ["BFA" "Burkina Faso"]
      ["BGD" "Bangladesh"]
      ["BGR" "Bulgaria"]
      ["BHR" "Bahrain"]
      ["BHS" "Bahamas"]
      ["BIH" "Bosnia and Herzegovina"]
      ["BLM" "Saint Barthélemy"]
      ["BLR" "Belarus"]
      ["BLZ" "Belize"]
      ["BMU" "Bermuda"]
      ["BOL" "Bolivia, Plurinational State of"]
      ["BRA" "Brazil"]
      ["BRB" "Barbados"]
      ["BRN" "Brunei Darussalam"]
      ["BTN" "Bhutan"]
      ["BVT" "Bouvet Island"]
      ["BWA" "Botswana"]
      ["CAF" "Central African Republic"]
      ["CAN" "Canada"]
      ["CCK" "Cocos (Keeling) Islands"]
      ["CHE" "Switzerland"]
      ["CHL" "Chile"]
      ["CHN" "China"]
      ["CIV" "Côte d'Ivoire"]
      ["CMR" "Cameroon"]
      ["COD" "Congo, the Democratic Republic of the"]
      ["COG" "Congo"]
      ["COK" "Cook Islands"]
      ["COL" "Colombia"]
      ["COM" "Comoros"]
      ["CPV" "Cape Verde"]
      ["CRI" "Costa Rica"]
      ["CUB" "Cuba"]
      ["CXR" "Christmas Island"]
      ["CYM" "Cayman Islands"]
      ["CYP" "Cyprus"]
      ["CZE" "Czech Republic"]
      ["DEU" "Germany"]
      ["DJI" "Djibouti"]
      ["DMA" "Dominica"]
      ["DNK" "Denmark"]
      ["DOM" "Dominican Republic"]
      ["DZA" "Algeria"]
      ["ECU" "Ecuador"]
      ["EGY" "Egypt"]
      ["ERI" "Eritrea"]
      ["ESH" "Western Sahara"]
      ["ESP" "Spain"]
      ["EST" "Estonia"]
      ["ETH" "Ethiopia"]
      ["FIN" "Finland"]
      ["FJI" "Fiji"]
      ["FLK" "Falkland Islands (Malvinas)"]
      ["FRA" "France"]
      ["FRO" "Faroe Islands"]
      ["FSM" "Micronesia, Federated States of"]
      ["GAB" "Gabon"]
      ["GBR" "United Kingdom"]
      ["GEO" "Georgia"]
      ["GGY" "Guernsey"]
      ["GHA" "Ghana"]
      ["GIB" "Gibraltar"]
      ["GIN" "Guinea"]
      ["GLP" "Guadeloupe"]
      ["GMB" "Gambia"]
      ["GNB" "Guinea-Bissau"]
      ["GNQ" "Equatorial Guinea"]
      ["GRC" "Greece"]
      ["GRD" "Grenada"]
      ["GRL" "Greenland"]
      ["GTM" "Guatemala"]
      ["GUF" "French Guiana"]
      ["GUM" "Guam"]
      ["GUY" "Guyana"]
      ["HKG" "Hong Kong"]
      ["HMD" "Heard Island and McDonald Islands"]
      ["HND" "Honduras"]
      ["HRV" "Croatia"]
      ["HTI" "Haiti"]
      ["HUN" "Hungary"]
      ["IDN" "Indonesia"]
      ["IMN" "Isle of Man"]
      ["IND" "India"]
      ["IOT" "British Indian Ocean Territory"]
      ["IRL" "Ireland"]
      ["IRN" "Iran, Islamic Republic of"]
      ["IRQ" "Iraq"]
      ["ISL" "Iceland"]
      ["ISR" "Israel"]
      ["ITA" "Italy"]
      ["JAM" "Jamaica"]
      ["JEY" "Jersey"]
      ["JOR" "Jordan"]
      ["JPN" "Japan"]
      ["KAZ" "Kazakhstan"]
      ["KEN" "Kenya"]
      ["KGZ" "Kyrgyzstan"]
      ["KHM" "Cambodia"]
      ["KIR" "Kiribati"]
      ["KNA" "Saint Kitts and Nevis"]
      ["KOR" "Korea, Republic of"]
      ["KWT" "Kuwait"]
      ["LAO" "Lao People's Democratic Republic"]
      ["LBN" "Lebanon"]
      ["LBR" "Liberia"]
      ["LBY" "Libyan Arab Jamahiriya"]
      ["LCA" "Saint Lucia"]
      ["LIE" "Liechtenstein"]
      ["LKA" "Sri Lanka"]
      ["LSO" "Lesotho"]
      ["LTU" "Lithuania"]
      ["LUX" "Luxembourg"]
      ["LVA" "Latvia"]
      ["MAC" "Macao"]
      ["MAF" "Saint Martin (French part)"]
      ["MAR" "Morocco"]
      ["MCO" "Monaco"]
      ["MDA" "Moldova, Republic of"]
      ["MDG" "Madagascar"]
      ["MDV" "Maldives"]
      ["MEX" "Mexico"]
      ["MHL" "Marshall Islands"]
      ["MKD" "Macedonia, the former Yugoslav Republic of"]
      ["MLI" "Mali"]
      ["MLT" "Malta"]
      ["MMR" "Myanmar"]
      ["MNE" "Montenegro"]
      ["MNG" "Mongolia"]
      ["MNP" "Northern Mariana Islands"]
      ["MOZ" "Mozambique"]
      ["MRT" "Mauritania"]
      ["MSR" "Montserrat"]
      ["MTQ" "Martinique"]
      ["MUS" "Mauritius"]
      ["MWI" "Malawi"]
      ["MYS" "Malaysia"]
      ["MYT" "Mayotte"]
      ["NAM" "Namibia"]
      ["NCL" "New Caledonia"]
      ["NER" "Niger"]
      ["NFK" "Norfolk Island"]
      ["NGA" "Nigeria"]
      ["NIC" "Nicaragua"]
      ["NIU" "Niue"]
      ["NLD" "Netherlands"]
      ["NOR" "Norway"]
      ["NPL" "Nepal"]
      ["NRU" "Nauru"]
      ["NZL" "New Zealand"]
      ["OMN" "Oman"]
      ["PAK" "Pakistan"]
      ["PAN" "Panama"]
      ["PCN" "Pitcairn"]
      ["PER" "Peru"]
      ["PHL" "Philippines"]
      ["PLW" "Palau"]
      ["PNG" "Papua New Guinea"]
      ["POL" "Poland"]
      ["PRI" "Puerto Rico"]
      ["PRK" "Korea, Democratic People's Republic of"]
      ["PRT" "Portugal"]
      ["PRY" "Paraguay"]
      ["PSE" "Palestinian Territory, Occupied"]
      ["PYF" "French Polynesia"]
      ["QAT" "Qatar"]
      ["REU" "Réunion"]
      ["ROU" "Romania"]
      ["RUS" "Russian Federation"]
      ["RWA" "Rwanda"]
      ["SAU" "Saudi Arabia"]
      ["SDN" "Sudan"]
      ["SEN" "Senegal"]
      ["SGP" "Singapore"]
      ["SGS" "South Georgia and the South Sandwich Islands"]
      ["SHN" "Saint Helena, Ascension and Tristan da Cunha"]
      ["SJM" "Svalbard and Jan Mayen"]
      ["SLB" "Solomon Islands"]
      ["SLE" "Sierra Leone"]
      ["SLV" "El Salvador"]
      ["SMR" "San Marino"]
      ["SOM" "Somalia"]
      ["SPM" "Saint Pierre and Miquelon"]
      ["SRB" "Serbia"]
      ["STP" "Sao Tome and Principe"]
      ["SUR" "Suriname"]
      ["SVK" "Slovakia"]
      ["SVN" "Slovenia"]
      ["SWE" "Sweden"]
      ["SWZ" "Swaziland"]
      ["SYC" "Seychelles"]
      ["SYR" "Syrian Arab Republic"]
      ["TCA" "Turks and Caicos Islands"]
      ["TCD" "Chad"]
      ["TGO" "Togo"]
      ["THA" "Thailand"]
      ["TJK" "Tajikistan"]
      ["TKL" "Tokelau"]
      ["TKM" "Turkmenistan"]
      ["TLS" "Timor-Leste"]
      ["TON" "Tonga"]
      ["TTO" "Trinidad and Tobago"]
      ["TUN" "Tunisia"]
      ["TUR" "Turkey"]
      ["TUV" "Tuvalu"]
      ["TWN" "Taiwan, Province of China"]
      ["TZA" "Tanzania, United Republic of"]
      ["UGA" "Uganda"]
      ["UKR" "Ukraine"]
      ["UMI" "United States Minor Outlying Islands"]
      ["URY" "Uruguay"]
      ["USA" "United States"]
      ["UZB" "Uzbekistan"]
      ["VAT" "Holy See (Vatican City State)"]
      ["VCT" "Saint Vincent and the Grenadines"]
      ["VEN" "Venezuela, Bolivarian Republic of"]
      ["VGB" "Virgin Islands, British"]
      ["VIR" "Virgin Islands, U.S."]
      ["VNM" "Viet Nam"]
      ["VUT" "Vanuatu"]
      ["WLF" "Wallis and Futuna"]
      ["WSM" "Samoa"]
      ["YEM" "Yemen"]
      ["ZAF" "South Africa"]
      ["ZMB" "Zambia"]
      ["ZWE" "Zimbabwe"]])

(def *iso-2-country-codes*
     [[ "AF"  "AFGHANISTAN" ]
      [ "AX"  "ÅLAND ISLANDS" ]
      [ "AL"  "ALBANIA" ]
      [ "DZ"  "ALGERIA" ]
      [ "AS"  "AMERICAN SAMOA" ]
      [ "AD"  "ANDORRA" ]
      [ "AO"  "ANGOLA" ]
      [ "AI"  "ANGUILLA" ]
      [ "AQ"  "ANTARCTICA" ]
      [ "AG"  "ANTIGUA AND BARBUDA" ]
      [ "AR"  "ARGENTINA" ]
      [ "AM"  "ARMENIA" ]
      [ "AW"  "ARUBA" ]
      [ "AU"  "AUSTRALIA" ]
      [ "AT"  "AUSTRIA" ]
      [ "AZ"  "AZERBAIJAN" ]
      [ "BS"  "BAHAMAS" ]
      [ "BH"  "BAHRAIN" ]
      [ "BD"  "BANGLADESH" ]
      [ "BB"  "BARBADOS" ]
      [ "BY"  "BELARUS" ]
      [ "BE"  "BELGIUM" ]
      [ "BZ"  "BELIZE" ]
      [ "BJ"  "BENIN" ]
      [ "BM"  "BERMUDA" ]
      [ "BT"  "BHUTAN" ]
      [ "BO"  "BOLIVIA, PLURINATIONAL STATE OF" ]
      [ "BA"  "BOSNIA AND HERZEGOVINA" ]
      [ "BW"  "BOTSWANA" ]
      [ "BV"  "BOUVET ISLAND" ]
      [ "BR"  "BRAZIL" ]
      [ "IO"  "BRITISH INDIAN OCEAN TERRITORY" ]
      [ "BN"  "BRUNEI DARUSSALAM" ]
      [ "BG"  "BULGARIA" ]
      [ "BF"  "BURKINA FASO" ]
      [ "BI"  "BURUNDI" ]
      [ "KH"  "CAMBODIA" ]
      [ "CM"  "CAMEROON" ]
      [ "CA"  "CANADA" ]
      [ "CV"  "CAPE VERDE" ]
      [ "KY"  "CAYMAN ISLANDS" ]
      [ "CF"  "CENTRAL AFRICAN REPUBLIC" ]
      [ "TD"  "CHAD" ]
      [ "CL"  "CHILE" ]
      [ "CN"  "CHINA" ]
      [ "CX"  "CHRISTMAS ISLAND" ]
      [ "CC"  "COCOS (KEELING) ISLANDS" ]
      [ "CO"  "COLOMBIA" ]
      [ "KM"  "COMOROS" ]
      [ "CG"  "CONGO" ]
      [ "CD"  "CONGO, THE DEMOCRATIC REPUBLIC OF THE" ]
      [ "CK"  "COOK ISLANDS" ]
      [ "CR"  "COSTA RICA" ]
      [ "CI"  "CÔTE D'IVOIRE" ]
      [ "HR"  "CROATIA" ]
      [ "CU"  "CUBA" ]
      [ "CY"  "CYPRUS" ]
      [ "CZ"  "CZECH REPUBLIC" ]
      [ "DK"  "DENMARK" ]
      [ "DJ"  "DJIBOUTI" ]
      [ "DM"  "DOMINICA" ]
      [ "DO"  "DOMINICAN REPUBLIC" ]
      [ "EC"  "ECUADOR" ]
      [ "EG"  "EGYPT" ]
      [ "SV"  "EL SALVADOR" ]
      [ "GQ"  "EQUATORIAL GUINEA" ]
      [ "ER"  "ERITREA" ]
      [ "EE"  "ESTONIA" ]
      [ "ET"  "ETHIOPIA" ]
      [ "FK"  "FALKLAND ISLANDS (MALVINAS)" ]
      [ "FO"  "FAROE ISLANDS" ]
      [ "FJ"  "FIJI" ]
      [ "FI"  "FINLAND" ]
      [ "FR"  "FRANCE" ]
      [ "GF"  "FRENCH GUIANA" ]
      [ "PF"  "FRENCH POLYNESIA" ]
      [ "TF"  "FRENCH SOUTHERN TERRITORIES" ]
      [ "GA"  "GABON" ]
      [ "GM"  "GAMBIA" ]
      [ "GE"  "GEORGIA" ]
      [ "DE"  "GERMANY" ]
      [ "GH"  "GHANA" ]
      [ "GI"  "GIBRALTAR" ]
      [ "GR"  "GREECE" ]
      [ "GL"  "GREENLAND" ]
      [ "GD"  "GRENADA" ]
      [ "GP"  "GUADELOUPE" ]
      [ "GU"  "GUAM" ]
      [ "GT"  "GUATEMALA" ]
      [ "GG"  "GUERNSEY" ]
      [ "GN"  "GUINEA" ]
      [ "GW"  "GUINEA-BISSAU" ]
      [ "GY"  "GUYANA" ]
      [ "HT"  "HAITI" ]
      [ "HM"  "HEARD ISLAND AND MCDONALD ISLANDS" ]
      [ "VA"  "HOLY SEE (VATICAN CITY STATE)" ]
      [ "HN"  "HONDURAS" ]
      [ "HK"  "HONG KONG" ]
      [ "HU"  "HUNGARY" ]
      [ "IS"  "ICELAND" ]
      [ "IN"  "INDIA" ]
      [ "ID"  "INDONESIA" ]
      [ "IR"  "IRAN, ISLAMIC REPUBLIC OF" ]
      [ "IQ"  "IRAQ" ]
      [ "IE"  "IRELAND" ]
      [ "IM"  "ISLE OF MAN" ]
      [ "IL"  "ISRAEL" ]
      [ "IT"  "ITALY" ]
      [ "JM"  "JAMAICA" ]
      [ "JP"  "JAPAN" ]
      [ "JE"  "JERSEY" ]
      [ "JO"  "JORDAN" ]
      [ "KZ"  "KAZAKHSTAN" ]
      [ "KE"  "KENYA" ]
      [ "KI"  "KIRIBATI" ]
      [ "KP"  "KOREA, DEMOCRATIC PEOPLE'S REPUBLIC OF" ]
      [ "KR"  "KOREA, REPUBLIC OF" ]
      [ "KW"  "KUWAIT" ]
      [ "KG"  "KYRGYZSTAN" ]
      [ "LA"  "LAO PEOPLE'S DEMOCRATIC REPUBLIC" ]
      [ "LV"  "LATVIA" ]
      [ "LB"  "LEBANON" ]
      [ "LS"  "LESOTHO" ]
      [ "LR"  "LIBERIA" ]
      [ "LY"  "LIBYAN ARAB JAMAHIRIYA" ]
      [ "LI"  "LIECHTENSTEIN" ]
      [ "LT"  "LITHUANIA" ]
      [ "LU"  "LUXEMBOURG" ]
      [ "MO"  "MACAO" ]
      [ "MK"  "MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF" ]
      [ "MG"  "MADAGASCAR" ]
      [ "MW"  "MALAWI" ]
      [ "MY"  "MALAYSIA" ]
      [ "MV"  "MALDIVES" ]
      [ "ML"  "MALI" ]
      [ "MT"  "MALTA" ]
      [ "MH"  "MARSHALL ISLANDS" ]
      [ "MQ"  "MARTINIQUE" ]
      [ "MR"  "MAURITANIA" ]
      [ "MU"  "MAURITIUS" ]
      [ "YT"  "MAYOTTE" ]
      [ "MX"  "MEXICO" ]
      [ "FM"  "MICRONESIA, FEDERATED STATES OF" ]
      [ "MD"  "MOLDOVA, REPUBLIC OF" ]
      [ "MC"  "MONACO" ]
      [ "MN"  "MONGOLIA" ]
      [ "ME"  "MONTENEGRO" ]
      [ "MS"  "MONTSERRAT" ]
      [ "MA"  "MOROCCO" ]
      [ "MZ"  "MOZAMBIQUE" ]
      [ "MM"  "MYANMAR" ]
      [ "NA"  "NAMIBIA" ]
      [ "NR"  "NAURU" ]
      [ "NP"  "NEPAL" ]
      [ "NL"  "NETHERLANDS" ]
      [ "AN"  "NETHERLANDS ANTILLES" ]
      [ "NC"  "NEW CALEDONIA" ]
      [ "NZ"  "NEW ZEALAND" ]
      [ "NI"  "NICARAGUA" ]
      [ "NE"  "NIGER" ]
      [ "NG"  "NIGERIA" ]
      [ "NU"  "NIUE" ]
      [ "NF"  "NORFOLK ISLAND" ]
      [ "MP"  "NORTHERN MARIANA ISLANDS" ]
      [ "NO"  "NORWAY" ]
      [ "OM"  "OMAN" ]
      [ "PK"  "PAKISTAN" ]
      [ "PW"  "PALAU" ]
      [ "PS"  "PALESTINIAN TERRITORY, OCCUPIED" ]
      [ "PA"  "PANAMA" ]
      [ "PG"  "PAPUA NEW GUINEA" ]
      [ "PY"  "PARAGUAY" ]
      [ "PE"  "PERU" ]
      [ "PH"  "PHILIPPINES" ]
      [ "PN"  "PITCAIRN" ]
      [ "PL"  "POLAND" ]
      [ "PT"  "PORTUGAL" ]
      [ "PR"  "PUERTO RICO" ]
      [ "QA"  "QATAR" ]
      [ "RE"  "RÉUNION" ]
      [ "RO"  "ROMANIA" ]
      [ "RU"  "RUSSIAN FEDERATION" ]
      [ "RW"  "RWANDA" ]
      [ "BL"  "SAINT BARTHÉLEMY" ]
      [ "SH"  "SAINT HELENA, ASCENSION AND TRISTAN DA CUNHA" ]
      [ "KN"  "SAINT KITTS AND NEVIS" ]
      [ "LC"  "SAINT LUCIA" ]
      [ "MF"  "SAINT MARTIN" ]
      [ "PM"  "SAINT PIERRE AND MIQUELON" ]
      [ "VC"  "SAINT VINCENT AND THE GRENADINES" ]
      [ "WS"  "SAMOA" ]
      [ "SM"  "SAN MARINO" ]
      [ "ST"  "SAO TOME AND PRINCIPE" ]
      [ "SA"  "SAUDI ARABIA" ]
      [ "SN"  "SENEGAL" ]
      [ "RS"  "SERBIA" ]
      [ "SC"  "SEYCHELLES" ]
      [ "SL"  "SIERRA LEONE" ]
      [ "SG"  "SINGAPORE" ]
      [ "SK"  "SLOVAKIA" ]
      [ "SI"  "SLOVENIA" ]
      [ "SB"  "SOLOMON ISLANDS" ]
      [ "SO"  "SOMALIA" ]
      [ "ZA"  "SOUTH AFRICA" ]
      [ "GS"  "SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS" ]
      [ "ES"  "SPAIN" ]
      [ "LK"  "SRI LANKA" ]
      [ "SD"  "SUDAN" ]
      [ "SR"  "SURINAME" ]
      [ "SJ"  "SVALBARD AND JAN MAYEN" ]
      [ "SZ"  "SWAZILAND" ]
      [ "SE"  "SWEDEN" ]
      [ "CH"  "SWITZERLAND" ]
      [ "SY"  "SYRIAN ARAB REPUBLIC" ]
      [ "TW"  "TAIWAN, PROVINCE OF CHINA" ]
      [ "TJ"  "TAJIKISTAN" ]
      [ "TZ"  "TANZANIA, UNITED REPUBLIC OF" ]
      [ "TH"  "THAILAND" ]
      [ "TL"  "TIMOR-LESTE" ]
      [ "TG"  "TOGO" ]
      [ "TK"  "TOKELAU" ]
      [ "TO"  "TONGA" ]
      [ "TT"  "TRINIDAD AND TOBAGO" ]
      [ "TN"  "TUNISIA" ]
      [ "TR"  "TURKEY" ]
      [ "TM"  "TURKMENISTAN" ]
      [ "TC"  "TURKS AND CAICOS ISLANDS" ]
      [ "TV"  "TUVALU" ]
      [ "UG"  "UGANDA" ]
      [ "UA"  "UKRAINE" ]
      [ "AE"  "UNITED ARAB EMIRATES" ]
      [ "GB"  "UNITED KINGDOM" ]
      [ "US"  "UNITED STATES" ]
      [ "UM"  "UNITED STATES MINOR OUTLYING ISLANDS" ]
      [ "UY"  "URUGUAY" ]
      [ "UZ"  "UZBEKISTAN" ]
      [ "VU"  "VANUATU" ]
      [ "VE"  "VENEZUELA, BOLIVARIAN REPUBLIC OF" ]
      [ "VN"  "VIET NAM" ]
      [ "VG"  "VIRGIN ISLANDS, BRITISH" ]
      [ "VI"  "VIRGIN ISLANDS, U.S." ]
      [ "WF"  "WALLIS AND FUTUNA" ]
      [ "EH"  "WESTERN SAHARA" ]
      [ "YE"  "YEMEN" ]
      [ "ZM"  "ZAMBIA" ]
      [ "ZW"  "ZIMBABWE" ]])
