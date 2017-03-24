package com.skoovy.android.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.skoovy.android.R;

public class SingleChoiceDialogFragment extends DialogFragment {

    public static String choice;
    private final String TAG = "AUC_COMPLEX";
    private final String[] countries = {"Afghanstan (+93) AFG",
                                        "Aland Islands (+358) ALA",
                                        "Albania (+355) ALB",
                                       // "Alderney (+44)",
                                        "Algeria (+213) ALG",
                                        "American Samoa (+1) ASM",
                                        "Andora (+376) AND",
                                        "Angola (+244) ANG",
                                        "Anguilla (+1 264) AIA",
                                        "Antarctica (672) ATA",
                                        "Antigua & Barbuda (+1 268) ATG",
                                        "Argentina (+54) ARG",
                                        "Armenia(+374) ARM",
                                        "Aruba (+297) ABW",
                                       // "Ascension Island (+247)",
                                        "Australia (+61) AUS",
                                        "Austria (+43) AUT",
                                        "Azerbaijan (+994) AZE",
                                        "Bahamas (+1 242) BHS",
                                        "Bahrain (+973) BHR",
                                        "Bangladesh (+880) BGD",
                                        "Barbados (+1 246) BRB",
                                        "Belarus (+375) BLR",
                                        "Belgium (+32) BEL",
                                        "Belize (+501) BLZ",
                                        "Benin (+229) BEN",
                                        "Bermuda (+1 441) BMU",
                                        "Bhutan (+975) BTN",
                                        "Bolivia (+591) BOL",
                                        "Bosnia and Herzegovina (+387) BIH",
                                        "Botswana (+267) BWA",
                                        "Brazil (+55) BRA",
                                        "British Indian Ocean Territory (+246) IOT",
                                        "Brunei (+673) BRU",
                                        "Bularia (+359) BUL",
                                        "Burkina Faso (+226) BUR",
                                        "Burundi (+257) BDI",
                                        "Camboida (+855) CAM",
                                        "Cameroon(+237) CMR",
                                        "Canada (+1) CAN",
                                        "Cape Verde (+248) CPV",
                                        "Caribbean Netherlands (+559) BES",
                                        "Cayman Islands (+1 345) CAY",
                                        "Central African Republic (+236) CAF",
                                        "Chad (+235) CHA",
                                        "Chile (+56) CHL",
                                        "China (+86) CHN",
                                        "Christmas Island (+61) CXR",
                                        "Cocos (Keeling) Islands (+61) CCK",
                                        "Colombia (+57) COL",
                                        "Comoros (+269) COM",
                                        "Democratic Republic of the Congo (+243) COD",
                                        "Republic of the Congo (+242) CGO",
                                        "Cook Islands (+682) COK",
                                        "Costa Rica (+506) CRC",
                                        "Croatia (+385) CRO",
                                        "Cuba (+53) CUB",
                                        "Curacao (+599) CUW",
                                        "Cyprus (+357) CYP",
                                        "Czech Republic (+420) CZE",
                                        "Denmark (+45) DNK",
                                        "Djibouti (+253) DJI",
                                        "Dominica (+1 767) DMA",
                                        "Dominican Republic (+1 809) DOM",
                                        "Dominican Republic (+1 829) DOM",
                                        "Dominican Republic (+1 849) DOM",
                                        "Ecuador (+593) ECU",
                                        "Egypt (+20) EGY",
                                        "El Salvador (+503) ESA",
                                        "Equatorial Guinea (+240) GEQ",
                                        "Eritrea (+291) ERI",
                                        "Estonia (+372) EST",
                                        "Ethiopia (+251) ETH",
                                        "Falkland Islands (+500)FLK",
                                        "Faroe Islands (+298) FRO",
                                        "Fiji (+679) FJI",
                                        "Finland (+358) FIN",
                                        "France (+33) FRA",
                                        "French Guiana (+594) GUF",
                                        "French Polynesia (+689) PYF",
                                        "Gabon (+241) GAB",
                                        "Gambia (+220) GMB",
                                        "Georgia (+995) GEO",
                                        "Germany (+49) DEU",
                                        "Ghana (+233) GHA",
                                        "Gibraltar (+350) GIB",
                                        "Greece (+30) GRE",
                                        "Greenland (+299) GRL",
                                        "Grenada (+1 473) GRD",
                                        "Guadeloupe (+590) GLP",
                                        "Guam (+1 671) GUM",
                                        "Guatemala (+502) GTM",
                                        "Guinea (+224) GIN",
                                        "Guinea-Bssau (+245) GNB",
                                        "Guyana (+592) GUY",
                                        "Haiti (+509) HTI",
                                        "Heard Island and McDonald Islands (+672) HMD",
                                        "Honduras (+504) HND",
                                        "Hong Kong (+852) HKG",
                                        "Hungary (+36) HUN",
                                        "Iceland(+354) ISL",
                                        "India (+91) IND",
                                        "Indonesia (+62) IDN",
                                        "Iran (+98) IRN",
                                        "Iraq (+964) IRQ",
                                        "Ireland (+353) IRL",
                                        "Isreal (+972) ISR",
                                        "Italy (+39) ITA",
                                        "Ivory Coast (+225) CIV",
                                        "Jamaica (+1 876) JAM",
                                        "Japan (+81) JPN",
                                        "Jersey (+44) JEY",
                                        "Jordan (+962) JOR",
                                        "Kazakhstan (+7) KAZ",
                                        "Kenya (+254) KEN",
                                        "Kiribati(+686) KIR",
                                        "North Korea (+850) PRK",
                                        "South Korea (+82) KOR",
                                        "Kosovo (+383) KOS",
                                        "Kuwait (+965) KWT",
                                        "Kyrgyzstan (+996) KGZ",
                                        "Laos (+856) LAO",
                                        "Latvia (+371) LAT",
                                        "Lebanon (+961) LBN",
                                        "Lesotho (+266) LSO",
                                        "Liberia (+231) LBR",
                                        "Lybia (+218) LBY",
                                        "Liechtenstein (+423) LTE",
                                        "Lithuania (+370) LTU",
                                        "Luxembourg (+352) LUX",
                                        "Macau (+853) MMAC",
                                        "Macedonia (+389) MKD",
                                        "Madagascar (+261) MAD",
                                        "Malawi (+265) MAW",
                                        "Malaysia (+60) MAS",
                                        "Maldives (+960) MDV",
                                        "Mali (+223) MLI",
                                        "Malta (+356) MLT",
                                        "Marshall Islands (+692) MHL",
                                        "Martinique (+596) MTQ",
                                        "Mauritania (+222) MTN",
                                        "Mauritius (+230) MRI",
                                        "Mayotte (+269) MYT",
                                        "Mexico (+52) MEX",
                                        "Federated States of Micronesia (+691) FSM",
                                        "Moldova (+373) MDA",
                                        "Monaco (+377) MON",
                                        "Mongolia (+976) MNG",
                                        "Montenegro (+382) MNE",
                                        "Montserrat (+1 664) MSR",
                                        "Morocco (+212) MAR",
                                        "Mozambique (+258) MOZ",
                                        "Myanmar (+95) MMR",
                                        "Nambia (+264) NAM",
                                        "Nauru (+674) NRU",
                                        "Nepal (+977) NEP",
                                        "Netherlands (+31) NED",
                                        "New Caledonia (+687) NCL",
                                        "New Zealand (+64) NZL",
                                        "Nicaragua (+505) NCA",
                                        "Niger (+227) NIG",
                                        "Nigeria (+234) NGR",
                                        "Niue (+683) NIU",
                                        "Norfolk Island (+672) NFK",
                                        "North Mariana Islands (+1 670) MNP",
                                        "Norway (+47) NOR",
                                        "Oman (+968) OMA",
                                        "Pakistan (+92) PAK",
                                        "Palau (+680) PLW",
                                        "Palestine (+970) PE",
                                        "Panama (+507) PAN",
                                        "Papua New Guinea (+675) PNG",
                                        "Paraguay (+595) PAR",
                                        "Peru (+51) PER",
                                        "Philippines (+63) PHI",
                                        "Pitcairn Islands (+64) PCN",
                                        "Poland (+48) POL",
                                        "Portugal (+351) POR",
                                        "Puerto Rico (+1 787) PUR",
                                        "Puerto Rico (+1 939) PUR",
                                        "Qatar (+974) QAT",
                                        "Reunion (+262) REU",
                                        "Romania (+40) ROU",
                                        "Russia (+7) RUS",
                                        "Rwanda (+250) RWA",
                                        "Saint Barthelemy (+590) BLM",
                                        "Saint Helena, Ascension and Tristan da Cunha (+290) SHN",
                                        "Saint Kitts and Nevis (+1 869) SKN",
                                        "Saint Lucia (+1 758) LCA",
                                        "Saint Martin (+590) MAF",
                                        "Saint Pierre and Miquelon (+508) SPM",
                                        "Saint Vincent and the Grenadines (+1 784) VCT",
                                        "Samoa (+685) SAM",
                                        "San Marino (+378) SMR",
                                        "Sao Tome and Principe (+239) STP",
                                        "Saudi Arabia (+966) KSA",
                                        "Senegal (+221) SEN",
                                        "Serbia (+381) SRB",
                                        "Seychelles (+248) SEY",
                                        "Sierra Leone (+232) SLE",
                                        "Singapore (+65) SGP",
                                        "Sint Maaren (+1 721) SXM",
                                        "Slovakia (+421) SVK",
                                        "Slovenia (+386) SLO",
                                        "Solomon Islands (+677) SOL",
                                        "Somalia (+252) SOM",
                                        "South Africa (+27) RSA",
                                        "South Georgia and the South Sandwich Islands (500) SGS",
                                        "South Sudan (+211) SSD",
                                        "Spain (+34) ESP",
                                        "Sri Lanka (+94) SRI",
                                        "Sudan (+2449) SUD",
                                        "Suriname (+597) SUR",
                                        "Svalbard and Jan Mayen (+47) SJM",
                                        "Swaziland (+268) SWZ",
                                        "Sweden (+46) SWE",
                                        "Switzerland (+41) SUI",
                                        "Syria (+963) SYR",
                                        "Taiwan (+886) TPE",
                                        "Tajikistan (+992) TJK",
                                        "Tanzania (+255) TAN",
                                        "Thailand (+66) THA",
                                        "Timor-Leste (+670) TLS",
                                        "Togo (+228) TOG",
                                        "Tokelau (+690) TKL",
                                        "Tonga (+676) TGA",
                                        "Trinidad and Tobago (+1 868) TTO",
                                        "Tunisia (+216) TUN",
                                        "Turkey (+90) TUR",
                                        "Turkmenistan (+993) TKM",
                                        "Turks and Caicos Islands (+1 649) TCA",
                                        "Tuvalu (+688) TUV",
                                        "Uganda (+256) UGA",
                                        "Ukraine (+380) UKR",
                                        "United Arab Emirates (+971) UAE",
                                        "United Kingdom (+44) GRB",
                                        "United States (+1) US",
                                        "Uruguay (+598) URU",
                                        "Uzbekistan (+998) UZB",
                                        "Vanuatu (+678) VAN",
                                        "Vatican City (+379) VAT",
                                        "Venezuala (+58) VEN",
                                        "Vietnam (+84) VIE",
                                        "British Virgin Islands (+1 284) IVB",
                                        "United States Virgin Islands (+1 340) ISV",
                                        "Wallis and Futuna (+681) WLF",
                                        "Western Sahara (+212) ESH",
                                        "Yemen (+967) YEM",
                                        "Zambia (+260) ZAM",
                                        "Zimbabwe (+263) ZIM"};

    //Dialog interface back to calling activity
    public interface MyDialogFragmentListener {
        void onReturnValue(String foo);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getActivity());

        builder.setIcon(R.drawable.backbuttonsmall);
        TextView title = new TextView(getActivity());
        title.setGravity(Gravity.START);
        title.setText(R.string.select_your_country_code);

        title.setTextSize(22);

        //From the countries array we just want to display the country name and country code
        String[] displayStrings = new String[countries.length];
        for (int i = 0; i < countries.length; i++) {
            String tempString = countries[i];
            Integer indexCloseParen = tempString.indexOf(")");
            String displayString = countries[i].substring(0, indexCloseParen + 1);
            displayStrings[i] = displayString;
        }

        // NOTE: setMessage doesn't work here because the list takes up the content
        // area. Use the setTitle method to set a descriptive prompt
        builder.setTitle(R.string.select_your_country_code);

        // The setItems function is used to create a list of content
        builder.setItems(displayStrings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                choice = countries[which];
                Log.i(TAG, String.format("countries chosen: %s",countries[which]));
                //once a choice has been made, pass the value back to the calling activity
                MyDialogFragmentListener activity = (MyDialogFragmentListener) getActivity();
                activity.onReturnValue(choice);
            }
        });

        // Single-choice dialogs don't need buttons because they
        // auto-dismiss when the user makes a choice

        return builder.create();
    }
}
