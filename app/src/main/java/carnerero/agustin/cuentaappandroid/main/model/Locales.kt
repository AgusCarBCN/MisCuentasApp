package carnerero.agustin.cuentaappandroid.main.model

import java.util.Locale
// Obtener el locale correspondiente



val currencyLocales = mapOf(
    "AED" to Locale("ar", "AE"), // UAE Dirham
    "AFN" to Locale("ps", "AF"), // Afghan Afghani
    "ALL" to Locale("sq", "AL"), // Albanian Lek
    "AMD" to Locale("hy", "AM"), // Armenian Dram
    "AOA" to Locale("pt", "AO"), // Angolan Kwanza
    "ARS" to Locale("es", "AR"), // Peso Argentino
    "AUD" to Locale("en", "AU"), // Australian Dollar
    "AWG" to Locale("nl", "AW"), // Aruban Florin
    "AZN" to Locale("az", "AZ"), // Azerbaijani Manat
    "BAM" to Locale("bs", "BA"), // Bosnia-Herzegovina Convertible Mark
    "BBD" to Locale("en", "BB"), // Barbadian Dollar
    "BDT" to Locale("bn", "BD"), // Bangladeshi Taka
    "BGN" to Locale("bg", "BG"), // Bulgarian Lev
    "BHD" to Locale("ar", "BH"), // Bahraini Dinar
    "BIF" to Locale("fr", "BI"), // Burundian Franc
    "BMD" to Locale("en", "BM"), // Bermudian Dollar
    "BND" to Locale("ms", "BN"), // Brunei Dollar
    "BOB" to Locale("es", "BO"), // Bolivian Boliviano
    "BRL" to Locale("pt", "BR"), // Real brasileño
    "BSD" to Locale("en", "BS"), // Bahamian Dollar
    "BTN" to Locale("dz", "BT"), // Bhutanese Ngultrum
    "BWP" to Locale("en", "BW"), // Botswana Pula
    "BYN" to Locale("be", "BY"), // Belarusian Ruble
    "BZD" to Locale("bz", "BZ"), // Belize Dollar
    "CAD" to Locale("en", "CA"), // Canadian Dollar
    "CDF" to Locale("fr", "CD"), // Congolese Franc
    "CHF" to Locale("de", "CH"), // Swiss Franc
    "CLP" to Locale("es", "CL"), // Peso chileno
    "CNY" to Locale("zh", "CN"), // Yuan chino
    "COP" to Locale("es", "CO"), // Peso colombiano
    "CRC" to Locale("es", "CR"), // Costa Rican Colón
    "CUP" to Locale("es", "CU"), // Cuban Peso
    "CVE" to Locale("pt", "CV"), // Cape Verdean Escudo
    "CZK" to Locale("cs", "CZ"), // Czech Koruna
    "DJF" to Locale("fr", "DJ"), // Djiboutian Franc
    "DKK" to Locale("da", "DK"), // Danish Krone
    "DOP" to Locale("es", "DO"), // Dominican Peso
    "DZD" to Locale("ar", "DZ"), // Algerian Dinar
    "EGP" to Locale("ar", "EG"), // Egyptian Pound
    "ERN" to Locale("ti", "ER"), // Eritrean Nakfa
    "ETB" to Locale("am", "ET"), // Ethiopian Birr
    "EUR" to Locale("es", "ES"), // Euro
    "FJD" to Locale("en", "FJ"), // Fijian Dollar
    "FKP" to Locale("en", "FK"), // Falkland Islands Pound
    "FOK" to Locale("fo", "FO"), // Faroese Króna
    "GBP" to Locale("en", "GB"), // British Pound
    "GEL" to Locale("ka", "GE"), // Georgian Lari
    "GHS" to Locale("ak", "GH"), // Ghanaian Cedi
    "GIP" to Locale("en", "GI"), // Gibraltar Pound
    "GMD" to Locale("en", "GM"), // Gambian Dalasi
    "GNF" to Locale("fr", "GN"), // Guinean Franc
    "GTQ" to Locale("es", "GT"), // Guatemalan Quetzal
    "GYD" to Locale("en", "GY"), // Guyanese Dollar
    "HKD" to Locale("zh", "HK"), // Hong Kong Dollar
    "HNL" to Locale("es", "HN"), // Lempira de Honduras
    "HRK" to Locale("hr", "HR"), // Croatian Kuna
    "HTG" to Locale("ht", "HT"), // Haitian Gourde
    "HUF" to Locale("hu", "HU"), // Hungarian Forint
    "IDR" to Locale("id", "ID"), // Indonesian Rupiah
    "ILS" to Locale("he", "IL"), // Israeli Shekel
    "INR" to Locale("hi", "IN"), // Indian Rupee
    "IQD" to Locale("ar", "IQ"), // Iraqi Dinar
    "IRR" to Locale("fa", "IR"), // Iranian Rial
    "ISK" to Locale("is", "IS"), // Icelandic Krona
    "JMD" to Locale("en", "JM"), // Jamaican Dollar
    "JOD" to Locale("ar", "JO"), // Jordanian Dinar
    "JPY" to Locale("ja", "JP"), // Yen japonés
    "KES" to Locale("en", "KE"), // Kenyan Shilling
    "KGS" to Locale("ky", "KG"), // Kyrgyzstani Som
    "KHR" to Locale("km", "KH"), // Cambodian Riel
    "KMF" to Locale("fr", "KM"), // Comorian Franc
    "KRW" to Locale("ko", "KR"), // South Korean Won
    "KWD" to Locale("ar", "KW"), // Kuwaiti Dinar
    "KYD" to Locale("en", "KY"), // Cayman Islands Dollar
    "KZT" to Locale("kk", "KZ"), // Kazakhstani Tenge
    "LAK" to Locale("lo", "LA"), // Lao Kip
    "LBP" to Locale("ar", "LB"), // Lebanese Pound
    "LKR" to Locale("si", "LK"), // Sri Lankan Rupee
    "LRD" to Locale("en", "LR"), // Liberian Dollar
    "LSL" to Locale("en", "LS"), // Lesotho Loti
    "LYD" to Locale("ly", "LY"), // Libyan Dinar
    "MAD" to Locale("ar", "MA"), // Moroccan Dirham
    "MDL" to Locale("ro", "MD"), // Moldovan Leu
    "MGA" to Locale("mg", "MG"), // Malagasy Ariary
    "MKD" to Locale("mk", "MK"), // Macedonian Denar
    "MMK" to Locale("my", "MM"), // Burmese Kyat
    "MNT" to Locale("mn", "MN"), // Mongolian Tögrög
    "MOP" to Locale("pt", "MO"), // Macanese Pataca
    "MRU" to Locale("mr", "MR"), // Mauritanian Ouguiya
    "MUR" to Locale("en", "MU"), // Mauritian Rupee
    "MVR" to Locale("dv", "MV"), // Maldivian Rufiyaa
    "MWK" to Locale("en", "MW"), // Malawian Kwacha
    "MXN" to Locale("es", "MX"), // Peso mexicano
    "MYR" to Locale("ms", "MY"), // Malaysian Ringgit
    "MZN" to Locale("pt", "MZ"), // Mozambican Metical
    "NAD" to Locale("en", "NA"), // Namibian Dollar
    "NGN" to Locale("en", "NG"), // Nigerian Naira
    "NIO" to Locale("es", "NI"), // Nicaraguan Córdoba
    "NOK" to Locale("no", "NO"), // Norwegian Krone
    "NPR" to Locale("ne", "NP"), // Nepalese Rupee
    "NZD" to Locale("en", "NZ"), // New Zealand Dollar
    "OMR" to Locale("ar", "OM"), // Omani Rial
    "PAB" to Locale("es", "PA"), // Panamanian Balboa
    "PEN" to Locale("es", "PE"), // Sol peruano
    "PGK" to Locale("en", "PG"), // Papua New Guinean Kina
    "PHP" to Locale("en", "PH"), // Peso filipino
    "PKR" to Locale("ur", "PK"), // Pakistani Rupee
    "PLN" to Locale("pl", "PL"), // Polish Zloty
    "PYG" to Locale("gn", "PY"), // Paraguayan Guarani
    "QAR" to Locale("ar", "QA"), // Qatari Riyal
    "RON" to Locale("ro", "RO"), // Romanian Leu
    "RSD" to Locale("sr", "RS"), // Serbian Dinar
    "RUB" to Locale("ru", "RU"), // Russian Ruble
    "RWF" to Locale("rw", "RW"), // Rwandan Franc
    "SAR" to Locale("ar", "SA"), // Saudi Riyal
    "SBD" to Locale("en", "SB"), // Solomon Islands Dollar
    "SCR" to Locale("en", "SC"), // Seychellois Rupee
    "SDG" to Locale("ar", "SD"), // Sudanese Pound
    "SEK" to Locale("sv", "SE"), // Swedish Krona
    "SGD" to Locale("en", "SG"), // Singapore Dollar
    "SHP" to Locale("en", "SH"), // Saint Helena Pound
    "SLL" to Locale("en", "SL"), // Sierra Leonean Leone
    "SOS" to Locale("so", "SO"), // Somali Shilling
    "SRD" to Locale("en", "SR"), // Surinamese Dollar
    "SSP" to Locale("en", "SS"), // South Sudanese Pound
    "STN" to Locale("st", "ST"), // São Tomé and Príncipe Dobra
    "SYP" to Locale("ar", "SY"), // Syrian Pound
    "SZL" to Locale("en", "SZ"), // Eswatini Lilangeni
    "THB" to Locale("th", "TH"), // Thai Baht
    "TJS" to Locale("tg", "TJ"), // Tajikistani Somoni
    "TMT" to Locale("tm", "TM"), // Turkmenistani Manat
    "TND" to Locale("ar", "TN"), // Tunisian Dinar
    "TOP" to Locale("to", "TO"), // Tongan Paʻanga
    "TRY" to Locale("tr", "TR"), // Turkish Lira
    "TTD" to Locale("en", "TT"), // Trinidad and Tobago Dollar
    "TWD" to Locale("zh", "TW"), // New Taiwan Dollar
    "TZS" to Locale("sw", "TZ"), // Tanzanian Shilling
    "UAH" to Locale("uk", "UA"), // Ukrainian Hryvnia
    "UGX" to Locale("en", "UG"), // Ugandan Shilling
    "USD" to Locale("en", "US"), // US Dollar
    "UYU" to Locale("es", "UY"), // Uruguayan Peso
    "UZS" to Locale("uz", "UZ"), // Uzbekistani Som
    "VES" to Locale("ve", "VE"), // Venezuelan Bolívar
    "VND" to Locale("vi", "VN"), // Vietnamese Dong
    "VUV" to Locale("vu", "VU"), // Vanuatu Vatu
    "WST" to Locale("sm", "WS"), // Samoan Tala
    "XAF" to Locale("fr", "CF"), // Central African CFA Franc
    "XOF" to Locale("fr", "WF"), // West African CFA Franc
    "XPF" to Locale("fr", "PF"), // CFP Franc
    "YER" to Locale("ar", "YE"), // Yemeni Rial
    "ZAR" to Locale("af", "ZA"), // South African Rand
    "ZMW" to Locale("en", "ZM"), // Zambian Kwacha
    "ZWL" to Locale("en", "ZW")  // Zimbabwean Dollar
)
val locale = currencyLocales["EUR"] ?: Locale.GERMAN
