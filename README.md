#IVEN FEED READER
================


![ScreenShot](https://raw.githubusercontent.com/enricocid/lffl-feed-reader/master-as/art/header.png)

Iven news Reader is a light and modern Android feed reader that will allow You to read feeds in RSS format (the default blog is XDA).

The app supports 2.3+ devices (minSdkVersion=9) and, thanks to support libs, provides Material theme/elements (Toolbar, material preferences) across all Android versions supported.


![ScreenShot](https://raw.githubusercontent.com/enricocid/lffl-feed-reader/master-as/art/showcase.png)


You can download the latest (7.3.3) signed APK from this repo here: https://github.com/enricocid/lffl-feed-reader/raw/master-as/project/app/app-release.apk

The app it's also on Google Play Store:

<a href="https://play.google.com/store/apps/details?id=com.iven.lfflfeedreader">
  <img alt="Get it on Google Play"
       src="https://developer.android.com/images/brand/en_generic_rgb_wo_60.png" />
</a>


Some noticeable features are the light/dark/darker themes, dark status bar icons (>=Marshmallow), immersive mode (>=KitKat), navbar tint (>=KitKat), in-app WebView, option to disable images, text size options, swipe to refresh layout, responsive toolbar design and a Googlish splash screen/action buttons.

You can also add **CUSTOM FEEDS** by opening the navigation drawer. There You can click the add button from the Toolbar, compile the dialog text fields, et voila! The feeds will be added to a sqlite database and the listview will be updated dynamically. 
You can also delete them if You want by long clicking on them, the sqlite database and the listview will be updated  



--------------------
NOTES (Please Read):

1. NOTE: It is highly recommended to use the feedburner service to burn feeds, and enable "Convert format burner" service inside Optimize tab and select "Convert feed format to: RSS 2.0" to ensure the best compatibility.

2. We are not pro developers, but we hope You'll appreciate our work!


This work is 100% Free and is released under gpl license.



-------------
**THANKS TO**

**- Isaac Whitfield**
@zackehh

RSSfeed sample:
https://github.com/zackehh/RSSDemo

**- Lucas Urbas**
@lurbas

Making android toolbar responsive: 
https://medium.com/@lucasurbas/making-android-toolbar-responsive-2627d4e07129

**- Chris Stewart**
@cstew

An example of a splash screen done the right way: 
https://github.com/cstew/Splash

**- Ha Duy Trung**
@hidroh

Support multiple themes in your Android app:
http://www.hidroh.com/2015/02/16/support-multiple-themes-android-app/

**- Stack Overflow**


-------------------
**USED LIBRARIES**
**- Android Support Libraries**
- Android Support v4;
- Android Support Appcompatv7;
- Android Design Support library;
- Android v7 Preference;
- Android Support Percent;

**- material-dialogs core by Aidan Follestad**
@afollestad

Git page:
https://github.com/afollestad/material-dialogs

**- Jsoup by Jonathan Hedley**
@jhy

Homepage:
http://jsoup.org/

**- Glide by Bump Technologies**
@bumptech

Git page:
https://github.com/bumptech/glide


-------------------
This app follows the **KISS phylosophy** (https://en.wikipedia.org/wiki/KISS_principle), it means that the code/design will be kept simple & fast with no useless implementations/features.
Anyway, if You have a feature request please send us an email with the following informations (use Issues for bug reports only please):

- Description of the feature;
- Why You think it would be useful;
