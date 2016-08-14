package de.NeonSoft.neopowermenu.helpers;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import de.NeonSoft.neopowermenu.*;
import de.NeonSoft.neopowermenu.Preferences.*;
import java.util.*;

public class visibilityOrder_ListAdapter extends ArrayAdapter<String>
{

		private Activity mContext;
		private LayoutInflater inflater;
		private ArrayList<Integer> itemsType;
		private ArrayList<String> itemsTitle;

		public static int TYPE_NORMAL = 0;
		public static int TYPE_MULTI = 1;
		public static int TYPE_MULTIPAGE_START = 2;
		public static int TYPE_MULTIPAGE_END = 3;

		public visibilityOrder_ListAdapter(Activity context,
																			 ArrayList<Integer> itemsType,
																			 ArrayList<String> itemsTitle)
		{
				super(context, R.layout.visibilityorder_normal, itemsTitle);
				this.mContext = context;
				this.inflater = context.getLayoutInflater();
				this.itemsType = itemsType;
				this.itemsTitle = itemsTitle;
		}

		@Override
		public View getView(final int position, View p2, ViewGroup p3)
		{
				// TODO: Implement this method
				View InflatedView = null;

				if (itemsType.get(position) == TYPE_NORMAL)
				{
						InflatedView = inflater.inflate(R.layout.visibilityorder_normal, p3, false);

						TextView item = (TextView) InflatedView.findViewById(R.id.visibilityordernormal_item);
						String string = "Failed to get String resource for " + itemsTitle.get(position);
						try
						{
								string = mContext.getResources().getString(mContext.getResources().getIdentifier("powerMenuMain_" + itemsTitle.get(position), "string", MainActivity.class.getPackage().getName()));
						}
						catch (Throwable t)
						{
								try
								{
										string = mContext.getResources().getString(mContext.getResources().getIdentifier("powerMenuBottom_" + itemsTitle.get(position), "string", MainActivity.class.getPackage().getName()));
								}
								catch (Throwable t1)
								{
								}
						}
						item.setText(string);

						LinearLayout ItemCheckBoxHolder = (LinearLayout) InflatedView.findViewById(R.id.visibilityordernormal_LinearLayout_HideDesc);
						final CheckBox ItemCheckBox = (CheckBox) InflatedView.findViewById(R.id.visibilityordernormal_hideDescCheckBox);
						ItemCheckBox.setClickable(false);
						ItemCheckBox.setFocusable(false);
						try
						{
								if (!mContext.getResources().getString(mContext.getResources().getIdentifier("powerMenuMain_" + this.itemsTitle.get(position) + "Desc", "string", MainActivity.class.getPackage().getName())).equalsIgnoreCase(""))
								{
										ItemCheckBox.setChecked(MainActivity.preferences.getBoolean(this.itemsTitle.get(position) + "_HideDesc", false));
										ItemCheckBoxHolder.setVisibility(View.VISIBLE);
										ItemCheckBoxHolder.setOnClickListener(new OnClickListener() {

														@Override
														public void onClick(View p1)
														{
																ItemCheckBox.setChecked(!ItemCheckBox.isChecked());
																MainActivity.preferences.edit().putBoolean(itemsTitle.get(position) + "_HideDesc", ItemCheckBox.isChecked()).commit();
														}
												});
								}
						}
						catch (Throwable t)
						{
						}

						item.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View p1)
										{
												// TODO: Implement this method
												slideDownDialogFragment dialogFragment = new slideDownDialogFragment(mContext, MainActivity.fragmentManager);
												dialogFragment.setListener(new slideDownDialogFragment.slideDownDialogInterface() {

																@Override
																public void onListItemClick(int listpos, String text)
																{
																		// TODO: Implement this method
																		removeAt(position);
																		insertAt(position, new Object[] {TYPE_NORMAL,PreferencesVisibilityOrderFragment.PowerMenuItems[listpos]});
																}

																@Override
																public void onNegativeClick()
																{
																		// TODO: Implement this method
																}

																@Override
																public void onNeutralClick()
																{
																		// TODO: Implement this method
																}

																@Override
																public void onPositiveClick(Bundle resultBundle)
																{
																		// TODO: Implement this method
																}

																@Override
																public void onTouchOutside()
																{
																		// TODO: Implement this method
																}
														});
												int selItem = 0;
												for (int i = 0;i < PreferencesVisibilityOrderFragment.PowerMenuItems.length;i++)
												{
														if (PreferencesVisibilityOrderFragment.PowerMenuItems[i].equalsIgnoreCase(itemsTitle.get(position)))
														{
																selItem = i;
																break;
														}
												}
												dialogFragment.setList(ListView.CHOICE_MODE_SINGLE, PreferencesVisibilityOrderFragment.PowerMenuItemsTexts, selItem, true);
												dialogFragment.setPositiveButton(mContext.getString(R.string.Dialog_Buttons).split("\\|")[4]);
												dialogFragment.showDialog(R.id.dialog_container);
										}
								});

				}
				else if (itemsType.get(position) == TYPE_MULTI)
				{
						InflatedView = inflater.inflate(R.layout.visibilityorder_multi, p3, false);

						final String[] items = itemsTitle.get(position).split("\\|");
						
						TextView item1 = (TextView) InflatedView.findViewById(R.id.visibilityordermulti_item1);
						String string = "Failed to get String resource for " + items[0];
						try
						{
								string = mContext.getResources().getString(mContext.getResources().getIdentifier("powerMenuMain_" + items[0], "string", MainActivity.class.getPackage().getName()));
						}
						catch (Throwable t)
						{
								try
								{
										string = mContext.getResources().getString(mContext.getResources().getIdentifier("powerMenuBottom_" + items[0], "string", MainActivity.class.getPackage().getName()));
								}
								catch (Throwable t1)
								{
								}
						}
						item1.setText(string);

						TextView item2 = (TextView) InflatedView.findViewById(R.id.visibilityordermulti_item2);
						if (items.length >= 2)
						{
								string = "Failed to get String resource for " + items[1];
								try
								{
										string = mContext.getResources().getString(mContext.getResources().getIdentifier("powerMenuMain_" + items[1], "string", MainActivity.class.getPackage().getName()));
								}
								catch (Throwable t)
								{
										try
										{
												string = mContext.getResources().getString(mContext.getResources().getIdentifier("powerMenuBottom_" + items[1], "string", MainActivity.class.getPackage().getName()));
										}
										catch (Throwable t1)
										{
										}
								}
								item2.setText(string);
						}
						else
						{
								item2.setText(PreferencesVisibilityOrderFragment.PowerMenuItemsTexts[0]);
						}

						TextView item3 = (TextView) InflatedView.findViewById(R.id.visibilityordermulti_item3);
						if (items.length == 3)
						{
								string = "Failed to get String resource for " + items[2];
								try
								{
										string = mContext.getResources().getString(mContext.getResources().getIdentifier("powerMenuMain_" + items[2], "string", MainActivity.class.getPackage().getName()));
								}
								catch (Throwable t)
								{
										try
										{
												string = mContext.getResources().getString(mContext.getResources().getIdentifier("powerMenuBottom_" + items[2], "string", MainActivity.class.getPackage().getName()));
										}
										catch (Throwable t1)
										{
										}
								}
								item3.setText(string);
						}
						else
						{
								item3.setText(PreferencesVisibilityOrderFragment.PowerMenuItemsTexts[0]);
						}

						item1.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View p1)
										{
												// TODO: Implement this method
												slideDownDialogFragment dialogFragment = new slideDownDialogFragment(mContext, MainActivity.fragmentManager);
												dialogFragment.setListener(new slideDownDialogFragment.slideDownDialogInterface() {

																@Override
																public void onListItemClick(int listpos, String text)
																{
																		// TODO: Implement this method
																		removeAt(position);
																		insertAt(position, new Object[] {TYPE_MULTI,PreferencesVisibilityOrderFragment.PowerMenuItems[listpos] + "|" + (items.length >= 2 ? items[1] : "Empty") + "|" + (items.length == 3 ? items[2] : "Empty")});
																}

																@Override
																public void onNegativeClick()
																{
																		// TODO: Implement this method
																}

																@Override
																public void onNeutralClick()
																{
																		// TODO: Implement this method
																}

																@Override
																public void onPositiveClick(Bundle resultBundle)
																{
																		// TODO: Implement this method
																}

																@Override
																public void onTouchOutside()
																{
																		// TODO: Implement this method
																}
														});
												int selItem = 0;
												for (int i = 0;i < PreferencesVisibilityOrderFragment.PowerMenuItems.length;i++)
												{
														if (PreferencesVisibilityOrderFragment.PowerMenuItems[i].equalsIgnoreCase(items[0]))
														{
																selItem = i;
																break;
														}
												}
												dialogFragment.setList(ListView.CHOICE_MODE_SINGLE, PreferencesVisibilityOrderFragment.PowerMenuItemsTexts, selItem, true);
												dialogFragment.setPositiveButton(mContext.getString(R.string.Dialog_Buttons).split("\\|")[4]);
												dialogFragment.showDialog(R.id.dialog_container);
										}
								});
						item2.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View p1)
										{
												// TODO: Implement this method
												slideDownDialogFragment dialogFragment = new slideDownDialogFragment(mContext, MainActivity.fragmentManager);
												dialogFragment.setListener(new slideDownDialogFragment.slideDownDialogInterface() {

																@Override
																public void onListItemClick(int listpos, String text)
																{
																		// TODO: Implement this method
																		removeAt(position);
																		insertAt(position, new Object[] {TYPE_MULTI,items[0] + "|" + PreferencesVisibilityOrderFragment.PowerMenuItems[listpos] + "|" + (items.length == 3 ? items[2] : "Empty")});
																}

																@Override
																public void onNegativeClick()
																{
																		// TODO: Implement this method
																}

																@Override
																public void onNeutralClick()
																{
																		// TODO: Implement this method
																}

																@Override
																public void onPositiveClick(Bundle resultBundle)
																{
																		// TODO: Implement this method
																}

																@Override
																public void onTouchOutside()
																{
																		// TODO: Implement this method
																}
														});
												int selItem = 0;
												if (items.length >= 2)
												{
														for (int i = 0;i < PreferencesVisibilityOrderFragment.PowerMenuItems.length;i++)
														{
																if (PreferencesVisibilityOrderFragment.PowerMenuItems[i].equalsIgnoreCase(items[1]))
																{
																		selItem = i;
																		break;
																}
														}
												}
												dialogFragment.setList(ListView.CHOICE_MODE_SINGLE, PreferencesVisibilityOrderFragment.PowerMenuItemsTexts, selItem, true);
												dialogFragment.setPositiveButton(mContext.getString(R.string.Dialog_Buttons).split("\\|")[4]);
												dialogFragment.showDialog(R.id.dialog_container);
										}
								});
						item3.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View p1)
										{
												// TODO: Implement this method
												slideDownDialogFragment dialogFragment = new slideDownDialogFragment(mContext, MainActivity.fragmentManager);
												dialogFragment.setListener(new slideDownDialogFragment.slideDownDialogInterface() {

																@Override
																public void onListItemClick(int listpos, String text)
																{
																		// TODO: Implement this method
																		removeAt(position);
																		insertAt(position, new Object[] {TYPE_MULTI,items[0] + "|" + (items.length >= 2 ? items[1] : "Empty") + "|" + PreferencesVisibilityOrderFragment.PowerMenuItems[listpos]});
																}

																@Override
																public void onNegativeClick()
																{
																		// TODO: Implement this method
																}

																@Override
																public void onNeutralClick()
																{
																		// TODO: Implement this method
																}

																@Override
																public void onPositiveClick(Bundle resultBundle)
																{
																		// TODO: Implement this method
																}

																@Override
																public void onTouchOutside()
																{
																		// TODO: Implement this method
																}
														});
												int selItem = 0;
												if (items.length == 3)
												{
														for (int i = 0;i < PreferencesVisibilityOrderFragment.PowerMenuItems.length;i++)
														{
																if (PreferencesVisibilityOrderFragment.PowerMenuItems[i].equalsIgnoreCase(items[2]))
																{
																		selItem = i;
																		break;
																}
														}
												}
												dialogFragment.setList(ListView.CHOICE_MODE_SINGLE, PreferencesVisibilityOrderFragment.PowerMenuItemsTexts, selItem, true);
												dialogFragment.setPositiveButton(mContext.getString(R.string.Dialog_Buttons).split("\\|")[4]);
												dialogFragment.showDialog(R.id.dialog_container);
										}
								});
				} else if (itemsType.get(position)==TYPE_MULTIPAGE_START) {
						InflatedView = inflater.inflate(R.layout.visibilityorder_normal, p3, false);

						TextView item = (TextView) InflatedView.findViewById(R.id.visibilityordernormal_item);
						item.setClickable(false);
						item.setText(mContext.getString(R.string.visibilityOrder_MultiPage).split("\\|")[0]);
				} else if (itemsType.get(position)==TYPE_MULTIPAGE_END) {
						InflatedView = inflater.inflate(R.layout.visibilityorder_normal, p3, false);

						TextView item = (TextView) InflatedView.findViewById(R.id.visibilityordernormal_item);
						LinearLayout dragHandle = (LinearLayout) InflatedView.findViewById(R.id.drag_handle);
						//dragHandle.setVisibility(View.GONE);
						item.setClickable(false);
						item.setText(mContext.getString(R.string.visibilityOrder_MultiPage).split("\\|")[1]);
				}
				InflatedView.setClickable(false);

				return InflatedView;
		}

		public void addItem(int type, String item)
		{
				itemsType.add(type);
				itemsTitle.add(item);
				notifyDataSetChanged();
				//outputSorting();
		}

		public Object[] getItemAt(int position)
		{
				return new Object[] {itemsType.get(position),itemsTitle.get(position)};
		}

		public void insertAt(int position, Object[] item)
		{
				itemsType.add(position, Integer.parseInt(item[0].toString()));
				itemsTitle.add(position, item[1].toString());
				notifyDataSetChanged();
				//outputSorting();
		}

		public void move(int from, int to) {
				if (from != to) {
						Object[] item=getItemAt(from);
						if((int) item[0]==TYPE_MULTIPAGE_START) {
								//Toast.makeText(mContext, "start move from "+from+" to "+to+"\nChecking from "+to+" to "+(itemsType.size()-1), Toast.LENGTH_LONG).show();
								for(int i = to; i < itemsType.size(); i++) {
										Object[] checkItem = getItemAt(i);
										if((int) checkItem[0]==TYPE_MULTIPAGE_END && checkItem[1].toString().equals(item[1]) && i != to) {
												itemsType.remove(from);
												itemsTitle.remove(from);
												itemsType.add(to,(int) item[0]);
												itemsTitle.add(to, item[1].toString());
												notifyDataSetChanged();
												//Toast.makeText(mContext, "Stopped end search at "+i, Toast.LENGTH_LONG).show();
												break;
										} else if ((int) checkItem[0]==TYPE_MULTIPAGE_START && !checkItem[1].toString().equals(item[1])) {
												//Toast.makeText(mContext, "Stopped start search at "+i, Toast.LENGTH_LONG).show();
												//break;
										}
								}
						}
						else if ((int) item[0]==TYPE_MULTIPAGE_END) {
								//Toast.makeText(mContext, "end move from "+from+" to "+to+"\nChecking from "+to+" to 0", Toast.LENGTH_LONG).show();
								for(int i = to; i >= 0; i--) {
										Object[] checkItem = getItemAt(i);
										if((int) checkItem[0]==TYPE_MULTIPAGE_START && checkItem[1].toString().equals(item[1]) && i != to) {
												itemsType.remove(from);
												itemsTitle.remove(from);
												itemsType.add(to,(int) item[0]);
												itemsTitle.add(to, item[1].toString());
												notifyDataSetChanged();
												//Toast.makeText(mContext, "Stopped start search at "+i, Toast.LENGTH_LONG).show();
												break;
										} else if ((int) checkItem[0]==TYPE_MULTIPAGE_END && !checkItem[1].toString().equals(item[1])) {
												//Toast.makeText(mContext, "Stopped end search at "+i, Toast.LENGTH_LONG).show();
												//break;
										}
								}
						} else {
								//Toast.makeText(mContext, "normal move from "+from+" to "+to, Toast.LENGTH_SHORT).show();
								itemsType.remove(from);
								itemsTitle.remove(from);
								itemsType.add(to,(int) item[0]);
								itemsTitle.add(to, item[1].toString());
								notifyDataSetChanged();
						}
				}
		}
		
		public void removeAt(int position)
		{
				if((int) itemsType.get(position)==TYPE_MULTIPAGE_START) {
						Object[] item = getItemAt(position);
						while(itemsType.size() > position) {
								Object[] checkItem = getItemAt(position);
								if((int) checkItem[0] == TYPE_MULTIPAGE_END && checkItem[1].toString().equals(item[1])) {
										itemsType.remove(position);
										itemsTitle.remove(position);
										break;
								} else {
										itemsType.remove(position);
										itemsTitle.remove(position);
								}
						}
				} else if ((int) itemsType.get(position)==TYPE_MULTIPAGE_END) {
				} else if ((int) itemsType.get(position)==TYPE_NORMAL || (int) itemsType.get(position)==TYPE_MULTI) {
						itemsType.remove(position);
						itemsTitle.remove(position);
				}
				notifyDataSetChanged();
				//outputSorting();
		}

		public void outputSorting()
		{
				ArrayList<String> MultiPage = new ArrayList<String>();
				MainActivity.orderPrefs.edit().clear().commit();
				for (int i = 0;i < itemsTitle.size();i++)
				{
						MainActivity.orderPrefs.edit().putInt((MultiPage.size()>0 ? MultiPage.get(MultiPage.size() - 1)+"_" : "") + i + "_item_type", itemsType.get(i)).commit();
						if (itemsType.get(i) == TYPE_NORMAL)
						{
								MainActivity.orderPrefs.edit().putString((MultiPage.size()>0 ? MultiPage.get(MultiPage.size()-1)+"_" : "") + i + "_item_title", itemsTitle.get(i)).commit();
						}
						else if (itemsType.get(i) == TYPE_MULTI)
						{
								String [] split = itemsTitle.get(i).split("\\|");
								MainActivity.orderPrefs.edit().putString((MultiPage.size()>0 ? MultiPage.get(MultiPage.size()-1)+"_" : "") + i + "_item1_title", split[0]).commit();
								MainActivity.orderPrefs.edit().putString((MultiPage.size()>0 ? MultiPage.get(MultiPage.size()-1)+"_" : "") + i + "_item2_title", (split.length >= 2 ? split[1] : "Empty")).commit();
								MainActivity.orderPrefs.edit().putString((MultiPage.size()>0 ? MultiPage.get(MultiPage.size()-1)+"_" : "") + i + "_item3_title", (split.length == 3 ? split[2] : "Empty")).commit();
						} else if (itemsType.get(i) == TYPE_MULTIPAGE_START) {
								MainActivity.orderPrefs.edit().putInt((MultiPage.size()>0 ? MultiPage.get(MultiPage.size()-1)+"_" : "")+i + "_item_type", itemsType.get(i)).commit();
								MainActivity.orderPrefs.edit().putString((MultiPage.size()>0 ? MultiPage.get(MultiPage.size()-1)+"_" : "")+i + "_item_title", itemsTitle.get(i)).commit();
								MultiPage.add(itemsTitle.get(i));
						} else if (itemsType.get(i) == TYPE_MULTIPAGE_END) {
								MainActivity.orderPrefs.edit().putInt((MultiPage.size()>0 ? MultiPage.get(MultiPage.size()-1)+"_" : "")+i + "_item_type", itemsType.get(i)).commit();
								MainActivity.orderPrefs.edit().putString((MultiPage.size()>0 ? MultiPage.get(MultiPage.size()-1)+"_" : "")+i + "_item_title", itemsTitle.get(i)).commit();
								MultiPage.remove(MultiPage.size()-1);
						}
				}
		}

}
