# Selene

Selene is an Android application. It provides its users with the tools to track their menstrual cycle and aims
to promote body literacy.

## Installation

It is currently necessary to run the application from Android Studio, either using an emulator or an Android device.

Android Studio can be downloaded from this link: (https://developer.android.com/studio)
Guidelines for using an emulator can be found here: (https://developer.android.com/studio/run/emulator)

If you are using an Android device, you can connect your device using a USB cable.

## How to Start the Application
Once you have prepared the emulator or your device, the application can be run by opening menu option `Run --> Run 'app'`

## How to Navigate through the Application

The first screen the user is presented with is the home page. On first use, this screen will be blank apart from a toolbar (containing a
non-functional search button and a calendar button) and a pink floating action button with a plus symbol. Use the pink floating action
button in the bottom right corner to navigate to the Add New screen.

Once on the Add New screen, you will have the opportunity to input data:

* Select a date using the calendar button. A date picker dialog will appear. Select a date and click 'OK'. Your selected date will
appear to the right of the calendar button and in the toolbar
* Utilise the checkbox if you want to log bleeding, leave it blank if you do not
* Press the spinner that says 'Happy' - a dialog will appear with a list of options. Select an option.
* Press the spinner that says 'Headache' - a dialog will appear with a list of options. Select an option.
* If you wish, you can add a note. Press the EditText (where it says 'Add Note...' and type. When you are finished, press the tick.
    If you would like to edit again, simply press on your text and make the necessary changes
* When you are finished, click the pink floating action button with the floppy disk symbol. A toast will appear saying 'Saved!'
   and you will be taken back to the home page. You will be able to see an item relating to what you've input. It will appear
   as a green box with the date relating to it.
* You can click on the item to review your input. You will be taken to a screen which shows that data.
* You can swipe the item to the left or right to delete it. If you do, a bar at the bottom of the screen will appear.
    It will give you the option to undo your action. If you select 'Undo' you will see your item reappear.
* If you click on the calendar button on the home screen, you will be taken to the calendar screen. You can scroll up through
    previous months in this calendar. You will see green circles around dates that have been logged as bleeding and a grey
    circle outline around today's date

## How to close the Application

Close the application by clicking the 'Stop' button in Android Studio/closing the emulator/navigated 'Home' on your device

## List of Errors that may occur

There is currently an error displaying bleeding dates on the calendar. If consecutive days are logged, the first day
disappears. This should not affect any interaction with the application
