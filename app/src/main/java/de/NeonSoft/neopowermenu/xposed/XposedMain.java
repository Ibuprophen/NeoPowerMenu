package de.NeonSoft.neopowermenu.xposed;

import android.content.*;
import android.os.*;
import de.NeonSoft.neopowermenu.*;
import de.NeonSoft.neopowermenu.helpers.*;
import de.robv.android.xposed.*;
import de.robv.android.xposed.callbacks.XC_LoadPackage.*;

/**
 * Created by naman on 20/03/15.
 */
public class XposedMain implements IXposedHookLoadPackage, IXposedHookZygoteInit
{

		private SharedPreferences preferences;
		
    public static final String PACKAGE_NAME = MainActivity.class.getPackage().getName();

    public static final String CLASS_GLOBAL_ACTIONS = "com.android.internal.policy.impl.GlobalActions";
		public static final String CLASS_GLOBAL_ACTIONS_MARSHMALLOW = "com.android.server.policy.GlobalActions";
    public static final String CLASS_GLOBAL_POWER_ACTIONS = "com.android.internal.policy.impl.GlobalActions.Actions";
    private static final String CLASS_PHONE_WINDOW_MANAGER = "com.android.internal.policy.impl.PhoneWindowManager";
    private static final String CLASS_PHONE_WINDOW_MANAGER_MARSHMALLOW = "com.android.server.policy.PhoneWindowManager";

		Handler xHandler;
    private static final Object mScreenshotLock = new Object();
    private static ServiceConnection mScreenshotConnection = null;  
		
    static Context mContext;
    Context context;
		public Object mObjectHolder;

		String versionName = "1.0";
		int versionCode = 0;
		

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable
		{
				log("Zygote init...");
				log("~~{ Module Infos }~~");
				log("Module Path: " + startupParam.modulePath);
				log("~~{ Device Infos }~~");
        log("Hardware: " + Build.HARDWARE);
        log("Product: " + Build.PRODUCT);
				log("Manufacturer: " + Build.MANUFACTURER);
				log("Model: " + Build.MODEL);
				log("Android Version: " + Build.VERSION.RELEASE);
				log("SDK Version: " + Build.VERSION.SDK_INT);
        log("ROM: " + Build.DISPLAY);
				log("Zygote init complete.");

    }


    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable
		{
				if (lpparam.packageName.equals("android"))
				{
						log("Loading Power Menu...");
						
						String usedGADClass;
						String usedPWMClass;
						if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
						{
								usedGADClass = CLASS_GLOBAL_ACTIONS_MARSHMALLOW;
								usedPWMClass = CLASS_PHONE_WINDOW_MANAGER_MARSHMALLOW;
						}
						else
						{
								usedGADClass = CLASS_GLOBAL_ACTIONS;
								usedPWMClass = CLASS_PHONE_WINDOW_MANAGER;
						}

						log("Detected "+android.os.Build.VERSION.RELEASE+", injecting to "+usedGADClass);
						final Class<?> phoneWindowManagerClass = XposedHelpers.findClass(usedPWMClass, lpparam.classLoader);
						final Class<?> globalActionsClass = XposedHelpers.findClass(usedGADClass, lpparam.classLoader);

						log("Hooking (replace) Constructors...");
						XposedBridge.hookAllConstructors(globalActionsClass, new XC_MethodReplacement() {
										@Override
										protected Object replaceHookedMethod(final MethodHookParam param) throws Throwable
										{
												mObjectHolder = param.thisObject;
												mContext = (Context) param.args[0];
												Handler mHandler = new Handler(); 
												xHandler = mHandler; 
												BroadcastReceiver mNPMReceiver = new BroadcastReceiver() {

														@Override
														public void onReceive(Context p1, Intent p2)
														{
																// TODO: Implement this method
																if (p2.getAction().equalsIgnoreCase("de.NeonSoft.neopowermenu.action.takeScreenshot")) {
																		final Handler handler = xHandler;
																		if (handler == null) return;

																		synchronized (mScreenshotLock) {  
																				if (mScreenshotConnection != null) {  
																						return;  
																				}  
																				ComponentName cn = new ComponentName("com.android.systemui",  
																																						 "com.android.systemui.screenshot.TakeScreenshotService");  
																				Intent intent = new Intent();  
																				intent.setComponent(cn);  
																				ServiceConnection conn = new ServiceConnection() {  
																						@Override  
																						public void onServiceConnected(ComponentName name, IBinder service) {  
																								synchronized (mScreenshotLock) {  
																										if (mScreenshotConnection != this) {  
																												return;  
																										}  
																										final Messenger messenger = new Messenger(service);  
																										final Message msg = Message.obtain(null, 1);  
																										final ServiceConnection myConn = this;  

																										Handler h = new Handler(handler.getLooper()) {  
																												@Override  
																												public void handleMessage(Message msg) {  
																														synchronized (mScreenshotLock) {  
																																if (mScreenshotConnection == myConn) {  
																																		mContext.unbindService(mScreenshotConnection);  
																																		mScreenshotConnection = null;  
																																		handler.removeCallbacks(mScreenshotTimeout);  
																																}  
																														}  
																												}  
																										};  
																										msg.replyTo = new Messenger(h);  
																										msg.arg1 = msg.arg2 = 0;  
																										h.post(new Runnable() {
																														@Override
																														public void run() {
																																try {
																																		messenger.send(msg);
																																} catch (RemoteException e) {
																																		XposedBridge.log(e);
																																}
																														}
																												});
																								}  
																						}  
																						@Override  
																						public void onServiceDisconnected(ComponentName name) {}  
																				};  
																				if (mContext.bindService(intent, conn, Context.BIND_AUTO_CREATE)) {  
																						mScreenshotConnection = conn;  
																						handler.postDelayed(mScreenshotTimeout, 10000);  
																				}  
																		} 
																} else if (p2.getAction().equalsIgnoreCase("de.NeonSoft.neopowermenu.action.Reboot")) {
																		PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
																		pm.reboot(null);
																} else if (p2.getAction().equalsIgnoreCase("de.NeonSoft.neopowermenu.action.RebootRecovery")) {
																		PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
																		pm.reboot("recovery");
																} else if (p2.getAction().equalsIgnoreCase("de.NeonSoft.neopowermenu.action.RebootBootloader")) {
																		PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
																		pm.reboot("bootloader");
																}
														}
												};
												IntentFilter filter = new IntentFilter();
												filter.addAction("de.NeonSoft.neopowermenu.action.takeScreenshot");
												filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
												mContext.registerReceiver(mNPMReceiver,filter,null,null);
												return null;
										}
								});
						log("Registering Broadcast Receiver and setting other values...");
						log("Hooking (replace) " + usedGADClass + "#showDialog...");
						XposedHelpers.findAndHookMethod(usedGADClass, lpparam.classLoader, "showDialog", boolean.class, boolean.class, new XC_MethodReplacement() {
										@Override
										protected Object replaceHookedMethod(final MethodHookParam methodHookParam) throws Throwable
										{
												showDialog();
												return null;
										}

								});
						log("Replaced with showDialog(), just executing startActivity() to start my own dialog.");
						log("Hooking (replace) " + usedGADClass + "#createDialog...");
						XposedHelpers.findAndHookMethod(usedGADClass, lpparam.classLoader, "createDialog", new XC_MethodReplacement() {
										@Override
										protected Object replaceHookedMethod(final MethodHookParam methodHookParam) throws Throwable
										{

												return null;
										}

								});
						log("Replaced with empty method to prevent crashes, hopefully working...");
						log("Hooking (replace) " + usedGADClass + "#onAirplaneModeChanged...");
						XposedHelpers.findAndHookMethod(usedGADClass, lpparam.classLoader, "onAirplaneModeChanged", new XC_MethodReplacement() {

										@Override
										protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable
										{
												// TODO: Implement this method
												return null;
										}
								});
						log("Replaced with empty method to prevent crashes, hopefully working...");
				}
				else if (lpparam.packageName.equals("de.NeonSoft.neopowermenu"))
				{
						log("Creating self inject...");
						XposedHelpers.findAndHookMethod("de.NeonSoft.neopowermenu.helpers.helper", lpparam.classLoader, "ModuleState", new XC_MethodReplacement() {
										@Override
										protected Object replaceHookedMethod(final MethodHookParam param) throws Throwable
										{
												String active = "active";
												return active;
										}
								});
						log("Self inject done!");
				}
				return;
    }

    private void showDialog()
		{
        if (mContext == null)
				{

            return;
        }

        try
				{
						//boolean mKeyguardShowing = XposedHelpers.getObjectField(mObjectHolder, "mKeyguardShowing");
            context = mContext.createPackageContext(PACKAGE_NAME, Context.CONTEXT_IGNORE_SECURITY);
            Intent intent = new Intent(context, XposedMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
						//intent.putExtra("mKeyguardShowing", mKeyguardShowing);
            context.startActivity(intent);
        }
				catch (Exception e)
				{
            log("Failed to show Power Menu (" + PACKAGE_NAME + "): " + e);
        }
    }


		public static void log(String message)
		{
				XposedBridge.log("[NeoPowerMenu] " + message);
		}


    private static final Runnable mScreenshotTimeout = new Runnable() {
        @Override
        public void run() {
            synchronized (mScreenshotLock) {
                if (mScreenshotConnection != null) {
                    mContext.unbindService(mScreenshotConnection);
                    mScreenshotConnection = null;
                }
            }
        }
    };
}

