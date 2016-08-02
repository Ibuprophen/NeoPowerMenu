package de.NeonSoft.neopowermenu.Preferences;
import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;
import de.NeonSoft.neopowermenu.*;
import de.NeonSoft.neopowermenu.helpers.*;
import java.util.*;

public class AboutFragment extends Fragment
{

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
				// TODO: Implement this method
				MainActivity.visibleFragment = "about";
				
				MainActivity.actionbar.hideButton();
				View InflatedView = inflater.inflate(R.layout.activity_about,container,false);
				
				ListView list = (ListView) InflatedView.findViewById(R.id.activityaboutListView1);
				
				String[] titles = new String[] {"About",
																				"User Id",
																				"Used Librarys",
																				"CustomActivityOnCrash",
																				"ACRA",
																				"SmartTabLayout",
																				"HoloColorPicker",
																				"DragSortListView",
																				"libsuperuser"};
				ArrayList<String> titlesList = new ArrayList<String>(Arrays.asList(titles));

				String[] texts = new String[] {"NeoPowerMenu by Neon-Soft / DrAcHe981\n" +
																				"based on a Source from Naman Dwivedi (naman14).\n\n" +
																				"Translators:\n" +
																				"> English:\n" +
																				"Robin G. (DrAcHe981 @xda), MrWasdennnoch (@xda)\n\n" +
																				"> German:\n" +
																				"Robin G. (DrAcHe981 @xda), MrWasdennnoch (@xda)\n\n" +
																				"> Polish:\n" +
																				"Witoslavski (@forum.android.com.pl), tmacher (@xda)\n\n" +
																				"> Portuguese (BR):\n" +
																				"DeluxeMark™ (@xda), RhaySF\n\n" +
																				"> Russia:\n" +
																				"Smirnov Yaroslav (Smirnaff @xda)\n\n" +
																				"> Dutch:\n" +
																				"mike2nl (@xda)\n\n" +
																				"> Romanian:\n" +
																				"azZA_09 (@xda)\n\n" +
																				"Special Thanks:\n" +
																				" You for using my Module.\n" +
																				" Naman Dwivedi (naman14) for the original source.\n" + 
																				" rovo89 and Tungstwenty for Xposed.\n" +
																				" Igor Da Silva for the concept.",
																				"Your Device Id:\n" + ((MainActivity.deviceUniqeId.isEmpty() || MainActivity.deviceUniqeId.equalsIgnoreCase("none")) ? "Not generated. (this is not normal...)" : MainActivity.deviceUniqeId) + "\nYour Account Id:\n" + ((MainActivity.accountUniqeId.isEmpty() || MainActivity.accountUniqeId.equalsIgnoreCase("none")) ? "Not logged in." : MainActivity.accountUniqeId) + "\nThe Id's are used by the Preset Sever to verify your identity.",
																				"This Project uses some public librarys, all (maybe i have forgot some...) used librarys are listed below.",
																				"Copyright 2014 Eduard Ereza Martinez.\nLicensed under the Apache License, Version 2.0",
																				"Licensed under the Apache License, Version 2.0",
																				"Copyright Oraclejapan\nLicensed under the Apache License, Version 2.0",
																				"Copyright Lars Werkman.\nAn Android Holo themed colorpicker designed by Marie Schweiz.\nLicensed under the Apache License, Version 2.0",
																				"Copyright Bauerca\nDragSortListView is an extension of the Android ListView that enables drag-and-drop reordering of list items.\nLicensed under the Apache License, Version 2.0",
																				"Copyright 2012-2015 Jorrit Chainfire Jongma.\nLicensed under the Apache License, Version 2.0"};
				ArrayList<String> textsList = new ArrayList<String>(Arrays.asList(texts));
				
				aboutAdapter aa = new aboutAdapter(getActivity(),titlesList,textsList);
				
				list.setFastScrollEnabled(true);
				list.setAdapter(aa);
				
				return InflatedView;
		}
		
}
